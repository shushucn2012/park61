package com.park61.moduel.shop.bean;

import java.io.Serializable;

/**
 * 自提点店铺
 * 
 * @author Roger
 * 
 */
public class DistrictStore implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;// 主键

	// 城市ID
	private Long areaId;
	// 城市名称
	private String areaName;
	// 区域ID
	private Long countyId;
	// 区域名称
	private String districtName;
	// 区域店铺数量
	private Long shopCount;
	// 店铺名称
	private String storeName;
	// 地址
	private String address;
	// 店铺电话
	private String phone;
	// 距离
	private Long distanceNum;
	// 当前区域是否选中
	private boolean isSelect;

	public DistrictStore() {
		super();
	}

	public DistrictStore(Long id, Long areaId, String areaName,
			Long countyId, String districtName, Long shopCount,
			String storeName, String address, String phone, Long distanceNum,
			boolean isSelect) {
		super();
		this.id = id;
		this.areaId = areaId;
		this.areaName = areaName;
		this.countyId = countyId;
		this.districtName = districtName;
		this.shopCount = shopCount;
		this.storeName = storeName;
		this.address = address;
		this.phone = phone;
		this.distanceNum = distanceNum;
		this.isSelect = isSelect;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Long getCountyId() {
		return countyId;
	}

	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public Long getShopCount() {
		return shopCount;
	}

	public void setShopCount(Long shopCount) {
		this.shopCount = shopCount;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getDistanceNum() {
		return distanceNum;
	}

	public void setDistanceNum(Long distanceNum) {
		this.distanceNum = distanceNum;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

}
