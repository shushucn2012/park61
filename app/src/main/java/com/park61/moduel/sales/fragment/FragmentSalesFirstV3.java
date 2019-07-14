package com.park61.moduel.sales.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.sales.adapter.MySalesFirstAdapter;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.moduel.sales.bean.MySalesTemplete;
import com.park61.moduel.sales.bean.ProductLimit;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.NetLoadingView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentSalesFirstV3 extends BaseFragment {

    private int PAGE_NUM = 1;
    public static final int PAGE_SIZE = 8;
    /**
     * 服务器端一共多少条数据
     */
    private int totalPage = 100;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断
    private MySalesFirstAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<GoodsCombine> myTopicViewList = new ArrayList<>();
    private List<GoodsCombine> myAllDataList = new ArrayList<>();

    private LRecyclerView mRecyclerView;
    private NetLoadingView view_loading;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_sales_special_v3, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        mRecyclerView = (LRecyclerView) parentView.findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        view_loading = (NetLoadingView) parentView.findViewById(R.id.view_loading);

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGE_NUM = 1;
                asyncGetTeMaiGoods();
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (PAGE_NUM < totalPage) {
                    PAGE_NUM++;
                    asyncGetTeMaiGoods();
                } else {
                    mRecyclerView.setNoMore(true);
                }
            }
        });
    }

    @Override
    public void initData() {
        asyncGetSalesTemplet();
    }

    @Override
    public void initListener() {
        view_loading.setOnRefreshClickedLsner(new NetLoadingView.OnRefreshClickedLsner() {
            @Override
            public void OnRefreshClicked() {
                asyncGetSalesTemplet();
            }
        });
    }

    public void asyncGetSalesTemplet() {
        String wholeUrl = AppUrl.host + AppUrl.GET_MALL_PAGE;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getSalesTempletLsner);
    }

    BaseRequestListener getSalesTempletLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            view_loading.showLoading(mRecyclerView);
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            view_loading.showRefresh();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
//            String res = FilesManager.getFromAssets(parentActivity, "sales_pager_temp_json_file");
//            JSONObject jsonResult123 = null;
//            try {
//                jsonResult123 = new JSONObject(res);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            JSONArray dataJay = jsonResult123.optJSONArray("data");
            JSONArray dataJay = jsonResult.optJSONArray("list");
            myTopicViewList.clear();
            for (int i = 0; i < dataJay.length(); i++) {
                JSONObject tempItemJot = dataJay.optJSONObject(i);
                String tempCode = tempItemJot.optString("templeteCode");
                String tempData = tempItemJot.optString("templeteData");
                GoodsCombine gc = new GoodsCombine();
                MySalesTemplete templeteData = new MySalesTemplete();
                templeteData.setTempleteCode(tempCode);
                templeteData.setTempleteData(tempData);
                templeteData.setTempleteHead(gson.fromJson(tempItemJot.optString("templeteHead"), MySalesTemplete.TempleteHead.class));
                gc.setTemplete(templeteData);
                if (!tempCode.equals("category_recommend")) {
                    myTopicViewList.add(gc);
                }
            }
            asyncGetTeMaiGoods();
        }
    };

    /**
     * 获取特卖商品
     */
    private void asyncGetTeMaiGoods() {
        String wholeUrl = AppUrl.host + AppUrl.GET_TEMAI_GOODS;
        String requestBodyData = ParamBuild.getTeMaiGoods(null, PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getTeMaiGoodsLsner);
    }

    BaseRequestListener getTeMaiGoodsLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            mRecyclerView.refreshComplete(PAGE_SIZE);
            view_loading.showRefresh();
            if (PAGE_NUM > 1) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            view_loading.hideLoading(mRecyclerView);
            mRecyclerView.refreshComplete(PAGE_SIZE);
            parseJosnToTemaiGoods(jsonResult);
        }
    };

    /**
     * 解析特卖首页商品
     */
    public void parseJosnToTemaiGoods(JSONObject jsonResult) {
        if (jsonResult == null)
            return;
        ArrayList<ProductLimit> currentPageList = new ArrayList<>();
        ArrayList<GoodsCombine> currentPageCombList = new ArrayList<>();
        JSONArray jay = jsonResult.optJSONArray("list");
        // 第一次查询的时候没有数据，则提示没有数据，页面置空
        if (PAGE_NUM == 1 && (jay == null || jay.length() <= 0)) {
            myAllDataList.clear();
            myAllDataList.addAll(myTopicViewList);
            adapter = new MySalesFirstAdapter(parentActivity, myAllDataList);
            mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
            mRecyclerView.setAdapter(mLRecyclerViewAdapter);
            return;
        }
        // 首次加载清空所有项列表,并重置控件为可下滑
        if (PAGE_NUM == 1) {
            myAllDataList.clear();
            myAllDataList.addAll(myTopicViewList);
        }
        totalPage = jsonResult.optInt("totalPage");
        for (int i = 0; i < jay.length(); i++) {
            JSONObject jot = jay.optJSONObject(i);
            ProductLimit p = gson.fromJson(jot.toString(), ProductLimit.class);
            currentPageList.add(p);
        }
        for (int i = 0; i < currentPageList.size(); i = i + 2) {
            GoodsCombine comb = new GoodsCombine();
            if (currentPageList.get(i) != null) {
                comb.setGoodsLeft(currentPageList.get(i));
            }
            if (i + 1 < currentPageList.size() && currentPageList.get(i + 1) != null) {
                comb.setGoodsRight(currentPageList.get(i + 1));
            }
            comb.setTemplete(new MySalesTemplete("goods", ""));
            currentPageCombList.add(comb);
        }
        myAllDataList.addAll(currentPageCombList);
        if (PAGE_NUM <= 1) {
            adapter = new MySalesFirstAdapter(parentActivity, myAllDataList);
            mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
            mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        } else {
            mLRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

}
