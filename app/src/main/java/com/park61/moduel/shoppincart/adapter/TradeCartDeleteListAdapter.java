package com.park61.moduel.shoppincart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.shoppincart.bean.DeleteItem;
import com.park61.moduel.shoppincart.bean.GoodsItem;
import com.park61.moduel.shoppincart.bean.ItemInfo;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("UseSparseArrays")
public class TradeCartDeleteListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context mContext;
    private ArrayList<GoodsItem> mList;

    public TradeCartDeleteListAdapter(Context _context, ArrayList<GoodsItem> _mList) {
        this.mList = _mList;
        this.mContext = _context;
        layoutInflater = LayoutInflater.from(mContext);
        for(int i=0;i<mList.size();i++){
            mList.get(i).setIsChecked(0);
            for(int j=0 ;j<mList.get(i).getItems().size();j++){
                mList.get(i).getItems().get(j).setIsChecked(0);
            }
        }
    }

    public List<DeleteItem> getSelectItems() {
        List<DeleteItem> dlist = new ArrayList<DeleteItem>();
        for (int i = 0; i < mList.size(); i++) {
            for (int j = 0; j < mList.get(i).getItems().size(); j++) {
                if (mList.get(i).getItems().get(j).getIsChecked() == 1) {
                    dlist.add(new DeleteItem(mList.get(i).getItems().get(j)
                            .getPmInfoId(), mList.get(i).getItems().get(j)
                            .getPromotionId()));
                }
            }
        }
        return dlist;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.shoppingcart_storelist_item, null);
            holder = new ViewHolder();
            holder.item_cb = (CheckBox) convertView.findViewById(R.id.item_cb);
            holder.tv_storename_series = (TextView) convertView.findViewById(R.id.tv_storename_series);
            holder.tv_total = (TextView) convertView.findViewById(R.id.tv_total);
            holder.lay_goods = (LinearLayout) convertView.findViewById(R.id.lay_goods);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GoodsItem items = mList.get(position);
        if (items.getMerchantType() == 0) {
            holder.tv_storename_series.setText("61区自营");
        } else if (items.getMerchantType() == 1) {
            holder.tv_storename_series.setText(items.getMerchantName());
        }

        holder.item_cb.setChecked(items.getIsChecked() == 0 ? false : true);

        holder.item_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                int checkedState = isChecked?1:0;
                items.setIsChecked(checkedState);
                for(int i=0 ;i<items.getItems().size();i++){
                    items.getItems().get(i).setIsChecked(checkedState);
                }
                notifyDataSetChanged();
            }
        });
        holder.lay_goods.removeAllViews();
        for (int i = 0; i < items.getItems().size(); i++) {
            final ItemInfo itemInfo = items.getItems().get(i);
            View view_goods_item = layoutInflater.inflate(R.layout.shoppingcart_goods_item, null);
            View area_cover = view_goods_item.findViewById(R.id.area_cover);
            area_cover.setVisibility(View.GONE);
            CheckBox goods_cb = (CheckBox) view_goods_item.findViewById(R.id.goods_cb);
            ImageView iv_icon = (ImageView) view_goods_item.findViewById(R.id.icon);
            ImageManager.getInstance().displayImg(iv_icon, itemInfo.getProductPicUrl());
            TextView title = (TextView) view_goods_item.findViewById(R.id.title);
            TextView tv_color = (TextView) view_goods_item.findViewById(R.id.tv_color);
            TextView tv_size = (TextView) view_goods_item.findViewById(R.id.tv_size);
            TextView cur_price = (TextView) view_goods_item.findViewById(R.id.cur_price);
            TextView src_price = (TextView) view_goods_item.findViewById(R.id.src_price);
            View line = view_goods_item.findViewById(R.id.line);
            final TextView tv_buy_num = (TextView) view_goods_item.findViewById(R.id.tv_buy_num);
            view_goods_item.findViewById(R.id.area_add_reduce).setVisibility(View.GONE);
            src_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 设置中划线
            title.setText(itemInfo.getProductCname());
            tv_color.setText(itemInfo.getProductColor());
            tv_size.setText(itemInfo.getProductSize());
            cur_price.setText(FU.formatPrice(itemInfo.getItemPrice()));
            src_price.setText("￥" + itemInfo.getMarketPrice());
            tv_buy_num.setText(itemInfo.getNum() + "");
            goods_cb.setChecked(itemInfo.getIsChecked() == 0 ? false : true);
            if(i == items.getItems().size()-1){
                line.setVisibility(View.INVISIBLE);
            }else{
                line.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(itemInfo.getProductColor())) {
                tv_color.setText("");
            } else {
                tv_color.setText("颜色:" + itemInfo.getProductColor());
            }
            if (TextUtils.isEmpty(itemInfo.getProductSize())) {
                tv_size.setText("");
            } else {
                tv_size.setText("尺码:" + itemInfo.getProductSize());
            }
            goods_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                int checkedState = isChecked?1:0;
                itemInfo.setIsChecked(checkedState);
                }
            });
            holder.lay_goods.addView(view_goods_item);
        }
        return convertView;
    }

    class ViewHolder {
        CheckBox item_cb;
        TextView tv_storename_series, tv_total;
        LinearLayout lay_goods;
    }

}
