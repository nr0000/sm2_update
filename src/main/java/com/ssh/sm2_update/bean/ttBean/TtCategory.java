package com.ssh.sm2_update.bean.ttBean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class TtCategory implements Serializable {
    private String name;
    private String type;
    @JSONField(name = "sub_catelist")
    private Integer subCatelist;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSubCatelist() {
        return subCatelist;
    }

    public void setSubCatelist(Integer subCatelist) {
        this.subCatelist = subCatelist;
    }
}
