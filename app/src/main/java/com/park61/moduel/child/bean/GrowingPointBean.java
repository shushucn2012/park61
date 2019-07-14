package com.park61.moduel.child.bean;

import java.io.Serializable;

public class GrowingPointBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4156979609194230733L;

	private Long id;// id
	private String growingDate;// 记录日期
	private Double height; // 身高
	private Double maxHeight; // 最大正常身高
	private Double minHeight; // 最小正常身高
	private Double weight; // 体重
	private Double maxWeight; // 最大正常体重
	private Double minWeight; // 最小正常体重

	public String getGrowingDate() {
		return growingDate;
	}

	public void setGrowingDate(String growingDate) {
		this.growingDate = growingDate;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(Double maxHeight) {
		this.maxHeight = maxHeight;
	}

	public Double getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(Double minHeight) {
		this.minHeight = minHeight;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public Double getMinWeight() {
		return minWeight;
	}

	public void setMinWeight(Double minWeight) {
		this.minWeight = minWeight;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
