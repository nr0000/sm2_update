package com.ssh.sm2_update.bean.ttBean;

import java.io.Serializable;

public class TtVodAlbumPage implements Serializable {

    private Integer errno;
    private String error;
    private TtVodAlbumData data;

    public Integer getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public TtVodAlbumData getData() {
        return data;
    }

    public void setData(TtVodAlbumData data) {
        this.data = data;
    }
}
