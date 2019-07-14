package com.park61.moduel.firsthead.adapterdelegate;

import android.content.Context;

import com.park61.R;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.widget.myrv.base.ItemViewDelegate;
import com.park61.widget.myrv.base.ViewHolder;

/**
 * Created by shubei on 2017/6/12.
 */

public class EmptyViewDelegate implements ItemViewDelegate<FirstHeadBean> {

    private Context mContext;

    public EmptyViewDelegate(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.firsthead_one_big_pic;
    }

    @Override
    public boolean isForViewType(FirstHeadBean item, int position) {
        return item.getClassifyType() == -2;
    }

    @Override
    public void convert(ViewHolder holder, final FirstHeadBean firstHeadBean, int position) {
        //点击加1
        firstHeadBean.setItemReadNum(firstHeadBean.getItemReadNum()+1);
    }
}
