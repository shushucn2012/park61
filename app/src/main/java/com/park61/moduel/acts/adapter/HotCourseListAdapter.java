package com.park61.moduel.acts.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.CourseDetailsActivity;
import com.park61.moduel.shophome.bean.MyCourseBean;

import java.util.List;

public class HotCourseListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context mContext;
    private List<MyCourseBean> list;

    public HotCourseListAdapter(Context _context, List<MyCourseBean> mList) {
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
            convertView = layoutInflater.inflate(R.layout.hot_course_list_item, null);
            holder = new ViewHolder();
            holder.root_view = convertView.findViewById(R.id.root_view);
            holder.image_course = (ImageView) convertView.findViewById(R.id.image_course);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_age = (TextView) convertView.findViewById(R.id.tv_age);
            holder.tv_price_old = (TextView) convertView.findViewById(R.id.tv_price_old);
            holder.view_divider = convertView.findViewById(R.id.view_divider);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MyCourseBean item = list.get(position);
        ImageManager.getInstance().displayImg(holder.image_course, item.getVideoImg(), R.drawable.img_default_h);
        holder.tv_title.setText(item.getName());
        holder.tv_price.setText(FU.formatPrice(item.getPriceSale()));
        holder.tv_price_old.setText("￥" + item.getPriceOriginal());
        if (item.getAgeMin() == item.getAgeMax()) {
            holder.tv_age.setText(item.getAgeMin() + "");
        } else {
            holder.tv_age.setText(item.getAgeMin() + "-" + item.getAgeMax());
        }
        ViewInitTool.lineText(holder.tv_price_old);
        if (position == list.size() - 1) {
            holder.view_divider.setVisibility(View.GONE);
        } else {
            holder.view_divider.setVisibility(View.VISIBLE);
        }
        holder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, CourseDetailsActivity.class);
                it.putExtra("courseId", item.getId());
                it.putExtra("priceSale", item.getPriceSale());
                it.putExtra("priceOriginal", item.getPriceOriginal());
                it.putExtra("className", item.getClassName());
                it.putExtra("courseNum", item.getCost());
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView image_course;
        TextView tv_title;
        TextView tv_price;
        TextView tv_price_old;
        View view_divider, root_view;
        TextView tv_age;
    }
}
