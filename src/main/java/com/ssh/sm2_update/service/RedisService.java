package com.ssh.sm2_update.service;

import com.sm2.bcl.content.entity.Collectable;
import com.sm2.bcl.content.entity.LiveAudio;
import com.sm2.bcl.content.entity.VodAlbum;
import com.sm2.bcl.content.entity.VodAudio;
import com.ssh.sm2_update.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    public void addToRedis(Collectable collectable){
        switch (collectable.getCollectableType()) {
            case VOD_ALBUM:
                addVodAlbumIdMap(collectable);
                break;
            case VOD_AUDIO:
                addVodAudioIdMap(collectable);
                break;
            case LIVE_AUDIO:
                addLiveAudioIdMap(collectable);
                break;
        }
    }

    private void addVodAlbumIdMap(Collectable collectable) {
        String md5Key = MD5Util.get32Md5(collectable.getCollectableType() + collectable.getIdFromProvider() + collectable.getProviderType());
        redisTemplate.opsForValue().set("vodAlbumIdMap: " + md5Key, collectable.getId());
    }

    private void addVodAudioIdMap(Collectable collectable) {
        String md5Key = MD5Util.get32Md5(collectable.getCollectableType() + collectable.getIdFromProvider() + collectable.getProviderType());
        redisTemplate.opsForValue().set("vodAudioIdMap: " + md5Key, collectable.getId());
    }

    private void addLiveAudioIdMap(Collectable collectable) {
        String md5Key = MD5Util.get32Md5(collectable.getCollectableType() + collectable.getIdFromProvider() + collectable.getProviderType());
        redisTemplate.opsForValue().set("liveAudioIdMap: " + md5Key, collectable.getId());
    }

    public Long getVodAlbumId(String md5Key){
        return (Long) redisTemplate.opsForValue().get("vodAlbumIdMap: " + md5Key);
    }

    public Long getVodAudioId(String md5Key){
        return (Long) redisTemplate.opsForValue().get("vodAudioIdMap: " + md5Key);
    }

    public Long getLiveAudioId(String md5Key){
        return (Long) redisTemplate.opsForValue().get("liveAudioIdMap: " + md5Key);
    }

    public Long getId(VodAlbum vodAlbum){
        String md5Key = MD5Util.get32Md5(vodAlbum.getCollectableType() + vodAlbum.getIdFromProvider() + vodAlbum.getProviderType());
        return getVodAlbumId(md5Key);
    }

    public Long getId(VodAudio vodAudio){
        String md5Key = MD5Util.get32Md5(vodAudio.getCollectableType() + vodAudio.getIdFromProvider() + vodAudio.getProviderType());
        return getVodAudioId(md5Key);
    }

    public Long getId(LiveAudio liveAudio){
        String md5Key = MD5Util.get32Md5(liveAudio.getCollectableType() + liveAudio.getIdFromProvider() + liveAudio.getProviderType());
        return getLiveAudioId(md5Key);
    }

    public void addLiveAudioToRedis(List<LiveAudio> liveAudios){
        liveAudios.forEach(liveAudio -> addLiveAudioIdMap(liveAudio));
    }

    public void addVodAlbumToRedis(List<VodAlbum> vodAlbums){
        vodAlbums.forEach(vodAlbum -> addVodAlbumIdMap(vodAlbum));
    }

    public void addVodAudioToRedis(List<VodAudio> vodAudios){
        vodAudios.forEach(vodAudio -> addVodAudioIdMap(vodAudio));
    }

    public void del(VodAlbum vodAlbum){
        String md5Key = MD5Util.get32Md5(vodAlbum.getCollectableType() + vodAlbum.getIdFromProvider() + vodAlbum.getProviderType());
        redisTemplate.delete("vodAlbumIdMap: " + md5Key);
    }
}
