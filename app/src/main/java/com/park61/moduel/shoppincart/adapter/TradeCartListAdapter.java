package com.park61.moduel.shoppincart.adapter;

import android.annotation.SuppressLint;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.shoppincart.bean.DeleteItem;
import com.park61.moduel.shoppincart.bean.GoodsItem;
import com.park61.moduel.shoppincart.bean.ItemInfo;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("UseSparseArrays")
public class TradeCartListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context mContext;
    private ArrayList<GoodsItem> mList;

    private AddBuyNumLsner mAddBuyNumLsner;
    private ReduceBuyNumLsner mReduceBuyNumLsner;
    private SelectBagLsner mSelectBagLsner;
    private SelectProductLsner mSelectProductLsner;

    public TradeCartListAdapter(Context _context, ArrayList<GoodsItem> mList) {
        this.mList = mList;
        this.mContext = _context;
        layoutInflater = LayoutInflater.from(mContext);
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
//        if (items.getMerchantType() == 0) {
//            holder.tv_storename_series.setText("61区自营");
//        } else if (items.getMerchantType() == 1) {
//            holder.tv_storename_series.setText(items.getMerchantName());
//        }
        holder.tv_storename_series.setText(items.getMerchantName());
        holder.item_cb.setChecked(items.getIsChecked() == 0 ? false : true);

        holder.item_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    mSelectBagLsner.selectBag(position,
                            items.getMerchantId(), true);
                } else {
                    mSelectBagLsner.selectBag(position,
                            items.getMerchantId(), false);
                }
            }
        });
        holder.lay_goods.removeAllViews();
        for (int i = 0; i < items.getItems().size(); i++) {
            final ItemInfo itemInfo = items.getItems().get(i);
            View view_goods_item = layoutInflater.inflate(R.layout.shoppingcart_goods_item, null);
            View area_cover = view_goods_item.findViewById(R.id.area_cover);
            CheckBox goods_cb = (CheckBox) view_goods_item.findViewById(R.id.goods_cb);
            ImageView iv_icon = (ImageView) view_goods_item.findViewById(R.id.icon);
            ImageManager.getInstance().displayImg(iv_icon, itemInfo.getProductPicUrl());
            TextView title = (TextView) view_goods_item.findViewById(R.id.title);
            TextView tv_color = (TextView) view_goods_item.findViewById(R.id.tv_color);
            TextView tv_size = (TextView) view_goods_item.findViewById(R.id.tv_size);
            TextView cur_price = (TextView) view_goods_item.findViewById(R.id.cur_price);
            TextView src_price = (TextView) view_goods_item.findViewById(R.id.src_price);
            final TextView tv_buy_num = (TextView) view_goods_item.findViewById(R.id.tv_buy_num);
            Button btn_reduce = (Button) view_goods_item.findViewById(R.id.btn_reduce);
            Button btn_add = (Button) view_goods_item.findViewById(R.id.btn_add);
            View line = view_goods_item.findViewById(R.id.line);
            FrameLayout root = (FrameLayout) view_goods_item.findViewById(R.id.root);
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
                tv_color.setText("颜色：" + itemInfo.getProductColor());
            }
            if (TextUtils.isEmpty(itemInfo.getProductSize())) {
                tv_size.setText("");
            } else {
                tv_size.setText("尺码：" + itemInfo.getProductSize());
            }

            if (itemInfo.getAvailableStockNum() <= 0) {
                area_cover.setVisibility(View.VISIBLE);
            } else {
                area_cover.setVisibility(View.GONE);
            }

            goods_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        mSelectProductLsner.selectProduct(position,
                                itemInfo.getItemId(), true);
                    } else {
                        mSelectProductLsner.selectProduct(position,
                                itemInfo.getItemId(), false);
                    }
                }
            });

            btn_add.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int num = FU.paseInt(tv_buy_num.getText().toString());
                    if (num < Math.max(200, itemInfo.getAvailableStockNum())) {
                        num++;
                    } else {
                        return;
                    }
                    tv_buy_num.setText(num + "");
                    itemInfo.setNum(num);
                    mAddBuyNumLsner.addBuyNum(itemInfo.getPmInfoId(), num);
                }
            });

            btn_reduce.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int num = FU.paseInt(tv_buy_num.getText().toString());
                    if (num > 1) {
                        num--;
                    } else {
                        return;
                    }
                    tv_buy_num.setText(num + "");
                    itemInfo.setNum(num);
                    mReduceBuyNumLsner.reduceBuyNum(itemInfo.getPmInfoId(), num);
                }
            });
            root.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, GoodsDetailsActivity.class);
                    it.putExtra("goodsId", itemInfo.getPmInfoId());
                    it.putExtra("goodsName", itemInfo.getProductCname());
                    it.putExtra("goodsPrice", itemInfo.getItemPrice() + "");
                    it.putExtra("goodsOldPrice", itemInfo.getMarketPrice() + "");
                    it.putExtra("promotionId", itemInfo.getPromotionId());
                    mContext.startActivity(it);
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

    public interface AddBuyNumLsner {
        public void addBuyNum(long pmInfoId, int num);
    }

    public void setAddBuyNumLsner(AddBuyNumLsner lsner) {
        mAddBuyNumLsner = lsner;
    }

    public interface ReduceBuyNumLsner {
        public void reduceBuyNum(long pmInfoId, int num);
    }

    public void setReduceBuyNumLsner(ReduceBuyNumLsner lsner) {
        mReduceBuyNumLsner = lsner;
    }

    public interface SelectBagLsner {
        public void selectBag(int position, long merchantId, boolean isSelect);
    }

    public void setSelectBagLsner(SelectBagLsner lsner) {
        mSelectBagLsner = lsner;
    }

    public interface SelectProductLsner {
        public void selectProduct(int position, String itemId, boolean isSelect);
    }

    public void setSelectProductLsner(SelectProductLsner lsner) {
        mSelectProductLsner = lsner;
    }

}
