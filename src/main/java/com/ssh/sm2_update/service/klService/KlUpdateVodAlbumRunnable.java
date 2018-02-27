package com.ssh.sm2_update.service.klService;

import com.sm2.bcl.content.entity.VodAlbum;
import com.sm2.bcl.content.entity.VodCategory;
import com.sm2.bcl.content.entity.enums.ProviderType;
import com.ssh.sm2_update.bean.klBean.KaolaVodAlbum;
import com.ssh.sm2_update.bean.klBean.KaolaVodAlbumPage;
import com.ssh.sm2_update.service.VodAlbumService;
import com.ssh.sm2_update.utils.CategoryMappings;
import com.ssh.sm2_update.utils.KlAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class KlUpdateVodAlbumRunnable implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(KlUpdateVodAlbumRunnable.class);

    private boolean fastUpdate;
    @Resource
    private GetResourceFromKlService getResourceFromKlService;
    @Resource
    private VodAlbumService vodAlbumService;


    @Override
    public void run() {
        logger.info("-----------------------------------------------------考拉专辑更新开始-----------------------------------------------------");

        KlTaskQueue.threadFinishVodAlbum.put(Thread.currentThread().getId(), false);
        KlTaskQueue.finishVodAlbum = new AtomicBoolean(false);
        while (!KlTaskQueue.vodCategoryIdQueue.isEmpty()) {
            try {
                update();
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }
        KlTaskQueue.threadFinishVodAlbum.put(Thread.currentThread().getId(), true);

        Boolean finishVodAlbum = true;
        for (Boolean o : KlTaskQueue.threadFinishVodAlbum.values()) {
            if (!o) {
                finishVodAlbum = o;
            }
        }
        if (finishVodAlbum) {
            KlTaskQueue.finishVodAlbum = new AtomicBoolean(true);
            logger.info("-----------------------------------------------------完成考拉专辑抓取任务-----------------------------------------------------");
        }
    }


    public void update() {
        String vodCategoryId = KlTaskQueue.vodCategoryIdQueue.poll();
        if (vodCategoryId == null) {
            try {
                Thread.sleep(5110);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        VodCategory vodCategory = new VodCategory();
        vodCategory.setId(CategoryMappings.getKaolaVodMapping().get(vodCategoryId));

        KaolaVodAlbumPage klVodAlbum1stPage = getResourceFromKlService.getVodAlbum(vodCategoryId, 1,250);
        if (klVodAlbum1stPage == null || klVodAlbum1stPage.getResult() == null) {
            return;
        }
        List<VodAlbum> vodAlbumList = new ArrayList<>();
        for (KaolaVodAlbum kaolaVodAlbum : klVodAlbum1stPage.getResult().getDataList()) {
            VodAlbum vodAlbum = KlAdapter.adapt(kaolaVodAlbum, vodCategory,vodCategoryId);
            if (vodAlbum != null) {
                vodAlbumList.add(vodAlbum);
            }
        }
        //判断是否只有一页
        if (klVodAlbum1stPage.getResult().getHaveNext()) {
            int totalPage = klVodAlbum1stPage.getResult().getSumPage();
            for (int i = 2; i <= totalPage; i++) {
                KaolaVodAlbumPage klVodAlbumPage = getResourceFromKlService.getVodAlbum(vodCategoryId, i,250);
                if (klVodAlbumPage == null || klVodAlbumPage.getResult() == null) {
                    continue;
                }
                for (KaolaVodAlbum klVodAlbumData : klVodAlbumPage.getResult().getDataList()) {
                    VodAlbum vodAlbum = KlAdapter.adapt(klVodAlbumData, vodCategory,vodCategoryId);
                    if (vodAlbum != null) {
                        vodAlbumList.add(vodAlbum);
                    }
                }
            }
        }
        vodAlbumService.save(vodAlbumList, fastUpdate, ProviderType.KAOLA);
    }


    public boolean isFastUpdate() {
        return fastUpdate;
    }

    public void setFastUpdate(boolean fastUpdate) {
        this.fastUpdate = fastUpdate;
    }
}
