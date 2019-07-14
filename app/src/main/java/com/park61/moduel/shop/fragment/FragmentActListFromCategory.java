package com.park61.moduel.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
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

import java.util.ArrayList;
import java.util.List;

public class FragmentActListFromCategory extends BaseFragment {

	private int PAGE_NUM = 1;
	private final int PAGE_SIZE = 5;
	private Long shopId;
	private String actStartTime, actEndTime;
	private String actType;

	private PullToRefreshListView mPullRefreshListView;
	private List<ActItem> actDataList = new ArrayList<ActItem>();
	private ActListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.fragment_category_actlist_by_weekday,
				container, false);
		actStartTime = getArguments().getString("actStartTime");
		actEndTime = getArguments().getString("actEndTime");
		shopId = getArguments().getLong("shopId");
		actType = getArguments().getString("actType");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void initView() {
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
		ViewInitTool.setListEmptyView(this.getActivity(), actualListView,
				this.getActivity().getResources().getString(R.string.shop_activity_no_data_title), R.drawable.quexing,
				null,100,95);
	}

	@Override
	public void initData() {
		asyncGetShopActs();
	}

	@Override
	public void initListener() {
	}

	/**
	 * 根据分类、时间段查询店铺游戏列表
	 */
	private void asyncGetShopActs() {
		String wholeUrl = AppUrl.host + AppUrl.GET_SHOP_ACTS_END;
		String requestBodyData = ParamBuild.getShopActListByCond(shopId,
				PAGE_NUM, PAGE_SIZE, actType, "", "", actStartTime, actEndTime);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				getShopActsTodayLsner);
	}

	BaseRequestListener getShopActsTodayLsner = new JsonRequestListener() {

		@Override
		public void onStart(int requestId) {
		}

		@Override
		public void onError(int requestId, String errorCode, String errorMsg) {
			listStopLoadView();
			showShortToast(errorMsg);
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			listStopLoadView();
			ArrayList<ActItem> currentPageList = new ArrayList<ActItem>();
			JSONArray actJay = jsonResult.optJSONArray("list");
			// 第一次查询的时候没有数据，则提示没有数据，页面置空
			if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
				actDataList.clear();
				adapter.notifyDataSetChanged();
				setPullToRefreshViewEnd();
				return;
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
				ActItem c = gson.fromJson(actJot.toString(),
						ActItem.class);
				currentPageList.add(c);
			}
			// 加载数据并刷新列表
			actDataList.addAll(currentPageList);
			adapter.notifyDataSetChanged();
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
