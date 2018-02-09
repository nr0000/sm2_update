package com.ssh.sm2_update.service.qtService;

import com.sm2.bcl.content.entity.*;
import com.ssh.sm2_update.bean.qtBean.QtLiveAudioData;
import com.ssh.sm2_update.bean.qtBean.QtLiveAudioPage;
import com.ssh.sm2_update.mapper.*;
import com.ssh.sm2_update.service.RedisService;
import com.ssh.sm2_update.utils.MD5Util;
import com.ssh.sm2_update.utils.MyCache;
import com.ssh.sm2_update.utils.QtAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class QtUpdateLiveAudioRunnable implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(QtUpdateLiveAudioRunnable.class);

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
    @Autowired
    private RedisService redisService;

    private boolean fastUpdate;
    @Resource
    private GetResourceFromQtService getResourceFromQtServiceImpl;


    @Override
    public void run() {
        QtTaskQueue.finishLiveAudio = new AtomicBoolean(false);
        QtTaskQueue.threadFinishLiveAudio.put(Thread.currentThread().getId(), false);

        while (!QtTaskQueue.liveCategoryIdQueue.isEmpty()) {
            try {
                update();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        QtTaskQueue.threadFinishLiveAudio.put(Thread.currentThread().getId(), true);
        Boolean finishLiveAudio = true;
        for (Boolean o : QtTaskQueue.threadFinishLiveAudio.values()) {
            if (!o) {
                finishLiveAudio = o;
            }
        }
        if (finishLiveAudio) {
            QtTaskQueue.finishLiveAudio = new AtomicBoolean(true);
            logger.info("完成蜻蜓电台抓取任务");
        }

    }

    public void update() {
        String liveCategoryId = QtTaskQueue.liveCategoryIdQueue.poll();
        if (liveCategoryId == null) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        QtLiveAudioPage qtLiveAudio1stPage = getResourceFromQtServiceImpl.getLiveAudio(true, 1, 250);
        List<LiveAudio> liveAudios = new ArrayList<>();
        if (qtLiveAudio1stPage == null || qtLiveAudio1stPage.getData() == null) {
            return;
        }
        for (QtLiveAudioData qtLiveAudioData : qtLiveAudio1stPage.getData()) {
            LiveAudio liveAudio = QtAdapter.adapt(qtLiveAudioData);
            if (liveAudio != null) {
                liveAudios.add(liveAudio);
            }
        }
        //判断是否只有一页
        if (qtLiveAudio1stPage.getTotal() > qtLiveAudio1stPage.getData().size()) {
            int totalPage = qtLiveAudio1stPage.getTotal() / 250 + 1;
            for (int i = 2; i <= totalPage; i++) {
                QtLiveAudioPage qtLiveAudioPage = getResourceFromQtServiceImpl.getLiveAudio(true, i, 250);
                if (qtLiveAudioPage == null || qtLiveAudioPage.getData() == null) {
                    continue;
                }
                for (QtLiveAudioData qtLiveAudioData : qtLiveAudioPage.getData()) {
                    LiveAudio liveAudio = QtAdapter.adapt(qtLiveAudioData);
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
                Long id = redisService.getLiveAudioId(md5Key);
                if (!MyCache.savedLiveAudio.contains(md5Key)) {
                    if (id == null) {
                        //从缓存map中取，如果没有对应的id就表明该曲目是新增的。
                        addedLiveAudio.add(liveAudio);
                        addedPlayableSources.addAll(liveAudio.getSources());
                    }
                    MyCache.savedLiveAudio.add(md5Key);
                }
            }
            if (addedLiveAudio.size() > 0) {
                logger.info("正在保存新增的" + addedLiveAudio.size() + "个电台");
                collectableMapper.batchInsertLiveAudioWithoutId(addedLiveAudio);
                liveAudioMapper.batchInsert(addedLiveAudio);
                playableSourceMapper.batchInsert(addedPlayableSources);
                QtTaskQueue.liveAudioQueue.addAll(addedLiveAudio);
                redisService.addLiveAudioToRedis(addedLiveAudio);
            }
        } else {
            for (LiveAudio liveAudio : liveAudios) {
                String md5Key = MD5Util.get32Md5(liveAudio.getCollectableType() + liveAudio.getIdFromProvider() + liveAudio.getProviderType());
                Long id = redisService.getLiveAudioId(md5Key);
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
                logger.info("正在保存新增的" + addedLiveAudio.size() + "个电台");

                collectableTempMapper.batchInsertLiveAudioWithoutId(addedLiveAudio);
                liveAudioTempMapper.batchInsert(addedLiveAudio);
                playableSourceTempMapper.batchInsert(addedPlayableSources);
                QtTaskQueue.liveAudioQueue.addAll(addedLiveAudio);
                redisService.addLiveAudioToRedis(addedLiveAudio);
            }
            if (existLiveAudio.size() > 0) {
                logger.info("正在保存更新的" + existLiveAudio.size() + "个电台");

                collectableTempMapper.batchInsertLiveAudioWithId(existLiveAudio);
                liveAudioTempMapper.batchInsert(existLiveAudio);
                playableSourceTempMapper.batchInsert(existPlayableSources);
                QtTaskQueue.liveAudioQueue.addAll(existLiveAudio);
            }
        }

    }

    private void sleep() {
        while (QtTaskQueue.liveAudioQueue.size() >= 1500) {
            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (QtTaskQueue.liveAudioQueue.size() >= 1800) {
            try {
                Thread.sleep(15 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
