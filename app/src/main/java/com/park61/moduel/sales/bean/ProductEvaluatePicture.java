package com.park61.moduel.sales.bean;

import java.io.Serializable;

public class ProductEvaluatePicture implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3727671772752619985L;
	// 主键ID
	private Long id;
	// 评价ID
	private Long evaluateId;
	// 评价图片地址
	private String pictureUrl;
	// 创建时间
	private String createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEvaluateId() {
		return evaluateId;
	}

	public void setEvaluateId(Long evaluateId) {
		this.evaluateId = evaluateId;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
