package com.park61.moduel.address.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.address.bean.TownBean;

import java.util.List;

/**
 * 选择乡镇Dialog的adapter
 * @author Lucia
 *
 */
public class SelectTownDialogListAdapter extends BaseAdapter {

	private List<TownBean> lists;
	private LayoutInflater factory;
	private int selectPosition=0 ;

	public SelectTownDialogListAdapter(List<TownBean> lists, Context mContext) {
		super();
		this.lists = lists;
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
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = factory.inflate(R.layout.address_select_dialog_item, null);
			holder = new ViewHolder();
			
			holder.tv_select_address = (TextView) convertView.findViewById(R.id.tv_select_address);			
			convertView.setTag(holder);			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
			holder.tv_select_address.setText(
					TextUtils.isEmpty(lists.get(position).getTownName()) ? lists.get(position).getName() : lists.get(position).getTownName());

		return convertView;
	}

	class ViewHolder {
		TextView tv_select_address;
	}

}
