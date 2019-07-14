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
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.bean.ProductLimit;

import java.util.List;

/**
 * 海外优品商品列表adapter
 */
public class OverseasGoodsListAdapter extends BaseAdapter {

	private List<ProductLimit> mList;
	private Context mContext;
	private LayoutInflater factory;
	private final int TYPE_HAS_BANNER = 1;
	private final int TYPE_NO_BANNER = 0;

	public OverseasGoodsListAdapter(Context _context, List<ProductLimit> _list) {
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
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if(position>0){
			return TYPE_NO_BANNER;
		}
		return TYPE_HAS_BANNER;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			convertView = factory.inflate(R.layout.overseas_goods_list_item, null);
			holder = new ViewHolder();
			holder.img_goods = (ImageView) convertView.findViewById(R.id.img_goods);
			holder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
			holder.tv_goods_price = (TextView) convertView.findViewById(R.id.tv_goods_price);
			holder.tv_old_goods_price = (TextView) convertView.findViewById(R.id.tv_old_goods_price);
			holder.tv_old_goods_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			holder.btn_gobuy = (Button) convertView.findViewById(R.id.btn_gobuy);
			holder.banner_area = convertView.findViewById(R.id.banner_area);
			holder.area_goods = convertView.findViewById(R.id.area_goods);
			switch (type) {
				case TYPE_HAS_BANNER:
					holder.banner_area.setVisibility(View.VISIBLE);
					convertView.setTag(holder);
					break;
				case TYPE_NO_BANNER:
					holder.banner_area.setVisibility(View.GONE);
					convertView.setTag(holder);
					break;
			}
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ProductLimit pl = mList.get(position);
		holder.tv_goods_name.setText(pl.getName());
		holder.tv_goods_price.setText(FU.formatPrice(pl.getSalesPrice()));
		holder.tv_old_goods_price.setText("￥" + pl.getOldPrice());
		ImageManager.getInstance().displayImg(holder.img_goods, pl.getImg());
		holder.btn_gobuy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(mContext, GoodsDetailsActivity.class);
				it.putExtra("goodsId", pl.getPmInfoid());
				it.putExtra("goodsName", pl.getName());
				it.putExtra("goodsPrice", pl.getSalesPrice() + "");
				it.putExtra("goodsOldPrice", pl.getOldPrice() + "");
				it.putExtra("promotionId", pl.getPromotionId());
				mContext.startActivity(it);
			}
		});
		holder.area_goods.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent it = new Intent(mContext, GoodsDetailsActivity.class);
				it.putExtra("goodsId", pl.getPmInfoid());
				it.putExtra("goodsName", pl.getName());
				it.putExtra("goodsPrice", pl.getSalesPrice() + "");
				it.putExtra("goodsOldPrice", pl.getOldPrice() + "");
				it.putExtra("promotionId", pl.getPromotionId());
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
		Button btn_gobuy;
		View banner_area,area_goods;
	}
}
