package com.park61.moduel.acts.bean;

import java.util.List;

import com.park61.moduel.child.bean.CapacityActVO;

/**
 * 游戏
 */
public class ActItem extends Entity {

	// 主键ID
	private Long id;

	// 游戏编码(游戏类型简称+游戏年月+序列号)
	private String actCode;

	// 游戏业务类型(0:户外;1:亲子;)
	private String actBussinessType;

	// 游戏标题
	private String actTitle;

	// 游戏明细
	private String actDetail;

	// 游戏开始时间
	private String actStartTime;

	// 游戏结束时间
	private String actEndTime;

	// 游戏发布时间
	private String actReleaseTime;

	// 游戏发布方(机构)
	private String actReleaseAuthor;

	// 游戏地址
	private String actAddress;

	// 游戏地经度
	private String actLongitude;

	// 游戏地纬度
	private String actLatitude;

	// 游戏阅读量
	private Long actReadCount;

	// 游戏分享量
	private Long actShareCount;

	// 参加游戏人数
	private Integer actNumberVisitor;

	// 游戏评论数
	private Long actCommentCount;

	// 游戏点赞数
	private Long actPraise;

	// 游戏报名方式(0:免费;1:收费)
	private String actVisitorType;

	// 联系人
	private String contactor;

	// 联系方式
	private String contactTel;

	// 城市ID
	private Long cityId;

	// 游戏发布类型（0:官方;1:自主）
	private String actReleaseType;

	// 游戏名额限制
	private Long actQuotaLimit;

	private Long actLowQuotaLimit;

	// 游戏年龄上限
	private Long actUppAgeLimit;

	// 游戏年龄下限
	private Long actLowAgeLimit;

	// 游戏状态
	private String actStatus;

	// 游戏封面图片
	private String actCover;

	// 距离
	private String distance = "";

	// 单位
	private String unit;

	// 距离数字
	private Long distanceNum;

	// 前端展示游戏状态
	private String statusName;

	// 关注时间
	private String focusTime;

	// 游戏价格
	private String actPrice;

	// 店名
	private String shopName;

	// 各项能力维度
	private List<CapacityActVO> capacityList;

	// 店铺id
	private Long backendShopId;

	private boolean bJoin;// 是否已参加

	private String labelName;// 游戏类型

	// 累计报名
	private Long grandTotal;

	// 游戏模板ID
	private Long refTemplateId;

	// 游戏场次数量
	private Long countActSession;

	// 店铺信息及场次
	private ActivitySessionVo activitySessionVo;

	// 成人价格
	private String adultPrice;

	//场次是否可报名
	private boolean isApply;

	//可报名场次数量
	private int canEnrolCount;

	//总场次数量
	private int actClassCount;

	//小课场次title
	private String classTime;

	//小课课程安排
	private  List<CourseBean> activityClasses;

	//活动原价
	private String actOriginalPrice;

	//孩子促销价格
	private String childPromPrice;

	//成人促销价格
	private String adultPromPrice;

	//是否促销 0否；1是
	private int isProm;

	//促销时是否可使用优惠券:0-不允许 1-允许
	private int canUseCoupon;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActCode() {
		return this.actCode;
	}

	public void setActCode(String actCode) {
		this.actCode = actCode;
	}

	public String getActBussinessType() {
		return this.actBussinessType;
	}

	public void setActBussinessType(String actBussinessType) {
		this.actBussinessType = actBussinessType;
	}

	public String getActTitle() {
		return this.actTitle;
	}

	public void setActTitle(String actTitle) {
		this.actTitle = actTitle;
	}

	public String getActDetail() {
		return this.actDetail;
	}

	public void setActDetail(String actDetail) {
		this.actDetail = actDetail;
	}

	public String getActReleaseAuthor() {
		return this.actReleaseAuthor;
	}

	public void setActReleaseAuthor(String actReleaseAuthor) {
		this.actReleaseAuthor = actReleaseAuthor;
	}

	public String getActAddress() {
		return this.actAddress;
	}

	public void setActAddress(String actAddress) {
		this.actAddress = actAddress;
	}

	public String getActLongitude() {
		return this.actLongitude;
	}

	public void setActLongitude(String actLongitude) {
		this.actLongitude = actLongitude;
	}

	public String getActLatitude() {
		return this.actLatitude;
	}

	public void setActLatitude(String actLatitude) {
		this.actLatitude = actLatitude;
	}

	public Long getActReadCount() {
		return this.actReadCount;
	}

	public void setActReadCount(Long actReadCount) {
		this.actReadCount = actReadCount;
	}

	public Long getActShareCount() {
		return this.actShareCount;
	}

	public void setActShareCount(Long actShareCount) {
		this.actShareCount = actShareCount;
	}

	public Integer getActNumberVisitor() {
		return this.actNumberVisitor;
	}

	public void setActNumberVisitor(Integer actNumberVisitor) {
		this.actNumberVisitor = actNumberVisitor;
	}

	public Long getActCommentCount() {
		return this.actCommentCount;
	}

	public void setActCommentCount(Long actCommentCount) {
		this.actCommentCount = actCommentCount;
	}

	public Long getActPraise() {
		return this.actPraise;
	}

