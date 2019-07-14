package com.park61.moduel.sales.bean;

import com.park61.moduel.acts.bean.Entity;

public class GoodsCombine extends Entity{
	private MySalesTemplete templete;
	private ProductLimit goodsLeft;
	private ProductLimit goodsRight;

	public GoodsCombine() {

	}

	public GoodsCombine(ProductLimit goodsLeft, ProductLimit goodsRight) {
		this.setGoodsLeft(goodsLeft);
		this.setGoodsRight(goodsRight);
	}

	public ProductLimit getGoodsLeft() {
		return goodsLeft;
	}

	public void setGoodsLeft(ProductLimit goodsLeft) {
		this.goodsLeft = goodsLeft;
	}

	public ProductLimit getGoodsRight() {
		return goodsRight;
	}

	public void setGoodsRight(ProductLimit goodsRight) {
		this.goodsRight = goodsRight;
	}

	public MySalesTemplete getTemplete() {
		return templete;
	}

	public void setTemplete(MySalesTemplete templete) {
		this.templete = templete;
	}

}
