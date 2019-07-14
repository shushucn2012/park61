package com.park61.moduel.sales.bean;

import java.io.Serializable;
import java.util.List;

public class ProductEvaluate implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3727671772752619985L;
	// 主键ID
	private Long id;
	// 产品ID
	private Long productId;
	// 商品ID
	private Long pmInfoId;
	// 描述相符分数
	private Double conformityScore;
	// 服务态度分数
	private Double serveScore;
	// 发货速度分数
	private Double deliveryScore;
	// 综合评分
	private Double compositeScore;
	// 评价内容
	private String content;
	// 评价人id
	private Long endUserId;
	// 创建时间
	private String createTime;
	// 是否匿名(0:是;1:否)
	private Integer isAnonymous;
	// 主系列图片地址
	private String picUrl;
	// 评价人名称
	private String userName;

	// 主系列的评价数
	private Long evaluateCount;

	// 评价图片地址
	private List<ProductEvaluatePicture> picList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getPmInfoId() {
		return pmInfoId;
	}

	public void setPmInfoId(Long pmInfoId) {
		this.pmInfoId = pmInfoId;
	}

	public Double getConformityScore() {
		return (conformityScore != null && !conformityScore.equals(0)) ? conformityScore
				: ConstantProduct.PRODUCT_COMPOSITE_SCORE;
	}

	public void setConformityScore(Double conformityScore) {
		this.conformityScore = conformityScore;
	}

	public Double getServeScore() {
		return (serveScore != null && !serveScore.equals(0)) ? serveScore
				: ConstantProduct.PRODUCT_SERVE_SCORE;
	}

	public void setServeScore(Double serveScore) {
		this.serveScore = serveScore;
	}

	public Double getDeliveryScore() {
		return (deliveryScore != null && !deliveryScore.equals(0)) ? deliveryScore
				: ConstantProduct.PRODUCT_DELIVERY_SCORE;
	}

	public void setDeliveryScore(Double deliveryScore) {
		this.deliveryScore = deliveryScore;
	}

	public Double getCompositeScore() {
		return (compositeScore != null && !compositeScore.equals(0)) ? compositeScore
				: ConstantProduct.PRODUCT_COMPOSITE_SCORE;
	}

	public void setCompositeScore(Double compositeScore) {
		this.compositeScore = compositeScore;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getEndUserId() {
		return endUserId;
	}

	public void setEndUserId(Long endUserId) {
		this.endUserId = endUserId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getEvaluateCount() {
		return evaluateCount;
	}

	public void setEvaluateCount(Long evaluateCount) {
		this.evaluateCount = evaluateCount;
	}

	public List<ProductEvaluatePicture> getPicList() {
		return picList;
	}

	public void setPicList(List<ProductEvaluatePicture> picList) {
		this.picList = picList;
	}

	public Integer getIsAnonymous() {
		return isAnonymous;
	}

	public void setIsAnonymous(Integer isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

}
