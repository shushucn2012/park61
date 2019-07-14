package com.park61.moduel.shop.bean;

import java.io.Serializable;

public class ShopBean implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7013507150218822864L;
    private Long id;// 主键id
    private Long areaId;// 归属区域
    private String name;// 店铺名称
    private String address;// 联系地址
    private String longitude;// 地址经度
    private String latitude;// 地址维度
    private String master;// 负责人
    private String phone;// 电话
    private String remarks;// 备注
    private String anDeviceScore;// 店铺服务
    private int pics = 0;// 店铺相册个数
    private String anExerciseScore;// 环境设备
    private String anEnjoyScore;// 游戏意义
    private double deviceScore;// 服务评分
    private double exerciseScore;// 环境评分
    private double enjoyScore;// 游戏评分
    private String storeName;// 店铺名称
    private String kiloDistance;// 距离
    private String logoPics;
    private String distanceNum;//距离
    private String picUrl;//店铺头像
    private long countyId;// 区域id
    private String cityName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAnDeviceScore() {
        return anDeviceScore;
    }

    public void setAnDeviceScore(String anDeviceScore) {
        this.anDeviceScore = anDeviceScore;
    }

    public String getAnExerciseScore() {
        return anExerciseScore;
    }

    public void setAnExerciseScore(String anExerciseScore) {
        this.anExerciseScore = anExerciseScore;
    }

    public String getAnEnjoyScore() {
        return anEnjoyScore;
    }

    public void setAnEnjoyScore(String anEnjoyScore) {
        this.anEnjoyScore = anEnjoyScore;
    }

    public double getDeviceScore() {
        return deviceScore;
    }

    public void setDeviceScore(double deviceScore) {
        this.deviceScore = deviceScore;
    }

    public double getExerciseScore() {
        return exerciseScore;
    }

    public void setExerciseScore(double exerciseScore) {
        this.exerciseScore = exerciseScore;
    }

    public double getEnjoyScore() {
        return enjoyScore;
    }

    public void setEnjoyScore(double enjoyScore) {
        this.enjoyScore = enjoyScore;
    }

    public int getPics() {
        return pics;
    }

    public void setPics(int pPics) {
        pics = pPics;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getKiloDistance() {
        return kiloDistance;
    }

    public void setKiloDistance(String kiloDistance) {
        this.kiloDistance = kiloDistance;
    }

    public String getLogoPics() {
        return logoPics;
    }

    public void setLogoPics(String logoPics) {
        this.logoPics = logoPics;
    }

    public String getDistanceNum() {
        return distanceNum;
    }

    public void setDistanceNum(String distanceNum) {
        this.distanceNum = distanceNum;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public long getCountyId() {
        return countyId;
    }

    public void setCountyId(long countyId) {
        this.countyId = countyId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
