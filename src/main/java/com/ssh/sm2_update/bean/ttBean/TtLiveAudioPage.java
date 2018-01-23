package com.ssh.sm2_update.bean.ttBean;

import java.io.Serializable;

public class TtLiveAudioPage implements Serializable {

    private Integer errno;
    private String error;
    private TtLiveAudioData data;

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

    public TtLiveAudioData getData() {
        return data;
    }

    public void setData(TtLiveAudioData data) {
        this.data = data;
    }
}
