package com.park61.moduel.grouppurchase.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.grouppurchase.GroupPurchaseGoodsDetailActivity;
import com.park61.moduel.grouppurchase.bean.GroupGoodsCombine;

import java.util.List;

/**
 * 拼团商品列表adapter
 */
public class GroupPurchaseGoodsListAdapter extends BaseAdapter{
    private List<GroupGoodsCombine> mList;
    private Context mContext;
    private LayoutInflater factory;

    public GroupPurchaseGoodsListAdapter(Context _context, List<GroupGoodsCombine> _list){
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public GroupGoodsCombine getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = factory.inflate(R.layout.grouppurchase_goodslist_item, null);
            holder.area_goods_left = convertView.findViewById(R.id.area_goods_left);
            holder.img_goods_left = (ImageView) convertView.findViewById(R.id.img_goods_left);
            holder.tv_goods_name_left = (TextView) convertView.findViewById(R.id.tv_goods_name_left);
            holder.tv_goods_price_left = (TextView) convertView.findViewById(R.id.tv_goods_price_left);
            holder.tv_old_goods_price_left = (TextView) convertView.findViewById(R.id.tv_old_goods_price_left);
            holder.tv_old_goods_price_left.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.btn_num_left = (Button) convertView.findViewById(R.id.btn_num_left);
            holder.btn_group_left = (Button) convertView.findViewById(R.id.btn_group_left);
            holder.area_goods_right = convertView.findViewById(R.id.area_goods_right);
            holder.img_goods_right = (ImageView) convertView.findViewById(R.id.img_goods_right);
            holder.tv_goods_name_right = (TextView) convertView.findViewById(R.id.tv_goods_name_right);
            holder.tv_goods_price_right = (TextView) convertView.findViewById(R.id.tv_goods_price_right);
            holder.tv_old_goods_price_right = (TextView) convertView.findViewById(R.id.tv_old_goods_price_right);
            holder.tv_old_goods_price_right.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.btn_num_right = (Button) convertView.findViewById(R.id.btn_num_right);
            holder.btn_group_right = (Button) convertView.findViewById(R.id.btn_group_right);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GroupGoodsCombine comb = mList.get(position);
        holder.tv_goods_name_left.setText(comb.getGoodsLeft().getName());
        holder.tv_goods_price_left.setText(FU.formatPrice(comb.getGoodsLeft().getFightGroupPrice()));
        holder.tv_old_goods_price_left.setText("￥" + comb.getGoodsLeft().getOldPrice());
        holder.btn_num_left.setText(comb.getGoodsLeft().getPersonNum()+"人拼团");
        if (holder.img_goods_left.getTag() == null
                || !holder.img_goods_left.getTag().equals(comb.getGoodsLeft().getPic())) {
            ImageManager.getInstance().displayImg(holder.img_goods_left, comb.getGoodsLeft().getPic());
        }
        holder.img_goods_left.setTag(comb.getGoodsLeft().getPic());
        holder.area_goods_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, GroupPurchaseGoodsDetailActivity.class);
                it.putExtra("goodsId", comb.getGoodsLeft().getPmInfoId());
                it.putExtra("goodsName", comb.getGoodsLeft().getName());
                it.putExtra("goodsPrice", comb.getGoodsLeft().getFightGroupPrice() + "");
                it.putExtra("goodsOldPrice", comb.getGoodsLeft().getOldPrice() + "");
//                it.putExtra("promotionId", comb.getGoodsLeft().getPromotionId());
//                it.putExtra("personNum", comb.getGoodsRight().getPersonNum() + "");
                mContext.startActivity(it);
            }
        });
        if (comb.getGoodsRight() != null) {
            holder.tv_goods_name_right.setVisibility(View.VISIBLE);
            holder.tv_goods_price_right.setVisibility(View.VISIBLE);
            holder.tv_old_goods_price_right.setVisibility(View.VISIBLE);
            holder.img_goods_right.setVisibility(View.VISIBLE);
            holder.btn_num_right.setVisibility(View.VISIBLE);
            holder.btn_group_right.setVisibility(View.VISIBLE);
            holder.tv_goods_name_right.setText(comb.getGoodsRight().getName());
            holder.tv_goods_price_right.setText(FU.formatPrice(comb.getGoodsRight().getFightGroupPrice()));
            holder.tv_old_goods_price_right.setText("￥" + comb.getGoodsRight().getOldPrice());
            holder.btn_num_right.setText(comb.getGoodsRight().getPersonNum()+"人拼团");
            if (holder.img_goods_right.getTag() == null
                    || !holder.img_goods_right.getTag().equals(comb.getGoodsRight().getPic())) {
                ImageManager.getInstance().displayImg(holder.img_goods_right, comb.getGoodsRight().getPic());
            }
            holder.img_goods_right.setTag(comb.getGoodsRight().getPic());
            holder.area_goods_right.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, GroupPurchaseGoodsDetailActivity.class);
                    it.putExtra("goodsId", comb.getGoodsRight().getPmInfoId());
                    it.putExtra("goodsName", comb.getGoodsRight().getName());
                    it.putExtra("goodsPrice", comb.getGoodsRight().getFightGroupPrice() + "");
                    it.putExtra("goodsOldPrice", comb.getGoodsRight().getOldPrice() + "");
//                    it.putExtra("promotionId", comb.getGoodsRight().getPromotionId());
                    it.putExtra("personNum", comb.getGoodsRight().getPersonNum()+"");
                    mContext.startActivity(it);
                }
            });
        } else {
            holder.tv_goods_name_right.setVisibility(View.GONE);
            holder.tv_goods_price_right.setVisibility(View.GONE);
            holder.tv_old_goods_price_right.setVisibility(View.GONE);
            holder.img_goods_right.setVisibility(View.GONE);
            holder.btn_num_right.setVisibility(View.GONE);
            holder.btn_group_right.setVisibility(View.GONE);
        }

        return convertView;
    }
    class ViewHolder {
        ImageView img_goods_left, img_goods_right;
        TextView tv_goods_name_left, tv_goods_name_right;
        TextView tv_goods_price_left, tv_goods_price_right;
        TextView tv_old_goods_price_left, tv_old_goods_price_right;
        Button btn_num_left,btn_num_right,btn_group_left,btn_group_right;
        View area_goods_left,area_goods_right;
    }
}
