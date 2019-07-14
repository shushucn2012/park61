package com.park61.moduel.me.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by HP on 2016/8/27.
 */
public class MeInfo implements Serializable {
    private BigDecimal amount;//余额
    private int toPaymentActivityNum;//代付款的游戏数
    private int appliedActivityNum;//已报名游戏数
    private int couponNum;//优惠券数量
    private int gameNum;
    private int isMotherPartner;//是否是妈妈合伙人
    private int isValidMember;//是否是会员
    private int messageNum;
    private int myChildNum;
    private int paymentGoodsNum;
    private int storeNum;
    private String outHave;//提现余额
    private int myDynamicNum;
    private int myFansNum;
    private int myFocusesNum;
    private int myCollectNum;

    public String getOutHave() {
        return outHave;
    }

    public void setOutHave(String outHave) {
        this.outHave = outHave;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getToPaymentActivityNum() {
        return toPaymentActivityNum;
    }

    public void setToPaymentActivityNum(int toPaymentActivityNum) {
        this.toPaymentActivityNum = toPaymentActivityNum;
    }

    public int getAppliedActivityNum() {
        return appliedActivityNum;
    }

    public void setAppliedActivityNum(int appliedActivityNum) {
        this.appliedActivityNum = appliedActivityNum;
    }

    public int getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(int couponNum) {
        this.couponNum = couponNum;
    }

    public int getGameNum() {
        return gameNum;
    }

    public void setGameNum(int gameNum) {
        this.gameNum = gameNum;
    }

    public int getIsMotherPartner() {
        return isMotherPartner;
    }

    public void setIsMotherPartner(int isMotherPartner) {
        this.isMotherPartner = isMotherPartner;
    }

    public int getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(int messageNum) {
        this.messageNum = messageNum;
    }

    public int getMyChildNum() {
        return myChildNum;
    }

    public void setMyChildNum(int myChildNum) {
        this.myChildNum = myChildNum;
    }

    public int getPaymentGoodsNum() {
        return paymentGoodsNum;
    }

    public void setPaymentGoodsNum(int paymentGoodsNum) {
        this.paymentGoodsNum = paymentGoodsNum;
    }

    public int getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(int storeNum) {
        this.storeNum = storeNum;
    }

    public int getIsValidMember() {
        return isValidMember;
    }

    public void setIsValidMember(int isValidMember) {
        this.isValidMember = isValidMember;
    }

    public int getMyDynamicNum() {
        return myDynamicNum;
    }

    public void setMyDynamicNum(int myDynamicNum) {
        this.myDynamicNum = myDynamicNum;
    }

    public int getMyFansNum() {
        return myFansNum;
    }

    public void setMyFansNum(int myFansNum) {
        this.myFansNum = myFansNum;
    }

    public int getMyFocusesNum() {
        return myFocusesNum;
    }

    public void setMyFocusesNum(int myFocusesNum) {
        this.myFocusesNum = myFocusesNum;
    }

    public int getMyCollectNum() {
        return myCollectNum;
    }

    public void setMyCollectNum(int myCollectNum) {
        this.myCollectNum = myCollectNum;
    }
}
