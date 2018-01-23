package com.ssh.sm2_update.bean.ttBean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

public class TtLiveAudioData implements Serializable {

    private Integer total;
    @JSONField(name = "fm_list")
    private List<TtLiveAudio> liveAudioList;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<TtLiveAudio> getLiveAudioList() {
        return liveAudioList;
    }

    public void setLiveAudioList(List<TtLiveAudio> liveAudioList) {
        this.liveAudioList = liveAudioList;
    }
}
