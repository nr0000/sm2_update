package com.ssh.sm2_update.service;

import com.sm2.bcl.common.util.ColUtils;
import com.sm2.bcl.content.entity.Collectable;
import com.sm2.bcl.content.entity.LiveCategory;
import com.ssh.sm2_update.bean.DBTable;
import com.ssh.sm2_update.mapper.*;
import com.ssh.sm2_update.service.ifService.IfTaskQueue;
import com.ssh.sm2_update.service.ifService.IfUpdateVodAlbumRunnable;
import com.ssh.sm2_update.service.ifService.IfUpdateVodAudioRunnable;
import com.ssh.sm2_update.service.klService.KlTaskQueue;
import com.ssh.sm2_update.service.klService.KlUpdateLiveAudioRunnable;
import com.ssh.sm2_update.service.klService.KlUpdateVodAlbumRunnable;
import com.ssh.sm2_update.service.klService.KlUpdateVodAudioRunnable;
import com.ssh.sm2_update.service.qtService.*;
import com.ssh.sm2_update.service.ttService.TtTaskQueue;
import com.ssh.sm2_update.service.ttService.TtUpdateVodAlbumRunnable;
import com.ssh.sm2_update.service.ttService.TtUpdateVodAudioRunnable;
import com.ssh.sm2_update.utils.CategoryMappings;
import com.ssh.sm2_update.utils.MD5Util;
import com.ssh.sm2_update.utils.MyCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

@Service
public class UpdateService {

    @Resource
    private QtUpdateVodAlbumRunnable qtUpdateVodAlbumRunnable;
    @Resource
    private QtUpdateVodAudioRunnable qtUpdateVodAudioRunnable;
    @Resource
    private QtUpdateLiveAudioRunnable qtUpdateLiveAudioRunnable;
    @Resource
    private QtUpdateLiveProgramRunnable qtUpdateLiveProgramRunnable;
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
    @Resource
    private LiveCategoryMapper liveCategoryMapper;
    @Resource
    private LiveAudioTempMapper liveAudioTempMapper;
    @Resource
    private LiveProgramTempMapper liveProgramTempMapper;
    @Resource
    private LiveAudioMapper liveAudioMapper;
    @Resource
    private LiveProgramMapper liveProgramMapper;
    @Autowired
    private VodAlbumSaleInfoTempMapper vodAlbumSaleInfoTempMapper;
    @Autowired
    private ChargingInfoTempMapper chargingInfoTempMapper;
    @Autowired
    private VodAlbumSaleInfoMapper vodAlbumSaleInfoMapper;
    @Autowired
    private ChargingInfoMapper chargingInfoMapper;
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private LabelTempMapper labelTempMapper;
    @Resource
    private VodAudioSaveRunnable vodAudioSaveRunnable;
    @Resource
    private IfUpdateVodAudioRunnable ifUpdateVodAudioRunnable;
    @Resource
    private IfUpdateVodAlbumRunnable ifUpdateVodAlbumRunnable;
    @Resource
    private KlUpdateVodAlbumRunnable klUpdateVodAlbumRunnable;
    @Resource
    private KlUpdateVodAudioRunnable klUpdateVodAudioRunnable;
    @Resource
    private KlUpdateLiveAudioRunnable klUpdateLiveAudioRunnable;
    @Autowired
    private TtUpdateVodAlbumRunnable ttUpdateVodAlbumRunnable;
    @Autowired
    private TtUpdateVodAudioRunnable ttUpdateVodAudioRunnable;

    ExecutorService executorService = Executors.newFixedThreadPool(60);

//    @Scheduled(cron = "0 10 10 * * ?")
    public void updateQtLive(){
        boolean fastUpdate = false;
        if (!fastUpdate) {
            createTempTable();
        }
        cacheAll();
        //更新蜻蜓直播
        qtUpdateLiveAudioRunnable.setFastUpdate(fastUpdate);
        qtUpdateLiveProgramRunnable.setFastUpdate(fastUpdate);
        QtTaskQueue.liveCategoryIdQueue.add("1");
        Future submit = executorService.submit(qtUpdateLiveAudioRunnable);

        executorService.execute(qtUpdateLiveProgramRunnable);
    }

    public void updateAll(boolean fastUpdate) {
        if (!fastUpdate) {
            createTempTable();
        }
        cacheAll();
        //蜻蜓
        updateQt(fastUpdate);
        //听听
        updateTt(fastUpdate);
        //凤凰
        updateIf(fastUpdate);
        //考拉
        updateKl(fastUpdate);


        vodAudioSaveRunnable.setFastUpdate(fastUpdate);
        executorService.execute(vodAudioSaveRunnable);
        executorService.execute(vodAudioSaveRunnable);
    }

