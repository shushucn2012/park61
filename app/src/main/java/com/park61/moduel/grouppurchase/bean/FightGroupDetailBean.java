package com.park61.moduel.grouppurchase.bean;

import java.math.BigDecimal;
import java.util.ArrayList;

public class FightGroupDetailBean {
    private String fightGroupDetailId;//详情id
    private int id;//开团id
    private int joinedNum;//参团人数
    private String name;//商品名称
    private BigDecimal oldPrice;//原价
    private String openTime;//开团时间
    private String pic;//团图片
    private ArrayList<FightGroupOpenDetails> fightGroupOpenDetails;
    private BigDecimal fightGroupPrice;//拼团价
    private int personNum;//几人团
    private int status;//1-未成团 2-已成团
    private String title;//团标题
    private String countDown;//倒计时
    private String pmInfoId;//商品id

    public String getPmInfoId() {
        return pmInfoId;
    }

    public void setPmInfoId(String pmInfoId) {
        this.pmInfoId = pmInfoId;
    }

    public BigDecimal getFightGroupPrice() {
        return fightGroupPrice;
    }

    public void setFightGroupPrice(BigDecimal fightGroupPrice) {
        this.fightGroupPrice = fightGroupPrice;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountDown() {
        return countDown;
    }

    public void setCountDown(String countDown) {
        this.countDown = countDown;
    }

    public String getFightGroupDetailId() {
        return fightGroupDetailId;
    }

    public void setFightGroupDetailId(String fightGroupDetailId) {
        this.fightGroupDetailId = fightGroupDetailId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJoinedNum() {
        return joinedNum;
    }

    public void setJoinedNum(int joinedNum) {
        this.joinedNum = joinedNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public ArrayList<FightGroupOpenDetails> getFightGroupOpenDetails() {
        return fightGroupOpenDetails;
    }

    public void setFightGroupOpenDetails(ArrayList<FightGroupOpenDetails> fightGroupOpenDetails) {
        this.fightGroupOpenDetails = fightGroupOpenDetails;
    }
}
