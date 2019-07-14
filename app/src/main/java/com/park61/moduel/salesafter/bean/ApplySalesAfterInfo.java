package com.park61.moduel.salesafter.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 售后退货参数信息
 * 
 * @author Lucia
 * 
 */
public class ApplySalesAfterInfo implements Serializable {
	private long updateTime;
	private String closeReason;//售后关闭原因;
	private long soId;// 订单id
	private long grfApplyTime;// 退货申请时间
	private long refundTime;// 退款时间
	private long waybillTime;// 回寄时间
	private long refundWay;// 退款方式：payment_method表id
	private long endUserId;// 用户id
	private long id;// 退货单id
	private int grfType;// 退货类型1到9
	private String remark;// 退货备注
	private int isReturnGoods;// 是否寄回实物：0、寄回，1、不寄回
	private float givebackItemAmount;// 退回总金额
	private float givebackItemPrice;// 退回单价
	private float grfDeliveryFee;// 运费
	private float pmAmount;// 商品金额
	private float grfAmount;// 退货金额：商品金额+运费
	private int returnGoodsNum;// 退回个数
	private float actualRefundAmount;// 实退金额
	private String waybillCode;// 快递单号
	private int waybillType;// 退货类型：0：快递退货 1：退回到自提点
	private int auditResults;// 审核结果：0、通过，1、不通过
	private int grfStatus;// 退款状态: 0、申请退货，1、退货审核，2、提交货品，3、货品入库，4、系统打款，5、订单关闭
	private String grfReason;// 退货原因
	private String grfCode;// 退货单编码
	private String deliveryCompanyName;// 快递名称
	private String storePhone;//门店电话
	private String storeAddress;//门店地址
	private ArrayList<ShowPicsInfo> pics;
	public ArrayList<GrfGoodsInfoBean> items;

	public ArrayList<GrfGoodsInfoBean> getItems() {
		return items;
	}

	public void setItems(ArrayList<GrfGoodsInfoBean> items) {
		this.items = items;
	}

	public String getCloseReason() {
		return closeReason;
	}

	public void setCloseReason(String closeReason) {
		this.closeReason = closeReason;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getStorePhone() {
		return storePhone;
	}

	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public ArrayList<ShowPicsInfo> getPics() {
		return pics;
	}

	public void setPics(ArrayList<ShowPicsInfo> pics) {
		this.pics = pics;
	}

	public long getSoId() {
		return soId;
	}

	public void setSoId(long soId) {
		this.soId = soId;
	}

	public long getGrfApplyTime() {
		return grfApplyTime;
	}

	public void setGrfApplyTime(long grfApplyTime) {
		this.grfApplyTime = grfApplyTime;
	}

	public long getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(long refundTime) {
		this.refundTime = refundTime;
	}

	public long getWaybillTime() {
		return waybillTime;
	}

	public void setWaybillTime(long waybillTime) {
		this.waybillTime = waybillTime;
	}

	public long getRefundWay() {
		return refundWay;
	}

	public void setRefundWay(long refundWay) {
		this.refundWay = refundWay;
	}

	public long getEndUserId() {
		return endUserId;
	}

	public void setEndUserId(long endUserId) {
		this.endUserId = endUserId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getGrfType() {
		return grfType;
	}

	public void setGrfType(int grfType) {
		this.grfType = grfType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getIsReturnGoods() {
		return isReturnGoods;
	}

	public void setIsReturnGoods(int isReturnGoods) {
		this.isReturnGoods = isReturnGoods;
	}

	public float getGivebackItemAmount() {
		return givebackItemAmount;
	}

	public void setGivebackItemAmount(float givebackItemAmount) {
		this.givebackItemAmount = givebackItemAmount;
	}

	public float getGivebackItemPrice() {
		return givebackItemPrice;
	}

	public void setGivebackItemPrice(float givebackItemPrice) {
		this.givebackItemPrice = givebackItemPrice;
	}

	public float getGrfDeliveryFee() {
		return grfDeliveryFee;
	}

	public void setGrfDeliveryFee(float grfDeliveryFee) {
		this.grfDeliveryFee = grfDeliveryFee;
	}

	public float getPmAmount() {
		return pmAmount;
	}

	public void setPmAmount(float pmAmount) {
		this.pmAmount = pmAmount;
	}

	public float getGrfAmount() {
		return grfAmount;
	}

	public void setGrfAmount(float grfAmount) {
		this.grfAmount = grfAmount;
	}

	public int getReturnGoodsNum() {
		return returnGoodsNum;
	}

	public void setReturnGoodsNum(int returnGoodsNum) {
		this.returnGoodsNum = returnGoodsNum;
	}

	public float getActualRefundAmount() {
		return actualRefundAmount;
	}

	public void setActualRefundAmount(float actualRefundAmount) {
		this.actualRefundAmount = actualRefundAmount;
	}

	public String getWaybillCode() {
		return waybillCode;
	}

	public void setWaybillCode(String waybillCode) {
		this.waybillCode = waybillCode;
	}

	public int getWaybillType() {
		return waybillType;
	}

	public void setWaybillType(int waybillType) {
		this.waybillType = waybillType;
	}

	public int getAuditResults() {
		return auditResults;
	}

	public void setAuditResults(int auditResults) {
		this.auditResults = auditResults;
	}

	public int getGrfStatus() {
		return grfStatus;
	}

	public void setGrfStatus(int grfStatus) {
		this.grfStatus = grfStatus;
	}

	public String getGrfReason() {
		return grfReason;
	}

	public void setGrfReason(String grfReason) {
		this.grfReason = grfReason;
	}

	public String getGrfCode() {
		return grfCode;
	}

	public void setGrfCode(String grfCode) {
		this.grfCode = grfCode;
	}

	public String getDeliveryCompanyName() {
		return deliveryCompanyName;
	}

	public void setDeliveryCompanyName(String deliveryCompanyName) {
		this.deliveryCompanyName = deliveryCompanyName;
	}

}
