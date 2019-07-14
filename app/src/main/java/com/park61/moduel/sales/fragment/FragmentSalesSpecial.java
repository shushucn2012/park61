package com.park61.moduel.sales.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.ListViewFootState;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.sales.adapter.SalesSpecialAdapter;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.moduel.sales.bean.ProductCategory;
import com.park61.moduel.sales.bean.ProductLimit;
import com.park61.moduel.sales.bean.PromotionBannerData;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.NetLoadingView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentSalesSpecial extends BaseFragment {

    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;
    private String cateId;

    private List<PromotionBannerData> bannerlList;
    private List<ProductCategory> topCateList;
    private List<GoodsCombine> gcombList;
    private SalesSpecialAdapter adapter;

    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private NetLoadingView view_loading;
    private ListViewFootState lvFootState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_sales_special_new, container, false);
        cateId = getArguments().getString("cateId");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        mPullRefreshListView = (PullToRefreshListView) parentView.findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, parentActivity);
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetGoodsClassify();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetGoodsClassify();
            }
        });
        actualListView = mPullRefreshListView.getRefreshableView();
        view_loading = (NetLoadingView) parentView.findViewById(R.id.view_loading);
        lvFootState = new ListViewFootState(actualListView, parentActivity);
    }

    @Override
    public void initData() {
        bannerlList = new ArrayList<PromotionBannerData>();
        topCateList = new ArrayList<ProductCategory>();
        gcombList = new ArrayList<GoodsCombine>();

        PAGE_NUM = 1;
        asyncBannerData();
    }

    @Override
    public void initListener() {
        view_loading.setOnRefreshClickedLsner(new NetLoadingView.OnRefreshClickedLsner() {
            @Override
            public void OnRefreshClicked() {
                asyncBannerData();
            }
        });
        mPullRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if(lvFootState.getCurrState() == ListViewFootState.IDLE) {
                    lvFootState.addListFootLoading();
                    PAGE_NUM++;
                    asyncGetGoodsClassify();
                }
            }
        });
//        mPullRefreshListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
    }

    /**
     * 请求banner数据
     */
    private void asyncBannerData() {
        String wholeUrl = AppUrl.host + AppUrl.GET_PROMOTION_BANNER;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "002");
        map.put("type", "2");
        map.put("level", "1");
        map.put("category", cateId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, bannerLsner);
    }

    BaseRequestListener bannerLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            view_loading.showLoading(mPullRefreshListView);
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            view_loading.showRefresh();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray jay = jsonResult.optJSONArray("list");
            bannerlList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                PromotionBannerData bi = gson.fromJson(jot.toString(), PromotionBannerData.class);
                bannerlList.add(bi);
            }
            adapter = new SalesSpecialAdapter(parentActivity, topCateList, gcombList, bannerlList);
            actualListView.setAdapter(adapter);
            asyncGetGoodsClassify();
        }
    };

    /**
     * 获取种类和列表
     */
    private void asyncGetGoodsClassify() {
        String wholeUrl = AppUrl.host + AppUrl.GET_GOODS_CLASSIFY;
        String requestBodyData = ParamBuild.goodsClassify(cateId, PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getGoodsClassifyLsner);
    }

    BaseRequestListener getGoodsClassifyLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            listStopLoadView();
            lvFootState.addListFootErr();
            if(PAGE_NUM > 1) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            listStopLoadView();
            view_loading.hideLoading(mPullRefreshListView);
            parseJosnToSetData(jsonResult);
        }
    };

    /**
     * 解析种类和商品
     */
    public void parseJosnToSetData(JSONObject jsonResult) {
        if (jsonResult == null)
            return;
        // **************解析头部种类数据************** START//
        JSONArray jay = jsonResult.optJSONArray("category");
        topCateList.clear();
        for (int i = 0; i < jay.length(); i++) {
            JSONObject jot = jay.optJSONObject(i);
            ProductCategory p = gson.fromJson(jot.toString(), ProductCategory.class);
            topCateList.add(p);
        }
        // **************解析头部种类数据************** END//

        // **************解析底部商品数据************** START//
        JSONArray gJay = jsonResult.optJSONObject("page").optJSONArray("list");
        ArrayList<ProductLimit> currentPageList = new ArrayList<ProductLimit>();
        // 第一次查询的时候没有数据，则提示没有数据，页面置空
        if (PAGE_NUM == 1 && (gJay == null || gJay.length() <= 0)) {
            gcombList.clear();
            setListTop();
            adapter.notifyDataSetChanged();
            lvFootState.addListFootEnd();
            return;
        }
        // 首次加载清空所有项列表,并重置控件为可下滑
        if (PAGE_NUM == 1) {
            gcombList.clear();
            adapter.setIsNeedRefreshBanner(true);//刷新时第一页需要重置banner
            setListTop();
        }
        // 如果当前页已经是最后一页，则列表控件置为不可下滑
        if (PAGE_NUM >= jsonResult.optJSONObject("page").optInt("totalPage")) {
            lvFootState.addListFootEnd();
        } else {
            lvFootState.addListFootIdle();
        }
        for (int i = 0; i < gJay.length(); i++) {
            JSONObject jot = gJay.optJSONObject(i);
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
            gcombList.add(comb);
        }
        adapter.notifyDataSetChanged();
        // **************解析底部商品数据************** END//
    }

    /**
     * 停止列表进度条
     */
    protected void listStopLoadView() {
        mPullRefreshListView.onRefreshComplete();
    }

    /**
     * 由于listview的0，1号位置插入了banner和分类行，所以list数据头部插入两条空数据以防数据错误
     */
    private void setListTop(){
        if(CommonMethod.isListEmpty(bannerlList)){//没有banner就多1位，否则多2位
            gcombList.add(new GoodsCombine());
        }else{
            gcombList.add(new GoodsCombine());
            gcombList.add(new GoodsCombine());
        }
    }

}
