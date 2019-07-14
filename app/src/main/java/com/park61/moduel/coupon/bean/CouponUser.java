package com.park61.moduel.coupon.bean;

import com.park61.moduel.me.bean.BabyItem;

public class CouponUser {
	private long id;// 用户优惠券id
	private long couponId;// 优惠券id
	private int limitType;// 有效期类型（1-有效期 2-不限期 3未知）
	private String receiveTime;// 获取时间
	private String startTime;// 有效期起始日期（limitType为1时有值）
	private String endTime;// 有效期结束日期（limitType为1时有值）
	private int status;// 状态（1可用 2已使用 3已过期）
	private long userId;// 用户id
	private long childId;// 宝宝id
	private Coupon coupon;
	private String code;
	private CouponRule couponRule;
	private BabyItem userChild;
	private String endReleaseDate;
	private String releaseDate;
	private boolean isChosen;//是否选中
	public boolean isChosen() {
		return isChosen;
	}

	public void setChosen(boolean chosen) {
		isChosen = chosen;
	}

	public CouponRule getCouponRule() {
		return couponRule;
	}

	public void setCouponRule(CouponRule couponRule) {
		this.couponRule = couponRule;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public int getLimitType() {
		return limitType;
	}

	public void setLimitType(int limitType) {
		this.limitType = limitType;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getChildId() {
		return childId;
	}

	public void setChildId(long childId) {
		this.childId = childId;
	}

	public BabyItem getUserChild() {
		return userChild;
	}

	public void setUserChild(BabyItem userChild) {
		this.userChild = userChild;
	}

	public String getEndReleaseDate() {
		return endReleaseDate;
	}

	public void setEndReleaseDate(String endReleaseDate) {
		this.endReleaseDate = endReleaseDate;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

}
