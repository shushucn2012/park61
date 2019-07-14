package com.park61.moduel.coupon.bean;

public class Coupon {
	private long id;// 优惠券id
	private String title;// 优惠券名称
	private int type;// 优惠券类型 （1-体验券 2-游戏兑换券 3-折扣券 4-代金券 5-礼品兑换券 6-课程券）
	private double ruleValue1;// 优惠券参数，（满额）
	private double ruleValue2;// 优惠券参数（折扣额、减金额）
	private int limitType;// 有效期类型（1 时间段 2领取后多少天 3 不限期）
	private String limitStart;// 有效期（起始）
	private String limitEnd;// 有效期（结束）
	private int limitDay;// 有效天数（领取后多少天有效的天数）
	private String releaseDate;// 发布日期
	private String endReleaseDate;// 截至发布日期
	private int countType;// 发行数量标识（0 不限量 1 限量）
	private int releaseCount;// 发布数量
	private int receiveCount;// 每人领取数量
	private int useCount;// 每次使用数量
	private String useRange;// 使用范围（1-全场通用 2-游戏专用 3-商城专用 4-指定店铺游戏专用）
	private String useRangeName;
	private int isRegistered;// 是否记名（0 否 1是）
	private int status;// 优惠卷状态 （1-待审核 2-审核通过 3-审核驳回 4-已发布 5-已结束）
	private String remarks;// 描述
	private String typeName;

	private int canGroup;//是否能组合，0：不能，1：能
	private int canRepeat;//是否允许叠加，0：不能，1：能

	public int getCanGroup() {
		return canGroup;
	}

	public void setCanGroup(int canGroup) {
		this.canGroup = canGroup;
	}

	public int getCanRepeat() {
		return canRepeat;
	}

	public void setCanRepeat(int canRepeat) {
		this.canRepeat = canRepeat;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getUseRangeName() {
		return useRangeName;
	}

	public void setUseRangeName(String useRangeName) {
		this.useRangeName = useRangeName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getRuleValue1() {
		return ruleValue1;
	}

	public void setRuleValue1(double ruleValue1) {
		this.ruleValue1 = ruleValue1;
	}

	public double getRuleValue2() {
		return ruleValue2;
	}

	public void setRuleValue2(double ruleValue2) {
		this.ruleValue2 = ruleValue2;
	}

	public int getLimitType() {
		return limitType;
	}

	public void setLimitType(int limitType) {
		this.limitType = limitType;
	}

	public String getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(String limitStart) {
		this.limitStart = limitStart;
	}

	public String getLimitEnd() {
		return limitEnd;
	}

	public void setLimitEnd(String limitEnd) {
		this.limitEnd = limitEnd;
	}

	public int getLimitDay() {
		return limitDay;
	}

	public void setLimitDay(int limitDay) {
		this.limitDay = limitDay;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getEndReleaseDate() {
		return endReleaseDate;
	}

	public void setEndReleaseDate(String endReleaseDate) {
		this.endReleaseDate = endReleaseDate;
	}

	public int getCountType() {
		return countType;
	}

	public void setCountType(int countType) {
		this.countType = countType;
	}

	public int getReleaseCount() {
		return releaseCount;
	}

	public void setReleaseCount(int releaseCount) {
		this.releaseCount = releaseCount;
	}

	public int getReceiveCount() {
		return receiveCount;
	}

	public void setReceiveCount(int receiveCount) {
		this.receiveCount = receiveCount;
	}

	public int getUseCount() {
		return useCount;
	}

	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}

	public String getUseRange() {
		return useRange;
	}

	public void setUseRange(String useRange) {
		this.useRange = useRange;
	}

	public int getIsRegistered() {
		return isRegistered;
	}

	public void setIsRegistered(int isRegistered) {
		this.isRegistered = isRegistered;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
