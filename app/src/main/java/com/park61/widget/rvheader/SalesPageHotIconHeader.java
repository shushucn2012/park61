package com.park61.widget.rvheader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.park61.R;

/**
 * 商城主页促销导航栏模块
 */
public class SalesPageHotIconHeader extends RelativeLayout{

    public SalesPageHotIconHeader(Context context) {
        super(context);
        init(context);
    }

    public SalesPageHotIconHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SalesPageHotIconHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        inflate(context, R.layout.salespage_hoticon_layout, this);
    }
}