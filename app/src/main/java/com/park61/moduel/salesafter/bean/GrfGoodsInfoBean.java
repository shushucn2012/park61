package com.park61.moduel.salesafter.bean;

import java.io.Serializable;

public class GrfGoodsInfoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	public long soId;// 订单id
	public long grfApplyTime;// 退货申请时间
	public float givebackItemAmount;// 退回总金额
	public float givebackItemPrice;// 退回单价
	public int givebackItemNum;// 退回个数
	public String productPicUrl;// 产品图片链接
	public String productCname;// 产品中文名
	public String productColor;// 颜色
	public String productSize;// 尺寸
	public float beforeAmount;// 之前的价格
	private long pmInfoId;//商品id
	private int isReturnGoods;
	private long grfId;//退货id

	public int getIsReturnGoods() {
		return isReturnGoods;
	}

	public void setIsReturnGoods(int isReturnGoods) {
		this.isReturnGoods = isReturnGoods;
	}

	public long getGrfId() {
		return grfId;
	}

	public void setGrfId(long grfId) {
		this.grfId = grfId;
	}

	public long getPmInfoId() {
		return pmInfoId;
	}

	public void setPmInfoId(long pmInfoId) {
		this.pmInfoId = pmInfoId;
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

	public int getGivebackItemNum() {
		return givebackItemNum;
	}

	public void setGivebackItemNum(int givebackItemNum) {
		this.givebackItemNum = givebackItemNum;
	}

	public String getProductPicUrl() {
		return productPicUrl;
	}

	public void setProductPicUrl(String productPicUrl) {
		this.productPicUrl = productPicUrl;
	}

	public String getProductCname() {
		return productCname;
	}

	public void setProductCname(String productCname) {
		this.productCname = productCname;
	}

	public String getProductColor() {
		return productColor;
	}

	public void setProductColor(String productColor) {
		this.productColor = productColor;
	}

	public String getProductSize() {
		return productSize;
	}

	public void setProductSize(String productSize) {
		this.productSize = productSize;
	}

	public float getBeforeAmount() {
		return beforeAmount;
	}

	public void setBeforeAmount(float beforeAmount) {
		this.beforeAmount = beforeAmount;
	}

}
