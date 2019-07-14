package com.park61.moduel.sales.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.PackageGoodsListAcyivity;
import com.park61.moduel.sales.bean.MerchantTradeOrder;
import com.park61.moduel.sales.bean.Product;
import com.park61.moduel.sales.bean.ProductMerchant;
import com.park61.moduel.sales.bean.TradeOrderConfirm;
import com.park61.moduel.sales.bean.TradeOrderItem;

import java.util.ArrayList;

/**
 * 商品订单详情adapter
 */
public class OrderConfirmAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context mContext;
    private ArrayList<TradeOrderConfirm> mList;

    public OrderConfirmAdapter(Context _context, ArrayList<TradeOrderConfirm> _list) {
        this.mContext = _context;
        this.mList = _list;
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
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.order_confirm_list_item, null);
            holder.tv_merchant_name = (TextView) convertView.findViewById(R.id.tv_merchant_name);
            holder.tv_expense_amount = (TextView) convertView.findViewById(R.id.tv_expense_amount);
            holder.tv_total_amount = (TextView) convertView.findViewById(R.id.tv_total_amount);

            holder.bags_area = convertView.findViewById(R.id.bags_area);
            holder.img_icon1 = (ImageView) convertView.findViewById(R.id.img_icon1);
            holder.img_icon2 = (ImageView) convertView.findViewById(R.id.img_icon2);
            holder.img_icon3 = (ImageView) convertView.findViewById(R.id.img_icon3);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_dots = (TextView) convertView.findViewById(R.id.tv_dots);

            holder.one_goods_area = convertView.findViewById(R.id.one_goods_area);
            holder.cur_price = (TextView) convertView.findViewById(R.id.cur_price);
            holder.src_price = (TextView) convertView.findViewById(R.id.src_price);
            holder.goods_num = (TextView) convertView.findViewById(R.id.goods_num);
            holder.goods_title = (TextView) convertView.findViewById(R.id.goods_title);
            holder.tv_color = (TextView) convertView.findViewById(R.id.tv_color);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            holder.img_goods = (ImageView) convertView.findViewById(R.id.img_goods);

            holder.expense_area = convertView.findViewById(R.id.expense_area);
            holder.total_area = convertView.findViewById(R.id.total_area);

            holder.remark_area = convertView.findViewById(R.id.remark_area);
            holder.et_order_remark = (EditText) convertView.findViewById(R.id.et_order_remark);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final TradeOrderConfirm orderItem = mList.get(position);
        holder.remark_area.setVisibility(View.GONE);
        holder.tv_merchant_name.setText(orderItem.getMerchant());
        if (mList.size() == 1) {//只有一个商家，小计和运费不显示
            holder.expense_area.setVisibility(View.GONE);
            holder.total_area.setVisibility(View.GONE);
        } else {
            holder.expense_area.setVisibility(View.VISIBLE);
            holder.total_area.setVisibility(View.VISIBLE);
            holder.tv_expense_amount.setText(FU.RENMINBI_UNIT+FU.strBFmt(orderItem.getMerchantOrderDeliveryFee()));
            holder.tv_total_amount.setText(FU.RENMINBI_UNIT+FU.strBFmt(orderItem.getMerchantOrderAmount()));
        }
        for (int i = 0; i < orderItem.getMerchantOrder().size(); i++) {
            MerchantTradeOrder item = orderItem.getMerchantOrder().get(i);
            if (orderItem.getProductPicUrlList().size()==1) {
                holder.one_goods_area.setVisibility(View.VISIBLE);
                holder.bags_area.setVisibility(View.GONE);
                TradeOrderItem goodsItem = item.getOrderItemList().get(0);
                if(TextUtils.isEmpty(item.getIsFightGroup())){//为空不是拼团，等于true时为拼团
                    holder.cur_price.setText(FU.RENMINBI_UNIT+FU.strBFmt(goodsItem.getProductMerchant().getPmPrice().getCurrentUnifyPrice()));
                }else{//拼团
                    holder.cur_price.setText(FU.RENMINBI_UNIT+FU.strBFmt(goodsItem.getProductMerchant().getPmPrice().getFightGroupPrice()));
                }
                // 如果有促销价格，就用促销价格
                if (goodsItem.getProductMerchant().getPmPrice().getPromPrice() != null
                        &&goodsItem.getProductMerchant().getPmPrice().getPromType()!=0) {
                    holder.cur_price.setText(FU.RENMINBI_UNIT+FU.strBFmt(goodsItem.getProductMerchant().getPmPrice().getPromPrice()));
                }
                holder.src_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 设置中划线
                holder.src_price.setText(FU.RENMINBI_UNIT+goodsItem.getProductMerchant().getPmPrice().getMarketPrice());
                holder.goods_num.setText("X" + goodsItem.getOrderItemNum() + "");
                Product product = goodsItem.getProductMerchant().getPmInfo().getProduct();
                holder.goods_title.setText(product.getProductCname());
                if(!TextUtils.isEmpty(product.getProductColor())){
                    holder.tv_color.setText("颜色:"+product.getProductColor());
                }else{
                    holder.tv_color.setText("");
                }
                if(!TextUtils.isEmpty(product.getProductSize())){
                    holder.tv_size.setText("尺寸:"+product.getProductSize());
                }else{
                    holder.tv_size.setText("");
                }
                ImageManager.getInstance().displayImg(holder.img_goods, product.getProductPicUrl());
            }else{
                holder.one_goods_area.setVisibility(View.GONE);
                holder.bags_area.setVisibility(View.VISIBLE);
                //holder.tv_num.setText(orderItem.getOrderNum()+"个包裹(共"+orderItem.getPmNum()+"件)");
                holder.tv_num.setText("共"+orderItem.getPmNum()+"件");
                if(orderItem.getProductPicUrlList().size()==2){
                    holder.tv_dots.setVisibility(View.GONE);
                    holder.img_icon1.setVisibility(View.VISIBLE);
                    holder.img_icon2.setVisibility(View.VISIBLE);
                    holder.img_icon3.setVisibility(View.GONE);
                    ImageManager.getInstance().displayImg(holder.img_icon1,orderItem.getProductPicUrlList().get(0));
                    ImageManager.getInstance().displayImg(holder.img_icon2,orderItem.getProductPicUrlList().get(1));
                }else if(orderItem.getProductPicUrlList().size()==3){
                    holder.tv_dots.setVisibility(View.GONE);
                    holder.img_icon1.setVisibility(View.VISIBLE);
                    holder.img_icon2.setVisibility(View.VISIBLE);
                    holder.img_icon3.setVisibility(View.VISIBLE);
                    ImageManager.getInstance().displayImg(holder.img_icon1,orderItem.getProductPicUrlList().get(0));
                    ImageManager.getInstance().displayImg(holder.img_icon2,orderItem.getProductPicUrlList().get(1));
                    ImageManager.getInstance().displayImg(holder.img_icon3,orderItem.getProductPicUrlList().get(2));
                }else if(orderItem.getProductPicUrlList().size()>3){
                    holder.tv_dots.setVisibility(View.VISIBLE);
                    holder.img_icon1.setVisibility(View.VISIBLE);
                    holder.img_icon2.setVisibility(View.VISIBLE);
                    holder.img_icon3.setVisibility(View.VISIBLE);
                    ImageManager.getInstance().displayImg(holder.img_icon1,orderItem.getProductPicUrlList().get(0));
                    ImageManager.getInstance().displayImg(holder.img_icon2,orderItem.getProductPicUrlList().get(1));
                    ImageManager.getInstance().displayImg(holder.img_icon3,orderItem.getProductPicUrlList().get(2));
                }
            }
        }
        holder.bags_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, PackageGoodsListAcyivity.class);
                it.putParcelableArrayListExtra("orderList", (ArrayList) (orderItem.getMerchantOrder()));
                it.putExtra("packageNum", orderItem.getOrderNum());
                it.putExtra("orderItemNum", orderItem.getPmNum());
                mContext.startActivity(it);
            }
        });
        holder.one_goods_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
                ProductMerchant productMerchant = orderItem.getMerchantOrder().get(0).getOrderItemList().get(0).getProductMerchant();
                intent.putExtra("goodsId", productMerchant.getPmInfo().getId());
                intent.putExtra("goodsName", productMerchant.getPmInfo().getProduct().getProductCname());
                intent.putExtra("goodsPrice", productMerchant.getPmPrice().getCurrentUnifyPrice() + "");
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_merchant_name, tv_expense_amount, tv_total_amount;
        TextView tv_dots, tv_num, cur_price, src_price, goods_num, goods_title, tv_color, tv_size;
        ImageView img_icon1, img_icon2, img_icon3, img_goods;
        View bags_area, one_goods_area, expense_area, total_area,remark_area;
        EditText et_order_remark;
    }

}
