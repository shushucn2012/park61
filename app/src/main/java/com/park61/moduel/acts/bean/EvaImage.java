package com.park61.moduel.acts.bean;

import java.io.Serializable;

public class EvaImage implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4277614381615516228L;

	private String createTime;// 时间
	private Long evaluateDetailId;// 评价id
	private Long id;// 记录id
	private String pictureUrl;// 图片url

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Long getEvaluateDetailId() {
		return evaluateDetailId;
	}

	public void setEvaluateDetailId(Long evaluateDetailId) {
		this.evaluateDetailId = evaluateDetailId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

}
