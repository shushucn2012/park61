package com.park61.moduel.salesafter.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class GrfOrderInfoBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ArrayList<GrfGoodsInfoBean> items;
	private long grfApplyTime;// 退货申请时间
	private int grfStatus;// 退款状态: 0、申请退货，1、退货审核，2、提交货品，3、货品入库，4、系统打款，5、订单关闭
	private float actualRefundAmount;// 实退金额
	private float grfAmount;// 退货金额：商品金额+运费
	private long soId;// 订单id;
	private ArrayList<String> productPicUrl;
	private BigDecimal orderAmount;//交易金额
	private int returnGoodsNum;//退货商品数量
	private String grfStatusName;//状态名
	private String grfCode;//退货单编号

	public String getGrfStatusName() {
		return grfStatusName;
	}

	public void setGrfStatusName(String grfStatusName) {
		this.grfStatusName = grfStatusName;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public int getReturnGoodsNum() {
		return returnGoodsNum;
	}

	public void setReturnGoodsNum(int returnGoodsNum) {
		this.returnGoodsNum = returnGoodsNum;
	}

	public ArrayList<String> getProductPicUrl() {
		return productPicUrl;
	}

	public void setProductPicUrl(ArrayList<String> productPicUrl) {
		this.productPicUrl = productPicUrl;
	}

	public float getActualRefundAmount() {
		return actualRefundAmount;
	}

	public void setActualRefundAmount(float actualRefundAmount) {
		this.actualRefundAmount = actualRefundAmount;
	}

	public ArrayList<GrfGoodsInfoBean> getItems() {
		return items;
	}

	public void setItems(ArrayList<GrfGoodsInfoBean> items) {
		this.items = items;
	}

	public long getGrfApplyTime() {
		return grfApplyTime;
	}

	public void setGrfApplyTime(long grfApplyTime) {
		this.grfApplyTime = grfApplyTime;
	}

	public int getGrfStatus() {
		return grfStatus;
	}

	public void setGrfStatus(int grfStatus) {
		this.grfStatus = grfStatus;
	}

	public float getGrfAmount() {
		return grfAmount;
	}

	public void setGrfAmount(float grfAmount) {
		this.grfAmount = grfAmount;
	}

	public long getSoId() {
		return soId;
	}

	public void setSoId(long soId) {
		this.soId = soId;
	}

	public String getGrfCode() {
		return grfCode;
	}

	public void setGrfCode(String grfCode) {
		this.grfCode = grfCode;
	}

}
