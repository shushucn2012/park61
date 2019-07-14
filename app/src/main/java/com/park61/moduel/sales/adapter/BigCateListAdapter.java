package com.park61.moduel.sales.adapter;

import java.util.List;

import com.park61.R;
import com.park61.moduel.sales.bean.ProductCategory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BigCateListAdapter extends BaseAdapter {

    private Context context;
    private List<ProductCategory> cateList;
    private int selectedPos = 0;

    public void selectItem(int pos) {
        selectedPos = pos;
        notifyDataSetChanged();
    }

    public BigCateListAdapter(Context c, List<ProductCategory> list) {
        this.context = c;
        this.cateList = list;
    }

    @Override
    public int getCount() {
        return cateList.size();
    }

    @Override
    public Object getItem(int position) {
        return cateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.bigcatelist_item, null);
            holder = new ViewHolder();
            holder.bigcate_item_root = convertView.findViewById(R.id.bigcate_item_root);
            holder.tv_cate_name = (TextView) convertView.findViewById(R.id.tv_cate_name);
            holder.cate_v_line = convertView.findViewById(R.id.cate_v_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ProductCategory pc = cateList.get(position);
        holder.tv_cate_name.setText(pc.getCategoryName());
        if (position == selectedPos) {
            holder.cate_v_line.setVisibility(View.VISIBLE);
            holder.bigcate_item_root.setBackgroundColor(context.getResources().getColor(R.color.gffffff));
        } else {
            holder.cate_v_line.setVisibility(View.GONE);
            holder.bigcate_item_root.setBackgroundColor(context.getResources().getColor(R.color.bg_color));
        }
        return convertView;
    }

    class ViewHolder {
        View bigcate_item_root;
        TextView tv_cate_name;
        View cate_v_line;
    }

}
