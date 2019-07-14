package com.park61.moduel.shop.bean;

import java.io.Serializable;

public class MerchantFocusVO implements Serializable {

	private static final long serialVersionUID = 8020379677036518186L;
	// 主键id
	private Long id;
	// 关注的店铺id
	private Long merchantId;
	// 关注的店铺
	private ShopBean merchant;
	// 用户id
	private Long userId;
	// 是否关注 0-关注 1-取消关注
	private Integer isFocus;
	// 收藏时间
	private String focusTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getIsFocus() {
		return isFocus;
	}

	public void setIsFocus(Integer isFocus) {
		this.isFocus = isFocus;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public ShopBean getMerchant() {
		return merchant;
	}

	public void setMerchant(ShopBean merchant) {
		this.merchant = merchant;
	}

	public String getFocusTime() {
		return focusTime;
	}

	public void setFocusTime(String focusTime) {
		this.focusTime = focusTime;
	}

}
