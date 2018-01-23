package com.ssh.sm2_update.bean.klBean;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 创建： 〇 on 2017/5/31
 * Version：
 */
public class KaolaVodAlbumResult implements Serializable {
    private Boolean haveNext;
    private Integer nextPage;
    private Integer havePre;
    private Integer prePage;
    private Integer currentPage;
    private Integer count;
    private Integer sumPage;
    private Integer pageSize;
    private List<KaolaVodAlbum> dataList;

    public Boolean getHaveNext() {
        return haveNext;
    }

    public void setHaveNext(Boolean haveNext) {
        this.haveNext = haveNext;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getHavePre() {
        return havePre;
    }

    public void setHavePre(Integer havePre) {
        this.havePre = havePre;
    }

    public Integer getPrePage() {
        return prePage;
    }

    public void setPrePage(Integer prePage) {
        this.prePage = prePage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSumPage() {
        return sumPage;
    }

    public void setSumPage(Integer sumPage) {
        this.sumPage = sumPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<KaolaVodAlbum> getDataList() {
        return dataList;
    }

    public void setDataList(List<KaolaVodAlbum> dataList) {
        this.dataList = dataList;
    }
}
