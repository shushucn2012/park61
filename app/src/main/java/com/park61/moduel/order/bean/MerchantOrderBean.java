package com.park61.moduel.order.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商家订单信息
 */
public class MerchantOrderBean implements Serializable {
    private long merchantId;//商家id
    private String merchantName;//商家名称
    private BigDecimal orderAmount;//小计
    private BigDecimal orderDeliveryFee;//运费
    private BigDecimal productAmount;//商品总价
    private String deliveryRemark;//订单备注
    private ArrayList<OrderBean> bagsList;
    public int orderStatus;//订单状态：1 已下单:货款未全收, 2 已下单:货款已全收,3 已转DO,4 已出库（货在途）
    //5 货物用户已收货,6 送货失败（其它）,7 要求退货,8 退货中 ,9 退货完成 ,10 订单取消,11 订单完成
    public int isEvaluated;//1：已评论，0：未评论
    private long id;//订单id
    private int orderItemNum;// 商品数量
    private int packageNum;// 包裹数
    private List<OrderBean> childOrders;
    private List<String> picUrlList;

    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
    }

    public int getIsEvaluated() {
        return isEvaluated;
    }

    public void setIsEvaluated(int isEvaluated) {
        this.isEvaluated = isEvaluated;
    }

    public List<String> getPicUrlList() {
        return picUrlList;
    }

    public void setPicUrlList(List<String> picUrlList) {
        this.picUrlList = picUrlList;
    }

    public List<OrderBean> getChildOrders() {
        return childOrders;
    }

    public void setChildOrders(List<OrderBean> childOrders) {
        this.childOrders = childOrders;
    }

    public int getOrderItemNum() {
        return orderItemNum;
    }

    public void setOrderItemNum(int orderItemNum) {
        this.orderItemNum = orderItemNum;
    }

    public int getPackageNum() {
        return packageNum;
    }

    public void setPackageNum(int packageNum) {
        this.packageNum = packageNum;
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

    public ArrayList<OrderBean> getBagsList() {
        return bagsList;
    }

    public void setBagsList(ArrayList<OrderBean> bagsList) {
        this.bagsList = bagsList;
    }

    public String getDeliveryRemark() {
        return deliveryRemark;
    }

    public void setDeliveryRemark(String deliveryRemark) {
        this.deliveryRemark = deliveryRemark;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderDeliveryFee() {
        return orderDeliveryFee;
    }

    public void setOrderDeliveryFee(BigDecimal orderDeliveryFee) {
        this.orderDeliveryFee = orderDeliveryFee;
    }

}
