package com.park61.moduel.sales.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ListViewFootState;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.grouppurchase.GroupPurchaseActivity;
import com.park61.moduel.sales.MaMaGroupActivity;
import com.park61.moduel.sales.OverValueActivity;
import com.park61.moduel.sales.OverseasGoodsActivity;
import com.park61.moduel.sales.SecKillActivityNew;
import com.park61.moduel.sales.WebViewActivity;
import com.park61.moduel.sales.adapter.GoodsListAdapter;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.moduel.sales.bean.ProductLimit;
import com.park61.moduel.sales.bean.PromotionBannerData;
import com.park61.moduel.sales.bean.PromotionType;
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

public class FragmentSalesFirst extends BaseFragment implements OnClickListener {

    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;
    private String cateId = "-1";

    private PullToRefreshScrollView mPullRefreshScrollView;
    private List<PromotionType> plist;
    private List<GoodsCombine> gcombList;
    private GoodsListAdapter mGoodsListAdapter;

    private ListView lv_bottom_goods, lv_top_cate;
    private View temai_mid_area, secondkill_area;
    private TextView tv_sk_title, tv_area1_title, tv_area2_title,
            tv_area3_title, tv_area4_title;
    private TextView tv_sk_des, tv_area1_des, tv_area2_des, tv_area3_des,
            tv_area4_des;
    private ImageView img_sk, img_area1, img_area2, img_area3, img_area4, img_promotion;
    private View area1, area2, area3, area4, banner_area;
    private TeBuyBanner banner;
    private ObservableScrollView scrollView;
    private ListViewFootState lvFootState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_sales_special,
                container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        mPullRefreshScrollView = (PullToRefreshScrollView) parentView
                .findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);
        ViewInitTool.initPullToRefresh(mPullRefreshScrollView, parentActivity);
        mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                PAGE_NUM = 1;
                asyncGetTeMaiType();
                asyncGetTeMaiGoods(null);
                asyncBannerData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }
        });
        scrollView = (ObservableScrollView) mPullRefreshScrollView.getRefreshableView();
        temai_mid_area = parentView.findViewById(R.id.temai_mid_area);
        lv_top_cate = (ListView) parentView.findViewById(R.id.lv_top_cate);
        lv_bottom_goods = (ListView) parentView.findViewById(R.id.lv_bottom_goods);
        lvFootState = new ListViewFootState(lv_bottom_goods, parentActivity);
        img_promotion = (ImageView) parentView.findViewById(R.id.img_promotion);

        secondkill_area = parentView.findViewById(R.id.secondkill_area);

        tv_sk_title = (TextView) parentView.findViewById(R.id.tv_sk_title);
        tv_area1_title = (TextView) parentView.findViewById(R.id.tv_area1_title);
        tv_area2_title = (TextView) parentView.findViewById(R.id.tv_area2_title);
        tv_area3_title = (TextView) parentView.findViewById(R.id.tv_area3_title);
        tv_area4_title = (TextView) parentView.findViewById(R.id.tv_area4_title);

        tv_sk_des = (TextView) parentView.findViewById(R.id.tv_sk_des);
        tv_area1_des = (TextView) parentView.findViewById(R.id.tv_area1_des);
        tv_area2_des = (TextView) parentView.findViewById(R.id.tv_area2_des);
        tv_area3_des = (TextView) parentView.findViewById(R.id.tv_area3_des);
        tv_area4_des = (TextView) parentView.findViewById(R.id.tv_area4_des);

        img_sk = (ImageView) parentView.findViewById(R.id.img_sk);
        img_area1 = (ImageView) parentView.findViewById(R.id.img_area1);
        img_area2 = (ImageView) parentView.findViewById(R.id.img_area2);
        img_area3 = (ImageView) parentView.findViewById(R.id.img_area3);
        img_area4 = (ImageView) parentView.findViewById(R.id.img_area4);

        area1 = parentView.findViewById(R.id.area1);
        area2 = parentView.findViewById(R.id.area2);
        area3 = parentView.findViewById(R.id.area3);
        area4 = parentView.findViewById(R.id.area4);
        banner_area = parentView.findViewById(R.id.banner_area);

        parentView.findViewById(R.id.banner_area).setVisibility(View.VISIBLE);
        initTopBanner();
    }

    /**
     * 初始化轮播图
     */
    private void initTopBanner() {
        if (banner != null)
            banner.clear();
        banner = new TeBuyBanner(parentActivity,
                (ViewPager) parentView.findViewById(R.id.top_viewpager),
                (LinearLayout) parentView.findViewById(R.id.top_vp_dot));
        asyncBannerData();
    }

    /**
     * 请求banner数据
     */
    private void asyncBannerData() {
        String wholeUrl = AppUrl.host + AppUrl.GET_PROMOTION_BANNER;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "001");
        map.put("type", "2");
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                bannerLsner);
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
            List<PromotionBannerData> bannerlList = new ArrayList<PromotionBannerData>();
            JSONArray jay = jsonResult.optJSONArray("list");
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                PromotionBannerData bi = gson.fromJson(jot.toString(),
                        PromotionBannerData.class);
                bannerlList.add(bi);
            }
            if (bannerlList.size() < 1) {
                banner_area.setVisibility(View.GONE);
            } else {
                banner_area.setVisibility(View.VISIBLE);
                banner.init(parentActivity, bannerlList);
            }
        }
    };

    @Override
    public void initData() {
        plist = new ArrayList<PromotionType>();
        gcombList = new ArrayList<GoodsCombine>();
        temai_mid_area.setVisibility(View.VISIBLE);
        lv_top_cate.setVisibility(View.GONE);
        mGoodsListAdapter = new GoodsListAdapter(parentActivity, gcombList);
        lv_bottom_goods.setAdapter(mGoodsListAdapter);
        PAGE_NUM = 1;
        asyncGetTeMaiType();
        asyncGetTeMaiGoods(null);
    }

    @Override
    public void initListener() {
        secondkill_area.setOnClickListener(this);
        area1.setOnClickListener(this);
        area2.setOnClickListener(this);
        area3.setOnClickListener(this);
        area4.setOnClickListener(this);
//        lv_bottom_goods.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        scrollView.setCallbacks(new ObservableScrollView.Callbacks() {
            @Override
            public void onScrollChanged(int scrollY) {
                // 滚动到底部自动加载下一页数据
                if (scrollView.getChildAt(0).getMeasuredHeight() == scrollView
                        .getScrollY() + scrollView.getHeight()
                        && lvFootState.getCurrState() == ListViewFootState.IDLE) {
                    lvFootState.addListFootLoading();
                    PAGE_NUM++;
                    asyncGetTeMaiGoods(null);
                }
            }

            @Override
            public void onDownMotionEvent() {
            }

            @Override
            public void onUpOrCancelMotionEvent() {
            }
        });
        img_promotion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(parentActivity, WebViewActivity.class);
                String url = AppUrl.APP_INVITE_URL + "/toDiscuntList.do";
                it.putExtra("url", url);
                it.putExtra("PAGE_TITLE", "满减会场");
                CommonMethod.startOnlyNewActivity(parentActivity, WebViewActivity.class, it);
            }
        });
    }

    /**
     * 获取特卖种类
     */
    private void asyncGetTeMaiType() {
        String wholeUrl = AppUrl.host + AppUrl.GET_TEMAI_TYPE;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                getTeMaiTypeLsner);
    }

    BaseRequestListener getTeMaiTypeLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            parseJosnToTemaiType(jsonResult);
        }
    };

    /**
     * 解析特卖首页分类
     */
    public void parseJosnToTemaiType(JSONObject jsonResult) {
        if (jsonResult == null)
            return;
        JSONArray jay = jsonResult.optJSONArray("list");
        plist.clear();
        for (int i = 0; i < jay.length(); i++) {
            JSONObject jot = jay.optJSONObject(i);
            PromotionType p = gson.fromJson(jot.toString(), PromotionType.class);
            if (p.getPromotionName() != null && p.getPromotionName().contains("秒杀")) {
                plist.add(0, p);
            } else {
                plist.add(p);
            }
        }
        setPromotionTypeData();
    }

    /**
     * 填充促销类别数据
     */
    private void setPromotionTypeData() {
        if (!CommonMethod.isListEmpty(plist)) {
            tv_sk_title.setText(plist.get(0).getPromotionName());
            tv_sk_des.setText(plist.get(0).getPromotionDescription());
            ImageManager.getInstance().displayImg(img_sk, plist.get(0).getPicUrl());
            if (plist.size() > 1) {
                tv_area1_title.setText(plist.get(1).getPromotionName());
                tv_area1_des.setText(plist.get(1).getPromotionDescription());
                ImageManager.getInstance().displayImg(img_area1, plist.get(1).getPicUrl());
            }
            if (plist.size() > 2) {
                tv_area2_title.setText(plist.get(2).getPromotionName());
                tv_area2_des.setText(plist.get(2).getPromotionDescription());
                ImageManager.getInstance().displayImg(img_area2, plist.get(2).getPicUrl());
            }
            if (plist.size() > 3) {
                tv_area3_title.setText(plist.get(3).getPromotionName());
                tv_area3_des.setText(plist.get(3).getPromotionDescription());
                ImageManager.getInstance().displayImg(img_area3, plist.get(3).getPicUrl());
            }
            if (plist.size() > 4) {
                tv_area4_title.setText(plist.get(4).getPromotionName());
                tv_area4_des.setText(plist.get(4).getPromotionDescription());
                ImageManager.getInstance().displayImg(img_area4, plist.get(4).getPicUrl());
            }
        }
    }

    /**
     * 获取特卖商品
     */
    private void asyncGetTeMaiGoods(String promotionType) {
        String wholeUrl = AppUrl.host + AppUrl.GET_TEMAI_GOODS;
        String requestBodyData = ParamBuild.getTeMaiGoods(promotionType,
                PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                getTeMaiGoodsLsner);
    }

    BaseRequestListener getTeMaiGoodsLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            listStopLoadView();
            lvFootState.addListFootErr();
            if (PAGE_NUM > 1) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            listStopLoadView();
            parseJosnToTemaiGoods(jsonResult);
        }
    };

    /**
     * 解析特卖首页商品
     */
    public void parseJosnToTemaiGoods(JSONObject jsonResult) {
        if (jsonResult == null)
            return;
        ArrayList<ProductLimit> currentPageList = new ArrayList<ProductLimit>();
        JSONArray jay = jsonResult.optJSONArray("list");
        // 第一次查询的时候没有数据，则提示没有数据，页面置空
        if (PAGE_NUM == 1 && (jay == null || jay.length() <= 0)) {
            gcombList.clear();
            mGoodsListAdapter = new GoodsListAdapter(parentActivity, gcombList);
            lv_bottom_goods.setAdapter(mGoodsListAdapter);
            lvFootState.addListFootEnd();
            return;
        }
        // 首次加载清空所有项列表,并重置控件为可下滑
        if (PAGE_NUM == 1) {
            gcombList.clear();
        }
        // 如果当前页已经是最后一页，则列表控件置为不可下滑
        if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
            lvFootState.addListFootEnd();
        } else {
            lvFootState.addListFootIdle();
        }
        for (int i = 0; i < jay.length(); i++) {
            JSONObject jot = jay.optJSONObject(i);
            ProductLimit p = gson.fromJson(jot.toString(), ProductLimit.class);
            currentPageList.add(p);
        }
        for (int i = 0; i < currentPageList.size(); i = i + 2) {
            GoodsCombine comb = new GoodsCombine();
            if (currentPageList.get(i) != null)
                comb.setGoodsLeft(currentPageList.get(i));
            if (i + 1 < currentPageList.size()
                    && currentPageList.get(i + 1) != null)
                comb.setGoodsRight(currentPageList.get(i + 1));
            gcombList.add(comb);
        }
        mGoodsListAdapter.notifyDataSetChanged();
    }

    /**
     * 停止列表进度条
     */
    protected void listStopLoadView() {
        mPullRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        int pos = 0;
        String promotionType = null;
        String title = "";
        switch (v.getId()) {
            case R.id.secondkill_area:
                pos = 0;
                break;
            case R.id.area1:
                pos = 1;
                break;
            case R.id.area2:
                pos = 2;
                break;
            case R.id.area3:
                pos = 3;
                break;
            case R.id.area4:
                pos = 4;
                break;
        }
        if (pos == 0) {
            promotionType = plist.get(pos).getPromotionType();
            title = plist.get(pos).getPromotionName();
            Intent it = new Intent(parentActivity, SecKillActivityNew.class);
            it.putExtra("promotionType", promotionType);
            it.putExtra("title", title);
            parentActivity.startActivity(it);
        } else if(pos == 1 ){
            Intent it = new Intent(parentActivity, GroupPurchaseActivity.class);
            it.putExtra("promotionType", promotionType);
            parentActivity.startActivity(it);
        } else if (pos == 3) {
            promotionType = plist.get(pos).getPromotionType();
            title = plist.get(pos).getPromotionName();
            Intent it = new Intent(parentActivity, OverValueActivity.class);
            it.putExtra("promotionType", promotionType);
            it.putExtra("title", title);
            parentActivity.startActivity(it);
        } else if (pos == 2) {
            promotionType = plist.get(pos).getPromotionType();
            Intent it = new Intent(parentActivity, MaMaGroupActivity.class);
            it.putExtra("promotionType", promotionType);
            parentActivity.startActivity(it);
        } else if (pos == 4) {
            promotionType = plist.get(pos).getPromotionType();
            Intent it = new Intent(parentActivity, OverseasGoodsActivity.class);
            it.putExtra("promotionType", promotionType);
            parentActivity.startActivity(it);
        }
    }

}
