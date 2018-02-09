package com.ssh.sm2_update.utils;

import com.sm2.bcl.content.entity.LiveCategory;
import com.sm2.bcl.content.entity.VodAudio;
import io.netty.util.internal.ConcurrentSet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyCache {
    /**
     * 更新之前已存在的专辑数据
     * key: collectableType+idFromProvider+providerType
     * value: id
     */
//    public static Map<String, Long> vodAlbumMap = new HashMap<>();


    /**
     * 更新之前已存在点播曲目数据
     * key: collectableType+idFromProvider+providerType
     * value: id
     */
//    public static Map<String, Long> vodAudioMap = new HashMap<>();

    /**
     * 更新之前已存在直播电台数据
     * key: collectableType+idFromProvider+providerType
     * value: id
     */
//    public static Map<String, Long> liveAudioMap = new HashMap<>();

    /**
     * 本次更新获取到的新增的点播曲目队列
     * 在从第三方获取到的数据经过适配后放入改队列
     * 由单独的线程从队列中取出并负责持久化
     */
    public static Queue<VodAudio> addedVodAudioQueue = new ConcurrentLinkedQueue<VodAudio>();

    /**
     * 本次更新获取到的之前就已存在点播曲目队列
     * 在从第三方获取到的数据经过适配后放入改队列
     * 由单独的线程从队列中取出并负责持久化
     */
    public static Queue<VodAudio> existVodAudioQueue = new ConcurrentLinkedQueue<VodAudio>();

    public static Set<String> savedVodAlbum = new ConcurrentSet<>();

    public static Set<String> savedVodAudio = new ConcurrentSet<>();

    public static Set<String> savedLiveAudio = new ConcurrentSet<>();

    public static long maxId;

    public static Map<String, LiveCategory> liveRegionalCatesMap;

    public static LiveCategory liveNationalCate;

    public static LiveCategory liveNetworkCate;

    public static String liveHlsPlayUrlPattern;
    public static String liveHttpPlayUrlPattern;
    public static String vodM4aPlayUrlPattern;


}
