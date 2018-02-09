package com.ssh.sm2_update.service.klService;

import com.sm2.bcl.content.entity.VodAlbum;
import com.sm2.bcl.content.entity.VodAudio;
import com.ssh.sm2_update.bean.klBean.KaolaVodAudio;
import com.ssh.sm2_update.bean.klBean.KaolaVodAudioPage;
import com.ssh.sm2_update.service.VodAlbumService;
import com.ssh.sm2_update.service.VodAudioService;
import com.ssh.sm2_update.utils.KlAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class KlUpdateVodAudioRunnable implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(KlUpdateVodAudioRunnable.class);

    @Resource
    private VodAlbumService vodAlbumService;
    @Resource
    private GetResourceFromKlService getResourceFromKlService;
    @Resource
    private VodAudioService vodAudioService;

    private boolean fastUpdate;

    @Override
    public void run() {
        KlTaskQueue.threadFinishVodAudio.put(Thread.currentThread().getId(), false);
        KlTaskQueue.finishVodAudio = new AtomicBoolean(false);
        while (!KlTaskQueue.vodAlbumQueue.isEmpty() || !KlTaskQueue.vodCategoryIdQueue.isEmpty() || !KlTaskQueue.finishVodAlbum.get()) {
            try {
                update();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        KlTaskQueue.threadFinishVodAudio.put(Thread.currentThread().getId(), true);

        Boolean finishVodAudio = true;
        for (Boolean o : KlTaskQueue.threadFinishVodAudio.values()) {
            if (!o) {
                finishVodAudio = o;
            }
        }
        if (finishVodAudio) {
            KlTaskQueue.finishVodAudio = new AtomicBoolean(true);
            logger.info("完成考拉点播抓取任务");
        }
    }

    private void update() {
        VodAlbum vodAlbum = KlTaskQueue.vodAlbumQueue.poll();
        if (vodAlbum == null) {
            try {
                Thread.sleep(5100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        List<VodAudio> vodAudios = new ArrayList<>();
        KaolaVodAudioPage klVodAudio1stPage = getResourceFromKlService.getVodAudio(vodAlbum.getIdFromProvider(), 1,250);
        if (klVodAudio1stPage == null || klVodAudio1stPage.getResult() == null) {
            logger.info(vodAlbum.getTitle() + " 下音频数量为0，该专辑将被自动删除");
            vodAlbumService.deleteVodAlbum(vodAlbum, fastUpdate);
            return;
        }
        for (KaolaVodAudio kaolaVodAudio : klVodAudio1stPage.getResult().getDataList()) {
            VodAudio vodAudio = KlAdapter.adapt(kaolaVodAudio, vodAlbum);
            if (vodAudio != null) {
                vodAudios.add(vodAudio);
            }
        }
        //判断是否只有一页
        if (klVodAudio1stPage.getResult().getHaveNext()) {
            int totalPage = klVodAudio1stPage.getResult().getSumPage();
            for (int i = 2; i <= totalPage; i++) {
                KaolaVodAudioPage klVodAudioPage = getResourceFromKlService.getVodAudio(vodAlbum.getIdFromProvider(), i,250);
                if (klVodAudioPage == null || klVodAudioPage.getResult() == null) {
                    continue;
                }
                for (KaolaVodAudio kaolaVodAudio : klVodAudioPage.getResult().getDataList()) {
                    VodAudio vodAudio = KlAdapter.adapt(kaolaVodAudio, vodAlbum);
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
