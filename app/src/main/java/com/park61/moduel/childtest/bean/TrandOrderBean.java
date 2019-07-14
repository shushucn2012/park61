package com.park61.moduel.childtest.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/12/11.
 */

public class TrandOrderBean implements Serializable {

    private String ageRange;
    private Double couponAmount;
    private int eaLowAge;
    private int eaServiceId;
    private int eaUpAge;
    private String imgUrl;
    private Double originalPrice;
    private Double salePrice;
    private String title;
    private int canUserCouponNum;
    private int couponId;

    public String getImgUrl() {
        return imgUrl;
    }

    public int getEaLowAge() {
        return eaLowAge;
    }

    public int getEaServiceId() {
        return eaServiceId;
    }

    public int getEaUpAge() {
        return eaUpAge;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public Double getCouponAmount() {
        return couponAmount;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public String getTitle() {
        return title;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setEaServiceId(int eaServiceId) {
        this.eaServiceId = eaServiceId;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public void setCouponAmount(Double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public void setEaLowAge(int eaLowAge) {
        this.eaLowAge = eaLowAge;
    }

    public void setEaUpAge(int eaUpAge) {
        this.eaUpAge = eaUpAge;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCanUserCouponNum() {
        return canUserCouponNum;
    }

    public void setCanUserCouponNum(int canUserCouponNum) {
        this.canUserCouponNum = canUserCouponNum;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }
}
