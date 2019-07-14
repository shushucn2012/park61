package com.park61.moduel.childtest;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.childtest.adapter.RecordListAdapter;
import com.park61.moduel.childtest.bean.TestRecodListBean;
import com.park61.moduel.childtest.bean.TestRecordItemBean;
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
 * Created by nieyu on 2017/12/4.
 */

public class TestHistoryActivity extends BaseActivity implements View.OnClickListener {
    private List<TestRecordItemBean> mList;
    private RecordListAdapter mAdapter;
    private int PAGE_NUM = 0;
    private static final int PAGE_SIZE = 10;
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private ImageView iv_back;
    @Override
    public void setLayout() {
setContentView(R.layout.activity_historytest);
    }

    @Override
    public void initView() {
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        mPullRefreshListView=(PullToRefreshListView)findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        actualListView = mPullRefreshListView.getRefreshableView();
    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        mAdapter = new RecordListAdapter(this,mList);
        actualListView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ansycGetComment();
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                PAGE_NUM=0;
//                ansycGetComment();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                ansycGetComment();
            }
        });
    }


    public void ansycGetComment(){
        String wholeUrl =AppUrl.NEWHOST_HEAD+ AppUrl.TEST_RECORD;
//        String wholeUrl ="http://192.168.100.13:8080/mockjsdata/12/service/ea/getRecordList";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, commlister);
    }

    BaseRequestListener commlister = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            mPullRefreshListView.onRefreshComplete();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullRefreshListView.onRefreshComplete();
            TestRecodListBean testRecodListBean  = gson.fromJson(jsonResult.toString(), TestRecodListBean.class);
            ArrayList<TestRecordItemBean> currentPageList = new ArrayList<TestRecordItemBean>();
            JSONArray cmtJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0&& (cmtJay == null || cmtJay.length() <= 0)) {
                mList.clear();
                mAdapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, mPullRefreshListView.getRefreshableView());
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                return;
            }
//            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 0) {
                mList.clear();
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
//
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM>= jsonResult.optInt("pageCount")-1) {
                mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
//                mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
//
            if(cmtJay!=null){
                for (int i = 0; i < cmtJay.length(); i++) {
                    JSONObject actJot = cmtJay.optJSONObject(i);
                    TestRecordItemBean p = gson.fromJson(actJot.toString(), TestRecordItemBean.class);
                    currentPageList.add(p);
                }
            }
            mList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
            actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent=new Intent(TestHistoryActivity.this,TestResultWebViewNewActivity.class);
                    intent.putExtra("batchCode",mList.get(i-1).getBatchCode());
                    intent.putExtra("childId",mList.get(i-1).getChildId());
                    startActivity(intent);
                }
            });
        }
    };

    @Override
    public void onClick(View view) {
        int viewid=view.getId();
        if(R.id.iv_back==viewid){
            finish();
        }
    }
}
