package com.ssh.sm2_update.bean.ifBean;

import java.io.Serializable;

/**
 * Description：
 * Created by fyx on 5/23/2017.
 * Version：
 */
public class IfVodCatesItemContent implements Serializable {
    private String nodeLogo;
    private String nodeName;
    private String showLogo;
    private String isReplace;
    private String updateTime;
    private String parentNodeName;
    private String nodeRoot;
    private String parentId;
    private String categoryType;
    private String createTime;
    private String redirectType;
    private Integer id;
    private String isChild;

    public String getNodeLogo() {
        return nodeLogo;
    }

    public void setNodeLogo(String nodeLogo) {
        this.nodeLogo = nodeLogo;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getShowLogo() {
        return showLogo;
    }

    public void setShowLogo(String showLogo) {
        this.showLogo = showLogo;
    }

    public String getIsReplace() {
        return isReplace;
    }

    public void setIsReplace(String isReplace) {
        this.isReplace = isReplace;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getParentNodeName() {
        return parentNodeName;
    }

    public void setParentNodeName(String parentNodeName) {
        this.parentNodeName = parentNodeName;
    }

    public String getNodeRoot() {
        return nodeRoot;
    }

    public void setNodeRoot(String nodeRoot) {
        this.nodeRoot = nodeRoot;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(String redirectType) {
        this.redirectType = redirectType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsChild() {
        return isChild;
    }

    public void setIsChild(String isChild) {
        this.isChild = isChild;
    }
}
