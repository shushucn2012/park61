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
 * RecyclerView的HeaderView，简单的展示banner
 */
public class BannerHeader extends RelativeLayout {

    private List<BannerItem> bannerlList;
    private  MyBanner mp;

    public BannerHeader(Context context, List<BannerItem> _bannerlList) {
        super(context);
        this.bannerlList = _bannerlList;
        init(context);
    }

    public BannerHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BannerHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        View rootView = inflate(context, R.layout.top_banner_layout, this);
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