package com.park61.moduel.address.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.address.bean.AddressDetailItem;

public class MyAddressManageAdapter extends BaseAdapter {
    private UpdateAddress mUpdateAddress;
    private DelAddress mDelAddress;
    private List<AddressDetailItem> mList;
    private int selectPosition = 0;
    private LayoutInflater factory;

    public MyAddressManageAdapter(List<AddressDetailItem> mList,
                                  Context mContext) {
        super();
        this.mList = mList;
        factory = LayoutInflater.from(mContext);
    }

    public void selectItem(int position) {
        selectPosition = position;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = factory.inflate(
                    R.layout.address_manage_listview_item, null);
            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView
                    .findViewById(R.id.browse_name_tv);
            holder.number_tv = (TextView) convertView
                    .findViewById(R.id.browse_number_tv);
            holder.addr_tv = (TextView) convertView
                    .findViewById(R.id.browse_address_tv);
            holder.line = convertView.findViewById(R.id.line);
            holder.split_line = convertView.findViewById(R.id.split_line);
            holder.default_addr = (TextView) convertView
                    .findViewById(R.id.default_addr);
            holder.update_tv = (TextView) convertView
                    .findViewById(R.id.update_tv);
            holder.delete_tv = (TextView) convertView
                    .findViewById(R.id.delete_tv);
            holder.img_chioce = (ImageView) convertView
                    .findViewById(R.id.img_chioce);
            holder.rl = convertView.findViewById(R.id.rl);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.store_name_tv = (TextView) convertView.findViewById(R.id.store_name_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AddressDetailItem item = mList.get(position);
        if (position == 0) {
            holder.split_line.setVisibility(View.GONE);
        } else {
            holder.split_line.setVisibility(View.VISIBLE);
        }
        holder.name_tv.setText(item.getGoodReceiverName());
        holder.number_tv.setText(item.getGoodReceiverMobile());
        if (item.getGoodReceiverType() == 1) {
            holder.update_tv.setVisibility(View.GONE);
            holder.line.setVisibility(View.GONE);
            holder.default_addr.setVisibility(View.GONE);
            holder.img_chioce.setVisibility(View.GONE);
            holder.rl.setVisibility(View.VISIBLE);
            holder.store_name_tv.setText(item.getStoreName() + "(联系电话:"
                    + item.getStorePhone() + ")");
            holder.addr_tv.setText(item.getGoodReceiverProvinceName()
                    + item.getGoodReceiverCityName()
                    + item.getGoodReceiverCountyName()
                    + item.getGoodReceiverAddress());
        } else {
            holder.rl.setVisibility(View.GONE);
            holder.update_tv.setVisibility(View.VISIBLE);
            holder.line.setVisibility(View.VISIBLE);
            if(item.getGoodReceiverTownName()!=null){
                holder.addr_tv.setText(item.getGoodReceiverProvinceName()
                        + item.getGoodReceiverCityName()
                        + item.getGoodReceiverCountyName()+item.getGoodReceiverTownName()
                        + item.getGoodReceiverAddress());
            }else{
                holder.addr_tv.setText(item.getGoodReceiverProvinceName()
                        + item.getGoodReceiverCityName()
                        + item.getGoodReceiverCountyName()
                        + item.getGoodReceiverAddress());
            }
        }

        // 删除地址条目
        holder.delete_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelAddress.delItem(position);
            }
        });
        // 修改地址
        holder.update_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mUpdateAddress.updateItem(position);
            }
        });

        if (position == selectPosition) {
            holder.img_chioce.setImageResource(R.drawable.xuanze_focus);
        } else {
            holder.img_chioce.setImageResource(R.drawable.xuanze_default2);
        }

        return convertView;
    }

    class ViewHolder {
        TextView name_tv, number_tv, default_addr, update_tv, addr_tv,
                delete_tv, tv_province, tv_city, tv_county, detail_address_tv;
        ImageView img_chioce;
        View line, rl, split_line;
        TextView tv, store_name_tv;
    }

    // 编辑地址的接口
    public interface UpdateAddress {
        public void updateItem(int position);
    }

    // 编辑地址接口实例化
    public void setUpdateAddress(UpdateAddress lsner) {
        mUpdateAddress = lsner;
    }

    // 删除地址的接口
    public interface DelAddress {
        public void delItem(int position);
    }

    // 删除地址接口实例化
    public void setDelAddress(DelAddress lsner) {
        mDelAddress = lsner;
    }
}
