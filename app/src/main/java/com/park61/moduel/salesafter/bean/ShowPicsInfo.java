package com.park61.moduel.salesafter.bean;

import java.io.Serializable;

public class ShowPicsInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;//主键id
	private long grfId;//退货单id
	private String givebackPicUrl;//凭证图片地址
	private String remark;//退货备注
	public ShowPicsInfo(){
		super();
	}
	public ShowPicsInfo(long id,long grfId,String givebackPicUrl){
		super();
		this.id = id;
		this.grfId = grfId;
		this.givebackPicUrl = givebackPicUrl;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getGrfId() {
		return grfId;
	}
	public void setGrfId(long grfId) {
		this.grfId = grfId;
	}
	public String getGivebackPicUrl() {
		return givebackPicUrl;
	}
	public void setGivebackPicUrl(String givebackPicUrl) {
		this.givebackPicUrl = givebackPicUrl;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
