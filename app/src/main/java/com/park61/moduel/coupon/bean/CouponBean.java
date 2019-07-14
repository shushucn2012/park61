package com.park61.moduel.coupon.bean;

/**
 * Created by shubei on 2018/1/26.
 */

public class CouponBean {

    /**
     * id : 133
     * userId : null
     * childId : null
     * receiveTime : null
     * limitType : 1
     * startTime : 1516935885000
     * endTime : 1548990286000
     * useTime : null
     * status : null
     * orderId : null
     * code : null
     * ruleId : null
     * officeId : null
     * activityApplyId : null
     * writeOffUser : null
     * writeOffTime : null
     * updateTime : null
     * updateBy : null
     * remarks : 童子满10元减1.98
     * type : null
     * checkCoupon : 0
     * title : 童子满10元减1.98
     * ruleValue2 : 2
     * showTime : 2018.01.26--2019.02.01
     */

    private int id;//用户优惠券id
    private String remarks;//备注
    private String title;//标题
    private String ruleValue2;//优惠金额
    private String showTime;//展示时间
    private String showPrice;//展示金额
    private boolean isChosen;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRuleValue2() {
        return ruleValue2;
    }

    public void setRuleValue2(String ruleValue2) {
        this.ruleValue2 = ruleValue2;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public String getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(String showPrice) {
        this.showPrice = showPrice;
    }
}