	public void setActPraise(Long actPraise) {
		this.actPraise = actPraise;
	}

	public String getActVisitorType() {
		return this.actVisitorType;
	}

	public void setActVisitorType(String actVisitorType) {
		this.actVisitorType = actVisitorType;
	}

	public String getContactor() {
		return this.contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public String getContactTel() {
		return this.contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public Long getCityId() {
		return this.cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getActReleaseType() {
		return this.actReleaseType;
	}

	public void setActReleaseType(String actReleaseType) {
		this.actReleaseType = actReleaseType;
	}

	public Long getActQuotaLimit() {
		return this.actQuotaLimit;
	}

	public void setActQuotaLimit(Long actQuotaLimit) {
		this.actQuotaLimit = actQuotaLimit;
	}

	public Long getActUppAgeLimit() {
		return this.actUppAgeLimit;
	}

	public void setActUppAgeLimit(Long actUppAgeLimit) {
		this.actUppAgeLimit = actUppAgeLimit;
	}

	public Long getActLowAgeLimit() {
		return this.actLowAgeLimit;
	}

	public void setActLowAgeLimit(Long actLowAgeLimit) {
		this.actLowAgeLimit = actLowAgeLimit;
	}

	public String getActStatus() {
		return this.actStatus;
	}

	public void setActStatus(String actStatus) {
		this.actStatus = actStatus;
	}

	public String getActCover() {
		return this.actCover;
	}

	public void setActCover(String actCover) {
		this.actCover = actCover;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Long getDistanceNum() {
		return distanceNum;
	}

	public void setDistanceNum(Long distanceNum) {
		this.distanceNum = distanceNum;
	}

	public String getActPrice() {
		return actPrice;
	}

	public void setActPrice(String actPrice) {
		this.actPrice = actPrice;
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

	public String getActReleaseTime() {
		return actReleaseTime;
	}

	public void setActReleaseTime(String actReleaseTime) {
		this.actReleaseTime = actReleaseTime;
	}

	public String getFocusTime() {
		return focusTime;
	}

	public void setFocusTime(String focusTime) {
		this.focusTime = focusTime;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public List<CapacityActVO> getCapacityList() {
		return capacityList;
	}

	public void setCapacityList(List<CapacityActVO> capacityList) {
		this.capacityList = capacityList;
	}

	public Long getBackendShopId() {
		return backendShopId;
	}

	public void setBackendShopId(Long backendShopId) {
		this.backendShopId = backendShopId;
	}

	public Long getActLowQuotaLimit() {
		return actLowQuotaLimit;
	}

	public void setActLowQuotaLimit(Long actLowQuotaLimit) {
		this.actLowQuotaLimit = actLowQuotaLimit;
	}

	public boolean isbJoin() {
		return bJoin;
	}

	public void setbJoin(boolean bJoin) {
		this.bJoin = bJoin;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public Long getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Long grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Long getRefTemplateId() {
		return refTemplateId;
	}

	public void setRefTemplateId(Long refTemplateId) {
		this.refTemplateId = refTemplateId;
	}

	public Long getCountActSession() {
		return countActSession;
	}

	public void setCountActSession(Long countActSession) {
		this.countActSession = countActSession;
	}

	public ActivitySessionVo getActivitySessionVo() {
		return activitySessionVo;
	}

	public void setActivitySessionVo(ActivitySessionVo activitySessionVo) {
		this.activitySessionVo = activitySessionVo;
	}

	public String getAdultPrice() {
		return adultPrice;
	}

	public void setAdultPrice(String adultPrice) {
		this.adultPrice = adultPrice;
	}

	public boolean isApply() {
		return isApply;
	}

	public void setApply(boolean apply) {
		isApply = apply;
	}

	public int getCanEnrolCount() {
		return canEnrolCount;
	}

	public void setCanEnrolCount(int canEnrolCount) {
		this.canEnrolCount = canEnrolCount;
	}

	public int getActClassCount() {
		return actClassCount;
	}

	public void setActClassCount(int actClassCount) {
		this.actClassCount = actClassCount;
	}

	public String getClassTime() {
		return classTime;
	}

	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}

	public List<CourseBean> getActivityClasses() {
		return activityClasses;
	}

	public void setActivityClasses(List<CourseBean> activityClasses) {
		this.activityClasses = activityClasses;
	}

	public String getActOriginalPrice() {
		return actOriginalPrice;
	}

	public void setActOriginalPrice(String actOriginalPrice) {
		this.actOriginalPrice = actOriginalPrice;
	}

	public String getChildPromPrice() {
		return childPromPrice;
	}

	public void setChildPromPrice(String childPromPrice) {
		this.childPromPrice = childPromPrice;
	}

	public String getAdultPromPrice() {
		return adultPromPrice;
	}

	public void setAdultPromPrice(String adultPromPrice) {
		this.adultPromPrice = adultPromPrice;
	}

	public int getIsProm() {
		return isProm;
	}

	public void setIsProm(int isProm) {
		this.isProm = isProm;
	}

	public int getCanUseCoupon() {
		return canUseCoupon;
	}

	public void setCanUseCoupon(int canUseCoupon) {
		this.canUseCoupon = canUseCoupon;
	}

}