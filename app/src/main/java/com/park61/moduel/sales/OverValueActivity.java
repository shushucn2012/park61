package com.park61.moduel.sales;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.sales.adapter.GoodsListAdapter;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.moduel.sales.bean.ProductLimit;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 超值限量和新品特惠页面
 */
public class OverValueActivity extends BaseActivity {
    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 6;
    private List<ProductLimit> goodsDataList;
    private List<GoodsCombine> goodsCombList;
    private PullToRefreshListView mPullRefreshListView;
    private GoodsListAdapter adapter;
    private TextView tv_page_title;
    private ListView actualListView;
    private String promotionType;
    private ImageView empty_img;
    private View block;
    private TextView empty_tv;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_overvalue);
    }

    @Override
    public void initView() {
        empty_img = (ImageView) findViewById(R.id.empty_img);
        empty_tv = (TextView) findViewById(R.id.empty_tv);
        block = findViewById(R.id.block);
        block.setVisibility(View.GONE);
        tv_page_title = (TextView) findViewById(R.id.tv_page_title);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

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
        actualListView = mPullRefreshListView.getRefreshableView();
    }

    @Override
    public void initData() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_page_title);
        String titleStr = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(titleStr)) {
            tvTitle.setText("");
        } else {
            tvTitle.setText(titleStr);
        }
        promotionType = getIntent().getStringExtra("promotionType");

        goodsDataList = new ArrayList<ProductLimit>();
        goodsCombList = new ArrayList<GoodsCombine>();
        setGoodsToCombList();

        adapter = new GoodsListAdapter(mContext, goodsCombList);
        actualListView.setAdapter(adapter);
        asyncGetGoodsList();
    }

    /**
     * 把商品列表的数据填充到商品联合bean列表
     */
    public void setGoodsToCombList() {
        goodsCombList.clear();
        for (int i = 0; i < goodsDataList.size(); i = i + 2) {
            GoodsCombine comb = new GoodsCombine();
            if (goodsDataList.get(i) != null)
                comb.setGoodsLeft(goodsDataList.get(i));
            if (i + 1 < goodsDataList.size()
                    && goodsDataList.get(i + 1) != null)
                comb.setGoodsRight(goodsDataList.get(i + 1));
            goodsCombList.add(comb);
        }
    }

    @Override
    public void initListener() {

    }

    /**
     * 获取商品列表
     */
    private void asyncGetGoodsList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_TEMAI_GOODS;
        String requestBodyData = ParamBuild.getTeMaiGoods(promotionType, PAGE_NUM, PAGE_SIZE);
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
                goodsDataList.clear();
                goodsCombList.clear();
                adapter.notifyDataSetChanged();
                actualListView.setEmptyView(block);
                setPullToRefreshViewEnd();
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                goodsDataList.clear();
                goodsCombList.clear();
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
            goodsDataList.addAll(currentPageList);
            setGoodsToCombList();
            adapter.notifyDataSetChanged();
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
