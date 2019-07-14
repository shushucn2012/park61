package com.park61.moduel.order.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.json.NullStringToEmptyAdapterFactory;
import com.park61.common.set.AppUrl;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.order.bean.GoodsInfoBean;
import com.park61.moduel.order.bean.OrderBean;
import com.park61.moduel.order.bean.OrderState;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.salesafter.AuditStatuActivity;
import com.park61.moduel.salesafter.MerchantSignActivity;
import com.park61.moduel.salesafter.RefundFinishActivity;
import com.park61.moduel.salesafter.SalesAfterActivity;
import com.park61.moduel.salesafter.SalesClosedActivity;
import com.park61.moduel.salesafter.WaitForMerchanSignActivity;
import com.park61.moduel.salesafter.WaitForSendGoodsActivity;
import com.park61.moduel.salesafter.bean.ApplySalesAfterInfo;
import com.park61.net.base.Request;
import com.park61.net.request.StringNetRequest;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 商品清单adapter
 */
public class OrderGoodsListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private BaseActivity mContext;
    private ArrayList<OrderBean> mList;
    public StringNetRequest netRequest;
    private BtnClickListener mBtnClickListener;
    long grfId = 0L;
    long pmInfoId = 0l;

    public OrderGoodsListAdapter(BaseActivity _context,
                                 ArrayList<OrderBean> _mList) {
        this.mList = _mList;
        this.mContext = _context;
        layoutInflater = LayoutInflater.from(mContext);
        netRequest = StringNetRequest.getInstance(_context);
        netRequest.setMainContext(_context);
        Log.out("mList.size------" + mList.size());
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
            convertView = layoutInflater.inflate(
                    R.layout.order_goodslist_list, null);
            holder = new ViewHolder();
            holder.btn1 = (Button) convertView.findViewById(R.id.btn1);
            holder.btn2 = (Button) convertView.findViewById(R.id.btn2);
            holder.btn3 = (Button) convertView.findViewById(R.id.btn3);
            holder.btn_area = convertView.findViewById(R.id.btn_area);
            holder.line = convertView.findViewById(R.id.line);
            holder.tv_bags_num = (TextView) convertView
                    .findViewById(R.id.tv_bags_num);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.lay_goods = (LinearLayout) convertView
                    .findViewById(R.id.lay_goods);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderBean order = mList.get(position);
        holder.tv_bags_num.setText("包裹" + (position + 1));
        holder.tv_num.setText(order.getOrderItemNum() + "");
        if (position == 0) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        int orderStatus = 0;//订单状态：1 已下单:货款未全收, 2 已下单:货款已全收,3 已转DO,4 已出库（货在途）
        //5 货物用户已收货,6 送货失败（其它）,7 要求退货,8 退货中 ,9 退货完成 ,10 订单取消,11 订单完成
        orderStatus = order.orderStatus;
        if (mList.size() > 1 && orderStatus == OrderState.ORDER_STATUS_TRUNED_TO_DO) {//3 已转DO
            holder.btn_area.setVisibility(View.VISIBLE);
            holder.btn1.setVisibility(View.GONE);
            holder.btn2.setVisibility(View.GONE);
            holder.btn3.setVisibility(View.VISIBLE);
            holder.btn3.setText("查看物流");
            holder.btn3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBtnClickListener.checkLogistics(position);
                }
            });
        } else if (mList.size() > 1 && orderStatus == OrderState.ORDER_STATUS_OUT_OF_WH) {//4 已出库（货在途）
            holder.btn_area.setVisibility(View.VISIBLE);
            holder.btn1.setVisibility(View.GONE);
            holder.btn2.setVisibility(View.VISIBLE);
            holder.btn3.setVisibility(View.VISIBLE);
            holder.btn2.setText("确认收货");
            holder.btn2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBtnClickListener.confirmReceipt(position);
                }
            });
            holder.btn3.setText("查看物流");
            holder.btn3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBtnClickListener.checkLogistics(position);
                }
            });
        } else if (mList.size() > 1 && orderStatus == OrderState.ORDER_STATUS_CUSTOM_RECEIVED) {//5 货物用户已收货
            holder.btn_area.setVisibility(View.VISIBLE);
            holder.btn1.setVisibility(View.GONE);
            holder.btn2.setVisibility(View.GONE);
            holder.btn3.setVisibility(View.VISIBLE);
            holder.btn3.setText("查看物流");
            holder.btn3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBtnClickListener.checkLogistics(position);
                }
            });
        } else if (mList.size() > 1 && orderStatus == OrderState.ORDER_STATUS_TO_RETURN ||
                orderStatus == OrderState.ORDER_STATUS_RETURNING || orderStatus == OrderState.ORDER_STATUS_RETURNED) {
            //7 要求退货,8 退货中 ,9 退货完成
            holder.btn_area.setVisibility(View.GONE);
        } else {
            holder.btn_area.setVisibility(View.GONE);
        }
        holder.lay_goods.removeAllViews();
        for (int i = 0; i < order.getItems().size(); i++) {
            final GoodsInfoBean items = order.getItems().get(i);
            View view_goods_item = layoutInflater.inflate(
                    R.layout.order_goodslist_item, null);
            View area = view_goods_item.findViewById(R.id.area);
            ImageView icon = (ImageView) view_goods_item
                    .findViewById(R.id.icon);
            TextView title = (TextView) view_goods_item
                    .findViewById(R.id.title);
            TextView cur_price = (TextView) view_goods_item
                    .findViewById(R.id.cur_price);
            TextView src_price = (TextView) view_goods_item
                    .findViewById(R.id.src_price);
            TextView num = (TextView) view_goods_item.findViewById(R.id.num);
            TextView tv_color = (TextView) view_goods_item
                    .findViewById(R.id.tv_color);
            TextView tv_size = (TextView) view_goods_item
                    .findViewById(R.id.tv_size);
            Button btn_apply_aftersales = (Button) view_goods_item
                    .findViewById(R.id.btn_apply_aftersales);
            ImageManager.getInstance().displayImg(icon, items.getProductPicUrl());
            title.setText(items.getProductCname());
            src_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 设置中划线
            cur_price.setText(FU.formatPrice(items.getOrderItemPrice()));

            src_price.setVisibility(View.GONE);
            num.setText("X" + items.getOrderItemNum() + "");
            if (TextUtils.isEmpty(items.getProductColor())) {
                tv_color.setText("");
            } else {
                tv_color.setText("颜色:" + items.getProductColor());
            }
            if (TextUtils.isEmpty(items.getProductSize())) {
                tv_size.setText("");
            } else {
                tv_size.setText("尺码:" + items.getProductSize());
            }
            holder.lay_goods.addView(view_goods_item);

            if (items.getIsGrfGoods() == 0 && orderStatus == 5) {//是否已经申请售后(1-是 0-否)
                btn_apply_aftersales.setVisibility(View.VISIBLE);
                btn_apply_aftersales.setText("申请售后");
                btn_apply_aftersales.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent it = new Intent(mContext, SalesAfterActivity.class);
                        it.putExtra("soId", mList.get(position).getId());
                        it.putExtra("pmInfoId", items.getPmInfoId());
                        it.putExtra("orderItemNum", items.getOrderItemNum());
                        mContext.startActivity(it);
                        mContext.finish();
                    }
                });
            } else if (items.getIsGrfGoods() == 1 && (orderStatus == 5 || orderStatus == 7 || orderStatus == 8 || orderStatus == 9)) {
                btn_apply_aftersales.setVisibility(View.VISIBLE);
                btn_apply_aftersales.setText("售后详情");
                btn_apply_aftersales.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        grfId = mList.get(position).getId();
                        pmInfoId = items.getPmInfoId();
                        asyncQueryReturnApply(grfId, pmInfoId);
                    }
                });
            } else {
                btn_apply_aftersales.setVisibility(View.GONE);
            }

            area.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, GoodsDetailsActivity.class);
                    it.putExtra("goodsId", items.getPmInfoId());
                    it.putExtra("goodsName", items.getProductCname());
                    it.putExtra("goodsPrice", items.getOrderItemPrice() + "");
                    mContext.startActivity(it);
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_bags_num, tv_num;
        LinearLayout lay_goods;
        View line, btn_area;
        Button btn1, btn2, btn3;
    }

    public interface BtnClickListener {
        public void confirmReceipt(int position);

        public void checkLogistics(int position);

        public void afterSaleDetail(int position);
    }

    public void setBtnClickListener(BtnClickListener lsner) {
        this.mBtnClickListener = lsner;
    }

    /**
     * 退货申请查询
     */
    private void asyncQueryReturnApply(long grfId, long pmInfoId) {
        String wholeUrl = AppUrl.host + AppUrl.RETURN_QUERY;
//        String requestBodyData = ParamBuild.queryById(grfId);
        String requestBodyData = ParamBuild.queryByIdV2(grfId, pmInfoId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            mContext.showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            mContext.dismissDialog();
            mContext.showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            mContext.dismissDialog();
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
            ApplySalesAfterInfo item = gson.fromJson(jsonResult.toString(), ApplySalesAfterInfo.class);// 把Json数据解析成对象
            int grfStatus = item.getGrfStatus();
            int auditResults = item.getAuditResults();
            int waybillType = item.getWaybillType();
            int isReturnGoods = item.getIsReturnGoods();

            long grfApplyTime = item.getGrfApplyTime();
            String grfCode = item.getGrfCode();
            String remark = item.getRemark();
            int returnGoodsNum = item.getReturnGoodsNum();
            float grfAmount = item.getGrfAmount();
            String grfReason = item.getGrfReason();
            String storePhone = item.getStorePhone();
            String storeAddress = item.getStoreAddress();

            String deliveryCompanyName = item.getDeliveryCompanyName();
            String waybillCode = item.getWaybillCode();

            long updateTime = item.getUpdateTime();
            String closeReason = item.getCloseReason();

            long refundWay = item.getRefundWay();
            ArrayList<String> grfPicUrls = new ArrayList<String>();
            for (int i = 0; i < item.getPics().size(); i++) {
                String givebackPicUrl = item.getPics().get(i).getGivebackPicUrl();
                grfPicUrls.add(givebackPicUrl);
            }
            // 退款状态: 0、申请退货，1、退货审核，2、提交货品，3、货品入库，4、系统打款，5、订单关闭
            if (grfStatus == 0) {
                Intent intent1 = new Intent(mContext, AuditStatuActivity.class);
                intent1.putExtra("grfApplyTime", grfApplyTime);
                intent1.putExtra("grfCode", grfCode);
                intent1.putExtra("remark", remark);
                intent1.putExtra("returnGoodsNum", returnGoodsNum);
                intent1.putExtra("grfAmount", grfAmount);
                intent1.putExtra("grfReason", grfReason);
                intent1.putExtra("grfPicUrls", grfPicUrls);
                mContext.startActivity(intent1);
            } else if (grfStatus == 1) {
                Intent intent2 = new Intent(mContext, WaitForSendGoodsActivity.class);
                intent2.putExtra("grfId",item.getItems().get(0).getGrfId());
                intent2.putExtra("pmInfoId",pmInfoId);
                intent2.putExtra("soId", item.getSoId());
                intent2.putExtra("auditResults", auditResults);
                intent2.putExtra("grfApplyTime", grfApplyTime);
                intent2.putExtra("grfCode", grfCode);
                intent2.putExtra("remark", remark);
                intent2.putExtra("returnGoodsNum", returnGoodsNum);
                intent2.putExtra("grfAmount", grfAmount);
                intent2.putExtra("grfReason", grfReason);
                intent2.putExtra("grfPicUrls", grfPicUrls);
                intent2.putExtra("waybillType", waybillType);
                intent2.putExtra("storePhone", storePhone);
                intent2.putExtra("storeAddress", storeAddress);
                mContext.startActivity(intent2);
            } else if (grfStatus == 2) {
                Intent intent3 = new Intent(mContext, WaitForMerchanSignActivity.class);
                intent3.putExtra("isReturnGoods", isReturnGoods);
                intent3.putExtra("grfApplyTime", grfApplyTime);
                intent3.putExtra("grfCode", grfCode);
                intent3.putExtra("remark", remark);
                intent3.putExtra("returnGoodsNum", returnGoodsNum);
                intent3.putExtra("grfAmount", grfAmount);
                intent3.putExtra("grfReason", grfReason);
                intent3.putExtra("deliveryCompanyName", deliveryCompanyName);
                intent3.putExtra("waybillCode", waybillCode);
                intent3.putExtra("grfPicUrls", grfPicUrls);

                intent3.putExtra("waybillType", waybillType);
                intent3.putExtra("storePhone", storePhone);
                intent3.putExtra("storeAddress", storeAddress);
                mContext.startActivity(intent3);
            } else if (grfStatus == 3) {
                Intent intent4 = new Intent(mContext, MerchantSignActivity.class);
                intent4.putExtra("grfApplyTime", grfApplyTime);
                intent4.putExtra("grfCode", grfCode);
                intent4.putExtra("remark", remark);
                intent4.putExtra("returnGoodsNum", returnGoodsNum);
                intent4.putExtra("grfAmount", grfAmount);
                intent4.putExtra("grfReason", grfReason);
                intent4.putExtra("deliveryCompanyName", deliveryCompanyName);
                intent4.putExtra("waybillCode", waybillCode);
                intent4.putExtra("grfPicUrls", grfPicUrls);

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
                intent.putExtra("updateTime", updateTime);
                intent.putExtra("closeReason", closeReason);

                intent.putExtra("isReturnGoods", isReturnGoods);
                intent.putExtra("grfApplyTime", grfApplyTime);
                intent.putExtra("grfCode", grfCode);
                intent.putExtra("remark", remark);
                intent.putExtra("returnGoodsNum", returnGoodsNum);
                intent.putExtra("grfAmount", grfAmount);
                intent.putExtra("grfReason", grfReason);
                intent.putExtra("deliveryCompanyName", deliveryCompanyName);
                intent.putExtra("waybillCode", waybillCode);
                intent.putExtra("grfPicUrls", grfPicUrls);
                mContext.startActivity(intent);
            }
        }
    };

}
