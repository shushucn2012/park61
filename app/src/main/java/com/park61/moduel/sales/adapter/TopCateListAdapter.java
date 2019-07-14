package com.park61.moduel.sales.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.GoodsListActivity;
import com.park61.moduel.sales.bean.CateCombine;

public class TopCateListAdapter extends BaseAdapter {

	private List<CateCombine> mList;
	private Context mContext;
	private LayoutInflater factory;

	public TopCateListAdapter(Context _context, List<CateCombine> _list) {
		this.mList = _list;
		this.mContext = _context;
		factory = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public CateCombine getItem(int position) {
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
			convertView = factory.inflate(R.layout.topcatelist_item, null);
			holder = new ViewHolder();
			holder.left_area = convertView.findViewById(R.id.left_area);
			holder.img_cate_left = (ImageView) convertView
					.findViewById(R.id.img_cate_left);
			holder.tv_big_name_left = (TextView) convertView
					.findViewById(R.id.tv_big_name_left);
			holder.tv_small_name_left = (TextView) convertView
					.findViewById(R.id.tv_small_name_left);
			holder.right_area = convertView.findViewById(R.id.right_area);
			holder.img_cate_right = (ImageView) convertView
					.findViewById(R.id.img_cate_right);
			holder.tv_big_name_right = (TextView) convertView
					.findViewById(R.id.tv_big_name_right);
			holder.tv_small_name_right = (TextView) convertView
					.findViewById(R.id.tv_small_name_right);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final CateCombine comb = mList.get(position);
		holder.tv_big_name_left.setText(comb.getCateLeft().getCategoryName());
		holder.tv_small_name_left.setText(comb.getCateLeft()
				.getCategoryDescription());
		ImageManager.getInstance().displayImg(holder.img_cate_left,
				comb.getCateLeft().getCategoryPicUrl());
		holder.left_area.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(mContext, GoodsListActivity.class);
				it.putExtra("cateId", comb.getCateLeft().getId());
				it.putExtra("cateName", comb.getCateLeft().getCategoryName());
				mContext.startActivity(it);
			}
		});
		if (comb.getCateRight() != null) {
			holder.tv_big_name_right.setVisibility(View.VISIBLE);
			holder.tv_small_name_right.setVisibility(View.VISIBLE);
			holder.img_cate_right.setVisibility(View.VISIBLE);
			holder.tv_big_name_right.setText(comb.getCateRight()
					.getCategoryName());
			holder.tv_small_name_right.setText(comb.getCateRight()
					.getCategoryDescription());
			ImageManager.getInstance().displayImg(
					holder.img_cate_right,
					comb.getCateRight().getCategoryPicUrl());
			holder.right_area.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent it = new Intent(mContext, GoodsListActivity.class);
					it.putExtra("cateId", comb.getCateRight().getId());
					it.putExtra("cateName", comb.getCateRight()
							.getCategoryName());
					mContext.startActivity(it);
				}
			});
		} else {
			holder.tv_big_name_right.setVisibility(View.GONE);
			holder.tv_small_name_right.setVisibility(View.GONE);
			holder.img_cate_right.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		View left_area, right_area;
		ImageView img_cate_left, img_cate_right;
		TextView tv_big_name_left, tv_big_name_right;
		TextView tv_small_name_left, tv_small_name_right;
	}
}
