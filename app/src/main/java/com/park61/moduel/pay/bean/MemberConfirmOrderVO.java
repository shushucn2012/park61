package com.park61.moduel.pay.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员充值 确认订单
 * 
 * @author james
 * 
 */
public class MemberConfirmOrderVO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7992196242541560755L;

	private Long rechargeId;// 主键ID

	private Long childId;// 小孩ID

	private String childName;// 小孩名称

	private String cardName;// 会员卡名称

	private String cardCode;// 会员卡编码

	private String cardPicUrl;// 会员卡图片

	private String cardLengthName;

	private Double amountPaid;// 实付金额

	private Double originalPrice;// 原价

	private BigDecimal subTotalPrice;// 小计

	private Long useCouPonId;// 优惠券数量

	private Integer useCouPonSize;// 优惠券数量

	private BigDecimal useCouponPrice;// 使用优惠券金额

	private BigDecimal totalPrice;// 总计

	// 充值方式(0:开通;1:续费;2:升级)
	private String rechargeType;

	// 充值方式(0:开通;1:续费;2:升级)
	private String rechargeTypeName;

	private long shopId;//充值店铺id

	private int hasContract;//是否有超链接(0:否，1：是)

	private String contractLink;//超链接地址
	private long cardId;//卡id

	public long getCardId() {
		return cardId;
	}

	public void setCardId(long cardId) {
		this.cardId = cardId;
	}

	public int getHasContract() {
		return hasContract;
	}

	public void setHasContract(int hasContract) {
		this.hasContract = hasContract;
	}

	public String getContractLink() {
		return contractLink;
	}

	public void setContractLink(String contractLink) {
		this.contractLink = contractLink;
	}

	public Long getRechargeId() {
		return rechargeId;
	}

	public void setRechargeId(Long rechargeId) {
		this.rechargeId = rechargeId;
	}

	public Long getChildId() {
		return childId;
	}

	public void setChildId(Long childId) {
		this.childId = childId;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getCardPicUrl() {
		return cardPicUrl;
	}

	public void setCardPicUrl(String cardPicUrl) {
		this.cardPicUrl = cardPicUrl;
	}

	public String getCardLengthName() {
		return cardLengthName;
	}

	public void setCardLengthName(String cardLengthName) {
		this.cardLengthName = cardLengthName;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public BigDecimal getSubTotalPrice() {
		return subTotalPrice;
	}

	public void setSubTotalPrice(BigDecimal subTotalPrice) {
		this.subTotalPrice = subTotalPrice;
	}

	public String getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}

	public String getRechargeTypeName() {
		return rechargeTypeName;
	}

	public void setRechargeTypeName(String rechargeTypeName) {
		this.rechargeTypeName = rechargeTypeName;
	}

	public Integer getUseCouPonSize() {
		return useCouPonSize;
	}

	public void setUseCouPonSize(Integer useCouPonSize) {
		this.useCouPonSize = useCouPonSize;
	}

	public BigDecimal getUseCouponPrice() {
		return useCouponPrice;
	}

	public void setUseCouponPrice(BigDecimal useCouponPrice) {
		this.useCouponPrice = useCouponPrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Long getUseCouPonId() {
		return useCouPonId;
	}

	public void setUseCouPonId(Long useCouPonId) {
		this.useCouPonId = useCouPonId;
	}

	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

}
