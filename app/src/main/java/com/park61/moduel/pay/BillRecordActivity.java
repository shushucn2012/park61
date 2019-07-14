package com.park61.moduel.pay;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.pay.adapter.BillRecListAdapter;
import com.park61.moduel.pay.bean.BillInfo;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillRecordActivity extends BaseActivity {

	private List<BillInfo> orderList;

	private PullToRefreshListView mPullRefreshListView;
	private BillRecListAdapter mAdapter;
	private ListView actualListView;
	private int PAGE_NUM = 1;
	private int PAGE_SIZE = 15;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_bill_record);
	}

	@Override
	public void initView() {
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(Mode.DISABLED);
		ViewInitTool.initPullToRefresh(mPullRefreshListView,mContext);
	}

	@Override
	public void initData() {
		actualListView = mPullRefreshListView.getRefreshableView();
		orderList = new ArrayList<BillInfo>();
		mAdapter = new BillRecListAdapter(mContext, orderList);
		actualListView.setAdapter(mAdapter);
		PAGE_NUM = 1;
		asyncGetBillRecord();
	}

	/**
	 * 获取余额
	 */
	protected void asyncGetBillRecord() {
//		String wholeUrl = AppUrl.host + AppUrl.GET_BILL_RECORDS;
		String wholeUrl = AppUrl.host + AppUrl.ACCOUNT_BALANCE_LIST;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currentPage", PAGE_NUM);
		map.put("pageSize", PAGE_SIZE);
		String requestBodyData = ParamBuild.buildParams(map);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getRecslistener);
	}

	BaseRequestListener getRecslistener = new JsonRequestListener() {

		@Override
		public void onStart(int requestId) {
			showDialog();
		}

		@Override
		public void onError(int requestId, String errorCode, String errorMsg) {
			dismissDialog();
			showShortToast(errorMsg);
			listStopLoadView();
			if (PAGE_NUM > 1) {// 如果是加载更多，失败时回退页码
				PAGE_NUM--;
			}
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			dismissDialog();
			listStopLoadView();
			ArrayList<BillInfo> currentPageList = new ArrayList<BillInfo>();
			JSONArray actJay = jsonResult.optJSONArray("list");
			if (PAGE_NUM == 1&&(actJay == null || actJay.length() <= 0)) {
				orderList.clear();
				ViewInitTool.setListEmptyView(mContext,actualListView,"还没有明细记录哦",
						R.drawable.quexing,null,100,95);
				mAdapter.notifyDataSetChanged();
				return;
			}
			if (PAGE_NUM == 1) {
				orderList.clear();
				setPullToRefreshViewBoth();
			}
			if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
				setPullToRefreshViewEnd();
			} else {
				setPullToRefreshViewBoth();
			}
			// 解析数据
			for (int i = 0; i < actJay.length(); i++) {
				JSONObject actJot = actJay.optJSONObject(i);
				BillInfo c = gson.fromJson(actJot.toString(), BillInfo.class);
				currentPageList.add(c);
			}
			orderList.addAll(currentPageList);
			mAdapter.notifyDataSetChanged();
		}
	};

	@Override
	public void initListener() {
		mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				PAGE_NUM = 1;
				asyncGetBillRecord();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				PAGE_NUM++;
				asyncGetBillRecord();
			}
		});
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				Intent it = new Intent(mContext, BillDetailsActivity.class);
				it.putExtra("id",orderList.get(position-1).getId());
				startActivity(it);
			}
		});
	}
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
