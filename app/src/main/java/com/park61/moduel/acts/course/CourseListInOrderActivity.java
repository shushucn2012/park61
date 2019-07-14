package com.park61.moduel.acts.course;

import android.widget.ListView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.acts.adapter.CourseListAdapter;
import com.park61.moduel.acts.bean.ActItem;
import com.park61.moduel.acts.bean.CourseBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by super on 2016/8/26.
 */
public class CourseListInOrderActivity extends BaseActivity {

    private long applyId;//场次id
    private long orderId;//订单id
    private ActItem curSession;//当前场次信息
    private CourseListAdapter courseListAdapter;
    private List<CourseBean> courseBeanList;

    private ListView lv_course;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_courselist_in_order);
    }

    @Override
    public void initView() {
        lv_course = (ListView) findViewById(R.id.lv_course);
    }

    @Override
    public void initData() {
        applyId = getIntent().getLongExtra("applyId", -1);
        orderId = getIntent().getLongExtra("orderId", -1);
        courseBeanList = new ArrayList<>();
        courseListAdapter = new CourseListAdapter(mContext, courseBeanList, true);
        lv_course.setAdapter(courseListAdapter);
        asyncGetCourseList();
    }

    @Override
    public void initListener() {

    }

    /**
     * 获取课程列表
     */
    protected void asyncGetCourseList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_REGISTER_CLASS;
        if (orderId != -1) {
            wholeUrl = AppUrl.host + AppUrl.GET_REGISTER_CLASS_BYORDER;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("actApplyId", applyId);
        map.put("orderId", orderId);
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
            logout("jsonResult======" + jsonResult);
            dismissDialog();
            courseBeanList.clear();
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay == null) {
                return;
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                CourseBean c = gson.fromJson(actJot.toString(), CourseBean.class);
                courseBeanList.add(c);
            }
            courseListAdapter.notifyDataSetChanged();
        }
    };

}
