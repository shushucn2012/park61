package com.park61.moduel.openmember.bean;

import java.io.Serializable;

/**
 * 小时卡、次卡信息
 */
public class CardInfo implements Serializable{
    private String id;
    private String userId;//用户id
    private String consumerTypeName;//交易类型名字
    private String consumerValueName;//交易值名字
    private String updateDate;//交易时间
    private String expireDate;//到期时间
    private String allTimes;//剩余总次数
    private String expireTimes;//快到期剩余次数
    private String date;
    private String text;//剩余次数，剩余小时
    private String type;//次/时

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getAllTimes() {
        return allTimes;
    }

    public void setAllTimes(String allTimes) {
        this.allTimes = allTimes;
    }

    public String getExpireTimes() {
        return expireTimes;
    }

    public void setExpireTimes(String expireTimes) {
        this.expireTimes = expireTimes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getConsumerTypeName() {
        return consumerTypeName;
    }

    public void setConsumerTypeName(String consumerTypeName) {
        this.consumerTypeName = consumerTypeName;
    }

    public String getConsumerValueName() {
        return consumerValueName;
    }

    public void setConsumerValueName(String consumerValueName) {
        this.consumerValueName = consumerValueName;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
