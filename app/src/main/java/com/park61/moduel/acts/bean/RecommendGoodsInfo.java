package com.park61.moduel.acts.bean;

import java.io.Serializable;

public class RecommendGoodsInfo implements Serializable {
    private String img;
    private String name;
    private String availableNum;
    private String oldPrice;
    private String salesPrice;
    private Long pmInfoid;
    private Long promotionId;

    public RecommendGoodsInfo() {
    }

    public RecommendGoodsInfo(String img, String name, String salesPrice) {
        this.img = img;
        this.name = name;
        this.salesPrice = salesPrice;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public String getAvailableNum() {
        return availableNum;
    }

    public void setAvailableNum(String availableNum) {
        this.availableNum = availableNum;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Long getPmInfoid() {
        return pmInfoid;
    }

    public void setPmInfoid(Long pmInfoid) {
        this.pmInfoid = pmInfoid;
    }
}
