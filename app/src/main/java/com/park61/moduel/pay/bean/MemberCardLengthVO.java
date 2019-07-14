package com.park61.moduel.pay.bean;

import java.io.Serializable;

public class MemberCardLengthVO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3130027738619871188L;

	private Long id;// 主键ID

	private String cardLengthName;// 会员时长类型名称

	private String cardLengthShowName;// 会员时长类型名称(页面显示)

	private Double cardLengthPrice;// 时长原单价

	private String cardLengthCode;// 会员时长编码

	private Double actualPrice;// 实付金额

	private String discount;// 折扣

	private Long salesId;// 促销游戏ID

	private String salesText;// 促销游戏文描

	private Integer rechargeAmount;// 充值时长

	private String rechargeUnit;// 时长单位

	public MemberCardLengthVO() {
		super();
	}

	public MemberCardLengthVO(Long id, String cardLengthName,
			String cardLengthShowName, Double cardLengthPrice,
			String cardLengthCode, Double actualPrice, String discount,
			Long salesId, String salesText, Integer rechargeAmount,
			String rechargeUnit) {
		super();
		this.id = id;
		this.cardLengthName = cardLengthName;
		this.cardLengthShowName = cardLengthShowName;
		this.cardLengthPrice = cardLengthPrice;
		this.cardLengthCode = cardLengthCode;
		this.actualPrice = actualPrice;
		this.discount = discount;
		this.salesId = salesId;
		this.salesText = salesText;
		this.rechargeAmount = rechargeAmount;
		this.rechargeUnit = rechargeUnit;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCardLengthName() {
		return cardLengthName;
	}

	public void setCardLengthName(String cardLengthName) {
		this.cardLengthName = cardLengthName;
	}

	public String getCardLengthShowName() {
		return cardLengthShowName;
	}

	public void setCardLengthShowName(String cardLengthShowName) {
		this.cardLengthShowName = cardLengthShowName;
	}

	public Double getCardLengthPrice() {
		return cardLengthPrice;
	}

	public void setCardLengthPrice(Double cardLengthPrice) {
		this.cardLengthPrice = cardLengthPrice;
	}

	public String getCardLengthCode() {
		return cardLengthCode;
	}

	public void setCardLengthCode(String cardLengthCode) {
		this.cardLengthCode = cardLengthCode;
	}

	public Double getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Long getSalesId() {
		return salesId;
	}

	public void setSalesId(Long salesId) {
		this.salesId = salesId;
	}

	public String getSalesText() {
		return salesText;
	}

	public void setSalesText(String salesText) {
		this.salesText = salesText;
	}

	public Integer getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Integer rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public String getRechargeUnit() {
		return rechargeUnit;
	}

	public void setRechargeUnit(String rechargeUnit) {
		this.rechargeUnit = rechargeUnit;
	}

}
