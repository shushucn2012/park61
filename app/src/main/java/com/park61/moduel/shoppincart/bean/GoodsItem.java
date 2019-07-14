package com.park61.moduel.shoppincart.bean;

import java.util.List;

public class GoodsItem {
	private int isChecked;// 收货地址
	private List<ItemInfo> items;
	private long merchantId;// 商家id
	private String merchantName;// 商家名称
	private int merchantType;// 商家类型 0-61区 1-商家
	private String totalAmount;// 购物袋总金额

	public int getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(int isChecked) {
		this.isChecked = isChecked;
	}

	public List<ItemInfo> getItems() {
		return items;
	}

	public void setItems(List<ItemInfo> items) {
		this.items = items;
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

	public int getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(int merchantType) {
		this.merchantType = merchantType;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

}
