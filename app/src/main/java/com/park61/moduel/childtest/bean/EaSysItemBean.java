package com.park61.moduel.childtest.bean;

/**
 * Created by shubei on 2017/9/19.
 */

public class EaSysItemBean {

    /**
     * canEa : false
     * eaLowAgeLimit : 3
     * eaSysId : 1
     * eaSysName : DAP测评(适合3-6岁)
     * eaUppAgeLimit : 6
     * finishNum : 87
     * id : 1
     * imgUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/test/20170909151521697_90.png
     * name : 学习潜能
     * subNum : 55
     * subTime : 330
     */

    private boolean canEa;
    private int eaLowAgeLimit;
    private int eaSysId;
    private String eaSysName;
    private int eaUppAgeLimit;
    private int finishNum;
    private int id;
    private String imgUrl;
    private String name;
    private int subNum;
    private int subTime;

    public boolean isCanEa() {
        return canEa;
    }

    public void setCanEa(boolean canEa) {
        this.canEa = canEa;
    }

    public int getEaLowAgeLimit() {
        return eaLowAgeLimit;
    }

    public void setEaLowAgeLimit(int eaLowAgeLimit) {
        this.eaLowAgeLimit = eaLowAgeLimit;
    }

    public int getEaSysId() {
        return eaSysId;
    }

    public void setEaSysId(int eaSysId) {
        this.eaSysId = eaSysId;
    }

    public String getEaSysName() {
        return eaSysName;
    }

    public void setEaSysName(String eaSysName) {
        this.eaSysName = eaSysName;
    }

    public int getEaUppAgeLimit() {
        return eaUppAgeLimit;
    }

    public void setEaUppAgeLimit(int eaUppAgeLimit) {
        this.eaUppAgeLimit = eaUppAgeLimit;
    }

    public int getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(int finishNum) {
        this.finishNum = finishNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubNum() {
        return subNum;
    }

    public void setSubNum(int subNum) {
        this.subNum = subNum;
    }

    public int getSubTime() {
        return subTime;
    }

    public void setSubTime(int subTime) {
        this.subTime = subTime;
    }
}
