package com.park61.moduel.childtest.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/12/6.
 */

public class TestRecordItemBean implements Serializable {
    private String batchCode;
    private int childId;
    private String childName;
    private String createDate;
    private int eaServiceId;
    private String eaServiceImg;
    private String eaServiceTitle;
    private int status;
    private int unfinishedEaItemNum;
    private int unfinishedTime;
    private int userId;


    public int getChildId() {
        return childId;
    }

    public int getEaServiceId() {
        return eaServiceId;
    }

    public int getStatus() {
        return status;
    }

    public int getUnfinishedEaItemNum() {
        return unfinishedEaItemNum;
    }

    public int getUnfinishedTime() {
        return unfinishedTime;
    }

    public int getUserId() {
        return userId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public String getChildName() {
        return childName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getEaServiceImg() {
        return eaServiceImg;
    }

    public String getEaServiceTitle() {
        return eaServiceTitle;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setEaServiceId(int eaServiceId) {
        this.eaServiceId = eaServiceId;
    }

    public void setEaServiceImg(String eaServiceImg) {
        this.eaServiceImg = eaServiceImg;
    }

    public void setEaServiceTitle(String eaServiceTitle) {
        this.eaServiceTitle = eaServiceTitle;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUnfinishedEaItemNum(int unfinishedEaItemNum) {
        this.unfinishedEaItemNum = unfinishedEaItemNum;
    }

    public void setUnfinishedTime(int unfinishedTime) {
        this.unfinishedTime = unfinishedTime;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
