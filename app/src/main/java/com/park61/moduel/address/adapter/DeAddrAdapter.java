package com.park61.moduel.address.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.address.bean.AddressDetailItem;

import java.util.List;

public class DeAddrAdapter extends BaseAdapter {
    private Context context;
    private List<AddressDetailItem> deAddrList;
    private int selectedPos;

    public void selectItem(int pos) {
        selectedPos = pos;
        notifyDataSetChanged();
    }

    public DeAddrAdapter(Context c, List<AddressDetailItem> list) {
        this.context = c;
        this.deAddrList = list;
    }

    @Override
    public int getCount() {
        return deAddrList.size();
    }

    @Override
    public AddressDetailItem getItem(int position) {
        return deAddrList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = buildLayout();
            holder = new ViewHolder();
            holder.tv_deaddr_item = (TextView) convertView.findViewById(R.id.tv_deaddr_item);
            holder.img_deaddr_item = (ImageView) convertView.findViewById(R.id.img_deaddr_item);
            holder.imgg_deaddr_item = (ImageView) convertView.findViewById(R.id.imgg_deaddr_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AddressDetailItem ad = deAddrList.get(position);
        holder.tv_deaddr_item.setText(ad.getGoodReceiverProvinceName()
                + ad.getGoodReceiverCityName()
                + ad.getGoodReceiverCountyName()+ad.getGoodReceiverTownName()
                + ad.getGoodReceiverAddress());
        if (selectedPos == position) {
            holder.img_deaddr_item.setImageResource(R.drawable.ic_map_focus);
            holder.tv_deaddr_item.setTextColor(context.getResources().getColor(R.color.com_orange));
            holder.imgg_deaddr_item.setVisibility(View.VISIBLE);
        } else {
            holder.img_deaddr_item.setImageResource(R.drawable.ic_map);
            holder.tv_deaddr_item.setTextColor(context.getResources().getColor(R.color.g333333));
            holder.imgg_deaddr_item.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_deaddr_item;
        ImageView img_deaddr_item,imgg_deaddr_item;
    }

    private View buildLayout() {
        LinearLayout rootlLayout = new LinearLayout(context);
        rootlLayout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.MATCH_PARENT));
        rootlLayout.setOrientation(LinearLayout.HORIZONTAL);
        rootlLayout.setGravity(Gravity.CENTER_VERTICAL);
        rootlLayout.setPadding(DevAttr.dip2px(context, 12), DevAttr.dip2px(context, 15),
                DevAttr.dip2px(context, 12), DevAttr.dip2px(context, 15));
        rootlLayout.setWeightSum(20);

        ImageView img = new ImageView(context);
        img.setId(R.id.img_deaddr_item);
        img.setImageResource(R.drawable.ic_map);
        img.setLayoutParams(new LinearLayout.LayoutParams(DevAttr.dip2px(context, 15),
                DevAttr.dip2px(context, 15)));

        TextView tv = new TextView(context);
        tv.setId(R.id.tv_deaddr_item);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv.setTextColor(context.getResources().getColor(R.color.g333333));
        LinearLayout.LayoutParams tvLParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 19);
        tvLParams.setMargins(DevAttr.dip2px(context, 5), 0, 0, 0);//4个参数按顺序分别是左上右下
        tv.setLayoutParams(tvLParams);

        ImageView imgg = new ImageView(context);
        imgg.setId(R.id.imgg_deaddr_item);
        imgg.setImageResource(R.drawable.gougou);
        LinearLayout.LayoutParams imggLParams = new LinearLayout.LayoutParams(
                DevAttr.dip2px(context, 15),
                DevAttr.dip2px(context, 15));
        imggLParams.setMargins(DevAttr.dip2px(context, 10), 0, 0, 0);//4个参数按顺序分别是左上右下
        imgg.setLayoutParams(imggLParams);
        imgg.setVisibility(View.INVISIBLE);

        rootlLayout.addView(img);
        rootlLayout.addView(tv);
        rootlLayout.addView(imgg);

        return rootlLayout;
    }

}
