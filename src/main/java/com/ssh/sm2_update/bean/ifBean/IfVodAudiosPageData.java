package com.ssh.sm2_update.bean.ifBean;

import java.io.Serializable;
import java.util.List;

/**
 * Description：
 * Created by fyx on 5/23/2017.
 * Version：
 */
public class IfVodAudiosPageData implements Serializable {
    private Integer count;
    private List<IfVodAudiosDataItem> list;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<IfVodAudiosDataItem> getList() {
        return list;
    }

    public void setList(List<IfVodAudiosDataItem> list) {
        this.list = list;
    }
}
