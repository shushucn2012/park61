package com.park61.moduel.acts.bean;

import java.io.Serializable;

public class BannerItem implements Serializable {

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = -1031008670725398690L;
	private int id;// bigint(20)主键id
	private String img;// varchar(200)图片地址
	private String url;// varchar(200)链接页面地址
	private String description;// varchar(200)页面描述
	private Integer sort;// int(2)排序序号
	private Integer isDelete;// int(1)是否删除：0 未删除；1 删除
	private Integer isShow;// int(1)是否显示：0 不显示；1 显示

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

}
