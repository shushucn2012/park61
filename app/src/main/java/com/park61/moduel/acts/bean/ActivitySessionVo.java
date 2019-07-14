package com.park61.moduel.acts.bean;

import java.io.Serializable;
import java.util.List;

import com.park61.moduel.address.bean.StoreVO;

public class ActivitySessionVo implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7300671826642372798L;
	// 店铺信息
	private StoreVO storeVO;
	// 游戏场次
	private List<ActItem> actSessionList;

	public ActivitySessionVo() {
		super();
	}

	public ActivitySessionVo(StoreVO storeVO, List<ActItem> actSessionList) {
		super();
		this.storeVO = storeVO;
		this.actSessionList = actSessionList;
	}

	public StoreVO getStoreVO() {
		return storeVO;
	}

	public void setStoreVO(StoreVO storeVO) {
		this.storeVO = storeVO;
	}

	public List<ActItem> getActSessionList() {
		return actSessionList;
	}

	public void setActSessionList(List<ActItem> actSessionList) {
		this.actSessionList = actSessionList;
	}

}
