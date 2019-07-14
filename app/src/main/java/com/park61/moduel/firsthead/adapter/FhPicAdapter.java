package com.park61.moduel.firsthead.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.bean.TopicDetailsBean;

import java.util.List;

/**
 * Created by shubei on 2017/6/12.
 */

public class FhPicAdapter extends BaseAdapter {

    private List<TopicDetailsBean.ItemMediaListBean> mList;
    private Context mContext;
    private LayoutInflater factory;

    public FhPicAdapter(Context _context, List<TopicDetailsBean.ItemMediaListBean> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
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
            convertView = factory.inflate(R.layout.fh_pic_list_item, null);
            holder = new ViewHolder();
            holder.img_fh_pic = (ImageView) convertView.findViewById(R.id.img_fh_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageManager.getInstance().displayImg(holder.img_fh_pic, mList.get(position).getMediaUrl());
        return convertView;
    }


    class ViewHolder {
        ImageView img_fh_pic;
    }

}
