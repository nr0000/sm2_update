package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;

/**
 * 描述：
 * 创建： 〇 on 2017/6/2
 * Version：
 */
public class QtLiveCategory implements Serializable {
    private Long id;

    private String name;

    private Integer sequence;

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

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
