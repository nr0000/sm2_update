package com.ssh.sm2_update.bean.ifBean;

import java.io.Serializable;
import java.util.List;

/**
 * Description：
 * Created by fyx on 5/23/2017.
 * Version：
 */
public class IfVodCatesPageData implements Serializable {
    private List<IfVodCatesDataItem> list;

    public List<IfVodCatesDataItem> getList() {
        return list;
    }

    public void setList(List<IfVodCatesDataItem> list) {
        this.list = list;
    }
}
