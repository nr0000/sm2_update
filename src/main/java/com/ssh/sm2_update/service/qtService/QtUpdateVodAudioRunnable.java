package com.ssh.sm2_update.service.qtService;

import com.sm2.bcl.content.entity.VodAlbum;
import com.sm2.bcl.content.entity.VodAudio;
import com.ssh.sm2_update.bean.qtBean.QtVodAudioData;
import com.ssh.sm2_update.bean.qtBean.QtVodAudioPage;
import com.ssh.sm2_update.service.VodAlbumService;
import com.ssh.sm2_update.service.VodAudioService;
import com.ssh.sm2_update.utils.QtAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class QtUpdateVodAudioRunnable implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(QtUpdateVodAudioRunnable.class);

    @Resource
    private VodAlbumService vodAlbumService;
    @Resource
    private GetResourceFromQtService getResourceFromQtServiceImpl;
    @Resource
    private VodAudioService vodAudioService;

    private boolean fastUpdate;

    @Override
    public void run() {
        QtTaskQueue.finishVodAudio = new AtomicBoolean(false);
        QtTaskQueue.threadFinishVodAudio.put(Thread.currentThread().getId(), false);
        while (!QtTaskQueue.vodAlbumQueue.isEmpty() || !QtTaskQueue.vodCategoryIdQueue.isEmpty() || !QtTaskQueue.finishVodAlbum.get()) {
            try {
                update();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        QtTaskQueue.threadFinishVodAudio.put(Thread.currentThread().getId(), true);
        Boolean finishVodAudio = true;
        for (Boolean o : QtTaskQueue.threadFinishVodAudio.values()) {
            if (!o) {
                finishVodAudio = o;
            }
        }
        if (finishVodAudio) {
            QtTaskQueue.finishVodAudio = new AtomicBoolean(true);
            logger.info("完成蜻蜓点播抓取任务");
        }
    }

    private void update() {
        VodAlbum vodAlbum = QtTaskQueue.vodAlbumQueue.poll();
        if (vodAlbum == null) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        List<VodAudio> vodAudios = new ArrayList<>();
        QtVodAudioPage qtVodAudio1stPage = getResourceFromQtServiceImpl.getVodAudio(vodAlbum.getIdFromProvider(), true, 1, 250);
        if (qtVodAudio1stPage == null || qtVodAudio1stPage.getData() == null) {
            logger.info(vodAlbum.getTitle() + " 下音频数量为0，该专辑将被自动删除");
            vodAlbumService.deleteVodAlbum(vodAlbum, fastUpdate);
            return;
        }
        int sortNum = 1;
        for (QtVodAudioData qtVodAudioData : qtVodAudio1stPage.getData()) {
            VodAudio vodAudio = QtAdapter.adapt(qtVodAudioData, vodAlbum, sortNum);
            sortNum++;
            if (vodAudio != null) {
                vodAudios.add(vodAudio);
            }
        }
        //判断是否只有一页
        if (qtVodAudio1stPage.getTotal() > qtVodAudio1stPage.getData().size()) {
            double totalPageD = (double) qtVodAudio1stPage.getTotal() / 250;
            int totalPage = (int) Math.ceil(totalPageD);
            for (int i = 2; i <= totalPage; i++) {
                QtVodAudioPage qtVodAudioPage = getResourceFromQtServiceImpl.getVodAudio(vodAlbum.getIdFromProvider(), true, i, 250);
                if (qtVodAudioPage == null || qtVodAudioPage.getData() == null) {
                    continue;
                }
                for (QtVodAudioData qtVodAudioData : qtVodAudioPage.getData()) {
                    VodAudio vodAudio = QtAdapter.adapt(qtVodAudioData, vodAlbum, sortNum);
                    sortNum++;
                    if (vodAudio != null) {
                        vodAudios.add(vodAudio);
                    }
                }
            }
        }
        if (vodAudios.size() == 0) {
            logger.info(vodAlbum.getTitle() + " 下音频数量为0，该专辑将被自动删除");
            vodAlbumService.deleteVodAlbum(vodAlbum, fastUpdate);
            return;
        }
        vodAudioService.addToQueue(vodAudios, fastUpdate);
    }

    public boolean isFastUpdate() {
        return fastUpdate;
    }

    public void setFastUpdate(boolean fastUpdate) {
        this.fastUpdate = fastUpdate;
    }

}
