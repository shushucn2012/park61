package com.park61.moduel.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.order.bean.LogisticBean;

import java.util.List;

/**
 * 物流详情信息的adapter
 * @author Lucia
 *
 */
public class LogisticListAdapter extends BaseAdapter {
	private List<LogisticBean> mList;
	private Context mContext;
	private LayoutInflater factory;

	public LogisticListAdapter(List<LogisticBean> mList, Context context){
		this.mList = mList;
		this.mContext = context;
		factory = LayoutInflater.from(mContext);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = factory.inflate(R.layout.logistics_info_listitem, null);
			holder = new ViewHolder();
			holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.cicle = convertView.findViewById(R.id.cicle);
			holder.h_line = convertView.findViewById(R.id.h_line);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		LogisticBean item = mList.get(position);
		if(item.getAreaCode()!=null){
			holder.tv_detail.setText("["+item.getAreaName()+"]"+item.getContext());
		}else{
			holder.tv_detail.setText(item.getContext());
		}
		holder.tv_time.setText(item.getTime());
		if(mList.size()<=1){
			holder.h_line.setVisibility(View.GONE);
		}else{
			holder.h_line.setVisibility(View.VISIBLE);
		}
		if(position == 0){
			holder.cicle.setBackgroundResource(R.drawable.circle_shape1);
			holder.tv_detail.setTextColor(mContext.getResources().getColor(R.color.com_orange));
			holder.tv_time.setTextColor(mContext.getResources().getColor(R.color.com_orange));
		}else{
			holder.cicle.setBackgroundResource(R.drawable.circle_shape2);
			holder.tv_detail.setTextColor(mContext.getResources().getColor(R.color.g666666));
			holder.tv_time.setTextColor(mContext.getResources().getColor(R.color.g999999));
		}
		return convertView;		
	}
	
	class ViewHolder {
		TextView tv_detail,tv_time;
		View cicle,h_line;
	}

}
