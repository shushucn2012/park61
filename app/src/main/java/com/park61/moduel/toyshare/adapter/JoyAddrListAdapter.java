package com.park61.moduel.toyshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.moduel.toyshare.bean.JoyApplyItem;
import com.park61.moduel.toyshare.bean.TSAddrBean;

import java.util.List;

public class JoyAddrListAdapter extends BaseAdapter {

    private List<TSAddrBean> mList;
    private Context mContext;
    private LayoutInflater factory;

    public JoyAddrListAdapter(Context _context, List<TSAddrBean> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TSAddrBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = factory.inflate(R.layout.toy_addr_list_item, null);
            holder = new ViewHolder();
            holder.browse_name_tv = (TextView) convertView.findViewById(R.id.browse_name_tv);
            holder.browse_number_tv = (TextView) convertView.findViewById(R.id.browse_number_tv);
            holder.browse_address_tv = (TextView) convertView.findViewById(R.id.browse_address_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TSAddrBean item = mList.get(position);
        holder.browse_name_tv.setText(item.getAddress());
        holder.browse_number_tv.setText(item.getPhone());
        String addrStr = "";
        addrStr += item.getProvinceName();
        if (!item.getProvinceName().equals(item.getCityName())) {
            addrStr += item.getCityName();
        }
        if (!item.getCityName().equals(item.getCountyName())) {
            addrStr += item.getCountyName();
        }
        addrStr += item.getTownName() + item.getAddress();
        holder.browse_address_tv.setText(addrStr);
        return convertView;
    }

    class ViewHolder {
        TextView browse_name_tv;
        TextView browse_number_tv;
        TextView browse_address_tv;
    }

}
