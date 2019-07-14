package com.park61.moduel.firsthead;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.firsthead.fragment.FragmentTopicSearchResult;
import com.park61.moduel.order.MyOrderActivty;
import com.park61.moduel.order.fragment.TradeOrderPaperFragment;
import com.park61.moduel.sales.SalesMainActivity;
import com.park61.moduel.sales.bean.ProductCategory;
import com.park61.moduel.salesafter.SalesAfterRefundListFragment;
import com.park61.widget.viewpager.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/6/14.
 */

public class TopicSearchResultActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private ImageView img_line = null;
    private ViewPager pager;
    private DisplayMetrics dm;
    private TextView tv_cancel;
    private EditText edit_sousuo;

    /*** 当前分类*/
    private int cur_index = 0;

    final int[] BUTION_IDS = {R.id.rb_tv, R.id.rb_topic};
    private View[] stickArray = new View[2];

    private String keyword;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_topic_search_result);
    }

    @Override
    public void initView() {
        dm = getResources().getDisplayMetrics();
        img_line = (ImageView) this.findViewById(R.id.img_line);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        stickArray[0] = findViewById(R.id.stick0);
        stickArray[1] = findViewById(R.id.stick1);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        edit_sousuo = (EditText) findViewById(R.id.edit_sousuo);
    }

    @Override
    public void initData() {
        keyword = getIntent().getStringExtra("keyword");
        cur_index = this.getIntent().getIntExtra("tab_index", 0);
        pager.setCurrentItem(cur_index, false);
        edit_sousuo.setText(keyword);
        edit_sousuo.setEnabled(false);
    }

    @Override
    public void initListener() {
        ((RadioButton) this.findViewById(R.id.rb_tv)).setOnCheckedChangeListener(this);
        ((RadioButton) this.findViewById(R.id.rb_topic)).setOnCheckedChangeListener(this);
        pager.addOnPageChangeListener(mOnPageChangeListener);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.rb_tv:
                    changeTabList(0);
                    break;
                case R.id.rb_topic:
                    changeTabList(1);
                    break;
            }
        }
    }

    private void changeTabList(int tabindex) {
        if (tabindex != cur_index) {
            cur_index = tabindex;
            pager.setCurrentItem(tabindex, true);
            showStick(tabindex);
        }
    }

    /**
     * 变化标签组下方红杠
     */
    private void showStick(int which) {
        stickArray[which].setVisibility(View.VISIBLE);
        for (int i = 0; i < stickArray.length; i++) {
            if (i != which) {
                stickArray[i].setVisibility(View.INVISIBLE);
            }
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
            FragmentTopicSearchResult fragment = new FragmentTopicSearchResult();
            Bundle data = new Bundle();
            data.putInt("sType", position);
            data.putString("keyword", keyword);
            fragment.setArguments(data);
            return fragment;
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) findViewById(BUTION_IDS[position]);
            radioButton.performClick();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

}
