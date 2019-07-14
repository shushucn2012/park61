package com.park61.moduel.acts.course;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.bean.ActCouponSelectChildCache;
import com.park61.moduel.acts.bean.ActivitySessionVo;
import com.park61.moduel.acts.fragment.CourseListFragment;
import com.park61.widget.viewpager.PagerSlidingTabStrip;

/**
 * 小课选择课程页面
 */
public class CourseChooseActivity extends BaseActivity {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    public TextView tv_page_title;

    private DisplayMetrics dm;// 获取当前屏幕的密度
    private ActivitySessionVo activitySessionVo;
    private Long reqPredId;//梦想预报名id

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_course_choose);
    }

    @Override
    public void initView() {
        dm = getResources().getDisplayMetrics();
        tv_page_title = (TextView) findViewById(R.id.tv_page_title);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
    }

    @Override
    public void initData() {
        reqPredId = getIntent().getLongExtra("reqPredId",-1l);
        activitySessionVo = (ActivitySessionVo) getIntent().getSerializableExtra("activitySessionVo");
        tv_page_title.setText("61区" + activitySessionVo.getStoreVO().getStoreName());
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(pager);
        setTabsValue();
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(false);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        tabs.setScrollOffset(0);
        tabs.setTabPaddingLeftRight(DevAttr.dip2px(mContext, 10));
        tabs.setDividerPadding(0);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        tabs.setUnderlineColor(Color.TRANSPARENT);
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.com_orange));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(getResources().getColor(R.color.com_orange));
        // 设置正常Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setTextColor(getResources().getColor(R.color.g333333));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }

    @Override
    public void initListener() {
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (TextUtils.isEmpty(activitySessionVo.getActSessionList().get(position).getClassTime())) {
                return DateTool.L2SByDay3(activitySessionVo.getActSessionList().get(position).getActStartTime()) + "开始";
            }
            return DateTool.L2SByDay3(activitySessionVo.getActSessionList().get(position).getClassTime()) + "开始";
        }

        @Override
        public int getCount() {
            return activitySessionVo.getActSessionList().size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            CourseListFragment fragment = new CourseListFragment();
            Bundle data = new Bundle();
            data.putLong("sessionId", activitySessionVo.getActSessionList().get(position).getId());
            data.putString("actTitle", activitySessionVo.getActSessionList().get(position).getActTitle());
            data.putLong("reqPredId",reqPredId);
            String price = "";
            String adultPrice = "";
            if (activitySessionVo.getActSessionList().get(position).getIsProm() == 0) {//非促销
                price = activitySessionVo.getActSessionList().get(position).getActPrice();
                adultPrice = activitySessionVo.getActSessionList().get(position).getAdultPrice();
            } else if (activitySessionVo.getActSessionList().get(position).getIsProm() == 1) {//促销
                price = activitySessionVo.getActSessionList().get(position).getChildPromPrice();
                adultPrice = activitySessionVo.getActSessionList().get(position).getAdultPromPrice();
            }
            data.putString("actPrice", price);
            data.putString("adultPrice", adultPrice);
            data.putString("actStartTime", activitySessionVo.getActSessionList().get(position).getClassTime());
            data.putBoolean("canApply", activitySessionVo.getActSessionList().get(position).isApply());

            ActCouponSelectChildCache.canUseCouponAct.put(
                    activitySessionVo.getActSessionList().get(position).getId(),
                    ViewInitTool.getCanUseCouponByAct(activitySessionVo.getActSessionList().get(position)));

            fragment.setArguments(data);
            return fragment;
        }
    }

}

