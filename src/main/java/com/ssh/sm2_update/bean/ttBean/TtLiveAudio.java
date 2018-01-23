package com.ssh.sm2_update.bean.ttBean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class TtLiveAudio implements Serializable {

    @JSONField(name = "fm_id")
    private Long fmId;

    private String name;

    @JSONField(name = "live_url")
    private String liveUrl;

    public Long getFmId() {
        return fmId;
    }

    public void setFmId(Long fmId) {
        this.fmId = fmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }
}
