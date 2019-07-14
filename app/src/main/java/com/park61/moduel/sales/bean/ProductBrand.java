package com.park61.moduel.sales.bean;

import java.io.Serializable;

public class ProductBrand implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7300276865762906280L;

	private String brandLogoUrl;
	private String brandName;
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBrandLogoUrl() {
		return brandLogoUrl;
	}

	public void setBrandLogoUrl(String brandLogoUrl) {
		this.brandLogoUrl = brandLogoUrl;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

}
