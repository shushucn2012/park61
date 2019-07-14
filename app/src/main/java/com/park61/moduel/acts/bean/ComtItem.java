package com.park61.moduel.acts.bean;

import java.io.Serializable;
import java.util.List;

public class ComtItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;// 评论id
	private Long actId;// 游戏id
	private String content;// 内容
	private String createTime;// 时间
	private String userHead;// 用户头像
	private Long userId;// 用户id
	private String userName;// 用户姓名
	private String parentName;// 上级评论的发表人姓名
	private List<ReplyItem> replyList;
	private Double activityTotalScore;// 游戏总评分
	private Double compositeScore;// 当前评分
	private Integer type;// 类型 0 评价;1 评论
	private List<EvaImage> listPictrue;//

	public Double getActivityTotalScore() {
		return activityTotalScore;
	}

	public void setActivityTotalScore(Double activityTotalScore) {
		this.activityTotalScore = activityTotalScore;
	}

	public Double getCompositeScore() {
		return compositeScore;
	}

	public void setCompositeScore(Double compositeScore) {
		this.compositeScore = compositeScore;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<EvaImage> getListPictrue() {
		return listPictrue;
	}

	public void setListPictrue(List<EvaImage> listPictrue) {
		this.listPictrue = listPictrue;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActId() {
		return actId;
	}

	public void setActId(Long actId) {
		this.actId = actId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserHead() {
		return userHead;
	}

	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<ReplyItem> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<ReplyItem> replyList) {
		this.replyList = replyList;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
