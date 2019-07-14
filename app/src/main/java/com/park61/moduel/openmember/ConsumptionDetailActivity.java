package com.park61.moduel.openmember;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.openmember.adapter.ConsumptionDetailListAdapter;
import com.park61.moduel.openmember.bean.ConsumptionDetail;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 消费明细界面
 */
public class ConsumptionDetailActivity extends BaseActivity {
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private ConsumptionDetailListAdapter mAdapter;
    private ArrayList<ConsumptionDetail> mList;
    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_consumption_detail);
    }

    @Override
    public void initView() {
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        actualListView = mPullRefreshListView.getRefreshableView();
        mList = new ArrayList<ConsumptionDetail>();
        mAdapter = new ConsumptionDetailListAdapter(mContext,mList);
        actualListView.setAdapter(mAdapter);
        ViewInitTool.setListEmptyView(mContext, actualListView,
                "好冷清呀，您还没有使用记录哦", R.drawable.quexing,
                null,100,95);
    }
    @Override
    protected void onResume() {
        super.onResume();
        PAGE_NUM = 1;
        asyncGetConsumptionDetail();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetConsumptionDetail();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetConsumptionDetail();
            }
        });
    }
    /**
     * 消费明细
     */
    private void asyncGetConsumptionDetail() {
        String wholeUrl = AppUrl.host + AppUrl.GET_DETAIL_RECORD;
        String requestBodyData = ParamBuild.getDetail(PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getCardTypeLsner);
    }
    BaseRequestListener getCardTypeLsner = new JsonRequestListener() {
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
            ArrayList<ConsumptionDetail> currentPageList = new ArrayList<ConsumptionDetail>();
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (jay == null || jay.length() <= 0)) {
                mList.clear();
                mAdapter.notifyDataSetChanged();
                setPullToRefreshViewEnd();
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                mList.clear();
                setPullToRefreshViewBoth();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
                setPullToRefreshViewEnd();
            }
            for (int i = 0; i < jay.length(); i++) {
                JSONObject actJot = jay.optJSONObject(i);
                ConsumptionDetail c = gson.fromJson(actJot.toString(), ConsumptionDetail.class);
                currentPageList.add(c);
            }
            mList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };
    /**
     * 停止列表进度条
     */
    protected void listStopLoadView() {
        mPullRefreshListView.onRefreshComplete();
    }

    /**
     * 将上下拉控件设为到底
     */
    protected void setPullToRefreshViewEnd() {
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    /**
     * 将上下拉控件设为可上下拉
     */
    protected void setPullToRefreshViewBoth() {
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
    }
}
