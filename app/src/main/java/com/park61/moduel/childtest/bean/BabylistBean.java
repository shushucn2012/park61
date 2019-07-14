package com.park61.moduel.childtest.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/12/5.
 */

public class BabylistBean implements Serializable {


    private String birthday;
    private String childAge;
    private int had5Sub;
    private int id;
    private String name;
    private String petName;
    private String pictureUrl;
    private int sex;

    public int getId() {
        return id;
    }

    public int getHad5Sub() {
        return had5Sub;
    }

    public int getSex() {
        return sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getChildAge() {
        return childAge;
    }

    public String getName() {
        return name;
    }

    public String getPetName() {
        return petName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setChildAge(String childAge) {
        this.childAge = childAge;
    }

    public void setHad5Sub(int had5Sub) {
        this.had5Sub = had5Sub;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

}
