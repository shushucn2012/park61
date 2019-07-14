package com.park61.moduel.address.bean;

import java.io.Serializable;

/**
 * 收货地址信息
 * 
 * @author Lucia
 * 
 */

public class AddressDetailItem implements Serializable {
	private static final long serialVersionUID = -1202880469358707538L;
	private Long id;// 主键id
	private long endUserId;// 用户id
	private String goodReceiverName;// 收货人姓名
	private String goodReceiverAddress;// 收货人地址
	private String goodReceiverMobile;// 收货人手机号
	private long goodReceiverCountryId;// 国家id
	private long goodReceiverProvinceId;// 省份id
	private String goodReceiverProvinceName;// 省份名称
	private long goodReceiverCityId;// 城市id
	private Long goodReceiverTownId;//乡镇Id
	private String goodReceiverCityName;// 城市名称
	private long goodReceiverCountyId;// 区县id
	private String goodReceiverCountyName;// 区县名称
	private String goodReceiverTownName;//乡镇名称
	private String goodReceiverPostCode;// 邮编
	private int goodReceiverType;// 0-快递 1-自提
	private long storeId;// 店铺主键id，sys_office(Type=1是有值)
	private String storeName;// 店铺名称
	private String storePhone;// 店铺联系方式
	private int isDefault;// 是否默认收货地址 0-否 1-是
	private NeaestStoreInfo store;
	private NeaestStoreArea area;

	public Long getGoodReceiverTownId() {
		return goodReceiverTownId;
	}

	public void setGoodReceiverTownId(Long goodReceiverTownId) {
		this.goodReceiverTownId = goodReceiverTownId;
	}

	public String getGoodReceiverTownName() {
		return goodReceiverTownName;
	}

	public void setGoodReceiverTownName(String goodReceiverTownName) {
		this.goodReceiverTownName = goodReceiverTownName;
	}

	public NeaestStoreInfo getStore() {
		return store;
	}

	public void setStore(NeaestStoreInfo store) {
		this.store = store;
	}

	public NeaestStoreArea getArea() {
		return area;
	}

	public void setArea(NeaestStoreArea area) {
		this.area = area;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getEndUserId() {
		return endUserId;
	}

	public void setEndUserId(long endUserId) {
		this.endUserId = endUserId;
	}

	public String getGoodReceiverName() {
		return goodReceiverName;
	}

	public void setGoodReceiverName(String goodReceiverName) {
		this.goodReceiverName = goodReceiverName;
	}

	public String getGoodReceiverAddress() {
		return goodReceiverAddress;
	}

	public void setGoodReceiverAddress(String goodReceiverAddress) {
		this.goodReceiverAddress = goodReceiverAddress;
	}

	public String getGoodReceiverMobile() {
		return goodReceiverMobile;
	}

	public void setGoodReceiverMobile(String goodReceiverMobile) {
		this.goodReceiverMobile = goodReceiverMobile;
	}

	public long getGoodReceiverCountryId() {
		return goodReceiverCountryId;
	}

	public void setGoodReceiverCountryId(long goodReceiverCountryId) {
		this.goodReceiverCountryId = goodReceiverCountryId;
	}

	public long getGoodReceiverProvinceId() {
		return goodReceiverProvinceId;
	}

	public void setGoodReceiverProvinceId(long goodReceiverProvinceId) {
		this.goodReceiverProvinceId = goodReceiverProvinceId;
	}

	public String getGoodReceiverProvinceName() {
		return goodReceiverProvinceName;
	}

	public void setGoodReceiverProvinceName(String goodReceiverProvinceName) {
		this.goodReceiverProvinceName = goodReceiverProvinceName;
	}

	public long getGoodReceiverCityId() {
		return goodReceiverCityId;
	}

	public void setGoodReceiverCityId(long goodReceiverCityId) {
		this.goodReceiverCityId = goodReceiverCityId;
	}

	public String getGoodReceiverCityName() {
		return goodReceiverCityName;
	}

	public void setGoodReceiverCityName(String goodReceiverCityName) {
		this.goodReceiverCityName = goodReceiverCityName;
	}

	public long getGoodReceiverCountyId() {
		return goodReceiverCountyId;
	}

	public void setGoodReceiverCountyId(long goodReceiverCountyId) {
		this.goodReceiverCountyId = goodReceiverCountyId;
	}

	public String getGoodReceiverCountyName() {
		return goodReceiverCountyName;
	}

	public void setGoodReceiverCountyName(String goodReceiverCountyName) {
		this.goodReceiverCountyName = goodReceiverCountyName;
	}

	public String getGoodReceiverPostCode() {
		return goodReceiverPostCode;
	}

	public void setGoodReceiverPostCode(String goodReceiverPostCode) {
		this.goodReceiverPostCode = goodReceiverPostCode;
	}

	public int getGoodReceiverType() {
		return goodReceiverType;
	}

	public void setGoodReceiverType(int goodReceiverType) {
		this.goodReceiverType = goodReceiverType;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStorePhone() {
		return storePhone;
	}

	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

}
