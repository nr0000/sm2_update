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
        while (!IfTaskQueue.vodAlbumQueue.isEmpty() || !IfTaskQueue.vodCategoryIdQueue.isEmpty() || !IfTaskQueue.finishVodAlbum.get()) {
            try {
                update();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        logger.info("完成点播抓取");
    }

    private void update() {
        VodAlbum vodAlbum = IfTaskQueue.vodAlbumQueue.poll();
        if (vodAlbum == null) {
            try {
                Thread.sleep(5120);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        List<VodAudio> vodAudios = new ArrayList<>();
        IfVodAudiosPage ifVodAudio1stPage = getResourceFromIfService.getVodAudio(vodAlbum.getIdFromProvider(), 1);
        if (ifVodAudio1stPage == null || ifVodAudio1stPage.getData() == null) {
            logger.info(vodAlbum.getTitle() + " 下音频数量为0，该专辑将被自动删除");
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
