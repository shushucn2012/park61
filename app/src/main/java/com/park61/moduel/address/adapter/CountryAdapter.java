package com.park61.moduel.address.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.shop.bean.DistrictStore;

public class CountryAdapter extends BaseAdapter {
	private Context context;
	private List<DistrictStore> cateList;
	// private int selectedPos = 0;
	private int selectedPos;

	public void selectItem(int pos) {
		selectedPos = pos;
		notifyDataSetChanged();
	}

	public CountryAdapter(Context c, List<DistrictStore> list) {
		this.context = c;
		this.cateList = list;
	}

	@Override
	public int getCount() {
		return cateList.size();
	}

	@Override
	public DistrictStore getItem(int position) {
		return cateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.county_listview_item, null);
			holder = new ViewHolder();
			holder.bigcate_item_root = convertView
					.findViewById(R.id.bigcate_item_root);
			holder.tv_cate_name = (TextView) convertView
					.findViewById(R.id.tv_cate_name);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DistrictStore ds = cateList.get(position);
		String name = TextUtils.isEmpty(ds.getAreaName()) ? ds
				.getDistrictName() : ds.getAreaName();
		holder.tv_cate_name.setText(name);
		holder.tv_num.setText(ds.getShopCount() + "");

		if (position == selectedPos) {
			// holder.bigcate_item_root.setBackgroundColor(context.getResources()
			// .getColor(R.color.gffffff));
			holder.tv_cate_name.setTextColor(context.getResources().getColor(
					R.color.com_orange));
			holder.tv_num.setTextColor(context.getResources().getColor(
					R.color.com_orange));
		} else {
			// holder.bigcate_item_root.setBackgroundColor(context.getResources()
			// .getColor(R.color.bg_color));
			holder.tv_cate_name.setTextColor(context.getResources().getColor(
					R.color.g333333));
			holder.tv_num.setTextColor(context.getResources().getColor(
					R.color.g666666));
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_cate_name, tv_num;
		View bigcate_item_root;
	}

}
