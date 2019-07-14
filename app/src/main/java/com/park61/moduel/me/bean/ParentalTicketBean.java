package com.park61.moduel.me.bean;

/**
 * Created by shubei on 2017/11/2.
 */

public class ParentalTicketBean {


    /**
     * id : 9
     * status : 0
     * qrCodeUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/test/20180110143733002_464.jpg?x-oss-process=style/compress_nologo
     * title : ace亲子游
     * schoolName : 贝玛教育
     * className : 亲子测试小班
     * startDate : 2018-01-12 10:02
     */

    private int id;
    private int status;
    private String qrCodeUrl;
    private String title;
    private String schoolName;
    private String className;
    private String startDate;

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
}
