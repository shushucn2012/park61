package com.park61.moduel.openmember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.moduel.coupon.bean.CouponUser;

import java.util.ArrayList;

public class MemberCardCouponListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater factory;
	private ArrayList<CouponUser> mlist;

	public MemberCardCouponListAdapter(Context _context, ArrayList<CouponUser> _list) {
		this.mContext = _context;
		this.mlist = _list;
		factory = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = factory.inflate(R.layout.selectcoupon_listview_item, null);
			holder = new ViewHolder();
			holder.img_chioce = (ImageView) convertView.findViewById(R.id.img_chioce);
			holder.amount_tv = (TextView) convertView
					.findViewById(R.id.amount_tv);
			holder.unit_tv = (TextView) convertView.findViewById(R.id.unit_tv);
			holder.coupon_type_tv = (TextView) convertView
					.findViewById(R.id.coupon_type_tv);
			holder.discount_tv = (TextView) convertView
					.findViewById(R.id.discount_tv);
			holder.limittime_tv = (TextView) convertView
					.findViewById(R.id.limittime_tv);
			holder.amount_area = convertView.findViewById(R.id.amount_area);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CouponUser item = mlist.get(position);
		holder.coupon_type_tv.setText(item.getCoupon().getTypeName());
		holder.amount_tv.setText(item.getCoupon().getRuleValue2()+"");
		holder.unit_tv.setText("元");
		holder.discount_tv.setText(item.getCoupon().getTitle());
		holder.discount_tv.setTextColor(mContext.getResources().getColor(R.color.com_orange));
		if (item.getCoupon().getLimitType() == 1) {
			holder.limittime_tv.setText("有效期："
					+ DateTool.L2SEndDay(item.getCoupon().getLimitStart())
					+ "至" + DateTool.L2SEndDay(item.getCoupon().getLimitEnd()));
		} else if (item.getCoupon().getLimitType() == 2) {
			holder.limittime_tv.setText("有效期：" + item.getCoupon().getLimitDay()
					+ "天");
		} else if (item.getCoupon().getLimitType() == 3) {
			holder.limittime_tv.setText("不限期");
		}
		if (item.getCoupon().getType()==1) {
			holder.coupon_type_tv.setText("体验券");
		}else if (item.getCoupon().getType()==2) {
			holder.coupon_type_tv.setText("游戏兑换券");
		}else if (item.getCoupon().getType() == 3) {
			holder.coupon_type_tv.setText("折扣券");
		}else if (item.getCoupon().getType() == 4) {
			holder.coupon_type_tv.setText("代金券");
		}else if (item.getCoupon().getType() == 5) {
			holder.coupon_type_tv.setText("礼品兑换券");
		}
		if(mlist.get(position).isChosen()){
			holder.img_chioce.setImageResource(R.drawable.xuanze_focus);
		}else{
			holder.img_chioce.setImageResource(R.drawable.xuanze_default);
		}
		return convertView;
	}

	class ViewHolder {
		TextView amount_tv, unit_tv, coupon_type_tv, discount_tv, limittime_tv;
		ImageView img_chioce;
		View amount_area;
	}
}
