package com.park61.moduel.childtest.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nieyu on 2017/12/8.
 */

public class TestResultBean implements Serializable {

private String batchCode;
    private String childAge;
    private int childId;
    private String childImg;
    private String childName;
    private String eaDate;
    private int eaServiceId;
    private String eaSysName;
    private String imgUrl;
    private int status;

    private List<ResultItemBean> eaItemVOList;

    public int getChildId() {
        return childId;
    }

    public int getEaServiceId() {
        return eaServiceId;
    }

    public int getStatus() {
        return status;
    }

    public List<ResultItemBean> getEaItemVOList() {
        return eaItemVOList;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public String getChildAge() {
        return childAge;
    }

    public String getChildImg() {
        return childImg;
    }

    public String getChildName() {
        return childName;
    }

    public String getEaDate() {
        return eaDate;
    }

    public String getEaSysName() {
        return eaSysName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public void setChildAge(String childAge) {
        this.childAge = childAge;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public void setChildImg(String childImg) {
        this.childImg = childImg;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public void setEaDate(String eaDate) {
        this.eaDate = eaDate;
    }

    public void setEaItemVOList(List<ResultItemBean> eaItemVOList) {
        this.eaItemVOList = eaItemVOList;
    }

    public void setEaServiceId(int eaServiceId) {
        this.eaServiceId = eaServiceId;
    }

    public void setEaSysName(String eaSysName) {
        this.eaSysName = eaSysName;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
