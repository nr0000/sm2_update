package com.ssh.sm2_update.service.klService;

import com.sm2.bcl.content.entity.LiveAudio;
import com.sm2.bcl.content.entity.LiveCategory;
import com.sm2.bcl.content.entity.PlayableSource;
import com.ssh.sm2_update.bean.klBean.KaolaLiveAudio;
import com.ssh.sm2_update.bean.klBean.KaolaLiveAudioPage;
import com.ssh.sm2_update.mapper.*;
import com.ssh.sm2_update.utils.CategoryMappings;
import com.ssh.sm2_update.utils.MD5Util;
import com.ssh.sm2_update.utils.MyCache;
import com.ssh.sm2_update.utils.KlAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class KlUpdateLiveAudioRunnable implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(KlUpdateLiveAudioRunnable.class);

    @Resource
    private CollectableMapper collectableMapper;
    @Resource
    private LiveAudioMapper liveAudioMapper;
    @Resource
    private PlayableSourceMapper playableSourceMapper;
    @Resource
    private LiveAudioTempMapper liveAudioTempMapper;
    @Resource
    private CollectableTempMapper collectableTempMapper;
    @Resource
    private PlayableSourceTempMapper playableSourceTempMapper;

    private boolean fastUpdate;
    @Resource
    private GetResourceFromKlService getResourceFromKlService;


    @Override
    public void run() {
        while (!KlTaskQueue.liveCategoryIdQueue.isEmpty()) {
            try {
                update();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        logger.info("完成考拉电台抓取任务");
    }

    public void update() {
        String liveCategoryId = KlTaskQueue.liveCategoryIdQueue.poll();
        Long kaoLaLiveType = CategoryMappings.getKaolaTypeMapping().get(liveCategoryId);
        if (liveCategoryId == null) {
            try {
                Thread.sleep(4100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        LiveCategory liveCategory = new LiveCategory();
        liveCategory.setId(CategoryMappings.getKaolaLiveMapping().get(liveCategoryId));

        KaolaLiveAudioPage klLiveAudio1stPage = getResourceFromKlService.getLiveAudio(liveCategoryId, kaoLaLiveType, 1, 250);
        List<LiveAudio> liveAudios = new ArrayList<>();
        if (klLiveAudio1stPage == null || klLiveAudio1stPage.getResult() == null) {
            return;
        }
        for (KaolaLiveAudio kaolaLiveAudio : klLiveAudio1stPage.getResult().getDataList()) {
            LiveAudio liveAudio = KlAdapter.adapt(kaolaLiveAudio, liveCategory);
            if (liveAudio != null) {
                liveAudios.add(liveAudio);
            }
        }
        //判断是否只有一页
        if (klLiveAudio1stPage.getResult().getHaveNext()) {
            int totalPage = klLiveAudio1stPage.getResult().getSumPage();
            for (int i = 2; i <= totalPage; i++) {
                KaolaLiveAudioPage klLiveAudioPage = getResourceFromKlService.getLiveAudio(liveCategoryId, kaoLaLiveType, i,250);
                if (klLiveAudioPage == null || klLiveAudioPage.getResult() == null) {
                    continue;
                }
                for (KaolaLiveAudio kaolaLiveAudio : klLiveAudioPage.getResult().getDataList()) {
                    LiveAudio liveAudio = KlAdapter.adapt(kaolaLiveAudio,liveCategory);
                    if (liveAudio != null) {
                        liveAudios.add(liveAudio);
                    }
                }
            }
        }

//        collectableMapper.batchInsertVodAlbumWithoutId(vodAlbumList);
//        vodAlbumMapper.batchInsert(vodAlbumList);
        saveAndAddToQueue(liveAudios);
    }

    private void saveAndAddToQueue(List<LiveAudio> liveAudios) {
        List<PlayableSource> existPlayableSources = new ArrayList<>();
        List<LiveAudio> existLiveAudio = new ArrayList<>();
        //本次新增的曲目
        List<LiveAudio> addedLiveAudio = new ArrayList<>();
        List<PlayableSource> addedPlayableSources = new ArrayList<>();

        if (fastUpdate) {
            for (LiveAudio liveAudio : liveAudios) {
                String md5Key = MD5Util.get32Md5(liveAudio.getCollectableType() + liveAudio.getIdFromProvider() + liveAudio.getProviderType());
                Long id = MyCache.liveAudioMap.get(md5Key);
                if (!MyCache.savedLiveAudio.contains(md5Key)) {
                    if (id == null) {
                        //从缓存map中取，如果没有对应的id就表明该曲目是新增的。
                        addedLiveAudio.add(liveAudio);
                        addedPlayableSources.addAll(liveAudio.getSources());
                    }
                    MyCache.savedLiveAudio.add(md5Key);
                }
            }
            logger.info("正在保存" + addedLiveAudio.size() + "个电台");
            if (addedLiveAudio.size() > 0) {
                collectableMapper.batchInsertLiveAudioWithoutId(addedLiveAudio);
                liveAudioMapper.batchInsert(addedLiveAudio);
                playableSourceMapper.batchInsert(addedPlayableSources);
            }
        } else {
            for (LiveAudio liveAudio : liveAudios) {
                String md5Key = MD5Util.get32Md5(liveAudio.getCollectableType() + liveAudio.getIdFromProvider() + liveAudio.getProviderType());
                Long id = MyCache.liveAudioMap.get(md5Key);
                if (!MyCache.savedLiveAudio.contains(md5Key)) {
                    if (id == null) {
                        //从缓存map中取，如果没有对应的id就表明该曲目是新增的。
                        addedLiveAudio.add(liveAudio);
                        addedPlayableSources.addAll(liveAudio.getSources());
                    } else {
                        liveAudio.setId(id);
                        existLiveAudio.add(liveAudio);
                        existPlayableSources.addAll(liveAudio.getSources());
                    }
                    MyCache.savedLiveAudio.add(md5Key);
                }
            }
            if (addedLiveAudio.size() > 0) {
                collectableTempMapper.batchInsertLiveAudioWithoutId(addedLiveAudio);
                liveAudioTempMapper.batchInsert(addedLiveAudio);
                playableSourceTempMapper.batchInsert(addedPlayableSources);
            }
            if (existLiveAudio.size() > 0) {
                collectableTempMapper.batchInsertLiveAudioWithId(existLiveAudio);
                liveAudioTempMapper.batchInsert(existLiveAudio);
                playableSourceTempMapper.batchInsert(existPlayableSources);
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
