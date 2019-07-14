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
import com.park61.moduel.sales.bean.ParentalOrderCfmBean;
import com.park61.moduel.sales.bean.TradeOrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 确认订单包裹商品清单adapter
 */
public class ParentalPkgGoodsListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context mContext;
    private List<ParentalOrderCfmBean.ListPmInfoBean> mList;

    public ParentalPkgGoodsListAdapter(Context _context, List<ParentalOrderCfmBean.ListPmInfoBean> _mList) {
        this.mList = _mList;
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
            convertView = layoutInflater.inflate(R.layout.package_goodslist_item, null);
            holder = new ViewHolder();
            holder.area = convertView.findViewById(R.id.area);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.cur_price = (TextView) convertView.findViewById(R.id.cur_price);
            holder.src_price = (TextView) convertView.findViewById(R.id.src_price);
            holder.num = (TextView) convertView.findViewById(R.id.num);
            holder.tv_color = (TextView) convertView.findViewById(R.id.tv_color);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            holder.bottom_line = convertView.findViewById(R.id.bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ParentalOrderCfmBean.ListPmInfoBean items = mList.get(position);
        ImageManager.getInstance().displayImg(holder.icon, items.getProductPicUrl());
        holder.title.setText(items.getProductCname());
        holder.src_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 设置中划线
        holder.cur_price.setText(FU.RENMINBI_UNIT + FU.strDbFmt(items.getCurrentUnifyPrice()));
        holder.src_price.setText(FU.RENMINBI_UNIT + FU.strDbFmt(items.getMarketPrice()));
        holder.num.setText("X" + items.getCount() + "");
        String color, size;
        color = items.getProductColor();
        size = items.getProductSize();
        holder.tv_color.setText(TextUtils.isEmpty(color) ? "" : "颜色：" + color);
        holder.tv_size.setText(TextUtils.isEmpty(size) ? "" : "尺寸：" + size);
        if (position == mList.size() - 1) {
            holder.bottom_line.setVisibility(View.GONE);
        } else {
            holder.bottom_line.setVisibility(View.VISIBLE);
        }
        /*holder.area.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, GoodsDetailsActivity.class);
                it.putExtra("goodsId", items.getId());
                it.putExtra("goodsName", items.getProductCname());
                it.putExtra("goodsPrice", items.getCurrentUnifyPrice() + "");
                mContext.startActivity(it);
            }
        });*/
        return convertView;
    }

    class ViewHolder {
        TextView title, cur_price, src_price, num, tv_color, tv_size;
        View area, bottom_line;
        ImageView icon;
    }
}
