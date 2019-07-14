package com.park61.moduel.acts.bean;

import java.io.Serializable;
import java.util.List;

public class CourseComtItem implements Serializable {


	/**
	 * avgScore : 3.5
	 * commentUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/60153c0f-63bf-419d-a65c-b89228b9e846.jpg,http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/b82834cf-c488-4fbe-b2c9-de94eb0566e9.jpg,http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/c88cfcaa-3c1e-4588-844e-c9819a29475d.jpg,http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/c01e232e-c6a1-42fc-bec0-c059a9f5a974.jpg
	 * content : 不错哟，很叼哟，就是喜欢哟
	 * courseId : 22
	 * createDate : 1500883471000
	 * id : 4
	 * mobile : 18062511980
	 * schedule : 100
	 * score : 6
	 * showDate : 1分钟之前
	 * teacherId : 22
	 * urlList : ["http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/60153c0f-63bf-419d-a65c-b89228b9e846.jpg","http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/b82834cf-c488-4fbe-b2c9-de94eb0566e9.jpg","http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/c88cfcaa-3c1e-4588-844e-c9819a29475d.jpg","http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/c01e232e-c6a1-42fc-bec0-c059a9f5a974.jpg"]
	 * userId : 11
	 * userName : 180****1980
	 * userUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/client/7aabf27d-5a65-4e29-90eb-4659628c1508.jpg
	 */

	private double avgScore;
	private String commentUrl;
	private String content;
	private int courseId;
	private long createDate;
	private int id;
	private String mobile;
	private double schedule;
	private double score;
	private String showDate;
	private int teacherId;
	private int userId;
	private String userName;
	private String userUrl;
	private List<String> urlList;

	public double getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(double avgScore) {
		this.avgScore = avgScore;
	}

	public String getCommentUrl() {
		return commentUrl;
	}

	public void setCommentUrl(String commentUrl) {
		this.commentUrl = commentUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public double getSchedule() {
		return schedule;
	}

	public void setSchedule(int schedule) {
		this.schedule = schedule;
	}

	public double getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getShowDate() {
		return showDate;
	}

	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserUrl() {
		return userUrl;
	}

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}

	public List<String> getUrlList() {
		return urlList;
	}

	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}
}
