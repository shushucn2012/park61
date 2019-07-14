package com.park61.moduel.shop.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.shop.bean.ActNShopBean;

public class ActNShopListAdapter extends BaseAdapter {

	private List<ActNShopBean> mList;
	private Context mContext;
	private LayoutInflater factory;

	public ActNShopListAdapter(Context _context, List<ActNShopBean> _list) {
		this.mList = _list;
		this.mContext = _context;
		factory = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public ActNShopBean getItem(int position) {
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
			convertView = factory.inflate(R.layout.search_list_item, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.divide_line = convertView.findViewById(R.id.divide_line);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ActNShopBean bi = mList.get(position);
		holder.tv_name.setText(bi.getTitle());
		if (position == mList.size() - 1)
			holder.divide_line.setVisibility(View.GONE);
		return convertView;
	}

	class ViewHolder {
		TextView tv_name;
		View divide_line;
	}

}
