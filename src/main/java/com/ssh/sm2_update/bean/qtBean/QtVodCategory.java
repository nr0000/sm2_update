package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;

/**
 * 描述：
 * 创建： 〇 on 2017/6/1
 * Version：
 */

public class QtVodCategory implements Serializable {

    private Long id;
    private String name;
    private Long section_id;
    private Integer sequence;
    private String type;

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

    public Long getSection_id() {
        return section_id;
    }

    public void setSection_id(Long section_id) {
        this.section_id = section_id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
