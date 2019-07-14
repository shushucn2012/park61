package com.park61.moduel.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.park61.common.set.Log;
import com.park61.common.tool.FU;
import com.park61.moduel.grouppurchase.FightGroupDetailsActivity;
import com.park61.moduel.order.bean.GoodsInfoBean;
import com.park61.moduel.order.bean.OrderInfoBean;
import com.park61.moduel.pay.PayGoodsConfirmActivity;
import com.park61.moduel.salesafter.SalesAfterActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单按钮点击类
 */
public class OrderBtnClickListenter {
    /**
     * 拼团详情
     */
    public static class FightGroupDetailListener implements OnClickListener {
        OrderInfoBean order;
        Context context;

        public FightGroupDetailListener(Context context, OrderInfoBean order) {
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

        OrderInfoBean order;
        Context context;

        public PayOrderListener(Context context, OrderInfoBean order) {
            super();
            this.order = order;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent it = new Intent(context, PayGoodsConfirmActivity.class);
            it.putExtra("orderId", order.id);
            context.startActivity(it);
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
        OrderInfoBean order;
        String strType = "";
        Context context;

        public SalesAfterListener(Context context, OrderInfoBean order, String pType) {
            super();
            this.order = order;
            this.context = context;
            strType = pType;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, SalesAfterActivity.class);
            intent.putExtra("type", strType);
            float amount = (float) FU.sub(FU.paseDb(order.getOrderToPayAmount() + ""), FU.paseDb(order.getOrderDeliveryFee() + ""));
            intent.putExtra("amount", amount);
            if (strType.equals(TYPE_ORDER_DETAIL)) {
                int num = 0;
                long soId = 0L;
                if (order.getChildOrders() != null) {
                    for (int i = 0; i < order.getChildOrders().size(); i++) {
                        List<GoodsInfoBean> list = order.getChildOrders().get(i).getItems();
                        for (int j = 0; j < list.size(); j++) {
                            num += list.get(j).getOrderItemNum();
                        }
                        soId = order.getChildOrders().get(i).getId();
                    }
                } else {
                    soId = order.getId();
                    List<GoodsInfoBean> list = order.getItems();
                    for (int j = 0; j < list.size(); j++) {
                        num += list.get(j).getOrderItemNum();
                    }
                }
                intent.putExtra("orderItemNum", num);
                intent.putExtra("soId", soId);
            } else {
                intent.putExtra("orderItemNum", order.getOrderItemNum());// 订单列表
            }
            context.startActivity(intent);
        }

    }

    /***
     * 查看物流
     *
     * @author HP
     */
    public static class SeeOrderEMSListener implements OnClickListener {

        OrderInfoBean order;
        Context context;

        public SeeOrderEMSListener(Context context, OrderInfoBean order) {
            super();
            this.order = order;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent it = new Intent(this.context, LogisticsDetailActivty.class);
            it.putExtra("soId",order.getId());
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
        OrderInfoBean order;
        Context context;

        public OrderCommmentListener(Context context, OrderInfoBean order) {
            super();
            this.order = order;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent it = new Intent(context, OrderEvaluateActivity.class);
            long soId = 0L;
            if (order.getChildOrders() != null) {
                for (int i = 0; i < order.getChildOrders().size(); i++) {
                    soId = order.getChildOrders().get(i).getId();
                }
            } else {
                soId = order.getId();
            }
            it.putExtra("soId", soId);

            it.putExtra("orderInfo", order);
            if (order.items == null) {
                ArrayList<GoodsInfoBean> items = new ArrayList<>();
                GoodsInfoBean bean = new GoodsInfoBean();
                bean.setProductPicUrl("");
                if (order.picUrlList.size() > 0) {
                    bean.setProductPicUrl(order.picUrlList.get(0));
                }
                items.add(bean);
                order.setItems(items);
            }
            ((Activity) context).startActivityForResult(it, REQUEST_COMMIT);
        }

    }

}
