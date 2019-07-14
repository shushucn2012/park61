package com.park61.moduel.sales.AdapterDelegate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.grouppurchase.GroupPurchaseActivity;
import com.park61.moduel.sales.BrandClassifyActivity;
import com.park61.moduel.sales.MaMaGroupActivity;
import com.park61.moduel.sales.OverValueActivity;
import com.park61.moduel.sales.OverseasGoodsActivity;
import com.park61.moduel.sales.SecKillActivityNew;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.moduel.sales.bean.PromotionType;
import com.park61.widget.myrv.base.ItemViewDelegate;
import com.park61.widget.myrv.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/5/19.
 */
public class SalesPagePromotionNavHeaderDelegate implements ItemViewDelegate<GoodsCombine> {

    private Context mContext;

    public SalesPagePromotionNavHeaderDelegate(Context c) {
        mContext = c;
    }


    @Override
    public int getItemViewLayoutId() {
        return R.layout.salespage_promotion_nav_layout;
    }

    @Override
    public boolean isForViewType(GoodsCombine item, int position) {
        return item.getTemplete().getTempleteCode().equals("function_navigation");
    }

    @Override
    public void convert(ViewHolder holder, GoodsCombine item, int position) {
        JSONArray ptJay = null;
        try {
            ptJay = new JSONArray(item.getTemplete().getTempleteData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ptJay != null && ptJay.length() > 0) {//如果数组不为空
            final List<PromotionType> ptList = new ArrayList<>();
            for (int j = 0; j < ptJay.length(); j++) {
                JSONObject ptItemJot = ptJay.optJSONObject(j);
                PromotionType ptItem = new PromotionType();
                ptItem.setPicUrl(ptItemJot.optString("promotionPicUrl"));
                ptItem.setPromotionType(ptItemJot.optString("promotionType"));
                ptItem.setPromotionName(ptItemJot.optString("title"));
                ptList.add(ptItem);
            }

            View.OnClickListener lsner = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = 0;
                    String promotionType = null;
                    String title = "";
                    switch (v.getId()) {
                        case R.id.view0:
                            pos = 0;
                            break;
                        case R.id.view1:
                            pos = 1;
                            break;
                        case R.id.view2:
                            pos = 2;
                            break;
                        case R.id.view3:
                            pos = 3;
                            break;
                    }
                    promotionType = ptList.get(pos).getPromotionType();
                    title = ptList.get(pos).getPromotionName();
                    if ("妈妈团".equals(title)) {
                        Intent it = new Intent(mContext, MaMaGroupActivity.class);
                        it.putExtra("title", title);
                        it.putExtra("promotionType", promotionType);
                        mContext.startActivity(it);
                    } else if ("海外优品".equals(title)) {
                        Intent it = new Intent(mContext, OverseasGoodsActivity.class);
                        it.putExtra("title", title);
                        it.putExtra("promotionType", promotionType);
                        mContext.startActivity(it);
                    } else if ("1".equals(promotionType)) { // "1"是限时秒杀
                        Intent it = new Intent(mContext, SecKillActivityNew.class);
                        it.putExtra("promotionType", promotionType);
                        it.putExtra("title", title);
                        mContext.startActivity(it);
                    } else if ("pb".equals(promotionType)) {
                        Intent it = new Intent(mContext, GroupPurchaseActivity.class);
                        it.putExtra("promotionType", promotionType);
                        it.putExtra("title", title);
                        mContext.startActivity(it);
                    } else if ("fl".equals(promotionType)) {
                        mContext.startActivity(new Intent(mContext, BrandClassifyActivity.class));
                    } else {
                        Intent it = new Intent(mContext, OverValueActivity.class);
                        it.putExtra("promotionType", promotionType);
                        it.putExtra("title", title);
                        mContext.startActivity(it);
                    }
                }
            };

            if (ptList.size() > 0) {
                PromotionType promotionType = ptList.get(0);
                ImageManager.getInstance().displayImg((ImageView) holder.getView(R.id.img0), promotionType.getPicUrl(), R.drawable.img_default_v);
                holder.setText(R.id.tv0, promotionType.getPromotionName());
                holder.getView(R.id.view0).setOnClickListener(lsner);
            }
            if (ptList.size() > 1) {
                PromotionType promotionType = ptList.get(1);
                ImageManager.getInstance().displayImg((ImageView) holder.getView(R.id.img1), promotionType.getPicUrl(), R.drawable.img_default_v);
                holder.setText(R.id.tv1, promotionType.getPromotionName());
                holder.getView(R.id.view1).setOnClickListener(lsner);
            }
            if (ptList.size() > 2) {
                PromotionType promotionType = ptList.get(2);
                ImageManager.getInstance().displayImg((ImageView) holder.getView(R.id.img2), promotionType.getPicUrl(), R.drawable.img_default_v);
                holder.setText(R.id.tv2, promotionType.getPromotionName());
                holder.getView(R.id.view2).setOnClickListener(lsner);
            }
            if (ptList.size() > 3) {
                PromotionType promotionType = ptList.get(3);
                ImageManager.getInstance().displayImg((ImageView) holder.getView(R.id.img3), promotionType.getPicUrl(), R.drawable.img_default_v);
                holder.setText(R.id.tv3, promotionType.getPromotionName());
                holder.getView(R.id.view3).setOnClickListener(lsner);
            }
        }

    }
}
