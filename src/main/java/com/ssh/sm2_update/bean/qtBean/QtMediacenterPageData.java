package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;

/**
 * Description：
 * Created by fyx on 6/5/2017.
 * Version：
 */
public class QtMediacenterPageData implements Serializable {
    private QtMediacenterPageDataRadiostations radiostations_hls;
    private QtMediacenterPageDataRadiostations radiostations_hls_https;
    private QtMediacenterPageDataRadiostations radiostations_http;
    private QtMediacenterPageDataRadiostations storedaudio_m4a;

    public QtMediacenterPageDataRadiostations getRadiostations_hls() {
        return radiostations_hls;
    }

    public void setRadiostations_hls(QtMediacenterPageDataRadiostations radiostations_hls) {
        this.radiostations_hls = radiostations_hls;
    }

    public QtMediacenterPageDataRadiostations getRadiostations_hls_https() {
        return radiostations_hls_https;
    }

    public void setRadiostations_hls_https(QtMediacenterPageDataRadiostations radiostations_hls_https) {
        this.radiostations_hls_https = radiostations_hls_https;
    }

    public QtMediacenterPageDataRadiostations getRadiostations_http() {
        return radiostations_http;
    }

    public void setRadiostations_http(QtMediacenterPageDataRadiostations radiostations_http) {
        this.radiostations_http = radiostations_http;
    }

    public QtMediacenterPageDataRadiostations getStoredaudio_m4a() {
        return storedaudio_m4a;
    }

    public void setStoredaudio_m4a(QtMediacenterPageDataRadiostations storedaudio_m4a) {
        this.storedaudio_m4a = storedaudio_m4a;
    }
}
