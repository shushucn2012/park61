package com.park61.moduel.sales.bean;

import java.util.List;

public class TradeCartOrderInfo {
	// 默认收货地址
	private GoodReceiverVO goodReceiver;
	private int isSplit;// 1-拆单 2-不拆单
	// 订单金额=商品总金额+运费
	private double orderAmount;
	private double productAmount;// 商品金额
	private double orderDeliveryFee;
	private double couponAmount;
	private double orderToPayAmount;// 订单待支付金额
	private long couponUseId;// 优惠券与用户关系id
	private int couponCount;// 可用优惠券张数
	// 订单明细
	private List<TradeOrderItem> orderItemList;
	// 拆单列表
	private List<TradeCartOrderInfo> childOrders;

	public double getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(double productAmount) {
		this.productAmount = productAmount;
	}

	public GoodReceiverVO getGoodReceiver() {
		return goodReceiver;
	}

	public void setGoodReceiver(GoodReceiverVO goodReceiver) {
		this.goodReceiver = goodReceiver;
	}

	public int getIsSplit() {
		return isSplit;
	}

	public void setIsSplit(int isSplit) {
		this.isSplit = isSplit;
	}

	public double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public double getOrderDeliveryFee() {
		return orderDeliveryFee;
	}

	public void setOrderDeliveryFee(double orderDeliveryFee) {
		this.orderDeliveryFee = orderDeliveryFee;
	}

	public double getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(double couponAmount) {
		this.couponAmount = couponAmount;
	}

	public double getOrderToPayAmount() {
		return orderToPayAmount;
	}

	public void setOrderToPayAmount(double orderToPayAmount) {
		this.orderToPayAmount = orderToPayAmount;
	}

	public long getCouponUseId() {
		return couponUseId;
	}

	public void setCouponUseId(long couponUseId) {
		this.couponUseId = couponUseId;
	}

	public int getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(int couponCount) {
		this.couponCount = couponCount;
	}

	public List<TradeOrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<TradeOrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public List<TradeCartOrderInfo> getChildOrders() {
		return childOrders;
	}

	public void setChildOrders(List<TradeCartOrderInfo> childOrders) {
		this.childOrders = childOrders;
	}

}
