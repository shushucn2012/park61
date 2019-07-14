package com.park61.moduel.salesafter.adapter;



import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.park61.R;
import com.park61.moduel.salesafter.bean.ApplySalesAfterInfo;
/**
 * 售后退货选择物流的adapter
 * @author Lucia
 *
 */
public class SelectLogisticsListAdapter extends BaseAdapter {

	private List<ApplySalesAfterInfo> lists;
	private int selectPosition=0 ;
	private LayoutInflater factory;
		

	public SelectLogisticsListAdapter(List<ApplySalesAfterInfo> lists, Context mContext) {
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
			convertView = factory.inflate(R.layout.select_logistics_listview_item, null);
			holder = new ViewHolder();		
			holder.img_choice = (ImageView) convertView.findViewById(R.id.img_choice);
//			holder.img_express_icon = (ImageView) convertView.findViewById(R.id.img_express_icon);
			holder.tv_express = (TextView) convertView.findViewById(R.id.tv_express);	
//			holder.tv_express_distance = (TextView) convertView.findViewById(R.id.tv_express_distance);
			convertView.setTag(holder);			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}		
		if (position==selectPosition) {
			holder.img_choice.setBackgroundResource(R.drawable.xuanze_focus);
		}else {
			holder.img_choice.setBackgroundResource(R.drawable.xuanze_default2);
		}
		ApplySalesAfterInfo item = lists.get(position);
		holder.tv_express.setText(item.getDeliveryCompanyName());

		return convertView;
	}

	class ViewHolder {
		TextView tv_express,tv_express_distance;
		ImageView img_express_icon,img_choice;
	}

}
