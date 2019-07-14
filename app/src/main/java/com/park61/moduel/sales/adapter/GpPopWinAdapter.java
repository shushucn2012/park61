package com.park61.moduel.sales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.sales.bean.DiscountActivity;

import java.util.List;

public class GpPopWinAdapter extends BaseAdapter {
    private Context context;
    private List<DiscountActivity> pList;

    public GpPopWinAdapter(Context c, List<DiscountActivity> list) {
        this.context = c;
        this.pList = list;
    }

    @Override
    public int getCount() {
        return pList.size();
    }

    @Override
    public DiscountActivity getItem(int position) {
        return pList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.pw_gplist_item, null);
            holder = new ViewHolder();
            holder.tv_p_title = (TextView) convertView.findViewById(R.id.tv_p_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DiscountActivity da = pList.get(position);
        holder.tv_p_title.setText(da.getTitle());
        return convertView;
    }

    class ViewHolder {
        TextView tv_p_title;
    }

}
