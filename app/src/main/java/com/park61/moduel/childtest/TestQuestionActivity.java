package com.park61.moduel.childtest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.childtest.bean.QuestionCache;
import com.park61.moduel.childtest.bean.ReturnResultBean;
import com.park61.moduel.childtest.bean.TestAnswers;
import com.park61.moduel.childtest.bean.TestQuestion;
import com.park61.net.base.Request;
import com.park61.net.request.SimpleRequestListener;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.viewpager.CanotSlidingViewpager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shushucn2012 on 2017/2/24.
 */
public class TestQuestionActivity extends BaseActivity {

    private CanotSlidingViewpager viewPager;//viewpager
    private List viewList;
    private List<ReturnResultBean> resultList = new ArrayList<>();
    private TextView tv_process;
    private View view_focus, view_default, view_cover;
    private String beginTime;
    private String endTime;
    private int finishNum;
    private String resultId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_test_question);
    }

    @Override
    public void initView() {
        resultId = getIntent().getStringExtra("resultId");
        finishNum = getIntent().getIntExtra("finished_num", 0);
        view_cover = findViewById(R.id.view_cover);
        tv_process = (TextView) findViewById(R.id.tv_process);
        view_focus = findViewById(R.id.view_focus);
        view_default = findViewById(R.id.view_default);
        viewPager = (CanotSlidingViewpager) findViewById(R.id.vp_question);

        //将之前做过的答案导入到答案列表
        for (int i = 0; i < finishNum; i++) {
            TestQuestion testQuestion = QuestionCache.questionList.get(i);
            for (TestAnswers t : testQuestion.getAnswers()) {
                if (t.isChoosed()) {
                    ReturnResultBean rrb = new ReturnResultBean();
                    rrb.setAnswerIds(t.getId());
                    rrb.setEaItemSubId(testQuestion.getEaItemSubId());
                    rrb.setSubjectId(testQuestion.getId());
                    resultList.add(rrb);
                }
            }
        }

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        for (int i = 0; i < QuestionCache.questionList.size(); i++) {
            View questionView = createNewView(i);
            viewList.add(questionView);
        }
        viewPager.setAdapter(new MyViewPagerAdapter(viewList));
        viewPager.setScrollble(false);

        // 判断是否是第一次进入APP
        SharedPreferences isFirstRunSp = getSharedPreferences("IsFirstTestFlag", Activity.MODE_PRIVATE);
        boolean isFirstFlag = isFirstRunSp.getBoolean("IsFirstTestRun", true);
        if (isFirstFlag) {
            view_cover.setVisibility(View.VISIBLE);
            view_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view_cover.setVisibility(View.GONE);
                }
            });
            isFirstRunSp.edit().putBoolean("IsFirstTestRun", false).commit();
        } else {
            view_cover.setVisibility(View.GONE);
            viewPager.setCurrentItem(finishNum);
        }
    }

    private View createNewView(int index) {
        LayoutInflater lf = getLayoutInflater().from(this);
        View mView = lf.inflate(R.layout.test_question_layout, null);
        final TestQuestion tq = QuestionCache.questionList.get(index);
        TextView tv_question_id = (TextView) mView.findViewById(R.id.tv_question_id);
        TextView tv_question_content = (TextView) mView.findViewById(R.id.tv_question_content);
        final TextView tv_answer_one = (TextView) mView.findViewById(R.id.tv_answer_one);
        final TextView tv_answer_two = (TextView) mView.findViewById(R.id.tv_answer_two);
        final TextView tv_answer_three = (TextView) mView.findViewById(R.id.tv_answer_three);

        tv_question_id.setText("问题" + (index + 1) + "：");
        tv_question_content.setText(tq.getContent());
        tv_answer_one.setText(tq.getAnswers().get(0).getContent());
        tv_answer_two.setText(tq.getAnswers().get(1).getContent());

        if (tq.getAnswers().size() > 2) {
            tv_answer_three.setVisibility(View.VISIBLE);
            tv_answer_three.setText(tq.getAnswers().get(2).getContent());
            if (tq.getAnswers().get(0).isChoosed()) {
                tv_answer_one.setBackgroundResource(R.drawable.btn_test_answer_chosen);
                tv_answer_two.setBackgroundResource(R.drawable.btn_test_answer);
                tv_answer_three.setBackgroundResource(R.drawable.btn_test_answer);
            } else if (tq.getAnswers().get(1).isChoosed()) {
                tv_answer_one.setBackgroundResource(R.drawable.btn_test_answer);
                tv_answer_two.setBackgroundResource(R.drawable.btn_test_answer_chosen);
                tv_answer_three.setBackgroundResource(R.drawable.btn_test_answer);
            } else if (tq.getAnswers().get(2).isChoosed()) {
                tv_answer_one.setBackgroundResource(R.drawable.btn_test_answer);
                tv_answer_two.setBackgroundResource(R.drawable.btn_test_answer);
                tv_answer_three.setBackgroundResource(R.drawable.btn_test_answer_chosen);
            }
        } else {
            tv_answer_three.setVisibility(View.GONE);
            if (tq.getAnswers().get(0).isChoosed()) {
                tv_answer_one.setBackgroundResource(R.drawable.btn_test_answer_chosen);
                tv_answer_two.setBackgroundResource(R.drawable.btn_test_answer);
            } else if (tq.getAnswers().get(1).isChoosed()) {
                tv_answer_one.setBackgroundResource(R.drawable.btn_test_answer);
                tv_answer_two.setBackgroundResource(R.drawable.btn_test_answer_chosen);
            }
        }

        tv_answer_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == QuestionCache.questionList.size() - 1) {
                    dDialog.showDialog("恭喜", "已完成答题，确认提交吗？", "取消", "确定", null,
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    submitResult();
                                }
                            });
                }
                if (viewPager.getCurrentItem() == resultList.size()) {//如果是第一次回答
                    ReturnResultBean rrb = new ReturnResultBean();
                    rrb.setAnswerIds(tq.getAnswers().get(0).getId());
                    rrb.setEaItemSubId(tq.getEaItemSubId());
                    rrb.setSubjectId(tq.getId());
                    resultList.add(rrb);
                } else {//如果是修改答案
                    resultList.get(viewPager.getCurrentItem()).setAnswerIds(tq.getAnswers().get(0).getId());
                }
                tv_answer_one.setBackgroundResource(R.drawable.btn_test_answer_chosen);
                tv_answer_two.setBackgroundResource(R.drawable.btn_test_answer);
                tv_answer_three.setBackgroundResource(R.drawable.btn_test_answer);
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        tv_answer_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == QuestionCache.questionList.size() - 1) {
                    dDialog.showDialog("恭喜", "已完成答题，确认提交吗？", "取消", "确定", null,
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dDialog.dismissDialog();
                                    submitResult();
                                }
                            });
                }
                if (viewPager.getCurrentItem() == resultList.size()) {//如果是第一次回答
                    ReturnResultBean rrb = new ReturnResultBean();
                    rrb.setAnswerIds(tq.getAnswers().get(1).getId());
                    rrb.setEaItemSubId(tq.getEaItemSubId());
                    rrb.setSubjectId(tq.getId());
                    resultList.add(rrb);
                } else {//如果是修改答案
                    resultList.get(viewPager.getCurrentItem()).setAnswerIds(tq.getAnswers().get(1).getId());
                }
                tv_answer_one.setBackgroundResource(R.drawable.btn_test_answer);
                tv_answer_two.setBackgroundResource(R.drawable.btn_test_answer_chosen);
                tv_answer_three.setBackgroundResource(R.drawable.btn_test_answer);
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        tv_answer_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == QuestionCache.questionList.size() - 1) {
                    dDialog.showDialog("恭喜", "已完成答题，确认提交吗？", "取消", "确定", null,
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    submitResult();
                                }
                            });
                }
                if (viewPager.getCurrentItem() == resultList.size()) {//如果是第一次回答
                    ReturnResultBean rrb = new ReturnResultBean();
                    rrb.setAnswerIds(tq.getAnswers().get(2).getId());
                    rrb.setEaItemSubId(tq.getEaItemSubId());
                    rrb.setSubjectId(tq.getId());
                    resultList.add(rrb);
                } else {//如果是修改答案
                    resultList.get(viewPager.getCurrentItem()).setAnswerIds(tq.getAnswers().get(2).getId());
                }
                tv_answer_one.setBackgroundResource(R.drawable.btn_test_answer);
                tv_answer_two.setBackgroundResource(R.drawable.btn_test_answer);
                tv_answer_three.setBackgroundResource(R.drawable.btn_test_answer_chosen);
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        return mView;
    }

    @Override
    public void initData() {
        beginTime = DateTool.getSystemDateTime();
        tv_process.setText((finishNum + 1) + "/" + QuestionCache.questionList.size());
        view_focus.setLayoutParams(new LinearLayout.LayoutParams(0, DevAttr.dip2px(mContext, 5), finishNum + 1));
        view_default.setLayoutParams(new LinearLayout.LayoutParams(0, DevAttr.dip2px(mContext, 5), QuestionCache.questionList.size() - finishNum - 1));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tv_process.setText((position + 1) + "/" + QuestionCache.questionList.size());
                view_focus.setLayoutParams(new LinearLayout.LayoutParams(0, DevAttr.dip2px(mContext, 5), position + 1));
                view_default.setLayoutParams(new LinearLayout.LayoutParams(0, DevAttr.dip2px(mContext, 5), QuestionCache.questionList.size() - position - 1));
                if (position == resultList.size()) {
                    viewPager.setScrollble(false);
                } else if (position < resultList.size()) {
                    viewPager.setScrollble(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 提交结果
     */
    private void submitResult() {
        String wholeUrl = AppUrl.host + AppUrl.RESULT_SUBMIT_V2;
        endTime = DateTool.getSystemDateTime();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eaItemId", QuestionCache.eaItemId);
        map.put("childId", QuestionCache.chosenChildId);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);

        if (!TextUtils.isEmpty(resultId)) {// resultId不为空 续答
            map.put("resultId", resultId);

            //已答部分
            JSONArray jay_first = new JSONArray();
            for (ReturnResultBean item : resultList.subList(0, finishNum)) {
                JSONObject jot = new JSONObject();
                try {
                    jot.put("subjectId", item.getSubjectId());
                    jot.put("eaItemSubId", item.getEaItemSubId());
                    jot.put("answerIds", item.getAnswerIds());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jay_first.put(jot);
            }
            map.put("modifyResult", jay_first.toString());

            //续答部分
            JSONArray jay_second = new JSONArray();
            for (ReturnResultBean item : resultList.subList(finishNum, resultList.size())) {
                JSONObject jot = new JSONObject();
                try {
                    jot.put("subjectId", item.getSubjectId());
                    jot.put("eaItemSubId", item.getEaItemSubId());
                    jot.put("answerIds", item.getAnswerIds());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jay_second.put(jot);
            }
            map.put("result", jay_second.toString());
        } else { // resultId为空 首答
            JSONArray jay = new JSONArray();
            for (ReturnResultBean item : resultList) {
                JSONObject jot = new JSONObject();
                try {
                    jot.put("subjectId", item.getSubjectId());
                    jot.put("eaItemSubId", item.getEaItemSubId());
                    jot.put("answerIds", item.getAnswerIds());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jay.put(jot);
            }
            map.put("result", jay.toString());
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, submitLsner);
    }

    BaseRequestListener submitLsner = new JsonRequestListener() {

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
            showShortToast("提交成功！");
            Intent it = new Intent(mContext, TestResultWebViewActivity.class);
            String id = jsonResult.optString("id");
            it.putExtra("PAGE_TITLE", "测评报告");
            String resultUrl = AppUrl.demoHost_head + "/html-sui/dapTest/test-result.html?resultid=" + id;
            it.putExtra("url", resultUrl);
            String title = jsonResult.optString("eaSysName") + "-" + jsonResult.optString("eaItemName");
            String picUrl = "";
            String content = "";
            if (jsonResult.optJSONObject("represent") != null) {
                picUrl = jsonResult.optJSONObject("represent").optString("picUrl");
                content = jsonResult.optJSONObject("represent").optString("property") + "\n"
                        + jsonResult.optJSONObject("represent").optString("name") + "\n"
                        + jsonResult.optJSONObject("represent").optString("content") + "\n";
            }
            it.putExtra("picUrl", picUrl);
            it.putExtra("title", title);
            it.putExtra("content", content);
            startActivity(it);
            finish();
        }
    };

    @Override
    public void initListener() {
        left_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDialog.showDialog("提示", "是否取消答题？", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitResultUnEnd();
                        dDialog.dismissDialog();
                        finish();
                    }
                });
            }
        });
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));//删除页卡
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
            container.addView(mListViews.get(position), 0);//添加页卡
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();//返回页卡的数量
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;//官方提示这样写
        }
    }

    /**
     * 中途提交结果
     */
    private void submitResultUnEnd() {
        String wholeUrl = AppUrl.host + AppUrl.RESULT_SUBMIT_V2;
        endTime = DateTool.getSystemDateTime();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eaItemId", QuestionCache.eaItemId);
        map.put("childId", QuestionCache.chosenChildId);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);

        if (!TextUtils.isEmpty(resultId)) {// resultId不为空 续答
            map.put("resultId", resultId);

            //已答部分
            JSONArray jay_first = new JSONArray();
            for (ReturnResultBean item : resultList.subList(0, finishNum)) {
                JSONObject jot = new JSONObject();
                try {
                    jot.put("subjectId", item.getSubjectId());
                    jot.put("eaItemSubId", item.getEaItemSubId());
                    jot.put("answerIds", item.getAnswerIds());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jay_first.put(jot);
            }
            map.put("modifyResult", jay_first.toString());

            //续答部分
            JSONArray jay_second = new JSONArray();
            for (ReturnResultBean item : resultList.subList(finishNum, resultList.size())) {
                JSONObject jot = new JSONObject();
                try {
                    jot.put("subjectId", item.getSubjectId());
                    jot.put("eaItemSubId", item.getEaItemSubId());
                    jot.put("answerIds", item.getAnswerIds());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jay_second.put(jot);
            }
            map.put("result", jay_second.toString());
        } else { // resultId为空 首答
            JSONArray jay = new JSONArray();
            for (ReturnResultBean item : resultList) {
                JSONObject jot = new JSONObject();
                try {
                    jot.put("subjectId", item.getSubjectId());
                    jot.put("eaItemSubId", item.getEaItemSubId());
                    jot.put("answerIds", item.getAnswerIds());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jay.put(jot);
            }
            map.put("result", jay.toString());
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        dDialog.showDialog("提示", "是否取消答题？", "取消", "确定", null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitResultUnEnd();
                dDialog.dismissDialog();
                finish();
            }
        });
    }
}
