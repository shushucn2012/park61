package com.park61.moduel.sales.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by shushucn2012 on 2016/12/7.
 */
public class UpDownPageTransformer implements ViewPager.PageTransformer {
    public float MIN_SCALE = 0.75f;

    @Override
    public void transformPage(View view, float position) {
        int pageHeight = view.getHeight();
        int pageWidth = view.getWidth();
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);
        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when
            // moving to the left page
            view.setAlpha(1 - Math.abs(position));
            view.setTranslationX(pageWidth * -position);
            view.setTranslationY(pageHeight * position);
        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            view.setAlpha(1 - position);
            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);
            view.setTranslationY(pageHeight * position);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}
