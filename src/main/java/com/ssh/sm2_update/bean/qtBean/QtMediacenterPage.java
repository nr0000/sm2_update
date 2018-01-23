package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;

/**
 * Description：
 * Created by fyx on 6/5/2017.
 * Version：
 */
public class QtMediacenterPage implements Serializable {
    private String errormsg;
    private Integer errorno;
    private QtMediacenterPageData data;

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public Integer getErrorno() {
        return errorno;
    }

    public void setErrorno(Integer errorno) {
        this.errorno = errorno;
    }

    public QtMediacenterPageData getData() {
        return data;
    }

    public void setData(QtMediacenterPageData data) {
        this.data = data;
    }
}
