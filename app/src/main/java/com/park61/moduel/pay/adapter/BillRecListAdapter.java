package com.park61.moduel.pay.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.moduel.pay.bean.BillInfo;

public class BillRecListAdapter extends BaseAdapter {

    private List<BillInfo> mList;
    private Context mContext;
    private LayoutInflater factory;

    public BillRecListAdapter(Context _context, List<BillInfo> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public BillInfo getItem(int position) {
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
            convertView = factory.inflate(R.layout.bill_rec_list_item, null);
            holder = new ViewHolder();
            holder.tv_pay_title = (TextView) convertView.findViewById(R.id.tv_pay_title);
            holder.tv_pay_time = (TextView) convertView.findViewById(R.id.tv_pay_time);
            holder.tv_operate_amount = (TextView) convertView.findViewById(R.id.tv_operate_amount);
            holder.tv_available_amount = (TextView) convertView.findViewById(R.id.tv_available_amount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BillInfo bb = mList.get(position);
        if(!TextUtils.isEmpty(bb.getRemark())){
            holder.tv_pay_title.setText(bb.getRemark());
        }else{
            holder.tv_pay_title.setText("");
        }
        holder.tv_pay_time.setText(DateTool.L2S(bb.getCreateTime()));
        if (bb.getAvailableOperateAmount() != null) {
            if(bb.getType()<200){
                holder.tv_operate_amount.setText("+"+bb.getAvailableOperateAmount() + "");
            }else{
                holder.tv_operate_amount.setText(bb.getAvailableOperateAmount() + "");
            }
        } else {
            holder.tv_operate_amount.setText("0");
        }
        if (bb.getAvailableAfterAmount() != null) {
            holder.tv_available_amount.setText(bb.getAvailableAfterAmount() + "");
        } else {
            holder.tv_available_amount.setText("0");
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_pay_title;
        TextView tv_pay_time;
        TextView tv_operate_amount;
        TextView tv_available_amount;
    }

}
