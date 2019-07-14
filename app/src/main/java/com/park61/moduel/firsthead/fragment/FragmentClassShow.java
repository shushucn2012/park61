package com.park61.moduel.firsthead.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.firsthead.adapter.FirstClassListAdapter;
import com.park61.moduel.firsthead.bean.TeachClassNotice;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/8/30.
 */

public class FragmentClassShow extends BaseFragment {

    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private FirstClassListAdapter mAdapter;
    private List<TeachClassNotice> mList;
    private int PAGE_NUM = 0;
    private final int PAGE_SIZE = 6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.activity_first_class, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        mPullRefreshListView = (PullToRefreshListView) parentView.findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, parentActivity);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                asyncGetList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetList();
            }
        });
        actualListView = mPullRefreshListView.getRefreshableView();
        mList = new ArrayList<>();
        mAdapter = new FirstClassListAdapter(parentActivity, mList);
        actualListView.setAdapter(mAdapter);
        parentView.findViewById(R.id.banner).setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*for (int i = 0; i < PAGE_SIZE; i++) {
            mList.remove(mList.size() - 1 - i);
        }*/

//        mList = mList.subList(0, mList.size() - PAGE_SIZE);
        //PAGE_NUM = PAGE_SIZE;
        PAGE_NUM = 0;
        asyncGetList();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    private void asyncGetList() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.classNotice_photoList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listListener);
    }

    BaseRequestListener listListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            if (PAGE_NUM == 0 && CommonMethod.isListEmpty(mList)) {
                showDialog();
            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            if (PAGE_NUM > 0) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            List<TeachClassNotice> currentPageList = new ArrayList<>();
            JSONArray jay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (jay == null || jay.length() <= 0)) {
                ViewInitTool.setListEmptyView(parentActivity, actualListView, "暂无数据", R.drawable.quexing, null, 100, 95);
                mList.clear();
                mAdapter.notifyDataSetChanged();
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                return;
            }
            if (PAGE_NUM == 0) {
                mList.clear();
            }
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            } else {
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                TeachClassNotice itemInfo = gson.fromJson(jot.toString(), TeachClassNotice.class);
                currentPageList.add(itemInfo);
            }
            mList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
            /*if(!CommonMethod.isListEmpty(mList)){
                ((KgShowActivity)parentActivity).setPagTitle(mList.get(0).getClassName());
            }*/
        }
    };
}
