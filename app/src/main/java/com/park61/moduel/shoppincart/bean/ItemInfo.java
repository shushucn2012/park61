package com.park61.moduel.shoppincart.bean;

public class ItemInfo {
	private int availableStockNum;// 可用库存
	private int isChecked;// 单个商品是否选中 0-否 1-是
	private String itemAmount;// 商品金额
	private String itemId;// 购物车明细id
	private String itemPrice;// 商品单价
	private int itemType;// 购物车明细类型 1-非促销 2-促销
	private String marketPrice;// 市场价
	private int num;// 购买数量
	private long pmInfoId;// 商品id
	private String productCname;// 商品中文名称
	private String productPicUrl;// 商品图片
	private String productColor;// 商品颜色
	private String productSize;// 商品尺寸
	private String productType;// 产品类型
	private String saleType;// 销售类型
	private long promotionId;
	

	public long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(long promotionId) {
		this.promotionId = promotionId;
	}

	public int getAvailableStockNum() {
		return availableStockNum;
	}

	public void setAvailableStockNum(int availableStockNum) {
		this.availableStockNum = availableStockNum;
	}

	public int getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(int isChecked) {
		this.isChecked = isChecked;
	}

	public String getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(String itemAmount) {
		this.itemAmount = itemAmount;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public long getPmInfoId() {
		return pmInfoId;
	}

	public void setPmInfoId(long pmInfoId) {
		this.pmInfoId = pmInfoId;
	}

	public String getProductCname() {
		return productCname;
	}

	public void setProductCname(String productCname) {
		this.productCname = productCname;
	}

	public String getProductPicUrl() {
		return productPicUrl;
	}

	public void setProductPicUrl(String productPicUrl) {
		this.productPicUrl = productPicUrl;
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

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

}
