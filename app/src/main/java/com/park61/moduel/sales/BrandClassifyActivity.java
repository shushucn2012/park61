package com.park61.moduel.sales;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.moduel.msg.MsgActivity;
import com.park61.moduel.sales.fragment.FragmentSalesBrand;
import com.park61.moduel.sales.fragment.FragmentSalesCate;
import com.park61.moduel.shoppincart.TradeCartActivity;
import com.park61.widget.viewpager.PagerSlidingTabStrip;


/**
 * 类目分类和品牌分类
 */
public class BrandClassifyActivity extends BaseActivity {
    private ViewPager pager;
    private DisplayMetrics dm;
    private PagerSlidingTabStrip tabs;
    private View sousuo_area;
    private Button btn_msg, btn_cart;

    private final String[] BUTION_NAME = {"分类", "品牌"};
    private String showWhich = "";//显示哪一个分页

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_category_brand_classify);
    }

    @Override
    public void initView() {
        showWhich = getIntent().getStringExtra("SHOW_WHICH");

        dm = getResources().getDisplayMetrics();
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(pager);
        setTabsValue();
        tabs.setFadeEnabled(false);
        if (TextUtils.isEmpty(showWhich) || showWhich.equals("SHOW_CATE")) {
            pager.setCurrentItem(0);
        } else if (showWhich.equals("SHOW_BRAND")) {
            pager.setCurrentItem(1);
        }

        sousuo_area = findViewById(R.id.sousuo_area);
        btn_msg = (Button) findViewById(R.id.btn_msg);
        btn_cart = (Button) findViewById(R.id.btn_cart);
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        tabs.setScrollOffset(0);
        tabs.setTabPaddingLeftRight(DevAttr.dip2px(mContext, 1));
        tabs.setDividerPadding(0);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight(1);
        tabs.setUnderlineColor(getResources().getColor(R.color.colorLineSales));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.com_orange));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(getResources().getColor(R.color.gffffff));
        // 设置正常Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setTextColor(getResources().getColor(R.color.com_orange));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        sousuo_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SalesSearchActivity.class));
            }
        });
        btn_cart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (GlobalParam.userToken == null) {// 没有登录则跳去登录
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                startActivity(new Intent(mContext, TradeCartActivity.class));
            }
        });
        btn_msg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (GlobalParam.userToken == null) {// 没有登录则跳去登录
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                startActivity(new Intent(mContext, MsgActivity.class));
            }
        });
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return BUTION_NAME[position];
        }

        @Override
        public int getCount() {
            return BUTION_NAME.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new FragmentSalesCate();
            }
            return new FragmentSalesBrand();
        }
    }

}
