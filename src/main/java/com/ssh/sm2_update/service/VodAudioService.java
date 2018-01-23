package com.ssh.sm2_update.service;

import com.sm2.bcl.content.entity.VodAudio;
import com.ssh.sm2_update.mapper.*;
import com.ssh.sm2_update.utils.MD5Util;
import com.ssh.sm2_update.utils.MyCache;
import org.hibernate.annotations.Synchronize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VodAudioService {
    private final static Logger logger = LoggerFactory.getLogger(VodAudioService.class);


    public void addToQueue(List<VodAudio> vodAudios, boolean fastUpdate) {
        if (fastUpdate) {
            for (VodAudio vodAudio : vodAudios) {
                String md5Key = MD5Util.get32Md5(vodAudio.getCollectableType() + vodAudio.getIdFromProvider() + vodAudio.getProviderType());
                Long id = MyCache.vodAudioMap.get(md5Key);
                if (!MyCache.savedVodAudio.contains(md5Key)) {
                    MyCache.savedVodAudio.add(md5Key);
                    if (id == null) {
                        //从缓存map中取，如果没有对应的id就表明该曲目是新增的。
                        while (MyCache.addedVodAudioQueue.size() > 20000) {
                            pause(5);
                        }
                        MyCache.addedVodAudioQueue.add(vodAudio);
                    }
                }
            }
        } else {
            for (VodAudio vodAudio : vodAudios) {
                String md5Key = MD5Util.get32Md5(vodAudio.getCollectableType() + vodAudio.getIdFromProvider() + vodAudio.getProviderType());
                Long id = MyCache.vodAudioMap.get(md5Key);
                if (!MyCache.savedVodAudio.contains(md5Key)) {
                    MyCache.savedVodAudio.add(md5Key);
                    if (id == null) {
                        //从缓存map中取，如果没有对应的id就表明该曲目是新增的。
                        while (MyCache.addedVodAudioQueue.size() > 20000) {
                            pause(5);
                        }
                        MyCache.addedVodAudioQueue.add(vodAudio);
                    } else {
                        vodAudio.setId(id);
                        while (MyCache.existVodAudioQueue.size() > 20000) {
                            pause(5);
                        }
                        MyCache.existVodAudioQueue.add(vodAudio);
                    }
                }
            }
        }
    }

    //线程暂停，单位s
    private void pause(long i) {
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
