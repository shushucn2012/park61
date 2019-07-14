package com.park61.moduel.shophome.bean;

import java.io.Serializable;

/**
 * Created by shubei on 2017/7/20.
 */

public class MyCourseBean implements Serializable {

    /**
     * ageMax : 7
     * ageMin : 3
     * canEvaluate : false
     * cost : 1
     * createDate : 1500276187000
     * detail : <p><img src="http://park61.oss-cn-zhangjiakou.aliyuncs.com/activity/20170717152251198_451.png" alt="" /></p><p><br /></p>
     * id : 23
     * name : 0021
     * officeId : 450
     * priceOriginal : 99.99
     * priceSale : 99.99
     * quotaMax : 8
     * quotaMin : 8
     * seriesId : 18
     * videoId : ceb4b724b0c3470d9f74877dfdb1fd26
     * videoImg : http://park61.oss-cn-zhangjiakou.aliyuncs.com/course/20170717151818788_321.png
     */

    private int ageMax;
    private int ageMin;
    private boolean canEvaluate;
    private int cost;
    private long createDate;
    private String detail;
    private int id;
    private String name;
    private int officeId;
    private String priceOriginal;
    private String priceSale;
    private int quotaMax;
    private int quotaMin;
    private int seriesId;
    private String videoId;
    private String videoImg;
    private int userViewNum;
    private String className;
    private int courseNum;

    public int getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(int ageMax) {
        this.ageMax = ageMax;
    }

    public int getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(int ageMin) {
        this.ageMin = ageMin;
    }

    public boolean isCanEvaluate() {
        return canEvaluate;
    }

    public void setCanEvaluate(boolean canEvaluate) {
        this.canEvaluate = canEvaluate;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public String getPriceOriginal() {
        return priceOriginal;
    }

    public void setPriceOriginal(String priceOriginal) {
        this.priceOriginal = priceOriginal;
    }

    public String getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(String priceSale) {
        this.priceSale = priceSale;
    }

    public int getQuotaMax() {
        return quotaMax;
    }

    public void setQuotaMax(int quotaMax) {
        this.quotaMax = quotaMax;
    }

    public int getQuotaMin() {
        return quotaMin;
    }

    public void setQuotaMin(int quotaMin) {
        this.quotaMin = quotaMin;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public int getUserViewNum() {
        return userViewNum;
    }

    public void setUserViewNum(int userViewNum) {
        this.userViewNum = userViewNum;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(int courseNum) {
        this.courseNum = courseNum;
    }
}
