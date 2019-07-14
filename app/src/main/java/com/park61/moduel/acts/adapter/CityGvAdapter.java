package com.park61.moduel.acts.adapter;

import java.util.List;

import com.park61.R;
import com.park61.moduel.acts.bean.CityBean;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityGvAdapter extends BaseAdapter {

	private List<CityBean> mList;
	private Context mContext;

	public CityGvAdapter(List<CityBean> _list, Context _context) {
		this.mList = _list;
		this.mContext = _context;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public CityBean getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_city_item, null);
		}
		TextView tv_now_city = (TextView) convertView.findViewById(R.id.tv_now_city);
		tv_now_city.setText(mList.get(position).getCityName());
		return convertView;
	}
}
