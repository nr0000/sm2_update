package com.ssh.sm2_update.utils;

import com.sm2.bcl.content.entity.*;
import com.sm2.bcl.content.entity.enums.CollectableType;
import com.sm2.bcl.content.entity.enums.ProviderType;
import com.sm2.bcl.content.entity.enums.ToneQuality;
import com.ssh.sm2_update.bean.ifBean.IfVodAlbumsDataItem;
import com.ssh.sm2_update.bean.ifBean.IfVodAudioDateItem;
import com.ssh.sm2_update.bean.ifBean.IfVodAudiosDataItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IfAdapter {

    public static VodAlbum adapt(IfVodAlbumsDataItem ifVodAlbumsDataItem, VodCategory vodCategory) {
        VodAlbum vodAlbum = new VodAlbum();
        vodAlbum.setCoverPicture(ifVodAlbumsDataItem.getImg640_640());
        vodAlbum.setDescription(ifVodAlbumsDataItem.getProgramDetails());

        vodAlbum.setTitle(ifVodAlbumsDataItem.getProgramName());

        vodAlbum.setVodCategory(vodCategory);
        vodAlbum.setProviderPlayCount(Integer.parseInt(ifVodAlbumsDataItem.getListenNumShow()));

        vodAlbum.setUpdateTime(System.currentTimeMillis());
        vodAlbum.setCollectableType(CollectableType.VOD_ALBUM);

        vodAlbum.setProviderType(ProviderType.IFENG);
        vodAlbum.setIdFromProvider(ifVodAlbumsDataItem.getId().toString());
        vodAlbum.setAvailable(true);
        return vodAlbum;
    }

    public static VodAudio adapt(IfVodAudiosDataItem ifVodAudiosDataItem, VodAlbum vodAlbum) {
        VodAudio vodAudio = new VodAudio();
        vodAudio.setProviderType(ProviderType.IFENG);
        vodAudio.setCollectableType(CollectableType.VOD_AUDIO);
        vodAudio.setIdFromProvider(vodAlbum.getIdFromProvider() + "_" + ifVodAudiosDataItem.getId());

        String md5Key = MD5Util.get32Md5(vodAudio.getCollectableType() + vodAudio.getIdFromProvider() + vodAudio.getProviderType());
        if (MyCache.savedVodAudio.contains(md5Key)) {
            return null;
        }
        vodAudio.setVodAlbum(vodAlbum);
        vodAudio.setDuration(Integer.parseInt(ifVodAudiosDataItem.getDuration()));
        vodAudio.setCoverPicture(ifVodAudiosDataItem.getImg100_100());
        vodAudio.setDescription(ifVodAudiosDataItem.getDescription());
        vodAudio.setTitle(ifVodAudiosDataItem.getTitle());
        vodAudio.setDefaultSortNumber(Integer.parseInt(ifVodAudiosDataItem.getSourceSort()));
        vodAudio.setProviderPlayCount(Integer.parseInt(ifVodAudiosDataItem.getListenNumShow()));

        vodAudio.setUpdateTime(System.currentTimeMillis());

        List<PlayableSource> playableSources = new ArrayList<>();
        if (!ifVodAudiosDataItem.getAudiolist().isEmpty()) {
            for (int i = 0; i < ifVodAudiosDataItem.getAudiolist().size(); i++) {
                PlayableSource source = new PlayableSource();
                source.setPlayUrl(ifVodAudiosDataItem.getAudiolist().get(i).getFilePath());
                //剔除播放地址为空音频
                if (source.getPlayUrl().trim().equals("") || source.getPlayUrl().trim().equals("undefined") || source.getPlayUrl() == null) {
                    continue;
                }
                source.setAudio(vodAudio);
                source.setToneQuality(ToneQuality.STANDARD);
                playableSources.add(source);
            }
        } else {
            return null;
        }
        if (playableSources.isEmpty()) {
            return null;
        }
        vodAudio.setSources(playableSources);
        Set<CollectableLabel> collectableLabels = new HashSet<>();
        CollectableLabel collectableLabel = new CollectableLabel();
        collectableLabel.setCollectable(vodAudio);
        collectableLabel.setLabel(ifVodAudiosDataItem.getTags());
        collectableLabels.add(collectableLabel);
        vodAudio.setLabels(collectableLabels);

        vodAudio.setAvailable(true);
        return vodAudio;
    }
}
