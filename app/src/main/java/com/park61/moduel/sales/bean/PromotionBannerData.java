package com.park61.moduel.sales.bean;

import java.io.Serializable;

public class PromotionBannerData implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5829243842868824767L;
	
	private String bannerPositionPic;
	private String bannerPositionWebsite;
	private String name;

	public String getBannerPositionPic() {
		return bannerPositionPic;
	}

	public void setBannerPositionPic(String bannerPositionPic) {
		this.bannerPositionPic = bannerPositionPic;
	}

	public String getBannerPositionWebsite() {
		return bannerPositionWebsite;
	}

	public void setBannerPositionWebsite(String bannerPositionWebsite) {
		this.bannerPositionWebsite = bannerPositionWebsite;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
