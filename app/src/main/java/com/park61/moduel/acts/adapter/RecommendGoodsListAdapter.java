package com.park61.moduel.acts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.bean.RecommendGoodsInfo;
import java.util.ArrayList;

public class RecommendGoodsListAdapter extends BaseAdapter{
    private LayoutInflater layoutInflater;
    private Context mContext;
    private ArrayList<RecommendGoodsInfo> list;

    public RecommendGoodsListAdapter(Context _context, ArrayList<RecommendGoodsInfo> mList) {
        this.list = mList;
        this.mContext = _context;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.recommend_goods_listview_item, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecommendGoodsInfo item = list.get(position);
        ImageManager.getInstance().displayImg(holder.image,item.getImg(), R.drawable.img_default_v);
        holder.title.setText(item.getName());
        holder.price.setText(FU.formatPrice(item.getSalesPrice()));
        return convertView;
    }

    class ViewHolder {
        ImageView image;
        TextView title;
        TextView price;
    }
}
