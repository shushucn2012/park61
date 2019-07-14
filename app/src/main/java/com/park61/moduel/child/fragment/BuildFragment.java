package com.park61.moduel.child.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.data.ChartData;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.child.BabyBuildActivity;
import com.park61.moduel.child.BuildHeightInputActivity;
import com.park61.moduel.child.BuildWeightInputActivity;
import com.park61.moduel.child.adapter.BuildRecListAdapter;
import com.park61.moduel.child.bean.GrowingRecBean;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildFragment extends BaseFragment {

//	private Button btn_cur_data;
//	private TextView tv_com_range;
//	private TextView tv_tip;
	private PullToRefreshListView mPRListView;
	private BuildRecListAdapter adapter;

	private WebView webViewChart;
	private Typeface mTf;
	protected ChartData<?> mChartData;

	private int build_type=0; // 体格类型 0 身高 1 体重
	// private int

	private BabyItem showingBb;// 当前显示的宝宝
	private List<GrowingRecBean> buildDataList;
	
	ScrollView tab_scrollView;
	LinearLayout tab_lay_list;

	private int PAGE_NUM = 1;
	private final int PAGE_SIZE = 5;
	private boolean isEnd;// 列表是否到底

	//private RadioGroup rg_list_type;
	private boolean hasTodayData = false;

	public BuildFragment(){

	}

	@SuppressLint("ValidFragment")
	public BuildFragment(int build_type, BabyItem showingBb) {
		this.build_type = build_type;
		this.showingBb = showingBb;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.fragment_baby_build2, container, false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void initView() {
		tab_scrollView = (ScrollView) parentView.findViewById(R.id.tab_scrollView);
		tab_lay_list = (LinearLayout) parentView.findViewById(R.id.tab_lay_list);
		initBaseBabyInfo(tab_scrollView);
		initBaseBabyInfo(tab_lay_list);
		
		mPRListView = (PullToRefreshListView) parentView.findViewById(R.id.pull_refresh_list);
		webViewChart = (WebView) parentView.findViewById(R.id.webView);
				
		TextView tv_lst_data = (TextView) parentView.findViewById(R.id.tv_lst_data);	
		if(build_type == BabyBuildActivity.BUILD_TYPE_WEIGHT){
			tv_lst_data.setText("体重");
			((RadioButton) tab_scrollView.findViewById(R.id.rb_tab_chart)).setText("体重曲线");
			((RadioButton) tab_lay_list.findViewById(R.id.rb_tab_chart)).setText("体重曲线");
		}else{
			tv_lst_data.setText("身高");
			((RadioButton) tab_scrollView.findViewById(R.id.rb_tab_chart)).setText("身高曲线");
			((RadioButton) tab_lay_list.findViewById(R.id.rb_tab_chart)).setText("身高曲线");
		}	
	}
	
	private void initBaseBabyInfo(View view){
		ImageManager.getInstance().displayImg((ImageView) view.findViewById(R.id.img_baby), showingBb.getPictureUrl());
		((TextView) view.findViewById(R.id.tv_date)).setText("今天是" + DateTool.getSystemDateInCn());
		((TextView) view.findViewById(R.id.tv_nameaage))
				.setText(showingBb.getPetName() + " " + showingBb.getAge() + "");
	}

	@Override
	public void initData() {
		ListView actualListView = mPRListView.getRefreshableView();
		buildDataList = new ArrayList<GrowingRecBean>();
		adapter = new BuildRecListAdapter(parentActivity, buildDataList);
		actualListView.setAdapter(adapter);
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				updateValue(position-1);
			}
		});
		asyncGetGrowingDataRg();
		initChart();
		asyncGetGrowingRecs();
	}
	
	private void updateValue(int position){
		if(build_type == 1){
			Intent it = new Intent(parentActivity, BuildHeightInputActivity.class);
			it.putExtra("mode", BuildHeightInputActivity.MODE_UPDATE + "");
			it.putExtra("recId", buildDataList.get(position).getId());
			it.putExtra("gDate", buildDataList.get(position).getGrowingDate());
			it.putExtra("oldHeight", buildDataList.get(position).getHeight());
			it.putExtra("minHeight", buildDataList.get(position).getMinHeight());
			parentActivity.startActivityForResult(it, BuildHeightInputActivity.MODE_UPDATE);
		}else{
			Intent it = new Intent(parentActivity, BuildWeightInputActivity.class);
			it.putExtra("mode", BuildHeightInputActivity.MODE_UPDATE + "");
			it.putExtra("recId", buildDataList.get(position).getId());
			it.putExtra("gDate", buildDataList.get(position).getGrowingDate());
			it.putExtra("oldWeight", buildDataList.get(position).getWeight());
			it.putExtra("minWeight", buildDataList.get(position).getMinWeight());
			parentActivity.startActivityForResult(it, BuildHeightInputActivity.MODE_UPDATE);
		}
	}
	
	private void addValue(){
		Intent it = new Intent(parentActivity, build_type == 1 ? BuildHeightInputActivity.class:BuildWeightInputActivity.class);
		it.putExtra("mode", BuildHeightInputActivity.MODE_ADD + "");
		it.putExtra("childId", showingBb.getId());
		parentActivity.startActivityForResult(it, BuildHeightInputActivity.MODE_ADD);
	}

	@Override
	public void initListener() {
		((RadioGroup) tab_scrollView.findViewById(R.id.mtab_group)).setOnCheckedChangeListener(listtypeCheckedChangeListener);
		((RadioGroup) tab_lay_list.findViewById(R.id.mtab_group)).setOnCheckedChangeListener(listtypeCheckedChangeListener);
		
		((Button) tab_scrollView.findViewById(R.id.btn_cur_data)).setOnClickListener(curdataClickListener);
		((Button) tab_lay_list.findViewById(R.id.btn_cur_data)).setOnClickListener(curdataClickListener);

		mPRListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				refreshRecList();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if (isEnd) {
					showShortToast("已经是最后一页了");
					listStopLoadView();
					return;
				}
				PAGE_NUM++;
				asyncGetGrowingRecs();
			}
		});
		
	}
	
	OnCheckedChangeListener listtypeCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == R.id.rb_tab_chart) {
				if(tab_scrollView.getVisibility() == View.VISIBLE){
					return;
				}
				tab_scrollView.scrollTo(0, 0);
				tab_scrollView.setVisibility(View.VISIBLE);
				//tab_scrollView.fullScroll(ScrollView.FOCUS_UP);

				tab_lay_list.setVisibility(View.GONE);
			} else {
				if(tab_scrollView.getVisibility() == View.GONE){
					return;
				}
				tab_scrollView.setVisibility(View.GONE);
				tab_lay_list.setVisibility(View.VISIBLE);
			}
			
			((RadioButton)tab_lay_list.findViewById(checkedId)).setChecked(true);
			((RadioButton)tab_scrollView.findViewById(checkedId)).setChecked(true);
		}
	};
	
	OnClickListener curdataClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(hasTodayData){
				updateValue(0);
			}else{
				addValue();
			}
		}
	};

	/**
	 * 获取体格列表
	 */
	private void asyncGetGrowingRecs() {
		String wholeUrl = AppUrl.host + AppUrl.GET_GROWING_RECS;
		String requestBodyData = ParamBuild.getGrowingRecs(PAGE_NUM, PAGE_SIZE, showingBb.getId(), build_type);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, build_type, getRecsLsner);
	}

	BaseRequestListener getRecsLsner = new JsonRequestListener() {

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
			ArrayList<GrowingRecBean> currentPageList = new ArrayList<GrowingRecBean>();
			JSONArray actJay = jsonResult.optJSONArray("list");
			// 第一次查询的时候没有数据，则提示没有数据，页面置空
			if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
				showShortToast("没有符合条件的数据！");
				buildDataList.clear();
				adapter.notifyDataSetChanged();
				// 没有列表
				//asyncGetGrowingDataRg();
				return;
			}
			// 首次加载清空所有项列表,并重置控件为可下滑
			if (PAGE_NUM == 1) {
				buildDataList.clear();
			}
			// 如果当前页已经是最后一页，则列表控件置为不可下滑
			if (PAGE_NUM == jsonResult.optInt("totalPage")) {
				isEnd = true;
				//showShortToast("已经是最后一页了");
				setPullToRefreshViewEnd();
			}else{
				setPullToRefreshViewBoth();
			}
			for (int i = 0; i < actJay.length(); i++) {
				JSONObject actJot = actJay.optJSONObject(i);
				GrowingRecBean g = gson.fromJson(actJot.toString(), GrowingRecBean.class);
				currentPageList.add(g);
			}
			buildDataList.addAll(currentPageList);
			adapter.notifyDataSetChanged();
			updateCurDayData(tab_scrollView);
			updateCurDayData(tab_lay_list);
		}
	};

	/**
	 * 获取体格数据正常范围
	 */
	private void asyncGetGrowingDataRg() {
		String wholeUrl = AppUrl.host + AppUrl.GET_GROWING_DATA_RG;
		String requestBodyData = ParamBuild.getGrowingDataRg(showingBb.getId(), DateTool.getSystemDate());
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getRangeLsner);
	}

	BaseRequestListener getRangeLsner = new JsonRequestListener() {

		@Override
		public void onStart(int requestId) {
			showDialog();
		}

		@Override
		public void onError(int requestId, String errorCode, String errorMsg) {
			dismissDialog();
			showShortToast(errorMsg);
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			dismissDialog();
			if (build_type == 1) {
				setRangeText(
						"正常范围：" + jsonResult.optString("heightMin") + "~" + jsonResult.optString("heightMax") + "厘米");
			} else {
				setRangeText(
						"正常范围：" + jsonResult.optString("weightMin") + "~" + jsonResult.optString("weightMax") + "公斤");
			}
		}
	};

	private void setRangeText(String text){
		((TextView) tab_scrollView.findViewById(R.id.tv_com_range)).setText(text);
		((TextView) tab_lay_list.findViewById(R.id.tv_com_range)).setText(text);
	}

	protected void listStopLoadView() {
		mPRListView.onRefreshComplete();
	}

	/**
	 * 更新当天主区域显示的体测数据
	 */
	protected void updateCurDayData(View view) {

		Button btn_cur_data = (Button) view.findViewById(R.id.btn_cur_data);
		TextView tv_com_range = (TextView) view.findViewById(R.id.tv_com_range);
		TextView tv_tip = (TextView) view.findViewById(R.id.tv_tip);
		
		GrowingRecBean firstRec = buildDataList.get(0);
		Long firstItemDate = Long.parseLong(firstRec.getGrowingDate());
		int diffDays = DateTool.dateDiff(firstItemDate, new Date().getTime());
		if (diffDays > 0) {// 今天没有数据
			//asyncGetGrowingDataRg();
		} else if (diffDays == 0) {// 今天有数据
			hasTodayData = true;
			btn_cur_data.setBackgroundResource(R.drawable.circle_shape_build_orange_fill);
			if (build_type == 0) {
				// 创建一个 SpannableString对象
				SpannableString msp = new SpannableString(firstRec.getWeight() + "\n公斤");
				msp.setSpan(new AbsoluteSizeSpan(22, true), 0, msp.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				btn_cur_data.setText(msp);
				btn_cur_data.setTextColor(getResources().getColor(R.color.gffffff));
				//tv_com_range.setText("正常范围：" + firstRec.getMinWeight() + "~" + firstRec.getMaxWeight() + "公斤");
				if (!"正常".equals(firstRec.getState())) {
					tv_tip.setText("宝宝体重" + firstRec.getState() + "，要注意哦！");
				} else {
					tv_tip.setText("宝宝体重在正常范围，继续保持！");
				}
			} else {
				// 创建一个 SpannableString对象
				SpannableString msp = new SpannableString(FU.strDbFmt(firstRec.getHeight()) + "\n厘米");
				msp.setSpan(new AbsoluteSizeSpan(22, true), 0, msp.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				btn_cur_data.setText(msp);
				btn_cur_data.setTextColor(getResources().getColor(R.color.gffffff));
				//tv_com_range.setText("正常范围：" + firstRec.getMinHeight() + "~" + firstRec.getMaxHeight() + "厘米");
				if (!"正常".equals(firstRec.getState())) {
					tv_tip.setText("宝宝身高" + firstRec.getState() + "，要注意哦！");
				} else {
					tv_tip.setText("宝宝身高在正常范围，继续保持！");
				}
			}
		}
	}

	/**
	 * 初始化图表
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initChart() {
		WebSettings webSettings = webViewChart.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);

		webSettings.setAppCacheEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setDatabaseEnabled(true);

		String url = String.format(AppUrl.BUILD_CHART, showingBb.getId(), build_type);;
		syncCookie(url);
		webViewChart.loadUrl(url,netRequest.getPrivateHeaders());
		webViewChart.setWebViewClient(new WebViewClient(){
		      @Override
		      public boolean shouldOverrideUrlLoading(WebView view, String url) {
		          view.loadUrl(url);
		          return true;
		      }
		      
		      public void onPageFinished(WebView view, String url) {
		            CookieManager cookieManager = CookieManager.getInstance();
		            String CookieStr = cookieManager.getCookie(url);
		            Log.e("----sunzn", "Cookies = " + CookieStr);
		            super.onPageFinished(view, url);
		        }
		  });
	}
	
	private void syncCookie( String url){
        try{
            Log.d("Nat: webView.syncCookie.url", url);           

            CookieSyncManager.createInstance(parentActivity);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(url);
            if(oldCookie != null){
                Log.d("Nat: webView.syncCookieOutter.oldCookie", oldCookie);
            }

        	StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("ut=%s",GlobalParam.userToken));
            sbCookie.append(";domain=" + AppUrl.BUILD_CHART_DOMAIN);
            sbCookie.append(";path=/");

            String cookieValue = sbCookie.toString();
            Log.i("--cookieValue", cookieValue);
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync(); 

            String newCookie = cookieManager.getCookie(url);
            if(newCookie != null){
                Log.d("Nat: webView.syncCookie.newCookie", newCookie);
            }
        }catch(Exception e){
            Log.e("Nat: webView.syncCookie failed", e.toString());
        }
    }
	

	
	public Map<String, String> getPrivateHeaders() {
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		header.put("domain", ".m.61park.cn");
		header.put("ut", GlobalParam.userToken);
		header.put("path", "/");
		header.put("ut", GlobalParam.userToken);
		JSONObject clientInfoJot = new JSONObject();
		try {
			clientInfoJot.put("clientAppName", "android");
			clientInfoJot.put("clientAppVersion", GlobalParam.versionName);
			clientInfoJot.put("clientSystem", "android");
			clientInfoJot.put("clientVersion", android.os.Build.VERSION.RELEASE);
			clientInfoJot.put("deviceCode", GlobalParam.macAddress);
			clientInfoJot.put("longitude", GlobalParam.longitude);
			clientInfoJot.put("latitude", GlobalParam.latitude);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		header.put("clientInfo", clientInfoJot.toString());
		Log.e("httpheader", "------httpheader======" + header);
		return header;
	}

	/**
	 * 刷新列表数据
	 */
	public void refreshRecList() {
		PAGE_NUM = 1;
		isEnd = false;
		asyncGetGrowingRecs();
	}
	
	/**
	 * 将上下拉控件设为到底
	 */
	protected void setPullToRefreshViewEnd() {
		mPRListView.setMode(Mode.PULL_FROM_START);
	}

	/**
	 * 将上下拉控件设为可上下拉
	 */
	protected void setPullToRefreshViewBoth() {
		mPRListView.setMode(Mode.BOTH);
	}

}
