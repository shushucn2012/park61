package com.park61.moduel.firsthead.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.park61.R;
import com.park61.moduel.acts.bean.BannerItem;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by chenlie on 2017/12/26.
 *
 * bga banner控件
 */

public class ParentalAdapter implements BGABanner.Adapter<ImageView,BannerItem> {

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, BannerItem model, int position) {
        Glide.with(itemView.getContext()).load(model.getImg()).placeholder(R.drawable.img_default_h)
                .error(R.drawable.img_default_h).centerCrop().dontAnimate().into(itemView);
    }
}
