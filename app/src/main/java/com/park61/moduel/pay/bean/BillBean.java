package com.park61.moduel.pay.bean;

import java.io.Serializable;

public class BillBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -512576188701124468L;

	private Long id;// bigint(20)主键id

	private Long userId;// 用户ID

	private Integer consumType;// 消费类型(0:在线充值;1:充值61区会员;2:充值VIP会员)

	private String consumRemark;// 消费备注

	private String consumDate;// 消费时间

	private Integer rechargeType;// 充值类型(0:微信;1:支付宝)

	private String receivingTime;// 到帐时间

	private Double amountCharge;// 充值金额

	private Double amountReceiving;// 到帐金额

	private Double amountPreferential;// 优惠金额

	private String preferentialRemark;// 优惠备注

	private String buyerEmail;// 付款账户

	private String result;// 充值结果

	private String tradeNo;// 交易流水号

	private String outTradeNo;// 订单号

	private String prepayId;// 微信预付款ID

	private String source;// 来源 (H5,客户端)

	private String clientSystem;// 客户端系统名称

	private String clientSystemVersion;// 客户端系统版本

	private String deviceCode;// 唯一设备号

	private Integer resultIdent;// 结果标识 0:成功;1:失败

	private Long busiId;// 对应的业务ID

	private String transactionId;// 退款交易单号

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getConsumType() {
		return consumType;
	}

	public void setConsumType(Integer consumType) {
		this.consumType = consumType;
	}

	public String getConsumRemark() {
		return consumRemark;
	}

	public void setConsumRemark(String consumRemark) {
		this.consumRemark = consumRemark;
	}

	public String getConsumDate() {
		return consumDate;
	}

	public void setConsumDate(String consumDate) {
		this.consumDate = consumDate;
	}

	public Integer getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(Integer rechargeType) {
		this.rechargeType = rechargeType;
	}

	public String getReceivingTime() {
		return receivingTime;
	}

	public void setReceivingTime(String receivingTime) {
		this.receivingTime = receivingTime;
	}

	public Double getAmountCharge() {
		return amountCharge;
	}

	public void setAmountCharge(Double amountCharge) {
		this.amountCharge = amountCharge;
	}

	public Double getAmountReceiving() {
		return amountReceiving;
	}

	public void setAmountReceiving(Double amountReceiving) {
		this.amountReceiving = amountReceiving;
	}

	public Double getAmountPreferential() {
		return amountPreferential;
	}

	public void setAmountPreferential(Double amountPreferential) {
		this.amountPreferential = amountPreferential;
	}

	public String getPreferentialRemark() {
		return preferentialRemark;
	}

	public void setPreferentialRemark(String preferentialRemark) {
		this.preferentialRemark = preferentialRemark;
	}

	public String getBuyerEmail() {
		return buyerEmail;
	}

	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getClientSystem() {
		return clientSystem;
	}

	public void setClientSystem(String clientSystem) {
		this.clientSystem = clientSystem;
	}

	public String getClientSystemVersion() {
		return clientSystemVersion;
	}

	public void setClientSystemVersion(String clientSystemVersion) {
		this.clientSystemVersion = clientSystemVersion;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public Integer getResultIdent() {
		return resultIdent;
	}

	public void setResultIdent(Integer resultIdent) {
		this.resultIdent = resultIdent;
	}

	public Long getBusiId() {
		return busiId;
	}

	public void setBusiId(Long busiId) {
		this.busiId = busiId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
