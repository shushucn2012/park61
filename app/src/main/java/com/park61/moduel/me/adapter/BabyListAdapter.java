package com.park61.moduel.me.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.moduel.me.bean.MemberButtonVo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BabyListAdapter extends BaseAdapter {

	private List<BabyItem> mList;
	private Context mContext;
	private LayoutInflater factory;

	public BabyListAdapter(Context _context, List<BabyItem> _list) {
		this.mList = _list;
		this.mContext = _context;
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
			convertView = factory.inflate(R.layout.babylist_item, null);
			holder = new ViewHolder();
			holder.img_baby = (ImageView) convertView.findViewById(R.id.img_baby);
			holder.img_member_type = (ImageView) convertView.findViewById(R.id.img_member_type);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_age = (TextView) convertView.findViewById(R.id.tv_age);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		BabyItem bi = mList.get(position);
		MemberButtonVo mb = bi.getMemberButtonVoList().get(0);
		holder.tv_name.setText(bi.getPetName());
		if(!TextUtils.isEmpty(mb.getButtonNamePic())) {
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
		if (!TextUtils.isEmpty(bi.getBirthday())) {
			Date birthday = new Date(Long.parseLong(bi.getBirthday()));
			Date nowDay = new Date();
			Calendar datebegin = Calendar.getInstance();
			Calendar dateend = Calendar.getInstance();
			datebegin.setTime(birthday);
			dateend.setTime(nowDay);
			int year = DateTool.getYear(datebegin, dateend);
			int month = DateTool.getMonth(datebegin, dateend);
			holder.tv_age.setText(year + "岁" + month + "个月");
		}
		if (!TextUtils.isEmpty(bi.getPictureUrl())) {
			DisplayImageOptions moptions = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.headimg_default_img)
					.showImageOnLoading(R.drawable.headimg_default_img)
					.showImageOnFail(R.drawable.headimg_default_img).cacheInMemory(true)
					.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
					.build();
			ImageManager.getInstance().displayImg(holder.img_baby,bi.getPictureUrl());
		}
		return convertView;
	}

	class ViewHolder {
		ImageView img_baby, img_member_type;
		TextView tv_name;
		TextView tv_age;
	}

}
