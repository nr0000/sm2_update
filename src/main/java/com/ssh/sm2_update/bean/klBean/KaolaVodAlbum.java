package com.ssh.sm2_update.bean.klBean;

import java.io.Serializable;

/**
 * 描述：
 * 创建： 〇 on 2017/5/31
 * Version：
 */
public class KaolaVodAlbum implements Serializable {
    private Long id;
    private String name;
    private String img;
    private Long listenNum;
    private Integer source;
    private String sourceName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Long getListenNum() {
        return listenNum;
    }

    public void setListenNum(Long listenNum) {
        this.listenNum = listenNum;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
