package com.park61.moduel.me.actlist;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.ActsOrderDetailActivity;
import com.park61.moduel.acts.PayConfirmActivity;
import com.park61.moduel.me.GameEvaluateActivity;
import com.park61.moduel.me.adapter.ActOrderListAdapter;
import com.park61.moduel.me.adapter.ActOrderListAdapter.ApplyItemCallBack;
import com.park61.moduel.me.bean.ApplyActItem;
import com.park61.moduel.pay.PayGoodsConfirmActivity;
import com.park61.net.base.Request;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActOrderListActivity extends BaseActivity implements ApplyItemCallBack {

    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;
    public static boolean needRefresh = false;// 是否需要刷新数据，避免每次进入页面都要刷新
    private List<ApplyActItem> orderList;
    private String curOrderListState;//当前活动订单列表的状态

    private PullToRefreshListView mPullRefreshListView;
    private ActOrderListAdapter mAdapter;
    private TextView tv_top_title;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_me_actlist);
    }

    @Override
    public void initView() {
        tv_top_title = (TextView) findViewById(R.id.tv_top_title);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetApplyActs();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetApplyActs();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needRefresh) {
            asyncGetApplyActs();
            needRefresh = false;//刷新之后重置
        }
    }

    @Override
    public void initData() {
        needRefresh = true; //数据初始化的时候重置为需要刷新
        curOrderListState = getIntent().getStringExtra("ACT_ORDER_STATE");
        if (GlobalParam.ActOrderState.WAITFORPAY.equals(curOrderListState)) {
            tv_top_title.setText("待付款的游戏");
        } else if (GlobalParam.ActOrderState.APPLIED.equals(curOrderListState)) {
            tv_top_title.setText("待参加的游戏");
        } else if (GlobalParam.ActOrderState.WAITFORCOMT.equals(curOrderListState)) {
            tv_top_title.setText("待评价的游戏");
        } else if (GlobalParam.ActOrderState.DONE.equals(curOrderListState)) {
            tv_top_title.setText("参加过的游戏");
        }
        orderList = new ArrayList<ApplyActItem>();
        mAdapter = new ActOrderListAdapter(mContext, orderList, curOrderListState);
        mAdapter.setListener(ActOrderListActivity.this);
        mPullRefreshListView.getRefreshableView().setAdapter(mAdapter);

    }

    @Override
    public void initListener() {

    }

    /**
     * 请求已报名的游戏数据
     */
    private void asyncGetApplyActs() {
        String wholeUrl = "";
        if (GlobalParam.ActOrderState.WAITFORPAY.equals(curOrderListState)) {
            wholeUrl = AppUrl.host + AppUrl.GET_TOPAY_ACTLIST;
        } else if (GlobalParam.ActOrderState.APPLIED.equals(curOrderListState)) {
            wholeUrl = AppUrl.host + AppUrl.GET_APPLY_ACTS_END;
        } else if (GlobalParam.ActOrderState.WAITFORCOMT.equals(curOrderListState)) {
            wholeUrl = AppUrl.host + AppUrl.GET_TO_EVALUATE_ACTS_END;
        } else if (GlobalParam.ActOrderState.DONE.equals(curOrderListState)) {
            wholeUrl = AppUrl.host + AppUrl.GET_DONE_ACTS_END;
        }
        String requestBodyData = ParamBuild.getActList(PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            if (PAGE_NUM == 1) {
                showDialog();
            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            showShortToast(errorMsg);
            if(PAGE_NUM > 1){//滚动更多时如果失败，页码回退
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            ArrayList<ApplyActItem> currentPageList = new ArrayList<ApplyActItem>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                orderList.clear();
                mAdapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, mPullRefreshListView.getRefreshableView());
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                orderList.clear();
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM == jsonResult.optInt("totalPage")) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                ApplyActItem c = gson.fromJson(actJot.toString(), ApplyActItem.class);
                currentPageList.add(c);
            }
            orderList.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 请求取消已报名的游戏
     */
    private void asyncCancelApplyActs(Long applyId) {
        String wholeUrl = AppUrl.host + AppUrl.CANCEL_APPLY_ACTS_END;
        String requestBodyData = ParamBuild.cancelApply(applyId);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, cancellistener);
    }

    BaseRequestListener cancellistener = new JsonRequestListener() {

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
            showShortToast("取消游戏报名成功！");
            // 刷新数据
            PAGE_NUM = 1;
            asyncGetApplyActs();
        }
    };

    /**
     * 取消商品订单
     */
    private void asyncCancelOrder(Long orderId) {
        String wholeUrl = AppUrl.host + AppUrl.ORDER_CANCEL;
        String requestBodyData = ParamBuild.getOrderDetail(orderId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, canceOrderListner);
    }

    BaseRequestListener canceOrderListner = new JsonRequestListener() {

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
            showShortToast("取消游戏报名成功!");
            // 刷新数据
            PAGE_NUM = 1;
            asyncGetApplyActs();
        }
    };

    @Override
    public void onDetailsClicked(int pos) {
        Intent it = new Intent(mContext, ActsOrderDetailActivity.class);
        ApplyActItem aai = orderList.get(pos);
        it.putExtra("orderId", aai.getOrderId());
        it.putExtra("applyId", aai.getId());
        it.putExtra("orderState", curOrderListState);
        it.putExtra("actBussinessType", aai.getActBussinessType());
        startActivity(it);
    }

    @Override
    public void onShareClicked(int pos) {
        String shareUrl = String.format(AppUrl.ACT_SHARE_URL, orderList.get(pos).getActId() + "");
        String picUrl = orderList.get(pos).getActCover();
        String title = getString(R.string.app_name);
        String description = orderList.get(pos).getActTitle() + "\n 游戏详情";
        showShareDialog(shareUrl, picUrl, title, description);
    }

    @Override
    public void onComtClicked(int pos) {
//        Intent it = new Intent(mContext, ActsEvaluateActivity.class);
        Intent it = new Intent(mContext, GameEvaluateActivity.class);
        it.putExtra("applyId", orderList.get(pos).getId());
        it.putExtra("actCover", orderList.get(pos).getActCover());
        startActivity(it);
    }

    @Override
    public void onCancelClicked(final int pos) {
        dDialog.showDialog("提醒", "确认取消游戏报名吗？", "确认", "取消",
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (orderList.get(pos).getOrderId() == null) {
                            asyncCancelApplyActs(orderList.get(pos).getId());
                        } else {
                            asyncCancelOrder(orderList.get(pos).getOrderId());
                        }
                        dDialog.dismissDialog();
                    }
                }, null);
    }

    @Override
    public void onPayClicked(int pos) {
        if (orderList.get(pos).getOrderId() == null) {
            Intent it = new Intent(mContext, PayConfirmActivity.class);
            it.putExtra("applyId", orderList.get(pos).getId());
            startActivity(it);
        }else {
            Intent payIt = new Intent(mContext, PayGoodsConfirmActivity.class);
            payIt.putExtra("orderId", orderList.get(pos).getOrderId());
            startActivity(payIt);
        }
    }

}
