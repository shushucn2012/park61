package com.park61.moduel.sales.AdapterDelegate;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.adapter.RecommendGoodsListAdapter;
import com.park61.moduel.acts.bean.RecommendGoodsInfo;
import com.park61.moduel.sales.BrandClassifyActivity;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.SalesSrceeningActivity;
import com.park61.moduel.sales.ShopkeeperSugGoodsActivity;
import com.park61.moduel.sales.bean.BrandBean;
import com.park61.moduel.sales.bean.GoodsCombine;
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
public class BrandRecommendDelegate implements ItemViewDelegate<GoodsCombine> ,View.OnClickListener{

    private View sales_brand_row0, sales_brand_row1, view0, view1, view2, view3, view4, view5, view6, view7;
    private ImageView img0, img1, img2, img3, img4, img5, img6, img7;
    private TextView tv0, tv1, tv2, tv3, tv4, tv5, tv6, tv7;
    private boolean isShowMoreFlag;
    private String titleImgUrl;
    private List<BrandBean> bList;

    private Context mContext;

    public BrandRecommendDelegate(Context c) {
        mContext = c;
    }


    @Override
    public int getItemViewLayoutId() {
        return R.layout.salespage_brand_layout;
    }

    @Override
    public boolean isForViewType(GoodsCombine item, int position) {
        return item.getTemplete().getTempleteCode().equals("brand_recommend");
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
            bList = new ArrayList<>();
            for (int j = 0; j < ptJay.length(); j++) {
                JSONObject bItemJot = ptJay.optJSONObject(j);
                BrandBean bItem = new BrandBean();
                bItem.setId(bItemJot.optLong("id"));
                bItem.setBrandLogoUrl(bItemJot.optString("brandLogoUrl"));
                bItem.setBrandName(bItemJot.optString("brandName"));
                bItem.setBrandCompanyName(bItemJot.optString("brandCompanyName"));
                bList.add(bItem);
            }

            ImageView img_title = holder.getView(R.id.img_title);
            sales_brand_row0 = holder.getView(R.id.sales_brand_row0);
            sales_brand_row1 = holder.getView(R.id.sales_brand_row1);

            view0 = holder.getView(R.id.view0);
            view1 = holder.getView(R.id.view1);
            view2 = holder.getView(R.id.view2);
            view3 = holder.getView(R.id.view3);
            view4 = holder.getView(R.id.view4);
            view5 = holder.getView(R.id.view5);
            view6 = holder.getView(R.id.view6);
            view7 = holder.getView(R.id.view7);

            img0 = holder.getView(R.id.img0);
            img1 = holder.getView(R.id.img1);
            img2 = holder.getView(R.id.img2);
            img3 = holder.getView(R.id.img3);
            img4 = holder.getView(R.id.img4);
            img5 = holder.getView(R.id.img5);
            img6 = holder.getView(R.id.img6);
            img7 = holder.getView(R.id.img7);

            tv0 = holder.getView(R.id.tv0);
            tv1 = holder.getView(R.id.tv1);
            tv2 = holder.getView(R.id.tv2);
            tv3 = holder.getView(R.id.tv3);
            tv4 = holder.getView(R.id.tv4);
            tv5 = holder.getView(R.id.tv5);
            tv6 = holder.getView(R.id.tv6);
            tv7 = holder.getView(R.id.tv7);
            View area_more = holder.getView(R.id.area_more);

            area_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, BrandClassifyActivity.class);
                    it.putExtra("SHOW_WHICH", "SHOW_BRAND");
                    mContext.startActivity(it);
                }
            });

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
                ImageManager.getInstance().displayImg(img_title, titleImgUrl, R.drawable.pinpaituijian);
            }
            initData();
        }
    }

    private void initData() {
        if (bList.size() > 0) {
            BrandBean brandBean = bList.get(0);
            ImageManager.getInstance().displayImg(img0, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv0.setText(brandBean.getBrandName());
            view0.setOnClickListener(this);
        }
        if (bList.size() > 1) {
            BrandBean brandBean = bList.get(1);
            ImageManager.getInstance().displayImg(img1, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv1.setText(brandBean.getBrandName());
            view1.setOnClickListener(this);
        }
        if (bList.size() > 2) {
            BrandBean brandBean = bList.get(2);
            ImageManager.getInstance().displayImg(img2, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv2.setText(brandBean.getBrandName());
            view2.setOnClickListener(this);
        }
        if (bList.size() > 3) {
            BrandBean brandBean = bList.get(3);
            ImageManager.getInstance().displayImg(img3, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv3.setText(brandBean.getBrandName());
            view3.setOnClickListener(this);
        }
        if (bList.size() > 4) {
            sales_brand_row1.setVisibility(VISIBLE);
        } else {
            sales_brand_row1.setVisibility(GONE);
        }
        if (bList.size() > 4) {
            BrandBean brandBean = bList.get(4);
            ImageManager.getInstance().displayImg(img4, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv4.setText(brandBean.getBrandName());
            view4.setOnClickListener(this);
        }
        if (bList.size() > 5) {
            BrandBean brandBean = bList.get(5);
            ImageManager.getInstance().displayImg(img5, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv5.setText(brandBean.getBrandName());
            view5.setOnClickListener(this);
        }
        if (bList.size() > 6) {
            BrandBean brandBean = bList.get(6);
            ImageManager.getInstance().displayImg(img6, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv6.setText(brandBean.getBrandName());
            view6.setOnClickListener(this);
        }
        if (bList.size() > 7) {
            BrandBean brandBean = bList.get(7);
            ImageManager.getInstance().displayImg(img7, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv7.setText(brandBean.getBrandName());
            view7.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int pos = 0;
        long brandId = 0;
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
            case R.id.view4:
                pos = 4;
                break;
            case R.id.view5:
                pos = 5;
                break;
            case R.id.view6:
                pos = 6;
                break;
            case R.id.view7:
                pos = 7;
                break;
        }
        brandId = bList.get(pos).getId();
        Intent it = new Intent(mContext, SalesSrceeningActivity.class);
        it.putExtra("BRAND_ID", brandId + "");
        mContext.startActivity(it);
    }

}
