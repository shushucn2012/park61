package com.park61.moduel.grouppurchase.bean;

import java.math.BigDecimal;

/**
 * 拼团详情拼团信息
 */
public class FightGroupInfo {
    private BigDecimal fightGroupPrice;
    private BigDecimal saveMoney;
    private Long invalidTime;
    private int personNum;
    private Long pmInfoId;
    private String title;
    private String sendTime;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getSaveMoney() {
        return saveMoney;
    }

    public void setSaveMoney(BigDecimal saveMoney) {
        this.saveMoney = saveMoney;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public BigDecimal getFightGroupPrice() {
        return fightGroupPrice;
    }

    public void setFightGroupPrice(BigDecimal fightGroupPrice) {
        this.fightGroupPrice = fightGroupPrice;
    }

    public Long getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Long invalidTime) {
        this.invalidTime = invalidTime;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    public Long getPmInfoId() {
        return pmInfoId;
    }

    public void setPmInfoId(Long pmInfoId) {
        this.pmInfoId = pmInfoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
