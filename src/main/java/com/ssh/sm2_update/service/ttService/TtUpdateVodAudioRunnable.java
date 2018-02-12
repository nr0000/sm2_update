package com.ssh.sm2_update.service.ttService;

import com.sm2.bcl.content.entity.VodAlbum;
import com.sm2.bcl.content.entity.VodAudio;
import com.ssh.sm2_update.bean.klBean.KaolaVodAudio;
import com.ssh.sm2_update.bean.klBean.KaolaVodAudioPage;
import com.ssh.sm2_update.bean.ttBean.TtVodAudio;
import com.ssh.sm2_update.bean.ttBean.TtVodAudioPage;
import com.ssh.sm2_update.service.VodAlbumService;
import com.ssh.sm2_update.service.VodAudioService;
import com.ssh.sm2_update.utils.TtAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TtUpdateVodAudioRunnable implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(TtUpdateVodAudioRunnable.class);

    @Resource
    private VodAlbumService vodAlbumService;
    @Resource
    private GetResourceFromTtService getResourceFromTtService;
    @Resource
    private VodAudioService vodAudioService;

    private boolean fastUpdate;

    @Override
    public void run() {
        logger.info("-----------------------------------------------------听听点播曲目更新开始-----------------------------------------------------");
        TtTaskQueue.finishVodAudio = new AtomicBoolean(false);
        TtTaskQueue.threadFinishVodAudio.put(Thread.currentThread().getId(), false);

        while (!TtTaskQueue.vodAlbumQueue.isEmpty() || !TtTaskQueue.vodCategoryIdQueue.isEmpty() || !TtTaskQueue.finishVodAlbum.get()) {
            try {
                update();
            } catch (Exception e) {
                logger.error("", e);
            }
        }

        TtTaskQueue.threadFinishVodAudio.put(Thread.currentThread().getId(), true);

        Boolean finishVodAudio = true;
        for (Boolean o : TtTaskQueue.threadFinishVodAudio.values()) {
            if (!o) {
                finishVodAudio = o;
            }
        }
        if (finishVodAudio) {
            TtTaskQueue.finishVodAudio = new AtomicBoolean(true);
            logger.info("-----------------------------------------------------完成听听点播抓取任务-----------------------------------------------------");
        }

    }

    private void update() {
        VodAlbum vodAlbum = TtTaskQueue.vodAlbumQueue.poll();
        if (vodAlbum == null) {
            try {
                Thread.sleep(5103);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        List<VodAudio> vodAudios = new ArrayList<>();
        TtVodAudioPage ttVodAudio1stPage = getResourceFromTtService.getVodAudio(vodAlbum.getIdFromProvider(), 1);
        if (ttVodAudio1stPage == null || ttVodAudio1stPage.getData() == null) {
            vodAlbumService.deleteVodAlbum(vodAlbum, fastUpdate);
            return;
        }
        int sortNum = 1;
        for (TtVodAudio ttVodAudio : ttVodAudio1stPage.getData().getAlbumAudioList()) {
            VodAudio vodAudio = TtAdapter.adapt(ttVodAudio, vodAlbum, sortNum);
            sortNum++;
            if (vodAudio != null) {
                vodAudios.add(vodAudio);
            }
        }
        //判断是否只有一页
        if (ttVodAudio1stPage.getData().getTotalPage() > 1) {
            int totalPage = ttVodAudio1stPage.getData().getTotalPage();
            for (int i = 2; i <= totalPage; i++) {
                TtVodAudioPage ttVodAudioPage = getResourceFromTtService.getVodAudio(vodAlbum.getIdFromProvider(), i);
                if (ttVodAudioPage == null || ttVodAudioPage.getData() == null) {
                    continue;
                }
                for (TtVodAudio ttVodAudio : ttVodAudioPage.getData().getAlbumAudioList()) {
                    VodAudio vodAudio = TtAdapter.adapt(ttVodAudio, vodAlbum, sortNum);
                    sortNum++;
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
        vodAudioService.addToQueue(vodAudios, fastUpdate);
    }

    public boolean isFastUpdate() {
        return fastUpdate;
    }

    public void setFastUpdate(boolean fastUpdate) {
        this.fastUpdate = fastUpdate;
    }

}
