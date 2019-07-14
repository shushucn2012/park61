package com.park61.moduel.sales.cache;

import java.util.List;

import com.park61.moduel.sales.bean.CateCombine;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.moduel.sales.bean.ProductCategory;
import com.park61.moduel.sales.bean.ProductLimit;
import com.park61.moduel.sales.bean.PromotionType;

/**
 * 特卖缓存实体
 */
public class TemaiCacheBean {

	private int pageNum;
	private List<PromotionType> plist;
	private List<ProductCategory> topCateList;
	private List<ProductLimit> bottomGoodsList;
	private List<CateCombine> combList;
	private List<GoodsCombine> gcombList;

	public List<PromotionType> getPlist() {
		return plist;
	}

	public void setPlist(List<PromotionType> plist) {
		this.plist = plist;
	}

	public List<CateCombine> getCombList() {
		return combList;
	}

	public void setCombList(List<CateCombine> combList) {
		this.combList = combList;
	}

	public List<GoodsCombine> getGcombList() {
		return gcombList;
	}

	public void setGcombList(List<GoodsCombine> gcombList) {
		this.gcombList = gcombList;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public List<ProductCategory> getTopCateList() {
		return topCateList;
	}

	public void setTopCateList(List<ProductCategory> topCateList) {
		this.topCateList = topCateList;
	}

	public List<ProductLimit> getBottomGoodsList() {
		return bottomGoodsList;
	}

	public void setBottomGoodsList(List<ProductLimit> bottomGoodsList) {
		this.bottomGoodsList = bottomGoodsList;
	}

}
