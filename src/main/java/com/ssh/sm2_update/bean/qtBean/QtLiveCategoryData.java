package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 创建： 〇 on 2017/6/5
 * Version：
 */
public class QtLiveCategoryData implements Serializable {
    private Long id;

    private String name;

    private List<QtLiveCategory> values;

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

    public List<QtLiveCategory> getValues() {
        return values;
    }

    public void setValues(List<QtLiveCategory> values) {
        this.values = values;
    }
}
