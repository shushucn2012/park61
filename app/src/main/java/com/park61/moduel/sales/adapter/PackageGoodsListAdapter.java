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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.bean.MerchantTradeOrder;
import com.park61.moduel.sales.bean.TradeOrderItem;

import java.util.ArrayList;

/**
 * 确认订单包裹商品清单
 */
public class PackageGoodsListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context mContext;
    private ArrayList<MerchantTradeOrder> mList;

    public PackageGoodsListAdapter(Context _context, ArrayList<MerchantTradeOrder> _mList) {
        this.mList = _mList;
        this.mContext = _context;
        layoutInflater = LayoutInflater.from(mContext);
        Log.out("mList.size------" + mList.size());
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
            convertView = layoutInflater.inflate(R.layout.package_goodslist_list, null);
            holder = new ViewHolder();
            holder.line = convertView.findViewById(R.id.line);
            holder.tv_bags_num = (TextView) convertView.findViewById(R.id.tv_bags_num);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.lay_goods = (LinearLayout) convertView.findViewById(R.id.lay_goods);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MerchantTradeOrder order = mList.get(position);
        holder.tv_bags_num.setText("包裹" + (position + 1));
        holder.tv_num.setText(order.getOrderItemNum() + "");
        if (position == 0) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        holder.lay_goods.removeAllViews();
        for (int i = 0; i < order.getOrderItemList().size(); i++) {
            final TradeOrderItem items = order.getOrderItemList().get(i);
            View view_goods_item = layoutInflater.inflate(R.layout.package_goodslist_item, null);
            View area = view_goods_item.findViewById(R.id.area);
            ImageView icon = (ImageView) view_goods_item.findViewById(R.id.icon);
            TextView title = (TextView) view_goods_item.findViewById(R.id.title);
            TextView cur_price = (TextView) view_goods_item.findViewById(R.id.cur_price);
            TextView src_price = (TextView) view_goods_item.findViewById(R.id.src_price);
            TextView num = (TextView) view_goods_item.findViewById(R.id.num);
            TextView tv_color = (TextView) view_goods_item.findViewById(R.id.tv_color);
            TextView tv_size = (TextView) view_goods_item.findViewById(R.id.tv_size);
            ImageManager.getInstance().displayImg(icon, items.getProductMerchant().getPmInfo().getProduct().getProductPicUrl());
            title.setText(items.getProductMerchant().getPmInfo().getProduct().getProductCname());
            src_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 设置中划线
            cur_price.setText(FU.formatPrice(items.getProductMerchant().getPmPrice().getCurrentUnifyPrice()));
            src_price.setText(FU.formatPrice(items.getProductMerchant().getPmPrice().getMarketPrice()));
            num.setText("X" + items.getOrderItemNum() + "");
            String color, size;
            color = items.getProductMerchant().getPmInfo().getProduct().getProductColor();
            size = items.getProductMerchant().getPmInfo().getProduct().getProductSize();
            if (!TextUtils.isEmpty(color)) {
                tv_color.setText("颜色：" + color);
            } else {
                tv_color.setText("");
            }
            if (!TextUtils.isEmpty(size)) {
                tv_size.setText("尺寸：" + size);
            } else {
                tv_size.setText("");
            }
            holder.lay_goods.addView(view_goods_item);
            area.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, GoodsDetailsActivity.class);
                    it.putExtra("goodsId", items.getProductMerchant().getPmStock().getPmInfoId());
                    it.putExtra("goodsName", items.getProductMerchant().getPmInfo().getProduct().getProductCname());
                    it.putExtra("goodsPrice", items.getProductMerchant().getPmPrice().getCurrentUnifyPrice() + "");
                    mContext.startActivity(it);
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_bags_num, tv_num;
        LinearLayout lay_goods;
        View line;
    }
}
