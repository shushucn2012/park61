package com.park61.moduel.pay.bean;

import java.io.Serializable;
import java.util.List;


public class MemberCardTypeVO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6831850320550720868L;

	private Long id;// 主键ID

	private String cardName;// 会员类型名称

	private String cardCode;// 会员类型编码

	private String memberLengthCode;// 会员时长ID

	private String cardUrl;// 卡icon
	private int isBindChild;//（0：无需绑定宝宝，1：需绑定宝宝）

	private List<MemberCardLengthVO> cardTypeLengthList;// 会员类型时长及费用集合

	public int getIsBindChild() {
		return isBindChild;
	}

	public void setIsBindChild(int isBindChild) {
		this.isBindChild = isBindChild;
	}

	public MemberCardTypeVO() {
		super();
	}

	public MemberCardTypeVO(Long id, String cardName, String cardCode,
			String memberLengthCode) {
		super();
		this.id = id;
		this.cardName = cardName;
		this.cardCode = cardCode;
		this.memberLengthCode = memberLengthCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getMemberLengthCode() {
		return memberLengthCode;
	}

	public void setMemberLengthCode(String memberLengthCode) {
		this.memberLengthCode = memberLengthCode;
	}

	public List<MemberCardLengthVO> getCardTypeLengthList() {
		return cardTypeLengthList;
	}

	public void setCardTypeLengthList(
			List<MemberCardLengthVO> cardTypeLengthList) {
		this.cardTypeLengthList = cardTypeLengthList;
	}

	public String getCardUrl() {
		return cardUrl;
	}

	public void setCardUrl(String cardUrl) {
		this.cardUrl = cardUrl;
	}

}
