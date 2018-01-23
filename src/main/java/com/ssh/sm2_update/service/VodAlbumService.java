package com.ssh.sm2_update.service;

import com.sm2.bcl.content.entity.ChargingInfo;
import com.sm2.bcl.content.entity.VodAlbum;
import com.sm2.bcl.content.entity.VodAlbumSaleInfo;
import com.sm2.bcl.content.entity.enums.ProviderType;
import com.ssh.sm2_update.mapper.*;
import com.ssh.sm2_update.service.ifService.IfTaskQueue;
import com.ssh.sm2_update.service.ifService.IfUpdateVodAlbumRunnable;
import com.ssh.sm2_update.service.klService.KlTaskQueue;
import com.ssh.sm2_update.service.qtService.QtTaskQueue;
import com.ssh.sm2_update.service.ttService.TtTaskQueue;
import com.ssh.sm2_update.utils.MD5Util;
import com.ssh.sm2_update.utils.MyCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
public class VodAlbumService {

    private final static Logger logger = LoggerFactory.getLogger(VodAlbumService.class);

    @Resource
    private CollectableMapper collectableMapper;
    @Resource
    private VodAlbumMapper vodAlbumMapper;
    @Resource
    private CollectableTempMapper collectableTempMapper;
    @Resource
    private VodAlbumTempMapper vodAlbumTempMapper;
    @Autowired
    private VodAlbumSaleInfoMapper vodAlbumSaleInfoMapper;
    @Autowired
    private VodAlbumSaleInfoTempMapper vodAlbumSaleInfoTempMapper;
    @Autowired
    private ChargingInfoMapper chargingInfoMapper;
    @Autowired
    private ChargingInfoTempMapper chargingInfoTempMapper;

    @Transactional
    public void deleteVodAlbum(VodAlbum vodAlbum, boolean fastUpdate) {
        if (fastUpdate) {
            if (vodAlbum.getChargingInfo() != null) {
                chargingInfoMapper.delete(vodAlbum.getChargingInfo());
            }
            if(vodAlbum.getSaleInfo()!=null){
                vodAlbumSaleInfoMapper.delete(vodAlbum.getSaleInfo());
            }
            vodAlbumMapper.delete(vodAlbum);
            collectableMapper.delete(vodAlbum);
        } else {
            if (vodAlbum.getChargingInfo() != null) {
                chargingInfoTempMapper.delete(vodAlbum.getChargingInfo());
            }
            if(vodAlbum.getSaleInfo()!=null){
                vodAlbumSaleInfoTempMapper.delete(vodAlbum.getSaleInfo());
            }
            vodAlbumTempMapper.delete(vodAlbum);
            collectableTempMapper.delete(vodAlbum);
        }
    }


