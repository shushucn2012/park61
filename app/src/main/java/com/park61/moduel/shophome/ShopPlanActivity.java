package com.park61.moduel.shophome;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.shophome.adapter.PlanCurseAdapter;
import com.park61.moduel.shophome.bean.TimetableInfo;
import com.park61.moduel.shophome.viewprotocol.LinkageScrollView;
import com.park61.moduel.shophome.viewprotocol.UpDownLeftRightScrollView;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by HP on 2017/3/6.
 */
public class ShopPlanActivity extends BaseActivity implements LinkageScrollView {
    private ListView mListView;
    //方便测试，直接写的public
    private HorizontalScrollView mTouchView;
    //装入所有的UpDownLeftRightScrollView
    protected List<UpDownLeftRightScrollView> mHScrollViews = new ArrayList<UpDownLeftRightScrollView>();
    private int mCurrentChooseWeek = -1;
    private LinearLayout weekOperationLlayout;
    private TextView weekOperationTxt;
    private TextView topBarMonthTxt;
    private FrameLayout topBarMondayFlayout;
    private TextView topBarMondayTxt;
    private TextView topBarMondayBgTxt;
    private FrameLayout topBarTuesdayFlayout;
    private TextView topBarTuesdayTxt;
    private TextView topBarTuesdayBgTxt;
    private FrameLayout topBarWednesdayFlayout;
    private TextView topBarWednesdayTxt;
    private TextView topBarWednesdayBgTxt;
    private FrameLayout topBarThursdayFlayout;
    private TextView topBarThursdayTxt;
    private TextView topBarThursdayBgTxt;
    private FrameLayout topBarFridayFlayout;
    private TextView topBarFridayTxt;
    private TextView topBarFridayBgTxt;
    private FrameLayout topBarSaturdayFlayout;
    private TextView topBarSaturdayTxt;
    private TextView topBarSaturdayBgTxt;
    private FrameLayout topBarSundayFlayout;
    private TextView topBarSundayTxt;
    private TextView topBarSundayBgTxt;
    private View[] mTopBarFlayout = new View[7];
    private PlanCurseAdapter adapter;
    private Long officeId;//店铺id
    /**
     * 当周标题头
     */
    private List<String> mScheduleHeaderList = new ArrayList<>();
    /**
     * 当周所有节数
     */
    private List<String[]> mScheduleSectionList = new ArrayList<String[]>();
    /**
     * 当周所有课程数据
     */
    private List<Map<String, TimetableInfo>> mAllPlanDataList = new ArrayList<Map<String, TimetableInfo>>();

    public void setLayout() {
        setContentView(R.layout.activity_xplan);
    }

    @Override
    public void initView() {

        initPlanTopbar();
        UpDownLeftRightScrollView headerScroll = (UpDownLeftRightScrollView) findViewById(R.id.item_scroll_title);
        //添加头滑动事件
        mHScrollViews.add(headerScroll);
        mListView = (ListView) findViewById(R.id.scroll_list);
        adapter = new PlanCurseAdapter(this, mAllPlanDataList, R.layout.xplan_item
                , new String[]{"title", "data_1", "data_2", "data_3", "data_4", "data_5", "data_6", "data_7",}
                , new int[]{R.id.txt_title
                , R.id.txt_monday
                , R.id.txt_tuesday
                , R.id.txt_wednesday
                , R.id.txt_thursday
                , R.id.txt_friday
                , R.id.txt_saturday
                , R.id.txt_sunday}
                , new int[]{
                R.id.flayout_monday
                , R.id.flayout_tuesday
                , R.id.flayout_wednesday
                , R.id.flayout_thursday
                , R.id.flayout_friday
                , R.id.flayout_saturday
                , R.id.flayout_sunday}
                , new int[]{
                R.id.img_monday
                , R.id.img_tuesday
                , R.id.img_wednesday
                , R.id.img_thursday
                , R.id.img_friday
                , R.id.img_saturday
                , R.id.img_sunday},
                this
        );
        mListView.setAdapter(adapter);

    }

