package com.ssh.sm2_update.service;

import com.sm2.bcl.common.util.ColUtils;
import com.sm2.bcl.content.entity.Collectable;
import com.sm2.bcl.content.entity.LiveCategory;
import com.ssh.sm2_update.bean.DBTable;
import com.ssh.sm2_update.config.MyConfig;
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
import com.ssh.sm2_update.utils.MyCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;

@Service
public class UpdateService {
    private final static Logger logger = LoggerFactory.getLogger(UpdateService.class);

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
    private RedisService redisService;
    @Autowired
    private TtUpdateVodAlbumRunnable ttUpdateVodAlbumRunnable;
    @Autowired
    private TtUpdateVodAudioRunnable ttUpdateVodAudioRunnable;
    @Autowired
    private MyConfig myConfig;
    @Autowired
    private SolrService solrService;

    //    ExecutorService executorService = Executors.newFixedThreadPool(20);
    ThreadPoolExecutor executorService = new ThreadPoolExecutor(15, 15,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    public void updateAll(boolean fastUpdate) {

        int activeCount = executorService.getActiveCount();
        logger.info("当前线程池中有" + activeCount + "个活动线程");

        if (activeCount != 0) {
            logger.info("当前线程池存在活动线程,本次更新任务将不会执行");
            logger.info("蜻蜓电台线程：" + QtTaskQueue.threadFinishLiveAudio.toString());
            logger.info("蜻蜓节目线程：" + QtTaskQueue.threadFinishLiveProgram.toString());
            logger.info("蜻蜓专辑线程：" + QtTaskQueue.threadFinishVodAlbum.toString());
            logger.info("蜻蜓点播线程：" + QtTaskQueue.threadFinishVodAudio.toString());

            logger.info("凤凰专辑线程：" + IfTaskQueue.threadFinishVodAlbum.toString());
            logger.info("凤凰点播线程：" + IfTaskQueue.threadFinishVodAudio.toString());

            logger.info("考拉专辑线程：" + KlTaskQueue.threadFinishVodAlbum.toString());
            logger.info("考拉点播线程：" + KlTaskQueue.threadFinishVodAudio.toString());
            logger.info("考拉电台线程：" + KlTaskQueue.threadFinishLiveAudio.toString());

            logger.info("听听专辑线程：" + TtTaskQueue.threadFinishVodAlbum.toString());
            logger.info("听听点播线程：" + TtTaskQueue.threadFinishVodAudio.toString());

            return;
        }
        logger.info("================================================================================================================================");

        if (!fastUpdate) {
            createTempTable();
        }
        cacheAll();

        vodAudioSaveRunnable.setFastUpdate(fastUpdate);
        executorService.execute(vodAudioSaveRunnable);
        executorService.execute(vodAudioSaveRunnable);

        //蜻蜓
        updateQt(fastUpdate);
        //凤凰
        updateIf(fastUpdate);
        //考拉
        updateKl(fastUpdate);
        //听听
        updateTt(fastUpdate);

    }

    public void updateTt(boolean fastUpdate) {
        ttUpdateVodAlbumRunnable.setFastUpdate(fastUpdate);
        Map<String, Long> ttVodCategoryMap = CategoryMappings.getTingtingVodMapping();
        for (String vodCategoryId : ttVodCategoryMap.keySet()) {
            TtTaskQueue.vodCategoryIdQueue.add(vodCategoryId);
        }
        executorService.execute(ttUpdateVodAlbumRunnable);
        ttUpdateVodAudioRunnable.setFastUpdate(fastUpdate);
        for (int i = 0; i < 1; i++) {
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
        for (int i = 0; i < 3; i++) {
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
        for (int i = 0; i < 2; i++) {
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
        for (int i = 0; i < 4; i++) {
            executorService.execute(qtUpdateVodAudioRunnable);
        }

    }


    private void cacheAll() {
        MyCache.savedVodAlbum.clear();
        MyCache.savedVodAudio.clear();
        MyCache.savedLiveAudio.clear();

        MyCache.addedVodAudioQueue.clear();
        MyCache.existVodAudioQueue.clear();

        Long maxId = null;
        try {
            maxId = collectableMapper.getMaxId();
        } catch (Exception e) {
            logger.info("", e);
        }
        if (maxId == null) {
            maxId = 0L;
        }
        MyCache.maxId = maxId;
        cacheLiveCate();
    }

    public void updateSolr() {
        solrService.dataImport();
        logger.info("solr索引更新任务完成");
    }

    public void dbToRedis() {
        logger.info("开始将数据库的数据存入Redis");
        Long maxId = collectableMapper.getMaxId();
        if (maxId == null) {
            maxId = 0L;
        }
        MyCache.maxId = maxId;
        int pageSize = 400000;
        int pageNum = (int) Math.ceil(maxId * 1.0 / pageSize);
        logger.info("总共有" + pageNum + "页，每页大约有400000条记录");

        for (int i = 0; i < pageNum; i++) {
            logger.info("正在缓存第" + (i + 1) + "页");
            List<Collectable> collectableList = collectableMapper.getIdBetween(i * pageSize, (i + 1) * pageSize);
            collectableList.forEach(collectable -> {
                redisService.addToRedis(collectable);
            });
        }
        logger.info("完成Redis缓存同步");
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

        Long maxId = null;
        try {
            maxId = collectableMapper.getMaxId();
        } catch (Exception e) {
            logger.info("", e);
        }

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

        DBTable dbTable = new DBTable("content_t_collectable_temp", maxId + 10000);
        collectableTempMapper.createTable(dbTable);
        liveAudioTempMapper.createTable();
        liveProgramTempMapper.createTable();
        vodAlbumTempMapper.createTable();
        vodAudioTempMapper.createTable();
        playableSourceTempMapper.createTable();
        vodAlbumSaleInfoTempMapper.createTable();
        chargingInfoTempMapper.createTable();
        labelTempMapper.createTable();

        collectableTempMapper.createTrigger(myConfig.getUserDB());
        liveAudioTempMapper.createTrigger(myConfig.getUserDB());
        vodAlbumTempMapper.createTrigger(myConfig.getUserDB());
        vodAudioTempMapper.createTrigger(myConfig.getUserDB());
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
