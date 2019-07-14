package com.park61.moduel.sales;

import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.sales.adapter.GoodsListAdapter;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.moduel.sales.bean.ProductLimit;
import com.park61.moduel.sales.bean.PromotionBannerData;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.viewpager.TeBuyBanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsListActivity extends BaseActivity {

    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;
    private Long cateId;
    private String cateName;
    private List<ProductLimit> goodsDataList;
    private List<GoodsCombine> goodsCombList;
    private List<PromotionBannerData> bannerlList;

    private TextView tv_empty_tip;
    private PullToRefreshListView mPullRefreshListView;
    private GoodsListAdapter adapter;
    private TextView tv_page_title;
    private ListView actualListView;
    private TeBuyBanner banner;
    private RelativeLayout banner_area;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_goodslist);
    }

    @Override
    public void initView() {
        banner_area = (RelativeLayout) findViewById(R.id.banner_area);
        tv_page_title = (TextView) findViewById(R.id.tv_page_title);
        tv_empty_tip = (TextView) findViewById(R.id.tv_empty_tip);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

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
        actualListView.setEmptyView(tv_empty_tip);
    }

    @Override
    public void initData() {
        cateId = getIntent().getLongExtra("cateId", -1l);
        cateName = getIntent().getStringExtra("cateName");
        tv_page_title.setText(cateName);

        goodsDataList = new ArrayList<ProductLimit>();
        goodsCombList = new ArrayList<GoodsCombine>();
        bannerlList = new ArrayList<PromotionBannerData>();
        setGoodsToCombList();

        adapter = new GoodsListAdapter(mContext, goodsCombList, bannerlList);
        actualListView.setAdapter(adapter);

        asyncBannerData();
        //initTopBanner();
    }

    /**
     * 初始化轮播图
     */
    private void initTopBanner() {
        if (banner != null) {
            banner.clear();
        }
        banner = new TeBuyBanner(this,
                (ViewPager) this.findViewById(R.id.top_viewpager),
                (LinearLayout) this.findViewById(R.id.top_vp_dot));
        asyncBannerData();
    }

    /**
     * 请求banner数据
     */
    private void asyncBannerData() {
        String wholeUrl = AppUrl.host + AppUrl.GET_PROMOTION_BANNER;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "002");
        map.put("type", "2");
        map.put("level", "2");
        map.put("category", cateId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, bannerLsner);
    }

    BaseRequestListener bannerLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            bannerlList.clear();
            JSONArray jay = jsonResult.optJSONArray("list");
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                PromotionBannerData bi = gson.fromJson(jot.toString(), PromotionBannerData.class);
                bannerlList.add(bi);
            }
            asyncGetGoodsList();
           /* banner.init(GoodsListActivity.this, bannerlList);
            if (bannerlList.size() < 1) {
                banner_area.setVisibility(View.GONE);
            } else {
                banner_area.setVisibility(View.VISIBLE);
            }*/
        }
    };

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
     * 请求特卖商品列表数据
     */
    private void asyncGetGoodsList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_TE_BUYLIST;
        String requestBodyData = ParamBuild.getGoodsList(cateId, PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, listener);
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
            if (PAGE_NUM == jsonResult.optInt("totalPage")) {
                setPullToRefreshViewEnd();
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                ProductLimit pl = gson.fromJson(actJot.toString(), ProductLimit.class);
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
        mPullRefreshListView.setMode(Mode.PULL_FROM_START);
    }

    /**
     * 将上下拉控件设为可上下拉
     */
    protected void setPullToRefreshViewBoth() {
        mPullRefreshListView.setMode(Mode.BOTH);
    }

}
