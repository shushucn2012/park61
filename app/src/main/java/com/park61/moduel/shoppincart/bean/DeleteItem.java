package com.park61.moduel.shoppincart.bean;

import java.io.Serializable;

public class DeleteItem implements Serializable{
	private long pmInfoId;
	private long promotionId;
	
	
	public DeleteItem(){
		
	}
	public DeleteItem(long pmInfoId, long promotionId) {
		super();
		this.pmInfoId = pmInfoId;
		this.promotionId = promotionId;
	}
	public long getPmInfoId() {
		return pmInfoId;
	}
	public void setPmInfoId(long pmInfoId) {
		this.pmInfoId = pmInfoId;
	}
	public long getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(long promotionId) {
		this.promotionId = promotionId;
	}
	
}
