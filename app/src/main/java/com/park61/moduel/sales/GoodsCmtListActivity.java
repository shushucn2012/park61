package com.park61.moduel.sales;

import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.sales.adapter.GoodsDetailsComtFiller;
import com.park61.moduel.sales.bean.ProductEvaluate;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shushucn2012 on 2016/12/14.
 */
public class GoodsCmtListActivity extends BaseActivity {

    private PullToRefreshScrollView mPullRefreshScrollView;
    private LinearLayout linear_comment;

    private Long goodsId;
    private int PAGE_NUM = 1;// 评论列表页码
    private final int PAGE_SIZE = 10;// 评论列表每页条数
    private boolean isEnd;// 评论列表是否到最后一页了
    private List<ProductEvaluate> comtList;// 评价列表

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_goodscmt_list);
    }

    @Override
    public void initView() {
        // 初始化上下拉刷新控件
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        ViewInitTool.initPullToRefresh(mPullRefreshScrollView, mContext);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                PAGE_NUM = 1;
                asyncGetGoodsComts();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (isEnd) {
                    showShortToast("已经是最后一页了");
                    mPullRefreshScrollView.onRefreshComplete();
                } else {
                    PAGE_NUM++;
                    asyncGetGoodsComts();
                }
            }
        });
        linear_comment = (LinearLayout) findViewById(R.id.linear_comment);
    }

    @Override
    public void initData() {
        goodsId = getIntent().getLongExtra("GOODS_ID", -1);
        PAGE_NUM = 1;
        isEnd = false;
        // 初始化评论列表
        comtList = new ArrayList<ProductEvaluate>();
        asyncGetGoodsComts();
    }

    @Override
    public void initListener() {

    }

    /**
     * 获取商品评价列表
     */
    protected void asyncGetGoodsComts() {
        String wholeUrl = AppUrl.host + AppUrl.GET_GOODS_COMMENTS_END;
        String requestBodyData = ParamBuild.getGoodsComts(goodsId, PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, comtslistener);
    }

    BaseRequestListener comtslistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            listStopLoadView();
            showShortToast(errorMsg);
            if(PAGE_NUM > 1){//如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            listStopLoadView();
            ArrayList<ProductEvaluate> currentPageList = new ArrayList<ProductEvaluate>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                showShortToast("没有评论数据！");
                comtList.clear();
                isEnd = true;
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                comtList.clear();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM == jsonResult.optInt("totalPage")) {
                isEnd = true;
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                ProductEvaluate p = gson.fromJson(actJot.toString(), ProductEvaluate.class);
                currentPageList.add(p);
            }
            comtList.addAll(currentPageList);
            GoodsDetailsComtFiller.buildListInLinear(linear_comment, comtList, mContext);
        }
    };

    protected void listStopLoadView() {
        mPullRefreshScrollView.onRefreshComplete();
    }
}
