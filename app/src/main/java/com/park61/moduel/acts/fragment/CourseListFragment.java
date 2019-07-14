package com.park61.moduel.acts.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.acts.adapter.CourseListAdapter;
import com.park61.moduel.acts.bean.ActItem;
import com.park61.moduel.acts.bean.CourseBean;
import com.park61.moduel.acts.course.CApplyConfirmActivity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by super on 2016/8/23.
 */
public class CourseListFragment extends BaseFragment {

    private long sessionId;//场次id
    private String actTitle;//活动标题
    private String actPrice, adultPrice;//活动场次价格
    private String courselistNumInfo;//课程可报名节数信息
    private int leftNum;//剩余可报人数
    private String actStartTime;//场次开始时间
    private ActItem curSession;//当前场次信息
    private CourseListAdapter courseListAdapter;
    private List<CourseBean> courseBeanList;
    private boolean canApply;//是否可报名

    private TextView tv_act_course_title, tv_course_prise, tv_course_applynum,
            tv_course_fullnum, tv_courselist_num_info;
    private ListView lv_course;
    private Button btn_apply;
    private Long reqPredId;//梦想预报名id

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_course_list, container, false);
        sessionId = getArguments().getLong("sessionId");
        actTitle = getArguments().getString("actTitle");
        actPrice = getArguments().getString("actPrice");
        adultPrice = getArguments().getString("adultPrice");
        actStartTime = getArguments().getString("actStartTime");
        canApply = getArguments().getBoolean("canApply");
        reqPredId = getArguments().getLong("reqPredId");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        tv_act_course_title = (TextView) parentView.findViewById(R.id.tv_act_course_title);
        tv_course_prise = (TextView) parentView.findViewById(R.id.tv_course_prise);
        tv_course_applynum = (TextView) parentView.findViewById(R.id.tv_course_applynum);
        tv_course_fullnum = (TextView) parentView.findViewById(R.id.tv_course_fullnum);
        tv_courselist_num_info = (TextView) parentView.findViewById(R.id.tv_courselist_num_info);
        lv_course = (ListView) parentView.findViewById(R.id.lv_course);
        btn_apply = (Button) parentView.findViewById(R.id.btn_apply);
        if (!canApply) {
            btn_apply.setBackgroundColor(getResources().getColor(R.color.gaaaaaa));
            btn_apply.setEnabled(false);
        }
    }

    @Override
    public void initData() {
        tv_act_course_title.setText(actTitle);
        tv_course_prise.setText("￥" + actPrice);
        courseBeanList = new ArrayList<>();
        courseListAdapter = new CourseListAdapter(parentActivity, courseBeanList);
        lv_course.setAdapter(courseListAdapter);
        asyncGetCourseList();
    }

    @Override
    public void initListener() {
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(parentActivity, CApplyConfirmActivity.class);
                it.putExtra("sessionId", sessionId);
                it.putExtra("actTitle", actTitle);
                it.putExtra("actPrice", actPrice);
                it.putExtra("adultPrice", adultPrice);
                it.putExtra("actStartTime", actStartTime);
                it.putExtra("courselistNumInfo", courselistNumInfo);
                it.putExtra("leftNum", leftNum);
                it.putExtra("reqPredId",reqPredId);
                parentActivity.startActivity(it);
            }
        });
    }

    /**
     * 获取课程列表
     */
    protected void asyncGetCourseList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_CLASS_AGMT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("actId", sessionId);
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
            showShortToast(errorMsg);
            dismissDialog();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            curSession = gson.fromJson(jsonResult.toString(), ActItem.class);
            setDataToView();
        }
    };

    private void setDataToView() {
        leftNum = curSession.getActLowQuotaLimit().intValue() - curSession.getActNumberVisitor();
        tv_course_applynum.setText("已报" + curSession.getActNumberVisitor() + "人,还剩" + leftNum + "个名额");
        tv_course_fullnum.setText(curSession.getActLowQuotaLimit() + "人班");
        int listLeftNum = 0;
        for (int i = 0; i < curSession.getActivityClasses().size(); i++) {
            if (!curSession.getActivityClasses().get(i).isEnd()) {
                listLeftNum++;
            }
        }
        courselistNumInfo = "可报" + listLeftNum + "节,共" + curSession.getActivityClasses().size() + "节";
        tv_courselist_num_info.setText(courselistNumInfo);

        courseBeanList.clear();
        courseBeanList.addAll(curSession.getActivityClasses());
        courseListAdapter.notifyDataSetChanged();
    }
}
