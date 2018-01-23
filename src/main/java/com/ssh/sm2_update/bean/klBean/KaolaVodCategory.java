package com.ssh.sm2_update.bean.klBean;

import java.io.Serializable;

/**
 * 描述：
 * 创建： 〇 on 2017/5/31
 * Version：
 */
public class KaolaVodCategory implements Serializable {
    private Long cid;
    private String name;
    private Integer hasSub;
    private String img;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHasSub() {
        return hasSub;
    }

    public void setHasSub(Integer hasSub) {
        this.hasSub = hasSub;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
