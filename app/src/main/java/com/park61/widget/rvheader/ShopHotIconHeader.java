package com.park61.widget.rvheader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.park61.R;

/**
 * 商城主页促销导航栏模块
 */
public class ShopHotIconHeader extends RelativeLayout{

    public ShopHotIconHeader(Context context) {
        super(context);
        init(context);
    }

    public ShopHotIconHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShopHotIconHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        inflate(context, R.layout.shop_hoticon_layout, this);
    }
}