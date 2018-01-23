package com.ssh.sm2_update.bean.klBean;

import java.io.Serializable;

/**
 * 描述：
 * 创建： 〇 on 2017/5/31
 * Version：
 */
public class KaolaVodAudioPage implements Serializable {
    private KaolaVodAudioResult result;
    private String serverTime;
    private String requestId;

    public KaolaVodAudioResult getResult() {
        return result;
    }

    public void setResult(KaolaVodAudioResult result) {
        this.result = result;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
