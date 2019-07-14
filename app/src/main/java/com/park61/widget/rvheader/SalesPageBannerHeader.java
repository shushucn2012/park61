package com.park61.widget.rvheader;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.park61.R;
import com.park61.moduel.acts.bean.BannerItem;
import com.park61.widget.viewpager.MyBanner;

import java.util.List;

/**
 * 商城主页banner模块
 */
public class SalesPageBannerHeader extends RelativeLayout {

    private List<BannerItem> bannerlList;
    private  MyBanner mp;

    public SalesPageBannerHeader(Context context, List<BannerItem> _bannerlList) {
        super(context);
        this.bannerlList = _bannerlList;
        init(context);
    }

    public SalesPageBannerHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SalesPageBannerHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        View rootView = inflate(context, R.layout.salestop_banner_layout, this);
        ViewPager top_viewpager = (ViewPager) rootView.findViewById(R.id.top_viewpager);
        LinearLayout top_vp_dot = (LinearLayout) rootView.findViewById(R.id.top_vp_dot);;
        mp = new MyBanner(context, top_viewpager, top_vp_dot);
        mp.init(bannerlList);
    }

    public void refreshData(List<BannerItem> _bannerlList){
        this.bannerlList = _bannerlList;
        mp.init(bannerlList);
    }

}