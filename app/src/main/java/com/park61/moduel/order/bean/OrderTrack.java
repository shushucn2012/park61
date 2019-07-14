package com.park61.moduel.order.bean;

import java.io.Serializable;

/**
 * 订单物流追踪信息
 */
public class OrderTrack implements Serializable {
    private long orderId;//订单id
    private String processContent;//进度内容
    private String processCreateTime;//进度时间

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getProcessContent() {
        return processContent;
    }

    public void setProcessContent(String processContent) {
        this.processContent = processContent;
    }

    public String getProcessCreateTime() {
        return processCreateTime;
    }

    public void setProcessCreateTime(String processCreateTime) {
        this.processCreateTime = processCreateTime;
    }
}
