package com.ssh.sm2_update.service.ifService;

import com.sm2.bcl.content.entity.VodAlbum;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public class IfTaskQueue {
    public static Queue<VodAlbum> vodAlbumQueue = new ConcurrentLinkedQueue<VodAlbum>();
    public static Queue<String> vodCategoryIdQueue = new ConcurrentLinkedQueue<String>();
    public static AtomicBoolean finishVodAlbum = new AtomicBoolean(true);
    public static Map<Long,Boolean> threadFinishVodAlbum = new ConcurrentHashMap();
    public static AtomicBoolean finishVodAudio = new AtomicBoolean(true);
    public static Map<Long,Boolean> threadFinishVodAudio = new ConcurrentHashMap();

}
