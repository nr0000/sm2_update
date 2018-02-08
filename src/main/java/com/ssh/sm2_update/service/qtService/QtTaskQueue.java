package com.ssh.sm2_update.service.qtService;

import com.sm2.bcl.content.entity.LiveAudio;
import com.sm2.bcl.content.entity.VodAlbum;
import com.sm2.bcl.content.entity.VodAudio;
import io.netty.util.internal.ConcurrentSet;


import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public class QtTaskQueue {
    public static Queue<VodAlbum> vodAlbumQueue = new ConcurrentLinkedQueue<VodAlbum>();
    public static Queue<String> vodCategoryIdQueue = new ConcurrentLinkedQueue<String>();
    public static AtomicBoolean finishVodAlbum = new AtomicBoolean(false);
    public static Map<Long,Boolean> threadFinishVodAlbum = new ConcurrentHashMap();

    public static Queue<LiveAudio> liveAudioQueue = new ConcurrentLinkedQueue<LiveAudio>();
    public static Queue<String> liveCategoryIdQueue = new ConcurrentLinkedQueue<String>();
    public static AtomicBoolean finishLiveAudio = new AtomicBoolean(false);


}
