package com.park61.moduel.sales.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.HtmlParserTool;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.bean.Product;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

public class FragmentGoodsDetails extends BaseFragment {

    private PullToRefreshScrollView mPullRefreshScrollView;
    private RadioGroup mtabGroup;

    private LinearLayout details_content, ask_content;
    private WebView webview, webview_problem;

    private Product productWebData;// 商品文描信息

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_goodsdetails, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            ((GoodsDetailsActivity) parentActivity).img_to_top.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(webview != null) {
                        webview.setVisibility(View.VISIBLE);
                    }
                    if(webview_problem != null) {
                        webview_problem.setVisibility(View.VISIBLE);
                    }
                }
            }, 200);
        } else {
            if(webview != null) {
                webview.setVisibility(View.INVISIBLE);
            }
            if(webview_problem != null) {
                webview_problem.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void initView() {
        // 初始化上下拉刷新控件
        mPullRefreshScrollView = (PullToRefreshScrollView) parentView.findViewById(R.id.pull_refresh_scrollview);
        ILoadingLayout startLabels = mPullRefreshScrollView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉查看上一页");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("放开查看上一页");// 刷新时
        startLabels.setReleaseLabel("放开查看上一页");// 下来达到一定距离时，显示的提示
        startLabels.setLoadingDrawable(null);

        ILoadingLayout endLabels = mPullRefreshScrollView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉查看购物评论");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("放开查看购物评论");// 刷新时
        endLabels.setReleaseLabel("放开查看购物评论");// 下来达到一定距离时，显示的提示
        endLabels.setLoadingDrawable(null);

        webview = (WebView) parentView.findViewById(R.id.webview);
        webview.setFocusable(false);
        webview_problem = (WebView) parentView.findViewById(R.id.webview_problem);
        webview_problem.setFocusable(false);
        details_content = (LinearLayout) parentView.findViewById(R.id.details_content);
        ask_content = (LinearLayout) parentView.findViewById(R.id.ask_content);

        initTabs();
    }

    /**
     * 初始化标签
     */
    private void initTabs() {
        mtabGroup = (RadioGroup) parentView.findViewById(R.id.mtab_group);
        mtabGroup.check(R.id.rb_goodsdetails);
        mtabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedIndex = 0;
                Log.out("checkedId======" + checkedId);
                for (int i = 0; i < group.getChildCount(); i++) {
                    Log.out("getId======" + group.getChildAt(i).getId());
                    if (checkedId == group.getChildAt(i).getId()) {
                        checkedIndex = i;
                    }
                }
                swithPage(checkedIndex);
            }
        });
    }

    /**
     * 切换详情和游戏评价
     */
    public void swithPage(int pageIndex) {
        switch (pageIndex) {
            case 0:
                details_content.setVisibility(View.VISIBLE);
                ask_content.setVisibility(View.GONE);
                break;
            case 2:
                details_content.setVisibility(View.GONE);
                ask_content.setVisibility(View.VISIBLE);
                webview_problem.loadUrl(" file:///android_asset/problem.html ");
                webview_problem.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void initData() {
        asyncGetGoodsWebData();
    }

    @Override
    public void initListener() {
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullRefreshScrollView.onRefreshComplete();
                ((GoodsDetailsActivity) parentActivity).getThisViewPager().setPageTransformer(true, new UpDownPageTransformer());
                ((GoodsDetailsActivity) parentActivity).getThisViewPager().setCurrentItem(0, true);
                //((GoodsDetailsActivity) parentActivity).img_to_top.setVisibility(View.GONE);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullRefreshScrollView.onRefreshComplete();
                ((GoodsDetailsActivity) parentActivity).getThisViewPager().setPageTransformer(true, new UpDownPageTransformer());
                ((GoodsDetailsActivity) parentActivity).getThisViewPager().setCurrentItem(2, true);
            }
        });
    }

    /**
     * 获取商品web详情
     */
    protected void asyncGetGoodsWebData() {
        String wholeUrl = AppUrl.host + AppUrl.GET_GOODS_DETAILS_END;
        Long goodsId = ((GoodsDetailsActivity) parentActivity).productDetail.getPmInfo().getId();
        String requestBodyData = ParamBuild.getGoodsDetails(goodsId, null, null);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getGoodsWebDataLsner);
    }

    BaseRequestListener getGoodsWebDataLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            productWebData = gson.fromJson(jsonResult.toString(), Product.class);
            setWebData();
        }
    };

    private void setWebData() {
        String htmlDetailsStr = "";
        try {
            htmlDetailsStr = HtmlParserTool.replaceImgStr(productWebData.getProductDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
        webview.loadDataWithBaseURL(null, htmlDetailsStr, "text/html", "utf-8", null);
    }


}
