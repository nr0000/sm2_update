package com.ssh.sm2_update.utils;

import com.sm2.bcl.common.util.ByteUtils;
import com.sm2.bcl.common.util.ColUtils;
import com.sm2.bcl.common.util.DateUtils;
import com.sm2.bcl.common.util.region.Region;
import com.sm2.bcl.common.util.region.RegionLevel;
import com.sm2.bcl.common.util.region.RegionUtils;
import com.sm2.bcl.content.entity.*;
import com.sm2.bcl.content.entity.enums.CollectableType;
import com.sm2.bcl.content.entity.enums.ProviderType;
import com.sm2.bcl.content.entity.enums.ToneQuality;
import com.ssh.sm2_update.bean.qtBean.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QtAdapter {
    private final static Logger logger = LoggerFactory.getLogger(QtAdapter.class);

    public static VodAlbum adapt(QtVodAlbumData qtVodAlbum, VodCategory vodCategory) {
        VodAlbum vodAlbum = new VodAlbum();

        if (qtVodAlbum.getThumbs() != null && qtVodAlbum.getThumbs().getSmall_thumb() != null) {
            vodAlbum.setCoverPicture(qtVodAlbum.getThumbs().getSmall_thumb());
        }

        vodAlbum.setTitle(qtVodAlbum.getTitle());
        vodAlbum.setDescription(qtVodAlbum.getDescription());
        vodAlbum.setProviderPlayCount(parseQtPlaycount(qtVodAlbum.getPlaycount()));

        vodAlbum.setProviderType(ProviderType.QINGTING);
        vodAlbum.setIdFromProvider(qtVodAlbum.getId().toString());

        vodAlbum.setCollectableType(CollectableType.VOD_ALBUM);

        vodAlbum.setVodCategory(vodCategory);
        vodAlbum.setUpdateTime(System.currentTimeMillis());

        vodAlbum.setAvailable(true);
        return vodAlbum;
    }

    public static VodAlbum addSaleInfo(VodAlbum vodAlbum, QtVodAlbumDetailData qtVodAlbumDetailData) {
        QtVodAlbumPurchaseItemData purchaseItem = qtVodAlbumDetailData.getPurchase_item();

        VodAlbumSaleInfo saleInfo = new VodAlbumSaleInfo();
        saleInfo.setVodAlbum(vodAlbum);
        saleInfo.setProductId(purchaseItem == null ? null : purchaseItem.getId());

        VodCategory vodCategory = new VodCategory();
        vodCategory.setId(0L);

        // 单集售卖
        if (1 == qtVodAlbumDetailData.getItem_type()) {//单集售卖
            saleInfo.setIfPackedSale(false);
            vodAlbum.setSaleInfo(saleInfo);
            vodAlbum.setVodCategory(vodCategory);
        } else if (2 == qtVodAlbumDetailData.getItem_type()) { //全集售卖
            saleInfo.setIfPackedSale(true);
            ChargingInfo chargingInfo = new ChargingInfo();
            vodAlbum.setChargingInfo(chargingInfo);
            chargingInfo.setCollectable(vodAlbum);
            chargingInfo.setProviderPrice(purchaseItem == null ? null : new BigDecimal(purchaseItem.getFee().toString()));
            vodAlbum.setSaleInfo(saleInfo);
            vodAlbum.setVodCategory(vodCategory);
        } else if (100 == qtVodAlbumDetailData.getItem_type()) { // 是付费内容，但是已经下架，则忽略此专辑
            //不仅忽略，还要考虑到：走到这步，已经保存了专辑本身的信息，要将专辑本身也删掉
            logger.info("item_type=100");
        } else {
            logger.info("未处理的item_type: " + qtVodAlbumDetailData.getItem_type());
        }
        return vodAlbum;
    }

    public static LiveAudio adapt(QtLiveAudioData qtLiveAudioData) {
        if (qtLiveAudioData.getMediainfo() == null) return null;
        LiveAudio liveAudio = new LiveAudio();
        Region region = RegionUtils.getByName(qtLiveAudioData.getTitle());
        LiveCategory liveCategory = new LiveCategory();
        if (region != null) {
            liveAudio.setLocatedInRegionCode(region.getCode());
            if (region.getLevel().equals(RegionLevel.CITY)) {
                region = region.getParentRegion();
            } else if (region.getLevel().equals(RegionLevel.DISTRICT)) {
                region = region.getParentRegion().getParentRegion();
            }
            //是省市台
            liveCategory = MyCache.liveRegionalCatesMap.get(region.getCode());
        } else {
            if (qtLiveAudioData.getTitle().contains("CRI") || qtLiveAudioData.getTitle().contains("CNR")) {
                liveCategory = MyCache.liveNationalCate;
            } else {
                liveCategory = MyCache.liveNetworkCate;
            }
        }
        liveAudio.setLiveCategory(liveCategory);
        if (qtLiveAudioData.getThumbs() != null) {
            liveAudio.setCoverPicture(qtLiveAudioData.getThumbs().getSmall_thumb());
        }

        liveAudio.setDescription(qtLiveAudioData.getAward_desc());
        liveAudio.setTitle(qtLiveAudioData.getTitle());
        liveAudio.setSources(new HashSet<>());
        liveAudio.setProviderPlayCount(getPlaycount(qtLiveAudioData.getPlaycount()));

        liveAudio.setProviderType(ProviderType.QINGTING);
        liveAudio.setIdFromProvider(qtLiveAudioData.getId().toString());

        liveAudio.setCollectableType(CollectableType.LIVE_AUDIO);

        PlayableSource hlsSource = new PlayableSource();
        hlsSource.setPlayUrl(MyCache.liveHlsPlayUrlPattern.replace("${res_id}", String.valueOf(qtLiveAudioData.getMediainfo().getId())));
        hlsSource.setAudio(liveAudio);
        hlsSource.setToneQuality(ToneQuality.STANDARD);
        liveAudio.getSources().add(hlsSource);

        PlayableSource httpSource = new PlayableSource();
        httpSource.setPlayUrl(MyCache.liveHttpPlayUrlPattern.replace("${res_id}", String.valueOf(qtLiveAudioData.getMediainfo().getId())));
        httpSource.setAudio(liveAudio);
        httpSource.setToneQuality(ToneQuality.SMOOTH);
        liveAudio.getSources().add(httpSource);
        liveAudio.setAvailable(true);
        liveAudio.setUpdateTime(System.currentTimeMillis());
        return liveAudio;
    }

    private static LiveProgram adapt(QtLProgramPageDataInfo temp, LiveAudio liveAudio) {
        LiveProgram liveProgram = new LiveProgram();
        liveProgram.setCompere(ColUtils.join(temp.getDetail().getBroadcasters().stream().map(new Function<QtLProgramPageDataItemDetailBroadcastersItem, String>() {
            @Override
            public String apply(QtLProgramPageDataItemDetailBroadcastersItem qtLProgramPageDataItemDetailBroadcastersItem) {
                return qtLProgramPageDataItemDetailBroadcastersItem.getUsername();
            }
        }).collect(Collectors.toList()), ","));
        liveProgram.setDuration(temp.getDuration());
        liveProgram.setEndAtSeconds(DateUtils.hmsToSeconds(temp.getEnd_time()));
        liveProgram.setStartAtSeconds(DateUtils.hmsToSeconds(temp.getStart_time()));
        liveProgram.setLiveAudio(liveAudio);
        liveProgram.setTitle(temp.getTitle());
        return liveProgram;
    }

    public static List<LiveProgram> adapt(QtLProgramPage adaptee, LiveAudio liveAudio) {
        try {
            int i = adaptee.getData().getDay_1().size()
                    + adaptee.getData().getDay_2().size()
                    + adaptee.getData().getDay_3().size()
                    + adaptee.getData().getDay_4().size()
                    + adaptee.getData().getDay_5().size()
                    + adaptee.getData().getDay_6().size()
                    + adaptee.getData().getDay_7().size();
        } catch (NullPointerException e) {

        }
        Function<QtLProgramPageDataInfo, String> keyFunc = new Function<QtLProgramPageDataInfo, String>() {
            @Override
            public String apply(QtLProgramPageDataInfo qtLProgramPageDataInfo) {
                return qtLProgramPageDataInfo.getTitle();
            }
        };
        Function<QtLProgramPageDataInfo, QtLProgramPageDataInfo> valueFunc = new Function<QtLProgramPageDataInfo, QtLProgramPageDataInfo>() {
            @Override
            public QtLProgramPageDataInfo apply(QtLProgramPageDataInfo qtLProgramPageDataInfo) {
                return qtLProgramPageDataInfo;
            }
        };
        List<LiveProgram> livePrograms = new ArrayList<>();

        Map<String, QtLProgramPageDataInfo> programMap = new HashMap<>();
        programMap.putAll(ColUtils.colToMap(adaptee.getData().getDay_1(), keyFunc, valueFunc));
        programMap.putAll(ColUtils.colToMap(adaptee.getData().getDay_2(), keyFunc, valueFunc));
        programMap.putAll(ColUtils.colToMap(adaptee.getData().getDay_3(), keyFunc, valueFunc));
        programMap.putAll(ColUtils.colToMap(adaptee.getData().getDay_4(), keyFunc, valueFunc));
        programMap.putAll(ColUtils.colToMap(adaptee.getData().getDay_5(), keyFunc, valueFunc));
        programMap.putAll(ColUtils.colToMap(adaptee.getData().getDay_6(), keyFunc, valueFunc));
        programMap.putAll(ColUtils.colToMap(adaptee.getData().getDay_7(), keyFunc, valueFunc));

        for (String programTitle : programMap.keySet()) {
            LiveProgram liveProgram = adapt(programMap.get(programTitle), liveAudio);
            List<Boolean> showsInDays = new ArrayList<>();
            showsInDays.add(containedInDay(adaptee.getData().getDay_1(), programTitle));
            showsInDays.add(containedInDay(adaptee.getData().getDay_2(), programTitle));
            showsInDays.add(containedInDay(adaptee.getData().getDay_3(), programTitle));
            showsInDays.add(containedInDay(adaptee.getData().getDay_4(), programTitle));
            showsInDays.add(containedInDay(adaptee.getData().getDay_5(), programTitle));
            showsInDays.add(containedInDay(adaptee.getData().getDay_6(), programTitle));
            showsInDays.add(containedInDay(adaptee.getData().getDay_7(), programTitle));
            liveProgram.setDaysOfWeek(ByteUtils.boolArrToByte(showsInDays.toArray(new Boolean[]{})));

            livePrograms.add(liveProgram);
        }

        return livePrograms;
    }

    private static boolean containedInDay(List<QtLProgramPageDataInfo> programsOfDay, String testedProgramTitle) {
        return programsOfDay.stream().anyMatch(new Predicate<QtLProgramPageDataInfo>() {
            @Override
            public boolean test(QtLProgramPageDataInfo qtLProgramPageDataInfo) {
                return qtLProgramPageDataInfo.getTitle().equals(testedProgramTitle);
            }
        });
    }


    private static Pattern patPlaycount = Pattern.compile("(?<number>\\d+(\\.\\d+){0,1})(?<wan>万){0,1}");

    private static int getPlaycount(String playcountString) {
        if (playcountString == null || playcountString.equals("---"))
            playcountString = "0";
        Matcher matcher = patPlaycount.matcher(playcountString);
        if (matcher.find()) {
            if (StringUtils.isEmpty(matcher.group("wan"))) {
                return (int) Double.parseDouble(matcher.group("number"));
            } else {
                return (int) (Double.parseDouble(matcher.group("number")) * 10000);
            }
        } else {
            throw new RuntimeException("playcount解析失败: " + playcountString);
        }
    }

    public static VodAudio adapt(QtVodAudioData qtVodAudioData, VodAlbum vodAlbum, int sortNum) {
        VodAudio vodAudio = new VodAudio();

        vodAudio.setCollectableType(CollectableType.VOD_AUDIO);
        vodAudio.setProviderType(ProviderType.QINGTING);
        // 在第3方的不同的专辑中,有可能包含相同id的音频
        vodAudio.setIdFromProvider(vodAlbum.getIdFromProvider() + "_" + qtVodAudioData.getId());

        String md5Key = MD5Util.get32Md5(vodAudio.getCollectableType() + vodAudio.getIdFromProvider() + vodAudio.getProviderType());
        if (MyCache.savedVodAudio.contains(md5Key)) {
            return null;
        }
        vodAudio.setDefaultSortNumber(sortNum);
        vodAudio.setDuration(qtVodAudioData.getDuration());
        if (qtVodAudioData.getThumbs() != null)
            vodAudio.setCoverPicture(qtVodAudioData.getThumbs().getSmall_thumb());

        vodAudio.setDescription(qtVodAudioData.getDescription());
        vodAudio.setTitle(qtVodAudioData.getTitle());


        // 如果是付费内容
        if (vodAlbum.getSaleInfo() != null && qtVodAudioData.getMediainfo() == null) {
            ChargingInfo chargingInfo = new ChargingInfo();

            vodAudio.setChargingInfo(chargingInfo);
            chargingInfo.setCollectable(vodAudio);

            if (!vodAlbum.getSaleInfo().getIfPackedSale()) { // 单集售卖
                chargingInfo.setProviderPrice(new BigDecimal(qtVodAudioData.getPrice()));
            } else if (vodAlbum.getSaleInfo().getIfPackedSale()) { // 全集售卖
                chargingInfo.setProviderPrice(null);
            }
            vodAudio.setChargingInfo(chargingInfo);
        }

        List sources = new ArrayList();
        if (qtVodAudioData.getMediainfo() != null && qtVodAudioData.getMediainfo().getBitrates_url() != null) {
            qtVodAudioData.getMediainfo().getBitrates_url().forEach(new Consumer<QtVodAudioUrl>() {
                @Override
                public void accept(QtVodAudioUrl qtVodAudioUrl) {
                    if (!qtVodAudioUrl.getFile_path().trim().equals("")) {
                        PlayableSource source = new PlayableSource();
                        String filePath = "http://od.open.qingting.fm/";
                        String url = filePath + qtVodAudioUrl.getFile_path();
                        source.setPlayUrl(url);
                        int bitrate = qtVodAudioUrl.getBitrate();
                        if (bitrate < 48) {
                            source.setToneQuality(ToneQuality.SMOOTH);
                        } else if (bitrate >= 48 && bitrate <= 70) {
                            source.setToneQuality(ToneQuality.STANDARD);
                        } else if (bitrate > 70 && bitrate <= 130) {
                            source.setToneQuality(ToneQuality.HQ);
                        } else if (bitrate > 130) {
                            source.setToneQuality(ToneQuality.SQ);
                        } else {
                            source.setToneQuality(ToneQuality.UNKNOWN);
                        }
                        source.setAudio(vodAudio);
                        sources.add(source);
                    }
                }
            });
        }
        if (sources.isEmpty() && vodAlbum.getSaleInfo() == null) {
            return null;
        }
        vodAudio.setSources(sources);
        vodAudio.setUpdateTime(System.currentTimeMillis());
        vodAudio.setVodAlbum(vodAlbum);
        vodAudio.setAvailable(true);
        return vodAudio;
    }

    static int parseQtPlaycount(String str) {
        int result = 0;
        if (str == null) {
            return result;
        }
        str = str.trim();
        if (str.contains("万")) {
            str = str.replace("万", "");
            result = (int) (Float.parseFloat(str) * 10000.0);
        } else if (str.contains("亿")) {
            str = str.replace("亿", "");
            result = (int) (Float.parseFloat(str) * 100000000.0);
        } else if (str.contains("-")) {

        } else {
            try {
                result = Integer.parseInt(str);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("", ex);
            }
        }
        return result;
    }
}
