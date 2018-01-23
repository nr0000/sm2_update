package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 创建： 〇 on 2017/6/1
 * Version：
 */
public class QtVodAudioPage implements Serializable {
    private List<QtVodAudioData> data;

    private String errormsg;

    private Integer errorno;

    private Integer total;

    public List<QtVodAudioData> getData() {
        return this.data;
    }

    public void setData(List<QtVodAudioData> data) {
        this.data = data;
    }

    public String getErrormsg() {
        return this.errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public Integer getErrorno() {
        return this.errorno;
    }

    public void setErrorno(Integer errorno) {
        this.errorno = errorno;
    }

    public Integer getTotal() {
        return this.total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
