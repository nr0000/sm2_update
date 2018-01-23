package com.ssh.sm2_update.bean.ifBean;

import java.io.Serializable;

/**
 * Description：
 * Created by fyx on 5/23/2017.
 * Version：
 */
public class IfVodAudiosPage implements Serializable {
    private String msg;
    private String code;
    private IfVodAudiosPageData data;

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

    public IfVodAudiosPageData getData() {
        return data;
    }

    public void setData(IfVodAudiosPageData data) {
        this.data = data;
    }
}
