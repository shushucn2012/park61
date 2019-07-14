package com.park61.moduel.child.bean;

import java.io.Serializable;

public class GrowingRecBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7437506049926480529L;

	private Long id;// bigint(20)主键id
	private Long childId;// bigint(20)记录的孩子的id
	private Double weight;// varchar(10)孩子的体重
	private Double height;// varchar(10)孩子的身高
	private String growingDate;// 成长日期
	private Integer ageYear;// 获取成长正常数据范围时需要传入的参数
	private Integer ageMonth;// 获取成长正常数据范围时需要传入的参数
	private Integer ageDay;// 获取成长正常数据范围时需要传入的参数
	private Double maxWeight;// 正常体重的最大值
	private Double minWeight;// 正常体重的最小值
	private Double maxHeight;// 正常身高的最大值
	private Double minHeight;// 正常身高的最小值
	private String state;// 状态描述

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getChildId() {
		return childId;
	}

	public void setChildId(Long childId) {
		this.childId = childId;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public String getGrowingDate() {
		return growingDate;
	}

	public void setGrowingDate(String growingDate) {
		this.growingDate = growingDate;
	}

	public Integer getAgeYear() {
		return ageYear;
	}

	public void setAgeYear(Integer ageYear) {
		this.ageYear = ageYear;
	}

	public Integer getAgeMonth() {
		return ageMonth;
	}

	public void setAgeMonth(Integer ageMonth) {
		this.ageMonth = ageMonth;
	}

	public Integer getAgeDay() {
		return ageDay;
	}

	public void setAgeDay(Integer ageDay) {
		this.ageDay = ageDay;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
