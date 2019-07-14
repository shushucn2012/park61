package com.park61.moduel.sales.AdapterDelegate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.adapter.SalesFmRvAdapter;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.widget.myrv.base.ItemViewDelegate;
import com.park61.widget.myrv.base.ViewHolder;

/**
 * Created by shubei on 2017/5/19.
 */

public class SalesFmGoodsDelegate implements ItemViewDelegate<GoodsCombine> {

    private Context mContext;

    public SalesFmGoodsDelegate(Context c) {
        mContext = c;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.goodslist_item;
    }

    @Override
    public boolean isForViewType(GoodsCombine item, int position) {
        return item.getTemplete().getTempleteCode().equals("goods");
    }

    @Override
    public void convert(ViewHolder holder, final GoodsCombine goodsCombine, int position) {
        holder.setText(R.id.tv_goods_name_left, goodsCombine.getGoodsLeft().getName());
        ((TextView) holder.getView(R.id.tv_goods_price_left)).setText(FU.formatPrice(goodsCombine.getGoodsLeft().getSalesPrice()));
        ((TextView) holder.getView(R.id.tv_old_goods_price_left)).setText(FU.formatPrice(goodsCombine.getGoodsLeft().getOldPrice()));
        ImageManager.getInstance().displayImg(((ImageView) holder.getView(R.id.img_goods_left)), goodsCombine.getGoodsLeft().getImg(), R.drawable.img_default_v);
        holder.getView(R.id.left_area_root).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, GoodsDetailsActivity.class);
                it.putExtra("goodsId", goodsCombine.getGoodsLeft().getPmInfoid());
                it.putExtra("goodsName", goodsCombine.getGoodsLeft().getName());
                it.putExtra("goodsPrice", goodsCombine.getGoodsLeft().getSalesPrice() + "");
                it.putExtra("goodsOldPrice", goodsCombine.getGoodsLeft().getOldPrice() + "");
                it.putExtra("promotionId", goodsCombine.getGoodsLeft().getPromotionId());
                mContext.startActivity(it);
            }
        });
        if (goodsCombine.getGoodsRight() != null) {
            holder.getView(R.id.tv_goods_name_right).setVisibility(View.VISIBLE);
            holder.getView(R.id.tv_goods_price_right).setVisibility(View.VISIBLE);
            holder.getView(R.id.tv_old_goods_price_right).setVisibility(View.VISIBLE);
            holder.getView(R.id.img_goods_right).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_goods_name_right, goodsCombine.getGoodsRight().getName());
            ((TextView) holder.getView(R.id.tv_goods_price_right)).setText(FU.formatPrice(goodsCombine.getGoodsRight().getSalesPrice()));
            ((TextView) holder.getView(R.id.tv_old_goods_price_right)).setText(FU.formatPrice(goodsCombine.getGoodsRight().getOldPrice()));
            ImageManager.getInstance().displayImg(((ImageView) holder.getView(R.id.img_goods_right)), goodsCombine.getGoodsRight().getImg(), R.drawable.img_default_v);
            holder.getView(R.id.right_area_root).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, GoodsDetailsActivity.class);
                    it.putExtra("goodsId", goodsCombine.getGoodsRight().getPmInfoid());
                    it.putExtra("goodsName", goodsCombine.getGoodsRight().getName());
                    it.putExtra("goodsPrice", goodsCombine.getGoodsRight().getSalesPrice() + "");
                    it.putExtra("goodsOldPrice", goodsCombine.getGoodsRight().getOldPrice() + "");
                    it.putExtra("promotionId", goodsCombine.getGoodsRight().getPromotionId());
                    mContext.startActivity(it);
                }
            });
        } else {
            holder.getView(R.id.tv_goods_name_right).setVisibility(View.GONE);
            holder.getView(R.id.tv_goods_price_right).setVisibility(View.GONE);
            holder.getView(R.id.tv_old_goods_price_right).setVisibility(View.GONE);
            holder.getView(R.id.img_goods_right).setVisibility(View.GONE);
        }
    }
}
