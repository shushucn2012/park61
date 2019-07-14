package com.park61.moduel.shophome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.dreamhouse.DreamDetailActivity;
import com.park61.moduel.dreamhouse.bean.DreamFlagBean;
import com.park61.moduel.dreamhouse.bean.DreamItemInfo;

import java.util.List;

public class DateGvAdapter extends BaseAdapter {

    private List<DreamItemInfo> mList;
    private Context mContext;

    public DateGvAdapter(Context _context, List<DreamItemInfo> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public DreamItemInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.shophome_date_item_layout, null);
        }
        ImageView img_date = (ImageView) convertView.findViewById(R.id.img_date);
        ImageManager.getInstance().displayImg(img_date, mList.get(position).getCoverPic());
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        tv_title.setText(mList.get(position).getTitle());
        img_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, DreamDetailActivity.class);
                it.putExtra("requirementId", mList.get(position).getId());
                mContext.startActivity(it);
            }
        });
        return convertView;
    }
}
