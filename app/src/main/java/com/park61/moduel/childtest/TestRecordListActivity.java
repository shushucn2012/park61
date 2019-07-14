package com.park61.moduel.childtest;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.childtest.adapter.TestRecordListAdapter;
import com.park61.moduel.childtest.bean.TestRecordBean;
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
 * Created by shubei on 2017/9/19.
 */

public class TestRecordListActivity extends BaseActivity {

    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;

    private PullToRefreshListView mPullRefreshListView;
    private TestRecordListAdapter mAdapter;
    private List<TestRecordBean> dataList;


    @Override
    public void setLayout() {
        setContentView(R.layout.activity_test_record_list);
    }

    @Override
    public void initView() {
        setPagTitle("测评记录");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
    }

    @Override
    public void initData() {
        dataList = new ArrayList<TestRecordBean>();
        mAdapter = new TestRecordListAdapter(mContext, dataList);
        mPullRefreshListView.setAdapter(mAdapter);
        asyncGetDataList();
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetDataList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetDataList();
            }
        });
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mContext, TestResultWebViewActivity.class);
                int rid = dataList.get(position - 1).getId();
                it.putExtra("PAGE_TITLE", "测评报告");
                String resultUrl = AppUrl.demoHost_head + "/html-sui/dapTest/test-result.html?resultid=" + rid;
                it.putExtra("url", resultUrl);
                String title = dataList.get(position - 1).getEaSysName() + dataList.get(position - 1).getEaItemName();
                String picUrl = AppUrl.SHARE_APP_ICON;
                String content = "测评报告";
                it.putExtra("picUrl", picUrl);
                it.putExtra("title", title);
                it.putExtra("content", content);
                startActivity(it);
            }
        });
        mPullRefreshListView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                dDialog.showDialog("提示", "确定删除吗？", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        asyncDeleteRecord(dataList.get(position - 1).getId());
                        dDialog.dismissDialog();
                    }
                });
                return true;
            }
        });
    }

    private void asyncDeleteRecord(int userResultId) {
        String wholeUrl = AppUrl.host + AppUrl.deleteRecord;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userResultId", userResultId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, dlistener);
    }

    BaseRequestListener dlistener = new JsonRequestListener() {

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
            showShortToast("删除成功！");
            PAGE_NUM = 1;
            asyncGetDataList();
        }
    };

    private void asyncGetDataList() {
        String wholeUrl = AppUrl.host + AppUrl.ea_getRecordList;
        String requestBodyData = ParamBuild.getMsgList(PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

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
            ArrayList<TestRecordBean> currentPageList = new ArrayList<TestRecordBean>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                dataList.clear();
                mAdapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, mPullRefreshListView.getRefreshableView());
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                dataList.clear();
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                TestRecordBean c = gson.fromJson(actJot.toString(), TestRecordBean.class);
                currentPageList.add(c);
            }
            dataList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };
}
