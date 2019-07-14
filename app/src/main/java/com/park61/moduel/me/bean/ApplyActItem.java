package com.park61.moduel.me.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ApplyActItem implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6488000691310522968L;

	private Long id; // 报名id
	private String actId; // 游戏id
	private String actTitle; // 游戏标题
	private String actAddress; // 游戏地址
	private String actCover; // 游戏封面
	private String actStartTime; // 游戏开始时间
	private String actEndTime; // 游戏结束时间
	private String actUppAgeLimit; // 年龄上限
	private String actLowAgeLimit; // 年龄下限
	private String applyAdultsNumber; // 成人人数
	private String applyChildrenNumber; // 孩子人数
	private String applyPrice; // 游戏价格
	private String countDownDay; // 游戏倒计时天数
	private String isEvaluate; // 是否评价
	private int isPayment; // 是否支付,0:未支付，1：已支付
	private String regTime; // 报名时间
	private String userId; // 用户id
	private String userName; // 用户名字
	private Double totalPrice;// 报名总价格
	private String prepayId;// 微信支付 预付款
	private String userTel;// 报名者联系方式
	
	private Double amount;//优惠券金额
	private Double preTotalPrice;//游戏总额

	private Long insuranceId;// 保险ID
	private String insuranceName;// 保险名称
	private Double totalInsurance;// 保险总额
	private String actBussinessType;
	private int actClassCount;
	private ArrayList<ApplyBabyName> activityApplyDetailsList;
	private String actClassSeries;//课程系列
	private String backendShopId;//后台店铺Id
	private Long orderId;//后台店铺Id
	private String timeExpireDate;

	public String getBackendShopId() {
		return backendShopId;
	}

	public void setBackendShopId(String backendShopId) {
		this.backendShopId = backendShopId;
	}

	public ArrayList<ApplyBabyName> getActivityApplyDetailsList() {
		return activityApplyDetailsList;
	}

	public void setActivityApplyDetailsList(ArrayList<ApplyBabyName> activityApplyDetailsList) {
		this.activityApplyDetailsList = activityApplyDetailsList;
	}

	public int getActClassCount() {
		return actClassCount;
	}

	public void setActClassCount(int actClassCount) {
		this.actClassCount = actClassCount;
	}

	public String getActBussinessType() {
		return actBussinessType;
	}

	public void setActBussinessType(String actBussinessType) {
		this.actBussinessType = actBussinessType;
	}

	public Double getPreTotalPrice() {
		return preTotalPrice;
	}

	public void setPreTotalPrice(Double preTotalPrice) {
		this.preTotalPrice = preTotalPrice;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getActTitle() {
		return actTitle;
	}

	public void setActTitle(String actTitle) {
		this.actTitle = actTitle;
	}

	public String getActAddress() {
		return actAddress;
	}

	public void setActAddress(String actAddress) {
		this.actAddress = actAddress;
	}

	public String getActCover() {
		return actCover;
	}

	public void setActCover(String actCover) {
		this.actCover = actCover;
	}

	public String getActStartTime() {
		return actStartTime;
	}

	public void setActStartTime(String actStartTime) {
		this.actStartTime = actStartTime;
	}

	public String getActEndTime() {
		return actEndTime;
	}

	public void setActEndTime(String actEndTime) {
		this.actEndTime = actEndTime;
	}

	public String getActUppAgeLimit() {
		return actUppAgeLimit;
	}

	public void setActUppAgeLimit(String actUppAgeLimit) {
		this.actUppAgeLimit = actUppAgeLimit;
	}

	public String getActLowAgeLimit() {
		return actLowAgeLimit;
	}

	public void setActLowAgeLimit(String actLowAgeLimit) {
		this.actLowAgeLimit = actLowAgeLimit;
	}

	public String getApplyAdultsNumber() {
		return applyAdultsNumber;
	}

	public void setApplyAdultsNumber(String applyAdultsNumber) {
		this.applyAdultsNumber = applyAdultsNumber;
	}

	public String getApplyChildrenNumber() {
		return applyChildrenNumber;
	}

	public void setApplyChildrenNumber(String applyChildrenNumber) {
		this.applyChildrenNumber = applyChildrenNumber;
	}

	public String getApplyPrice() {
		return applyPrice;
	}

	public void setApplyPrice(String applyPrice) {
		this.applyPrice = applyPrice;
	}

	public String getCountDownDay() {
		return countDownDay;
	}

	public void setCountDownDay(String countDownDay) {
		this.countDownDay = countDownDay;
	}

	public String getIsEvaluate() {
		return isEvaluate;
	}

	public void setIsEvaluate(String isEvaluate) {
		this.isEvaluate = isEvaluate;
	}

	public int getIsPayment() {
		return isPayment;
	}

	public void setIsPayment(int isPayment) {
		this.isPayment = isPayment;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getUserTel() {
		return userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	public Long getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Long insuranceId) {
		this.insuranceId = insuranceId;
	}

	public String getInsuranceName() {
		return insuranceName;
	}

	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}

	public Double getTotalInsurance() {
		return totalInsurance;
	}

	public void setTotalInsurance(Double totalInsurance) {
		this.totalInsurance = totalInsurance;
	}

	public String getActClassSeries() {
		return actClassSeries;
	}

	public void setActClassSeries(String actClassSeries) {
		this.actClassSeries = actClassSeries;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getTimeExpireDate() {
		return timeExpireDate;
	}

	public void setTimeExpireDate(String timeExpireDate) {
		this.timeExpireDate = timeExpireDate;
	}

}
