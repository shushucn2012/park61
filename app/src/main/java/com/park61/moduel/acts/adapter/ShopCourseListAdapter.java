package com.park61.moduel.acts.adapter;

import android.content.Context;
import android.text.TextUtils;
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
import com.park61.moduel.acts.bean.ActItem;
import com.park61.moduel.shophome.bean.MyCourseBean;

import java.util.List;

public class ShopCourseListAdapter extends BaseAdapter {

    private List<MyCourseBean> mList;
    private Context mContext;
    private LayoutInflater factory;

    public ShopCourseListAdapter(Context _context, List<MyCourseBean> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public MyCourseBean getItem(int position) {
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
            convertView = factory.inflate(R.layout.shop_courselist_item, null);
            holder = new ViewHolder();
            holder.act_img = (ImageView) convertView.findViewById(R.id.act_img);
            holder.tv_act_title = (TextView) convertView.findViewById(R.id.tv_act_title);
            holder.tv_act_price = (TextView) convertView.findViewById(R.id.tv_act_price);
            holder.tv_leiji = (TextView) convertView.findViewById(R.id.tv_leiji);
            holder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);
            holder.tv_act_price_old = (TextView) convertView.findViewById(R.id.tv_act_price_old);
            holder.tv_age = (TextView) convertView.findViewById(R.id.tv_age);
            holder.tv_read_num = (TextView) convertView.findViewById(R.id.tv_read_num);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyCourseBean item = mList.get(position);
        //标题
        holder.tv_act_title.setText(item.getName());
        //价格
        holder.tv_act_price.setText(FU.formatBigPrice(item.getPriceSale()));
        holder.tv_act_price_old.setText("￥" + item.getPriceOriginal());
        ViewInitTool.lineText(holder.tv_act_price_old);
        if (item.getAgeMin() == item.getAgeMax()) {
            holder.tv_age.setText(item.getAgeMin() + "");
        } else {
            holder.tv_age.setText(item.getAgeMin() + "-" + item.getAgeMax());
        }
        holder.tv_read_num.setText(item.getUserViewNum() + "");
        //封面图
        ImageManager.getInstance().displayImg(holder.act_img, item.getVideoImg());
        return convertView;
    }

    class ViewHolder {
        ImageView act_img;
        TextView tv_act_title;
        TextView tv_act_price;
        TextView tv_age;
        TextView tv_author;
        TextView tv_leiji;
        TextView tv_act_price_old, tv_read_num;
    }
}
