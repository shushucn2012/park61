package com.park61.moduel.sales.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.moduel.sales.bean.PromotionBannerData;
import com.park61.widget.viewpager.TeBuyBanner;

import java.util.List;

public class GoodsListAdapter extends BaseAdapter {

    private List<GoodsCombine> mList;
    private List<PromotionBannerData> pList;
    private boolean isHasBanner;
    private Context mContext;
    private LayoutInflater factory;

    public GoodsListAdapter(Context _context, List<GoodsCombine> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
        isHasBanner = false;
    }

    public GoodsListAdapter(Context _context, List<GoodsCombine> _list, List<PromotionBannerData> picList) {
        this.mList = _list;
        this.pList = picList;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
        isHasBanner = true;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public GoodsCombine getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private final int TYPE_BANNER_AREA = 0;
    private final int TYPE_GOODS_ITEM = 1;

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER_AREA;
        } else {
            return TYPE_GOODS_ITEM;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = TYPE_GOODS_ITEM;
        if(isHasBanner && !CommonMethod.isListEmpty(pList)) {
            type = getItemViewType(position);
        }
        if (convertView == null) {
            holder = new ViewHolder();
            if (type == TYPE_BANNER_AREA){
                convertView = factory.inflate(R.layout.goodslist_item_top, null);
                holder.banner_area = convertView.findViewById(R.id.banner_area);
                holder.banner = new TeBuyBanner(mContext,
                        (ViewPager) convertView.findViewById(R.id.top_viewpager),
                        (LinearLayout) convertView.findViewById(R.id.top_vp_dot));
            }else {
                convertView = factory.inflate(R.layout.goodslist_item, null);
                holder.left_area_root = convertView.findViewById(R.id.left_area_root);
                holder.img_goods_left = (ImageView) convertView.findViewById(R.id.img_goods_left);
                holder.tv_goods_name_left = (TextView) convertView.findViewById(R.id.tv_goods_name_left);
                holder.tv_goods_price_left = (TextView) convertView.findViewById(R.id.tv_goods_price_left);
                holder.tv_old_goods_price_left = (TextView) convertView.findViewById(R.id.tv_old_goods_price_left);

                holder.right_area_root = convertView.findViewById(R.id.right_area_root);
                holder.tv_old_goods_price_left.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.img_goods_right = (ImageView) convertView.findViewById(R.id.img_goods_right);
                holder.tv_goods_name_right = (TextView) convertView.findViewById(R.id.tv_goods_name_right);
                holder.tv_goods_price_right = (TextView) convertView.findViewById(R.id.tv_goods_price_right);
                holder.tv_old_goods_price_right = (TextView) convertView.findViewById(R.id.tv_old_goods_price_right);
                holder.tv_old_goods_price_right.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (type == TYPE_BANNER_AREA){
            if(!holder.banner.isAutoPlay) {
                holder.banner.init(mContext, pList);
            }
        }else {
            final GoodsCombine comb = mList.get(position);
            holder.tv_goods_name_left.setText(comb.getGoodsLeft().getName());
            holder.tv_goods_price_left.setText(FU.formatPrice(comb.getGoodsLeft().getSalesPrice()));
            holder.tv_old_goods_price_left.setText(FU.formatPrice(comb.getGoodsLeft().getOldPrice()));
            ImageManager.getInstance().displayImg(holder.img_goods_left, comb.getGoodsLeft().getImg(),
                    R.drawable.img_default_v);
            holder.left_area_root.setOnClickListener(new OnClickListener() {

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
                holder.tv_goods_name_right.setVisibility(View.VISIBLE);
                holder.tv_goods_price_right.setVisibility(View.VISIBLE);
                holder.tv_old_goods_price_right.setVisibility(View.VISIBLE);
                holder.img_goods_right.setVisibility(View.VISIBLE);
                holder.tv_goods_name_right.setText(comb.getGoodsRight().getName());
                holder.tv_goods_price_right.setText(FU.formatPrice(comb.getGoodsRight().getSalesPrice()));
                holder.tv_old_goods_price_right.setText(FU.formatPrice(comb.getGoodsRight().getOldPrice()));
                ImageManager.getInstance().displayImg(holder.img_goods_right, comb.getGoodsRight().getImg(),
                        R.drawable.img_default_v);
                holder.right_area_root.setOnClickListener(new OnClickListener() {

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
                holder.tv_goods_name_right.setVisibility(View.GONE);
                holder.tv_goods_price_right.setVisibility(View.GONE);
                holder.tv_old_goods_price_right.setVisibility(View.GONE);
                holder.img_goods_right.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_goods_left, img_goods_right;
        TextView tv_goods_name_left, tv_goods_name_right;
        TextView tv_goods_price_left, tv_goods_price_right;
        TextView tv_old_goods_price_left, tv_old_goods_price_right;
        View banner_area, left_area_root, right_area_root;
        TeBuyBanner banner;
    }

}
