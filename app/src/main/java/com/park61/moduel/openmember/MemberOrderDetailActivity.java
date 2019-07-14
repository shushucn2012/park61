package com.park61.moduel.openmember;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.order.bean.OrderInfoBean;
import com.park61.moduel.order.bean.OrderState;
import com.park61.moduel.pay.PayGoodsConfirmActivity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.textview.TimeTextView;

import org.json.JSONObject;

/**
 * 会员订单详情
 */
public class MemberOrderDetailActivity extends BaseActivity {
    private TextView tv_orderstatue,tv_membertype_name,tv_card_price,tv_child_name,tv_card_money,
            tv_total_price,tv_coupon_money,tv_final_money;
    private Button btn_rebuy,btn_pay,btn_cancle;
    private TimeTextView ttv_time;
    private ImageView img_membertyp_logo;
    private OrderInfoBean order;
    private long orderId;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_member_orderdetail);
    }

    @Override
    public void initView() {
        tv_orderstatue = (TextView) findViewById(R.id.tv_orderstatue);
        tv_membertype_name = (TextView) findViewById(R.id.tv_membertype_name);
        tv_card_price = (TextView) findViewById(R.id.tv_card_price);
        tv_child_name = (TextView) findViewById(R.id.tv_child_name);
        tv_card_money = (TextView) findViewById(R.id.tv_card_money);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_coupon_money = (TextView) findViewById(R.id.tv_coupon_money);
        tv_final_money = (TextView) findViewById(R.id.tv_final_money);
        ttv_time = (TimeTextView) findViewById(R.id.ttv_time);
        img_membertyp_logo = (ImageView) findViewById(R.id.img_membertyp_logo);
        btn_rebuy = (Button) findViewById(R.id.btn_rebuy);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
    }

    @Override
    public void initData() {
        orderId = this.getIntent().getLongExtra("orderid", 0);
        if (orderId == 0) {
            showShortToast("获取订单号异常");
            this.finish();
            return;
        }
        asyncGetOrderDetail();
    }

    @Override
    public void initListener() {
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.showDialog("提示", "确定要取消该订单吗？", "确定", "取消", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                        showDialog();
                        asyncCancelOrder(order);
                    }
                }, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                    }
                });
            }
        });
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, PayGoodsConfirmActivity.class);
                it.putExtra("orderId", order.id);
                startActivity(it);
            }
        });
        btn_rebuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mContext, OpenMemberActivity.class);
                mContext.startActivity(intent);
            }
        });
    }
    /**
     * 取消
     */
    private void asyncCancelOrder(OrderInfoBean order) {
        String wholeUrl = AppUrl.host + AppUrl.ORDER_CANCEL;
        String requestBodyData = ParamBuild.getOrderDetail(order.id);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new CancelOrderRequestListener(order));
    }

    class CancelOrderRequestListener implements JsonRequestListener {

        OrderInfoBean order;

        public CancelOrderRequestListener(OrderInfoBean order) {
            super();
            this.order = order;
        }

        @Override
        public void onStart(int requestId) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast("取消订单失败!");

        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            showShortToast("取消订单成功");
            dismissDialog();
            order.orderStatus = OrderState.ORDER_STATUS_ORDER_CANCELED;
            GlobalParam.MyOrderNeedRefresh = true;
            finish();
        }

    }

    /**
     * 请求订单详情
     */
    private void asyncGetOrderDetail() {
        String wholeUrl =   AppUrl.host + AppUrl.ORDER_DETAIL_V2;
        String requestBodyData =  ParamBuild.getOrderDetail(orderId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,
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
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            order = gson.fromJson(jsonResult.toString(),
                    OrderInfoBean.class);
            setDataToView(order);
        }
    };
    public void setDataToView(OrderInfoBean order) {
//        tv_orderstatue.setText(getResources().getStringArray(
//                R.array.order_statue_info)[order.orderStatus]);
        tv_coupon_money.setText("-￥" + order.getCouponAmount() + "");
        tv_card_price.setText("￥" + order.getProductAmount() + "");
        tv_card_money.setText("￥" + order.getProductAmount() + "");
        tv_total_price.setText("￥" + order.getProductAmount() + "");
        tv_final_money.setText("￥" + order.getOrderToPayAmount() + "");
        ImageManager.getInstance().displayImg(img_membertyp_logo, order.getItems().get(0).getProductPicUrl());
        tv_membertype_name.setText(order.getItems().get(0).getProductCname());

        if(order.getOrderStatus()==1){
            tv_orderstatue.setText("等待买家付款");
            btn_cancle.setVisibility(View.VISIBLE);
            btn_pay.setVisibility(View.VISIBLE);
            btn_rebuy.setVisibility(View.GONE);
        }else if(order.getOrderStatus()==2){
            tv_orderstatue.setText("交易完成，下次再来哦！");
            btn_cancle.setVisibility(View.GONE);
            btn_pay.setVisibility(View.GONE);
            btn_rebuy.setVisibility(View.VISIBLE);
        }else if(order.getOrderStatus()==10){
            tv_orderstatue.setText("订单取消");
            btn_cancle.setVisibility(View.GONE);
            btn_pay.setVisibility(View.GONE);
            btn_rebuy.setVisibility(View.GONE);
        }else{
            btn_cancle.setVisibility(View.GONE);
            btn_pay.setVisibility(View.GONE);
            btn_rebuy.setVisibility(View.GONE);
        }
    }
}
