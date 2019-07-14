package com.park61.moduel.child.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class CapacityActVO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3502797973820469283L;
	private Long id;// 主键id
	private String capacityName;// 能力名称
	private BigDecimal capacityIncreaseValue;// 能力值

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCapacityName() {
		return capacityName;
	}

	public void setCapacityName(String capacityName) {
		this.capacityName = capacityName;
	}

	public BigDecimal getCapacityIncreaseValue() {
		return capacityIncreaseValue;
	}

	public void setCapacityIncreaseValue(BigDecimal capacityIncreaseValue) {
		this.capacityIncreaseValue = capacityIncreaseValue;
	}

}
