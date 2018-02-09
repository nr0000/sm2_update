package com.ssh.sm2_update.service;

import com.ssh.sm2_update.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LiveAudioService {
    @Resource
    private CollectableMapper collectableMapper;
    @Resource
    private LiveAudioMapper liveAudioMapper;
    @Resource
    private PlayableSourceMapper playableSourceMapper;
    @Resource
    private LiveAudioTempMapper liveAudioTempMapper;
    @Resource
    private CollectableTempMapper collectableTempMapper;
    @Resource
    private PlayableSourceTempMapper playableSourceTempMapper;
    @Autowired
    private RedisService redisService;



}