    public void updateTt(boolean fastUpdate) {
        ttUpdateVodAlbumRunnable.setFastUpdate(fastUpdate);
        Map<String, Long> ttVodCategoryMap = CategoryMappings.getTingtingVodMapping();
        for (String vodCategoryId : ttVodCategoryMap.keySet()) {
            TtTaskQueue.vodCategoryIdQueue.add(vodCategoryId);
        }
        executorService.execute(ttUpdateVodAlbumRunnable);
        ttUpdateVodAudioRunnable.setFastUpdate(fastUpdate);
        for (int i = 0; i < 6; i++) {
            executorService.execute(ttUpdateVodAudioRunnable);
        }
    }

    public void updateIf(boolean fastUpdate) {
        ifUpdateVodAlbumRunnable.setFastUpdate(fastUpdate);
        Map<String, Long> ifVodCategoryMap = CategoryMappings.getIfengVodMapping();
        for (String vodCategoryId : ifVodCategoryMap.keySet()) {
            IfTaskQueue.vodCategoryIdQueue.add(vodCategoryId);
        }
        executorService.execute(ifUpdateVodAlbumRunnable);
        ifUpdateVodAudioRunnable.setFastUpdate(fastUpdate);
        for (int i = 0; i < 4; i++) {
            executorService.execute(ifUpdateVodAudioRunnable);
        }
    }

    public void updateKl(boolean fastUpdate) {
        //考拉点播
        klUpdateVodAlbumRunnable.setFastUpdate(fastUpdate);
        Map<String, Long> klVodCategoryMap = CategoryMappings.getKaolaVodMapping();
        for (String vodCategoryId : klVodCategoryMap.keySet()) {
            KlTaskQueue.vodCategoryIdQueue.add(vodCategoryId);
        }
        executorService.execute(klUpdateVodAlbumRunnable);
        klUpdateVodAudioRunnable.setFastUpdate(fastUpdate);
        for (int i = 0; i < 10; i++) {
            executorService.execute(klUpdateVodAudioRunnable);
        }

        //考拉直播
        klUpdateLiveAudioRunnable.setFastUpdate(fastUpdate);

        Map<String, Long> klLiveCategoryMap = CategoryMappings.getKaolaLiveMapping();
        for (String liveCategoryId : klLiveCategoryMap.keySet()) {
            KlTaskQueue.liveCategoryIdQueue.add(liveCategoryId);
        }
        klUpdateLiveAudioRunnable.setFastUpdate(fastUpdate);
        executorService.execute(klUpdateLiveAudioRunnable);
    }

    public void updateQt(boolean fastUpdate) {
        //更新蜻蜓直播
        qtUpdateLiveAudioRunnable.setFastUpdate(fastUpdate);
        qtUpdateLiveProgramRunnable.setFastUpdate(fastUpdate);
        QtTaskQueue.liveCategoryIdQueue.add("1");
        executorService.execute(qtUpdateLiveAudioRunnable);
        executorService.execute(qtUpdateLiveProgramRunnable);

        //更新蜻蜓专辑
        qtUpdateVodAlbumRunnable.setFastUpdate(fastUpdate);
        Map<String, Long> qtVodCategoryMap = CategoryMappings.getQingtingVodMapping();
        for (String vodCategoryId : qtVodCategoryMap.keySet()) {
            QtTaskQueue.vodCategoryIdQueue.add(vodCategoryId);
        }
        executorService.execute(qtUpdateVodAlbumRunnable);

        //更新蜻蜓点播曲目
        qtUpdateVodAudioRunnable.setFastUpdate(fastUpdate);
        for (int i = 0; i < 18; i++) {
            executorService.execute(qtUpdateVodAudioRunnable);
        }

    }


