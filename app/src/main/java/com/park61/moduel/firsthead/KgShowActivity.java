package com.park61.moduel.firsthead;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.park61.BaseActivity;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.tool.CommonMethod;
import com.park61.moduel.firsthead.fragment.FragmentClassShow;
import com.park61.moduel.firsthead.fragment.FragmentClassWordsShow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/11/21.
 */

public class KgShowActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
    private int[] BUTION_IDS = {R.id.rb_class, R.id.rb_first, R.id.rb_tv, R.id.rb_topic};
    private View[] stickArray = new View[2];
    private ViewPager pager;
    private int cur_index = 0;//当前分类

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_kgshow);
    }

    @Override
    public void initView() {
        setPagTitle("幼儿园");
        if (!CommonMethod.isListEmpty(fragmentList)) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            for (Fragment f : fragmentList) {
                ft.remove(f);
            }
            ft.commitAllowingStateLoss();
            getSupportFragmentManager().executePendingTransactions();
        }

        fragmentList.clear();
        FragmentClassWordsShow fragmentClassWordsShow = new FragmentClassWordsShow();
        //班级圈活动banner需要 幼儿园id
        Bundle bundle = new Bundle();
        bundle.putInt("groupId", getIntent().getIntExtra("groupId", -1));
        bundle.putInt("classId", getIntent().getIntExtra("classId", -1));
        fragmentClassWordsShow.setArguments(bundle);
        fragmentList.add(fragmentClassWordsShow);

        FragmentClassShow fragmentClassShow = new FragmentClassShow();
        fragmentList.add(fragmentClassShow);

        BUTION_IDS = new int[]{R.id.rb_first, R.id.rb_tv};
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(2);
        stickArray[0] = findViewById(R.id.stick0);
        stickArray[1] = findViewById(R.id.stick1);
        pager.setCurrentItem(cur_index, false);
        ((RadioButton) findViewById(R.id.rb_first)).setChecked(true);
        ((RadioButton) findViewById(R.id.rb_first)).setOnCheckedChangeListener(KgShowActivity.this);
        ((RadioButton) findViewById(R.id.rb_tv)).setOnCheckedChangeListener(KgShowActivity.this);
        pager.addOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    public void initData() {
        String pageTitle = getIntent().getStringExtra("PAGE_TITLE");
        if (!TextUtils.isEmpty(pageTitle)) {
            setPagTitle(pageTitle);
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.rb_first:
                    changeTabList(0);
                    break;
                case R.id.rb_tv:
                    changeTabList(1);
                    break;
                case R.id.rb_topic:
                    changeTabList(2);
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
            return fragmentList.get(position);
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
