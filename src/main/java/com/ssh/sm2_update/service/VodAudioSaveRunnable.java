package com.ssh.sm2_update.service;

import com.sm2.bcl.content.entity.ChargingInfo;
import com.sm2.bcl.content.entity.CollectableLabel;
import com.sm2.bcl.content.entity.PlayableSource;
import com.sm2.bcl.content.entity.VodAudio;
import com.ssh.sm2_update.mapper.*;
import com.ssh.sm2_update.utils.MyCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class VodAudioSaveRunnable implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(VodAudioSaveRunnable.class);

    @Resource
    private CollectableMapper collectableMapper;
    @Resource
    private VodAlbumMapper vodAlbumMapper;
    @Resource
    private VodAudioMapper vodAudioMapper;
    @Resource
    private PlayableSourceMapper playableSourceMapper;
    @Resource
    private CollectableTempMapper collectableTempMapper;
    @Resource
    private VodAlbumTempMapper vodAlbumTempMapper;
    @Resource
    private VodAudioTempMapper vodAudioTempMapper;
    @Resource
    private PlayableSourceTempMapper playableSourceTempMapper;
    @Autowired
    private ChargingInfoMapper chargingInfoMapper;
    @Autowired
    private ChargingInfoTempMapper chargingInfoTempMapper;
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private LabelTempMapper labelTempMapper;

    private boolean fastUpdate;

    @Override
    public void run() {
        List<VodAudio> addedVodAudioList = new ArrayList<>();
        List<VodAudio> existVodAudioList = new ArrayList<>();
        int bufferNum = 1999;
        int tryNum = 0;
        while (true) {
            try {
                if (MyCache.addedVodAudioQueue.isEmpty() && MyCache.existVodAudioQueue.isEmpty()) {
                    try {
                        Thread.sleep(5000);
                        tryNum++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    tryNum = 0;
                }
                if (!MyCache.addedVodAudioQueue.isEmpty()) {
                    VodAudio addedVod = MyCache.addedVodAudioQueue.poll();
                    if (addedVod != null) {
                        addedVodAudioList.add(addedVod);
                    }
                }
                if (!MyCache.existVodAudioQueue.isEmpty()) {
                    VodAudio existVod = MyCache.existVodAudioQueue.poll();
                    if (existVod != null) {
                        existVodAudioList.add(existVod);
                    }
                }
                boolean abc = addedVodAudioList.size() > 0 && tryNum > 50;
                if (addedVodAudioList.size() > bufferNum || abc) {
                    List<PlayableSource> playableSources = new ArrayList<>();
                    List<ChargingInfo> chargingInfoList = new ArrayList<>();
                    List<CollectableLabel> collectableLabels = new ArrayList<>();
                    addedVodAudioList.forEach(vodAudio -> {
                        if (vodAudio.getSources() != null) {
                            playableSources.addAll(vodAudio.getSources());
                        }
                        if (vodAudio.getChargingInfo() != null) {
                            chargingInfoList.add(vodAudio.getChargingInfo());
                        }
                        if (vodAudio.getLabels() != null) {
                            collectableLabels.addAll(vodAudio.getLabels());
                        }
                    });
                    logger.info("正在保存" + addedVodAudioList.size() + "个音频");
                    if (fastUpdate) {
                        collectableMapper.batchInsertVodAudioWithoutId(addedVodAudioList);
                        vodAudioMapper.batchInsert(addedVodAudioList);
                        if (playableSources.size() > 0) {
                            playableSourceMapper.batchInsert(playableSources);
                        }
                        if (chargingInfoList.size() > 0) {
                            chargingInfoMapper.batchInsert(chargingInfoList);
                        }
                        if (collectableLabels.size() > 0) {
                            labelMapper.batchInsert(collectableLabels);
                        }
                    } else {
                        collectableTempMapper.batchInsertVodAudioWithoutId(addedVodAudioList);
                        vodAudioTempMapper.batchInsert(addedVodAudioList);
                        if (playableSources.size() > 0) {
                            playableSourceTempMapper.batchInsert(playableSources);
                        }
                        if (chargingInfoList.size() > 0) {
                            chargingInfoTempMapper.batchInsert(chargingInfoList);
                        }
                        if (collectableLabels.size() > 0) {
                            labelTempMapper.batchInsert(collectableLabels);
                        }
                    }
                    addedVodAudioList.clear();
                }
                boolean cba = existVodAudioList.size() > 0 && tryNum > 50;
                if (!fastUpdate && (existVodAudioList.size() > bufferNum || cba)) {
                    List<PlayableSource> playableSources = new ArrayList<>();
                    List<ChargingInfo> chargingInfoList = new ArrayList<>();
                    List<CollectableLabel> collectableLabels = new ArrayList<>();
                    existVodAudioList.forEach(vodAudio -> {
                        if (vodAudio.getSources() != null) {
                            playableSources.addAll(vodAudio.getSources());
                        }
                        if (vodAudio.getChargingInfo() != null) {
                            chargingInfoList.add(vodAudio.getChargingInfo());
                        }
                        if (vodAudio.getLabels() != null) {
                            collectableLabels.addAll(vodAudio.getLabels());
                        }
                    });
                    logger.info("正在保存" + existVodAudioList.size() + "个音频");
                    collectableTempMapper.batchInsertVodAudioWithId(existVodAudioList);
                    vodAudioTempMapper.batchInsert(existVodAudioList);
                    if (playableSources.size() > 0) {
                        playableSourceTempMapper.batchInsert(playableSources);
                    }
                    if (chargingInfoList.size() > 0) {
                        chargingInfoTempMapper.batchInsert(chargingInfoList);
                    }
                    if (collectableLabels.size() > 0) {
                        labelTempMapper.batchInsert(collectableLabels);
                    }
                    existVodAudioList.clear();
                }
            } catch (Exception ex) {
                logger.error("", ex);
                continue;
            }

        }
    }

    private void save() {

    }

    public void setFastUpdate(boolean fastUpdate) {
        this.fastUpdate = fastUpdate;
    }
}
