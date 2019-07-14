package com.park61.moduel.acts;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.park61.moduel.acts.adapter.ActTempListAdapter;
import com.park61.moduel.acts.bean.ActItem;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmallClassListActivity extends BaseActivity {

	private int PAGE_NUM = 1;
	private final int PAGE_SIZE = 10;
	private final int SMALL_CLASS_TYPE_CODE = 7;//小课类型编号

	private String activityMotif;
	private String title;
	private List<ActItem> actDataList;
	private ActTempListAdapter mActTempListAdapter;

	private PullToRefreshListView mPullRefreshListView;
	private ListView actualListView;
	private TextView tv_page_title;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_acttopic_list);
	}

	@Override
	public void initView() {
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		ViewInitTool.initPullToRefresh(mPullRefreshListView,mContext);
		actualListView = mPullRefreshListView.getRefreshableView();
		ViewInitTool.setListEmptyView(mContext, actualListView);
		tv_page_title = (TextView) findViewById(R.id.tv_page_title);
	}

	@Override
	public void initData() {
		tv_page_title.setText("小课");
		actDataList = new ArrayList<ActItem>();
		mActTempListAdapter = new ActTempListAdapter(mContext, actDataList);
		actualListView.setAdapter(mActTempListAdapter);
		asyncGetActTemps();
	}

	@Override
	public void initListener() {
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				PAGE_NUM = 1;
				asyncGetActTemps();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				PAGE_NUM++;
				asyncGetActTemps();
			}
		});
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(mContext, ActDetailsActivity.class);
				it.putExtra("actTempId", actDataList.get(position - 1).getId());
				startActivity(it);
			}
		});
	}

	/**
	 * 请求首页游戏模板数据
	 */
	private void asyncGetActTemps() {
		String wholeUrl = AppUrl.host + AppUrl.GET_HOME_ACTTEMP;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("curPage", PAGE_NUM);
		map.put("pageSize", PAGE_SIZE);
		map.put("actBussinessType", SMALL_CLASS_TYPE_CODE);
		String requestBodyData = ParamBuild.buildParams(map);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, listener);
	}

	BaseRequestListener listener = new JsonRequestListener() {

		@Override
		public void onStart(int requestId) {
			showDialog();
		}

		@Override
		public void onError(int requestId, String errorCode, String errorMsg) {
			dismissDialog();
			listStopLoadView();
			showShortToast(errorMsg);
			if (PAGE_NUM > 1) {// 如果是加载更多，失败时回退页码
				PAGE_NUM--;
			}
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			dismissDialog();
			listStopLoadView();
			JSONArray actJay = jsonResult.optJSONArray("list");
			// 第一次查询的时候没有数据，则提示没有数据，页面置空
			if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
				actDataList.clear();
				mActTempListAdapter.notifyDataSetChanged();
				setPullToRefreshViewEnd();
				return;
			}
			// 首次加载清空所有项列表,并重置控件为可下滑
			if (PAGE_NUM == 1) {
				actDataList.clear();
			}
			// 如果当前页已经是最后一页，则列表控件置为不可下滑
			if (PAGE_NUM == jsonResult.optInt("totalPage")) {
				setPullToRefreshViewEnd();
			}
			new ParseJsonTask().execute(actJay);
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

	/**
	 * 解析JSON数组
	 */
	private class ParseJsonTask extends AsyncTask<JSONArray, Integer, String> {
		@Override
		protected String doInBackground(JSONArray... params) {
			JSONArray actJay = params[0];
			ArrayList<ActItem> currentPageList = new ArrayList<ActItem>();
			for (int i = 0; i < actJay.length(); i++) {
				JSONObject actJot = actJay.optJSONObject(i);
				ActItem c = gson.fromJson(actJot.toString(), ActItem.class);
				currentPageList.add(c);
			}
			actDataList.addAll(currentPageList);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			mActTempListAdapter.notifyDataSetChanged();
		}
	}

}
