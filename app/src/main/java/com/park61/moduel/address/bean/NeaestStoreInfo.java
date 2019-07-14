package com.park61.moduel.address.bean;

public class NeaestStoreInfo {
	private long id;// 最近店铺id
	private long countyId;// 区域id
	private String countyName;// 区域名称
	private String storeName;// 店铺名称
	private String address;// 店铺地址
	private String phone;// 店铺电话
	private String distanceNum;// 距离(米)
	private String kiloDistance;// 距离(千米)

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCountyId() {
		return countyId;
	}

	public void setCountyId(long countyId) {
		this.countyId = countyId;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
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

	public String getDistanceNum() {
		return distanceNum;
	}

	public void setDistanceNum(String distanceNum) {
		this.distanceNum = distanceNum;
	}

	public String getKiloDistance() {
		return kiloDistance;
	}

	public void setKiloDistance(String kiloDistance) {
		this.kiloDistance = kiloDistance;
	}

}
