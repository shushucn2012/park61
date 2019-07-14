package com.park61.moduel.childtest.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/12/5.
 */

public class TestRecommedBean implements Serializable {

    private int answerType;
    private int btnStatus;
    private String btnStatusName;
    private int buyStatus;
    private int eaLowAgeLimit;
    private int eaUpAgeLimit;
    private int id;
    private String imgUrl;
    private double originalPrice;
    private double salePrice;
    private String serviceTypeName;
    private String title;
    private String countFinishedNum;
    private int isLogin;
    private int canUseCoupon;

    public int getAnswerType() {
        return answerType;
    }

    public int getBtnStatus() {
        return btnStatus;
    }

    public int getBuyStatus() {
        return buyStatus;
    }

    public int getEaLowAgeLimit() {
        return eaLowAgeLimit;
    }

    public int getEaUpAgeLimit() {
        return eaUpAgeLimit;
    }

    public int getId() {
        return id;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public String getBtnStatusName() {
        return btnStatusName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public String getTitle() {
        return title;
    }

    public void setAnswerType(int answerType) {
        this.answerType = answerType;
    }

    public void setBtnStatus(int btnStatus) {
        this.btnStatus = btnStatus;
    }

    public void setBtnStatusName(String btnStatusName) {
        this.btnStatusName = btnStatusName;
    }

    public void setBuyStatus(int buyStatus) {
        this.buyStatus = buyStatus;
    }

    public void setEaLowAgeLimit(int eaLowAgeLimit) {
        this.eaLowAgeLimit = eaLowAgeLimit;
    }

    public void setEaUpAgeLimit(int eaUpAgeLimit) {
        this.eaUpAgeLimit = eaUpAgeLimit;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountFinishedNum() {
        return countFinishedNum;
    }

    public void setCountFinishedNum(String countFinishedNum) {
        this.countFinishedNum = countFinishedNum;
    }

    public int getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(int isLogin) {
        this.isLogin = isLogin;
    }

    public int getCanUseCoupon() {
        return canUseCoupon;
    }

    public void setCanUseCoupon(int canUseCoupon) {
        this.canUseCoupon = canUseCoupon;
    }
}
