package com.park61.moduel.shophome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.shop.bean.ShopBean;

import java.util.List;

public class ShopChooseListAdapter extends BaseAdapter {

    private List<ShopBean> mList;
    private Context mContext;
    private LayoutInflater factory;

    public ShopChooseListAdapter(Context _context, List<ShopBean> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
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
            convertView = factory.inflate(R.layout.map_shoplist_item, null);
            holder = new ViewHolder();
            holder.img_shop = (ImageView) convertView.findViewById(R.id.img_shop);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_addr = (TextView) convertView.findViewById(R.id.tv_addr);
            holder.tv_downarrow = (TextView) convertView.findViewById(R.id.tv_downarrow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShopBean mf = mList.get(position);
        holder.tv_name.setText(mf.getName());
        holder.tv_addr.setText("地址：" + mf.getAddress() == null ? "" : mf.getAddress());
        holder.tv_downarrow.setText(mf.getDistanceNum());
        String imgUrl = mf.getPicUrl();
        ImageManager.getInstance().displayImg(holder.img_shop, imgUrl);
        return convertView;
    }

    class ViewHolder {
        ImageView img_shop;
        TextView tv_name;
        TextView tv_addr;
        TextView tv_downarrow;
    }

}
