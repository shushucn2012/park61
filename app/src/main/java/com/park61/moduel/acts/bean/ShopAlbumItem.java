package com.park61.moduel.acts.bean;

import java.io.Serializable;

/**
 * 店铺相册实体类
 */
public class ShopAlbumItem implements Serializable {

	/**
	 * serialVersionUID:TODO
	 */
	private static final long serialVersionUID = -86446363405203759L;

	//
	private Long id;

	private String createBy;
	private String type;
	private String merchantId;
	private String thumbNail;
	private String picUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getThumbNail() {
		return thumbNail;
	}

	public void setThumbNail(String thumbNail) {
		this.thumbNail = thumbNail;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}