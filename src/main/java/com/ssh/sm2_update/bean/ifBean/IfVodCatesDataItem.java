package com.ssh.sm2_update.bean.ifBean;

import java.io.Serializable;
import java.util.List;

/**
 * Description：
 * Created by fyx on 5/23/2017.
 * Version：
 */
public class IfVodCatesDataItem implements Serializable {
    private List<IfVodCatesItemContent> channelContent;
    private String channelName;
    private String id;

    public List<IfVodCatesItemContent> getChannelContent() {
        return channelContent;
    }

    public void setChannelContent(List<IfVodCatesItemContent> channelContent) {
        this.channelContent = channelContent;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
