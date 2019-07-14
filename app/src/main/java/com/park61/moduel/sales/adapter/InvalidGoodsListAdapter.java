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
import com.park61.moduel.sales.bean.InvalidGoodsBean;

import java.util.ArrayList;

/**
 * 失效商品adapter
 */
public class InvalidGoodsListAdapter extends BaseAdapter {
    private ArrayList<InvalidGoodsBean> mList;
    private Context mContext;
    private LayoutInflater inflater;

    public InvalidGoodsListAdapter(Context _context,ArrayList<InvalidGoodsBean> _list){
        this.mList = _list;
        this.mContext = _context;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public InvalidGoodsBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.invalid_goods_list_item,null);
            holder.img_goods = (ImageView) convertView.findViewById(R.id.img_goods);
            holder.tv_cover = (TextView) convertView.findViewById(R.id.tv_cover);
            holder.goods_title = (TextView) convertView.findViewById(R.id.goods_title);
            holder.tv_color = (TextView) convertView.findViewById(R.id.tv_color);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        InvalidGoodsBean item = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_goods,item.getProductPicUrl());
        holder.goods_title.setText(item.getProductCname());
        if(item.getProductColor()==null){
            holder.tv_color.setText("");
        }else{
            holder.tv_color.setText("颜色："+item.getProductColor());
        }
        if(item.getProductSize()==null){
            holder.tv_size.setText("");
        }else{
            holder.tv_size.setText("尺寸："+item.getProductSize());
        }
        return convertView;
    }
    class ViewHolder{
        ImageView img_goods;
        TextView tv_cover,goods_title,tv_color,tv_size;

    }
}
