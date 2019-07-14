package com.park61.moduel.acts.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.shop.bean.ShopBean;

public class ActDetailsChangeShopListAdapter extends BaseAdapter {

	private List<ShopBean> mList;
	private Context mContext;
	private LayoutInflater factory;
	private int selectedPos = 0;

	public ActDetailsChangeShopListAdapter(Context _context,
			List<ShopBean> _list) {
		this.mList = _list;
		this.mContext = _context;
		factory = LayoutInflater.from(mContext);
	}

	public void selectItem(int pos) {
		selectedPos = pos;
		notifyDataSetChanged();
	}

	public int getSelectItem() {
		return selectedPos;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public ShopBean getItem(int position) {
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
			convertView = factory.inflate(R.layout.selftakepoint_listview_item,
					null);
			holder = new ViewHolder();
			holder.img_chioce = (ImageView) convertView
					.findViewById(R.id.img_chioce);
			holder.tv_shopname = (TextView) convertView
					.findViewById(R.id.tv_shopname);
			holder.tv_distance = (TextView) convertView
					.findViewById(R.id.tv_distance);
			holder.tv_addr = (TextView) convertView.findViewById(R.id.tv_addr);
			holder.tv_phone = (TextView) convertView
					.findViewById(R.id.tv_phone);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ShopBean item = mList.get(position);
		holder.tv_shopname.setText(item.getStoreName());
		holder.tv_addr.setText("地址：" + item.getAddress());
		holder.tv_phone.setText("电话：" + item.getPhone());
		holder.tv_distance.setText("距您" + item.getKiloDistance() + "km");
		if (position == selectedPos) {
			holder.img_chioce.setBackgroundResource(R.drawable.xuanze_focus);
		} else {
			holder.img_chioce.setBackgroundResource(R.drawable.xuanze_default2);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView img_chioce;
		TextView tv_shopname;
		TextView tv_distance;
		TextView tv_addr;
		TextView tv_phone;
	}
}
