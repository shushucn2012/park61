package com.park61.moduel.sales.bean;

import java.io.Serializable;

public class PromotionType implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7524189504348950556L;

	// id
	private Long id;
	// 特卖类型
	private String promotionType;
	// 特卖类型图片
	private String picUrl;
	// 特卖类型图片
	private String promotionName;
	// 特卖描述
	private String promotionDescription;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public String getPromotionDescription() {
		return promotionDescription;
	}

	public void setPromotionDescription(String promotionDescription) {
		this.promotionDescription = promotionDescription;
	}

}
