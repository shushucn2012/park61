package com.park61.moduel.acts.bean;

import java.io.Serializable;

/**
 * Created by shushucn2012 on 2016/11/1.
 */
public class AdvertisingPageBean implements Serializable {

    private String downUrl;
    private String endDate;
    private String startDate;
    private String version;
    private int imgNum;

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getImgNum() {
        return imgNum;
    }

    public void setImgNum(int imgNum) {
        this.imgNum = imgNum;
    }

}
