package com.park61.moduel.sales.adapter;

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
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.bean.RecommendGoodsInfo;
import com.park61.moduel.sales.bean.CmsItem;

import java.util.ArrayList;
import java.util.List;

public class StarBabyListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context mContext;
    private List<CmsItem> list;

    public StarBabyListAdapter(Context _context, List<CmsItem> mList) {
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
            convertView = layoutInflater.inflate(R.layout.starbaby_hlistview_item, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CmsItem item = list.get(position);
        ImageManager.getInstance().displayImg(holder.image, item.getLinkPic(), R.drawable.img_default_h);
       /* holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewInitTool.judgeGoWhere(item, mContext);
            }
        });*/
        return convertView;
    }

    class ViewHolder {
        ImageView image;
    }
}
