package com.park61.moduel.me;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.moduel.firsthead.fragment.FragmentMyActsCollect;
import com.park61.moduel.firsthead.fragment.FragmentMyTopicCollect;

/**
 * Created by shubei on 2017/6/16.
 */

public class MyCollectListActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private ViewPager pager;
    private ImageView back;
    private DisplayMetrics dm;

    /*** 当前分类*/
    private int cur_index = 0;

    final int[] BUTION_IDS = {R.id.rb_tv, R.id.rb_topic};
    private View[] stickArray = new View[2];

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_my_collect_list);
    }

    @Override
    public void initView() {
        dm = getResources().getDisplayMetrics();
        pager = (ViewPager) findViewById(R.id.pager);
        back = (ImageView) findViewById(R.id.back);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        stickArray[0] = findViewById(R.id.stick0);
        stickArray[1] = findViewById(R.id.stick1);
    }

    @Override
    public void initData() {
        cur_index = this.getIntent().getIntExtra("tab_index", 0);
        pager.setCurrentItem(cur_index, false);
    }

    @Override
    public void initListener() {
        ((RadioButton) this.findViewById(R.id.rb_tv)).setOnCheckedChangeListener(this);
        ((RadioButton) this.findViewById(R.id.rb_topic)).setOnCheckedChangeListener(this);
        pager.addOnPageChangeListener(mOnPageChangeListener);
        back.setOnClickListener(new View.OnClickListener() {
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
            if(position == 0){
                return new FragmentMyActsCollect();
            }else if(position == 1) {
                return new FragmentMyTopicCollect();
            }
            return new FragmentMyTopicCollect();
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
