package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;

/**
 * Description：
 * Created by fyx on 6/4/2017.
 * Version：
 */
public class QtThumbs implements Serializable {

    private String thumb_200;
    private String thumb_400;
    private String thumb_800;
    private String large_thumb;
    private String medium_thumb;
    private String small_thumb;

    public String getThumb_200() {
        return thumb_200;
    }

    public void setThumb_200(String thumb_200) {
        this.thumb_200 = thumb_200;
    }

    public String getThumb_400() {
        return thumb_400;
    }

    public void setThumb_400(String thumb_400) {
        this.thumb_400 = thumb_400;
    }

    public String getThumb_800() {
        return thumb_800;
    }

    public void setThumb_800(String thumb_800) {
        this.thumb_800 = thumb_800;
    }

    public String getLarge_thumb() {
        return large_thumb;
    }

    public void setLarge_thumb(String large_thumb) {
        this.large_thumb = large_thumb;
    }

    public String getMedium_thumb() {
        return medium_thumb;
    }

    public void setMedium_thumb(String medium_thumb) {
        this.medium_thumb = medium_thumb;
    }

    public String getSmall_thumb() {
        return small_thumb;
    }

    public void setSmall_thumb(String small_thumb) {
        this.small_thumb = small_thumb;
    }
}
