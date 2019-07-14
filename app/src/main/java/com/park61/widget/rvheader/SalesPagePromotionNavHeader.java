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
import com.park61.moduel.grouppurchase.GroupPurchaseActivity;
import com.park61.moduel.sales.BrandClassifyActivity;
import com.park61.moduel.sales.MaMaGroupActivity;
import com.park61.moduel.sales.OverValueActivity;
import com.park61.moduel.sales.OverseasGoodsActivity;
import com.park61.moduel.sales.SecKillActivityNew;
import com.park61.moduel.sales.bean.PromotionType;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城主页促销导航栏模块
 */
public class SalesPagePromotionNavHeader extends RelativeLayout implements View.OnClickListener {

    private View view0, view1, view2, view3;
    private ImageView img0, img1, img2, img3;
    private TextView tv0, tv1, tv2, tv3;
    private Context mContext;

    private List<PromotionType> plist = new ArrayList<>();

    public SalesPagePromotionNavHeader(Context context, List<PromotionType> _plist) {
        super(context);
        this.plist = _plist;
        init(context);
    }

    public SalesPagePromotionNavHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SalesPagePromotionNavHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        View root = inflate(context, R.layout.salespage_promotion_nav_layout, this);
        view0 = root.findViewById(R.id.view0);
        view1 = root.findViewById(R.id.view1);
        view2 = root.findViewById(R.id.view2);
        view3 = root.findViewById(R.id.view3);

        img0 = (ImageView) root.findViewById(R.id.img0);
        img1 = (ImageView) root.findViewById(R.id.img1);
        img2 = (ImageView) root.findViewById(R.id.img2);
        img3 = (ImageView) root.findViewById(R.id.img3);

        tv0 = (TextView) root.findViewById(R.id.tv0);
        tv1 = (TextView) root.findViewById(R.id.tv1);
        tv2 = (TextView) root.findViewById(R.id.tv2);
        tv3 = (TextView) root.findViewById(R.id.tv3);

        initData();
    }

    private void initData() {
        if (plist.size() > 0) {
            PromotionType promotionType = plist.get(0);
            ImageManager.getInstance().displayImg(img0, promotionType.getPicUrl(), R.drawable.img_default_v);
            tv0.setText(promotionType.getPromotionName());
            view0.setOnClickListener(this);
        }
        if (plist.size() > 1) {
            PromotionType promotionType = plist.get(1);
            ImageManager.getInstance().displayImg(img1, promotionType.getPicUrl(), R.drawable.img_default_v);
            tv1.setText(promotionType.getPromotionName());
            view1.setOnClickListener(this);
        }
        if (plist.size() > 2) {
            PromotionType promotionType = plist.get(2);
            ImageManager.getInstance().displayImg(img2, promotionType.getPicUrl(), R.drawable.img_default_v);
            tv2.setText(promotionType.getPromotionName());
            view2.setOnClickListener(this);
        }
        if (plist.size() > 3) {
            PromotionType promotionType = plist.get(3);
            ImageManager.getInstance().displayImg(img3, promotionType.getPicUrl(), R.drawable.img_default_v);
            tv3.setText(promotionType.getPromotionName());
            view3.setOnClickListener(this);
        }
    }

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
        promotionType = plist.get(pos).getPromotionType();
        title = plist.get(pos).getPromotionName();
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

}