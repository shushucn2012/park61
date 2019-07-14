package com.park61.moduel.child.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.moduel.me.bean.MemberButtonVo;

public class PwGrowChildListAdapter extends BaseAdapter {

	private List<BabyItem> mList;
	private Context mContext;
	private LayoutInflater factory;
	private int selectedPos = -1;

	public PwGrowChildListAdapter(Context _context, List<BabyItem> _list,
			int selectedPos) {
		this.mList = _list;
		this.mContext = _context;
		this.selectedPos = selectedPos;
		factory = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public BabyItem getItem(int position) {
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
					.inflate(R.layout.pw_growchild_list_item, null);
			holder = new ViewHolder();
			holder.img_baby = (ImageView) convertView
					.findViewById(R.id.img_baby);
			holder.img_member_type = (ImageView) convertView
					.findViewById(R.id.img_member_type);
			holder.img_select = (ImageView) convertView
					.findViewById(R.id.img_select);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_age = (TextView) convertView.findViewById(R.id.tv_age);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		BabyItem bi = mList.get(position);
		holder.tv_name.setText(bi.getPetName());
		MemberButtonVo mb = bi.getMemberButtonVoList().get(0);
		if(mb.getButtonNamePic()!=null){
			holder.img_member_type.setVisibility(View.VISIBLE);
			ImageManager.getInstance().displayImg(holder.img_member_type, mb.getButtonNamePic());
		}else{
			holder.img_member_type.setVisibility(View.GONE);
		}
//		if ("0".equals(bi.getGhpOrdinary())) {
//			holder.img_member_type.setVisibility(View.VISIBLE);
//			holder.img_member_type.setImageResource(R.drawable.ico_v);
//		} else if ("0".equals(bi.getGhpVip())) {
//			holder.img_member_type.setVisibility(View.VISIBLE);
//			holder.img_member_type.setImageResource(R.drawable.ico_vip);
//		} else {
//			holder.img_member_type.setVisibility(View.GONE);
//		}
		if (position == selectedPos) {
			holder.img_select.setVisibility(View.VISIBLE);
		} else {
			holder.img_select.setVisibility(View.GONE);
		}
		holder.tv_age.setText(bi.getAge());
		ImageManager.getInstance().displayImg(holder.img_baby, bi.getPictureUrl(), R.drawable.headimg_default_img);
		return convertView;
	}

	class ViewHolder {
		ImageView img_baby, img_member_type, img_select;
		TextView tv_name;
		TextView tv_age;
	}

}
