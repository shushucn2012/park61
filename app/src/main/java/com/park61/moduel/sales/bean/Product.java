package com.park61.moduel.sales.bean;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable {

	private static final long serialVersionUID = 3796469550928383356L;
	// 主键id
	private Long id;
	// 产品code
	private String productCode;
	// 产品名称
	private String productCname;
	// 产品英文名
	private String productEname;
	// 产品品牌id
	private Long productBrandId;
	// 产品类目id
	private Long productCategoryId;
	// 条形码
	private String productEan13;
	// 净重
	private Double productNetWeight;
	// 毛重
	private Double productGrossWeight;
	// 产品描述
	private String productDescription;
	// 产品类型 0-普通产品 1-主系列产品 2-子系列产品
	private Integer productType;
	// 颜色
	private String productColor;
	// 尺码
	private String productSize;
	// 年龄下限
	private Integer productAgeLowerLimit;
	// 年龄上限
	private Integer productAgeUpperLimit;
	// 图片地址
	private String productPicUrl;
	// 创建时间
	private Date createTime;
	// 创建人
	private Long createOperatorId;
	// 更新时间
	private Date updateTime;
	// 更新人
	private Long updateOperatorId;

	// 商品ID
	private Long pmInfoId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductCname() {
		return productCname;
	}

	public void setProductCname(String productCname) {
		this.productCname = productCname;
	}

	public String getProductEname() {
		return productEname;
	}

	public void setProductEname(String productEname) {
		this.productEname = productEname;
	}

	public Long getProductBrandId() {
		return productBrandId;
	}

	public void setProductBrandId(Long productBrandId) {
		this.productBrandId = productBrandId;
	}

	public Long getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductEan13() {
		return productEan13;
	}

	public void setProductEan13(String productEan13) {
		this.productEan13 = productEan13;
	}

	public Double getProductNetWeight() {
		return productNetWeight;
	}

	public void setProductNetWeight(Double productNetWeight) {
		this.productNetWeight = productNetWeight;
	}

	public Double getProductGrossWeight() {
		return productGrossWeight;
	}

	public void setProductGrossWeight(Double productGrossWeight) {
		this.productGrossWeight = productGrossWeight;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public String getProductColor() {
		return productColor;
	}

	public void setProductColor(String productColor) {
		this.productColor = productColor;
	}

	public String getProductSize() {
		return productSize;
	}

	public void setProductSize(String productSize) {
		this.productSize = productSize;
	}

	public Integer getProductAgeLowerLimit() {
		return productAgeLowerLimit;
	}

	public void setProductAgeLowerLimit(Integer productAgeLowerLimit) {
		this.productAgeLowerLimit = productAgeLowerLimit;
	}

	public Integer getProductAgeUpperLimit() {
		return productAgeUpperLimit;
	}

	public void setProductAgeUpperLimit(Integer productAgeUpperLimit) {
		this.productAgeUpperLimit = productAgeUpperLimit;
	}

	public String getProductPicUrl() {
		return productPicUrl;
	}

	public void setProductPicUrl(String productPicUrl) {
		this.productPicUrl = productPicUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Long getPmInfoId() {
		return pmInfoId;
	}

	public void setPmInfoId(Long pmInfoId) {
		this.pmInfoId = pmInfoId;
	}

}
