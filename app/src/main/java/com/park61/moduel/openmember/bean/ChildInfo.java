package com.park61.moduel.openmember.bean;

import java.io.Serializable;

/**
 * 会员卡小孩信息
 */
public class ChildInfo implements Serializable{
    private Long childId;
    private String petName;
    private String picUrl;
    private int type;//时间字体颜色：0灰色，1黑色
    private String expireDate;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
