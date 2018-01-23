package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 创建： 〇 on 2017/6/1
 * Version：
 */
public class QtVodAudioinfo implements Serializable {
    private List<QtVodAudioUrl> bitrates_url;

    private Integer duration;

    private Integer id;

    public List<QtVodAudioUrl> getBitrates_url() {
        return this.bitrates_url;
    }

    public void setBitrates_url(List<QtVodAudioUrl> bitrates_url) {
        this.bitrates_url = bitrates_url;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
