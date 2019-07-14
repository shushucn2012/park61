package com.park61.moduel.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.park61.R;
import com.park61.moduel.me.bean.ApplyBabyName;
import java.util.List;


public class ApplyBabyListAdapter extends BaseAdapter {
    private List<ApplyBabyName> mList;
    private Context mContext;
    private LayoutInflater factory;

    public ApplyBabyListAdapter(Context _context, List<ApplyBabyName> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ApplyBabyName getItem(int position) {
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
            convertView = factory.inflate(R.layout.applybaby_list_item, null);
            holder = new ViewHolder();
            holder.tv_baby_name = (TextView) convertView
                    .findViewById(R.id.tv_baby_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ApplyBabyName item = mList.get(position);
        holder.tv_baby_name.setText(item.getVisitorName());
        return convertView;
    }

    class ViewHolder {
        TextView tv_baby_name;
    }

}
