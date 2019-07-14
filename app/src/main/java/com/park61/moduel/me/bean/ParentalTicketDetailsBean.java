package com.park61.moduel.me.bean;

/**
 * Created by shubei on 2017/11/2.
 */

public class ParentalTicketDetailsBean {


    /**
     * id : 9
     * status : 2
     * qrCodeUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/test/20180110143733002_464.jpg
     * title : ace亲子游
     * schoolName : 贝玛教育
     * className : 亲子测试小班
     * startDate : 2018-01-12 10:02
     * signDate : null
     * applyDate : 2018-01-10 14:38
     * coverUrl : null
     * boughtGoods : false
     * partyId : 26
     */

    private int id;
    private int status;
    private String qrCodeUrl;
    private String title;
    private String schoolName;
    private String className;
    private String startDate;
    private String signDate;
    private String applyDate;
    private String coverUrl;
    private boolean boughtGoods;
    private int partyId;
    private String startDateStr;
    private String userName;
    private String userTel;
    private int classId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public boolean isBoughtGoods() {
        return boughtGoods;
    }

    public void setBoughtGoods(boolean boughtGoods) {
        this.boughtGoods = boughtGoods;
    }

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
