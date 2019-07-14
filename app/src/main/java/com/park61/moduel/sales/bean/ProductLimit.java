package com.park61.moduel.sales.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductLimit implements Serializable {

	private static final long serialVersionUID = 4403989746012877095L;

	// 图片
	private String img;
	// 商品名称
	private String name;
	// 限量
	private Long num;
	// 已抢
	private String salesNum;
	// 价格
	private BigDecimal salesPrice;
	// 商品id
	private Long pmInfoid;
	// 促销id
	private Long promotionId;
	private String surplusTime;//剩余时间

	private String start;//秒杀时间段
	private long id;
	private String isStart;
	private String isCurrent;
	private String end;
	private String countDownTime;
	private String msg;//秒杀描述（如：即将开抢）
	private String startTimeStr;//秒杀时间段

	public String getCountDownTime() {
		return countDownTime;
	}

	public void setCountDownTime(String countDownTime) {
		this.countDownTime = countDownTime;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIsStart() {
		return isStart;
	}

	public void setIsStart(String isStart) {
		this.isStart = isStart;
	}

	public String getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(String isCurrent) {
		this.isCurrent = isCurrent;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getSurplusTime() {
		return surplusTime;
	}

	public void setSurplusTime(String surplusTime) {
		this.surplusTime = surplusTime;
	}

	public ProductLimit() {

	}

	public ProductLimit(Long pmInfoid, String name, BigDecimal salesPrice,
			BigDecimal oldPrice, String img) {
		this.pmInfoid = pmInfoid;
		this.name = name;
		this.salesPrice = salesPrice;
		this.oldPrice = oldPrice;
		this.img = img;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public String getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(String salesNum) {
		this.salesNum = salesNum;
	}

	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}

	private BigDecimal oldPrice;

	public BigDecimal getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(BigDecimal oldPrice) {
		this.oldPrice = oldPrice;
	}

	public Long getPmInfoid() {
		return pmInfoid;
	}

	public void setPmInfoid(Long pmInfoid) {
		this.pmInfoid = pmInfoid;
	}

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
}
