package com.ssh.sm2_update.bean.klBean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 创建： 〇 on 2017/5/31
 * Version：
 */
public class KaolaVodCategoryPage implements Serializable {
    private List<KaolaVodCategory> result;
    private String serverTime;
    private String requestId;

    public List<KaolaVodCategory> getResult() {
        return result;
    }

    public void setResult(List<KaolaVodCategory> result) {
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
