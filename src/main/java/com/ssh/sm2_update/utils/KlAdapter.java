package com.ssh.sm2_update.utils;

import com.sm2.bcl.common.util.region.Region;
import com.sm2.bcl.common.util.region.RegionUtils;
import com.sm2.bcl.content.entity.*;
import com.sm2.bcl.content.entity.enums.CollectableType;
import com.sm2.bcl.content.entity.enums.ProviderType;
import com.sm2.bcl.content.entity.enums.ToneQuality;
import com.ssh.sm2_update.bean.ifBean.IfVodAlbumsDataItem;
import com.ssh.sm2_update.bean.ifBean.IfVodAudiosDataItem;
import com.ssh.sm2_update.bean.klBean.KaolaLiveAudio;
import com.ssh.sm2_update.bean.klBean.KaolaVodAlbum;
import com.ssh.sm2_update.bean.klBean.KaolaVodAlbumPage;
import com.ssh.sm2_update.bean.klBean.KaolaVodAudio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KlAdapter {

    public static LiveAudio adapt(KaolaLiveAudio kaolaLiveAudio, LiveCategory liveCategory) {
        LiveAudio liveAudio = new LiveAudio();
        liveAudio.setTitle(kaolaLiveAudio.getName());
        liveAudio.setCoverPicture(kaolaLiveAudio.getImg());
        liveAudio.setLiveCategory(liveCategory);

        liveAudio.setProviderType(ProviderType.KAOLA);
        liveAudio.setIdFromProvider(kaolaLiveAudio.getBroadcastId().toString());

        Region region = RegionUtils.getByName(kaolaLiveAudio.getName());
        if (region != null) {
            liveAudio.setLocatedInRegionCode(region.getCode());//位于哪个地区
        }
        Set<PlayableSource> playableSources = new HashSet<PlayableSource>();
        PlayableSource playableSource = new PlayableSource();
        playableSource.setAudio(liveAudio);
        playableSource.setPlayUrl(kaolaLiveAudio.getPlayUrl());

        //剔除播放地址为空音频
        if (playableSource.getPlayUrl().trim().equals("") || playableSource.getPlayUrl() == null) {
            return null;
        }
        playableSource.setToneQuality(ToneQuality.STANDARD);
        playableSources.add(playableSource);

        liveAudio.setSources(playableSources);

        liveAudio.setUpdateTime(System.currentTimeMillis());
        liveAudio.setCollectableType(CollectableType.LIVE_AUDIO);
        liveAudio.setAvailable(true);
        return liveAudio;
    }

    public static VodAlbum adapt(KaolaVodAlbum kaolaVodAlbum, VodCategory vodCategory, String providerCate) {
        VodAlbum vodAlbum = new VodAlbum();
        vodAlbum.setCoverPicture(kaolaVodAlbum.getImg());

        vodAlbum.setTitle(kaolaVodAlbum.getName());
        vodAlbum.setProviderPlayCount(kaolaVodAlbum.getListenNum().intValue());
        vodAlbum.setVodCategory(vodCategory);

        vodAlbum.setProviderType(ProviderType.KAOLA);
        vodAlbum.setIdFromProvider(kaolaVodAlbum.getId().toString());
        vodAlbum.setUpdateTime(System.currentTimeMillis());
        vodAlbum.setCollectableType(CollectableType.VOD_ALBUM);
        vodAlbum.setProviderCate(providerCate);
        vodAlbum.setAvailable(true);
        return vodAlbum;
    }

    public static VodAudio adapt(KaolaVodAudio kaolaVodAudio, VodAlbum vodAlbum) {
        VodAudio vodAudio = new VodAudio();

        vodAudio.setProviderType(ProviderType.KAOLA);
        vodAudio.setIdFromProvider(vodAlbum.getIdFromProvider() + "_" + kaolaVodAudio.getAudioId().toString());
        vodAudio.setCollectableType(CollectableType.VOD_AUDIO);

        String md5Key = MD5Util.get32Md5(vodAudio.getCollectableType() + vodAudio.getIdFromProvider() + vodAudio.getProviderType());
        if (MyCache.savedVodAudio.contains(md5Key)) {
            return null;
        }

        vodAudio.setVodAlbum(vodAlbum);
        vodAudio.setDuration(kaolaVodAudio.getDuration() / 1000);
        vodAudio.setCoverPicture(kaolaVodAudio.getAudioPic());
        vodAudio.setDescription(kaolaVodAudio.getAudioDes());
        vodAudio.setTitle(kaolaVodAudio.getAudioName());
        vodAudio.setDefaultSortNumber(kaolaVodAudio.getOrderNum());
        vodAudio.setProviderPlayCount(kaolaVodAudio.getListenNum().intValue());

        List<PlayableSource> playableSources = new ArrayList<PlayableSource>();

        if (!kaolaVodAudio.getMp3PlayUrl32().trim().equals("") && kaolaVodAudio.getMp3PlayUrl32() != null) {
            PlayableSource sourceMp32 = getSource(kaolaVodAudio.getMp3PlayUrl32());
            sourceMp32.setToneQuality(ToneQuality.SMOOTH);
            sourceMp32.setAudio(vodAudio);
            playableSources.add(sourceMp32);
        }

        if (!kaolaVodAudio.getMp3PlayUrl64().trim().equals("") && kaolaVodAudio.getMp3PlayUrl64() != null) {
            PlayableSource sourceMp64 = getSource(kaolaVodAudio.getMp3PlayUrl64());
            sourceMp64.setToneQuality(ToneQuality.STANDARD);
            sourceMp64.setAudio(vodAudio);
            playableSources.add(sourceMp64);
        }

        if (!kaolaVodAudio.getAacPlayUrl32().trim().equals("") && kaolaVodAudio.getAacPlayUrl32() != null) {
            PlayableSource sourceAac32 = getSource(kaolaVodAudio.getAacPlayUrl32());
            sourceAac32.setToneQuality(ToneQuality.STANDARD);
            sourceAac32.setAudio(vodAudio);
            playableSources.add(sourceAac32);
        }

        if (!kaolaVodAudio.getAacPlayUrl64().trim().equals("") && kaolaVodAudio.getAacPlayUrl64() != null) {
            PlayableSource sourceAac64 = getSource(kaolaVodAudio.getAacPlayUrl64());
            sourceAac64.setToneQuality(ToneQuality.STANDARD);
            sourceAac64.setAudio(vodAudio);
            playableSources.add(sourceAac64);
        }

        if (!kaolaVodAudio.getAacPlayUrl128().trim().equals("") && kaolaVodAudio.getAacPlayUrl128() != null) {
            PlayableSource sourceAac128 = getSource(kaolaVodAudio.getAacPlayUrl128());
            sourceAac128.setToneQuality(ToneQuality.HQ);
            sourceAac128.setAudio(vodAudio);
            playableSources.add(sourceAac128);
        }

        if (playableSources.size() == 0) {
            return null;
        }

        vodAudio.setSources(playableSources);
        vodAudio.setUpdateTime(System.currentTimeMillis());
        vodAudio.setAvailable(true);
        return vodAudio;
    }

    private static PlayableSource getSource(String playUrl) {
        PlayableSource source = new PlayableSource();
        source.setPlayUrl(playUrl);
        return source;
    }
}
