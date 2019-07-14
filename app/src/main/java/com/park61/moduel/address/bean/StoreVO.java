package com.park61.moduel.address.bean;

import java.io.Serializable;

/**
 * 自提点店铺
 * 
 * @author Roger
 * 
 */
public class StoreVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;// 主键
	private String storeName;// 店铺名称
	private String address;// 地址
	private String phone;// 店铺电话
	private Long distanceNum;// 距离
	private Long countyId;// 店铺所属区域id
	private String master;// 店主
	private String latitude;
	private String longitude;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getCountyId() {
		return countyId;
	}

	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
