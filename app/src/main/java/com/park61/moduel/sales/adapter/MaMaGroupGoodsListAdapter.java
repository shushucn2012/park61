package com.park61.moduel.sales.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
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
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.bean.ProductLimit;

import java.util.List;

/**
 * 妈妈团商品列表adapter
 */
public class MaMaGroupGoodsListAdapter extends BaseAdapter {

	private List<ProductLimit> mList;
	private Context mContext;
	private LayoutInflater factory;
	private final int TYPE_HAS_BANNER = 1;
	private final int TYPE_NO_BANNER = 0;

	public MaMaGroupGoodsListAdapter(Context _context, List<ProductLimit> _list) {
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
			convertView = factory.inflate(R.layout.mamagroup_goods_list_item, null);
			holder = new ViewHolder();
			holder.img_goods = (ImageView) convertView.findViewById(R.id.img_goods);
			holder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
			holder.tv_goods_price = (TextView) convertView.findViewById(R.id.tv_goods_price);
			holder.tv_old_goods_price = (TextView) convertView.findViewById(R.id.tv_old_goods_price);
			holder.tv_old_goods_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			holder.tv_total_num = (TextView) convertView.findViewById(R.id.tv_total_num);
			holder.tv_cover = (TextView) convertView.findViewById(R.id.tv_cover);
			holder.seekbar = (SeekBar) convertView.findViewById(R.id.seekbar);
			holder.btn_gobuy = (Button) convertView.findViewById(R.id.btn_gobuy);
			holder.img_banner = (ImageView) convertView.findViewById(R.id.img_banner);
			holder.area_goods = convertView.findViewById(R.id.area_goods);
			switch (type) {
				case TYPE_HAS_BANNER:
					holder.img_banner.setVisibility(View.VISIBLE);
					convertView.setTag(holder);
					break;
				case TYPE_NO_BANNER:
					holder.img_banner.setVisibility(View.GONE);
					convertView.setTag(holder);
					break;
			}
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.seekbar.setEnabled(false);
		final ProductLimit pl = mList.get(position);
		holder.tv_goods_name.setText(pl.getName());
		holder.tv_goods_price.setText(FU.formatPrice(pl.getSalesPrice()));
		holder.tv_old_goods_price.setText("￥" + pl.getOldPrice());
		String num = "0";
		if(!TextUtils.isEmpty(pl.getNum().toString())){
			num = pl.getNum().toString();
		}
		String salesNum = pl.getSalesNum();
		if(num.equals(salesNum)){
			holder.btn_gobuy.setText("团完了");
			holder.btn_gobuy.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_sec_finish));
			holder.tv_cover.setVisibility(View.VISIBLE);
		}else{
			holder.btn_gobuy.setText("马上团");
			holder.btn_gobuy.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_apply_selector));
			holder.tv_cover.setVisibility(View.GONE);
		}

		if (pl.getNum() != null) {
			holder.tv_total_num.setText("限量" + pl.getNum() + "件");
			holder.seekbar.setMax(pl.getNum().intValue());
		}
		holder.seekbar.setProgress(FU.paseInt(pl.getSalesNum()));
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
		return convertView;
	}

	class ViewHolder {
		ImageView img_goods;
		TextView tv_goods_name;
		TextView tv_goods_price;
		TextView tv_old_goods_price;
		TextView tv_total_num;
		TextView tv_cover;
		SeekBar seekbar;
		Button btn_gobuy;
		ImageView img_banner;
		View area_goods;
	}
}
