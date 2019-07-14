package com.park61.moduel.openmember;

import android.content.Intent;
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
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.moduel.openmember.adapter.MyBabyListAdapter;
import com.park61.moduel.openmember.bean.ChildInfo;
import com.park61.moduel.openmember.fragment.FragmentOpenMemeber;
import com.park61.moduel.pay.bean.MemberCardTypeVO;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.viewpager.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 开通会员界面
 */
public class OpenMemberActivity extends BaseActivity {
    private PagerSlidingTabStrip tabs;// PagerSlidingTabStrip的实例
    private DisplayMetrics dm;// 获取当前屏幕的密度
    private ViewPager pager;
    private int pos = 0;
    private List<MemberCardTypeVO> memberCardTyList;// 卡类型列表
    private MemberCardTypeVO cardItem;
    private MemberCardTypeVO selectedType;// 选中的卡类型
    private TextView tv_member_right;
    private BabyItem selectedChild;
    private ChildInfo child;
    private String type;// 充值方式(0:开通;1:续费;2:升级)
    private String cardId;//卡id
    private String cardCode;
    public List<BabyItem> babyList;
    private MyBabyListAdapter mAdapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_openmember);
    }

    @Override
    public void initView() {
        tv_member_right = (TextView) findViewById(R.id.tv_member_right);
        dm = getResources().getDisplayMetrics();
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
    }

    @Override
    public void initData() {

        babyList = new ArrayList<BabyItem>();
        mAdapter = new MyBabyListAdapter(mContext, babyList);

        cardCode = getIntent().getStringExtra("cardCode");
        cardId = getIntent().getStringExtra("cardId");
        type = getIntent().getStringExtra("type");
        selectedChild = (BabyItem) getIntent().getSerializableExtra("child");
        child = (ChildInfo) getIntent().getSerializableExtra("childInfo");
        memberCardTyList = new ArrayList<MemberCardTypeVO>();
        asyncGetUserChilds();
    }

    @Override
    public void initListener() {
        tv_member_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MemberRightsActivity.class));
            }
        });
    }

    /**
     * 查询顶级充值类型
     */
    private void asyncGetAllMemberCardList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_ALL_MEMBERCARD_LIST;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, getTopTypeLsner);
    }

    BaseRequestListener getTopTypeLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray jay = jsonResult.optJSONArray("list");
            if (jay.length() <= 0) {
                showShortToast("该城市没有开通会员卡功能！");
                finish();
            }
            memberCardTyList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                memberCardTyList.add(gson.fromJson(jot.toString(), MemberCardTypeVO.class));
            }
            pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
            tabs.setViewPager(pager);
            if (memberCardTyList.size() == 1) {
                setTabsValue0();
            } else if (memberCardTyList.size() < 4) {
                setTabsValueShort();
            } else {
                setTabsValue();
            }
            tabs.setFadeEnabled(false);
            for (int i = 0; i < memberCardTyList.size(); i++) {
                if (memberCardTyList.get(i).getCardCode().equals(cardId) || memberCardTyList.get(i).getCardCode().equals(cardCode)) {
                    pos = i;
                }
            }
            pager.setCurrentItem(pos, false);
            pager.setOffscreenPageLimit(memberCardTyList.size());
            dismissDialog();
        }
    };

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        tabs.setScrollOffset(0);
        tabs.setTabPaddingLeftRight(DevAttr.dip2px(mContext, 20));
        tabs.setDividerPadding(0);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        tabs.setUnderlineColor(Color.TRANSPARENT);
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.com_orange));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(getResources().getColor(R.color.com_orange));
        // 设置正常Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setTextColor(getResources().getColor(R.color.g666666));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }

    private void setTabsValueShort() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        tabs.setScrollOffset(0);
        tabs.setTabPaddingLeftRight(DevAttr.dip2px(mContext, 10));
        tabs.setDividerPadding(0);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        tabs.setUnderlineColor(Color.TRANSPARENT);
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.com_orange));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(getResources().getColor(R.color.com_orange));
        // 设置正常Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setTextColor(getResources().getColor(R.color.g666666));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }

    private void setTabsValue0() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(false);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        tabs.setScrollOffset(0);
        tabs.setTabPaddingLeftRight(DevAttr.dip2px(mContext, 10));
        tabs.setDividerPadding(0);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        tabs.setUnderlineColor(Color.TRANSPARENT);
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.com_orange));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(getResources().getColor(R.color.com_orange));
        // 设置正常Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setTextColor(getResources().getColor(R.color.g666666));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return memberCardTyList.get(position).getCardName();
        }

        @Override
        public Fragment getItem(int position) {
            FragmentOpenMemeber fragment = new FragmentOpenMemeber();
            Bundle bundle = new Bundle();
            bundle.putString("cardName", memberCardTyList.get(position).getCardName());
            bundle.putString("cardCode", memberCardTyList.get(position).getCardCode());
            bundle.putLong("memberTypeId", memberCardTyList.get(position).getId());
            bundle.putString("type", type);
            bundle.putSerializable("child", selectedChild);
            bundle.putSerializable("childInfo", child);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return memberCardTyList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }
    }

    /**
     * 获取用户孩子列表
     */
    private void asyncGetUserChilds() {
        String wholeUrl = AppUrl.host + AppUrl.GET_USER_CHILDS_END;
        String requestBodyData = ParamBuild.getUserChilds();
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getChildsLsner);
    }

    BaseRequestListener getChildsLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray jay = jsonResult.optJSONArray("list");
            babyList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                BabyItem b = gson.fromJson(jot.toString(), BabyItem.class);
                babyList.add(b);
            }
            if (CommonMethod.isListEmpty(babyList)) {
                showShortToast("您还没有添加宝宝，请添加宝宝");
            }
            mAdapter.notifyDataSetChanged();
            asyncGetAllMemberCardList();
        }
    };

}
