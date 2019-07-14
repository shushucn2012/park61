package com.park61.moduel.shop.bean;

import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * 店铺下2周內所有游戏实体类
 * 
 * @author HP
 *
 */
public class ShopActWeekDateBean implements Serializable {

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = -42654374320996671L;

	private String id;

	private int position;

	private String strWeek;

	private String strDate;

	private DateTime dateNormal;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getStrWeek() {
		return strWeek;
	}

	public void setStrWeek(String strWeek) {
		this.strWeek = strWeek;
	}

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	public DateTime getDateNormal() {
		return dateNormal;
	}

	public void setDateNormal(DateTime dateNormal) {
		this.dateNormal = dateNormal;
	}
}
