package com.park61.moduel.acts.bean;

import java.io.Serializable;

public class ReplyItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;// 评论id
	private Long actId;// 游戏id
	private String content;// 内容
	private String contentTime;// 时间
	private String userHead;// 用户头像
	private Long userId;// 用户id
	private String userName;// 用户姓名
	private String parentName;// 上级评论的发表人姓名

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

	public String getContentTime() {
		return contentTime;
	}

	public void setContentTime(String contentTime) {
		this.contentTime = contentTime;
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

}
