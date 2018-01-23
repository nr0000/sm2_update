package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;

/**
 * Description：
 * Created by fyx on 6/4/2017.
 * Version：
 */
public class QtLProgramPageDataInfo implements Serializable {
    private Integer channel_id;
    private Integer chatgroup_id;
    private QtLProgramPageDataItemDetail detail;
    private Integer duration;
    private String end_time;
    private Integer id;
    private QtLProgramPageDataItemMediainfo mediainfo;
    private Integer program_id;
    private String start_time;
    private String title;
    private String type;

    public Integer getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(Integer channel_id) {
        this.channel_id = channel_id;
    }

    public Integer getChatgroup_id() {
        return chatgroup_id;
    }

    public void setChatgroup_id(Integer chatgroup_id) {
        this.chatgroup_id = chatgroup_id;
    }

    public QtLProgramPageDataItemDetail getDetail() {
        return detail;
    }

    public void setDetail(QtLProgramPageDataItemDetail detail) {
        this.detail = detail;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public QtLProgramPageDataItemMediainfo getMediainfo() {
        return mediainfo;
    }

    public void setMediainfo(QtLProgramPageDataItemMediainfo mediainfo) {
        this.mediainfo = mediainfo;
    }

    public Integer getProgram_id() {
        return program_id;
    }

    public void setProgram_id(Integer program_id) {
        this.program_id = program_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
