package com.park61.moduel.me;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.me.adapter.MyBabyCourseListAdapter;
import com.park61.moduel.me.bean.MyBabyCourseItem;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/7/23.
 */

public class MyBabyCourseActivity extends BaseActivity {

    private PullToRefreshListView mPullRefreshListView;
    private MyBabyCourseListAdapter adapter;
    private List<MyBabyCourseItem> dataList;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_mybaby_course);
    }

    @Override
    public void initView() {
        setPagTitle("宝宝课程");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        ViewInitTool.setListEmptyByDefaultTipPic(mContext, mPullRefreshListView.getRefreshableView());
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                asyncGetChildCourseList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });
    }

    @Override
    public void initData() {
        dataList = new ArrayList<>();
        adapter = new MyBabyCourseListAdapter(mContext, dataList);
        mPullRefreshListView.setAdapter(adapter);
        asyncGetChildCourseList();
    }

    @Override
    public void initListener() {

    }

    private void asyncGetChildCourseList() {
        String wholeUrl = AppUrl.host + AppUrl.childCourseList;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getClosestShopLsner);
    }

    BaseRequestListener getClosestShopLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            JSONArray dateJay = jsonResult.optJSONArray("list");
            dataList.clear();
            if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                for (int j = 0; j < dateJay.length(); j++) {
                    JSONObject dateJot = dateJay.optJSONObject(j);
                    MyBabyCourseItem item = gson.fromJson(dateJot.toString(), MyBabyCourseItem.class);
                    dataList.add(item);
                }
            }
            adapter.notifyDataSetChanged();
        }
    };

}
