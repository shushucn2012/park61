package com.park61.moduel.shophome.viewprotocol;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by ouyangji on 17/3/10.
 */

public class UpDownLeftRightScrollView extends HorizontalScrollView {

    LinkageScrollView activity;

    public UpDownLeftRightScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        activity = (LinkageScrollView) context;
    }


    public UpDownLeftRightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (LinkageScrollView) context;
    }

    public UpDownLeftRightScrollView(Context context) {
        super(context);
        activity = (LinkageScrollView) context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //进行触摸赋值
        activity.setMainScrollView(this);
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        //当当前的UpDownLeftRightScrollView被触摸时，滑动其它
        if(activity.getMainScrollView() == this) {
            activity.onScrollChanged(l, t, oldl, oldt);
        }else{
            super.onScrollChanged(l, t, oldl, oldt);
        }
    }
}
