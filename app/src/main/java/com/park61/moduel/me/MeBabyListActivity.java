package com.park61.moduel.me;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.me.adapter.BabyListAdapter;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MeBabyListActivity extends BaseActivity {

	private PullToRefreshListView mPullRefreshListView;
	private BabyListAdapter mAdapter;
	private List<BabyItem> babyDataList;
	private Button btn_add_child;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_me_babylist);
	}

	@Override
	public void initView() {
		btn_add_child = (Button) findViewById(R.id.btn_add_child);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				asyncGetUserChilds();
			}
		});
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		babyDataList = new ArrayList<BabyItem>();
		mAdapter = new BabyListAdapter(mContext, babyDataList);
		actualListView.setAdapter(mAdapter);
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				Intent it = new Intent(mContext, BabyInfoActivity.class);
				it.putExtra("baby_info", babyDataList.get(position - 1));
				startActivity(it);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		asyncGetUserChilds();
	}

	@Override
	public void initData() {
	}

	@Override
	public void initListener() {
		btn_add_child.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, AddBabyActivity.class));
			}
		});
	}

	/**
	 * 获取用户孩子列表
	 */
	private void asyncGetUserChilds() {
		String wholeUrl = AppUrl.host + AppUrl.GET_USER_CHILDS_END;
		String requestBodyData = ParamBuild.getUserChilds();
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
			showShortToast(errorMsg);
			listStopLoadView();
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			dismissDialog();
			listStopLoadView();
			JSONArray jay = jsonResult.optJSONArray("list");
			babyDataList.clear();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject jot = jay.optJSONObject(i);
				BabyItem b = gson.fromJson(jot.toString(), BabyItem.class);
				babyDataList.add(b);
			}
			mAdapter.notifyDataSetChanged();
		}
	};

	protected void listStopLoadView() {
		mPullRefreshListView.onRefreshComplete();
	}

}
