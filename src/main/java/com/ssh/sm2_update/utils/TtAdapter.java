package com.ssh.sm2_update.utils;

import com.sm2.bcl.content.entity.PlayableSource;
import com.sm2.bcl.content.entity.VodAlbum;
import com.sm2.bcl.content.entity.VodAudio;
import com.sm2.bcl.content.entity.VodCategory;
import com.sm2.bcl.content.entity.enums.CollectableType;
import com.sm2.bcl.content.entity.enums.ProviderType;
import com.sm2.bcl.content.entity.enums.ToneQuality;
import com.ssh.sm2_update.bean.ttBean.TtVodAlbum;
import com.ssh.sm2_update.bean.ttBean.TtVodAudio;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TtAdapter {

    public static VodAlbum adapt(TtVodAlbum ttVodAlbum, VodCategory vodCategory) {
        VodAlbum vodAlbum = new VodAlbum();

        vodAlbum.setCoverPicture(ttVodAlbum.getCoversUrl());

        vodAlbum.setTitle(ttVodAlbum.getName().replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", " "));
        vodAlbum.setDescription(ttVodAlbum.getRecommendation().replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", " "));
        vodAlbum.setProviderType(ProviderType.TINGTING);
        vodAlbum.setIdFromProvider(ttVodAlbum.getType());

        vodAlbum.setVodCategory(vodCategory);
        vodAlbum.setUpdateTime(System.currentTimeMillis());
        vodAlbum.setCollectableType(CollectableType.VOD_ALBUM);
        vodAlbum.setAvailable(true);
        return vodAlbum;
    }

    public static VodAudio adapt(TtVodAudio ttVodAudio, VodAlbum vodAlbum,int sortNum) {
        VodAudio vodAudio = new VodAudio();

        vodAudio.setProviderType(ProviderType.TINGTING);
        vodAudio.setCollectableType(CollectableType.VOD_AUDIO);
        if (ttVodAudio.getName() != null)
            vodAudio.setTitle(ttVodAudio.getName().replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", " "));

        String str = ttVodAudio.getM4a();
        String regex = "[a-z0-9]{32}";
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return null;
        }
        Matcher matcher = p.matcher(str);
        if (matcher.find()) {
            vodAudio.setIdFromProvider(vodAlbum.getIdFromProvider() + "_" + matcher.group());
        } else {
            vodAudio.setIdFromProvider(vodAlbum.getIdFromProvider() + "_" + ttVodAudio.getM4a());
        }

        String md5Key = MD5Util.get32Md5(vodAudio.getCollectableType() + vodAudio.getIdFromProvider() + vodAudio.getProviderType());
        if (MyCache.savedVodAudio.contains(md5Key)) {
            return null;
        }

        vodAudio.setDefaultSortNumber(sortNum);
        vodAudio.setVodAlbum(vodAlbum);
        vodAudio.setDuration(ttVodAudio.getDuration());

        List sources = new ArrayList();
        PlayableSource source = new PlayableSource();
        source.setAudio(vodAudio);
        source.setToneQuality(ToneQuality.STANDARD);
        source.setPlayUrl(ttVodAudio.getM4a());
        sources.add(source);
        vodAudio.setSources(sources);
        vodAudio.setUpdateTime(System.currentTimeMillis());
        vodAudio.setAvailable(true);
        return vodAudio;
    }
}
