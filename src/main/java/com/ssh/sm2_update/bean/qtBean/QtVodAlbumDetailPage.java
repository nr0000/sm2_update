package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;

/**
 * 描述：
 * 创建： 〇 on 2017/6/1
 * Version：
 */
public class QtVodAlbumDetailPage implements Serializable {
    private String errormsg;

    private Integer errorno;

    private QtVodAlbumDetailData data;

    public QtVodAlbumDetailData getData() {
        return data;
    }

    public void setData(QtVodAlbumDetailData data) {
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
}
