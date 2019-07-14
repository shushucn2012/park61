package com.park61.moduel.grouppurchase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.grouppurchase.bean.FightGroupOpenDetails;

import java.util.List;

/**
 * Created by HP on 2016/7/21.
 */
public class FightGroupOpenDetailsAdapter extends BaseAdapter {
    private List<FightGroupOpenDetails> mList;
    private Context mContext;
    private LayoutInflater factory;
    private int type;
    public FightGroupOpenDetailsAdapter(Context _context, List<FightGroupOpenDetails> _list){
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public FightGroupOpenDetails getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = factory.inflate(R.layout.fightgroup_opendetail_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_join_info = (TextView) convertView.findViewById(R.id.tv_join_info);
            holder.tv_opentime = (TextView) convertView.findViewById(R.id.tv_opentime);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        FightGroupOpenDetails item = mList.get(position);
        ImageManager.getInstance().displayCircleImg(holder.img,item.getPictureUrl());
        holder.tv_name.setText(item.getName());
        holder.tv_opentime.setText(DateTool.L2S(item.getJoinTime()));
        type = item.getType();
        if(type==1){
            holder.tv_join_info.setText("开团");
        }else{
            holder.tv_join_info.setText("参团");
        }
        return convertView;
    }
    class ViewHolder{
        ImageView img;
        TextView tv_name,tv_join_info,tv_opentime;
    }
}
