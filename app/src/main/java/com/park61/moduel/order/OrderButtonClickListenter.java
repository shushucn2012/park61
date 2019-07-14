package com.park61.moduel.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.park61.BaseActivity;
import com.park61.common.set.Log;
import com.park61.moduel.grouppurchase.FightGroupDetailsActivity;
import com.park61.moduel.order.bean.GoodsInfoBean;
import com.park61.moduel.order.bean.MerchantOrderBean;
import com.park61.moduel.order.bean.OrderDetailBean;
import com.park61.moduel.order.bean.OrderBean;
import com.park61.moduel.pay.PayGoodsConfirmActivity;
import com.park61.moduel.salesafter.SalesAfterActivity;

import java.util.ArrayList;

/**
 * 订单按钮点击类
 */
public class OrderButtonClickListenter {
    /**
     * 拼团详情
     */
    public static class FightGroupDetailListener implements OnClickListener {
        OrderDetailBean order;
        Context context;

        public FightGroupDetailListener(Context context, OrderDetailBean order) {
            super();
            this.order = order;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent it = new Intent(context, FightGroupDetailsActivity.class);
            it.putExtra("opneId", order.getFightGroupOpenId());
            it.putExtra("soId", order.getId());
            Log.i("====OrderBtnClickListenter====", order.getFightGroupOpenId() + "");
            context.startActivity(it);
        }
    }

    /***
     * 支付订单
     *
     * @author HP
     */
    public static class PayOrderListener implements OnClickListener {

        OrderDetailBean order;
        Context context;

        public PayOrderListener(Context context, OrderDetailBean order) {
            super();
            this.order = order;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent it = new Intent(context, PayGoodsConfirmActivity.class);
            it.putExtra("orderId", order.getId());
            context.startActivity(it);
            ((BaseActivity)context).finish();
        }

    }

    /***
     * 申请售后
     *
     * @author HP
     */
    public static class SalesAfterListener implements OnClickListener {

        public final static String TYPE_ORDER_LIST = "type_order_list";
        public final static String TYPE_ORDER_DETAIL = "type_order_detail";
        MerchantOrderBean order;
        String strType = "";
        Context context;

        public SalesAfterListener(Context context, MerchantOrderBean order, String pType) {
            super();
            this.order = order;
            this.context = context;
            strType = pType;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, SalesAfterActivity.class);
            intent.putExtra("type", strType);
            intent.putExtra("amount", order.getChildOrders().get(0).getReturnAmount());
            intent.putExtra("orderItemNum", order.getOrderItemNum());
            intent.putExtra("soId", order.getId());
            context.startActivity(intent);
        }

    }

    /***
     * 查看物流
     *
     * @author HP
     */
    public static class SeeOrderEMSListener implements OnClickListener {

        MerchantOrderBean order;
        Context context;

        public SeeOrderEMSListener(Context context, MerchantOrderBean order) {
            super();
            this.order = order;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent it = new Intent(this.context, LogisticsDetailActivty.class);
            it.putExtra("soId", order.getId());
            context.startActivity(it);
        }

    }

    /***
     * 评价订单
     *
     * @author HP
     */
    public static class OrderCommmentListener implements OnClickListener {
        public static final int REQUEST_COMMIT = 2;
        MerchantOrderBean order;
        Context context;

        public OrderCommmentListener(Context context, MerchantOrderBean order) {
            super();
            this.order = order;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent it = new Intent(context, TradeOrderEvaluateActivity.class);
            long soId = 0L;
            ArrayList<GoodsInfoBean> items = new ArrayList<>();
            it.putExtra("soId", order.getId());
            OrderBean item = new OrderBean();
            for(int i =0;i<order.getChildOrders().size();i++){
                item = order.getChildOrders().get(i);
                for(int j =0;j<item.getItems().size();j++){
                    GoodsInfoBean goodsItem = item.getItems().get(j);
                    items.add(goodsItem);
                }
                item.setItems(items);
            }
            it.putExtra("orderInfo", item);
            ((Activity) context).startActivityForResult(it, REQUEST_COMMIT);
        }

    }
}
