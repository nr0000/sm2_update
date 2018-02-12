package com.ssh.sm2_update.service.ttService;

import com.sm2.bcl.content.entity.VodAlbum;
import com.sm2.bcl.content.entity.VodCategory;
import com.sm2.bcl.content.entity.enums.ProviderType;
import com.ssh.sm2_update.bean.ttBean.TtVodAlbum;
import com.ssh.sm2_update.bean.ttBean.TtVodAlbumPage;
import com.ssh.sm2_update.service.VodAlbumService;
import com.ssh.sm2_update.utils.CategoryMappings;
import com.ssh.sm2_update.utils.TtAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TtUpdateVodAlbumRunnable implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(TtUpdateVodAlbumRunnable.class);

    private boolean fastUpdate;
    @Resource
    private GetResourceFromTtService getResourceFromTtService;
    @Resource
    private VodAlbumService vodAlbumService;


    @Override
    public void run() {
        logger.info("-----------------------------------------------------听听专辑更新开始-----------------------------------------------------");
        TtTaskQueue.finishVodAlbum = new AtomicBoolean(false);
        TtTaskQueue.threadFinishVodAlbum.put(Thread.currentThread().getId(), false);
        while (!TtTaskQueue.vodCategoryIdQueue.isEmpty()) {
            try {
                update();
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }
        TtTaskQueue.threadFinishVodAlbum.put(Thread.currentThread().getId(), true);

        Boolean finishVodAlbum = true;
        for (Boolean o : TtTaskQueue.threadFinishVodAlbum.values()) {
            if (!o) {
                finishVodAlbum = o;
            }
        }
        if (finishVodAlbum) {
            TtTaskQueue.finishVodAlbum = new AtomicBoolean(true);
            logger.info("-----------------------------------------------------完成听听专辑抓取任务-----------------------------------------------------");
        }
    }


    public void update() {
        String vodCategoryId = TtTaskQueue.vodCategoryIdQueue.poll();
        if (vodCategoryId == null) {
            try {
                Thread.sleep(5111);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        VodCategory vodCategory = new VodCategory();
        vodCategory.setId(CategoryMappings.getTingtingVodMapping().get(vodCategoryId));

        TtVodAlbumPage ttVodAlbumPage = getResourceFromTtService.getVodAlbum(vodCategoryId);
        if (ttVodAlbumPage == null || ttVodAlbumPage.getData() == null) {
            return;
        }
        List<VodAlbum> vodAlbumList = new ArrayList<>();
        for (TtVodAlbum ttVodAlbum : ttVodAlbumPage.getData().getAlbumList()) {
            VodAlbum vodAlbum = TtAdapter.adapt(ttVodAlbum, vodCategory);
            if (vodAlbum != null) {
                vodAlbumList.add(vodAlbum);
            }
        }
        vodAlbumService.save(vodAlbumList, fastUpdate, ProviderType.TINGTING);
    }


    public boolean isFastUpdate() {
        return fastUpdate;
    }

    public void setFastUpdate(boolean fastUpdate) {
        this.fastUpdate = fastUpdate;
    }
}
