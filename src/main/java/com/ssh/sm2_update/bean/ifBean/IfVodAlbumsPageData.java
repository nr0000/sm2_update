package com.ssh.sm2_update.bean.ifBean;

import java.io.Serializable;
import java.util.List;

/**
 * Description：
 * Created by fyx on 5/23/2017.
 * Version：
 */
public class IfVodAlbumsPageData implements Serializable {
    private Integer listCount;
    private List<IfVodAlbumsDataItem> list;

    public Integer getListCount() {
        return listCount;
    }

    public void setListCount(Integer listCount) {
        this.listCount = listCount;
    }

    public List<IfVodAlbumsDataItem> getList() {
        return list;
    }

    public void setList(List<IfVodAlbumsDataItem> list) {
        this.list = list;
    }
}
