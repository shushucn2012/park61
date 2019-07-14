package com.park61.moduel.order;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.moduel.order.adapter.ParentalSoGoodsListAdapter;
import com.park61.moduel.order.bean.MeOrderDetailBean;
import com.park61.moduel.order.bean.MeOrderDetailsGoodsBean;
import com.park61.moduel.order.bean.OrderState;
import com.park61.moduel.pay.PayGoodsConfirmActivity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.list.ListViewForScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品订单详情页面
 * create by super on 20180111
 */
public class MeOrderDetailActivity extends BaseActivity {

    private long orderId;
    private MeOrderDetailBean meOrderDetailBean;
    private List<MeOrderDetailsGoodsBean> mList;
    private ParentalSoGoodsListAdapter soGoodsListAdapter;
    private String telPhone;

    private ListViewForScrollView lv_orderdetail_goods;
    private TextView tv_orderstatue, tv_order_number, tv_order_time, tv_receiver_name, tv_receiver_phone, tv_address,
            tv_amount_value, tv_ems_value, tv_real_amount_value, tv_to_saleafter, tv_remark;
    private ImageView img_state;
    private View bottom_bar, area_unpay, area_cfm_get, area_remark;
    private Button btn_cancel, btn_pay, btn_cfm_get;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_me_orderdetail);
    }

    @Override
    public void initView() {
        lv_orderdetail_goods = (ListViewForScrollView) findViewById(R.id.lv_orderdetail_goods);
        tv_orderstatue = (TextView) findViewById(R.id.tv_orderstatue);
        img_state = (ImageView) findViewById(R.id.img_state);
        bottom_bar = findViewById(R.id.bottom_bar);
        area_unpay = findViewById(R.id.area_unpay);
        area_cfm_get = findViewById(R.id.area_cfm_get);
        tv_order_number = (TextView) findViewById(R.id.tv_order_number);
        tv_order_time = (TextView) findViewById(R.id.tv_order_time);
        tv_receiver_name = (TextView) findViewById(R.id.tv_receiver_name);
        tv_receiver_phone = (TextView) findViewById(R.id.tv_receiver_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_amount_value = (TextView) findViewById(R.id.tv_amount_value);
        tv_ems_value = (TextView) findViewById(R.id.tv_ems_value);
        tv_real_amount_value = (TextView) findViewById(R.id.tv_real_amount_value);
        tv_to_saleafter = (TextView) findViewById(R.id.tv_to_saleafter);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_cfm_get = (Button) findViewById(R.id.btn_cfm_get);
        tv_remark = (TextView) findViewById(R.id.tv_remark);
        area_remark = findViewById(R.id.area_remark);
    }

    @Override
    public void initData() {
        orderId = getIntent().getLongExtra("orderid", -1);

        mList = new ArrayList<>();
        soGoodsListAdapter = new ParentalSoGoodsListAdapter(mContext, mList);
        lv_orderdetail_goods.setAdapter(soGoodsListAdapter);

        asyncGetOrderDetail();
        asyncGetTel();
    }

    @Override
    public void initListener() {
        tv_to_saleafter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(telPhone)) {
                    telPhone = "02765524760";
                }
                dDialog.showDialog("提醒", "确定需要拨打" + telPhone + "申请售后吗？", "取消", "确定", null, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telPhone));
                        startActivity(intent);
                        dDialog.dismissDialog();
                    }
                });
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDialog.showDialog("提示", "确定要取消该订单吗？", "取消", "确定", null, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                        asyncCancelOrder();
                    }
                });
            }
        });
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, PayGoodsConfirmActivity.class);
                it.putExtra("orderId", orderId);
                startActivity(it);
                finish();
            }
        });
        btn_cfm_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDialog.showDialog("提示", "确定已收到货物吗？", "取消", "确定", null, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                        asyncConfirmReceiptGoods();
                    }
                });
            }
        });
    }

    /**
     * 请求获取售后电话
     */
    private void asyncGetTel() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getTel;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, tlistener);
    }

    BaseRequestListener tlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            telPhone = jsonResult.optString("data");
        }
    };

    /**
     * 请求取消订单
     */
    private void asyncCancelOrder() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.cancelOrder;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", orderId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, clistener);
    }

    BaseRequestListener clistener = new JsonRequestListener() {

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
            showShortToast("取消订单成功");
            //发送刷新广播
            mContext.sendBroadcast(new Intent().setAction("ACTION_REFRESH_ORDER"));
            finish();
        }
    };

    /**
     * 请求确认订单
     */
    private void asyncConfirmReceiptGoods() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.confirmGoods;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", orderId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, crlistener);
    }

    BaseRequestListener crlistener = new JsonRequestListener() {

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
            showShortToast("确认收货成功");
            //发送刷新广播
            mContext.sendBroadcast(new Intent().setAction("ACTION_REFRESH_ORDER"));
            asyncGetOrderDetail();
        }
    };

    /**
     * 请求订单详情
     */
    private void asyncGetOrderDetail() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getOrderDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
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
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            meOrderDetailBean = gson.fromJson(jsonResult.toString(), MeOrderDetailBean.class);
            mList.clear();
            mList.addAll(meOrderDetailBean.getItems());
            soGoodsListAdapter.notifyDataSetChanged();

            setDataToView();
        }
    };

    public void setDataToView() {
        tv_order_number.setText("订单编号：" + meOrderDetailBean.getId());
        tv_order_time.setText("下单时间：" + meOrderDetailBean.getOrderCreateTime());
        if (TextUtils.isEmpty(meOrderDetailBean.getOrderCancelReason())) {//订单取消原因为空
            tv_orderstatue.setText(meOrderDetailBean.getOrderStatusName());
        } else {
            tv_orderstatue.setText(meOrderDetailBean.getOrderCancelReason());
        }
        tv_amount_value.setText(FU.RENMINBI_UNIT + FU.strDbFmt(meOrderDetailBean.getProductAmount()));
        tv_real_amount_value.setText(FU.RENMINBI_UNIT + FU.strDbFmt(meOrderDetailBean.getOrderToPayAmount()));
        tv_ems_value.setText(FU.RENMINBI_UNIT + FU.strDbFmt(meOrderDetailBean.getOrderDeliveryFee()));
        if (TextUtils.isEmpty(meOrderDetailBean.getOrderRemark())) {
            area_remark.setVisibility(View.GONE);
        } else {
            area_remark.setVisibility(View.VISIBLE);
            tv_remark.setText(meOrderDetailBean.getOrderRemark());
        }

        //order_statue_info订单状态信息：1 等待买家付款，2 买家已付款，3 等待商家发货，4 商品正在配送中，5 商品已签收
        //6 商品送货失败, 7 申请退货中,8 商品正在退货中,9 退货完成,10 订单已取消,11 交易完成
        if (meOrderDetailBean.getOrderStatus() == OrderState.ORDER_STATUS_ORDERED_WAITING_FOR_PAYMENT) {//1 已下单:货款未全收
            //tv_orderstatue.setText("待付款");
            img_state.setImageResource(R.drawable.dengdaifukuan);
            bottom_bar.setVisibility(View.VISIBLE);
            area_unpay.setVisibility(View.VISIBLE);
            area_cfm_get.setVisibility(View.GONE);
            tv_to_saleafter.setVisibility(View.GONE);
        } else if (meOrderDetailBean.getOrderStatus() == OrderState.ORDER_STATUS_ORDERED_PAYED//2 已下单:货款已全收
                || meOrderDetailBean.getOrderStatus() == OrderState.ORDER_STATUS_TRUNED_TO_DO) {//3 已转DO
            //tv_orderstatue.setText("待收货");
            img_state.setImageResource(R.drawable.peisongzhong);
            bottom_bar.setVisibility(View.GONE);
            tv_to_saleafter.setVisibility(View.GONE);
        } else if (meOrderDetailBean.getOrderStatus() == OrderState.ORDER_STATUS_OUT_OF_WH) {//4 已出库（货在途）
            //tv_orderstatue.setText("待收货");
            img_state.setImageResource(R.drawable.peisongzhong);
            bottom_bar.setVisibility(View.VISIBLE);
            area_unpay.setVisibility(View.GONE);
            area_cfm_get.setVisibility(View.VISIBLE);
            tv_to_saleafter.setVisibility(View.VISIBLE);
        } else if (meOrderDetailBean.getOrderStatus() == OrderState.ORDER_STATUS_CUSTOM_RECEIVED) {//5 货物用户已收货
            //tv_orderstatue.setText("订单完成");
            img_state.setImageResource(R.drawable.jiaoyichenggong);
            bottom_bar.setVisibility(View.GONE);
            tv_to_saleafter.setVisibility(View.VISIBLE);
        } else if (meOrderDetailBean.getOrderStatus() == OrderState.ORDER_STATUS_ORDER_CANCELED) {//10 订单取消
            img_state.setImageResource(R.drawable.jiaoyiguanbi);
            bottom_bar.setVisibility(View.GONE);
            tv_to_saleafter.setVisibility(View.GONE);
        } else if (meOrderDetailBean.getOrderStatus() == OrderState.ORDER_STATUS_ORDER_FINISHED) {//11 订单完成
            img_state.setImageResource(R.drawable.jiaoyiguanbi);
            bottom_bar.setVisibility(View.GONE);
            tv_to_saleafter.setVisibility(View.VISIBLE);
        } else { //7 要求退货//8 退货中//9 退货完成
            img_state.setImageResource(R.drawable.jiaoyiguanbi);
            bottom_bar.setVisibility(View.GONE);
            tv_to_saleafter.setVisibility(View.GONE);
        }
        setDateToAddr();
    }

    /**
     * 联系地址数据
     */
    private void setDateToAddr() {
        tv_receiver_name.setText("联系人：" + meOrderDetailBean.getGoodReceiverName());
        tv_receiver_phone.setText(meOrderDetailBean.getGoodReceiverMobile());
        tv_address.setText(meOrderDetailBean.getGoodReceiverProvince()
                + meOrderDetailBean.getGoodReceiverCity()
                + meOrderDetailBean.getGoodReceiverCounty()
                + (TextUtils.isEmpty(meOrderDetailBean.getGoodReceiverTown()) ? "" : meOrderDetailBean.getGoodReceiverTown())
                + meOrderDetailBean.getGoodReceiverAddress());
    }


}
