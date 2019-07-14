package com.park61.moduel.sales.bean;

import com.park61.moduel.grouppurchase.bean.FightGroupInfo;

import java.io.Serializable;
import java.util.List;

public class ProductDetail implements Serializable {

	private static final long serialVersionUID = 8302600043690077582L;

	/** 商品基本信息 */
	private PmInfo pmInfo;
	/** 商品价格信息 */
	private PmPrice pmPrice;
	/** 商品库存 */
	private PmStock pmStock;
	/** 商品品牌 */
	private ProductBrand productBrand;
	/**
	 * 商品色系及尺寸
	 */
	private List<SubProduct> subProductList;

	// 颜色集合
	private List<SubProduct> colorProductList;

	// 尺寸集合
	private List<SubProduct> sizeProductList;

	// 颜色尺寸关系集合
	private List<SubProduct> relationProductList;

	// 该商品是否可以点击购买按钮
	private boolean buy;

	//配送地址
	private ProductDeliverRegionVo productDeliverRegionVo;

	//满减信息列表
	private List<DiscountActivity> discountActivity;

	//拼团信息
	private FightGroupInfo fightGroup;

	//支持服务类型
	private List<PmInfoServicetype> pmInfoServicetype;

	public FightGroupInfo getFightGroup() {
		return fightGroup;
	}

	public void setFightGroup(FightGroupInfo fightGroup) {
		this.fightGroup = fightGroup;
	}

	public ProductDeliverRegionVo getProductDeliverRegionVo() {
		return productDeliverRegionVo;
	}

	public void setProductDeliverRegionVo(ProductDeliverRegionVo productDeliverRegionVo) {
		this.productDeliverRegionVo = productDeliverRegionVo;
	}

	public PmInfo getPmInfo() {
		return pmInfo;
	}

	public void setPmInfo(PmInfo pmInfo) {
		this.pmInfo = pmInfo;
	}

	public PmPrice getPmPrice() {
		return pmPrice;
	}

	public void setPmPrice(PmPrice pmPrice) {
		this.pmPrice = pmPrice;
	}

	public PmStock getPmStock() {
		return pmStock;
	}

	public void setPmStock(PmStock pmStock) {
		this.pmStock = pmStock;
	}

	public List<SubProduct> getSubProductList() {
		return subProductList;
	}

	public void setSubProductList(List<SubProduct> subProductList) {
		this.subProductList = subProductList;
	}

	public List<SubProduct> getColorProductList() {
		return colorProductList;
	}

	public void setColorProductList(List<SubProduct> colorProductList) {
		this.colorProductList = colorProductList;
	}

	public List<SubProduct> getSizeProductList() {
		return sizeProductList;
	}

	public void setSizeProductList(List<SubProduct> sizeProductList) {
		this.sizeProductList = sizeProductList;
	}

	public List<SubProduct> getRelationProductList() {
		return relationProductList;
	}

	public void setRelationProductList(List<SubProduct> relationProductList) {
		this.relationProductList = relationProductList;
	}

	public boolean isBuy() {
		return buy;
	}

	public void setBuy(boolean buy) {
		this.buy = buy;
	}

	public ProductBrand getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(ProductBrand productBrand) {
		this.productBrand = productBrand;
	}

	public List<DiscountActivity> getDiscountActivity() {
		return discountActivity;
	}

	public void setDiscountActivity(List<DiscountActivity> discountActivity) {
		this.discountActivity = discountActivity;
	}

	public List<PmInfoServicetype> getPmInfoServicetype() {
		return pmInfoServicetype;
	}

	public void setPmInfoServicetype(List<PmInfoServicetype> pmInfoServicetype) {
		this.pmInfoServicetype = pmInfoServicetype;
	}

}
