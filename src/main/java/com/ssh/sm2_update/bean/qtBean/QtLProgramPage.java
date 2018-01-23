package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;

/**
 * Description：
 * Created by fyx on 6/4/2017.
 * Version：
 */
public class QtLProgramPage implements Serializable {

    private QtLProgramPageDate data;
    private String errormsg;
    private Integer errorno;

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

    public QtLProgramPageDate getData() {
        return data;
    }

    public void setData(QtLProgramPageDate data) {
        this.data = data;
    }
}
