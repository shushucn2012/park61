package com.park61.moduel.sales;


import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.sales.adapter.MaMaGroupGoodsListAdapter;
import com.park61.moduel.sales.bean.ProductLimit;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 妈妈团界面
 */
public class MaMaGroupActivity extends BaseActivity {
    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;

    private PullToRefreshListView mPullToRefreshListView;
    private ListView actualListView;

    private MaMaGroupGoodsListAdapter mAdapter;
    private List<ProductLimit> dataList;
    private String promotionType;
    private TextView tv_page_title;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_mamagroup);
    }

    @Override
    public void initView() {
        tv_page_title = (TextView) findViewById(R.id.tv_page_title);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ViewInitTool.initPullToRefresh(mPullToRefreshListView, this);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetGoodsList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetGoodsList();
            }
        });
        actualListView = mPullToRefreshListView.getRefreshableView();
    }

    @Override
    public void initData() {
        dataList = new ArrayList<ProductLimit>();
        mAdapter = new MaMaGroupGoodsListAdapter(mContext, dataList);
        actualListView.setAdapter(mAdapter);

        promotionType = getIntent().getStringExtra("promotionType");
        asyncGetGoodsList();
    }

    @Override
    public void initListener() {

    }

    /**
     * 获取妈妈团商品列表
     */
    private void asyncGetGoodsList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_TEMAI_GOODS;
        String requestBodyData = ParamBuild.getTeMaiGoods(promotionType,
                PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,
                listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            listStopLoadView();
            showShortToast(errorMsg);
            if (PAGE_NUM > 1) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            listStopLoadView();
            ArrayList<ProductLimit> currentPageList = new ArrayList<ProductLimit>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                dataList.clear();
                mAdapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyView(mContext, actualListView, "暂无数据",
                        R.drawable.quexing, null, 100, 90);
                setPullToRefreshViewEnd();
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                dataList.clear();
                setPullToRefreshViewBoth();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
                setPullToRefreshViewEnd();
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                ProductLimit pl = gson.fromJson(actJot.toString(),
                        ProductLimit.class);
                currentPageList.add(pl);
            }
            dataList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 停止列表进度条
     */
    protected void listStopLoadView() {
        mPullToRefreshListView.onRefreshComplete();
    }

    /**
     * 将上下拉控件设为到底
     */
    protected void setPullToRefreshViewEnd() {
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    /**
     * 将上下拉控件设为可上下拉
     */
    protected void setPullToRefreshViewBoth() {
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

}
