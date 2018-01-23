package com.ssh.sm2_update.bean.ttBean;

import java.io.Serializable;

public class TtCategoryPage implements Serializable {

    private Integer errno;
    private String error;
    private TtCategoryData data;

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

    public TtCategoryData getData() {
        return data;
    }

    public void setData(TtCategoryData data) {
        this.data = data;
    }
}
