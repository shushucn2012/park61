package com.park61.moduel.order.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 订单详情信息
 */
public class OrderDetailBean implements Serializable{
    private BigDecimal couponAmount;//优惠券金额
    private BigDecimal discountAmount;//抵扣券金额
    private String goodReceiverAddress;//收货地址
    private String goodReceiverProvince;//收货省份
    private String goodReceiverCity;//收货城市
    private String goodReceiverCounty;//收货区域
    private String goodReceiverTown;//乡镇名称
    private String goodReceiverMobile;//收货人电话
    private String goodReceiverName;//收货人姓名
    private long id;//订单id
    private ArrayList<MerchantOrderBean> merchantOrder;
    private BigDecimal orderAmount;//订单总额
    private String orderCancelReason;//订单取消原因
    private String orderCreateTime;//下单时间
    private String orderDeliveryFee;//运费
    private int orderStatus;//订单状态
    private String orderStatusName;//订单状态名称
    private BigDecimal orderToPayAmount;// 实际支付或待支付金额
    private int orderType;//1：普通订单，2：拼团订单，3：会员订单
    private BigDecimal productAmount;//产品总额
    private boolean returnFlag;//true:付款后可以取消订单,false:款后不能取消订单
    private Long fightGroupOpenId;//拼团id
    private int fightGroupOpensStatus;

    public String getGoodReceiverTown() {
        return goodReceiverTown;
    }

    public void setGoodReceiverTown(String goodReceiverTown) {
        this.goodReceiverTown = goodReceiverTown;
    }

    public Long getFightGroupOpenId() {
        return fightGroupOpenId;
    }

    public void setFightGroupOpenId(Long fightGroupOpenId) {
        this.fightGroupOpenId = fightGroupOpenId;
    }

    public int getFightGroupOpensStatus() {
        return fightGroupOpensStatus;
    }

    public void setFightGroupOpensStatus(int fightGroupOpensStatus) {
        this.fightGroupOpensStatus = fightGroupOpensStatus;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderCancelReason() {
        return orderCancelReason;
    }

    public void setOrderCancelReason(String orderCancelReason) {
        this.orderCancelReason = orderCancelReason;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getOrderDeliveryFee() {
        return orderDeliveryFee;
    }

    public void setOrderDeliveryFee(String orderDeliveryFee) {
        this.orderDeliveryFee = orderDeliveryFee;
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

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
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

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getGoodReceiverAddress() {
        return goodReceiverAddress;
    }

    public void setGoodReceiverAddress(String goodReceiverAddress) {
        this.goodReceiverAddress = goodReceiverAddress;
    }

    public String getGoodReceiverProvince() {
        return goodReceiverProvince;
    }

    public void setGoodReceiverProvince(String goodReceiverProvince) {
        this.goodReceiverProvince = goodReceiverProvince;
    }

    public String getGoodReceiverCity() {
        return goodReceiverCity;
    }

    public void setGoodReceiverCity(String goodReceiverCity) {
        this.goodReceiverCity = goodReceiverCity;
    }

    public String getGoodReceiverCounty() {
        return goodReceiverCounty;
    }

    public void setGoodReceiverCounty(String goodReceiverCounty) {
        this.goodReceiverCounty = goodReceiverCounty;
    }

    public String getGoodReceiverMobile() {
        return goodReceiverMobile;
    }

    public void setGoodReceiverMobile(String goodReceiverMobile) {
        this.goodReceiverMobile = goodReceiverMobile;
    }

    public String getGoodReceiverName() {
        return goodReceiverName;
    }

    public void setGoodReceiverName(String goodReceiverName) {
        this.goodReceiverName = goodReceiverName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<MerchantOrderBean> getMerchantOrder() {
        return merchantOrder;
    }

    public void setMerchantOrder(ArrayList<MerchantOrderBean> merchantOrder) {
        this.merchantOrder = merchantOrder;
    }
}