    private void cacheAll() {
        MyCache.vodAlbumMap.clear();
        MyCache.vodAudioMap.clear();
        MyCache.liveAudioMap.clear();

        MyCache.savedVodAlbum.clear();
        MyCache.savedVodAudio.clear();
        MyCache.savedLiveAudio.clear();

        MyCache.addedVodAudioQueue.clear();
        MyCache.existVodAudioQueue.clear();

        Long maxId = collectableMapper.getMaxId();
        if (maxId == null) {
            maxId = 0L;
        }
        MyCache.maxId = maxId;
        int pageSize = 400000;
        int pageNum = (int) Math.ceil(maxId * 1.0 / pageSize);
        for (int i = 0; i < pageNum; i++) {
            List<Collectable> collectableList = collectableMapper.getIdBetween(i * pageSize, (i + 1) * pageSize);
            collectableList.forEach(collectable -> {
                switch (collectable.getCollectableType()) {
                    case VOD_ALBUM:
                        MyCache.vodAlbumMap.put(MD5Util.get32Md5(collectable.getCollectableType() + collectable.getIdFromProvider() + collectable.getProviderType()), collectable.getId());
                        break;
                    case VOD_AUDIO:
                        MyCache.vodAudioMap.put(MD5Util.get32Md5(collectable.getCollectableType() + collectable.getIdFromProvider() + collectable.getProviderType()), collectable.getId());
                        break;
                    case LIVE_AUDIO:
                        MyCache.liveAudioMap.put(MD5Util.get32Md5(collectable.getCollectableType() + collectable.getIdFromProvider() + collectable.getProviderType()), collectable.getId());
                        break;
                }
            });
        }

        cacheLiveCate();
    }

    public void cacheLiveCate() {
        if (MyCache.liveRegionalCatesMap == null) {
            MyCache.liveRegionalCatesMap = ColUtils.colToMap(liveCategoryMapper.getLiveRegionalCates(), new Function<LiveCategory, String>() {
                @Override
                public String apply(LiveCategory liveCategory) {
                    return liveCategory.getLocatedInRegionCode();
                }
            }, new Function<LiveCategory, LiveCategory>() {
                @Override
                public LiveCategory apply(LiveCategory liveCategory) {
                    return liveCategory;
                }
            });
        }

        if (MyCache.liveNationalCate == null) {
            MyCache.liveNationalCate = liveCategoryMapper.getLiveNationalCate();
        }
        if (MyCache.liveNetworkCate == null) {
            MyCache.liveNetworkCate = liveCategoryMapper.getLiveNetworkCate();
        }
    }

    private void createTempTable() {
        Long maxId = collectableMapper.getMaxId();
        if (maxId == null) {
            maxId = 0L;
        }
        labelTempMapper.dropTable();
        chargingInfoTempMapper.dropTable();
        vodAlbumSaleInfoTempMapper.dropTable();
        playableSourceTempMapper.dropTable();
        vodAudioTempMapper.dropTable();
        vodAlbumTempMapper.dropTable();
        liveProgramTempMapper.dropTable();
        liveAudioTempMapper.dropTable();
        collectableTempMapper.dropTable();

        DBTable dbTable = new DBTable("content_t_collectable_temp", maxId + 1);
        collectableTempMapper.createTable(dbTable);
        liveAudioTempMapper.createTable();
        liveProgramTempMapper.createTable();
        vodAlbumTempMapper.createTable();
        vodAudioTempMapper.createTable();
        playableSourceTempMapper.createTable();
        vodAlbumSaleInfoTempMapper.createTable();
        chargingInfoTempMapper.createTable();
        labelTempMapper.createTable();
    }

    public void th() {
        executorService.execute(() -> {
            System.out.println("ddd");
        });
    }


    public void replaceTable() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String dateDtr = sf.format(new Date());
        labelMapper.renameTable("content_t_collectablelabel_" + dateDtr);
        chargingInfoMapper.renameTable("content_t_charginginfo_" + dateDtr);
        vodAlbumSaleInfoMapper.renameTable("content_t_vodalbumsaleinfo_" + dateDtr);
        playableSourceMapper.renameTable("content_t_playablesource_" + dateDtr);
        vodAudioMapper.renameTable("content_t_vodaudio_" + dateDtr);
        vodAlbumMapper.renameTable("content_t_vodalbum_" + dateDtr);
        liveProgramMapper.renameTable("content_t_liveprogram_" + dateDtr);
        liveAudioMapper.renameTable("content_t_liveaudio_" + dateDtr);
        collectableMapper.renameTable("content_t_collectable_" + dateDtr);


        collectableTempMapper.renameTable("content_t_collectable");
        liveAudioTempMapper.renameTable("content_t_liveaudio");
        liveProgramTempMapper.renameTable("content_t_liveprogram");
        vodAlbumTempMapper.renameTable("content_t_vodalbum");
        vodAudioTempMapper.renameTable("content_t_vodaudio");
        playableSourceTempMapper.renameTable("content_t_playablesource");
        vodAlbumSaleInfoTempMapper.renameTable("content_t_vodalbumsaleinfo");
        chargingInfoTempMapper.renameTable("content_t_charginginfo");
        labelTempMapper.renameTable("content_t_collectablelabel");
    }

}
