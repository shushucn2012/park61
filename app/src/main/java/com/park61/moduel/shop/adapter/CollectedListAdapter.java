package com.park61.moduel.shop.adapter;

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
import com.park61.moduel.shop.bean.MerchantFocusVO;

public class CollectedListAdapter extends BaseAdapter {

	private List<MerchantFocusVO> mList;
	private Context mContext;
	private LayoutInflater factory;

	public CollectedListAdapter(Context _context, List<MerchantFocusVO> _list) {
		this.mList = _list;
		this.mContext = _context;
		factory = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public MerchantFocusVO getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = factory.inflate(R.layout.collectedlist_item, null);
			holder = new ViewHolder();
			holder.img_shop = (ImageView) convertView
					.findViewById(R.id.img_shop);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_addr = (TextView) convertView.findViewById(R.id.tv_addr);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MerchantFocusVO mf = mList.get(position);
		holder.tv_name.setText(mf.getMerchant().getName());
		holder.tv_addr.setText("");
		String imgUrl = "http://m.61park.cn/images/store_index/shop.jpg";
		ImageManager.getInstance().displayImg(holder.img_shop, imgUrl);
		return convertView;
	}

	class ViewHolder {
		ImageView img_shop;
		TextView tv_name;
		TextView tv_addr;
	}

}
