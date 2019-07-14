package com.park61.moduel.me.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.park61.common.set.AppUrl;
import com.park61.moduel.child.bean.GrowingRecBean;

public class BabyItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String petName;
	private String bloodType;
	private String birthday;
	private String sex;
	private String pictureUrl;
	private String age;
	private String ghpOrdinary;// 61区会员 0:已开通；1已过期；2未开通
	private String ghpVip;// 61区vip 0:已开通；1已过期；2未开通
	private String ghpAngel;// 61区天使卡 0:已开通；1已过期；2未开通
	private String timeExpireDate;
	private String memberExpireStr;//会员有效日期
	private GrowingRecBean growingRecordVO;// 身高体重记录
	private ArrayList<MemberButtonVo> memberButtonVoList;
	private boolean isChosen;
	private boolean hasClass;

	public ArrayList<MemberButtonVo> getMemberButtonVoList() {
		return memberButtonVoList;
	}

	public void setMemberButtonVoList(ArrayList<MemberButtonVo> memberButtonVoList) {
		this.memberButtonVoList = memberButtonVoList;
	}

	public String getMemberExpireStr() {
		return memberExpireStr;
	}

	public void setMemberExpireStr(String memberExpireStr) {
		this.memberExpireStr = memberExpireStr;
	}

	public BabyItem() {

	}

	public BabyItem(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setChildId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPictureUrl() {
		if ("images/baobao.jpg".equals(pictureUrl)) {
			return AppUrl.APP_INVITE_URL + "/images/baobao.jpg";
		} else {
			return pictureUrl;
		}
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGhpOrdinary() {
		return ghpOrdinary;
	}

	public void setGhpOrdinary(String ghpOrdinary) {
		this.ghpOrdinary = ghpOrdinary;
	}

	public String getGhpVip() {
		return ghpVip;
	}

	public void setGhpVip(String ghpVip) {
		this.ghpVip = ghpVip;
	}

	public String getTimeExpireDate() {
		return timeExpireDate;
	}

	public void setTimeExpireDate(String timeExpireDate) {
		this.timeExpireDate = timeExpireDate;
	}

	public GrowingRecBean getGrowingRecordVO() {
		return growingRecordVO;
	}

	public void setGrowingRecordVO(GrowingRecBean growingRecordVO) {
		this.growingRecordVO = growingRecordVO;
	}

	public String getGhpAngel() {
		return ghpAngel;
	}

	public void setGhpAngel(String ghpAngel) {
		this.ghpAngel = ghpAngel;
	}

	public boolean isChosen() {
		return isChosen;
	}

	public void setChosen(boolean chosen) {
		isChosen = chosen;
	}

	public boolean isHasClass() {
		return hasClass;
	}

	public void setHasClass(boolean hasClass) {
		this.hasClass = hasClass;
	}
}
