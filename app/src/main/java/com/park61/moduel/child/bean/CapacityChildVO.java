package com.park61.moduel.child.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CapacityChildVO implements Serializable {
	private static final long serialVersionUID = 3525762631528505333L;
	// 主键id
	private Long id;
	// 孩子主键id
	private Long userChildId;
	// 能力主键id
	private Long capacityDimensionId;
	// 能力名称
	private String capacityDimensionName;
	// 能力值
	private BigDecimal capacityValue;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date updateTime;
	// 权重因数
	private BigDecimal weightFactor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserChildId() {
		return userChildId;
	}

	public void setUserChildId(Long userChildId) {
		this.userChildId = userChildId;
	}

	public Long getCapacityDimensionId() {
		return capacityDimensionId;
	}

	public void setCapacityDimensionId(Long capacityDimensionId) {
		this.capacityDimensionId = capacityDimensionId;
	}

	public String getCapacityDimensionName() {
		return capacityDimensionName;
	}

	public void setCapacityDimensionName(String capacityDimensionName) {
		this.capacityDimensionName = capacityDimensionName;
	}

	public BigDecimal getCapacityValue() {
		return capacityValue;
	}

	public void setCapacityValue(BigDecimal capacityValue) {
		this.capacityValue = capacityValue;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public BigDecimal getWeightFactor() {
		return weightFactor;
	}

	public void setWeightFactor(BigDecimal weightFactor) {
		this.weightFactor = weightFactor;
	}

}
