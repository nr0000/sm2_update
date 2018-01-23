package com.ssh.sm2_update.bean.ttBean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class TtVodAlbum implements Serializable {
    private String name;
    private String recommendation;
    @JSONField(name = "covers_url")
    private String coversUrl;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getCoversUrl() {
        return coversUrl;
    }

    public void setCoversUrl(String coversUrl) {
        this.coversUrl = coversUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
