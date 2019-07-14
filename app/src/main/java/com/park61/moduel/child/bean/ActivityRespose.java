package com.park61.moduel.child.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.park61.moduel.acts.bean.ActItem;

public class ActivityRespose implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3502797973820469284L;

	private String dateOfDay = "";

	private List<ActItem> notJoinActivity = new ArrayList<>();
	private List<ActItem> JoinActivity = new ArrayList<>();

	public List<ActItem> getNotJoinActivity() {
		return notJoinActivity;
	}

	public void setNotJoinActivity(List<ActItem> notJoinActivity) {
		this.notJoinActivity = notJoinActivity;
	}

	public List<ActItem> getJoinActivity() {
		return JoinActivity;
	}

	public void setJoinActivity(List<ActItem> joinActivity) {
		JoinActivity = joinActivity;
	}

	public String getDateOfDay() {
		return dateOfDay;
	}

	public void setDateOfDay(String dateOfDay) {
		this.dateOfDay = dateOfDay;
	}

}
