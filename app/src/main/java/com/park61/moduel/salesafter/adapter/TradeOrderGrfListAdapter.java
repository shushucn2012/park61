package com.park61.moduel.salesafter.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.salesafter.bean.GrfOrderInfoBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TradeOrderGrfListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<GrfOrderInfoBean> mList;
    private Context mContext;

    public TradeOrderGrfListAdapter(Context mContext, List<GrfOrderInfoBean> lists) {
        super();
        this.mList = lists;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.trade_grforder_item, null);
            holder = new ViewHolder();
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.tv_deal_amount = (TextView) convertView.findViewById(R.id.tv_deal_amount);
            holder.tv_grf_amount = (TextView) convertView.findViewById(R.id.tv_grf_amount);
            holder.img_icon1 = (ImageView) convertView.findViewById(R.id.img_icon1);
            holder.img_icon2 = (ImageView) convertView.findViewById(R.id.img_icon2);
            holder.img_icon3 = (ImageView) convertView.findViewById(R.id.img_icon3);
            holder.tv_dots = (TextView) convertView.findViewById(R.id.tv_dots);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.line = convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GrfOrderInfoBean grfOrder = mList.get(position);
        if (position == 0) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        //long time = Long.valueOf(grfOrder.getGrfApplyTime());
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.tv_time.setText("订单编号：" + grfOrder.getSoId());
        //holder.tv_time.setText("申请时间 ：" + sdf.format(new Date(time)));
        holder.tv_deal_amount.setText(FU.formatPrice(grfOrder.getGrfAmount()));
        if (!FU.isNumEmpty(grfOrder.getActualRefundAmount() + "")) {
            holder.tv_grf_amount.setText(FU.formatPrice(grfOrder.getActualRefundAmount()));
        } else {
            holder.tv_grf_amount.setText(FU.formatPrice(grfOrder.getGrfAmount()));
        }
        holder.tv_num.setText("共" + grfOrder.getReturnGoodsNum() + "件");
        //退款状态: 0、申请退货，1、退货审核，2、提交货品，3、货品入库，4、系统打款，5、订单关闭
        holder.tv_state.setText(grfOrder.getGrfStatusName());
        if (grfOrder.getItems() != null) {
            ImageManager.getInstance().displayImg(holder.img_icon1, grfOrder.getItems().get(0).getProductPicUrl());
        }
        holder.img_icon2.setVisibility(View.GONE);
        holder.img_icon3.setVisibility(View.GONE);

        return convertView;
    }

    class ViewHolder {
        TextView tv_time, tv_state, tv_deal_amount, tv_grf_amount;
        LinearLayout lay_goods;
        ImageView img_icon1, img_icon2, img_icon3;
        TextView tv_dots, tv_num;
        View line;
    }
}