    private void initPlanTopbar() {
        weekOperationLlayout = (LinearLayout) findViewById(R.id.llayout_week);
        weekOperationLlayout.setTag("0");
        weekOperationTxt = (TextView) findViewById(R.id.txt_week_operation);
        topBarMonthTxt = (TextView) findViewById(R.id.txt_topbar_month);
        topBarMondayFlayout = (FrameLayout) findViewById(R.id.flayout_topbar_monday);
        topBarMondayTxt = (TextView) findViewById(R.id.txt_topbar_monday);
        topBarMondayBgTxt = (TextView) findViewById(R.id.txt_topbar_monday_bg);
        View[] topBarMondayFlayoutChild = new View[2];
        topBarMondayFlayoutChild[0] = topBarMondayTxt;
        topBarMondayFlayoutChild[1] = topBarMondayBgTxt;
        topBarMondayFlayout.setTag(topBarMondayFlayoutChild);
        mTopBarFlayout[0] = topBarMondayFlayout;

        topBarTuesdayFlayout = (FrameLayout) findViewById(R.id.flayout_topbar_tuesday);
        topBarTuesdayTxt = (TextView) findViewById(R.id.txt_topbar_tuesday);
        topBarTuesdayBgTxt = (TextView) findViewById(R.id.txt_topbar_tuesday_bg);
        View[] topBarTuesdayFlayoutChild = new View[2];
        topBarTuesdayFlayoutChild[0] = topBarTuesdayTxt;
        topBarTuesdayFlayoutChild[1] = topBarTuesdayBgTxt;
        topBarTuesdayFlayout.setTag(topBarTuesdayFlayoutChild);
        mTopBarFlayout[1] = topBarTuesdayFlayout;

        topBarWednesdayFlayout = (FrameLayout) findViewById(R.id.flayout_topbar_wednesday);
        topBarWednesdayTxt = (TextView) findViewById(R.id.txt_topbar_wednesday);
        topBarWednesdayBgTxt = (TextView) findViewById(R.id.txt_topbar_wednesday_bg);
        View[] topBarWednesdayFlayoutChild = new View[2];
        topBarWednesdayFlayoutChild[0] = topBarWednesdayTxt;
        topBarWednesdayFlayoutChild[1] = topBarWednesdayBgTxt;
        topBarWednesdayFlayout.setTag(topBarWednesdayFlayoutChild);
        mTopBarFlayout[2] = topBarWednesdayFlayout;

        topBarThursdayFlayout = (FrameLayout) findViewById(R.id.flayout_topbar_thursday);
        topBarThursdayTxt = (TextView) findViewById(R.id.txt_topbar_thursday);
        topBarThursdayBgTxt = (TextView) findViewById(R.id.txt_topbar_thursday_bg);
        View[] topBarThursdayFlayoutChild = new View[2];
        topBarThursdayFlayoutChild[0] = topBarThursdayTxt;
        topBarThursdayFlayoutChild[1] = topBarThursdayBgTxt;
        topBarThursdayFlayout.setTag(topBarThursdayFlayoutChild);
        mTopBarFlayout[3] = topBarThursdayFlayout;

        topBarFridayFlayout = (FrameLayout) findViewById(R.id.flayout_topbar_friday);
        topBarFridayTxt = (TextView) findViewById(R.id.txt_topbar_friday);
        topBarFridayBgTxt = (TextView) findViewById(R.id.txt_topbar_friday_bg);
        View[] topBarFridayFlayoutChild = new View[2];
        topBarFridayFlayoutChild[0] = topBarFridayTxt;
        topBarFridayFlayoutChild[1] = topBarFridayBgTxt;
        topBarFridayFlayout.setTag(topBarFridayFlayoutChild);
        mTopBarFlayout[4] = topBarFridayFlayout;

        topBarSaturdayFlayout = (FrameLayout) findViewById(R.id.flayout_topbar_saturday);
        topBarSaturdayTxt = (TextView) findViewById(R.id.txt_topbar_saturday);
        topBarSaturdayBgTxt = (TextView) findViewById(R.id.txt_topbar_saturday_bg);
        View[] topBarSaturdayFlayoutChild = new View[2];
        topBarSaturdayFlayoutChild[0] = topBarSaturdayTxt;
        topBarSaturdayFlayoutChild[1] = topBarSaturdayBgTxt;
        topBarSaturdayFlayout.setTag(topBarSaturdayFlayoutChild);
        mTopBarFlayout[5] = topBarSaturdayFlayout;

        topBarSundayFlayout = (FrameLayout) findViewById(R.id.flayout_topbar_sunday);
        topBarSundayTxt = (TextView) findViewById(R.id.txt_topbar_sunday);
        topBarSundayBgTxt = (TextView) findViewById(R.id.txt_topbar_sunday_bg);
        View[] topBarSundayFlayoutChild = new View[2];
        topBarSundayFlayoutChild[0] = topBarSundayTxt;
        topBarSundayFlayoutChild[1] = topBarSundayBgTxt;
        topBarSundayFlayout.setTag(topBarSundayFlayoutChild);
        mTopBarFlayout[6] = topBarSundayFlayout;
    }

