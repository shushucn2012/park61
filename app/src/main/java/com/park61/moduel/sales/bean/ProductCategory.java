package com.park61.moduel.sales.bean;

import java.io.Serializable;
import java.util.Date;

public class ProductCategory implements Serializable{
	private static final long serialVersionUID = 7025755284948875833L;
	// 主键id
	private Long id;
	// 类目名称
	private String categoryName;
	// 父id
	private Long categoryParentId;
	// 类目描述
	private String categoryDescription;
	// 类目层次
	private Integer categoryLevel;
	// 类目图片链接
	private String categoryPicUrl;
	// 排序
	private Integer sort;
	// 是否删除 0-否 1-是
	private Integer isDelete;
	// 创建时间
	private Date create_time;
	// 创建操作人
	private Long createOperatorId;
	// 更新时间
	private Date updateTime;
	// 更新操作人
	private Long updateOperatorId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Long getCategoryParentId() {
		return categoryParentId;
	}
	public void setCategoryParentId(Long categoryParentId) {
		this.categoryParentId = categoryParentId;
	}
	public String getCategoryDescription() {
		return categoryDescription;
	}
	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}
	public Integer getCategoryLevel() {
		return categoryLevel;
	}
	public void setCategoryLevel(Integer categoryLevel) {
		this.categoryLevel = categoryLevel;
	}
	public String getCategoryPicUrl() {
		return categoryPicUrl;
	}
	public void setCategoryPicUrl(String categoryPicUrl) {
		this.categoryPicUrl = categoryPicUrl;
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
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Long getCreateOperatorId() {
		return createOperatorId;
	}
	public void setCreateOperatorId(Long createOperatorId) {
		this.createOperatorId = createOperatorId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getUpdateOperatorId() {
		return updateOperatorId;
	}
	public void setUpdateOperatorId(Long updateOperatorId) {
		this.updateOperatorId = updateOperatorId;
	}

}
