package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 创建： 〇 on 2017/6/1
 * Version：
 */
public class QtVodAlbumPage implements Serializable {
    private List<QtVodAlbumData> data;

    private String errormsg;

    private Integer errorno;

    private Integer total;

    public List<QtVodAlbumData> getData() {
        return data;
    }

    public void setData(List<QtVodAlbumData> data) {
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
