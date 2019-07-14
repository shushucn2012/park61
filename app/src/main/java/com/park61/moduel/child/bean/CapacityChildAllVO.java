package com.park61.moduel.child.bean;

import java.io.Serializable;
import java.util.List;

public class CapacityChildAllVO implements Serializable {

	private static final long serialVersionUID = -666755971761595309L;

	// 综合能力领先百分比
	private Double precedeNum;

	// 参加游戏次数
	private Integer joinActivityNum;

	// 各项能力
	private List<CapacityChildVO> list;

	public Double getPrecedeNum() {
		return precedeNum;
	}

	public void setPrecedeNum(Double precedeNum) {
		this.precedeNum = precedeNum;
	}

	public List<CapacityChildVO> getList() {
		return list;
	}

	public void setList(List<CapacityChildVO> list) {
		this.list = list;
	}

	public Integer getJoinActivityNum() {
		return joinActivityNum;
	}

	public void setJoinActivityNum(Integer joinActivityNum) {
		this.joinActivityNum = joinActivityNum;
	}

}
