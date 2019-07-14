package com.park61.moduel.sales.adapter;

import android.content.Context;

import com.park61.moduel.sales.AdapterDelegate.BrandRecommendDelegate;
import com.park61.moduel.sales.AdapterDelegate.SalesFmGoodsDelegate;
import com.park61.moduel.sales.AdapterDelegate.SalesHotIconDelegate;
import com.park61.moduel.sales.AdapterDelegate.SalesPageBanner2HeaderDelegate;
import com.park61.moduel.sales.AdapterDelegate.SalesPageBannerHeaderDelegate;
import com.park61.moduel.sales.AdapterDelegate.SalesPagePromotionNavHeaderDelegate;
import com.park61.moduel.sales.AdapterDelegate.SalesPagePromotionRecommendHeaderDelegate;
import com.park61.moduel.sales.AdapterDelegate.ShopkeeperSuggestHeaderDelegate;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.widget.myrv.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by super
 */
public class MySalesFirstAdapter extends MultiItemTypeAdapter<GoodsCombine> {

    public MySalesFirstAdapter(Context context, List<GoodsCombine> mlsit) {
        super(context, mlsit);
        addItemViewDelegate(new SalesPageBannerHeaderDelegate(context));// bannerone
        addItemViewDelegate(new SalesPagePromotionNavHeaderDelegate(context));// 分类导航栏
        addItemViewDelegate(new SalesPageBanner2HeaderDelegate(context));//  bannertwo
        addItemViewDelegate(new SalesPagePromotionRecommendHeaderDelegate(context)); //促销分类
        addItemViewDelegate(new ShopkeeperSuggestHeaderDelegate(context)); //店长推荐
        addItemViewDelegate(new BrandRecommendDelegate(context)); //品牌推荐
        addItemViewDelegate(new SalesHotIconDelegate()); //品牌推荐
        addItemViewDelegate(new SalesFmGoodsDelegate(context)); //品牌推荐
    }
}