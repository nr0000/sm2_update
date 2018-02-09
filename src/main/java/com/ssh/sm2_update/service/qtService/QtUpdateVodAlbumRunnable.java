package com.ssh.sm2_update.service.qtService;

import com.sm2.bcl.content.entity.VodAlbum;
import com.sm2.bcl.content.entity.VodCategory;
import com.sm2.bcl.content.entity.enums.ProviderType;
import com.ssh.sm2_update.bean.qtBean.QtVodAlbumData;
import com.ssh.sm2_update.bean.qtBean.QtVodAlbumDetailPage;
import com.ssh.sm2_update.bean.qtBean.QtVodAlbumPage;
import com.ssh.sm2_update.service.VodAlbumService;
import com.ssh.sm2_update.utils.CategoryMappings;
import com.ssh.sm2_update.utils.QtAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class QtUpdateVodAlbumRunnable implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(QtUpdateVodAlbumRunnable.class);

    @Resource
    private VodAlbumService vodAlbumService;

    private boolean fastUpdate;
    @Resource
    private GetResourceFromQtService getResourceFromQtServiceImpl;


    @Override
    public void run() {
        QtTaskQueue.finishVodAlbum = new AtomicBoolean(false);
        QtTaskQueue.threadFinishVodAlbum.put(Thread.currentThread().getId(), false);
        while (!QtTaskQueue.vodCategoryIdQueue.isEmpty()) {
            try {
                update();
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }
        QtTaskQueue.threadFinishVodAlbum.put(Thread.currentThread().getId(), true);
        Boolean finishVodAlbum = true;
        for (Boolean o : QtTaskQueue.threadFinishVodAlbum.values()) {
            if (!o) {
                finishVodAlbum = o;
            }
        }
        if (finishVodAlbum) {
            QtTaskQueue.finishVodAlbum = new AtomicBoolean(true);
            logger.info("完成蜻蜓专辑抓取任务");
        }

    }


    public void update() {
        String vodCategoryId = QtTaskQueue.vodCategoryIdQueue.poll();
        if (vodCategoryId == null) {
            try {
                Thread.sleep(5500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        VodCategory qtVodCategory = new VodCategory();
        qtVodCategory.setId(CategoryMappings.getQingtingVodMapping().get(vodCategoryId));

        QtVodAlbumPage qtVodAlbum1stPage = getResourceFromQtServiceImpl.getVodAlbum(vodCategoryId, true, 1, 250);
        if (qtVodAlbum1stPage == null || qtVodAlbum1stPage.getData() == null) {
            return;
        }
        List<VodAlbum> vodAlbumList = new ArrayList<>();
        adaptAddToList(qtVodAlbum1stPage, qtVodCategory, vodAlbumList);

        //判断是否只有一页
        if (qtVodAlbum1stPage.getTotal() > qtVodAlbum1stPage.getData().size()) {
            //每页有250条数据
            int totalPage = qtVodAlbum1stPage.getTotal() / 250 + 1;
            for (int i = 2; i <= totalPage; i++) {
                QtVodAlbumPage qtVodAlbumPage = getResourceFromQtServiceImpl.getVodAlbum(vodCategoryId, true, i, 250);
                if (qtVodAlbumPage == null || qtVodAlbumPage.getData() == null) {
                    continue;
                }
                adaptAddToList(qtVodAlbumPage, qtVodCategory, vodAlbumList);
            }
        }
        vodAlbumService.save(vodAlbumList, fastUpdate, ProviderType.QINGTING);
    }

    private void adaptAddToList(QtVodAlbumPage qtVodAlbumPage, VodCategory qtVodCategory, List<VodAlbum> vodAlbumList) {
        for (QtVodAlbumData qtVodAlbumData : qtVodAlbumPage.getData()) {
            VodAlbum vodAlbum = QtAdapter.adapt(qtVodAlbumData, qtVodCategory);
            if (vodAlbum != null) {
                //sale_type不为0，表明该专辑是付费内容
                if (qtVodAlbumData.getSale_type() != 0) {
                    QtVodAlbumDetailPage qtVodAlbumDetailPage = getResourceFromQtServiceImpl.getVodAlbumDetail(qtVodAlbumData.getId().toString());
                    if (qtVodAlbumDetailPage != null && qtVodAlbumDetailPage.getData() != null) {
                        QtAdapter.addSaleInfo(vodAlbum, qtVodAlbumDetailPage.getData());
                    }
                }
                vodAlbumList.add(vodAlbum);
            }
        }
    }

    public boolean isFastUpdate() {
        return fastUpdate;
    }

    public void setFastUpdate(boolean fastUpdate) {
        this.fastUpdate = fastUpdate;
    }
}
