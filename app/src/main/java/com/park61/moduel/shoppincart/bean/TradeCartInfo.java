package com.park61.moduel.shoppincart.bean;

import java.util.List;

public class TradeCartInfo {
	private List<GoodsItem> bags;
	private int cartItemNum;// 商品数量
	private int isChecked;// 购物车是否选中 0-未选中 1-已选中
	private boolean isEmpty;// 购物车是否为空 true-空 false-非空
	private double totalAmount;// 购物车总金额
	private double totalSaveAmount;// 购物车总节省金额
	private int userId;// 用户id

	public List<GoodsItem> getBags() {
		return bags;
	}

	public void setBags(List<GoodsItem> bags) {
		this.bags = bags;
	}

	public int getCartItemNum() {
		return cartItemNum;
	}

	public void setCartItemNum(int cartItemNum) {
		this.cartItemNum = cartItemNum;
	}

	public int getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(int isChecked) {
		this.isChecked = isChecked;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getTotalSaveAmount() {
		return totalSaveAmount;
	}

	public void setTotalSaveAmount(double totalSaveAmount) {
		this.totalSaveAmount = totalSaveAmount;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
