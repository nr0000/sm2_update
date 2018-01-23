package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 创建： 〇 on 2017/6/2
 * Version：
 */
public class QtLiveAudioPage implements Serializable {
    private List<QtLiveAudioData> data;

    private String errormsg;

    private Integer errorno;

    private Integer total;

    public List<QtLiveAudioData> getData() {
        return data;
    }

    public void setData(List<QtLiveAudioData> data) {
        this.data = data;
    }

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
