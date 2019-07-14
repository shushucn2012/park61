package com.park61.moduel.order;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.FU;
import com.park61.moduel.grouppurchase.FightGroupDetailsActivity;
import com.park61.moduel.order.OrderButtonClickListenter.PayOrderListener;
import com.park61.moduel.order.adapter.OrderDetailAdapter;
import com.park61.moduel.order.bean.MerchantOrderBean;
import com.park61.moduel.order.bean.OrderDetailBean;
import com.park61.moduel.order.bean.OrderState;
import com.park61.moduel.salesafter.AuditStatuActivity;
import com.park61.moduel.salesafter.MerchantSignActivity;
import com.park61.moduel.salesafter.RefundFinishActivity;
import com.park61.moduel.salesafter.SalesClosedActivity;
import com.park61.moduel.salesafter.WaitForMerchanSignActivity;
import com.park61.moduel.salesafter.WaitForSendGoodsActivity;
import com.park61.moduel.salesafter.bean.ApplySalesAfterInfo;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.textview.TimeTextView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 商品订单详情页面
 */
public class TradeOrderDetailActivity extends BaseActivity implements OrderDetailAdapter.BtnClickListener {
    private ListView orderdetail_listview;
    long orderId;
    private TextView tv_empty_tip;
    private View lay_ems_statue;
    public TimeTextView ttv_time;
    private TextView tv_paytime;
    private TextView tv_amount_value, tv_real_amount_value, tv_order_number,
            tv_order_time, tv_orderstatue, tv_coupon_value, tv_ems_value;
    private TextView tv_receiver_value, tv_receiver_phone, tv_address, discount_amount_value;
    private Button btn_1, btn_2;
    private View bottom_bar;
    private ImageView img;
    private ArrayList<MerchantOrderBean> mList;
    private OrderDetailAdapter mAdapter;
    private OrderDetailBean order;
    private boolean isReturnFlag;//是否能退款

    private int grfStatus,auditResults,waybillType,isReturnGoods,returnGoodsNum;
    private long grfApplyTime,refundWay;
    private String grfCode,remark,grfReason,storePhone,storeAddress,deliveryCompanyName,waybillCode;
    private float grfAmount;
    private ArrayList<String> grfPicUrls;
    private long pmInfoId;
    private long grfId;//退货id
    private long updateTime;//撤销售后申请时间
    private String closeReason;//售后关闭原因;


    @Override
    public void setLayout() {
        setContentView(R.layout.activity_trade_orderdetail);

    }

