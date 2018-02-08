package com.ssh.sm2_update.service.ifService;

import com.sm2.bcl.content.entity.VodAlbum;
import com.sm2.bcl.content.entity.VodCategory;
import com.sm2.bcl.content.entity.enums.ProviderType;
import com.ssh.sm2_update.bean.ifBean.IfVodAlbumsDataItem;
import com.ssh.sm2_update.bean.ifBean.IfVodAlbumsPage;
import com.ssh.sm2_update.service.VodAlbumService;
import com.ssh.sm2_update.utils.CategoryMappings;
import com.ssh.sm2_update.utils.IfAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class IfUpdateVodAlbumRunnable implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(IfUpdateVodAlbumRunnable.class);

    private boolean fastUpdate;
    @Resource
    private GetResourceFromIfService getResourceFromIfService;
    @Resource
    private VodAlbumService vodAlbumService;


    @Override
    public void run() {
        IfTaskQueue.threadFinishVodAlbum.put(Thread.currentThread().getId(), false);
        while (!IfTaskQueue.vodCategoryIdQueue.isEmpty()) {
            try {
                update();
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }
        IfTaskQueue.threadFinishVodAlbum.put(Thread.currentThread().getId(), true);

        Boolean finishVodAlbum = true;
        for (Boolean o : IfTaskQueue.threadFinishVodAlbum.values()) {
            if (!o) {
                finishVodAlbum = o;
            }
        }
        if (finishVodAlbum) {
            IfTaskQueue.finishVodAlbum = new AtomicBoolean(true);
        }
        logger.info("完成凤凰专辑抓取任务");
    }


    public void update() {
        String vodCategoryId = IfTaskQueue.vodCategoryIdQueue.poll();
        if (vodCategoryId == null) {
            try {
                Thread.sleep(5200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        VodCategory vodCategory = new VodCategory();
        vodCategory.setId(CategoryMappings.getIfengVodMapping().get(vodCategoryId));

        IfVodAlbumsPage ifVodAlbum1stPage = getResourceFromIfService.getVodAlbum(vodCategoryId, 1);
        if (ifVodAlbum1stPage == null || ifVodAlbum1stPage.getData() == null) {
            return;
        }
        List<VodAlbum> vodAlbumList = new ArrayList<>();
        for (IfVodAlbumsDataItem ifVodAlbumsDataItem : ifVodAlbum1stPage.getData().getList()) {
            VodAlbum vodAlbum = IfAdapter.adapt(ifVodAlbumsDataItem, vodCategory);
            if (vodAlbum != null) {
                vodAlbumList.add(vodAlbum);
            }
        }
        //判断是否只有一页
        if (ifVodAlbum1stPage.getData().getListCount() > ifVodAlbum1stPage.getData().getList().size()) {
            int totalPage = ifVodAlbum1stPage.getData().getListCount() / 20 + 1;
            for (int i = 2; i <= totalPage; i++) {
                IfVodAlbumsPage ifVodAlbumPage = getResourceFromIfService.getVodAlbum(vodCategoryId, i);
                if (ifVodAlbumPage == null || ifVodAlbumPage.getData() == null) {
                    continue;
                }
                for (IfVodAlbumsDataItem ifVodAlbumData : ifVodAlbumPage.getData().getList()) {
                    VodAlbum vodAlbum = IfAdapter.adapt(ifVodAlbumData, vodCategory);
                    if (vodAlbum != null) {
                        vodAlbumList.add(vodAlbum);
                    }
                }
            }
        }
        vodAlbumService.save(vodAlbumList, fastUpdate, ProviderType.IFENG);
    }


    public boolean isFastUpdate() {
        return fastUpdate;
    }

    public void setFastUpdate(boolean fastUpdate) {
        this.fastUpdate = fastUpdate;
    }
}
