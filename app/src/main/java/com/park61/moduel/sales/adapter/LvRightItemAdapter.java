package com.park61.moduel.sales.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.sales.bean.DataEntity;

import java.util.List;

/**
 * Created by shushucn2012 on 2016/10/30.
 */

public class LvRightItemAdapter extends BaseAdapter {

    private List<DataEntity> mEntities;

    private Activity context;
    private LayoutInflater layoutInflater;

    public LvRightItemAdapter(Activity context,List<DataEntity> entities) {
        this.context = context;
        this.mEntities = entities;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mEntities.size();
    }

    @Override
    public DataEntity getItem(int position) {
        return mEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.lv_right_item, parent,false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tvLvRightItem = (TextView) convertView.findViewById(R.id.tv_lv_right_item);

            convertView.setTag(viewHolder);
        }
        initializeViews((DataEntity)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(DataEntity entity, ViewHolder holder) {
        //TODO implement
        holder.tvLvRightItem.setText(String.valueOf(entity.getChar_First()));
    }

    protected class ViewHolder {
        private TextView tvLvRightItem;
    }
}
