package com.park61.moduel.sales.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 限时秒杀页面实体类
 * Created by super on 2016/9/12.
 */
public class PromotionPageBean implements Serializable {

    private String id;
    private String start;
    private String end;
    private String countDownTime;
    private String isCurrent;
    private String isStart;
    private ArrayList<ProductLimit> productLimit;
    private String msg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCountDownTime() {
        return countDownTime;
    }

    public void setCountDownTime(String countDownTime) {
        this.countDownTime = countDownTime;
    }

    public String getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(String isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getIsStart() {
        return isStart;
    }

    public void setIsStart(String isStart) {
        this.isStart = isStart;
    }

    public ArrayList<ProductLimit> getProductLimit() {
        return productLimit;
    }

    public void setProductLimit(ArrayList<ProductLimit> productLimit) {
        this.productLimit = productLimit;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
