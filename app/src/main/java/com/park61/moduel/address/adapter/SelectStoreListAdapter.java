package com.park61.moduel.address.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.CommonMethod;
import com.park61.moduel.address.bean.StoreVO;

public class SelectStoreListAdapter extends BaseAdapter {

	private List<StoreVO> mList;
	private int selectPosition;
	private LayoutInflater factory;

	public SelectStoreListAdapter(int selectPos,List<StoreVO> mList, Context mContext) {
		this.mList = mList;
		this.selectPosition = selectPos;
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
			convertView = factory.inflate(R.layout.select_store_listview_item,
					null);
			holder = new ViewHolder();
			holder.browse_stote_name_tv = (TextView) convertView
					.findViewById(R.id.browse_stote_name_tv);
			holder.phone_tv = (TextView) convertView
					.findViewById(R.id.phone_tv);
			holder.address_tv = (TextView) convertView
					.findViewById(R.id.address_tv);
			holder.distance_tv = (TextView) convertView
					.findViewById(R.id.distance_tv);
			holder.img_chioce = (ImageView) convertView
					.findViewById(R.id.img_chioce);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final StoreVO item = mList.get(position);
		holder.browse_stote_name_tv.setText("61区"+item.getStoreName());
		holder.phone_tv.setText("电话："+item.getPhone());
		holder.address_tv.setText("地址："+item.getAddress());
		holder.distance_tv.setText("距您"+CommonMethod.formatM2Km(item
				.getDistanceNum() + "")
				+ "km");
		if (position == selectPosition) {
			holder.img_chioce.setImageResource(R.drawable.xuanze_focus);
		} else {
			holder.img_chioce.setImageResource(R.drawable.xuanze_default2);
		}
		return convertView;
	}

	class ViewHolder {
		TextView browse_stote_name_tv, phone_tv, address_tv, distance_tv;
		ImageView img_chioce;
	}

}