    public void save(List<VodAlbum> vodAlbumList, boolean fastUpdate, ProviderType providerType) {
        List<VodAlbum> existVodAlbum = new ArrayList<>();
        //本次新增的曲目
        List<VodAlbum> newVodAlbum = new ArrayList<>();

        List<VodAlbumSaleInfo> vodAlbumSaleInfoList = new ArrayList<>();
        List<ChargingInfo> chargingInfoList = new ArrayList<>();

        //如果是快速更新就只保存新增的专辑
        if (fastUpdate) {
            for (VodAlbum vodAlbum : vodAlbumList) {
                String md5Key = MD5Util.get32Md5(vodAlbum.getCollectableType() + vodAlbum.getIdFromProvider() + vodAlbum.getProviderType());
                Long id = MyCache.vodAlbumMap.get(md5Key);
                //判断该专辑是否已经保存过，如果没有保存过就进行处理，如果已经保存过了就直接跳过
                if (!MyCache.savedVodAlbum.contains(md5Key)) {
                    //本次已经处理过的专辑放入savedVodAlbum
                    MyCache.savedVodAlbum.add(md5Key);
                    //从缓存map中取，如果没有对应的id就表明该专辑是新增的。
                    if (id == null) {
                        newVodAlbum.add(vodAlbum);

                        //如果是蜻蜓提供的专辑，同时要保存付费专辑的售卖信息
                        if (providerType == ProviderType.QINGTING) {
                            if (vodAlbum.getSaleInfo() != null) {
                                vodAlbumSaleInfoList.add(vodAlbum.getSaleInfo());
                            }
                            if (vodAlbum.getChargingInfo() != null) {
                                chargingInfoList.add(vodAlbum.getChargingInfo());
                            }
                        }
                    }
                }
            }
            if (newVodAlbum.size() > 0) {
                logger.info("正在保存新增的" + newVodAlbum.size() + "个专辑");
                collectableMapper.batchInsertVodAlbumWithoutId(newVodAlbum);
                vodAlbumMapper.batchInsert(newVodAlbum);
            }
            if (vodAlbumSaleInfoList.size() > 0) {
                vodAlbumSaleInfoMapper.batchInsert(vodAlbumSaleInfoList);
            }
            if (chargingInfoList.size() > 0) {
                chargingInfoMapper.batchInsert(chargingInfoList);
            }
        } else {
            //如果不是快速更新，所有的内容都保存到temp表中，更新完成之后再修改表名替换原来的表
            for (VodAlbum vodAlbum : vodAlbumList) {
                String md5Key = MD5Util.get32Md5(vodAlbum.getCollectableType() + vodAlbum.getIdFromProvider() + vodAlbum.getProviderType());
                Long id = MyCache.vodAlbumMap.get(md5Key);
                //判断该专辑是否已经保存过，
                if (!MyCache.savedVodAlbum.contains(md5Key)) {
                    //本次已经处理过的专辑放入savedVodAlbum
                    MyCache.savedVodAlbum.add(md5Key);
                    if (id == null) {
                        newVodAlbum.add(vodAlbum);
                    } else {
                        vodAlbum.setId(id);
                        existVodAlbum.add(vodAlbum);
                    }
                    //如果是蜻蜓提供的专辑，同时要保存付费专辑的售卖信息
                    if (providerType == ProviderType.QINGTING) {
                        if (vodAlbum.getSaleInfo() != null) {
                            vodAlbumSaleInfoList.add(vodAlbum.getSaleInfo());
                        }
                        if (vodAlbum.getChargingInfo() != null) {
                            chargingInfoList.add(vodAlbum.getChargingInfo());
                        }
                    }
                }
            }
            if (newVodAlbum.size() > 0) {
                logger.info("正在保存新增的" + newVodAlbum.size() + "个专辑");
                collectableTempMapper.batchInsertVodAlbumWithoutId(newVodAlbum);
                vodAlbumTempMapper.batchInsert(newVodAlbum);
            }
            if (existVodAlbum.size() > 0) {
                logger.info("正在保存更新的" + existVodAlbum.size() + "个专辑");
                collectableTempMapper.batchInsertVodAlbumWithId(existVodAlbum);
                vodAlbumTempMapper.batchInsert(existVodAlbum);
            }
            if (vodAlbumSaleInfoList.size() > 0) {
                vodAlbumSaleInfoTempMapper.batchInsert(vodAlbumSaleInfoList);
            }
            if (chargingInfoList.size() > 0) {
                chargingInfoTempMapper.batchInsert(chargingInfoList);
            }
        }

        addToQueue(newVodAlbum, providerType);
        addToQueue(existVodAlbum, providerType);
    }

    private void addToQueue(List<VodAlbum> vodAlbums, ProviderType providerType) {
        Queue<VodAlbum> vodAlbumQueue = null;
        switch (providerType) {
            case IFENG: {
                vodAlbumQueue = IfTaskQueue.vodAlbumQueue;
                break;
            }
            case QINGTING: {
                vodAlbumQueue = QtTaskQueue.vodAlbumQueue;
                break;
            }
            case KAOLA: {
                vodAlbumQueue = KlTaskQueue.vodAlbumQueue;
                break;
            }
            case TINGTING: {
                vodAlbumQueue = TtTaskQueue.vodAlbumQueue;
                break;
            }
        }
        for (VodAlbum vodAlbum : vodAlbums) {
            while (vodAlbumQueue.size() >= 2500) {
                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (vodAlbumQueue.size() >= 2000) {
                try {
                    Thread.sleep(15 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            vodAlbumQueue.add(vodAlbum);
        }
    }
}
