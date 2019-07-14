package com.park61.moduel.acts;

import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.adapter.CourseComtListAdapter;
import com.park61.moduel.acts.bean.CourseComtItem;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.MyRatingBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/7/24.
 */

public class CourseEvaListActivity extends BaseActivity {

    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;

    private PullToRefreshListView mPullRefreshListView;
    private CourseComtListAdapter adapter;
    private TextView tv_course_avg_point, tv_comt_num;
    private MyRatingBar ratingbar_course_point;

    private List<CourseComtItem> cList;
    private int courseId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_course_evalist);
    }

    @Override
    public void initView() {
        setPagTitle("课程评价");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetEvaluateList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetEvaluateList();
            }
        });
        tv_course_avg_point = (TextView) findViewById(R.id.tv_course_avg_point);
        tv_comt_num = (TextView) findViewById(R.id.tv_comt_num);
        ratingbar_course_point = (MyRatingBar) findViewById(R.id.ratingbar_course_point);
    }

    @Override
    public void initData() {
        cList = new ArrayList<CourseComtItem>();
        adapter = new CourseComtListAdapter(mContext, cList);
        mPullRefreshListView.setAdapter(adapter);
        courseId = getIntent().getIntExtra("courseId", -1);
        asyncGetEvaluateList();
    }

    @Override
    public void initListener() {

    }

    private void asyncGetEvaluateList() {
        String wholeUrl = AppUrl.host + AppUrl.evaluateList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseId);
        map.put("curPage", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, eLsner);
    }

    BaseRequestListener eLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            if (PAGE_NUM == 1) {
                showDialog();
            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            showShortToast(errorMsg);
            if (PAGE_NUM > 1) {//翻页出错回滚
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            ArrayList<CourseComtItem> currentPageList = new ArrayList<CourseComtItem>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                cList.clear();
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, mPullRefreshListView.getRefreshableView());
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                cList.clear();
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM == jsonResult.optInt("totalPage")) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            }
            for (int i = 0; i < actJay.length(); i++) {
                currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), CourseComtItem.class));
            }
            cList.addAll(currentPageList);
            adapter.notifyDataSetChanged();

            tv_course_avg_point.setText(currentPageList.get(0).getAvgScore() + "");
            tv_comt_num.setText(jsonResult.optInt("totalSize") + "评价");
            ratingbar_course_point.setPoint((int) (currentPageList.get(0).getAvgScore() * 2));
        }
    };
}
