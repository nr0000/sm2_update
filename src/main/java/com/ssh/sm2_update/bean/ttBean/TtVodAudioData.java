package com.ssh.sm2_update.bean.ttBean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

public class TtVodAudioData implements Serializable {

    @JSONField(name = "total_page")
    private int totalPage;
    @JSONField(name = "album_audio_list")
    private List<TtVodAudio> albumAudioList;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<TtVodAudio> getAlbumAudioList() {
        return albumAudioList;
    }

    public void setAlbumAudioList(List<TtVodAudio> albumAudioList) {
        this.albumAudioList = albumAudioList;
    }
}
