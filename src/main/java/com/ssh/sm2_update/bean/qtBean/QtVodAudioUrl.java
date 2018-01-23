package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;

/**
 * 描述：
 * 创建： 〇 on 2017/6/1
 * Version：
 */
public class QtVodAudioUrl implements Serializable {
    private Integer bitrate;

    private String file_path;

    private String qetag;

    public Integer getBitrate() {
        return this.bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public String getFile_path() {
        return this.file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getQetag() {
        return this.qetag;
    }

    public void setQetag(String qetag) {
        this.qetag = qetag;
    }
}
