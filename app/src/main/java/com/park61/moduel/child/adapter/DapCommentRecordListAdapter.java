package com.park61.moduel.child.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.moduel.child.bean.DapCommentInfo;

import java.util.ArrayList;

/**
 * Created by HP on 2017/2/27.
 */
public class DapCommentRecordListAdapter extends BaseAdapter {
    private ArrayList<DapCommentInfo> mList;
    private Context mContext;
    private LayoutInflater factory;

    public DapCommentRecordListAdapter(Context _context, ArrayList<DapCommentInfo> _list) {
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = factory.inflate(R.layout.dap_comment_record_list_item, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.line = convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DapCommentInfo item = mList.get(position);
        holder.tv_name.setText(item.getEaItemName());
        holder.tv_date.setText(DateTool.L2SEndDay(item.getCreateDate()));
        if (position == mList.size() - 1) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_name, tv_date;
        View line;
    }
}
