package com.park61.moduel.sales.adapter;

import java.util.List;

import com.park61.R;
import com.park61.moduel.sales.bean.SubProduct;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ColorGvAdapter extends BaseAdapter {

	private List<SubProduct> mList;
	private Context mContext;
	private int selectedPos = -1;

	public void selectItem(int pos) {
		selectedPos = pos;
		notifyDataSetChanged();
	}

	public int getSelectedPos() {
		return selectedPos;
	}

	public ColorGvAdapter(List<SubProduct> _list, Context _context) {
		this.mList = _list;
		this.mContext = _context;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public SubProduct getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.gridview_child_item, null);
		}
		TextView tv_child_name = (TextView) convertView
				.findViewById(R.id.tv_child_name);

		String name = TextUtils.isEmpty(mList.get(position).getProductColor()) ? mList
				.get(position).getProductSize() : mList.get(position)
				.getProductColor();
		if (TextUtils.isEmpty(name)) {
			name = "通用";
		}
		tv_child_name.setText(name);

		if (position == selectedPos) {
			tv_child_name.setBackgroundResource(R.drawable.btn_apply_selector);
			tv_child_name.setTextColor(mContext.getResources().getColor(
					R.color.gffffff));
		} else {
			tv_child_name.setBackgroundResource(R.drawable.edit_comt_selector);
			tv_child_name.setTextColor(mContext.getResources().getColor(
					R.color.g666666));
		}

		if (!mList.get(position).isFlag()) {
			tv_child_name.setBackgroundResource(R.drawable.edit_search_shape);
			tv_child_name.setTextColor(mContext.getResources().getColor(
					R.color.gffffff));
		}
		return convertView;
	}
}
