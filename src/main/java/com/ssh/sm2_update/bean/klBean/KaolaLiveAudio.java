package com.ssh.sm2_update.bean.klBean;

import java.io.Serializable;

/**
 * 描述：
 * 创建： 〇 on 2017/6/1
 * Version：
 */
public class KaolaLiveAudio implements Serializable {
    private Long broadcastId;
    private String name;
    private String img;
    private String currentProgramTitle;
    private Integer status;
    private Integer onLineNum;
    private Integer likedNum;
    private Integer isSubscribe;
    private String playUrl;

    public Long getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(Long broadcastId) {
        this.broadcastId = broadcastId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCurrentProgramTitle() {
        return currentProgramTitle;
    }

    public void setCurrentProgramTitle(String currentProgramTitle) {
        this.currentProgramTitle = currentProgramTitle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOnLineNum() {
        return onLineNum;
    }

    public void setOnLineNum(Integer onLineNum) {
        this.onLineNum = onLineNum;
    }

    public Integer getLikedNum() {
        return likedNum;
    }

    public void setLikedNum(Integer likedNum) {
        this.likedNum = likedNum;
    }

    public Integer getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(Integer isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }
}
