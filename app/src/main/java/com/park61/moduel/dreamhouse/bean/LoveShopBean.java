package com.park61.moduel.dreamhouse.bean;

import java.io.Serializable;

/**
 * 常去店铺
 */
public class LoveShopBean implements Serializable {
    private Long areaId;
    private Long shopId;
    private String shopName;

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
