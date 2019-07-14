package com.park61.moduel.order.bean;

import java.io.Serializable;

/**
 * 物流信息
 */
public class EmsMessage implements Serializable {
    private long id;//订单id
    private int merchantId;//商家id,2:普通商家，3：京东商家
    private long orderDeliveryCode;//运单号
    private String orderDeliveryCompany;//快递公司名
    private int orderStatus;//订单状态
    private String orderStatusName;

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }


    public long getOrderDeliveryCode() {
        return orderDeliveryCode;
    }

    public void setOrderDeliveryCode(long orderDeliveryCode) {
        this.orderDeliveryCode = orderDeliveryCode;
    }

    public String getOrderDeliveryCompany() {
        return orderDeliveryCompany;
    }

    public void setOrderDeliveryCompany(String orderDeliveryCompany) {
        this.orderDeliveryCompany = orderDeliveryCompany;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }
}
