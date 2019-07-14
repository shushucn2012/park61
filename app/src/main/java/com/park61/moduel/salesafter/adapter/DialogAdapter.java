package com.park61.moduel.salesafter.adapter;




import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.salesafter.bean.GrfReason;

/**
 * 退货原因及售后类型选择Dialog的adapter
 * @author Lucia
 *
 */
public class DialogAdapter extends BaseAdapter {
	private ArrayList<GrfReason> reasonList;
	private int selectPosition;
	private LayoutInflater factory;
		

	public DialogAdapter(int selectPos,ArrayList<GrfReason> reasonList, Context mContext) {
		super();
		this.selectPosition = selectPos;
		this.reasonList = reasonList;
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
		return reasonList.size();
	}

	@Override
	public Object getItem(int position) {
		return reasonList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = factory.inflate(R.layout.select_reason_dialog_item, null);
			holder = new ViewHolder();
			
			holder.img_chioce = (ImageView) convertView.findViewById(R.id.img_chioce);
			holder.reason_value = (TextView) convertView.findViewById(R.id.reason_value);			
			convertView.setTag(holder);			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}		
		if (position==selectPosition) {
			holder.img_chioce.setBackgroundResource(R.drawable.xuanze_focus);
		}else {
			holder.img_chioce.setBackgroundResource(R.drawable.xuanze_default2);
		}
		GrfReason item = reasonList.get(position);
		holder.reason_value.setText(item.getReason());

		return convertView;
	}

	class ViewHolder {
		TextView reason_value;
		ImageView img_chioce;
	}

}
