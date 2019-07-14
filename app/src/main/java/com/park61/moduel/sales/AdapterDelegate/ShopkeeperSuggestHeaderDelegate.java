package com.park61.moduel.sales.AdapterDelegate;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.adapter.RecommendGoodsListAdapter;
import com.park61.moduel.acts.bean.RecommendGoodsInfo;
import com.park61.moduel.grouppurchase.GroupPurchaseActivity;
import com.park61.moduel.sales.BrandClassifyActivity;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.MaMaGroupActivity;
import com.park61.moduel.sales.OverValueActivity;
import com.park61.moduel.sales.OverseasGoodsActivity;
import com.park61.moduel.sales.SecKillActivityNew;
import com.park61.moduel.sales.ShopkeeperSugGoodsActivity;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.moduel.sales.bean.PromotionType;
import com.park61.widget.list.HorizontalListViewV2;
import com.park61.widget.myrv.base.ItemViewDelegate;
import com.park61.widget.myrv.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by shubei on 2017/5/19.
 */
public class ShopkeeperSuggestHeaderDelegate implements ItemViewDelegate<GoodsCombine> {

    private Context mContext;

    public ShopkeeperSuggestHeaderDelegate(Context c) {
        mContext = c;
    }


    @Override
    public int getItemViewLayoutId() {
        return R.layout.shopkeeper_suggest_layout;
    }

    @Override
    public boolean isForViewType(GoodsCombine item, int position) {
        return item.getTemplete().getTempleteCode().equals("shop_manager_recommend");
    }

    @Override
    public void convert(ViewHolder holder, GoodsCombine item, int position) {
        HorizontalListViewV2 horilistview = (HorizontalListViewV2) holder.getView(R.id.horilistview);
        View area_more = holder.getView(R.id.area_more);
        ImageView img_title = (ImageView) holder.getView(R.id.img_title);
        JSONArray ptJay = null;
        try {
            ptJay = new JSONArray(item.getTemplete().getTempleteData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ptJay != null && ptJay.length() > 0) {//如果数组不为空
            final List<RecommendGoodsInfo> rgList = new ArrayList<>();
            for (int j = 0; j < ptJay.length(); j++) {
                JSONObject rgItemJot = ptJay.optJSONObject(j);
                RecommendGoodsInfo rgItem = new RecommendGoodsInfo();
                rgItem.setPmInfoid(rgItemJot.optLong("pmInfoid"));
                rgItem.setImg(rgItemJot.optString("img"));
                rgItem.setName(rgItemJot.optString("name"));
                rgItem.setOldPrice(rgItemJot.optString("oldPrice"));
                rgItem.setSalesPrice(rgItemJot.optString("salesPrice"));
                rgItem.setAvailableNum(rgItemJot.optString("availableNum"));
                rgList.add(rgItem);
            }

            if (item.getTemplete().getTempleteHead() != null) {
                String isShowMore = item.getTemplete().getTempleteHead().getMoreBtnCanShow();
                boolean isShowMoreFlag = false;
                if (!TextUtils.isEmpty(isShowMore) && !"0".equals(isShowMore)) {
                    isShowMoreFlag = true;
                }
                if (isShowMoreFlag) {
                    area_more.setVisibility(VISIBLE);
                } else {
                    area_more.setVisibility(GONE);
                }
                String titleImgUrl = item.getTemplete().getTempleteHead().getTitlePicUrl();
                Log.out("titleImgUrl======" + titleImgUrl);
                ImageManager.getInstance().displayImg(img_title, titleImgUrl);
            }

            RecommendGoodsListAdapter mAdapter = new RecommendGoodsListAdapter(mContext, (ArrayList<RecommendGoodsInfo>) rgList);
            horilistview.setAdapter(mAdapter);
            horilistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent it = new Intent(mContext, GoodsDetailsActivity.class);
                    it.putExtra("goodsId", rgList.get(position).getPmInfoid());
                    it.putExtra("promotionId", rgList.get(position).getPromotionId());
                    it.putExtra("goodsName", rgList.get(position).getName());
                    it.putExtra("goodsOldPrice", rgList.get(position).getOldPrice());
                    it.putExtra("goodsPrice", rgList.get(position).getSalesPrice());
                    mContext.startActivity(it);
                }
            });
            area_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, ShopkeeperSugGoodsActivity.class);
                    it.putExtra("GOODS_LIST", (Serializable) rgList);
                    mContext.startActivity(it);
                }
            });

        }

    }
}
