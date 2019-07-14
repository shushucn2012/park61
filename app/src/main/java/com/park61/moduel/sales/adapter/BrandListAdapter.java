package com.park61.moduel.sales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.sales.bean.BrandBean;

import java.util.List;

/**
 * Created by HP on 2016/10/18.
 */
public class BrandListAdapter extends BaseAdapter {
    private Context context;
    private List<BrandBean> mList;
    private boolean isHiden;

    public BrandListAdapter(Context _context, List<BrandBean> _List) {
        this.context = _context;
        this.mList = _List;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.brand_item, null);
            holder = new ViewHolder();
            holder.tv_brand_name = (TextView) convertView.findViewById(R.id.tv_brand_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_brand_name.setText(mList.get(position).getBrandName());
        if (mList.get(position).isChosen()) {
            holder.tv_brand_name.setTextColor(context.getResources().getColor(R.color.gffffff));
            holder.tv_brand_name.setBackgroundResource(R.drawable.jbbackground);
        }else{
            holder.tv_brand_name.setTextColor(context.getResources().getColor(R.color.g666666));
            holder.tv_brand_name.setBackgroundResource(R.drawable.rec_gray_stroke_gray_solid);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_brand_name;
    }

}
