package com.park61.moduel.sales;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.park61.BaseActivity;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.sales.bean.ProductCategory;
import com.park61.moduel.sales.fragment.FragmentSalesFirst;
import com.park61.moduel.sales.fragment.FragmentSalesSpecial;
import com.park61.moduel.shoppincart.TradeCartActivity;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.NetLoadingView;
import com.park61.widget.NetLoadingView.OnRefreshClickedLsner;
import com.park61.widget.viewpager.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SalesMainActivity extends BaseActivity {

	private PagerSlidingTabStrip tabs;// PagerSlidingTabStrip的实例
	private DisplayMetrics dm;// 获取当前屏幕的密度
	private ViewPager pager;
	private ImageView img_sousuo;
	private Button btn_msg;
	private View area_content;
	private NetLoadingView view_loading;

	private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
	private List<ProductCategory> bigcateList;
	private String cateId;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_sales_main);
		if (VERSION.SDK_INT < VERSION_CODES.KITKAT) {
			lowSdkLayoutInit();
		}
	}

	@Override
	public void initView() {
		setOverflowShowingAlways();
		area_content = findViewById(R.id.area_content);
		view_loading = (NetLoadingView) findViewById(R.id.view_loading);

		dm = getResources().getDisplayMetrics();
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

		img_sousuo = (ImageView) findViewById(R.id.img_sousuo);
		btn_msg = (Button) findViewById(R.id.btn_msg);
	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		tabs.setScrollOffset(0);
		tabs.setTabPaddingLeftRight(DevAttr.dip2px(mContext, 20));
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
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(getResources().getColor(R.color.com_orange));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(getResources().getColor(R.color.com_orange));
		// 设置正常Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setTextColor(getResources().getColor(R.color.g666666));
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
	}

	private void setTabsValueShort() {
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		tabs.setScrollOffset(0);
		tabs.setTabPaddingLeftRight(DevAttr.dip2px(mContext, 10));
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
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(getResources().getColor(R.color.com_orange));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(getResources().getColor(R.color.com_orange));
		// 设置正常Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setTextColor(getResources().getColor(R.color.g666666));
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
	}

	@Override
	public void initData() {
		bigcateList = new ArrayList<ProductCategory>();
		ProductCategory pc = new ProductCategory();
		pc.setId(-1l);
		pc.setCategoryName("特卖");
		bigcateList.add(pc);

		FragmentSalesFirst tMFragment = new FragmentSalesFirst();
		fragmentList.add(tMFragment);

		asyncGetTopNodes();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (GlobalParam.SalesMainNeedRefresh) {
			asyncGetTopNodes();
		}
	}

	/**
	 * 获取顶级类目
	 */
	protected void asyncGetTopNodes() {
		String wholeUrl = AppUrl.host + AppUrl.GET_TOP_NODE;
		String requestBodyData = "";
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				getTopNodesLsner);
	}

	BaseRequestListener getTopNodesLsner = new JsonRequestListener() {

		@Override
		public void onStart(int requestId) {
			view_loading.showLoading(area_content);
		}

		@Override
		public void onError(int requestId, String errorCode, String errorMsg) {
			view_loading.showRefresh();
			showShortToast(errorMsg);
			GlobalParam.SalesMainNeedRefresh = true;
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			view_loading.hideLoading(area_content);
			JSONArray jay = jsonResult.optJSONArray("list");
			bigcateList.clear();
			ProductCategory pc = new ProductCategory();
			pc.setId(-1l);
			pc.setCategoryName("特卖");
			bigcateList.add(pc);
			for (int i = 0; i < jay.length(); i++) {
				JSONObject jot = jay.optJSONObject(i);
				ProductCategory p = gson.fromJson(jot.toString(), ProductCategory.class);
				bigcateList.add(p);
				FragmentSalesSpecial fragment = new FragmentSalesSpecial();
				Bundle data = new Bundle();
				cateId = p.getId() + "";
				data.putString("cateId", cateId);
				fragment.setArguments(data);
				fragmentList.add(fragment);
			}
			pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
			tabs.setViewPager(pager);
			if (bigcateList.size() < 5) {
				setTabsValueShort();
			}else {
				setTabsValue();
			}
			tabs.setFadeEnabled(false);
			pager.setCurrentItem(0);
			// 重置
			GlobalParam.SalesMainNeedRefresh = false;
		}
	};

	@Override
	public void initListener() {
		img_sousuo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, CateNaviNSearchActivity.class));
			}
		});
		btn_msg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, TradeCartActivity.class));
			}
		});
		view_loading.setOnRefreshClickedLsner(new OnRefreshClickedLsner() {

			@Override
			public void OnRefreshClicked() {
				asyncGetTopNodes();
			}
		});
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return bigcateList.get(position).getCategoryName();
		}

		@Override
		public int getCount() {
			return bigcateList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public Fragment getItem(int position) {
			return fragmentList.get(position);
		}
	}

}
