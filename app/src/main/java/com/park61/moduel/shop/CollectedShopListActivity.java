package com.park61.moduel.shop;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.shop.adapter.CollectedListAdapter;
import com.park61.moduel.shop.bean.MerchantFocusVO;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CollectedShopListActivity extends BaseActivity {
	private int PAGE_NUM = 1;
	private final int PAGE_SIZE = 5;
	private List<MerchantFocusVO> collectedList;

	private PullToRefreshListView mPullRefreshListView;
	private CollectedListAdapter mAdapter;
	private TextView tv_empty_tip;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_collected_shops);
	}

	@Override
	public void initView() {
		tv_empty_tip = (TextView) findViewById(R.id.tv_empty_tip);
		tv_empty_tip.setText("暂无数据！");
		tv_empty_tip.setVisibility(View.GONE);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		ViewInitTool.initPullToRefresh(mPullRefreshListView,mContext);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						PAGE_NUM = 1;
						asyncGetShopCollected();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						PAGE_NUM++;
						asyncGetShopCollected();
					}
				});
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		collectedList = new ArrayList<MerchantFocusVO>();
		mAdapter = new CollectedListAdapter(mContext, collectedList);
		actualListView.setAdapter(mAdapter);
	}

	@Override
	public void initData() {
		asyncGetShopCollected();
	}

	@Override
	public void initListener() {

	}

	/**
	 * 请求收藏店铺列表数据
	 */
	private void asyncGetShopCollected() {
		String wholeUrl = AppUrl.host + AppUrl.GET_SHOP_COLLECTED;
		String requestBodyData = ParamBuild.getCollectedShopList(PAGE_NUM,
				PAGE_SIZE);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				getShopCollected);
	}

	BaseRequestListener getShopCollected = new JsonRequestListener() {

		@Override
		public void onStart(int requestId) {
			showDialog();
		}

		@Override
		public void onError(int requestId, String errorCode, String errorMsg) {
			dismissDialog();
			listStopLoadView();
			showShortToast(errorMsg);
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			dismissDialog();
			listStopLoadView();
			ArrayList<MerchantFocusVO> currentPageList = new ArrayList<MerchantFocusVO>();
			JSONArray actJay = jsonResult.optJSONArray("list");
			// 第一次查询的时候没有数据，则提示没有数据，页面置空
			if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
				tv_empty_tip.setVisibility(View.VISIBLE);
				collectedList.clear();
				mAdapter.notifyDataSetChanged();
				setPullToRefreshViewEnd();
				return;
			}
			// 首次加载清空所有项列表,并重置控件为可下滑
			if (PAGE_NUM == 1) {
				collectedList.clear();
				setPullToRefreshViewBoth();
			}
			// 如果当前页已经是最后一页，则列表控件置为不可下滑
			if (PAGE_NUM == jsonResult.optInt("totalPage")) {
				setPullToRefreshViewEnd();
			}
			// 解析数据
			for (int i = 0; i < actJay.length(); i++) {
				JSONObject actJot = actJay.optJSONObject(i);
				MerchantFocusVO c = new MerchantFocusVO();
				c = gson.fromJson(actJot.toString(), MerchantFocusVO.class);
				currentPageList.add(c);
			}
			// 加载数据列表
			collectedList.addAll(currentPageList);
			// 隐藏为空提示
			tv_empty_tip.setVisibility(View.GONE);
			// 更新显示列表
			mAdapter.notifyDataSetChanged();
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
