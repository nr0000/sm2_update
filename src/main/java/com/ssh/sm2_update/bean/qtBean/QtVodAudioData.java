package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;

/**
 * 描述：
 * 创建： 〇 on 2017/6/1
 * Version：
 */
public class QtVodAudioData implements Serializable {
    private Integer chatgroup_id;

    private String description;

    private Integer duration;

    private Long id;

    private QtVodAudioinfo mediainfo;

    private Integer original_fee;

    private Integer price;

    private String redirect_url;

    private String sale_status;

    private Integer sequence;

    private QtThumbs thumbs;

    private String title;

    private String type;

    private String update_time;

    private String weburl;

    public Integer getChatgroup_id() {
        return this.chatgroup_id;
    }

    public void setChatgroup_id(Integer chatgroup_id) {
        this.chatgroup_id = chatgroup_id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QtVodAudioinfo getMediainfo() {
        return this.mediainfo;
    }

    public void setMediainfo(QtVodAudioinfo mediainfo) {
        this.mediainfo = mediainfo;
    }

    public Integer getOriginal_fee() {
        return this.original_fee;
    }

    public void setOriginal_fee(Integer original_fee) {
        this.original_fee = original_fee;
    }

    public Integer getPrice() {
        return this.price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getRedirect_url() {
        return this.redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public String getSale_status() {
        return this.sale_status;
    }

    public void setSale_status(String sale_status) {
        this.sale_status = sale_status;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public QtThumbs getThumbs() {
        return this.thumbs;
    }

    public void setThumbs(QtThumbs thumbs) {
        this.thumbs = thumbs;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdate_time() {
        return this.update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getWeburl() {
        return this.weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }
}
