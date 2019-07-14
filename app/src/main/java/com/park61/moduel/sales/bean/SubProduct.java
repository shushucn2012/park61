package com.park61.moduel.sales.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class SubProduct implements Serializable {

	private static final long serialVersionUID = 3796469550928383356L;

	// 商品ID
	private Long pmInfoId;

	// 颜色
	private String productColor;
	// 尺码
	private String productSize;
	// 真是库存
	private Long availableStock;
	// 市场价
	private BigDecimal marketPrice;
	// 当前统一售价
	private BigDecimal currentUnifyPrice;
	// 促销ID
	private Long promId;
	// 促销价格
	private BigDecimal promPrice;
	// 颜色标识
	private int colorRelationIndex;
	// 尺寸标识
	private int sizeRelationIndex;
	// 图片地址
	private String picUrl;
	// 前台页面用于是否选择的标识
	private Integer index;
	// 当前子商品是否可选
	private boolean flag;

	// 尺码集合
	private List<SubProduct> subProductSizeList;

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

	public Long getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Long availableStock) {
		this.availableStock = availableStock;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getCurrentUnifyPrice() {
		return currentUnifyPrice;
	}

	public void setCurrentUnifyPrice(BigDecimal currentUnifyPrice) {
		this.currentUnifyPrice = currentUnifyPrice;
	}

	public Long getPmInfoId() {
		return pmInfoId;
	}

	public void setPmInfoId(Long pmInfoId) {
		this.pmInfoId = pmInfoId;
	}

	public List<SubProduct> getSubProductSizeList() {
		return subProductSizeList;
	}

	public void setSubProductSizeList(List<SubProduct> subProductSizeList) {
		this.subProductSizeList = subProductSizeList;
	}

	public Long getPromId() {
		return promId;
	}

	public void setPromId(Long promId) {
		this.promId = promId;
	}

	public BigDecimal getPromPrice() {
		return promPrice;
	}

	public void setPromPrice(BigDecimal promPrice) {
		this.promPrice = promPrice;
	}

	public int getColorRelationIndex() {
		return colorRelationIndex;
	}

	public void setColorRelationIndex(int colorRelationIndex) {
		this.colorRelationIndex = colorRelationIndex;
	}

	public int getSizeRelationIndex() {
		return sizeRelationIndex;
	}

	public void setSizeRelationIndex(int sizeRelationIndex) {
		this.sizeRelationIndex = sizeRelationIndex;
	}

}
