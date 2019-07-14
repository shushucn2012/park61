package com.park61.moduel.toyshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.MipcaActivityCapture;
import com.park61.moduel.toyshare.adapter.JoyApplyListAdapter;
import com.park61.moduel.toyshare.bean.JoyApplyItem;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/6/19.
 */

public class TSMyApplyToyListActivity extends BaseActivity {

    public static boolean NEED_FRESH = false;//是否刷新页面
    private static final int SCANNIN_GREQUEST_CODE = 1;// 扫描二维码请求

    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;
    private boolean isFirstIn = true;

    private PullToRefreshListView mPullRefreshListView;

    private JoyApplyListAdapter mAdapter;
    private List<JoyApplyItem> mDataList;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_ts_myapply_toylist);
    }

    @Override
    public void initView() {
        setPagTitle("我的申领");

        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PAGE_NUM = 1;
        asyncGetDatas();
    }

    @Override
    public void initData() {
        mDataList = new ArrayList<JoyApplyItem>();
        mAdapter = new JoyApplyListAdapter(mContext, mDataList);
        mPullRefreshListView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetDatas();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetDatas();
            }
        });
       /* mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mContext, TSApplyDetailsActivity.class);
                it.putExtra("TOY_APPLY", mDataList.get(position - 1));
                startActivity(it);
            }
        });*/
        mAdapter.setLsner(new JoyApplyListAdapter.OnScanLsner() {
            @Override
            public void onScan() {
                Intent intent = new Intent();
                intent.setClass(mContext, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });
    }

    private void asyncGetDatas() {
        String wholeUrl = AppUrl.host + AppUrl.GETUSERTOYAPPLYLOGLIST;
        String requestBodyData = ParamBuild.getActList(PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            if (isFirstIn) {
                showDialog();
                isFirstIn = false;
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
            ArrayList<JoyApplyItem> currentPageList = new ArrayList<JoyApplyItem>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                mDataList.clear();
                mAdapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, mPullRefreshListView.getRefreshableView());
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                mDataList.clear();
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM == jsonResult.optInt("totalPage")) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            }
            for (int i = 0; i < actJay.length(); i++) {
                currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), JoyApplyItem.class));
            }
            mDataList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCANNIN_GREQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String str = bundle.getString("result");
            CommonMethod.dealWithScanBack(str, mContext);
        }
    }
}
