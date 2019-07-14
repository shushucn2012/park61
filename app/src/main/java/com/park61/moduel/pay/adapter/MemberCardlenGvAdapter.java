package com.park61.moduel.pay.adapter;

import java.util.List;

import com.park61.R;
import com.park61.moduel.pay.bean.MemberCardLengthVO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MemberCardlenGvAdapter extends BaseAdapter {

	private List<MemberCardLengthVO> mList;
	private Context mContext;
	private int selectedPos = 0;

	public void selectItem(int pos) {
		selectedPos = pos;
		notifyDataSetChanged();
	}

	public int getSelectedPos() {
		return selectedPos;
	}

	public MemberCardlenGvAdapter(List<MemberCardLengthVO> _list, Context _context) {
		this.mList = _list;
		this.mContext = _context;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public MemberCardLengthVO getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_child_item, null);
		}
		TextView tv_child_name = (TextView) convertView.findViewById(R.id.tv_child_name);
		tv_child_name.setText(mList.get(position).getCardLengthShowName());
		if (position == selectedPos) {
			tv_child_name.setBackgroundResource(R.drawable.rec_orange_stroke_trans_solid);
			tv_child_name.setTextColor(mContext.getResources().getColor(R.color.com_orange));
		} else {
			tv_child_name.setBackgroundResource(R.drawable.rec_gray_stroke_trans_solid);
			tv_child_name.setTextColor(mContext.getResources().getColor(R.color.g333333));
		}
		return convertView;
	}

}
