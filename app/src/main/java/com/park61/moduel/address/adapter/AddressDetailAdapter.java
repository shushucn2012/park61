package com.park61.moduel.address.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.address.bean.AddressDetailItem;

/**
 * 详细收货地址适配器
 *
 * @author Lucia
 */
public class AddressDetailAdapter extends BaseAdapter {
    private UpdateAddress mUpdateAddress;
    private DelAddress mDelAddress;
    private List<AddressDetailItem> mList;
    private int selectPosition = -1;
    private LayoutInflater factory;
    private Context mContext;

    public AddressDetailAdapter(List<AddressDetailItem> mList, Context mContext) {
        super();
        this.mList = mList;
        this.mContext = mContext;
        factory = LayoutInflater.from(mContext);
    }

    public void selectItem(int position) {
        selectPosition = position;
        notifyDataSetChanged();
    }

    public int getSelectItem() {
        return selectPosition;
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
            convertView = factory.inflate(R.layout.address_listview_item, null);
            holder = new ViewHolder();
            holder.name_tv = (TextView) convertView.findViewById(R.id.browse_name_tv);
            holder.number_tv = (TextView) convertView.findViewById(R.id.browse_number_tv);
            holder.addr_tv = (TextView) convertView.findViewById(R.id.browse_address_tv);

            holder.line = (View) convertView.findViewById(R.id.line);
            holder.update_tv = (TextView) convertView.findViewById(R.id.update_tv);
            holder.delete_tv = (TextView) convertView.findViewById(R.id.delete_tv);
            holder.rl = convertView.findViewById(R.id.rl);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.store_name_tv = (TextView) convertView.findViewById(R.id.store_name_tv);
            holder.bottom_blod_line = convertView.findViewById(R.id.bottom_blod_line);
            holder.bottom_nomorl_line = convertView.findViewById(R.id.bottom_nomorl_line);
            holder.tv_default = (TextView) convertView.findViewById(R.id.tv_default);
            holder.area_edit = convertView.findViewById(R.id.area_edit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final AddressDetailItem item = mList.get(position);
        holder.name_tv.setText(item.getGoodReceiverName());
        holder.number_tv.setText(item.getGoodReceiverMobile());
        if (item.getGoodReceiverType() == 1) {
            holder.update_tv.setVisibility(View.GONE);
            holder.line.setVisibility(View.GONE);
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
            if (item.getGoodReceiverTownName() != null) {
                holder.addr_tv.setText(item.getGoodReceiverProvinceName()
                        + item.getGoodReceiverCityName()
                        + item.getGoodReceiverCountyName() + item.getGoodReceiverTownName()
                        + item.getGoodReceiverAddress());
            } else {
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

        holder.area_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mUpdateAddress.updateItem(position);
            }
        });

        if (position == 0 && item.getIsDefault() == 1) {
            holder.bottom_blod_line.setVisibility(View.VISIBLE);
            holder.addr_tv.setTextColor(mContext.getResources().getColor(R.color.g333333));
            holder.tv_default.setVisibility(View.VISIBLE);
        } else {
            holder.bottom_blod_line.setVisibility(View.GONE);
            holder.addr_tv.setTextColor(mContext.getResources().getColor(R.color.g999999));
            holder.tv_default.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView name_tv, number_tv, addr_tv, update_tv, delete_tv,
                tv_province, tv_city, tv_county, detail_address_tv;
        View line, rl, bottom_blod_line, bottom_nomorl_line, area_edit;
        TextView tv, store_name_tv, tv_default;
        // ImageView img_chioce;
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
