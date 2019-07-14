package com.park61.moduel.order.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 包裹订单信息
 */
public class OrderBean implements Serializable{
    private BigDecimal couponAmount;
    private long id;//订单id
    private int isEvaluated;//1：已评论，0：未评论
    private int merchantId;//商家id
    private BigDecimal orderAmount;
    private String orderCreateTime;//订单时间
    private int orderItemNum;//// 商品数量
    public  int orderStatus;//订单状态
    private String orderStatusName;//状态名称
    private BigDecimal orderToPayAmount;// 实际支付或待支付金额
    private BigDecimal productAmount;//产品金额
    private double returnAmount;//退货金额
    public boolean returnFlag;
    private ArrayList<GoodsInfoBean> items;
    // 分包子单
    private List<OrderBean> childOrders;

    public double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(double returnAmount) {
        this.returnAmount = returnAmount;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIsEvaluated() {
        return isEvaluated;
    }

    public void setIsEvaluated(int isEvaluated) {
        this.isEvaluated = isEvaluated;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public int getOrderItemNum() {
        return orderItemNum;
    }

    public void setOrderItemNum(int orderItemNum) {
        this.orderItemNum = orderItemNum;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public BigDecimal getOrderToPayAmount() {
        return orderToPayAmount;
    }

    public void setOrderToPayAmount(BigDecimal orderToPayAmount) {
        this.orderToPayAmount = orderToPayAmount;
    }

    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
    }

    public boolean isReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
    }

    public ArrayList<GoodsInfoBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<GoodsInfoBean> items) {
        this.items = items;
    }

    public List<OrderBean> getChildOrders() {
        return childOrders;
    }

    public void setChildOrders(List<OrderBean> childOrders) {
        this.childOrders = childOrders;
    }
}
