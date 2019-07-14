package com.park61.moduel.sales.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.SalesSrceeningActivity;
import com.park61.moduel.sales.bean.DataEntity;

import java.util.List;

/**
 * Created by shushucn2012 on 2016/10/30.
 */

public class SimpleExpAdapter extends MyExpandAdapter {
    private List<DataEntity> mDataEntities;   //列表数据存放的集合
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;  //布局渲染成一个view
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.img_default_h)
            .showImageOnLoading(R.drawable.img_default_h)
            .showImageOnFail(R.drawable.img_default_h)
            .cacheInMemory(true).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .delayBeforeLoading(100)
            .build();

    public SimpleExpAdapter(List<DataEntity> dataEntities, Activity activity) {
        mDataEntities = dataEntities;
        mActivity = activity;
        mLayoutInflater = mActivity.getLayoutInflater();
    }

    @Override
    public int getGroupCount() {
        return mDataEntities.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDataEntities.get(groupPosition).getDatas().size();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.exlv_item_group, parent, false);
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_exlv_group);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(String.valueOf(mDataEntities.get(groupPosition).getChar_First()));//适配器设置列表数据
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.exlv_item_child, parent, false);
            viewHolder.area_brand_content = convertView.findViewById(R.id.area_brand_content);
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_exlv_child);
            viewHolder.img_exlv_child = (ImageView) convertView.findViewById(R.id.img_exlv_child);
            viewHolder.bottom_line = convertView.findViewById(R.id.bottom_line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(mDataEntities.get(groupPosition).getDatas().get(childPosition).getBrandName());
        Log.out("logo_url======"+mDataEntities.get(groupPosition).getDatas().get(childPosition).getBrandLogoUrl());
        ImageManager.getInstance().displayImg(viewHolder.img_exlv_child,
                mDataEntities.get(groupPosition).getDatas().get(childPosition).getBrandLogoUrl());
        viewHolder.area_brand_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mActivity, SalesSrceeningActivity.class);
                it.putExtra("BRAND_ID", mDataEntities.get(groupPosition).getDatas().get(childPosition).getId()+"");
                mActivity.startActivity(it);
            }
        });
        if (isLastChild) {
            viewHolder.bottom_line.findViewById(R.id.bottom_line).setVisibility(View.INVISIBLE);
        }else{
            viewHolder.bottom_line.findViewById(R.id.bottom_line).setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {//item放置的控件
        TextView mTextView;
        ImageView img_exlv_child;
        View area_brand_content;
        View bottom_line;
    }
}
