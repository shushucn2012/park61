package com.park61.moduel.sales.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.adapter.ListBaseAdapter;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.bean.GoodsCombine;

import java.util.List;

public class SalesFmRvAdapter extends ListBaseAdapter<GoodsCombine>{

    private List<GoodsCombine> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public SalesFmRvAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TheViewHolder(mLayoutInflater.inflate(R.layout.goodslist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final GoodsCombine comb = mDataList.get(position);
        TheViewHolder viewHolder = (TheViewHolder) holder;
        viewHolder.tv_goods_name_left.setText(comb.getGoodsLeft().getName());
        viewHolder.tv_goods_price_left.setText(FU.formatPrice(comb.getGoodsLeft().getSalesPrice()));
        viewHolder.tv_old_goods_price_left.setText(FU.formatPrice(comb.getGoodsLeft().getOldPrice()));
        ImageManager.getInstance().displayImg(viewHolder.img_goods_left, comb.getGoodsLeft().getImg(), R.drawable.img_default_v);
        viewHolder.left_area_root.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, GoodsDetailsActivity.class);
                it.putExtra("goodsId", comb.getGoodsLeft().getPmInfoid());
                it.putExtra("goodsName", comb.getGoodsLeft().getName());
                it.putExtra("goodsPrice", comb.getGoodsLeft().getSalesPrice() + "");
                it.putExtra("goodsOldPrice", comb.getGoodsLeft().getOldPrice() + "");
                it.putExtra("promotionId", comb.getGoodsLeft().getPromotionId());
                mContext.startActivity(it);
            }
        });
        if (comb.getGoodsRight() != null) {
            viewHolder.tv_goods_name_right.setVisibility(View.VISIBLE);
            viewHolder.tv_goods_price_right.setVisibility(View.VISIBLE);
            viewHolder.tv_old_goods_price_right.setVisibility(View.VISIBLE);
            viewHolder.img_goods_right.setVisibility(View.VISIBLE);
            viewHolder.tv_goods_name_right.setText(comb.getGoodsRight().getName());
            viewHolder.tv_goods_price_right.setText(FU.formatPrice(comb.getGoodsRight().getSalesPrice()));
            viewHolder.tv_old_goods_price_right.setText(FU.formatPrice(comb.getGoodsRight().getOldPrice()));
            ImageManager.getInstance().displayImg(viewHolder.img_goods_right, comb.getGoodsRight().getImg(), R.drawable.img_default_v);
            viewHolder.right_area_root.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, GoodsDetailsActivity.class);
                    it.putExtra("goodsId", comb.getGoodsRight().getPmInfoid());
                    it.putExtra("goodsName", comb.getGoodsRight().getName());
                    it.putExtra("goodsPrice", comb.getGoodsRight().getSalesPrice() + "");
                    it.putExtra("goodsOldPrice", comb.getGoodsRight().getOldPrice() + "");
                    it.putExtra("promotionId", comb.getGoodsRight().getPromotionId());
                    mContext.startActivity(it);
                }
            });
        } else {
            viewHolder.tv_goods_name_right.setVisibility(View.GONE);
            viewHolder.tv_goods_price_right.setVisibility(View.GONE);
            viewHolder.tv_old_goods_price_right.setVisibility(View.GONE);
            viewHolder.img_goods_right.setVisibility(View.GONE);
        }
    }

    private class TheViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_goods_left, img_goods_right;
        private TextView tv_goods_name_left, tv_goods_name_right;
        private TextView tv_goods_price_left, tv_goods_price_right;
        private TextView tv_old_goods_price_left, tv_old_goods_price_right;
        private View left_area_root, right_area_root;

        public TheViewHolder(View itemView) {
            super(itemView);
            left_area_root = itemView.findViewById(R.id.left_area_root);
            img_goods_left = (ImageView) itemView.findViewById(R.id.img_goods_left);
            tv_goods_name_left = (TextView) itemView.findViewById(R.id.tv_goods_name_left);
            tv_goods_price_left = (TextView) itemView.findViewById(R.id.tv_goods_price_left);
            tv_old_goods_price_left = (TextView) itemView.findViewById(R.id.tv_old_goods_price_left);
            tv_old_goods_price_left.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            right_area_root = itemView.findViewById(R.id.right_area_root);
            img_goods_right = (ImageView) itemView.findViewById(R.id.img_goods_right);
            tv_goods_name_right = (TextView) itemView.findViewById(R.id.tv_goods_name_right);
            tv_goods_price_right = (TextView) itemView.findViewById(R.id.tv_goods_price_right);
            tv_old_goods_price_right = (TextView) itemView.findViewById(R.id.tv_old_goods_price_right);
            tv_old_goods_price_right.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
