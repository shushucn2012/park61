package com.park61.moduel.child.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.acts.bean.ActItem;

public class BabyScheduleActivityListAdapter extends BaseAdapter {

	private List<ActItem> mList;
	private Context mContext;
	private LayoutInflater factory;

	public BabyScheduleActivityListAdapter(Context _context, List<ActItem> _list) {
		this.mList = _list;
		this.mContext = _context;
		factory = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public ActItem getItem(int position) {
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
			convertView = factory
					.inflate(R.layout.schedule_activity_list_item, null);
			holder = new ViewHolder();
			holder.txtTime = (TextView) convertView
					.findViewById(R.id.time_txt);
			holder.txtAddress = (TextView) convertView
					.findViewById(R.id.address_txt);
			holder.txtDesc = (TextView) convertView
					.findViewById(R.id.desc_txt);
			holder.imgRightMore = (ImageView) convertView
					.findViewById(R.id.right_more_txt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ActItem activityBean = mList.get(position);
		if(activityBean.isbJoin())
		{
			//改变文字颜色
			holder.txtTime.setTextColor(mContext.getResources().getColor(R.color.g999999));
			holder.txtAddress.setTextColor(mContext.getResources().getColor(R.color.g999999));
			holder.txtDesc.setTextColor(mContext.getResources().getColor(R.color.g999999));
			holder.txtTime.setText(activityBean.getActEndTime());
			holder.imgRightMore.setVisibility(View.GONE);
		}
		else
		{
			holder.txtTime.setTextColor(Color.parseColor("#4799ff"));
			holder.txtAddress.setTextColor(Color.parseColor("#333333"));
			holder.txtDesc.setTextColor(Color.parseColor("#666666"));
			holder.imgRightMore.setVisibility(View.VISIBLE);
			holder.txtTime.setText(activityBean.getActStartTime());
		}
		
		holder.txtAddress.setText(activityBean.getActTitle());
		holder.txtDesc.setText(activityBean.getActAddress()+"/"+activityBean.getDistance());
	
		return convertView;
	}
	
	public void setmList(List<ActItem> mList) {
		this.mList = mList;
//		this.notifyDataSetChanged();
	}

	class ViewHolder {
		TextView txtTime;
		TextView txtAddress;
		TextView txtDesc;
		ImageView imgRightMore;
	}

}
