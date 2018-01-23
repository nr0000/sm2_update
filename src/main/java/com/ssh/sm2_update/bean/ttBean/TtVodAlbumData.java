package com.ssh.sm2_update.bean.ttBean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

public class TtVodAlbumData implements Serializable {

    @JSONField(name = "album_list")
    private List<TtVodAlbum> albumList;

    public List<TtVodAlbum> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<TtVodAlbum> albumList) {
        this.albumList = albumList;
    }
}
