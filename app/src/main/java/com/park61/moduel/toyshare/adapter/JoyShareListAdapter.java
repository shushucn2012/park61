package com.park61.moduel.toyshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.msg.MsgItem;
import com.park61.moduel.toyshare.bean.JoyShareItem;

import java.util.List;

public class JoyShareListAdapter extends BaseAdapter {

	private List<JoyShareItem> mList;
	private Context mContext;
	private LayoutInflater factory;

	public JoyShareListAdapter(Context _context, List<JoyShareItem> _list) {
		this.mList = _list;
		this.mContext = _context;
		factory = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public JoyShareItem getItem(int position) {
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
			convertView = factory.inflate(R.layout.toyshare_list_item, null);
			holder = new ViewHolder();
			holder.img_toy = (ImageView) convertView.findViewById(R.id.img_toy);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_read_num = (TextView) convertView.findViewById(R.id.tv_read_num);
			holder.tv_get_num = (TextView) convertView.findViewById(R.id.tv_get_num);
			holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		JoyShareItem item = mList.get(position);
		ImageManager.getInstance().displayImg(holder.img_toy, item.getIntroductionImg());
		holder.tv_title.setText(item.getName());
		holder.tv_read_num.setText(item.getUserViewNum()+"");
		holder.tv_get_num.setText(item.getUserParticipateNum()+"人已参加");
		holder.tv_price.setText(item.getSharePrice());
		return convertView;
	}

	class ViewHolder {
		ImageView img_toy;
		TextView tv_title;
		TextView tv_read_num;
		TextView tv_get_num;
		TextView tv_price;
	}

}
