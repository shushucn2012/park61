package com.park61.moduel.msg.adapter;

import java.util.List;

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

public class MsgListAdapter extends BaseAdapter {

	private List<MsgItem> mList;
	private Context mContext;
	private LayoutInflater factory;

	public MsgListAdapter(Context _context, List<MsgItem> _list) {
		this.mList = _list;
		this.mContext = _context;
		factory = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public MsgItem getItem(int position) {
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
			convertView = factory.inflate(R.layout.msglist_item, null);
			holder = new ViewHolder();
			holder.img_user = (ImageView) convertView.findViewById(R.id.img_user);
			holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MsgItem mi = mList.get(position);
		if (mi.getType() == 0 || mi.getType() == 4 || mi.getType() == 5
				|| mi.getType() == 6 || mi.getType() == 8 || mi.getType() == 9) {
			holder.tv_username.setText("61区官方");
			holder.img_user.setImageResource(R.drawable.icon_qujiawan);
		} else {
			holder.tv_username.setText(mi.getSenderName() + " 回复了我");
			ImageManager.getInstance().displayImg(holder.img_user, mi.getSenderPicture());
		}
		holder.tv_content.setText(mi.getContent());
		holder.tv_time.setText(DateTool.L2S(mi.getTime()));
		View bottom_line = convertView.findViewById(R.id.bottom_line);
		if (position == getCount() - 1) {
			bottom_line.setVisibility(View.GONE);
		} else {
			bottom_line.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView img_user;
		TextView tv_username;
		TextView tv_content;
		TextView tv_time;
	}

}
