package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;

/**
 * Created by xxb on 17/10/30.
 */
public class QtVodAlbumDetailData implements Serializable {
    private Long id;
    /**
     * 0为免费，1为单集购买，2为全集购买
     */
    private Integer item_type;

    private QtVodAlbumPurchaseItemData purchase_item;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getItem_type() {
        return item_type;
    }

    public void setItem_type(Integer item_type) {
        this.item_type = item_type;
    }

    public QtVodAlbumPurchaseItemData getPurchase_item() {
        return purchase_item;
    }

    public void setPurchase_item(QtVodAlbumPurchaseItemData purchase_item) {
        this.purchase_item = purchase_item;
    }
}
