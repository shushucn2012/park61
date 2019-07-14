package com.park61.moduel.sales;

import android.widget.ListView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推荐商品列表页面
 */
public class RecommendGoodsActivity extends BaseActivity {
    private String actTempId;
    private List<ProductLimit> goodsDataList;
    private List<GoodsCombine> goodsCombList;
    private PullToRefreshListView mPullRefreshListView;
    private GoodsListAdapter adapter;
    private ListView actualListView;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_recommend_goods);
    }

    @Override
    public void initView() {
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                asyncGetRecommendGoods();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });
        actualListView = mPullRefreshListView.getRefreshableView();
    }

    @Override
    public void initData() {
        actTempId = getIntent().getStringExtra("refTemplateId");
        goodsDataList = new ArrayList<ProductLimit>();
        goodsCombList = new ArrayList<GoodsCombine>();
        setGoodsToCombList();
        adapter = new GoodsListAdapter(mContext, goodsCombList, null);
        actualListView.setAdapter(adapter);
        asyncGetRecommendGoods();
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
     * 推荐商品列表
     */
    private void asyncGetRecommendGoods() {
        String wholeUrl = AppUrl.host + AppUrl.RECOMMEND_GOODS;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("refTemplateId", actTempId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, recommendlistener);
    }

    BaseRequestListener recommendlistener = new JsonRequestListener() {

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
            listStopLoadView();
            goodsDataList.clear();
            goodsCombList.clear();
            ArrayList<ProductLimit> currentPageList = new ArrayList<ProductLimit>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                ProductLimit pl = gson.fromJson(actJot.toString(), ProductLimit.class);
                currentPageList.add(pl);
            }
            goodsDataList.addAll(currentPageList);
            setGoodsToCombList();
            if (goodsDataList.size() < 1) {
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, actualListView);
            }
            adapter.notifyDataSetChanged();
        }
    };

    /**
     * 停止列表进度条
     */
    protected void listStopLoadView() {
        mPullRefreshListView.onRefreshComplete();
    }


}
