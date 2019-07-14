package com.park61.moduel.sales.bean;

import java.io.Serializable;

/**
 * 失效商品信息
 */
public class InvalidGoodsBean implements Serializable {
    private long pmInfoId;
    private String productCname;
    private String productColor;
    private long productId;
    private String productPicUrl;
    private String productSize;
    private long promotionId;

    public long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(long promotionId) {
        this.promotionId = promotionId;
    }

    public long getPmInfoId() {
        return pmInfoId;
    }

    public void setPmInfoId(long pmInfoId) {
        this.pmInfoId = pmInfoId;
    }

    public String getProductCname() {
        return productCname;
    }

    public void setProductCname(String productCname) {
        this.productCname = productCname;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductPicUrl() {
        return productPicUrl;
    }

    public void setProductPicUrl(String productPicUrl) {
        this.productPicUrl = productPicUrl;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }
}
