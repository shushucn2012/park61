package com.park61.moduel.acts.bean;

import java.io.Serializable;

/**
 * @author james
 * 
 */
public class ActivityClassify implements Serializable {

	private static final long serialVersionUID = 66053546603745724L;
	private String value;// 游戏分类值
	private String name;// 游戏分类名称
	private String type; // 游戏类型
	private Integer actCount;// 游戏条数
	private String actStatus;// 游戏状态
	private Long merchantId;// 店铺主键id

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getActCount() {
		return actCount;
	}

	public void setActCount(Integer actCount) {
		this.actCount = actCount;
	}

	public String getActStatus() {
		return actStatus;
	}

	public void setActStatus(String actStatus) {
		this.actStatus = actStatus;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

}
