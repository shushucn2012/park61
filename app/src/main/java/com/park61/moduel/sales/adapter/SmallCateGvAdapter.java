package com.park61.moduel.sales.adapter;

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
import com.park61.moduel.sales.bean.ProductCategory;

public class SmallCateGvAdapter extends BaseAdapter {

	private Context context;
	private List<ProductCategory> cateList;

	public SmallCateGvAdapter(Context c, List<ProductCategory> list) {
		this.context = c;
		this.cateList = list;
	}

	@Override
	public int getCount() {
		return cateList.size();
	}

	@Override
	public Object getItem(int position) {
		return cateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.smallcategv_item, null);
			holder = new ViewHolder();
			holder.tv_cate_name = (TextView) convertView.findViewById(R.id.tv_cate_name);
			holder.img_cate = (ImageView) convertView.findViewById(R.id.img_cate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ProductCategory pc = cateList.get(position);
		holder.tv_cate_name.setText(pc.getCategoryName());
		ImageManager.getInstance().displayImg(holder.img_cate, pc.getCategoryPicUrl(), R.drawable.img_default_v);
		return convertView;
	}

	class ViewHolder {
		TextView tv_cate_name;
		ImageView img_cate;
	}
}
