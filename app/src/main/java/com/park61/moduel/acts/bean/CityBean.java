package com.park61.moduel.acts.bean;

import java.io.Serializable;

public class CityBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4708917085793091338L;

	private Long id;// bigint(20)主键id
	private String cityName;// varchar(30)城市名称
	private Long pid;// bigint(20)所属省份id
	private Integer isSupport;// int(1)是否是支持城市
	private Integer sortWeight;// int(1)排序权重
	private String provinceName;
	private String countyName;
	private Long goodReceiverCityId;
	private Long endUserId;
	private String name;
	private int hasChild; //子节点个数，0代表没有子节点

	public int getHasChild() {
		return hasChild;
	}

	public void setHasChild(int hasChild) {
		this.hasChild = hasChild;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getEndUserId() {
		return endUserId;
	}

	public void setEndUserId(Long endUserId) {
		this.endUserId = endUserId;
	}

	public Long getGoodReceiverCityId() {
		return goodReceiverCityId;
	}

	public void setGoodReceiverCityId(Long goodReceiverCityId) {
		this.goodReceiverCityId = goodReceiverCityId;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Integer getIsSupport() {
		return isSupport;
	}

	public void setIsSupport(Integer isSupport) {
		this.isSupport = isSupport;
	}

	public Integer getSortWeight() {
		return sortWeight;
	}

	public void setSortWeight(Integer sortWeight) {
		this.sortWeight = sortWeight;
	}

}
