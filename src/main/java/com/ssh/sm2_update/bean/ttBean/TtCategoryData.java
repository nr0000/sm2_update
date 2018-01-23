package com.ssh.sm2_update.bean.ttBean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

public class TtCategoryData implements Serializable {

    @JSONField(name = "category_list")
    private List<TtCategory> categoryList;


    public List<TtCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<TtCategory> categoryList) {
        this.categoryList = categoryList;
    }
}
