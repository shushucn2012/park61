package com.park61.moduel.sales.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.bean.ProductLimit;

import java.util.List;

/**
 * 海筛选商品列表adapter
 */
public class ScreeningGoodsListAdapter extends BaseAdapter {

	private List<ProductLimit> mList;
	private Context mContext;
	private LayoutInflater factory;

	public ScreeningGoodsListAdapter(Context _context, List<ProductLimit> _list) {
		this.mList = _list;
		this.mContext = _context;
		factory = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public ProductLimit getItem(int position) {
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
			convertView = factory.inflate(R.layout.screening_goods_list_item, null);
			holder = new ViewHolder();
			holder.img_goods = (ImageView) convertView
					.findViewById(R.id.img_goods);
			holder.tv_goods_name = (TextView) convertView
					.findViewById(R.id.tv_goods_name);
			holder.tv_goods_price = (TextView) convertView
					.findViewById(R.id.tv_goods_price);
			holder.tv_old_goods_price = (TextView) convertView
					.findViewById(R.id.tv_old_goods_price);
			holder.tv_old_goods_price.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ProductLimit pl = mList.get(position);
		holder.tv_goods_name.setText(pl.getName());
		holder.tv_goods_price.setText(FU.formatPrice(pl.getSalesPrice()));
		holder.tv_old_goods_price.setText("￥" + pl.getOldPrice());
		ImageManager.getInstance().displayImg(holder.img_goods, pl.getImg());
		return convertView;
	}

	class ViewHolder {
		ImageView img_goods;
		TextView tv_goods_name;
		TextView tv_goods_price;
		TextView tv_old_goods_price;
	}
}
