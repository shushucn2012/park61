package com.park61.moduel.sales.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.bean.Product;
import com.park61.moduel.sales.bean.TradeOrder;
import com.park61.moduel.sales.bean.TradeOrderItem;

public class GoodsItemListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private Context mContext;
	private ArrayList<TradeOrder> mList;

	public GoodsItemListAdapter(Context _context, ArrayList<TradeOrder> mList) {
		this.mList = mList;
		this.mContext = _context;
		layoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
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
			convertView = layoutInflater.inflate(R.layout.bags_item, null);
			holder = new ViewHolder();
			holder.tv_bags_name = (TextView) convertView
					.findViewById(R.id.tv_bags_name);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.edit_mark = (EditText) convertView
					.findViewById(R.id.edit_mark);
			holder.lay_goods = (LinearLayout) convertView
					.findViewById(R.id.lay_goods);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Log.out("mList.size------" + mList.size());
		TradeOrder info = mList.get(position);
		holder.tv_bags_name.setText("包裹" + (position + 1));
		holder.tv_num.setText(info.getOrderItemNum() + "");
		holder.lay_goods.removeAllViews();
		for (int i = 0; i < info.getOrderItemList().size(); i++) {
			TradeOrderItem item = mList.get(position).getOrderItemList().get(i);
			View view_goods_item = layoutInflater.inflate(
					R.layout.confirm_order_item, null);
			ImageView icon = (ImageView) view_goods_item
					.findViewById(R.id.icon);
			TextView title = (TextView) view_goods_item
					.findViewById(R.id.title);
			TextView cur_price = (TextView) view_goods_item
					.findViewById(R.id.cur_price);
			TextView src_price = (TextView) view_goods_item
					.findViewById(R.id.src_price);
			TextView num = (TextView) view_goods_item.findViewById(R.id.num);
			TextView tv_color = (TextView) view_goods_item
					.findViewById(R.id.tv_color);
			TextView tv_size = (TextView) view_goods_item
					.findViewById(R.id.tv_size);

			Product product = item.getProductMerchant().getPmInfo()
					.getProduct();
			ImageManager.getInstance().displayImg(icon, product.getProductPicUrl());
			title.setText(product.getProductCname());
			src_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 设置中划线
			if(info.getIsFightGroup()==null){//不拼团
				cur_price.setText(FU.formatPrice(item.getProductMerchant().getPmPrice()
						.getCurrentUnifyPrice()));
			}else{//info.getIsFightGroup()不为空时为拼团
				cur_price.setText(FU.formatPrice(item.getProductMerchant().getPmPrice()
						.getFightGroupPrice()));
			}
//			cur_price.setText("￥"
//					+ item.getProductMerchant().getPmPrice()
//							.getCurrentUnifyPrice() + "");
			// 如果有促销价格，就用促销价格
			if (item.getProductMerchant().getPmPrice().getPromPrice() != null
					&&item.getProductMerchant().getPmPrice().getPromType()!=0) {
				cur_price.setText(FU.formatPrice(item.getProductMerchant().getPmPrice()
						.getPromPrice()));
			}
			src_price.setText("￥"
					+ item.getProductMerchant().getPmPrice().getMarketPrice()
					+ "");
			num.setText("X" + item.getOrderItemNum() + "");
			tv_color.setText(product.getProductColor());
			tv_size.setText(product.getProductSize());
			if (TextUtils.isEmpty(product.getProductColor())) {
				tv_color.setText("");
			} else {
				tv_color.setText("颜色:" + product.getProductColor());
			}
			if (TextUtils.isEmpty(product.getProductSize())) {
				tv_size.setText("");
			} else if (!TextUtils.isEmpty(product.getProductSize())) {
				if (TextUtils.isEmpty(product.getProductColor())) {
					tv_size.setText("尺码:" + product.getProductSize());
				} else if (!TextUtils.isEmpty(product.getProductColor())) {
					tv_size.setText("  " + "尺码:" + product.getProductSize());
				}
			}
			holder.lay_goods.addView(view_goods_item);
		}
		return convertView;

	}

	class ViewHolder {
		TextView tv_bags_name, tv_num;
		EditText edit_mark;
		LinearLayout lay_goods;
	}
}
