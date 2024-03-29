package com.park61.moduel.dreamhouse.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.park61.common.tool.DevAttr;

/**
 * Base on https://github.com/Frank-Zhu/PullZoomView
 * Thanks to Frank-Zhu
 */
public class PullToZoomListViewEx extends PullToZoomBase<ListView> implements AbsListView.OnScrollListener {

    private static final String TAG = PullToZoomListViewEx.class.getSimpleName();
    private float mOldValue;
    private float mD;
    private int mHeaderHeight;
    public FrameLayout mHeaderContainer;
    private ScalingRunnable mScalingRunnable;
    private SrollToShowListener mSrollToShowListener;
    int lastItem;
    private Context mContext;


    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float paramAnonymousFloat) {
            float f = paramAnonymousFloat - 1.0F;
            return 1.0F + f * (f * (f * (f * f)));
        }
    };

    public PullToZoomListViewEx(Context context) {
        this(context, null);
    }

    public PullToZoomListViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRootView.setOnScrollListener(this);
        mScalingRunnable = new ScalingRunnable();
        mContext = context;
    }

    /**
     * 是否显示headerView
     *
     * @param isHideHeader true: show false: hide
     */
    @Override
    public void setHideHeader(boolean isHideHeader) {
        if (isHideHeader != isHideHeader()) {
            super.setHideHeader(isHideHeader);
            if (isHideHeader) {
                removeHeaderView();
            } else {
                updateHeaderView();
            }
        }
    }


    @Override
    public void setHeaderView(BezierView headerView) {
        if (headerView != null) {
            this.mHeaderView = headerView;
            updateHeaderView();
        }
    }

    @Override
    public void setZoomView(View zoomView) {
        if (zoomView != null) {
            this.mZoomView = zoomView;
            updateHeaderView();
        }
    }

    private void removeHeaderView() {
        if (mHeaderContainer != null) {
            mRootView.removeHeaderView(mHeaderContainer);
        }
    }

    private void updateHeaderView() {
        if (mHeaderContainer != null) {
            mRootView.removeHeaderView(mHeaderContainer);

            mHeaderContainer.removeAllViews();

            if (mZoomView != null) {
                mHeaderContainer.addView(mZoomView);
            }

            if (mHeaderView != null) {
                mHeaderContainer.addView(mHeaderView);
            }

            if (mBezierView != null) {
                mHeaderContainer.addView(mBezierView);
            }

            mHeaderHeight = mHeaderContainer.getHeight();
            mRootView.addHeaderView(mHeaderContainer);
        }
    }

    public void setAdapter(ListAdapter adapter) {
        mRootView.setAdapter(adapter);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mRootView.setOnItemClickListener(listener);
    }

    @Override
    protected ListView createRootView(Context context, AttributeSet attrs) {
        ListView lv = new ListView(context, attrs);
        // Set it to this so it can be used in ListActivity/ListFragment
        lv.setId(android.R.id.list);
        return lv;
    }

    /**
     * 重置动画，自动滑动到顶部
     */
    @Override
    protected void smoothScrollToTop() {
        Log.d(TAG, "smoothScrollToTop --> ");
        mScalingRunnable.startAnimation(200L);
    }

    @Override
    protected void pullHeaderToZoom(int newScrollValue) {
        Log.d(TAG, "pullHeaderToZoom --> newScrollValue = " + newScrollValue);
        Log.d(TAG, "pullHeaderToZoom --> mHeaderHeight = " + mHeaderHeight);
        if (mScalingRunnable != null && !mScalingRunnable.isFinished()) {
            mScalingRunnable.abortAnimation();
        }

        ViewGroup.LayoutParams localLayoutParams = mHeaderContainer.getLayoutParams();
        localLayoutParams.height = Math.abs(newScrollValue) + mHeaderHeight;
        mHeaderContainer.setLayoutParams(localLayoutParams);
    }


    @Override
    protected boolean isReadyForPullStart() {
        return isFirstItemVisible();
    }

    private boolean isFirstItemVisible() {
        final Adapter adapter = mRootView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        } else {
            /**
             * This check should really just be:
             * mRootView.getFirstVisiblePosition() == 0, but PtRListView
             * internally use a HeaderView which messes the positions up. For
             * now we'll just add one to account for it and rely on the inner
             * condition which checks getTop().
             */
            if (mRootView.getFirstVisiblePosition() <= 1) {
                final View firstVisibleChild = mRootView.getChildAt(0);
                if (firstVisibleChild != null) {
                    return firstVisibleChild.getTop() >= mRootView.getTop();
                }
            }
        }

        return false;
    }

    @Override
    public void handleStyledAttributes(TypedArray a) {
        mHeaderContainer = new FrameLayout(getContext());
        if (mZoomView != null) {
            mHeaderContainer.addView(mZoomView);
        }
        if (mHeaderView != null) {
            mHeaderContainer.addView(mHeaderView);
        }
        if (mBezierView != null) {
            mHeaderContainer.addView(mBezierView);
        }

        mRootView.addHeaderView(mHeaderContainer);
    }

    /**
     * 设置HeaderView高度
     *
     * @param width
     * @param height
     */
    public void setHeaderViewSize(int width, int height) {
        if (mHeaderContainer != null) {
            Object localObject = mHeaderContainer.getLayoutParams();
            if (localObject == null) {
                localObject = new AbsListView.LayoutParams(width, height);
            }
            ((ViewGroup.LayoutParams) localObject).width = width;
            ((ViewGroup.LayoutParams) localObject).height = height;
            mHeaderContainer.setLayoutParams((ViewGroup.LayoutParams) localObject);
            mHeaderHeight = height;
        }
    }

    public void setHeaderLayoutParams(AbsListView.LayoutParams layoutParams) {
        if (mHeaderContainer != null) {
            mHeaderContainer.setLayoutParams(layoutParams);
            mHeaderHeight = layoutParams.height;
        }
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2,
                            int paramInt3, int paramInt4) {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        Log.d(TAG, "onLayout --> ");
        if (mHeaderHeight == 0 && mHeaderContainer != null) {
            mHeaderHeight = mHeaderContainer.getHeight();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d(TAG, "onScrollStateChanged --> ");
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                mSrollToShowListener.loadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mZoomView != null && !isHideHeader() && isPullToZoomEnabled()) {
            Log.e(TAG, "mHeaderHeight --> mHeaderHeight = " + mHeaderHeight);
            Log.e(TAG, "mHeaderContainer.getBottom() --> mHeaderContainer.getBottom() = " + mHeaderContainer.getBottom());
            Log.e(TAG, "mOldValue --> mOldValue = " + mOldValue);
            float f = mHeaderHeight - mHeaderContainer.getBottom();
            if (mSrollToShowListener != null) {
                mSrollToShowListener.scrolling(mHeaderContainer.getBottom() - DevAttr.dip2px(mContext, 60), firstVisibleItem);
            }
            mD = f - mOldValue;
            mOldValue = f;
            if (f >= 200f) {
                mSrollToShowListener.srollOnShow();
            } else {
                if (mSrollToShowListener != null) {
                    mSrollToShowListener.srollHide();
                }
            }
            Log.e(TAG, "onScroll --> f = " + f);
            if (mBezierView != null) {
                Log.e(TAG, "mBezierView.getArcHeight() --> f = " + mBezierView.getArcHeight());
                //mBezierView.setArcHeight(mBezierView.getArcHeight() + mD);
            }
            Log.d(TAG, "onScroll --> mD = " + mD);
        }
    }

    public interface SrollToShowListener {
        public void srollOnShow();

        public void srollHide();

        public void loadMore();

        public void scrolling(int top, int firstVisibleItem);
    }

    public void setSrollToShowListener(SrollToShowListener lsner) {
        mSrollToShowListener = lsner;
    }


    class ScalingRunnable implements Runnable {
        protected long mDuration;
        protected boolean mIsFinished = true;
        protected float mScale;
        protected long mStartTime;

        ScalingRunnable() {
        }

        public void abortAnimation() {
            mIsFinished = true;
        }

        public boolean isFinished() {
            return mIsFinished;
        }

        public void run() {
            if (mZoomView != null) {
                float f2;
                ViewGroup.LayoutParams localLayoutParams;
                if ((!mIsFinished) && (mScale > 1.0D)) {
                    float f1 = ((float) SystemClock.currentThreadTimeMillis() - (float) mStartTime) / (float) mDuration;
                    f2 = mScale - (mScale - 1.0F) * PullToZoomListViewEx.sInterpolator.getInterpolation(f1);
                    localLayoutParams = mHeaderContainer.getLayoutParams();

                    if (f2 > 1.0F) {
                        localLayoutParams.height = ((int) (f2 * mHeaderHeight));
                        mHeaderContainer.setLayoutParams(localLayoutParams);
                        post(this);
                        return;
                    }
                    mIsFinished = true;
                }
            }
        }

        public void startAnimation(long paramLong) {
            if (mZoomView != null) {
                mStartTime = SystemClock.currentThreadTimeMillis();
                mDuration = paramLong;
                mScale = ((float) (mHeaderContainer.getBottom()) / mHeaderHeight);
                mIsFinished = false;
                post(this);
            }
        }
    }

}