    private void setTopBarListener() {
        for (int i = 0; i < mTopBarFlayout.length; i++) {
            final FrameLayout parentFlayout = (FrameLayout) mTopBarFlayout[i];
            final View[] topBarFlayoutChild = (View[]) parentFlayout.getTag();
            final int week = i;
            parentFlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTopbarWeek(week, parentFlayout, topBarFlayoutChild);
                }
            });
        }

        weekOperationLlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weekOperationLlayout.getTag().equals("0")) {
                    weekOperationTxt.setText("上一周");
                    weekOperationLlayout.setTag("1");
                } else if (weekOperationLlayout.getTag().equals("1")) {
                    weekOperationTxt.setText("下一周");
                    weekOperationLlayout.setTag("0");
                }
                getPlanByWeek(weekOperationLlayout.getTag().toString(), officeId + "");

            }
        });

    }

    @Override
    public void changeTopbarWeek(int week, FrameLayout parentFlayout, View[] topBarFlayoutChild) {
        if (week != mCurrentChooseWeek) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    DevAttr.dip2px(ShopPlanActivity.this, 60), ViewGroup.LayoutParams.MATCH_PARENT
            );
            for (int j = 0; j < mTopBarFlayout.length; j++) {
                mTopBarFlayout[j].setLayoutParams(lp);
                TextView topBartxt = (TextView) ((View[]) mTopBarFlayout[j].getTag())[0];
                TextView topBarBgtxt = (TextView) ((View[]) mTopBarFlayout[j].getTag())[1];
                topBartxt.setTextColor(Color.parseColor("#777777"));
                topBarBgtxt.setVisibility(View.GONE);
            }

            LinearLayout.LayoutParams lpNew = new LinearLayout.LayoutParams(
                    DevAttr.dip2px(ShopPlanActivity.this, 90), ViewGroup.LayoutParams.MATCH_PARENT
            );
            parentFlayout.setLayoutParams(lpNew);
            TextView topBartxt = (TextView) topBarFlayoutChild[0];
            TextView topBarBgtxt = (TextView) topBarFlayoutChild[1];
            topBartxt.setTextColor(Color.parseColor("#000000"));
            topBarBgtxt.setVisibility(View.VISIBLE);

            mCurrentChooseWeek = week;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initData() {
        officeId = GlobalParam.CUR_SHOP_ID;
    }

    @Override
    protected void onResume() {
        super.onResume();
        officeId = GlobalParam.CUR_SHOP_ID;
        getPlanByWeek(weekOperationLlayout.getTag().toString(), officeId + "");
    }

    /**
     * 根据类型获取每周计划列表
     *
     * @param pType
     * @param pOfficeId
     */
    private void getPlanByWeek(String pType, String pOfficeId) {
        String wholeUrl = AppUrl.host + AppUrl.courseSchedule;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("weekOffset", pType);
        map.put("officeId", pOfficeId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, resultListener);
    }

    BaseRequestListener resultListener = new JsonRequestListener() {

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
            mAllPlanDataList.clear();
            mScheduleSectionList.clear();
            mScheduleHeaderList.clear();
            JSONArray scheduleSectionArr = jsonResult.optJSONArray("scheduleSection");
            for (int i = 0; i < scheduleSectionArr.length(); i++) {
                JSONObject jsonObject = scheduleSectionArr.optJSONObject(i);
                String[] sectionMap = new String[2];
                sectionMap[0] = jsonObject.optString("sectionName");
                sectionMap[1] = jsonObject.optString("sectionNum");
                mScheduleSectionList.add(sectionMap);
            }

            JSONArray scheduleHeaderArr = jsonResult.optJSONArray("scheduleHeader");
            for (int i = 0; i < scheduleHeaderArr.length(); i++) {
                try {
                    mScheduleHeaderList.add(DateTool.L2SEndDay(scheduleHeaderArr.getString(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONObject scheduleCurseArr = null;
            try {
                scheduleCurseArr = jsonResult.getJSONObject("scheduleData");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (scheduleCurseArr == null) {
                scheduleCurseArr = new JSONObject();
            }

            List<Map<String, TimetableInfo>> mAllPlanDataListCircle0 = new ArrayList<Map<String, TimetableInfo>>();
            List<Map<String, TimetableInfo>> mAllPlanDataListCircle1 = new ArrayList<Map<String, TimetableInfo>>();
            List<Map<String, TimetableInfo>> mAllPlanDataListCircle2 = new ArrayList<Map<String, TimetableInfo>>();

            for (int i = 0; i < mScheduleSectionList.size(); i++) {
                HashMap<String, TimetableInfo> data = new HashMap<String, TimetableInfo>();
                TimetableInfo leftTitle = new TimetableInfo();
                leftTitle.setStartTime(mScheduleSectionList.get(i)[0]);
                data.put("title", leftTitle);
                for (int j = 0; j < mScheduleHeaderList.size(); j++) {
                    String headerDate = mScheduleHeaderList.get(j);
                    String curseKey = headerDate + "_" + mScheduleSectionList.get(i)[0];
                    if (scheduleCurseArr.optJSONArray(curseKey) != null) {
                        JSONArray jayData = scheduleCurseArr.optJSONArray(curseKey);
                        if (jayData.length() > 0) {
                            TimetableInfo xplanInfo = gson.fromJson(jayData.optJSONObject(0).toString(), TimetableInfo.class);
                            xplanInfo.setWeekDay(j);
                            data.put("data_" + (j + 1), xplanInfo);
                        }
                    }
                }
                mAllPlanDataListCircle0.add(data);
            }

            for (int i = 0; i < mScheduleSectionList.size(); i++) {
                HashMap<String, TimetableInfo> data = new HashMap<String, TimetableInfo>();
                TimetableInfo leftTitle = new TimetableInfo();
                leftTitle.setStartTime(mScheduleSectionList.get(i)[0]);
                data.put("title", leftTitle);
                for (int j = 0; j < mScheduleHeaderList.size(); j++) {
                    String headerDate = mScheduleHeaderList.get(j);
                    String curseKey = headerDate + "_" + mScheduleSectionList.get(i)[0];
                    if (scheduleCurseArr.optJSONArray(curseKey) != null) {
                        JSONArray jayData = scheduleCurseArr.optJSONArray(curseKey);
                        if (jayData.length() > 1) {
                            TimetableInfo xplanInfo = gson.fromJson(jayData.optJSONObject(1).toString(), TimetableInfo.class);
                            xplanInfo.setWeekDay(j);
                            data.put("data_" + (j + 1), xplanInfo);
                        }
                    }
                }
                mAllPlanDataListCircle1.add(data);
            }

            for (int i = 0; i < mScheduleSectionList.size(); i++) {
                HashMap<String, TimetableInfo> data = new HashMap<String, TimetableInfo>();
                TimetableInfo leftTitle = new TimetableInfo();
                leftTitle.setStartTime(mScheduleSectionList.get(i)[0]);
                data.put("title", leftTitle);
                for (int j = 0; j < mScheduleHeaderList.size(); j++) {
                    String headerDate = mScheduleHeaderList.get(j);
                    String curseKey = headerDate + "_" + mScheduleSectionList.get(i)[0];
                    if (scheduleCurseArr.optJSONArray(curseKey) != null) {
                        JSONArray jayData = scheduleCurseArr.optJSONArray(curseKey);
                        if (jayData.length() > 2) {
                            TimetableInfo xplanInfo = gson.fromJson(jayData.optJSONObject(2).toString(), TimetableInfo.class);
                            xplanInfo.setWeekDay(j);
                            data.put("data_" + (j + 1), xplanInfo);
                        }
                    }
                }
                mAllPlanDataListCircle2.add(data);
            }

            Iterator<Map<String, TimetableInfo>> iterator0 = mAllPlanDataListCircle0.iterator();
            while (iterator0.hasNext()) {
                Map<String, TimetableInfo> mapData = iterator0.next();
                boolean hasData = false;
                for (int j = 1; j < 8; j++) {
                    if (mapData.get("data_" + j) != null) {
                        hasData = true;
                    }
                }
                if (!hasData) {
                    iterator0.remove();
                }
            }

            Iterator<Map<String, TimetableInfo>> iterator1 = mAllPlanDataListCircle1.iterator();
            while (iterator1.hasNext()) {
                Map<String, TimetableInfo> mapData = iterator1.next();
                boolean hasData = false;
                for (int j = 1; j < 8; j++) {
                    if (mapData.get("data_" + j) != null) {
                        hasData = true;
                    }
                }
                if (!hasData) {
                    iterator1.remove();
                }
            }

            Iterator<Map<String, TimetableInfo>> iterator2 = mAllPlanDataListCircle2.iterator();
            while (iterator2.hasNext()) {
                Map<String, TimetableInfo> mapData = iterator2.next();
                boolean hasData = false;
                for (int j = 1; j < 8; j++) {
                    if (mapData.get("data_" + j) != null) {
                        hasData = true;
                    }
                }
                if (!hasData) {
                    iterator2.remove();
                }
            }

            mAllPlanDataList.addAll(mAllPlanDataListCircle0);
            mAllPlanDataList.addAll(mAllPlanDataListCircle1);
            mAllPlanDataList.addAll(mAllPlanDataListCircle2);

            Collections.sort(mAllPlanDataList, new Comparator<Map<String, TimetableInfo>>() {
                @Override
                public int compare(Map<String, TimetableInfo> o1, Map<String, TimetableInfo> o2) {
                    return o1.get("title").getStartTime().compareTo(o2.get("title").getStartTime());
                }
            });

            adapter.notifyDataSetChanged();

            for (int i = 0; i < mTopBarFlayout.length; i++) {
                FrameLayout parentFlayout = (FrameLayout) mTopBarFlayout[i];
                View[] topBarFlayoutChild = (View[]) parentFlayout.getTag();

                String[] weekDaysName = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
                ((TextView) topBarFlayoutChild[0]).setText(weekDaysName[i] +
                        "\n" + mScheduleHeaderList.get(i).substring(5, 10));
            }
            topBarMonthTxt.setText(mScheduleHeaderList.get(0).substring(5, 7) + "月");
        }
    };

    @Override
    public void initListener() {
        setTopBarListener();
    }

    @Override
    public void addHViews(final UpDownLeftRightScrollView hScrollView) {
        if (!mHScrollViews.isEmpty()) {
            int size = mHScrollViews.size();
            UpDownLeftRightScrollView scrollView = mHScrollViews.get(size - 1);
            final int scrollX = scrollView.getScrollX();
            //第一次满屏后，向下滑动，有一条数据在开始时未加入
            if (scrollX != 0) {
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        //当listView刷新完成之后，把该条移动到最终位置
                        hScrollView.scrollTo(scrollX, 0);
                    }
                });
            }
        }
        mHScrollViews.add(hScrollView);
    }

    @Override
    public View[] getTopBarFlayout() {
        return mTopBarFlayout;
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        for (UpDownLeftRightScrollView scrollView : mHScrollViews) {
            //防止重复滑动
            if (mTouchView != scrollView)
                scrollView.smoothScrollTo(l, t);
        }
    }

    @Override
    public int getSelectWeekIndex() {
        return mCurrentChooseWeek;
    }

    @Override
    public void setSelectWeekIndex(int pSelectWeenPosition) {
        mCurrentChooseWeek = pSelectWeenPosition;
    }

    @Override
    public HorizontalScrollView getMainScrollView() {
        return mTouchView;
    }

    @Override
    public void setMainScrollView(HorizontalScrollView pHorizontalScrollView) {
        mTouchView = pHorizontalScrollView;
    }


}
