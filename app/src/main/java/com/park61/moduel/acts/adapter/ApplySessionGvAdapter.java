package com.park61.moduel.acts.adapter;

import java.util.List;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.FU;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.bean.ActItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplySessionGvAdapter extends BaseAdapter {

	private List<ActItem> mList;
	private Context mContext;
	private int selectedPos = 0;

	public void selectItem(int pos) {
		selectedPos = pos;
		notifyDataSetChanged();
	}

	public int getSelectedPos() {
		return selectedPos;
	}

	public ApplySessionGvAdapter(List<ActItem> _list, Context _context) {
		this.mList = _list;
		this.mContext = _context;
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
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_applysession_item, null);
		}
		TextView tv_first_line = (TextView) convertView.findViewById(R.id.tv_first_line);
		TextView tv_second_line = (TextView) convertView.findViewById(R.id.tv_second_line);
		ImageView img_chosen = (ImageView) convertView.findViewById(R.id.img_chosen);
		ActItem ai = mList.get(position);
		String dateStr = DateTool.L2SByDay2(ai.getActStartTime()) + " - "
				+ DateTool.L2SByDay2(ai.getActEndTime());
		String price = ViewInitTool.getActCurPrice(ai);
		if(ai.isApply()) {
			tv_first_line.setText(FU.zeroToMF(price));
		} else {
			tv_first_line.setText("报名已满");
		}
		tv_second_line.setText(dateStr);
		if (position == selectedPos) {
			convertView.setBackgroundResource(R.drawable.rec_orange_stroke_trans_solid);
			img_chosen.setVisibility(View.VISIBLE);
			tv_first_line.setTextColor(mContext.getResources().getColor(R.color.com_orange));
			tv_second_line.setTextColor(mContext.getResources().getColor(R.color.com_orange));
		} else {
			convertView.setBackgroundResource(R.drawable.rec_gray_stroke_gray_solid);
			img_chosen.setVisibility(View.GONE);
			if (ai.isApply()) {
				tv_first_line.setTextColor(mContext.getResources().getColor(R.color.g333333));
				tv_second_line.setTextColor(mContext.getResources().getColor(R.color.g333333));
			} else {
				tv_first_line.setTextColor(mContext.getResources().getColor(R.color.gaaaaaa));
				tv_second_line.setTextColor(mContext.getResources().getColor(R.color.gaaaaaa));
			}
		}
		return convertView;
	}

}
