package com.park61.moduel.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.ActDetailsActivity;
import com.park61.moduel.acts.adapter.ActListAdapter;
import com.park61.moduel.acts.bean.ActItem;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentActListByWeekday extends BaseFragment {

	private int PAGE_NUM = 1;
	private final int PAGE_SIZE = 5;
	private Long shopId;
	private String actBussinessType;
	private String actType;
	private String isFree;
	private String actStartTime, actEndTime;

	private TextView tv_empty_tip;
	private PullToRefreshListView mPullRefreshListView;
	private List<ActItem> actDataList = new ArrayList<ActItem>();
	private ActListAdapter adapter;
	private int curDayIndexOfArray;
	private int weekIndex;
	private boolean isNextWeek;// 是否是下周游戏

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.fragment_actlist_by_weekday,
				container, false);
		weekIndex = getArguments().getInt("weekIndex");
		actBussinessType = getArguments().getString("actBussinessType");
		actType = getArguments().getString("actType");
		isFree = getArguments().getString("isFree");
		isNextWeek = getArguments().getBoolean("isNextWeek");
		shopId = getArguments().getLong("shopId");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void initView() {
		tv_empty_tip = (TextView) parentView.findViewById(R.id.tv_empty_tip);
		tv_empty_tip.setText("暂无数据");
		tv_empty_tip.setVisibility(View.GONE);
		mPullRefreshListView = (PullToRefreshListView) parentView
				.findViewById(R.id.pull_refresh_list);
		ViewInitTool.initPullToRefresh(mPullRefreshListView,parentActivity);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						PAGE_NUM = 1;
						asyncGetShopActs();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						PAGE_NUM++;
						asyncGetShopActs();
					}
				});
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actDataList = new ArrayList<ActItem>();
		adapter = new ActListAdapter(parentActivity, actDataList);
		actualListView.setAdapter(adapter);
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(parentActivity, ActDetailsActivity.class);
				it.putExtra("id", actDataList.get(position - 1).getId());
				startActivity(it);
			}
		});
	}

	@Override
	public void initData() {
		// shopId = getIntent().getLongExtra("shopId", -1l);
		// actBussinessType = getIntent().getStringExtra("actBussinessType");
		// actStartTime = getIntent().getStringExtra("actStartTime");
		// actEndTime = getIntent().getStringExtra("actEndTime");
		// tv_page_title.setText(getIntent().getStringExtra("actTypeName"));

		Calendar c = Calendar.getInstance();
		int curDayIndexOfWeekInEn = c.get(Calendar.DAY_OF_WEEK);// 获致是本周的第几天地,
		curDayIndexOfArray = weekDay2WeekIndex(curDayIndexOfWeekInEn);

		Log.e("", "curDayIndexOfArray======" + curDayIndexOfArray);
		Log.e("", "weekIndex======" + weekIndex);

		String theDate = getActDateByWeekIndex(weekIndex);
		if (isNextWeek)
			theDate = getActDateByNextWeekIndex(weekIndex);
		// shopId = ActListByWeekdayActivity.shopId;
		actStartTime = theDate + " 00:00:00";
		actEndTime = theDate + " 23:59:59";
		asyncGetShopActs();
	}

	@Override
	public void initListener() {
	}

	/**
	 * 将周几转为星期数组的序号：英语日期 1代表星期天...7代表星期六
	 */
	private int weekDay2WeekIndex(int weekday) {
		// -----------周1-周2-周3-周4-周5-周6-周日
		// 我的数组标- -1--0---1---2---3---4---5
		// 英语第几天- 2---3---4---5---6---7---1
		int index = -1;
		if (weekday == Calendar.SUNDAY) { // 如果周日那么下标为5
			index = 5;
		} else {// 否则下标都是星期减3
			index = weekday - 3;
		}
		return index;
	}

	/**
	 * 通过星期序号获取对应日期
	 */
	protected String getActDateByWeekIndex(int weekIndex) {
		Calendar calendar = Calendar.getInstance();
		int roll = weekIndex - curDayIndexOfArray;
		calendar.add(Calendar.DATE, roll);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * 通过下星期序号获取对应日期
	 */
	protected String getActDateByNextWeekIndex(int weekIndex) {
		Calendar calendar = Calendar.getInstance();
		int roll = weekIndex - curDayIndexOfArray;
		calendar.add(Calendar.DATE, roll + 7);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * 根据分类、时间段查询店铺游戏列表
	 */
	private void asyncGetShopActs() {
		String wholeUrl = AppUrl.host + AppUrl.GET_SHOP_ACTS_END;
		String requestBodyData = ParamBuild.getShopActListByCond(shopId,
				PAGE_NUM, PAGE_SIZE, actBussinessType, actType, isFree,
				actStartTime, actEndTime);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				getShopActsTodayLsner);
	}

	BaseRequestListener getShopActsTodayLsner = new JsonRequestListener() {

		@Override
		public void onStart(int requestId) {
			// showDialog();
		}

		@Override
		public void onError(int requestId, String errorCode, String errorMsg) {
			// dismissDialog();
			listStopLoadView();
			showShortToast(errorMsg);
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			// dismissDialog();
			listStopLoadView();
			ArrayList<ActItem> currentPageList = new ArrayList<ActItem>();
			JSONArray actJay = jsonResult.optJSONArray("list");
			// 第一次查询的时候没有数据，则提示没有数据，页面置空
			if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
				// showShortToast("没有符合条件的数据！");
				tv_empty_tip.setVisibility(View.VISIBLE);
				actDataList.clear();
				adapter.notifyDataSetChanged();
				setPullToRefreshViewEnd();
				return;
			} else {// 隐藏为空提示
				tv_empty_tip.setVisibility(View.GONE);
			}
			// 首次加载清空所有项列表,并重置控件为可下滑
			if (PAGE_NUM == 1) {
				actDataList.clear();
				setPullToRefreshViewBoth();
			}
			// 如果当前页已经是最后一页，则列表控件置为不可下滑
			if (PAGE_NUM == jsonResult.optInt("totalPage")) {
				setPullToRefreshViewEnd();
			}
			// 解析数据
			for (int i = 0; i < actJay.length(); i++) {
				JSONObject actJot = actJay.optJSONObject(i);
				ActItem c = new ActItem();
				c = gson.fromJson(actJot.toString(), ActItem.class);
				currentPageList.add(c);
			}
			// 加载数据并刷新列表
			actDataList.addAll(currentPageList);
			adapter.notifyDataSetChanged();
			// if (PAGE_NUM == 1) {// 第一页滚动到头部
			// scrollToBottom(scrollView, my_content, currentPageList.size());
			// }
		}
	};

	/**
	 * 停止列表进度条
	 */
	protected void listStopLoadView() {
		mPullRefreshListView.onRefreshComplete();
	}

	/**
	 * 将上下拉控件设为到底
	 */
	protected void setPullToRefreshViewEnd() {
		mPullRefreshListView.setMode(Mode.PULL_FROM_START);
	}

	/**
	 * 将上下拉控件设为可上下拉
	 */
	protected void setPullToRefreshViewBoth() {
		mPullRefreshListView.setMode(Mode.BOTH);
	}

}
