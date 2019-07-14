package com.park61.moduel.dreamhouse.bean;

import java.io.Serializable;

/**
 * 梦想选择游戏item
 * Created by shushucn2012 on 2017/1/17.
 */
public class DreamChooseActTempBean implements Serializable {

    private String actBussinessType;
    private String actCover;
    private String actDetail;
    private String actOriginalPrice;
    private String actPrice;
    private String actTitle;
    private String isApply;
    private String isSmallClass;
    private String popularSum;

    public String getActBussinessType() {
        return actBussinessType;
    }

    public void setActBussinessType(String actBussinessType) {
        this.actBussinessType = actBussinessType;
    }

    public String getActCover() {
        return actCover;
    }

    public void setActCover(String actCover) {
        this.actCover = actCover;
    }

    public String getActDetail() {
        return actDetail;
    }

    public void setActDetail(String actDetail) {
        this.actDetail = actDetail;
    }

    public String getActOriginalPrice() {
        return actOriginalPrice;
    }

    public void setActOriginalPrice(String actOriginalPrice) {
        this.actOriginalPrice = actOriginalPrice;
    }

    public String getActPrice() {
        return actPrice;
    }

    public void setActPrice(String actPrice) {
        this.actPrice = actPrice;
    }

    public String getActTitle() {
        return actTitle;
    }

    public void setActTitle(String actTitle) {
        this.actTitle = actTitle;
    }

    public String getIsApply() {
        return isApply;
    }

    public void setIsApply(String isApply) {
        this.isApply = isApply;
    }

    public String getIsSmallClass() {
        return isSmallClass;
    }

    public void setIsSmallClass(String isSmallClass) {
        this.isSmallClass = isSmallClass;
    }

    public String getPopularSum() {
        return popularSum;
    }

    public void setPopularSum(String popularSum) {
        this.popularSum = popularSum;
    }

}
