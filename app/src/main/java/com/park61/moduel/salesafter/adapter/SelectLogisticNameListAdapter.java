package com.park61.moduel.salesafter.adapter;



import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;

public class SelectLogisticNameListAdapter extends BaseAdapter {

	private List<String> list;
	private LayoutInflater factory;
	private int selectPosition=0 ;

	public SelectLogisticNameListAdapter(List<String> lists, Context mContext) {
		super();
		this.list = lists;
		factory = LayoutInflater.from(mContext);
	}
	
	public void selectItem(int position){
		selectPosition = position;
		notifyDataSetChanged();
	}
	public int getSelectItem(){
		return selectPosition;		
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = factory.inflate(R.layout.select_logistics_name_item, null);
			holder = new ViewHolder();
			
			holder.tv_select_name = (TextView) convertView.findViewById(R.id.tv_select_name);			
			convertView.setTag(holder);			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}				
		holder.tv_select_name.setText(list.get(position));

		return convertView;
	}

	class ViewHolder {
		TextView tv_select_name;
	}

}
