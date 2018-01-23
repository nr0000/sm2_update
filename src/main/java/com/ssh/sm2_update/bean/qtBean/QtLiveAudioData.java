package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;

/**
 * 描述：
 * 创建： 〇 on 2017/6/2
 * Version：
 */
public class QtLiveAudioData implements Serializable {
    private Integer audience_count;

    private Integer auto_play;

    private String award_desc;

    private Integer award_open;

    private String award_text;

    private Integer category_id;

    private Integer chatgroup_id;

    private String description;

    private String freq;

    private Long id;

    private Integer link_id;

    private String playcount;

    private String sale_props;

    private Integer sale_type;

    private Integer star;

    private QtThumbs thumbs;

    private String title;

    private String type;

    private String update_time;

    private String weburl;

    private QtLiveAudioMediaInfo mediainfo;

    public Integer getAudience_count() {
        return this.audience_count;
    }

    public void setAudience_count(Integer audience_count) {
        this.audience_count = audience_count;
    }

    public Integer getAuto_play() {
        return this.auto_play;
    }

    public void setAuto_play(Integer auto_play) {
        this.auto_play = auto_play;
    }

    public String getAward_desc() {
        return this.award_desc;
    }

    public void setAward_desc(String award_desc) {
        this.award_desc = award_desc;
    }

    public Integer getAward_open() {
        return this.award_open;
    }

    public void setAward_open(Integer award_open) {
        this.award_open = award_open;
    }

    public String getAward_text() {
        return this.award_text;
    }

    public void setAward_text(String award_text) {
        this.award_text = award_text;
    }

    public Integer getCategory_id() {
        return this.category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

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

    public String getFreq() {
        return this.freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLink_id() {
        return this.link_id;
    }

    public void setLink_id(Integer link_id) {
        this.link_id = link_id;
    }

    public String getPlaycount() {
        return this.playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    public String getSale_props() {
        return this.sale_props;
    }

    public void setSale_props(String sale_props) {
        this.sale_props = sale_props;
    }

    public Integer getSale_type() {
        return this.sale_type;
    }

    public void setSale_type(Integer sale_type) {
        this.sale_type = sale_type;
    }

    public Integer getStar() {
        return this.star;
    }

    public void setStar(Integer star) {
        this.star = star;
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

    public QtLiveAudioMediaInfo getMediainfo() {
        return mediainfo;
    }

    public void setMediainfo(QtLiveAudioMediaInfo mediainfo) {
        this.mediainfo = mediainfo;
    }
}
