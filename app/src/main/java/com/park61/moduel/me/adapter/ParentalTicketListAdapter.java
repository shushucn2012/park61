package com.park61.moduel.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.me.bean.ParentalTicketBean;

import java.util.List;

/**
 * Created by shubei on 2017/6/12.
 */

public class ParentalTicketListAdapter extends BaseAdapter {

    private List<ParentalTicketBean> mList;
    private Context mContext;
    private LayoutInflater factory;

    public ParentalTicketListAdapter(Context _context, List<ParentalTicketBean> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ParentalTicketBean getItem(int position) {
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
            convertView = factory.inflate(R.layout.parental_ticketlist_item, null);
            holder = new ViewHolder();
            holder.area_top_space = convertView.findViewById(R.id.area_top_space);
            holder.img_qrcode = (ImageView) convertView.findViewById(R.id.img_qrcode);
            holder.img_state = (ImageView) convertView.findViewById(R.id.img_state);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_addr = (TextView) convertView.findViewById(R.id.tv_addr);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.area_top_space.setVisibility(View.VISIBLE);
        } else {
            holder.area_top_space.setVisibility(View.GONE);
        }

        ParentalTicketBean mParentalTicketBean = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_qrcode, mParentalTicketBean.getQrCodeUrl());
        holder.tv_name.setText(mParentalTicketBean.getTitle());
        holder.tv_addr.setText(mParentalTicketBean.getSchoolName() + " " + mParentalTicketBean.getClassName());
        holder.tv_time.setText(mParentalTicketBean.getStartDate());

        if (mParentalTicketBean.getStatus() == 0) { //未签到
            holder.img_state.setImageResource(R.drawable.icon_state_daiqiandao);
        } else if (mParentalTicketBean.getStatus() == 1) {//已签到
            holder.img_state.setImageResource(R.drawable.icon_state_yiqiandao);
        } else if (mParentalTicketBean.getStatus() == 2) {//已结束
            holder.img_state.setImageResource(R.drawable.icon_state_yijiesu);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_qrcode, img_state;
        TextView tv_name, tv_addr, tv_time, tv_comt_content;
        View item_area, area_commt_content, area_top_space;
        ImageView img_face;
    }

}
