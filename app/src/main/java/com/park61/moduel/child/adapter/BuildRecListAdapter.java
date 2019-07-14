package com.park61.moduel.child.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.FU;
import com.park61.moduel.child.bean.GrowingRecBean;

/**
 * 体格列表数据适配器
 * 
 * @author super
 */
public class BuildRecListAdapter extends BaseAdapter {

	private List<GrowingRecBean> mList;
	private Context mContext;
	private LayoutInflater factory;

	public BuildRecListAdapter(Context _context, List<GrowingRecBean> _list) {
		this.mList = _list;
		this.mContext = _context;
		factory = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public GrowingRecBean getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Log.out("getView position======" + position);
		if (convertView == null) {
			convertView = factory.inflate(R.layout.buildlist_item, null);
			holder = new ViewHolder();
			holder.tv_rec_date = (TextView) convertView
					.findViewById(R.id.tv_rec_date);
			holder.tv_rec_age = (TextView) convertView
					.findViewById(R.id.tv_rec_age);
			holder.tv_rec_data = (TextView) convertView
					.findViewById(R.id.tv_rec_data);
			holder.tv_rec_state = (TextView) convertView
					.findViewById(R.id.tv_rec_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		GrowingRecBean grb = mList.get(position);
		holder.tv_rec_date.setText(DateTool.L2SEndDay(grb.getGrowingDate()));
		holder.tv_rec_age.setText(grb.getAgeYear() + "岁" + grb.getAgeMonth()
				+ "月" + grb.getAgeDay() + "天");
		String dataStr = "";
		if (grb.getHeight() == null) {
			dataStr = grb.getWeight() + "公斤";
		} else {
			dataStr = FU.strDbFmt(grb.getHeight()) + "厘米";
		}
		holder.tv_rec_data.setText(dataStr);
		if("正常".equals(grb.getState())){
			holder.tv_rec_state.setTextColor(0Xff666666);
		}else{
			holder.tv_rec_state.setTextColor(0Xffff0000);
		}
		holder.tv_rec_state.setText(grb.getState());
		return convertView;
	}

	class ViewHolder {
		TextView tv_rec_date;
		TextView tv_rec_age;
		TextView tv_rec_data;
		TextView tv_rec_state;
	}
}
