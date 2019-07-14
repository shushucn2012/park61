package com.park61.moduel.sales.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.OrderConfirmV3Activity;
import com.park61.moduel.sales.bean.ProductLimit;

import java.util.List;

public class SkListAdapter extends BaseAdapter {

	private List<ProductLimit> mList;
	private Context mContext;
	private LayoutInflater factory;

	public SkListAdapter(Context _context, List<ProductLimit> _list) {
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
			convertView = factory.inflate(R.layout.sklist_item, null);
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
			holder.tv_total_num = (TextView) convertView
					.findViewById(R.id.tv_total_num);
			holder.seekbar = (SeekBar) convertView.findViewById(R.id.seekbar);
			holder.btn_gobuy = (Button) convertView
					.findViewById(R.id.btn_gobuy);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.seekbar.setEnabled(false);
		final ProductLimit pl = mList.get(position);
		holder.tv_goods_name.setText(pl.getName());
		holder.tv_goods_price.setText("￥" + pl.getSalesPrice());
		holder.tv_old_goods_price.setText("￥" + pl.getOldPrice());
		if (pl.getNum() != null) {
			holder.tv_total_num.setText("限量" + pl.getNum() + "件");
			holder.seekbar.setMax(pl.getNum().intValue());
		}
		holder.seekbar.setProgress(FU.paseInt(pl.getSalesNum()));
		ImageManager.getInstance().displayImg(holder.img_goods, pl.getImg());
		holder.btn_gobuy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent it = new Intent(mContext, OrderConfirmV2Activity.class);
				Intent it = new Intent(mContext, OrderConfirmV3Activity.class);
				it.putExtra("pmInfoId", pl.getPmInfoid());
				it.putExtra("buyNum", 1);
				it.putExtra("promotionId", pl.getPromotionId());
				it.putExtra("from", "detail");
				mContext.startActivity(it);
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView img_goods;
		TextView tv_goods_name;
		TextView tv_goods_price;
		TextView tv_old_goods_price;
		TextView tv_total_num;
		SeekBar seekbar;
		Button btn_gobuy;
	}
}
