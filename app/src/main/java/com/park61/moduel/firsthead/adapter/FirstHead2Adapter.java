package com.park61.moduel.firsthead.adapter;

import android.content.Context;

import com.park61.moduel.firsthead.adapterdelegate.OneBigPicDelegate;
import com.park61.moduel.firsthead.adapterdelegate.OneBigVideo2Delegate;
import com.park61.moduel.firsthead.adapterdelegate.OneBigVideoDelegate;
import com.park61.moduel.firsthead.adapterdelegate.OneSmallPicDelegate;
import com.park61.moduel.firsthead.adapterdelegate.OneSmallVideoDelegate;
import com.park61.moduel.firsthead.adapterdelegate.PopularizeBannerDelegate;
import com.park61.moduel.firsthead.adapterdelegate.ThreeSmallPicDelegate;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.widget.myrv.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by super
 */
public class FirstHead2Adapter extends MultiItemTypeAdapter<FirstHeadBean> {

    public FirstHead2Adapter(Context context, List<FirstHeadBean> mlsit) {
        super(context, mlsit);
        addItemViewDelegate(new PopularizeBannerDelegate(context));// banner
        addItemViewDelegate(new OneSmallPicDelegate(context));// one_small_pic
        addItemViewDelegate(new ThreeSmallPicDelegate(context));// three_small_pic
        addItemViewDelegate(new OneBigVideo2Delegate(context));// one_big_videio
        addItemViewDelegate(new OneBigPicDelegate(context));// one_big_pic
        addItemViewDelegate(new OneSmallVideoDelegate(context));// one_small_video
    }
}