package com.ssh.sm2_update.bean.ttBean;

import java.io.Serializable;

public class TtVodAudioPage implements Serializable {

    private Integer errno;
    private String error;
    private TtVodAudioData data;

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

    public TtVodAudioData getData() {
        return data;
    }

    public void setData(TtVodAudioData data) {
        this.data = data;
    }
}
