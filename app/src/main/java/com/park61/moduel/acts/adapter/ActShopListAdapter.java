package com.park61.moduel.acts.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.ActDetailsActivity;
import com.park61.moduel.acts.bean.ActItem;
import com.park61.moduel.child.bean.AblityLevelRecGames;

import java.util.List;

public class ActShopListAdapter extends BaseAdapter {

    private List<ActItem> mList;
    private Context mContext;

    public ActShopListAdapter(Context context, List<ActItem> list) {
        mContext = context;
        this.mList = list;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.actshoplist_item, null);
            holder = new ViewHolder();
            holder.root_hotgame = convertView.findViewById(R.id.root_hotgame);
            holder.img_game = (ImageView) convertView.findViewById(R.id.img_game);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_src_price = (TextView) convertView.findViewById(R.id.tv_src_price);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_lable = (TextView) convertView.findViewById(R.id.tv_lable);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ActItem item = mList.get(position);
        holder.tv_title.setText(item.getActTitle());
        holder.tv_src_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_src_price.setText(FU.formatPrice(item.getActOriginalPrice()));
        holder.tv_src_price.setVisibility(View.GONE);
        holder.tv_price.setText(FU.formatPrice(item.getActPrice()));
        holder.tv_num.setText("已售" + item.getGrandTotal());
        holder.tv_lable.setVisibility(View.GONE);
        ImageManager.getInstance().displayImg(holder.img_game, item.getActCover(), R.drawable.img_default_h);
        holder.root_hotgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ActDetailsActivity.class);
                it.putExtra("actTempId", item.getRefTemplateId());
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img_game;
        TextView tv_title;
        TextView tv_price;
        TextView tv_src_price;
        TextView tv_num;
        TextView tv_lable;
        View root_hotgame;
    }
}
