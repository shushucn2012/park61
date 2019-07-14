package com.park61.moduel.shophome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.bean.CourseSessionBean;
import com.park61.moduel.shop.bean.ShopBean;

import java.util.List;

public class ChooseShopListAdapter extends BaseAdapter {

    private List<ShopBean> mList;
    private Context mContext;

    public ChooseShopListAdapter(Context _context, List<ShopBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ShopBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.choose_shoplist_item, null);
            holder = new ViewHolder();
            holder.img_shop = (ImageView) convertView.findViewById(R.id.img_shop);
            holder.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holder.tv_shop_distance = (TextView) convertView.findViewById(R.id.tv_shop_distance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShopBean sb = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_shop, sb.getPicUrl());
        holder.tv_shop_name.setText(sb.getName());
        holder.tv_shop_distance.setText(sb.getDistanceNum());
        return convertView;
    }

    class ViewHolder {
        ImageView img_shop;
        TextView tv_shop_name;
        TextView tv_shop_distance;
    }


}
