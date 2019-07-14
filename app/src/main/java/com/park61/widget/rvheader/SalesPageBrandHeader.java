package com.park61.widget.rvheader;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.BrandClassifyActivity;
import com.park61.moduel.sales.SalesSrceeningActivity;
import com.park61.moduel.sales.bean.BrandBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城主页促销导航栏模块
 */
public class SalesPageBrandHeader extends RelativeLayout implements View.OnClickListener {

    private View sales_brand_row0, sales_brand_row1, view0, view1, view2, view3, view4, view5, view6, view7;
    private ImageView img0, img1, img2, img3, img4, img5, img6, img7;
    private TextView tv0, tv1, tv2, tv3, tv4, tv5, tv6, tv7;
    private Context mContext;
    private boolean isShowMore;
    private String titleImgUrl;

    private List<BrandBean> blist = new ArrayList<>();

    public SalesPageBrandHeader(Context context, List<BrandBean> _blist, boolean isShowMore, String titleImgUrl) {
        super(context);
        this.blist = _blist;
        this.isShowMore = isShowMore;
        this.titleImgUrl = titleImgUrl;
        init(context);
    }

    public SalesPageBrandHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SalesPageBrandHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        View root = inflate(context, R.layout.salespage_brand_layout, this);
        ImageView img_title = (ImageView) root.findViewById(R.id.img_title);
        sales_brand_row0 = root.findViewById(R.id.sales_brand_row0);
        sales_brand_row1 = root.findViewById(R.id.sales_brand_row1);

        view0 = root.findViewById(R.id.view0);
        view1 = root.findViewById(R.id.view1);
        view2 = root.findViewById(R.id.view2);
        view3 = root.findViewById(R.id.view3);
        view4 = root.findViewById(R.id.view4);
        view5 = root.findViewById(R.id.view5);
        view6 = root.findViewById(R.id.view6);
        view7 = root.findViewById(R.id.view7);

        img0 = (ImageView) root.findViewById(R.id.img0);
        img1 = (ImageView) root.findViewById(R.id.img1);
        img2 = (ImageView) root.findViewById(R.id.img2);
        img3 = (ImageView) root.findViewById(R.id.img3);
        img4 = (ImageView) root.findViewById(R.id.img4);
        img5 = (ImageView) root.findViewById(R.id.img5);
        img6 = (ImageView) root.findViewById(R.id.img6);
        img7 = (ImageView) root.findViewById(R.id.img7);

        tv0 = (TextView) root.findViewById(R.id.tv0);
        tv1 = (TextView) root.findViewById(R.id.tv1);
        tv2 = (TextView) root.findViewById(R.id.tv2);
        tv3 = (TextView) root.findViewById(R.id.tv3);
        tv4 = (TextView) root.findViewById(R.id.tv4);
        tv5 = (TextView) root.findViewById(R.id.tv5);
        tv6 = (TextView) root.findViewById(R.id.tv6);
        tv7 = (TextView) root.findViewById(R.id.tv7);

        View area_more = root.findViewById(R.id.area_more);
        if (isShowMore) {
            area_more.setVisibility(VISIBLE);
        } else {
            area_more.setVisibility(GONE);
        }
        area_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, BrandClassifyActivity.class);
                it.putExtra("SHOW_WHICH", "SHOW_BRAND");
                mContext.startActivity(it);
            }
        });
        ImageManager.getInstance().displayImg(img_title, titleImgUrl, R.drawable.pinpaituijian);

        initData();
    }

    private void initData() {
        if (blist.size() > 0) {
            BrandBean brandBean = blist.get(0);
            ImageManager.getInstance().displayImg(img0, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv0.setText(brandBean.getBrandName());
            view0.setOnClickListener(this);
        }
        if (blist.size() > 1) {
            BrandBean brandBean = blist.get(1);
            ImageManager.getInstance().displayImg(img1, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv1.setText(brandBean.getBrandName());
            view1.setOnClickListener(this);
        }
        if (blist.size() > 2) {
            BrandBean brandBean = blist.get(2);
            ImageManager.getInstance().displayImg(img2, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv2.setText(brandBean.getBrandName());
            view2.setOnClickListener(this);
        }
        if (blist.size() > 3) {
            BrandBean brandBean = blist.get(3);
            ImageManager.getInstance().displayImg(img3, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv3.setText(brandBean.getBrandName());
            view3.setOnClickListener(this);
        }
        if (blist.size() > 4) {
            sales_brand_row1.setVisibility(VISIBLE);
        } else {
            sales_brand_row1.setVisibility(GONE);
        }
        if (blist.size() > 4) {
            BrandBean brandBean = blist.get(4);
            ImageManager.getInstance().displayImg(img4, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv4.setText(brandBean.getBrandName());
            view4.setOnClickListener(this);
        }
        if (blist.size() > 5) {
            BrandBean brandBean = blist.get(5);
            ImageManager.getInstance().displayImg(img5, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv5.setText(brandBean.getBrandName());
            view5.setOnClickListener(this);
        }
        if (blist.size() > 6) {
            BrandBean brandBean = blist.get(6);
            ImageManager.getInstance().displayImg(img6, brandBean.getBrandLogoUrl(), R.drawable.img_default_v);
            tv6.setText(brandBean.getBrandName());
            view6.setOnClickListener(this);
        }
        if (blist.size() > 7) {
            BrandBean brandBean = blist.get(7);
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
        brandId = blist.get(pos).getId();
        Intent it = new Intent(mContext, SalesSrceeningActivity.class);
        it.putExtra("BRAND_ID", brandId + "");
        mContext.startActivity(it);
    }

}