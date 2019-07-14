package com.park61.moduel.child.bean;

import java.io.Serializable;
import java.util.List;

public class ShowItem implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1194512476072325791L;

	private Long id;// bigint(20)主键id
	private Long childId;// bigint(20)记录的孩子的id
	private Long kinship;// bigint(20)亲属主键id
	private String comment;// 描述
	private String location;// 位置信息
	private String creator;// 创建人
	private String createTime;// datetime记录时间2015-10-10 12:33:33
	private String orderTime;// 记录排序字段
	private Double weight;// varchar(10)孩子的体重
	private Double height;// varchar(10)孩子的身高
	private String growingDate;// 成长日期
	private Integer ageYear;// 获取成长正常数据范围时需要传入的参数
	private Integer ageMonth;// 获取成长正常数据范围时需要传入的参数
	private Integer ageDay;// 获取成长正常数据范围时需要传入的参数
	private Double maxWeight;// 正常体重的最大值
	private Double minWeight;// 正常体重的最小值
	private Double maxHeight;// 正常身高的最大值
	private Double minHeight;// 正常身高的最小值
	private String state;// 状态描述
	private String relationName;// 关系名称
	private Integer type;// 0:萌照 1:成长记录
	private String userHead;// 头像
	private String userName;// 姓名
	private String currDate;// 当前时间

	private Long resultId;//测评id
	private String eaItemName;
	private String eaSysName;
	private Represent represent;

	private Long voteApplyId;//投票id
	private VoteActivity voteActivity;
	private VoteApply voteApply;

	public Long getResultId() {
		return resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public String getEaItemName() {
		return eaItemName;
	}

	public void setEaItemName(String eaItemName) {
		this.eaItemName = eaItemName;
	}

	public String getEaSysName() {
		return eaSysName;
	}

	public void setEaSysName(String eaSysName) {
		this.eaSysName = eaSysName;
	}

	public Represent getRepresent() {
		return represent;
	}

	public void setRepresent(Represent represent) {
		this.represent = represent;
	}

	private List<ShowPhotosDetailsVO> listDetails;// 照片地址集合

	private List<GrowCommentVO> listGrowComment;// 成才记录的评论列表

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getChildId() {
		return childId;
	}

	public void setChildId(Long childId) {
		this.childId = childId;
	}

	public Long getKinship() {
		return kinship;
	}

	public void setKinship(Long kinship) {
		this.kinship = kinship;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public String getGrowingDate() {
		return growingDate;
	}

	public void setGrowingDate(String growingDate) {
		this.growingDate = growingDate;
	}

	public Integer getAgeYear() {
		return ageYear;
	}

	public void setAgeYear(Integer ageYear) {
		this.ageYear = ageYear;
	}

	public Integer getAgeMonth() {
		return ageMonth;
	}

	public void setAgeMonth(Integer ageMonth) {
		this.ageMonth = ageMonth;
	}

	public Integer getAgeDay() {
		return ageDay;
	}

	public void setAgeDay(Integer ageDay) {
		this.ageDay = ageDay;
	}

	public Double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public Double getMinWeight() {
		return minWeight;
	}

	public void setMinWeight(Double minWeight) {
		this.minWeight = minWeight;
	}

	public Double getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(Double maxHeight) {
		this.maxHeight = maxHeight;
	}

	public Double getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(Double minHeight) {
		this.minHeight = minHeight;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<ShowPhotosDetailsVO> getListDetails() {
		return listDetails;
	}

	public void setListDetails(List<ShowPhotosDetailsVO> listDetails) {
		this.listDetails = listDetails;
	}

	public List<GrowCommentVO> getListGrowComment() {
		return listGrowComment;
	}

	public void setListGrowComment(List<GrowCommentVO> listGrowComment) {
		this.listGrowComment = listGrowComment;
	}

	public String getUserHead() {
		return userHead;
	}

	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCurrDate() {
		return currDate;
	}

	public void setCurrDate(String currDate) {
		this.currDate = currDate;
	}

	public Long getVoteApplyId() {
		return voteApplyId;
	}

	public void setVoteApplyId(Long voteApplyId) {
		this.voteApplyId = voteApplyId;
	}

	public VoteActivity getVoteActivity() {
		return voteActivity;
	}

	public void setVoteActivity(VoteActivity voteActivity) {
		this.voteActivity = voteActivity;
	}

	public VoteApply getVoteApply() {
		return voteApply;
	}

	public void setVoteApply(VoteApply voteApply) {
		this.voteApply = voteApply;
	}

}
