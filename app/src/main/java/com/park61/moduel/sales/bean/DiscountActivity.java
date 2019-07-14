package com.park61.moduel.sales.bean;

import java.io.Serializable;
import java.util.List;

public class DiscountActivity implements Serializable {

	private static final long serialVersionUID = -5348967035158177883L;
	private Long id;
	private String code;// 活动编号
	private String title;// 活动标题
	private Long ruleId;// 规则id
	private Integer vipDiscount;// 是否同时享有vip折扣（0 否 1 是）
	private String startDate;// 活动开始日期
	private String endDate;// 活动结束日期
	private Integer status;// 状态（0 待审核 1 已审核 2 审核不通过）
	private String checkDate;// 审核时间
	private String url;// 地址
	
	private List<ProductLimit> plList;

	public List<ProductLimit> getPlList() {
		return plList;
	}

	public void setPlList(List<ProductLimit> plList) {
		this.plList = plList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getVipDiscount() {
		return vipDiscount;
	}

	public void setVipDiscount(Integer vipDiscount) {
		this.vipDiscount = vipDiscount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
