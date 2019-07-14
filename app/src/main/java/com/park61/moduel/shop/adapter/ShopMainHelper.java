package com.park61.moduel.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.park61.R;
import com.park61.common.json.NullStringToEmptyAdapterFactory;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.shop.bean.MerchantFocusVO;
import com.park61.net.base.Request.Method;
import com.park61.net.request.StringNetRequest;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.viewpager.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShopMainHelper {

	private static ShopMainHelper instance;

	private StringNetRequest netRequest;
	private Context mContext;
	private boolean isFocus;
	private ImageView img_collect;

	private ShopMainHelper(Context c) {
		mContext = c;
		netRequest = StringNetRequest.getInstance(c);
		netRequest.setMainContext(c);
	}

	public static ShopMainHelper getInstance(Context c) {
		if (instance == null)
			instance = new ShopMainHelper(c);
		return instance;
	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	public void setTabsValue(PagerSlidingTabStrip tabs) {
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		tabs.setScrollOffset(0);
		tabs.setTabPaddingLeftRight(DevAttr.dip2px(mContext, 15));
		tabs.setDividerPadding(0);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		tabs.setUnderlineColor(Color.TRANSPARENT);
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 12, dm));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(mContext.getResources().getColor(
				R.color.com_orange));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(mContext.getResources().getColor(
				R.color.com_orange));
		// 设置正常Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setTextColor(mContext.getResources().getColor(R.color.g666666));
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	public void setTabsValue(PagerSlidingTabStrip tabs, int textSize) {
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		tabs.setScrollOffset(0);
		tabs.setTabPaddingLeftRight(DevAttr.dip2px(mContext, 15));
		tabs.setDividerPadding(0);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		tabs.setUnderlineColor(Color.TRANSPARENT);
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, textSize, dm));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(mContext.getResources().getColor(
				R.color.com_orange));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(mContext.getResources().getColor(
				R.color.com_orange));
		// 设置正常Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setTextColor(mContext.getResources().getColor(R.color.g666666));
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
	}

	public void judgeIsCollected(ImageView img_collect, Long shopId) {
		this.img_collect = img_collect;
		if (GlobalParam.userToken != null) {// 登录状态下验证
			asyncGetShopCollected(shopId);
		}
	}

	/**
	 * 请求收藏店铺列表数据
	 */
	private void asyncGetShopCollected(Long shopId) {
		String wholeUrl = AppUrl.host + AppUrl.GET_SHOP_COLLECTED;
		String requestBodyData = ParamBuild.getCollectedShopList(1, 100);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData,
				shopId.intValue(), getShopCollected);
	}

	private BaseRequestListener getShopCollected = new JsonRequestListener() {

		@Override
		public void onStart(int requestId) {
		}

		@Override
		public void onError(int requestId, String errorCode, String errorMsg) {
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			ArrayList<MerchantFocusVO> currentPageList = new ArrayList<MerchantFocusVO>();
			JSONArray actJay = jsonResult.optJSONArray("list");
			// 第一次查询的时候没有数据，则提示没有数据，页面置空
			if (actJay == null || actJay.length() <= 0) {
				return;
			}
			for (int i = 0; i < actJay.length(); i++) {
				JSONObject actJot = actJay.optJSONObject(i);
				MerchantFocusVO c = new MerchantFocusVO();
				Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
				c = gson.fromJson(actJot.toString(), MerchantFocusVO.class);
				currentPageList.add(c);
			}
			for (MerchantFocusVO mf : currentPageList) {
				if (mf.getMerchant().getId().intValue() == requestId) {
					isFocus = true;
					img_collect
							.setBackgroundResource(R.drawable.yishoucang_focus);
					return;
				}
			}
			isFocus = false;
		}
	};

	/**
	 * 请求收藏店铺
	 */
	public void asyncFocusShop(Long shopId) {
		String wholeUrl = AppUrl.host + AppUrl.FOCUS_SHOP;
		String requestBodyData = ParamBuild.collectShop(shopId);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				focusShopLsner);
	}

	/**
	 * 请求取消收藏店铺
	 */
	public void asyncUnFocusShop(Long shopId) {
		String wholeUrl = AppUrl.host + AppUrl.UNFOCUS_SHOP;
		String requestBodyData = ParamBuild.collectShop(shopId);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 1,
				focusShopLsner);
	}

	BaseRequestListener focusShopLsner = new JsonRequestListener() {

		@Override
		public void onStart(int requestId) {
		}

		@Override
		public void onError(int requestId, String errorCode, String errorMsg) {
			showShortToast(errorMsg);
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			if (requestId == 0) {// 收藏请求，则收藏置为focus图
				img_collect.setBackgroundResource(R.drawable.yishoucang_focus);
				isFocus = true;
				showShortToast("收藏成功！");
			} else if (requestId == 1) {// 收藏取消请求，则收藏置为unfocus图
				img_collect.setBackgroundResource(R.drawable.shop_shoucang_btn);
				isFocus = false;
				showShortToast("已取消收藏！");
			}
		}
	};

	public boolean getIsFocus() {
		return isFocus;
	}

	/**
	 * toast短提示
	 */
	public void showShortToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

}
