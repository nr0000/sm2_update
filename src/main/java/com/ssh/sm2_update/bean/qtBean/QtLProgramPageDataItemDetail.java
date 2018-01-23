package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;
import java.util.List;

/**
 * Description：
 * Created by fyx on 6/4/2017.
 * Version：
 */
public class QtLProgramPageDataItemDetail implements Serializable {
    private List<QtLProgramPageDataItemDetailBroadcastersItem> broadcasters;

    public List<QtLProgramPageDataItemDetailBroadcastersItem> getBroadcasters() {
        return broadcasters;
    }

    public void setBroadcasters(List<QtLProgramPageDataItemDetailBroadcastersItem> broadcasters) {
        this.broadcasters = broadcasters;
    }
}
