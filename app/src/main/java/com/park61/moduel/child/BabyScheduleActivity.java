package com.park61.moduel.child;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.ActsOrderDetailActivity;
import com.park61.moduel.acts.bean.ActItem;
import com.park61.moduel.child.adapter.BabyScheduleActivityListAdapter;
import com.park61.moduel.child.bean.ActivityRespose;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BabyScheduleActivity extends BaseActivity implements OnClickListener {

	private LinearLayout scheduleWeekLlayout, scheduleDateLlayout, activityEmptyViewLlayout, notJoinActivityLlayout,
			joinActivityLlayout;
	private List<ActivityRespose> queryService = new ArrayList<ActivityRespose>(7);
	private ListView joinList, notJoinList;
	private BabyScheduleActivityListAdapter noJoinBabyScheduleActivityListAdapter, joinBabyScheduleActivityListAdapter;
	private PullToRefreshScrollView pullRefreshScrollView;
	private List<Date> dateArrayList = new ArrayList<>(7);
	private Long curChildId;
	private int mCurrentSelectedWeekPosition = 0;
	private String[] weekDaysName = { "日", "一", "二", "三", "四", "五", "六" };

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_baby_schedule);
	}

	@Override
	public void initView() {
		scheduleDateLlayout = (LinearLayout) findViewById(R.id.llayout_date_container);
		scheduleWeekLlayout = (LinearLayout) findViewById(R.id.llayout_week_container);
		activityEmptyViewLlayout = (LinearLayout) findViewById(R.id.llayout_empty_activity_container);
		notJoinActivityLlayout = (LinearLayout) findViewById(R.id.llayout_not_join_activity_container);
		joinActivityLlayout = (LinearLayout) findViewById(R.id.llayout_join_activity_container);
		joinList = (ListView) findViewById(R.id.list_join);
		notJoinList = (ListView) findViewById(R.id.list_not_join);
		pullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		ViewInitTool.initPullToRefresh(pullRefreshScrollView,mContext);
		initArrayListDate();

		for (int i = 0; i < dateArrayList.size(); i++) {
			if (DateTool.getDateTimeStr(dateArrayList.get(i), "yyyy-MM-dd")
					.equals(new DateTime().toString("yyyy-MM-dd"))) {
				mCurrentSelectedWeekPosition = i;
			}
			createWeekChildView(i);
			createDateChildView(i);
		}
		noJoinBabyScheduleActivityListAdapter = new BabyScheduleActivityListAdapter(mContext,
				queryService.get(mCurrentSelectedWeekPosition).getNotJoinActivity());
		joinBabyScheduleActivityListAdapter = new BabyScheduleActivityListAdapter(mContext,
				queryService.get(mCurrentSelectedWeekPosition).getJoinActivity());

		joinList.setAdapter(joinBabyScheduleActivityListAdapter);
		notJoinList.setAdapter(noJoinBabyScheduleActivityListAdapter);
		notJoinList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent it = new Intent(mContext,
						ActsOrderDetailActivity.class);
				it.putExtra("applyId",queryService.get(mCurrentSelectedWeekPosition).getNotJoinActivity().get(position).getId());
				it.putExtra("orderState","applied");
				startActivity(it);
			}
		});
	}

	@Override
	public void initData() {
		curChildId = getIntent().getLongExtra("childId", -1l);
		listViewFillData();
	}

	@Override
	public void initListener() {

		pullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// 请求数据
				asyncGetActs();
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (null != scheduleDateLlayout.getTag() && scheduleDateLlayout.getTag().toString().equals(v.getId())) {
			return;
		}
		for (int i = 0; i < 7; i++) {
			TextView cancelSelectWeekTxt = (TextView) findViewById(i);
			cancelSelectWeekTxt.setBackgroundColor(Color.TRANSPARENT);
		}
		TextView selectWeekTxt = (TextView) findViewById(v.getId());
		selectWeekTxt.setBackgroundResource(R.drawable.current_week_shape);
		scheduleDateLlayout.setTag(v.getId());
		mCurrentSelectedWeekPosition = v.getId();
		changeWeekRefreshAct();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 请求数据
		asyncGetActs();
	}

	private void changeWeekRefreshAct() {
		// 刷新游戏数据
		noJoinBabyScheduleActivityListAdapter
				.setmList(queryService.get(mCurrentSelectedWeekPosition).getNotJoinActivity());
		joinBabyScheduleActivityListAdapter.setmList(queryService.get(mCurrentSelectedWeekPosition).getJoinActivity());
		listViewFillData();
	}

	private DateTime midDate;

	/**
	 * 初始化当前时间和之后的6天日期集合
	 */
	private void initArrayListDate() {
		midDate = new DateTime();
		midDate = midDate.withDayOfWeek(DateTimeConstants.THURSDAY);

		for (int i = -4; i <= 2; i++) {
			dateArrayList.add(midDate.plusDays(i).toDate());
			ActivityRespose actOtherRes = new ActivityRespose();
			actOtherRes.setDateOfDay(midDate.plusDays(i).toString("yyyy-MM-dd"));
			queryService.add(actOtherRes);
		}
	}

	private void createWeekChildView(int i) {
		LinearLayout weekContainerLlayout = new LinearLayout(mContext);
		weekContainerLlayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams weekParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		weekParams.weight = 1;
		weekContainerLlayout.setLayoutParams(weekParams);
		weekContainerLlayout.setGravity(Gravity.CENTER);
		TextView weekChildTxt = new TextView(this);
		weekChildTxt.setGravity(Gravity.CENTER);
		weekChildTxt.setTextAppearance(mContext, R.style.text_33312_style);
		weekChildTxt.setText(weekDaysName[i]);
		weekChildTxt.setTextColor(mContext.getResources().getColor(R.color.g999999));
		LinearLayout.LayoutParams paramsWeek = new LinearLayout.LayoutParams(
				(int) (mContext.getResources().getDimension(R.dimen.activity_week_bg_size)),
				(int) (mContext.getResources().getDimension(R.dimen.activity_week_bg_size)));
		weekChildTxt.setLayoutParams(paramsWeek);
		weekContainerLlayout.addView(weekChildTxt);
		scheduleDateLlayout.addView(weekContainerLlayout);
	}

	private void createDateChildView(int i) {
		LinearLayout dateContainerLlayout = new LinearLayout(mContext);
		dateContainerLlayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		params.weight = 1;
		dateContainerLlayout.setLayoutParams(params);
		dateContainerLlayout.setGravity(Gravity.CENTER);
		TextView dateChildTxt = new TextView(this);
		dateChildTxt.setId(i);
		dateChildTxt.setOnClickListener(this);
		LinearLayout.LayoutParams paramsWeek = new LinearLayout.LayoutParams(
				(int) (mContext.getResources().getDimension(R.dimen.activity_week_bg_size)),
				(int) (mContext.getResources().getDimension(R.dimen.activity_week_bg_size)));
		dateChildTxt.setLayoutParams(paramsWeek);
		dateChildTxt.setText("" + DateTool.getDayOfDate(dateArrayList.get(i)));
		dateChildTxt.setTextAppearance(mContext, R.style.text_33314_style);
		dateChildTxt.setGravity(Gravity.CENTER);
		if (DateTool.getDateTimeStr(dateArrayList.get(i), "yyyy-MM-dd").equals(new DateTime().toString("yyyy-MM-dd"))) {
			dateChildTxt.setBackgroundResource(R.drawable.current_week_shape);
		}
		dateContainerLlayout.addView(dateChildTxt);
		scheduleWeekLlayout.addView(dateContainerLlayout);
	}

	private void listViewFillData() {
		if (queryService.get(mCurrentSelectedWeekPosition).getJoinActivity().size() <= 0
				&& queryService.get(mCurrentSelectedWeekPosition).getNotJoinActivity().size() <= 0) {
			activityEmptyViewLlayout.setVisibility(View.VISIBLE);
			notJoinActivityLlayout.setVisibility(View.GONE);
			joinActivityLlayout.setVisibility(View.GONE);
		} else {
			activityEmptyViewLlayout.setVisibility(View.GONE);
			notJoinActivityLlayout
					.setVisibility(queryService.get(mCurrentSelectedWeekPosition).getNotJoinActivity().size() > 0
							? View.VISIBLE : View.GONE);
			joinActivityLlayout
					.setVisibility(queryService.get(mCurrentSelectedWeekPosition).getJoinActivity().size() > 0
							? View.VISIBLE : View.GONE);
			noJoinBabyScheduleActivityListAdapter.notifyDataSetChanged();
			joinBabyScheduleActivityListAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 获取游戏列表
	 */
	private void asyncGetActs() {
		String wholeUrl = AppUrl.host + AppUrl.GET_BABY_CALENDAR;
		String requestBodyData = ParamBuild.getBabySchedules(curChildId);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getActSchedulesLsner);
	}

	BaseRequestListener getActSchedulesLsner = new JsonRequestListener() {

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
			JSONArray jay = jsonResult.optJSONArray("list");
			try {
				clearServiceListData();
				for (int i = 0; i < jay.length(); i++) {
					JSONObject jot = jay.optJSONObject(i);
					JSONArray jaJoined = jot.getJSONArray("joined");
					JSONArray jaNotJoin = jot.getJSONArray("unjoin");
					setUnJoinAndJoinedList(DateTool.L2SEndDay(jot.getString("date")), jaJoined, jaNotJoin);
				}
				// 解析完数据后，更新游戏列表数据
				listViewFillData();
				// 解析完数据后，更新星期，如果有数据，则修改星期的显示颜色
				for (int j = 0; j < queryService.size(); j++) {
					if (queryService.get(j).getJoinActivity().size() > 0
							|| queryService.get(j).getNotJoinActivity().size() > 0) {
						TextView hasActsTxt = (TextView) findViewById(j);
						if (null != hasActsTxt) {
							hasActsTxt.setTextColor(mContext.getResources().getColor(R.color.text_txt_bg));
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		private void setUnJoinAndJoinedList(String dayOfAct, JSONArray jaJoined, JSONArray jaNotJoin)
				throws JSONException {
			for (int j = 0; j < 7; j++) {
				if (dayOfAct.equals(queryService.get(j).getDateOfDay())) {
					for (int j2 = 0; j2 < jaJoined.length(); j2++) {
						ActItem act = gson.fromJson(jaJoined.get(j2).toString(), ActItem.class);
						if (!jaJoined.getJSONObject(j2).isNull("applyDistance")) {
							act.setDistance(jaJoined.getJSONObject(j2).getString("applyDistance") == null ? ""
									: jaJoined.getJSONObject(j2).getString("applyDistance"));
						}
						act.setId(jaJoined.getJSONObject(j2).getLong("id"));
						act.setActEndTime(DateTool.L6SEndDay(act.getActEndTime()));
						act.setbJoin(true);
						queryService.get(j).getJoinActivity().add(act);
					}
					for (int j2 = 0; j2 < jaNotJoin.length(); j2++) {
						ActItem act = gson.fromJson(jaNotJoin.get(j2).toString(), ActItem.class);
						if (!jaNotJoin.getJSONObject(j2).isNull("applyDistance")) {
							act.setDistance(TextUtils.isEmpty(jaNotJoin.getJSONObject(j2).getString("applyDistance"))
									? "" : jaNotJoin.getJSONObject(j2).getString("applyDistance"));
						}
						act.setId(jaNotJoin.getJSONObject(j2).getLong("id"));
						act.setActStartTime(DateTool.L6SEndDay(act.getActStartTime()));
						act.setbJoin(false);
						queryService.get(j).getNotJoinActivity().add(act);
					}
					break;
				}
			}
		}

		private void clearServiceListData() {
			for (int j = 0; j < 7; j++) {
				queryService.get(j).getJoinActivity().clear();
				queryService.get(j).getNotJoinActivity().clear();
			}
		}
	};

	protected void listStopLoadView() {
		pullRefreshScrollView.onRefreshComplete();
	}

}
