package com.park61.widget.rvheader;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
 * 商城主页促销模块
 */
public class SalesPagePromotionCateHeader extends RelativeLayout implements View.OnClickListener {

    private ImageView img_sk, img_area1, img_area2, img_area3;
    private Context mContext;

    private List<PromotionType> plist = new ArrayList<>();

    public SalesPagePromotionCateHeader(Context context, List<PromotionType> _plist) {
        super(context);
        this.plist = _plist;
        init(context);
    }

    public SalesPagePromotionCateHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SalesPagePromotionCateHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        View root = inflate(context, R.layout.temai_mid_v2_layout, this);
        img_sk = (ImageView) root.findViewById(R.id.img_sk);
        img_area1 = (ImageView) root.findViewById(R.id.img_area1);
        img_area2 = (ImageView) root.findViewById(R.id.img_area2);
        img_area3 = (ImageView) root.findViewById(R.id.img_area3);

        initData();
    }

    private void initData() {
        if (plist.size() > 0) {
            PromotionType promotionType = plist.get(0);
            ImageManager.getInstance().displayImg(img_sk, promotionType.getPicUrl(), R.drawable.img_default_v);
            img_sk.setOnClickListener(this);
        }
        if (plist.size() > 1) {
            PromotionType promotionType = plist.get(1);
            ImageManager.getInstance().displayImg(img_area1, promotionType.getPicUrl(), R.drawable.img_default_h);
            img_area1.setOnClickListener(this);
        }
        if (plist.size() > 2) {
            PromotionType promotionType = plist.get(2);
            ImageManager.getInstance().displayImg(img_area2, promotionType.getPicUrl(), R.drawable.img_default_v);
            img_area2.setOnClickListener(this);
        }
        if (plist.size() > 3) {
            PromotionType promotionType = plist.get(3);
            ImageManager.getInstance().displayImg(img_area3, promotionType.getPicUrl(), R.drawable.img_default_v);
            img_area3.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int pos = 0;
        String promotionType = null;
        String title = "";
        switch (v.getId()) {
            case R.id.img_sk:
                pos = 0;
                break;
            case R.id.img_area1:
                pos = 1;
                break;
            case R.id.img_area2:
                pos = 2;
                break;
            case R.id.img_area3:
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