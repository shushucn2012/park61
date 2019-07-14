package com.park61.moduel.order.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.openmember.MemberOrderDetailActivity;
import com.park61.moduel.order.MyOrderActivty;
import com.park61.moduel.order.TradeOrderDetailActivity;
import com.park61.moduel.order.adapter.TradeOrderListAdapter;
import com.park61.moduel.order.bean.OrderInfoBean;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TradeOrderPaperFragment extends BaseFragment {

    private final int PAGE_SIZE = 10;

    private View area_error_tip;
    private PullToRefreshListView mPullRefreshListView;
    private ArrayList<OrderInfoBean> mList;
    private TradeOrderListAdapter mAdapter;

    private int orderListType;
    private int page_num = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_myorders, container, false);
        orderListType = getArguments().getInt("orderListType");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        registerRefreshReceiver();
        area_error_tip = parentView.findViewById(R.id.area_error_tip);
        mPullRefreshListView = (PullToRefreshListView) parentView.findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, parentActivity);
    }

    @Override
    public void initData() {
        mList = new ArrayList<OrderInfoBean>();
        mAdapter = new TradeOrderListAdapter((BaseActivity) parentActivity, this, mList, orderListType);
        mPullRefreshListView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        page_num = 1;
        asyncGetOrderList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalParam.MyOrderNeedRefresh) {
            page_num = 1;
            asyncGetOrderList();
            GlobalParam.MyOrderNeedRefresh = false;
            android.util.Log.e("MyOrderNeedRefresh", "1111111111111111111111111");
            android.util.Log.e("MyOrderNeedRefresh", "orderListType==========" + orderListType);
        }
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page_num = 1;
                asyncGetOrderList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page_num++;
                asyncGetOrderList();
            }
        });
        mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position <= mList.size()) {
                    if ((mList.get(position - 1).orderType) == 3) {
                        Intent it = new Intent(parentActivity, MemberOrderDetailActivity.class);
                        it.putExtra("orderid", mList.get(position - 1).id);
                        it.putExtra("orderType", mList.get(position - 1).orderType);
                        startActivity(it);
                    } else {
                        Intent it = new Intent(parentActivity, TradeOrderDetailActivity.class);
                        it.putExtra("orderid", mList.get(position - 1).id);
                        startActivity(it);
                    }
                }
            }
        });
    }

    /**
     * 请求订单列表数据
     */
    private void asyncGetOrderList() {
        area_error_tip.setVisibility(View.GONE);
        String wholeUrl = AppUrl.host + AppUrl.ORDER_LIST;
        String requestBodyData = ParamBuild.getOrderList(orderListType, page_num, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, listener);
    }

    JsonRequestListener listener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            mPullRefreshListView.onRefreshComplete();
            showShortToast("获取数据失败!");
            if (page_num == 1 && mList.size() == 0) {
                area_error_tip.setVisibility(View.VISIBLE);
                mPullRefreshListView.setMode(Mode.PULL_FROM_START);
            }
            if (page_num >= 1) {// 如果是加载更多，失败时回退页码
                page_num--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            Log.i("OrderPaperFragment", "onSuccess:" + requestId + ",jsonResult:" + jsonResult.toString());
            mPullRefreshListView.onRefreshComplete();
            dismissDialog();
            JSONArray orders_js = jsonResult.optJSONArray("list");
            if (page_num == 1 && (orders_js == null || orders_js.length() <= 0)) {
                area_error_tip.setVisibility(View.VISIBLE);
                mList.clear();
                mAdapter.notifyDataSetChanged();
                mPullRefreshListView.setMode(Mode.PULL_FROM_START);
                return;
            }
            if (page_num == 1) {
                mList.clear();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (page_num >= jsonResult.optInt("totalPage")) {
                mPullRefreshListView.setMode(Mode.PULL_FROM_START);
            } else {
                mPullRefreshListView.setMode(Mode.BOTH);
            }
            for (int i = 0; i < orders_js.length(); i++) {
                JSONObject orderJot = orders_js.optJSONObject(i);
                OrderInfoBean oib = gson.fromJson(orderJot.toString(), OrderInfoBean.class);
                mList.add(oib);
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void showShortToast(String text) {
        if (orderListType == ((MyOrderActivty) parentActivity).getCurPaperIndex() + 1) {
            super.showShortToast(text);
        }
    }

    @Override
    public void showDialog() {
        if (orderListType == ((MyOrderActivty) parentActivity).getCurPaperIndex() + 1) {
            super.showDialog();
        }
    }

    /**
     * 其他页面要改变当前页的广播
     */
    private void registerRefreshReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_REFRESH_ORDER");
        parentActivity.registerReceiver(orderChangeReceiver, filter);
    }

    private BroadcastReceiver orderChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收到广播，改变当前tab页
            Log.out("onReceive______orderListType=================================" + orderListType);
            int type = intent.getIntExtra("ORDER_TYPE", -1);
            Log.out("onReceive______type=================================" + type);
            if (type > 0 && type == orderListType) {
                page_num = 1;
                asyncGetOrderList();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        parentActivity.unregisterReceiver(orderChangeReceiver);
    }
}
