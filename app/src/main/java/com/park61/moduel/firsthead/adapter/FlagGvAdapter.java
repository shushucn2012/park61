package com.park61.moduel.firsthead.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.sales.bean.TabBean;

import java.util.List;

public class FlagGvAdapter extends BaseAdapter {

    private List<TabBean> mList;
    private Context mContext;
    private int selectedPos = -1;

    public FlagGvAdapter(List<TabBean> _list, Context _context) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TabBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_role_item, null);
        }
        TextView tv_child_name = (TextView) convertView.findViewById(R.id.tv_child_name);
        tv_child_name.setText(mList.get(position).getName());
        ViewGroup.LayoutParams layoutParams = tv_child_name.getLayoutParams();
        layoutParams.height = DevAttr.dip2px(mContext, 48);
        if (mList.get(position).isChosen()) {
            Log.out("mList.get(position).isChosen()==========================" + position);
            tv_child_name.setBackgroundResource(R.drawable.rec_white_stroke_white_solid_corner5);
            tv_child_name.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
        } else {
            tv_child_name.setBackgroundResource(R.drawable.rec_white_stroke_white_solid_corner5);
            tv_child_name.setTextColor(mContext.getResources().getColor(R.color.g333333));
        }
        return convertView;
    }
}
