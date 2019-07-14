package com.park61.moduel.childtest.bean;

/**
 * Created by shubei on 2017/9/19.
 */

public class TestRecordBean {


    /**
     * childName : 崔塔诺
     * createDate : 1505455676000
     * eaItemId : 1
     * eaItemName : 学习潜能
     * eaSysName : DAP测评(适合3-6岁)
     * imgUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/test/20170909151521697_90.png
     * status : 1
     * subjectCount : 55
     * unfinishedCount : 0
     * unfinishedTime : 0
     */

    private String childName;
    private long createDate;
    private int eaItemId;
    private String eaItemName;
    private String eaSysName;
    private String imgUrl;
    private int status;
    private int subjectCount;
    private int unfinishedCount;
    private int unfinishedTime;
    private int id;

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getEaItemId() {
        return eaItemId;
    }

    public void setEaItemId(int eaItemId) {
        this.eaItemId = eaItemId;
    }

    public String getEaItemName() {
        return eaItemName;
    }

    public void setEaItemName(String eaItemName) {
        this.eaItemName = eaItemName;
    }

    public String getEaSysName() {
        return eaSysName;
    }

    public void setEaSysName(String eaSysName) {
        this.eaSysName = eaSysName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSubjectCount() {
        return subjectCount;
    }

    public void setSubjectCount(int subjectCount) {
        this.subjectCount = subjectCount;
    }

    public int getUnfinishedCount() {
        return unfinishedCount;
    }

    public void setUnfinishedCount(int unfinishedCount) {
        this.unfinishedCount = unfinishedCount;
    }

    public int getUnfinishedTime() {
        return unfinishedTime;
    }

    public void setUnfinishedTime(int unfinishedTime) {
        this.unfinishedTime = unfinishedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
