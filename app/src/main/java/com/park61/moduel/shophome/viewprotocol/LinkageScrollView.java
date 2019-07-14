package com.park61.moduel.shophome.viewprotocol;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

/**
 * Created by ouyangji on 17/3/13.
 */

public interface LinkageScrollView {

    HorizontalScrollView getMainScrollView();

    void setMainScrollView(HorizontalScrollView pHorizontalScrollView);

    void onScrollChanged(int l, int t, int oldl, int oldt);

    int getSelectWeekIndex();

    void setSelectWeekIndex(int pSelectWeenPosition);

    void changeTopbarWeek(int week, FrameLayout parentFlayout, View[] topBarFlayoutChild);

    void addHViews(final UpDownLeftRightScrollView hScrollView);

    View[] getTopBarFlayout();
}
