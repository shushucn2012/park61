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
import com.park61.moduel.sales.GoodsListActivity;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.moduel.sales.bean.ProductCategory;
import com.park61.moduel.sales.bean.PromotionBannerData;
import com.park61.widget.gridview.GridViewForScrollView;
import com.park61.widget.viewpager.TeBuyBanner;

import java.util.List;

public class SalesSpecialAdapter extends BaseAdapter {

    private List<ProductCategory> cateList;
    private List<GoodsCombine> mList;
    private List<PromotionBannerData> pList;
    private Context mContext;
    private LayoutInflater factory;
    private boolean isHasBanner;//banner是否显示
    private boolean isNeedRefreshBanner;//banner是否显示是否需要刷新
    private CateGvAdapter gvAdapter;

    public SalesSpecialAdapter(Context _context, List<ProductCategory> topCateList,
                               List<GoodsCombine> _list, List<PromotionBannerData> picList) {
        this.mList = _list;
        this.cateList = topCateList;
        this.pList = picList;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
        if (CommonMethod.isListEmpty(picList)) {//初始化的时候就确定有没有banner
            isHasBanner = false;
        } else {
            isHasBanner = true;
        }
        setIsNeedRefreshBanner(true);
    }

    /**
     * 设置banner是否需要刷新
     * @param isNeedRefreshBanner
     */
    public void setIsNeedRefreshBanner(boolean isNeedRefreshBanner){
        this.isNeedRefreshBanner = isNeedRefreshBanner;
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

    private final int TYPE_BANNER_AREA = 0;//头部布局
    private final int TYPE_CATE_AREA = 1;//分类布局
    private final int TYPE_GOODS_ITEM = 2;//列表布局

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER_AREA;
        } else if (position == 1) {
            return TYPE_CATE_AREA;
        } else {
            return TYPE_GOODS_ITEM;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (!isHasBanner) {
            if (type == TYPE_BANNER_AREA) {//没有banner时，其他布局位置上移
                type = TYPE_CATE_AREA;
            } else if (type == TYPE_CATE_AREA) {
                type = TYPE_GOODS_ITEM;
            }
        }
        if (convertView == null) {
            holder = new ViewHolder();
            if (type == TYPE_BANNER_AREA) {
                convertView = factory.inflate(R.layout.goodslist_item_top, null);
                holder.banner_area = convertView.findViewById(R.id.banner_area);
                holder.banner = new TeBuyBanner(mContext,
                        (ViewPager) convertView.findViewById(R.id.top_viewpager),
                        (LinearLayout) convertView.findViewById(R.id.top_vp_dot));
            } else if (type == TYPE_CATE_AREA) {
                convertView = factory.inflate(R.layout.special_mid_cate_layout, null);
                holder.cateGv = (GridViewForScrollView) convertView.findViewById(R.id.gv_cate);
            } else {
                convertView = factory.inflate(R.layout.goodslist_item, null);
                holder.left_area_root = convertView.findViewById(R.id.left_area_root);
                holder.img_goods_left = (ImageView) convertView.findViewById(R.id.img_goods_left);
                holder.tv_goods_name_left = (TextView) convertView.findViewById(R.id.tv_goods_name_left);
                holder.tv_goods_price_left = (TextView) convertView.findViewById(R.id.tv_goods_price_left);
                holder.tv_old_goods_price_left = (TextView) convertView.findViewById(R.id.tv_old_goods_price_left);
                holder.tv_old_goods_price_left.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                holder.right_area_root = convertView.findViewById(R.id.right_area_root);
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
        if (type == TYPE_BANNER_AREA) {
            if (isNeedRefreshBanner) {
                holder.banner.init(mContext, pList);
                setIsNeedRefreshBanner(false);//刷新后重置，重复刷新
            }
        } else if (type == TYPE_CATE_AREA) {
            if (gvAdapter == null) {//只加载一次数据，以后直接返回视图
                gvAdapter = new CateGvAdapter(mContext, cateList);
                holder.cateGv.setAdapter(gvAdapter);
            }
        } else {
            final GoodsCombine comb = mList.get(position);
            holder.tv_goods_name_left.setText(comb.getGoodsLeft().getName());
            holder.tv_goods_price_left.setText(FU.formatPrice(comb.getGoodsLeft().getSalesPrice()));
            holder.tv_old_goods_price_left.setText(FU.formatPrice(comb.getGoodsLeft().getOldPrice()));
            ImageManager.getInstance().displayImg(holder.img_goods_left, comb.getGoodsLeft().getImg(), R.drawable.img_default_v);
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
                ImageManager.getInstance().displayImg(holder.img_goods_right, comb.getGoodsRight().getImg(), R.drawable.img_default_v);
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
        GridViewForScrollView cateGv;
        ImageView img_goods_left, img_goods_right;
        TextView tv_goods_name_left, tv_goods_name_right;
        TextView tv_goods_price_left, tv_goods_price_right;
        TextView tv_old_goods_price_left, tv_old_goods_price_right;
        View banner_area, left_area_root, right_area_root;;
        TeBuyBanner banner;
    }

    private class CateGvAdapter extends BaseAdapter {

        private List<ProductCategory> mList;
        private Context mContext;
        private LayoutInflater factory;

        public CateGvAdapter(Context _context, List<ProductCategory> _list) {
            this.mList = _list;
            this.mContext = _context;
            factory = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public ProductCategory getItem(int position) {
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
                convertView = factory.inflate(R.layout.special_cate_gv_item, null);
                holder = new ViewHolder();
                holder.left_area = convertView.findViewById(R.id.left_area);
                holder.img_cate_left = (ImageView) convertView.findViewById(R.id.img_cate_left);
                holder.tv_big_name_left = (TextView) convertView.findViewById(R.id.tv_big_name_left);
                holder.tv_small_name_left = (TextView) convertView.findViewById(R.id.tv_small_name_left);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final ProductCategory cate = mList.get(position);
            holder.tv_big_name_left.setText(cate.getCategoryName());
            holder.tv_small_name_left.setText(cate.getCategoryDescription());
            ImageManager.getInstance().displayImg(holder.img_cate_left, cate.getCategoryPicUrl(), R.drawable.img_default_v);
            holder.left_area.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, GoodsListActivity.class);
                    it.putExtra("cateId", cate.getId());
                    it.putExtra("cateName", cate.getCategoryName());
                    mContext.startActivity(it);
                }
            });
            return convertView;
        }

        class ViewHolder {
            View left_area;
            ImageView img_cate_left;
            TextView tv_big_name_left;
            TextView tv_small_name_left;
        }
    }

}
