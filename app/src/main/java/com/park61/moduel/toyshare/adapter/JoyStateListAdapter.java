package com.park61.moduel.toyshare.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.toyshare.TSApplyDetailsActivity;
import com.park61.moduel.toyshare.bean.JoyApplyItem;
import com.park61.moduel.toyshare.bean.TSProgressBean;

import java.util.List;

public class JoyStateListAdapter extends BaseAdapter {

    private List<TSProgressBean> mList;
    private Context mContext;
    private LayoutInflater factory;

    public JoyStateListAdapter(Context _context, List<TSProgressBean> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TSProgressBean getItem(int position) {
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
            convertView = factory.inflate(R.layout.toyapp_state_list_item, null);
            holder = new ViewHolder();
            holder.img_deng = (ImageView) convertView.findViewById(R.id.img_deng);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_state_tip = (TextView) convertView.findViewById(R.id.tv_state_tip);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TSProgressBean item = mList.get(position);
        holder.tv_state.setText(item.getStatusName());
        holder.tv_time.setText(DateTool.L2S(item.getTimes() + ""));
        holder.tv_state_tip.setText(item.getMsg());
        if (position == 0) {
            holder.img_deng.setImageResource(R.drawable.sljchongse);
        } else {
            holder.img_deng.setImageResource(R.drawable.sljchuise);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_deng;
        TextView tv_state;
        TextView tv_time;
        TextView tv_state_tip;
    }

}
