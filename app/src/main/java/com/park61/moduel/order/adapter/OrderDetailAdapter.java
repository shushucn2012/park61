package com.park61.moduel.order.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.order.OrderButtonClickListenter.OrderCommmentListener;
import com.park61.moduel.order.OrderButtonClickListenter.SeeOrderEMSListener;
import com.park61.moduel.order.OrderDealSuccessActivity;
import com.park61.moduel.order.SalesGoodsListActivity;
import com.park61.moduel.order.bean.GoodsInfoBean;
import com.park61.moduel.order.bean.MerchantOrderBean;
import com.park61.moduel.order.bean.OrderBean;
import com.park61.moduel.order.bean.OrderState;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.salesafter.SalesAfterActivity;
import com.park61.net.base.Request;
import com.park61.net.request.StringNetRequest;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 商品订单详情adapter
 */
public class OrderDetailAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private BaseActivity mContext;
    private ArrayList<MerchantOrderBean> mList;
    public StringNetRequest netRequest;
    private BtnClickListener mBtnClickListener;

    public OrderDetailAdapter(BaseActivity _context, ArrayList<MerchantOrderBean> _list) {
        this.mContext = _context;
        this.mList = _list;
        layoutInflater = LayoutInflater.from(mContext);
        netRequest = StringNetRequest.getInstance(_context);
        netRequest.setMainContext(_context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.order_detail_list_item, null);
            holder.tv_merchant_name = (TextView) convertView.findViewById(R.id.tv_merchant_name);
            holder.tv_expense_amount = (TextView) convertView.findViewById(R.id.tv_expense_amount);
            holder.tv_total_amount = (TextView) convertView.findViewById(R.id.tv_total_amount);
            holder.tv_product_amount = (TextView) convertView.findViewById(R.id.tv_product_amount);
            holder.btn1 = (Button) convertView.findViewById(R.id.btn1);
            holder.btn2 = (Button) convertView.findViewById(R.id.btn2);
            holder.btn3 = (Button) convertView.findViewById(R.id.btn3);
            holder.btn_area = convertView.findViewById(R.id.btn_area);

            holder.bags_area = convertView.findViewById(R.id.bags_area);
            holder.img_icon1 = (ImageView) convertView.findViewById(R.id.img_icon1);
            holder.img_icon2 = (ImageView) convertView.findViewById(R.id.img_icon2);
            holder.img_icon3 = (ImageView) convertView.findViewById(R.id.img_icon3);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_dots = (TextView) convertView.findViewById(R.id.tv_dots);

            holder.one_goods_area = convertView.findViewById(R.id.one_goods_area);
            holder.cur_price = (TextView) convertView.findViewById(R.id.cur_price);
            holder.src_price = (TextView) convertView.findViewById(R.id.src_price);
            holder.goods_num = (TextView) convertView.findViewById(R.id.goods_num);
            holder.goods_title = (TextView) convertView.findViewById(R.id.goods_title);
            holder.tv_color = (TextView) convertView.findViewById(R.id.tv_color);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            holder.img_goods = (ImageView) convertView.findViewById(R.id.img_goods);

            holder.expense_area = convertView.findViewById(R.id.expense_area);
            holder.total_area = convertView.findViewById(R.id.total_area);
            holder.trade_total_area = convertView.findViewById(R.id.trade_total_area);
            holder.remark_area = convertView.findViewById(R.id.remark_area);
            holder.tv_order_remark = (TextView) convertView.findViewById(R.id.tv_order_remark);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MerchantOrderBean order = mList.get(position);
        holder.tv_merchant_name.setText(order.getMerchantName());
        if(mList.size()==1){//只有一个商家，小计和运费不显示
            holder.expense_area.setVisibility(View.GONE);
            holder.total_area.setVisibility(View.GONE);
            holder.trade_total_area.setVisibility(View.GONE);
        }else{
            holder.expense_area.setVisibility(View.VISIBLE);
            holder.total_area.setVisibility(View.VISIBLE);
            holder.trade_total_area.setVisibility(View.VISIBLE);
            holder.tv_total_amount.setText(FU.formatBigPrice(order.getOrderAmount()));
            holder.tv_expense_amount.setText(FU.formatBigPrice(order.getOrderDeliveryFee()));
            holder.tv_product_amount.setText(FU.formatBigPrice(order.getProductAmount()));
        }
        int orderStatus = 0;//订单状态：1 已下单:货款未全收, 2 已下单:货款已全收,3 已转DO,4 已出库（货在途）
        //5 货物用户已收货,6 送货失败（其它）,7 要求退货,8 退货中 ,9 退货完成 ,10 订单取消,11 订单完成
        int isEvaluated = 0;//评价：0 未评价，1 已评价
        orderStatus = order.orderStatus;
        isEvaluated = order.isEvaluated;
        OrderBean orderBean=null;
        for(int i =0;i<order.getChildOrders().size();i++){
            orderBean = order.getChildOrders().get(i);
        }
        if(orderStatus == OrderState.ORDER_STATUS_TRUNED_TO_DO){//3 已转DO
            if(order.getPackageNum()==1){
                holder.btn_area.setVisibility(View.VISIBLE);
                holder.btn1.setVisibility(View.GONE);
                holder.btn3.setVisibility(View.GONE);
                holder.btn2.setVisibility(View.VISIBLE);
                holder.btn2.setText("查看物流");
                holder.btn2.setOnClickListener(new SeeOrderEMSListener(
                        this.mContext, order));
            }else{
                holder.btn_area.setVisibility(View.GONE);
            }
        }else if (orderStatus == OrderState.ORDER_STATUS_OUT_OF_WH) {//4 已出库（货在途）
            if(order.getPackageNum()==1){
                holder.btn_area.setVisibility(View.VISIBLE);
                holder.btn3.setVisibility(View.GONE);
                holder.btn1.setVisibility(View.VISIBLE);
                holder.btn2.setVisibility(View.VISIBLE);
                holder.btn2.setText("查看物流");
                holder.btn2.setOnClickListener(new SeeOrderEMSListener(
                        this.mContext, order));
                holder.btn1.setText("确认收货");
                holder.btn1.setOnClickListener(new OrderReceiveListener(order));
            }else{
                holder.btn_area.setVisibility(View.GONE);
            }

        } else if (orderStatus == OrderState.ORDER_STATUS_CUSTOM_RECEIVED) {//5 货物用户已收货
            holder.btn_area.setVisibility(View.VISIBLE);
            int count = 0;
            for(int j =0;j<orderBean.getItems().size();j++){
                int isGrfGoods = orderBean.getItems().get(j).getIsGrfGoods();
                if(isGrfGoods==1){
                    count++;
                }
            }
            if(count==orderBean.getItems().size()){
                holder.btn1.setText("售后详情");
            }else{
                holder.btn1.setText("申请售后");
            }
            if(order.getPackageNum()==1){
                holder.btn1.setVisibility(View.VISIBLE);
                if(orderBean.getItems().size()>1){
                    holder.btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent it = new Intent(mContext, SalesGoodsListActivity.class);
                            it.putParcelableArrayListExtra("orderList",(ArrayList)(mList.get(position).getChildOrders()));
                            it.putExtra("packageNum",mList.get(position).getPackageNum());
                            it.putExtra("orderItemNum",mList.get(position).getOrderItemNum());
                            it.putExtra("soId",order.getId());
                            mContext.startActivity(it);
                        }
                    });
                }else{
                    if(count==orderBean.getItems().size()){
                        holder.btn1.setText("售后详情");
                        holder.btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mBtnClickListener.afterSaleDetail(position);
                            }
                        });
                    }else{
                        holder.btn1.setText("申请售后");
                        holder.btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                OrderBean orderBean= mList.get(position).getChildOrders().get(0);
                                Intent it = new Intent(mContext, SalesAfterActivity.class);
                                it.putExtra("soId", mList.get(position).getId());
                                it.putExtra("pmInfoId", orderBean.getItems().get(0).getPmInfoId());
                                it.putExtra("orderItemNum", orderBean.getItems().get(0).getOrderItemNum());
                                mContext.startActivity(it);
                            }
                        });
                    }
                }
                holder.btn2.setVisibility(View.GONE);
                holder.btn2.setText("查看物流");
                holder.btn2.setOnClickListener(new SeeOrderEMSListener(
                        this.mContext, order));
            }else{
                holder.btn1.setVisibility(View.GONE);
                holder.btn2.setVisibility(View.GONE);
            }
            if (isEvaluated == 1) {
                holder.btn3.setVisibility(View.GONE);
            } else {
                holder.btn3.setVisibility(View.VISIBLE);
                holder.btn3.setText("立即评价");
                holder.btn3.setOnClickListener(new OrderCommmentListener(
                        this.mContext, order));
            }

        } else if (orderStatus == OrderState.ORDER_STATUS_TO_RETURN ||
                orderStatus == OrderState.ORDER_STATUS_RETURNING || orderStatus == OrderState.ORDER_STATUS_RETURNED) {
            //7 要求退货,8 退货中,9 退货完成
            if(order.getPackageNum()==1){
                holder.btn_area.setVisibility(View.VISIBLE);
                holder.btn2.setVisibility(View.GONE);
                holder.btn3.setVisibility(View.GONE);
                holder.btn1.setVisibility(View.VISIBLE);
                holder.btn1.setText("售后详情");
                if(orderBean.getItems().size()>1){
                    holder.btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent it = new Intent(mContext, SalesGoodsListActivity.class);
                            it.putParcelableArrayListExtra("orderList",(ArrayList)(mList.get(position).getChildOrders()));
                            it.putExtra("packageNum",mList.get(position).getPackageNum());
                            it.putExtra("orderItemNum",mList.get(position).getOrderItemNum());
                            it.putExtra("soId",order.getId());
                            mContext.startActivity(it);
                        }
                    });
                }else{
                    holder.btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mBtnClickListener.afterSaleDetail(position);
                        }
                    });
                }
            }else{
                holder.btn_area.setVisibility(View.GONE);
            }
        } else {
            holder.btn_area.setVisibility(View.GONE);
        }
        if(TextUtils.isEmpty(order.getDeliveryRemark())){
            holder.remark_area.setVisibility(View.GONE);
        }else{
            holder.remark_area.setVisibility(View.VISIBLE);
            holder.tv_order_remark.setText(order.getDeliveryRemark());
        }
        for(int i = 0; i < order.getChildOrders().size(); i++){
            OrderBean item = order.getChildOrders().get(i);
            if(order.getPicUrlList().size()==1&&item.getItems().size()==1){
                holder.one_goods_area.setVisibility(View.VISIBLE);
                holder.bags_area.setVisibility(View.GONE);
                final GoodsInfoBean goodsItem = item.getItems().get(0);
                holder.cur_price.setText(FU.formatPrice(goodsItem.getOrderItemPrice()));
                holder.src_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 设置中划线
                holder.src_price.setVisibility(View.GONE);
                holder.goods_num.setText("X"+goodsItem.getOrderItemNum()+"");
                holder.goods_title.setText(goodsItem.getProductCname());
                if(!TextUtils.isEmpty(goodsItem.getProductColor())){
                    holder.tv_color.setText("颜色："+goodsItem.getProductColor());
                }else{
                    holder.tv_color.setText("");
                }
                if(!TextUtils.isEmpty(goodsItem.getProductSize())){
                    holder.tv_size.setText("尺寸："+goodsItem.getProductSize());
                }else{
                    holder.tv_size.setText("");
                }
                ImageManager.getInstance().displayImg(holder.img_goods,goodsItem.getProductPicUrl());
            }else{
                holder.one_goods_area.setVisibility(View.GONE);
                holder.bags_area.setVisibility(View.VISIBLE);
                holder.tv_num.setText(order.getPackageNum()+"个包裹(共"+order.getOrderItemNum()+"件)");
                if(order.getPicUrlList().size()==2){
                    holder.tv_dots.setVisibility(View.GONE);
                    holder.img_icon1.setVisibility(View.VISIBLE);
                    holder.img_icon2.setVisibility(View.VISIBLE);
                    holder.img_icon3.setVisibility(View.GONE);
                    ImageManager.getInstance().displayImg(holder.img_icon1,order.getPicUrlList().get(0));
                    ImageManager.getInstance().displayImg(holder.img_icon2,order.getPicUrlList().get(1));
                }else if(order.getPicUrlList().size()==3){
                    holder.tv_dots.setVisibility(View.GONE);
                    holder.img_icon1.setVisibility(View.VISIBLE);
                    holder.img_icon2.setVisibility(View.VISIBLE);
                    holder.img_icon3.setVisibility(View.VISIBLE);
                    ImageManager.getInstance().displayImg(holder.img_icon1,order.getPicUrlList().get(0));
                    ImageManager.getInstance().displayImg(holder.img_icon2,order.getPicUrlList().get(1));
                    ImageManager.getInstance().displayImg(holder.img_icon3,order.getPicUrlList().get(2));
                }else if(order.getPicUrlList().size()>3){
                    holder.tv_dots.setVisibility(View.VISIBLE);
                    holder.img_icon1.setVisibility(View.VISIBLE);
                    holder.img_icon2.setVisibility(View.VISIBLE);
                    holder.img_icon3.setVisibility(View.VISIBLE);
                    ImageManager.getInstance().displayImg(holder.img_icon1,order.getPicUrlList().get(0));
                    ImageManager.getInstance().displayImg(holder.img_icon2,order.getPicUrlList().get(1));
                    ImageManager.getInstance().displayImg(holder.img_icon3,order.getPicUrlList().get(2));
                }
            }
        }

        holder.bags_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnClickListener.getGoodsList(position);
            }
        });
        holder.one_goods_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, GoodsDetailsActivity.class);
                it.putExtra("goodsId", order.getChildOrders().get(0).getItems().get(0).getPmInfoId());
                it.putExtra("goodsName", order.getChildOrders().get(0).getItems().get(0).getProductCname());
                it.putExtra("goodsPrice", order.getChildOrders().get(0).getItems().get(0).getOrderItemPrice() + "");
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    public interface BtnClickListener{
        public void getGoodsList(int position);
        public void afterSaleDetail(int position);
    }
    public void setBtnClickListener(BtnClickListener lsner){
        this.mBtnClickListener = lsner;
    }

    class ViewHolder {
        TextView tv_merchant_name, tv_expense_amount, tv_total_amount,tv_product_amount;
        TextView tv_dots,tv_num,cur_price,src_price,goods_num,goods_title,tv_color,tv_size,tv_order_remark;
        Button btn1,btn2,btn3;
        ImageView img_icon1,img_icon2,img_icon3,img_goods;
        View bags_area,one_goods_area,expense_area,total_area,remark_area,btn_area,trade_total_area;
    }
    /***
     * 收到货物
     *
     * @author HP
     */
    public class OrderReceiveListener implements View.OnClickListener {

        MerchantOrderBean order;

        public OrderReceiveListener(MerchantOrderBean order) {
            super();
            this.order = order;
        }

        @Override
        public void onClick(View v) {
            showConfirmReceiptDialog(order);
        }

    }

    public void showConfirmReceiptDialog(final MerchantOrderBean order) {
        mContext.dDialog.showDialog("提示", "确定已收到货物吗？", "确定", "取消",
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mContext.dDialog.dismissDialog();
                        mContext.showDialog();
                        asyncConfirmReceiptGoods(order);
                    }
                }, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mContext.dDialog.dismissDialog();
                    }
                });
    }

    /**
     * 确认收货
     */
    private void asyncConfirmReceiptGoods(MerchantOrderBean order) {

        String wholeUrl = AppUrl.host + AppUrl.ORDER_CONFRIM_RECEIPT;
        long soId = 0L;
        soId = order.getId();
        String requestBodyData = ParamBuild.getOrderDetail(soId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,
                new ConfirmReceiptRequestListener(order));
    }

    class ConfirmReceiptRequestListener implements JsonRequestListener {

        MerchantOrderBean order;

        public ConfirmReceiptRequestListener(MerchantOrderBean order) {
            super();
            this.order = order;
        }

        @Override
        public void onStart(int requestId) {

        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            mContext.dismissDialog();
            mContext.showShortToast("确认收货失败!");

        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            mContext.showShortToast("确认收货成功");
            mContext.dismissDialog();
            order.orderStatus = OrderState.ORDER_STATUS_CUSTOM_RECEIVED;
            GlobalParam.MyOrderNeedRefresh = true;
            GlobalParam.TradeOrderDetailNeedRefresh = true;
            notifyDataSetChanged();
            intentNext(order);
        }
    }
    private void intentNext(MerchantOrderBean order){
        ArrayList<GoodsInfoBean> items = new ArrayList<>();
        OrderBean item = new OrderBean();
        for(int i =0;i<order.getChildOrders().size();i++){
            item = order.getChildOrders().get(i);
            for(int j =0;j<item.getItems().size();j++){
                GoodsInfoBean goodsItem = item.getItems().get(j);
                items.add(goodsItem);
            }
            item.setItems(items);
        }
        Intent it = new Intent(mContext, OrderDealSuccessActivity.class);
        it.putExtra("soId", order.getId());
        it.putExtra("orderInfo", item);
        ((Activity) mContext).startActivity(it);
    }
}
