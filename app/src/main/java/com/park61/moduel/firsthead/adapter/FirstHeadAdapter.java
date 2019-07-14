package com.park61.moduel.firsthead.adapter;

import android.content.Context;

import com.park61.moduel.firsthead.adapterdelegate.EmptyViewDelegate;
import com.park61.moduel.firsthead.adapterdelegate.OneBigPicDelegate;
import com.park61.moduel.firsthead.adapterdelegate.OneBigVideoDelegate;
import com.park61.moduel.firsthead.adapterdelegate.OneSmallPicDelegate;
import com.park61.moduel.firsthead.adapterdelegate.OneSmallVideoDelegate;
import com.park61.moduel.firsthead.adapterdelegate.PopularizeBannerDelegate;
import com.park61.moduel.firsthead.adapterdelegate.PopularizeBannerOneSmallDelegate;
import com.park61.moduel.firsthead.adapterdelegate.PopularizeBannerThreeDelegate;
import com.park61.moduel.firsthead.adapterdelegate.ThreeSmallPicDelegate;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.widget.myrv.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by super
 */
public class FirstHeadAdapter extends MultiItemTypeAdapter<FirstHeadBean> {

    public FirstHeadAdapter(Context context, List<FirstHeadBean> mlsit) {
        super(context, mlsit);
        addItemViewDelegate(new PopularizeBannerDelegate(context));// banner
        addItemViewDelegate(new OneSmallPicDelegate(context));// one_small_pic
        addItemViewDelegate(new ThreeSmallPicDelegate(context));// three_small_pic
        addItemViewDelegate(new OneBigVideoDelegate(context));// one_big_videio
        addItemViewDelegate(new OneBigPicDelegate(context));// one_big_pic
        addItemViewDelegate(new EmptyViewDelegate(context));// null
        addItemViewDelegate(new OneSmallVideoDelegate(context));// one_small_video
        addItemViewDelegate(new PopularizeBannerOneSmallDelegate(context));// one_small_video
        addItemViewDelegate(new PopularizeBannerThreeDelegate(context));// one_small_video
    }
}