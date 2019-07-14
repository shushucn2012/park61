package com.park61.moduel.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.me.bean.ApplyActItem;

import java.util.List;

public class ActOrderListAdapter extends BaseAdapter {

    private List<ApplyActItem> mList;
    private Context mContext;
    private LayoutInflater factory;
    private ApplyItemCallBack mApplyItemCallBack;
    private String orderState;//订单状态

    public ActOrderListAdapter(Context _context, List<ApplyActItem> _list, String orderState) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
        this.orderState = orderState;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ApplyActItem getItem(int position) {
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
            convertView = factory.inflate(R.layout.apply_actlist_item, null);
            holder = new ViewHolder();
            holder.img_act = (ImageView) convertView.findViewById(R.id.img_act);
            holder.actinfo_area = convertView.findViewById(R.id.actinfo_area);
            holder.tv_act_title = (TextView) convertView.findViewById(R.id.tv_act_title);
            holder.tv_act_addr = (TextView) convertView.findViewById(R.id.tv_act_addr);
            holder.tv_act_price = (TextView) convertView.findViewById(R.id.tv_act_price);
            holder.tv_small_course_num = (TextView) convertView.findViewById(R.id.tv_small_course_num);
            holder.tv_apply_child_num = (TextView) convertView.findViewById(R.id.tv_apply_child_num);
            holder.tv_apply_adult_num = (TextView) convertView.findViewById(R.id.tv_apply_adult_num);
            holder.tv_pay_value = (TextView) convertView.findViewById(R.id.tv_pay_value);
            holder.tv_order_number = (TextView) convertView.findViewById(R.id.tv_order_number);
            holder.tv_order_state = (TextView) convertView.findViewById(R.id.tv_order_state);
            holder.btn_cancel_apply = (Button) convertView.findViewById(R.id.btn_cancel_apply);
            holder.btn_pay = (Button) convertView.findViewById(R.id.btn_pay);
            holder.btn_share = (Button) convertView.findViewById(R.id.btn_share);
            holder.btn_comt = (Button) convertView.findViewById(R.id.btn_comt);
            holder.line = convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ApplyActItem ai = mList.get(position);
        if (position == 0) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        ImageManager.getInstance().displayImg(holder.img_act, ai.getActCover(), R.drawable.img_default_v);
        holder.tv_act_title.setText(ai.getActTitle());
        holder.tv_act_addr.setText(ai.getActAddress());
        holder.tv_act_price.setText(FU.isNumEmpty(ai.getApplyPrice()) ? "免费" : "￥" + ai.getApplyPrice());
        holder.tv_pay_value.setText(ai.getTotalPrice() + "");
        holder.tv_apply_child_num.setText("儿童×" + ai.getApplyChildrenNumber());
        holder.tv_apply_adult_num.setText("成人×" + ai.getApplyAdultsNumber());
        holder.tv_small_course_num.setText("共" + ai.getActClassCount() + "节小课");
        holder.tv_order_number.setText("订单编号：" + (ai.getOrderId() == null ? ai.getId() : ai.getOrderId()));
        if (GlobalParam.SMALL_CLASS_CODE.equals(ai.getActBussinessType())) {
            holder.tv_small_course_num.setVisibility(View.VISIBLE);
            holder.tv_apply_adult_num.setVisibility(View.GONE);
        } else {
            holder.tv_small_course_num.setVisibility(View.GONE);
            holder.tv_apply_adult_num.setVisibility(View.VISIBLE);
        }
        holder.btn_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mApplyItemCallBack.onShareClicked(position);
            }
        });
        holder.btn_cancel_apply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mApplyItemCallBack.onCancelClicked(position);
            }
        });
        holder.btn_pay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mApplyItemCallBack.onPayClicked(position);
            }
        });
        holder.btn_comt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mApplyItemCallBack.onComtClicked(position);
            }
        });
        holder.actinfo_area.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mApplyItemCallBack.onDetailsClicked(position);
            }
        });
        if (GlobalParam.ActOrderState.WAITFORPAY.equals(orderState)) {
            holder.tv_order_state.setText("待付款");
            holder.btn_cancel_apply.setVisibility(View.VISIBLE);
            holder.btn_pay.setVisibility(View.VISIBLE);
        } else if (GlobalParam.ActOrderState.APPLIED.equals(orderState)) {
            holder.tv_order_state.setText("报名成功");
            holder.btn_cancel_apply.setVisibility(View.VISIBLE);
            holder.btn_share.setVisibility(View.VISIBLE);
            holder.btn_share.setBackgroundResource(R.drawable.btn_orderlist_selector);
            holder.btn_share.setTextColor(mContext.getResources().getColorStateList(R.color.txt_orderlist_selector));
        } else if (GlobalParam.ActOrderState.WAITFORCOMT.equals(orderState)) {
            holder.tv_order_state.setText("待评价");
            holder.btn_share.setVisibility(View.VISIBLE);
            holder.btn_comt.setVisibility(View.VISIBLE);
        } else if (GlobalParam.ActOrderState.DONE.equals(orderState)) {
            holder.btn_share.setVisibility(View.VISIBLE);
            if ("0".equals(ai.getIsEvaluate())) {
                holder.tv_order_state.setText("待评价");
                holder.btn_comt.setVisibility(View.VISIBLE);
            } else {
                holder.tv_order_state.setText("交易完成");
                holder.btn_comt.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_act;
        TextView tv_act_title;
        TextView tv_act_addr;
        TextView tv_act_price;
        TextView tv_small_course_num;
        TextView tv_apply_child_num;
        TextView tv_apply_adult_num;
        TextView tv_pay_value;
        TextView tv_order_number;
        TextView tv_order_state;
        Button btn_cancel_apply;
        Button btn_share;
        Button btn_comt;
        Button btn_pay;
        View actinfo_area, line;
    }

    public interface ApplyItemCallBack {
        public void onDetailsClicked(int pos);

        public void onShareClicked(int pos);

        public void onComtClicked(int pos);

        public void onCancelClicked(int pos);

        public void onPayClicked(int pos);
    }

    public void setListener(ApplyItemCallBack listener) {
        this.mApplyItemCallBack = listener;
    }

}
