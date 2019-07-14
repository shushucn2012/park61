package com.park61.moduel.dreamhouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.dreamhouse.bean.DreamFlagBean;

import java.util.List;

public class DreamFlagGvAdapter extends BaseAdapter {

    private List<DreamFlagBean> mList;
    private Context mContext;
    private int selectedPos = -1;

    public void selectItem(int pos) {
        mList.get(pos).setChosen(true);
        notifyDataSetChanged();
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public DreamFlagGvAdapter(List<DreamFlagBean> _list, Context _context) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public DreamFlagBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_child_item, null);
        }
        View root = convertView.findViewById(R.id.root);
        root.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        TextView tv_child_name = (TextView) convertView.findViewById(R.id.tv_child_name);
        tv_child_name.setText(mList.get(position).getName());
        ViewGroup.LayoutParams layoutParams = tv_child_name.getLayoutParams();
        layoutParams.height = DevAttr.dip2px(mContext, 30);
        if (mList.get(position).isChosen()) {
            tv_child_name.setBackgroundResource(R.drawable.shape_dream_flag);
            tv_child_name.setTextColor(mContext.getResources().getColor(R.color.com_red));
        } else {
            tv_child_name.setBackgroundResource(R.drawable.shape_dream_flag);
            tv_child_name.setTextColor(mContext.getResources().getColor(R.color.g333333));
        }
        return convertView;
    }
}
