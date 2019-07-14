package com.park61.moduel.sales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.bean.GoldTeacher;

import java.util.List;

public class MExpertListAdapter extends BaseAdapter {

    private List<GoldTeacher> mList;
    private Context mContext;

    public MExpertListAdapter(Context context, List<GoldTeacher> list) {
        mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public GoldTeacher getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mexpertlist_item, null);
            holder = new ViewHolder();
            holder.root_hotgame = convertView.findViewById(R.id.root_hotgame);
            holder.img_teacher = (ImageView) convertView.findViewById(R.id.img_teacher);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_lable = (TextView) convertView.findViewById(R.id.tv_lable);
            holder.bottom_line = convertView.findViewById(R.id.bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GoldTeacher teacher = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_teacher, teacher.getHeadPictureUrl());
        holder.tv_title.setText(teacher.getName()+" · "+teacher.getSubhead());
        holder.tv_lable.setText(teacher.getDescription());
        holder.tv_num.setText(teacher.getContentTitle());
        if (position == mList.size() - 1) {
            holder.bottom_line.setVisibility(View.GONE);
        } else {
            holder.bottom_line.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView img_teacher;
        TextView tv_title;
        TextView tv_num;
        TextView tv_lable;
        View root_hotgame, bottom_line;
    }
}
