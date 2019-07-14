package com.park61.moduel.sales;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.sales.bean.PromotionPageBean;
import com.park61.moduel.sales.cache.SecKillDTO;
import com.park61.moduel.sales.fragment.FragmentSecKillNew;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SecKillActivity extends BaseActivity {

    private MPagerSlidingTabStrip tabs;
    private ViewPager pager;

    private String promotionType;
    private int pos = 0;
    private ArrayList<PromotionPageBean> ppbList;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_sec_kill);
    }

    @Override
    public void initView() {
        tabs = (MPagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
    }

    @Override
    public void initData() {
        ppbList = new ArrayList<>();
        promotionType = getIntent().getStringExtra("promotionType");
        asyncGetPromotionMerge();
    }

    @Override
    public void initListener() {

    }

    /**
     * 获取限时秒杀所有列表
     */
    public void asyncGetPromotionMerge() {
        String wholeUrl = AppUrl.host + AppUrl.PROMOTION_MERGE;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("promotionType", promotionType);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, pmlistener);
    }

    BaseRequestListener pmlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ppbList.clear();
            JSONArray actJay = jsonResult.optJSONArray("list");
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject jot = actJay.optJSONObject(i);
                PromotionPageBean ppb = gson.fromJson(jot.toString(), PromotionPageBean.class);
                String isStart = ppb.getIsStart();
                String isCurrent = ppb.getIsCurrent();
                String msg = "";
                if (isStart.equals("1") && isCurrent.equals("1")) {
                    msg = "抢购中";
                } else if (isStart.equals("1") && isCurrent.equals("")) {
                    msg = "即将开始";
                } else if (isStart.equals("0") && isCurrent.equals("")) {
                    msg = "已开抢";
                }
                ppb.setMsg(msg);
                ppbList.add(ppb);
            }
            SecKillDTO.ppbCacheList = ppbList;
            pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
            tabs.setViewPager(pager);
            setTabsValue();
            tabs.setFadeEnabled(false);
            pager.setCurrentItem(pos, false);
            pager.setOffscreenPageLimit(ppbList.size());
        }
    };

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        // 设置Tab是自动填充满屏幕的
        //tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        tabs.setScrollOffset(0);
        tabs.setTabPaddingLeftRight(DevAttr.dip2px(mContext, 10));
        tabs.setDividerPadding(0);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        tabs.setUnderlineColor(Color.TRANSPARENT);
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 48, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 15, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.com_orange));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(getResources().getColor(R.color.gffffff));
        // 设置正常Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setTextColor(getResources().getColor(R.color.gffffff));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (ppbList.get(position).getMsg().equals("抢购中")) {
                pos = position;
            }
            logout("123123123");
            return ppbList.get(position).getStart() + "\n" + ppbList.get(position).getMsg();
        }

        @Override
        public int getCount() {
            return ppbList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            FragmentSecKillNew fragment = new FragmentSecKillNew();
            Bundle data = new Bundle();
            data.putString("id", ppbList.get(position).getId());
            data.putInt("index", position);
            fragment.setArguments(data);
            return fragment;
        }
    }

}
