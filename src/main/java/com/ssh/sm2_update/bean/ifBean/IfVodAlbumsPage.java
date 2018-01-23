package com.ssh.sm2_update.bean.ifBean;

import java.io.Serializable;

/**
 * Description：
 * Created by fyx on 5/23/2017.
 * Version：
 */
public class IfVodAlbumsPage implements Serializable {
    private IfVodAlbumsPageData data;
    private String code;
    private String msg;

    public IfVodAlbumsPageData getData() {
        return data;
    }

    public void setData(IfVodAlbumsPageData data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