    @Override
    public void initView() {
        orderdetail_listview = (ListView) findViewById(R.id.orderdetail_listview);
        tv_empty_tip = (TextView) this.findViewById(R.id.tv_empty_tip);
        tv_empty_tip.setText("获取数据失败");
        tv_empty_tip.setVisibility(View.GONE);
        bottom_bar = findViewById(R.id.bottom_bar);

        tv_amount_value = (TextView) findViewById(R.id.tv_amount_value);
        tv_real_amount_value = (TextView) findViewById(R.id.tv_real_amount_value);
        tv_order_number = (TextView) findViewById(R.id.tv_order_number);
        tv_order_time = (TextView) findViewById(R.id.tv_order_time);
        tv_coupon_value = (TextView) findViewById(R.id.tv_coupon_value);
        tv_ems_value = (TextView) findViewById(R.id.tv_ems_value);
        discount_amount_value = (TextView) findViewById(R.id.discount_amount_value);

        tv_receiver_value = (TextView) findViewById(R.id.tv_receiver_value);
        tv_receiver_phone = (TextView) findViewById(R.id.tv_receiver_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_orderstatue = (TextView) findViewById(R.id.tv_orderstatue);

        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        lay_ems_statue = findViewById(R.id.lay_ems_statue);
        lay_ems_statue.setVisibility(View.GONE);
        img = (ImageView) findViewById(R.id.img);
    }

    @Override
    public void initData() {
        mList = new ArrayList<MerchantOrderBean>();
        mAdapter = new OrderDetailAdapter((BaseActivity) mContext, mList);
        mAdapter.setBtnClickListener(this);
        orderdetail_listview.setAdapter(mAdapter);

        orderId = this.getIntent().getLongExtra("orderid", 0);
        if (orderId == 0) {
            showShortToast("获取订单号异常");
            this.finish();
            return;
        }
        asyncGetOrderDetail();
    }

    @Override
    protected void onResume() {
        if (GlobalParam.TradeOrderDetailNeedRefresh) {
            asyncGetOrderDetail();
            GlobalParam.TradeOrderDetailNeedRefresh = false;
        }
        super.onResume();
    }

    /**
     * 请求订单详情
     */
    private void asyncGetOrderDetail() {
        tv_empty_tip.setVisibility(View.GONE);
        String wholeUrl = AppUrl.host + AppUrl.ORDER_MERCHANT_DETAIL;
        String requestBodyData = ParamBuild.getOrderDetail(orderId);
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
                    OrderDetailBean.class);
            mList.clear();
            for (int i = 0; i < order.getMerchantOrder().size(); i++) {
                MerchantOrderBean item = order.getMerchantOrder().get(i);
                mList.add(item);
            }
            mAdapter.notifyDataSetChanged();
            setDataToView(order);
        }
    };

    public void setDataToView(OrderDetailBean order) {
        tv_amount_value.setText(FU.formatPrice(order.getProductAmount()));
        tv_real_amount_value.setText(FU.formatPrice(order.getOrderToPayAmount()));
        tv_order_number.setText("订单编号：" + order.getId() + "");
        tv_order_time.setText("下单时间：" + DateTool.L2S(order.getOrderCreateTime()));
        if (TextUtils.isEmpty(order.getOrderCancelReason())) {//订单取消原因为空
            tv_orderstatue.setText(order.getOrderStatusName());
        } else {
            tv_orderstatue.setText(order.getOrderCancelReason());
        }
        //order_statue_info订单状态信息：1 等待买家付款，2 买家已付款，3 等待商家发货，4 商品正在配送中，5 商品已签收
        //6 商品送货失败, 7 申请退货中,8 商品正在退货中,9 退货完成,10 订单已取消,11 交易完成

        tv_coupon_value.setText("-" + FU.formatPrice(order.getCouponAmount()));
        tv_ems_value.setText("+" + FU.formatPrice(order.getOrderDeliveryFee()));
        discount_amount_value.setText("-" + FU.formatPrice(order.getDiscountAmount()));
        isReturnFlag = order.isReturnFlag();
        if (order.getOrderStatus() == OrderState.ORDER_STATUS_ORDERED_WAITING_FOR_PAYMENT) {//1 已下单:货款未全收
            img.setImageResource(R.drawable.dengdaifukuan);
            bottom_bar.setVisibility(View.VISIBLE);
            btn_1.setVisibility(View.VISIBLE);
            btn_2.setVisibility(View.VISIBLE);
            btn_1.setText("取消订单");
            btn_1.setOnClickListener(new CancelOrderListener(order));
            btn_2.setText("继续支付");
            btn_2.setOnClickListener(new PayOrderListener(this.mContext, order));
        } else if (order.getOrderStatus() == OrderState.ORDER_STATUS_ORDERED_PAYED) {//2 已下单:货款已全收
            img.setImageResource(R.drawable.dengdaichuli);
            if (order.getOrderType() == 2) {
                bottom_bar.setVisibility(View.VISIBLE);
                btn_1.setVisibility(View.VISIBLE);
                btn_2.setVisibility(View.VISIBLE);
                btn_1.setText("邀请好友");
                btn_2.setText("拼团详情");
                btn_1.setOnClickListener(new FightGroupDetailListener(order));
                btn_2.setOnClickListener(new FightGroupDetailListener(order));
            } else {
                bottom_bar.setVisibility(View.VISIBLE);
                btn_1.setVisibility(View.GONE);
                btn_2.setVisibility(View.VISIBLE);
                btn_2.setText("取消订单");
                btn_2.setOnClickListener(new CancelOrderListener(order));
            }
        } else if (order.getOrderStatus() == OrderState.ORDER_STATUS_TRUNED_TO_DO) {//3 已转DO
            img.setImageResource(R.drawable.beihuozhong);
            if (order.getOrderType() == 2) {
                bottom_bar.setVisibility(View.VISIBLE);
                btn_1.setVisibility(View.GONE);
                btn_2.setVisibility(View.VISIBLE);
                btn_2.setText("拼团详情");
                btn_2.setOnClickListener(new FightGroupDetailListener(order));
            } else {
                bottom_bar.setVisibility(View.GONE);
            }
        } else if (order.getOrderStatus() == OrderState.ORDER_STATUS_OUT_OF_WH) {//4 已出库（货在途）
            img.setImageResource(R.drawable.peisongzhong);
            bottom_bar.setVisibility(View.GONE);
        } else if (order.getOrderStatus() == OrderState.ORDER_STATUS_CUSTOM_RECEIVED) {//5 货物用户已收货
            img.setImageResource(R.drawable.jiaoyichenggong);
            bottom_bar.setVisibility(View.GONE);
        } else if (order.getOrderStatus() == OrderState.ORDER_STATUS_TO_RETURN ||
                order.getOrderStatus() == OrderState.ORDER_STATUS_RETURNING) {//7 要求退货//8 退货中
            img.setImageResource(R.drawable.shouhouchuli);
            bottom_bar.setVisibility(View.GONE);
        } else if (order.getOrderStatus() == OrderState.ORDER_STATUS_RETURNED) {//9 退货完成
            img.setImageResource(R.drawable.tuikuanchenggong);
            if (order.getOrderType() == 2) {//拼团
                bottom_bar.setVisibility(View.VISIBLE);
                btn_1.setVisibility(View.VISIBLE);
                btn_2.setVisibility(View.VISIBLE);
                btn_1.setText("返回首页");
                btn_2.setText("拼团详情");
                btn_1.setOnClickListener(new HomePagerListener(order));
                btn_2.setOnClickListener(new FightGroupDetailListener(order));
            } else {
                bottom_bar.setVisibility(View.GONE);
            }
        } else if (order.getOrderStatus() == OrderState.ORDER_STATUS_ORDER_CANCELED) {//10 订单取消
            img.setImageResource(R.drawable.jiaoyiguanbi);
            if (order.getFightGroupOpenId() != null || order.getOrderType() == 2) {//拼团
                bottom_bar.setVisibility(View.VISIBLE);
                btn_1.setVisibility(View.VISIBLE);
                btn_2.setVisibility(View.VISIBLE);
                btn_1.setText("返回首页");
                btn_2.setText("拼团详情");
                btn_1.setOnClickListener(new HomePagerListener(order));
                btn_2.setOnClickListener(new FightGroupDetailListener(order));
            } else {
                bottom_bar.setVisibility(View.GONE);
            }
        } else if (order.getOrderStatus() == OrderState.ORDER_STATUS_ORDER_FINISHED) {//11 订单完成
            img.setImageResource(R.drawable.jiaoyiguanbi);
            bottom_bar.setVisibility(View.GONE);
        } else {
            bottom_bar.setVisibility(View.GONE);
        }
        setDateToAddr();
    }

    /**
     * 联系地址数据
     */
    private void setDateToAddr() {
        tv_receiver_value.setText(order.getGoodReceiverName());
        tv_receiver_phone.setText(order.getGoodReceiverMobile());
        if (order.getGoodReceiverTown() != null) {//乡镇名不为空
            tv_address.setText("地址：" + order.getGoodReceiverProvince()
                    + order.getGoodReceiverCity() + order.getGoodReceiverCounty()
                    + order.getGoodReceiverTown() + order.getGoodReceiverAddress());
        } else {
            tv_address.setText("地址：" + order.getGoodReceiverProvince()
                    + order.getGoodReceiverCity() + order.getGoodReceiverCounty()
                    + order.getGoodReceiverAddress());
        }
    }

    @Override
    public void initListener() {
        // TODO Auto-generated method stub

    }

    @Override
    public void getGoodsList(int position) {
        Intent it = new Intent(mContext, SalesGoodsListActivity.class);
        it.putParcelableArrayListExtra("orderList", (ArrayList) (mList.get(position).getChildOrders()));
        it.putExtra("packageNum", mList.get(position).getPackageNum());
        it.putExtra("orderItemNum", mList.get(position).getOrderItemNum());
        it.putExtra("soId", order.getId());
        mContext.startActivity(it);
    }

    @Override
    public void afterSaleDetail(int position) {
        grfId = mList.get(position).getId();
        pmInfoId = mList.get(position).getChildOrders().get(0).getItems().get(0).getPmInfoId();
        asyncQueryReturnApply();
    }

    /**
     * 返回首页
     */
    class HomePagerListener implements View.OnClickListener {
        OrderDetailBean order;

        public HomePagerListener(OrderDetailBean order) {
            super();
            this.order = order;
        }

        @Override
        public void onClick(View view) {
            backToHome();
        }
    }
    /**
     * 删除订单
     */
    class DeleteOrderListener implements OnClickListener{
        OrderDetailBean order;
        public DeleteOrderListener(OrderDetailBean order){
            super();
            this.order = order;
        }
        @Override
        public void onClick(View view) {
            showDeleteOrderDialog(order);
        }
    }
    public void showDeleteOrderDialog(final OrderDetailBean order){
        dDialog.showDialog("提示", "确定要删除该订单吗？", "确定", "取消", new OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.dismissDialog();
                showDialog();


            }
        }, new OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.dismissDialog();
            }
        });
    }

    /**
     * 取消订单
     */
    class CancelOrderListener implements View.OnClickListener {

        OrderDetailBean order;

        public CancelOrderListener(OrderDetailBean order) {
            super();
            this.order = order;
        }

        @Override
        public void onClick(View v) {
            showCancelOrderDialog(order);
        }
    }

    public void showCancelOrderDialog(final OrderDetailBean order) {
        dDialog.showDialog("提示", "确定要取消该订单吗？", "确定", "取消",
                new View.OnClickListener() {

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

    /**
     * 取消
     */
    private void asyncCancelOrder(OrderDetailBean order) {
        String wholeUrl = AppUrl.host + AppUrl.ORDER_CANCEL;
        String requestBodyData = ParamBuild.getOrderDetail(order.getId());
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,
                new CancelOrderRequestListener(order));
    }

    class CancelOrderRequestListener implements JsonRequestListener {

        OrderDetailBean order;
        int orderStatus;

        public CancelOrderRequestListener(OrderDetailBean order) {
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
            orderStatus = order.getOrderStatus();
            orderStatus = OrderState.ORDER_STATUS_ORDER_CANCELED;//10 订单取消
            GlobalParam.MyOrderNeedRefresh = true;
            finish();
        }
    }

    /**
     * 拼团详情
     */
    public class FightGroupDetailListener implements View.OnClickListener {
        OrderDetailBean order;

        public FightGroupDetailListener(OrderDetailBean order) {
            super();
            this.order = order;
        }

        @Override
        public void onClick(View v) {
            Intent it = new Intent(mContext, FightGroupDetailsActivity.class);
            it.putExtra("opneId", order.getFightGroupOpenId());
            it.putExtra("soId", order.getId());
            startActivity(it);
        }
    }
    /**
     * 退货申请查询
     */
    private void asyncQueryReturnApply() {
        String wholeUrl = AppUrl.host + AppUrl.RETURN_QUERY;
//        String requestBodyData = ParamBuild.queryById(grfId);
        String requestBodyData = ParamBuild.queryByIdV2(grfId,pmInfoId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, detailListener);
    }
    BaseRequestListener detailListener = new JsonRequestListener() {
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
            ApplySalesAfterInfo item = gson.fromJson(jsonResult.toString(), ApplySalesAfterInfo.class);// 把Json数据解析成对象
            grfStatus = item.getGrfStatus();
            auditResults = item.getAuditResults();
            waybillType = item.getWaybillType();
            isReturnGoods = item.getIsReturnGoods();

            grfApplyTime = item.getGrfApplyTime();
            grfCode = item.getGrfCode();
            remark = item.getRemark();
            returnGoodsNum = item.getReturnGoodsNum();
            grfAmount = item.getGrfAmount();
            grfReason = item.getGrfReason();
            storePhone = item.getStorePhone();
            storeAddress = item.getStoreAddress();

            deliveryCompanyName = item.getDeliveryCompanyName();
            waybillCode = item.getWaybillCode();
            updateTime = item.getUpdateTime();
            closeReason = item.getCloseReason();

            refundWay = item.getRefundWay();
            grfPicUrls = new ArrayList<String>();
            for (int i = 0; i < item.getPics().size(); i++) {
                String givebackPicUrl = item.getPics().get(i).getGivebackPicUrl();
                grfPicUrls.add(givebackPicUrl);
            }
            // 退款状态: 0、申请退货，1、退货审核，2、提交货品，3、货品入库，4、系统打款，5、订单关闭
            if (grfStatus == 0) {
                Intent intent1 = new Intent(mContext, AuditStatuActivity.class);
                transmitData(intent1);
                mContext.startActivity(intent1);
            } else if (grfStatus == 1) {
                Intent intent2 = new Intent(mContext, WaitForSendGoodsActivity.class);
                transmitData(intent2);
                intent2.putExtra("grfId",item.getItems().get(0).getGrfId());
                intent2.putExtra("pmInfoId",pmInfoId);
                intent2.putExtra("soId", item.getSoId());
                intent2.putExtra("auditResults", auditResults);
                intent2.putExtra("waybillType", waybillType);
                intent2.putExtra("storePhone", storePhone);
                intent2.putExtra("storeAddress", storeAddress);
                mContext.startActivity(intent2);
            } else if (grfStatus == 2) {
                Intent intent3 = new Intent(mContext, WaitForMerchanSignActivity.class);
                transmitData(intent3);
                intent3.putExtra("isReturnGoods", isReturnGoods);
                intent3.putExtra("deliveryCompanyName", deliveryCompanyName);
                intent3.putExtra("waybillCode", waybillCode);

                intent3.putExtra("waybillType", waybillType);
                intent3.putExtra("storePhone", storePhone);
                intent3.putExtra("storeAddress", storeAddress);
                mContext.startActivity(intent3);
            } else if (grfStatus == 3) {
                Intent intent4 = new Intent(mContext, MerchantSignActivity.class);
                transmitData(intent4);
                intent4.putExtra("deliveryCompanyName", deliveryCompanyName);
                intent4.putExtra("waybillCode", waybillCode);
                intent4.putExtra("waybillType", waybillType);
                intent4.putExtra("storePhone", storePhone);
                intent4.putExtra("storeAddress", storeAddress);
                mContext.startActivity(intent4);
            } else if (grfStatus == 4) {
                Intent intent5 = new Intent(mContext, RefundFinishActivity.class);
                intent5.putExtra("grfAmount", grfAmount);
                intent5.putExtra("refundWay", refundWay);
                mContext.startActivity(intent5);
            } else if (grfStatus == 5) {
                Intent intent = new Intent(mContext, SalesClosedActivity.class);
                transmitData(intent);
                intent.putExtra("updateTime", updateTime);
                intent.putExtra("closeReason", closeReason);

                intent.putExtra("isReturnGoods", isReturnGoods);
                intent.putExtra("deliveryCompanyName", deliveryCompanyName);
                intent.putExtra("waybillCode", waybillCode);
                mContext.startActivity(intent);
            }
        }
    };
    private void transmitData(Intent intent){
        intent.putExtra("grfApplyTime", grfApplyTime);
        intent.putExtra("grfCode", grfCode);
        intent.putExtra("remark", remark);
        intent.putExtra("returnGoodsNum", returnGoodsNum);
        intent.putExtra("grfAmount", grfAmount);
        intent.putExtra("grfReason", grfReason);
        intent.putExtra("grfPicUrls", grfPicUrls);
    }

}
