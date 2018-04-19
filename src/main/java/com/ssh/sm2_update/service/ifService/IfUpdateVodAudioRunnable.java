package com.ssh.sm2_update.service.ifService;

import com.sm2.bcl.content.entity.VodAlbum;
import com.sm2.bcl.content.entity.VodAudio;
import com.ssh.sm2_update.bean.ifBean.IfVodAudiosDataItem;
import com.ssh.sm2_update.bean.ifBean.IfVodAudiosPage;
import com.ssh.sm2_update.service.VodAlbumService;
import com.ssh.sm2_update.service.VodAudioService;
import com.ssh.sm2_update.utils.IfAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class IfUpdateVodAudioRunnable implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(IfUpdateVodAudioRunnable.class);

    @Resource
    private VodAlbumService vodAlbumService;
    @Resource
    private GetResourceFromIfService getResourceFromIfService;
    @Resource
    private VodAudioService vodAudioService;

    private boolean fastUpdate;

    @Override
    public void run() {
        logger.info("-----------------------------------------------------凤凰点播曲目更新开始-----------------------------------------------------");

        IfTaskQueue.finishVodAudio = new AtomicBoolean(false);
        IfTaskQueue.threadFinishVodAudio.put(Thread.currentThread().getId(), false);
        while (!IfTaskQueue.vodAlbumQueue.isEmpty() || !IfTaskQueue.vodCategoryIdQueue.isEmpty() || !IfTaskQueue.finishVodAlbum.get()) {
            try {
                update();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        IfTaskQueue.threadFinishVodAudio.put(Thread.currentThread().getId(), true);
        Boolean finishVodAudio = true;
        for (Boolean o : IfTaskQueue.threadFinishVodAudio.values()) {
            if (!o) {
                finishVodAudio = o;
            }
        }
        if (finishVodAudio) {
            IfTaskQueue.finishVodAudio = new AtomicBoolean(true);
            logger.info("-----------------------------------------------------完成凤凰点播抓取任务-----------------------------------------------------");
        }
    }

    private void update() {
        VodAlbum vodAlbum = IfTaskQueue.vodAlbumQueue.poll();
        if (vodAlbum == null) {
            logger.info("凤凰专辑为空");
            try {
                Thread.sleep(5120);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        List<VodAudio> vodAudios = new ArrayList<>();
        logger.info("获取" + vodAlbum.getTitle() + " 下的内容的第一页");
        IfVodAudiosPage ifVodAudio1stPage = getResourceFromIfService.getVodAudio(vodAlbum.getIdFromProvider(), 1);
        if (ifVodAudio1stPage == null || ifVodAudio1stPage.getData() == null) {
            vodAlbumService.deleteVodAlbum(vodAlbum, fastUpdate);
            return;
        }
        for (IfVodAudiosDataItem ifVodAudioData : ifVodAudio1stPage.getData().getList()) {
            VodAudio vodAudio = IfAdapter.adapt(ifVodAudioData, vodAlbum);
            if (vodAudio != null) {
                vodAudios.add(vodAudio);
            }
        }
        //判断是否只有一页
        if (ifVodAudio1stPage.getData().getCount() > ifVodAudio1stPage.getData().getList().size()) {
            double totalPageD = (double) ifVodAudio1stPage.getData().getCount() / 20;
            int totalPage = (int) Math.ceil(totalPageD);
            logger.info("获取" + vodAlbum.getTitle() + " 下的内容，总共" + totalPage + "页");
            for (int i = 2; i <= totalPage; i++) {
                IfVodAudiosPage ifVodAudioPage = getResourceFromIfService.getVodAudio(vodAlbum.getIdFromProvider(), i);
                if (ifVodAudioPage == null || ifVodAudioPage.getData() == null) {
                    continue;
                }
                for (IfVodAudiosDataItem ifVodAudioData : ifVodAudioPage.getData().getList()) {
                    VodAudio vodAudio = IfAdapter.adapt(ifVodAudioData, vodAlbum);
                    if (vodAudio != null) {
                        vodAudios.add(vodAudio);
                    }
                }
            }
        }
        if (vodAudios.size() == 0) {
            vodAlbumService.deleteVodAlbum(vodAlbum, fastUpdate);
            return;
        }
        logger.info("将"+vodAlbum.getTitle()+"下的"+vodAudios.size()+"个点播音频放入队列");
        vodAudioService.addToQueue(vodAudios, fastUpdate);
    }

    public boolean isFastUpdate() {
        return fastUpdate;
    }

    public void setFastUpdate(boolean fastUpdate) {
        this.fastUpdate = fastUpdate;
    }

}
