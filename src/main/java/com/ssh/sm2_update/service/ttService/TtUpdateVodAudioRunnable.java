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
        while (!TtTaskQueue.vodAlbumQueue.isEmpty() || !TtTaskQueue.vodCategoryIdQueue.isEmpty() || !TtTaskQueue.finishVodAlbum.get()) {
            try {
                update();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        logger.info("完成听听点播曲目抓取");
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
            logger.info(vodAlbum.getTitle() + " 下音频数量为0，该专辑将被自动删除");
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
