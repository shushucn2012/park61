package com.park61.moduel.order;

import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.order.adapter.LogisticListAdapter;
import com.park61.moduel.order.adapter.LogisticsDetailListAdapter;
import com.park61.moduel.order.bean.LogisticBean;
import com.park61.moduel.order.bean.LogisticsInfoBean;
import com.park61.moduel.order.bean.OrderTrack;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单物流详情页面
 */
public class LogisticsDetailActivty extends BaseActivity {
    private LogisticsDetailListAdapter mAdapter;
    private List<OrderTrack> mList;
    private ListView lv_listview,lv_listview2;
    private TextView tv_state, tv_company, tv_logisitic_number, tv_order_number;
    private View delivery_area, companyname_area;
    private long soId;
    private int merchantId;//商家id,2:普通商家，3：京东商家
    private LogisticsInfoBean info;
    private String orderDeliveryCompany;//物流名称

    private LogisticListAdapter logisticListAdapter;
    private List<LogisticBean> logisticList;
    private long orderDeliveryCode;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_orderdetail_logistics_detail);
    }

    @Override
    public void initView() {
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_company = (TextView) findViewById(R.id.tv_company);
        tv_logisitic_number = (TextView) findViewById(R.id.tv_logisitic_number);
        tv_order_number = (TextView) findViewById(R.id.tv_order_number);
        lv_listview = (ListView) findViewById(R.id.lv_listview);
        lv_listview2 = (ListView) findViewById(R.id.lv_listview2);
        delivery_area = findViewById(R.id.delivery_area);
        companyname_area = findViewById(R.id.companyname_area);
    }

    @Override
    public void initData() {
        soId = getIntent().getLongExtra("soId", 0l);
        mList = new ArrayList<OrderTrack>();
        mAdapter = new LogisticsDetailListAdapter(mList, mContext);
        lv_listview.setAdapter(mAdapter);

        logisticList = new ArrayList<LogisticBean>();
        logisticListAdapter = new LogisticListAdapter(logisticList,mContext);
        lv_listview2.setAdapter(logisticListAdapter);

        asyncGetEmsDetail();
    }

    @Override
    public void initListener() {
        // TODO Auto-generated method stub

    }

    private void asyncGetEmsDetail() {
		String wholeUrl = AppUrl.host + AppUrl.ORDER_TRACK;
		String requestBodyData = ParamBuild.getOrderTrack(soId);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {
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
            info = gson.fromJson(jsonResult.toString(), LogisticsInfoBean.class);
            merchantId = info.getBasicMessage().getMerchantId();
            orderDeliveryCompany = info.getBasicMessage().getOrderDeliveryCompany();
            if (!TextUtils.isEmpty(orderDeliveryCompany)) {
                companyname_area.setVisibility(View.VISIBLE);
                tv_company.setText(orderDeliveryCompany);
            } else {
                companyname_area.setVisibility(View.GONE);
            }
            tv_state.setText(info.getBasicMessage().getOrderStatusName());
            tv_order_number.setText(info.getBasicMessage().getId() + "");
            orderDeliveryCode = info.getBasicMessage().getOrderDeliveryCode();
//            //orderDeliveryCode = 2835463340l;
            for (int i = 0; i < info.getTrack().size(); i++) {
                OrderTrack item = info.getTrack().get(i);
                mList.add(item);
            }
            mAdapter.notifyDataSetChanged();
            if (orderDeliveryCode == 0l) {
                delivery_area.setVisibility(View.GONE);
            } else {
                delivery_area.setVisibility(View.VISIBLE);
                tv_logisitic_number.setText(orderDeliveryCode + "");
//                asyncEmsDetail();
            }
        }
    };

    /**
     * 第三方物流接口
     */
    private void asyncEmsDetail(){
        String wholeUrl = "http://p.kuaidi100.com/mobile/mobileapi.do?json={%22num%22:%22"+orderDeliveryCode +"%22}&method=query";
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                emsListener);
    }
    BaseRequestListener emsListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            String name = jsonResult.optJSONObject("lastResult").optJSONObject("routeInfo").optJSONObject("to").optString("name");
            JSONArray jay = jsonResult.optJSONObject("lastResult").optJSONArray("data");
            for(int i=0;i<jay.length();i++){
                JSONObject jot = jay.optJSONObject(i);
                LogisticBean item = gson.fromJson(jot.toString(),LogisticBean.class);
                logisticList.add(item);
            }
            logisticListAdapter.notifyDataSetChanged();

        }

        @Override
        public void onStart(int requestId) {

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {

        }
    };

}
