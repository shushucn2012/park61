package com.park61.moduel.childtest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.childtest.bean.QuestionPage;
import com.park61.moduel.childtest.fragment.FragmentQuestionPage;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.viewpager.NoScrollViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测评题目主页
 * Created by shubei on 2017/12/4.
 */

public class TestQuestionNewActivity extends BaseActivity {

    public NoScrollViewPager pager;
    private View area_left;

    private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
    public List<QuestionPage> questionPagesList = new ArrayList<>();
    public int childId, eaServiceId;
    public int curPos;
    public String relationId, batchCode;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_test_question_new);
    }

    @Override
    public void initView() {
        pager = (NoScrollViewPager) findViewById(R.id.pager);
        area_left = findViewById(R.id.area_left);
    }

    @Override
    public void initData() {
        childId = getIntent().getIntExtra("childId", 0);
        eaServiceId = getIntent().getIntExtra("eaServiceId", 0);
        relationId = getIntent().getStringExtra("relationId");
        batchCode = getIntent().getStringExtra("batchCode");
        asyncGetDataList();
    }

    @Override
    public void initListener() {
        setPagTitle("宝宝评测项");
        ((TextView) findViewById(R.id.tv_area_right)).setText("上一步");
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curPos > 0)
                    pager.setCurrentItem(curPos - 1);
            }
        });
        area_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curPos = position;
                if (curPos == 0) {//第一页不显示上一步
                    area_right.setVisibility(View.GONE);
                } else {
                    area_right.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void asyncGetDataList() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.listSubjectsV3;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("childId", childId);
        map.put("eaServiceId", eaServiceId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

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
            JSONArray pageJay = jsonResult.optJSONArray("listEaSubject");
            for (int i = 0; i < pageJay.length(); i++) {
                QuestionPage questionPage = gson.fromJson(pageJay.optJSONObject(i).toString(), QuestionPage.class);
                questionPagesList.add(questionPage);
            }

            fragmentList.clear();
            for (int i = 0; i < questionPagesList.size(); i++) {
                FragmentQuestionPage fragmentQandA = new FragmentQuestionPage();
                Bundle data = new Bundle();
                data.putInt("index", i);
                fragmentQandA.setArguments(data);
                fragmentList.add(fragmentQandA);
            }

            pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

            for (int i = 0; i < questionPagesList.size(); i++) {
                if (!questionPagesList.get(i).isHasFinished()) {
                    curPos = i;
                    break;
                }
            }
            if (curPos == 0) {//第一页不显示上一步
                area_right.setVisibility(View.GONE);
            } else {
                area_right.setVisibility(View.VISIBLE);
            }
            pager.setCurrentItem(curPos);
        }
    };

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position + "";
        }

        @Override
        public int getCount() {
            return questionPagesList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }
}
