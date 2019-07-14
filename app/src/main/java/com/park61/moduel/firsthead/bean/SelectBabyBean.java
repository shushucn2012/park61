package com.park61.moduel.firsthead.bean;

/**
 * Created by chenlie on 2018/1/16.
 */

public class SelectBabyBean {

    private int id;
    private String petName;
    private String name;
    private String birthday;
    private String childAge;
    private int sex;
    private String pictureUrl;
    private String bloodType;
    private String createTime;
    private String updateTime;
    private int userId;
    private String relationConstantId;
    private String had5Sub;
    private boolean isChoose;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getChildAge() {
        return childAge;
    }

    public void setChildAge(String childAge) {
        this.childAge = childAge;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRelationConstantId() {
        return relationConstantId;
    }

    public void setRelationConstantId(String relationConstantId) {
        this.relationConstantId = relationConstantId;
    }

    public String getHad5Sub() {
        return had5Sub;
    }

    public void setHad5Sub(String had5Sub) {
        this.had5Sub = had5Sub;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
