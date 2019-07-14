package com.park61.moduel.dreamhouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.dreamhouse.bean.DreamItemInfo;

import java.util.ArrayList;

/**
 * 我的梦想列表adapter
 */
public class DreamThemeListAdapter extends BaseAdapter {
    private ArrayList<DreamItemInfo> mList;
    private Context mContext;
    private LayoutInflater factory;

    public DreamThemeListAdapter(Context _context, ArrayList<DreamItemInfo> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = factory.inflate(R.layout.dreamtheme_list_item, null);
            holder.img_user = (ImageView) convertView.findViewById(R.id.img_user);
            holder.img_game = (ImageView) convertView.findViewById(R.id.img_game);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_lable = (TextView) convertView.findViewById(R.id.tv_lable);
            holder.line = convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == mList.size()-1) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        DreamItemInfo item = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_game, item.getCoverPic());
        ImageManager.getInstance().displayCircleImg(holder.img_user, item.getHeadUrl());
        holder.tv_name.setText(item.getUserName());
        holder.tv_title.setText(item.getTitle());
        holder.tv_lable.setText(item.getRequirementLabel());
        if(item.getPredictionNum()!=0){
            holder.tv_num.setVisibility(View.VISIBLE);
            holder.tv_num.setText(item.getPredictionNum()+"同梦人");
        }else{
            holder.tv_num.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_user;
        TextView tv_title, tv_lable, tv_name, tv_num;
        ImageView img_game;
        View line;
    }
}
