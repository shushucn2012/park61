package com.park61.moduel.sales.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.moduel.sales.GoodsCmtListActivity;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.adapter.GoodsDetailsComtFiller;
import com.park61.moduel.sales.bean.ProductEvaluate;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.NetLoadingView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentGoodsComment extends BaseFragment {

    private PullToRefreshScrollView mPullRefreshScrollView;
    private LinearLayout linear_comment;
    private NetLoadingView view_loading;
    private TextView tv_cmt_num;
    private View area_look_all;

    private List<ProductEvaluate> comtList;// 评价列表

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_goodscomment, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            ((GoodsDetailsActivity) parentActivity).img_to_top.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initView() {
        // 初始化上下拉刷新控件
        mPullRefreshScrollView = (PullToRefreshScrollView) parentView.findViewById(R.id.pull_refresh_scrollview);
        ILoadingLayout startLabels = mPullRefreshScrollView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉查看图文详情");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("放开查看图文详情");// 刷新时
        startLabels.setReleaseLabel("放开查看图文详情");// 下来达到一定距离时，显示的提示
        startLabels.setLoadingDrawable(null);

        view_loading = (NetLoadingView) parentView.findViewById(R.id.view_loading);
        linear_comment = (LinearLayout) parentView.findViewById(R.id.linear_comment);
        tv_cmt_num = (TextView) parentView.findViewById(R.id.tv_cmt_num);
        area_look_all = parentView.findViewById(R.id.area_look_all);
    }

    @Override
    public void initData() {
        // 初始化评论列表
        comtList = new ArrayList<ProductEvaluate>();
        asyncGetGoodsComts();
    }

    @Override
    public void initListener() {
        view_loading.setOnRefreshClickedLsner(new NetLoadingView.OnRefreshClickedLsner() {
            @Override
            public void OnRefreshClicked() {
                asyncGetGoodsComts();
            }
        });
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullRefreshScrollView.onRefreshComplete();
                ((GoodsDetailsActivity) parentActivity).getThisViewPager().setPageTransformer(true, new UpDownPageTransformer());
                ((GoodsDetailsActivity) parentActivity).getThisViewPager().setCurrentItem(1, true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }
        });
        area_look_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(parentActivity, GoodsCmtListActivity.class);
                it.putExtra("GOODS_ID", ((GoodsDetailsActivity) parentActivity).productDetail.getPmInfo().getId());
                startActivity(it);
            }
        });
    }

    /**
     * 获取商品评价列表
     */
    protected void asyncGetGoodsComts() {
        String wholeUrl = AppUrl.host + AppUrl.GET_GOODS_COMMENTS_END;
        Long goodsId = ((GoodsDetailsActivity) parentActivity).productDetail.getPmInfo().getId();
        String requestBodyData = ParamBuild.getGoodsComts(goodsId, 1, 10);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, comtslistener);
    }

    BaseRequestListener comtslistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            //showDialog();
            view_loading.showLoading(linear_comment);
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            //dismissDialog();
            view_loading.showRefresh();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            //dismissDialog();
            view_loading.hideLoading(linear_comment);
            //把总数显示出来
            tv_cmt_num.setText(FU.paseInt(jsonResult.optInt("totalSize") + "") + "");
            ArrayList<ProductEvaluate> currentPageList = new ArrayList<ProductEvaluate>();
            JSONArray cmtJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (cmtJay == null || cmtJay.length() <= 0) {
                comtList.clear();
                return;
            }
            for (int i = 0; i < cmtJay.length(); i++) {
                JSONObject actJot = cmtJay.optJSONObject(i);
                ProductEvaluate p = gson.fromJson(actJot.toString(), ProductEvaluate.class);
                currentPageList.add(p);
            }
            comtList.addAll(currentPageList);
            GoodsDetailsComtFiller.buildListInLinear(linear_comment, comtList, parentActivity);
        }
    };


}
