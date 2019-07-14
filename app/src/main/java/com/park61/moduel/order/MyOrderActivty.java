package com.park61.moduel.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.moduel.order.fragment.TradeOrderPaperFragment;
import com.park61.moduel.salesafter.SalesAfterRefundListFragment;

/**
 * 商品订单列表界面
 */
public class MyOrderActivty extends BaseActivity implements OnCheckedChangeListener, OnPageChangeListener {

    public static final int TAB_ALL = 0;
    public static final int TAB_WAIT_PAY = 1;
    public static final int TAB_WAIT_RECEIVE = 2;
    public static final int TAB_WAIT_COMMENT = 3;

    private ImageView img_line = null;
    private ViewPager pager;
    private DisplayMetrics dm;

    /**
     * 当前分类
     */
    private int cur_index = 0;

    final int[] BUTION_IDS = {R.id.all_tv, R.id.pay_tv, R.id.receive_tv, R.id.evaluate_tv, R.id.salesAfter_tv};

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_myorder);
    }

    @Override
    public void initView() {
        dm = getResources().getDisplayMetrics();
        img_line = (ImageView) this.findViewById(R.id.img_line);
        img_line.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels / BUTION_IDS.length, (int) (4 * dm.density)));
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    public void initData() {
        cur_index = this.getIntent().getIntExtra("tab_index", 0);
        pager.setCurrentItem(cur_index, false);
    }

    @Override
    public void initListener() {
        ((RadioButton) this.findViewById(R.id.all_tv)).setOnCheckedChangeListener(this);
        ((RadioButton) this.findViewById(R.id.pay_tv)).setOnCheckedChangeListener(this);
        ((RadioButton) this.findViewById(R.id.receive_tv)).setOnCheckedChangeListener(this);
        ((RadioButton) this.findViewById(R.id.evaluate_tv)).setOnCheckedChangeListener(this);
        ((RadioButton) this.findViewById(R.id.salesAfter_tv)).setOnCheckedChangeListener(this);
        pager.setOnPageChangeListener(this);
    }

    public void imageLineChanged(int paper, int offset) {
        RadioButton radioButton = (RadioButton) findViewById(BUTION_IDS[paper]);
        LinearLayout.LayoutParams lp = (LayoutParams) img_line.getLayoutParams();
        lp.leftMargin = (int) (radioButton.getLeft() + offset);
        img_line.setLayoutParams(lp);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.all_tv:
                    changeTabList(0);
                    break;
                case R.id.pay_tv:
                    changeTabList(1);
                    break;
                case R.id.receive_tv:
                    changeTabList(2);
                    break;
                case R.id.evaluate_tv:
                    changeTabList(3);
                    break;
                case R.id.salesAfter_tv:
                    changeTabList(4);
                    break;
                default:
                    break;
            }
        }
    }

    private void changeTabList(int tabindex) {
        if (tabindex != cur_index) {
            cur_index = tabindex;
            pager.setCurrentItem(tabindex, true);
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return BUTION_IDS.length;
        }

        @Override
        public Fragment getItem(int position) {
            if (position < 4) {
                TradeOrderPaperFragment fragment = new TradeOrderPaperFragment();
                Bundle data = new Bundle();
                data.putInt("orderListType", position + 1);
                fragment.setArguments(data);
                return fragment;
            } else {
                return new SalesAfterRefundListFragment();
            }

        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
        //Log.i("onPageScrolled", "arg0:" + arg0 + ",arg1:" + arg1 + ",arg2:" + arg2);
        imageLineChanged(arg0, arg2 / BUTION_IDS.length);
    }

    @Override
    public void onPageSelected(int arg0) {
        RadioButton radioButton = (RadioButton) findViewById(BUTION_IDS[arg0]);
        radioButton.performClick();
    }

    public int getCurPaperIndex() {
        return this.cur_index;
    }

}
