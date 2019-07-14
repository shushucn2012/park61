package com.park61.moduel.childtest.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.childtest.TestQuestionNewActivity;
import com.park61.moduel.childtest.TestResultActivity;
import com.park61.moduel.childtest.TestResultWebViewNewActivity;
import com.park61.moduel.childtest.adapter.TestPageListAdapter;
import com.park61.moduel.childtest.bean.QuestionPage;
import com.park61.moduel.childtest.bean.TestBigQuestion;
import com.park61.moduel.childtest.bean.TestQuestionItem;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.list.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubei on 2017/12/7.
 */

public class FragmentQuestionPage extends BaseFragment {

    private ListViewForScrollView lv_questions;
    private Button btn_next;
    private LinearLayout area_top_progress;

    private TestPageListAdapter mTestPageListAdapter;
    private int index;
    private QuestionPage mQuestionPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_question_page, container, false);
        index = getArguments().getInt("index");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        area_top_progress = (LinearLayout) parentView.findViewById(R.id.area_top_progress);
        int totalPage = ((TestQuestionNewActivity) parentActivity).questionPagesList.size();
        ((TextView) parentView.findViewById(R.id.tv_progress_num_end)).setText(totalPage + "");
        for (int i = 1; i < totalPage - 1; i++) { //动态添加首项和尾项中间各进度的状态
            View viewItem = LayoutInflater.from(parentActivity).inflate(R.layout.question_page_top_progress_item, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            ((TextView) viewItem.findViewById(R.id.tv_progress_num)).setText(i + 1 + "");
            if (i < index) {//比当前页前的都点亮
                viewItem.findViewById(R.id.img1_leftline).setBackgroundColor(parentActivity.getResources().getColor(R.color.color_progress_red_deep));
                ((ImageView) viewItem.findViewById(R.id.img_circle1)).setImageResource(R.drawable.testprogress_circleboth_red);
                viewItem.findViewById(R.id.img1_rightline).setBackgroundColor(parentActivity.getResources().getColor(R.color.color_progress_red_deep));
            } else if (i == index) {//当前页前点亮左侧和圆圈
                viewItem.findViewById(R.id.img1_leftline).setBackgroundColor(parentActivity.getResources().getColor(R.color.color_progress_red_deep));
                ((ImageView) viewItem.findViewById(R.id.img_circle1)).setImageResource(R.drawable.testprogress_circleboth_red);
            }
            area_top_progress.addView(viewItem, i, lp);
        }
        if (index > 0) {//只要当前页不是第一页，第一项右侧点亮
            parentView.findViewById(R.id.imgstart_rightline).setBackgroundColor(parentActivity.getResources().getColor(R.color.color_progress_red_deep));
        }
        if (index == totalPage - 1) {//当前页是最后页，最后一项左侧点亮,最后一项圆圈点亮
            parentView.findViewById(R.id.imgend_leftline).setBackgroundColor(parentActivity.getResources().getColor(R.color.color_progress_red_deep));
            ((ImageView) parentView.findViewById(R.id.img_circle_end)).setImageResource(R.drawable.testprogress_circleright_red);
        }

        lv_questions = (ListViewForScrollView) parentView.findViewById(R.id.lv_questions);
        btn_next = (Button) parentView.findViewById(R.id.btn_next);

        if (totalPage == 1) {
            area_top_progress.setVisibility(View.GONE);
        }
        if (index == totalPage - 1) {
            btn_next.setText("提交");
        } else {
            btn_next.setText("下一步");
        }
    }

    @Override
    public void initData() {
        mQuestionPage = ((TestQuestionNewActivity) parentActivity).questionPagesList.get(index);
        mTestPageListAdapter = new TestPageListAdapter(parentActivity, mQuestionPage.getListEaSubject());
        lv_questions.setAdapter(mTestPageListAdapter);
    }

    @Override
    public void initListener() {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncSubmitAnswers();
            }
        });
    }

    /**
     * 提交答案
     */
    private void asyncSubmitAnswers() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.submitV3;
        Map<String, Object> map = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(mQuestionPage.getBatchCode())) {//批次号不为空则提交答案时传入
            map.put("batchCode", mQuestionPage.getBatchCode());
        } else if (!TextUtils.isEmpty(((TestQuestionNewActivity) parentActivity).batchCode)) {//未登录用户的batchCode在填写资料时回传，提交必须带上
            map.put("batchCode", ((TestQuestionNewActivity) parentActivity).batchCode);
        }
        map.put("childId", ((TestQuestionNewActivity) parentActivity).childId);
        map.put("eaItemId", mQuestionPage.getEaItemId());
        map.put("eaServiceId", ((TestQuestionNewActivity) parentActivity).eaServiceId);

        //答案部分
        JSONArray jay_first = new JSONArray();
        for (TestBigQuestion mTestBigQuestion : mQuestionPage.getListEaSubject()) {//一页中的所有大项
            for (TestQuestionItem qItem : mTestBigQuestion.getListEaSubject()) {
                JSONObject jot = new JSONObject();
                try {
                    jot.put("id", qItem.getId());
                    jot.put("hasChecked", qItem.isHasChecked());
                    jot.put("eaItemSubId", qItem.getEaItemSubId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jay_first.put(jot);
            }
        }
        map.put("result", jay_first.toString());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, slistener);
    }

    BaseRequestListener slistener = new JsonRequestListener() {

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
            if (index == ((TestQuestionNewActivity) parentActivity).questionPagesList.size() - 1) {//最后一页查看结果
                showShortToast("此页答案提交成功！请查看结果");
                String getCode = jsonResult.optString("batchCode");
                if (!TextUtils.isEmpty(getCode)) {//如果当前答题返回了batchCode 就接收
                    ((TestQuestionNewActivity) parentActivity).batchCode = getCode;
                }
                Intent it = new Intent(parentActivity, TestResultWebViewNewActivity.class);
                if (!TextUtils.isEmpty(mQuestionPage.getBatchCode())) {//批次号不为空则提交答案时传入
                    it.putExtra("batchCode", mQuestionPage.getBatchCode());
                } else if (!TextUtils.isEmpty(((TestQuestionNewActivity) parentActivity).batchCode)) {//未登录用户的batchCode在填写资料时回传，提交必须带上
                    it.putExtra("batchCode", ((TestQuestionNewActivity) parentActivity).batchCode);
                }
                it.putExtra("childId", ((TestQuestionNewActivity) parentActivity).childId);
                it.putExtra("relationId", ((TestQuestionNewActivity) parentActivity).relationId);
                parentActivity.startActivity(it);
                parentActivity.finish();
            } else {//展示下一页题目
                showShortToast("此页答案提交成功！请继续答题");
                ((TestQuestionNewActivity) parentActivity).pager.setCurrentItem(index + 1);
                String getCode = jsonResult.optString("batchCode");
                if (!TextUtils.isEmpty(getCode)) {
                    ((TestQuestionNewActivity) parentActivity).batchCode = getCode;
                }
            }
        }
    };
}
