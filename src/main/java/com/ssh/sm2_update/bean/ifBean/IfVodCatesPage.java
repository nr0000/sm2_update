package com.ssh.sm2_update.bean.ifBean;

import java.io.Serializable;

/**
 * Description：
 * Created by fyx on 5/23/2017.
 * Version：
 */
public class IfVodCatesPage implements Serializable {
    private String msg;
    private String code;
    private IfVodCatesPageData data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public IfVodCatesPageData getData() {
        return data;
    }

    public void setData(IfVodCatesPageData data) {
        this.data = data;
    }
}
