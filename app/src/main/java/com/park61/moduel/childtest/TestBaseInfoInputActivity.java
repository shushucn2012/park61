package com.park61.moduel.childtest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DateTool;
import com.park61.moduel.childtest.adapter.QuestionsBaseInfoAdapter;
import com.park61.moduel.childtest.bean.QBaseInfoItem;
import com.park61.moduel.me.MeEditActivity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.list.ListViewForScrollView;
import com.park61.widget.wheel.DateTimeDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 答题基础信息输入页面
 * Created by shubei on 2017/12/7.
 */

public class TestBaseInfoInputActivity extends BaseActivity {

    private static final int REQ_GET_NICKNAME = 1;
    private static final int REQ_GET_RELATION = 3;

    private ListViewForScrollView lv_base_questions;
    private Button btn_next;
    private View nickname_area, sex_area, birthday_area, relation_area;
    private TextView tv_nickname_value, tv_sex_value, tv_birthday_value, tv_relation_value;

    private QuestionsBaseInfoAdapter mQuestionsBaseInfoAdapter;
    private List<QBaseInfoItem> listItems;
    private int eaServiceId, childId;
    private String relationId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_test_baseinfo_input);
    }

    @Override
    public void initView() {
        setPagTitle("宝宝测评项");
        nickname_area = findViewById(R.id.nickname_area);
        sex_area = findViewById(R.id.sex_area);
        birthday_area = findViewById(R.id.birthday_area);
        relation_area = findViewById(R.id.relation_area);

        tv_nickname_value = (TextView) findViewById(R.id.tv_nickname_value);
        tv_sex_value = (TextView) findViewById(R.id.tv_sex_value);
        tv_birthday_value = (TextView) findViewById(R.id.tv_birthday_value);
        tv_relation_value = (TextView) findViewById(R.id.tv_relation_value);

        lv_base_questions = (ListViewForScrollView) findViewById(R.id.lv_base_questions);
        lv_base_questions.setFocusable(false);
        btn_next = (Button) findViewById(R.id.btn_next);
    }

    @Override
    public void initData() {
        childId = getIntent().getIntExtra("childId", 0);
        eaServiceId = getIntent().getIntExtra("eaServiceId", 0);
        listItems = new ArrayList<>();
        mQuestionsBaseInfoAdapter = new QuestionsBaseInfoAdapter(mContext, listItems);
        lv_base_questions.setAdapter(mQuestionsBaseInfoAdapter);
        asyncGetDataList();

        if (childId == 0) {
            findViewById(R.id.area_baseinfo_top).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.area_baseinfo_top).setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {
        nickname_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIt = new Intent(mContext, MeEditActivity.class);
                editIt.putExtra("current_data", tv_nickname_value.getText().toString());
                startActivityForResult(editIt, REQ_GET_NICKNAME);
            }
        });
        sex_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建我们的单选对话框
                int sexCode = tv_sex_value.getText().toString().equals("女") ? 1 : 0;
                Dialog dialog = new AlertDialog.Builder(mContext)
                        .setSingleChoiceItems(R.array.sex, sexCode, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                tv_sex_value.setText(mContext.getResources().getStringArray(R.array.sex)[which]);
                            }
                        })
                        .create();
                dialog.show();
            }
        });
        birthday_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimeDialog myTimeDialog = new DateTimeDialog(mContext);
                Calendar calendar = Calendar.getInstance();
                String birthday = tv_birthday_value.getText().toString();
                if (!TextUtils.isEmpty(birthday))
                    calendar.setTime(DateTool.getDateByDay(birthday));
                myTimeDialog.setTime(calendar);
                myTimeDialog.setOnChosenListener(new DateTimeDialog.OnChosenListener() {

                    @Override
                    public void onFinish(String year, String month, String day, String hour, String min) {
                        String backData = year + "-" + month + "-" + day;
                        if (DateTool.getDateByDay(backData).after(new Date(new Date().getTime() - 24 * 60 * 60 * 1000))) {
                            showShortToast("您选择的出生日期不能大于今天！");
                            return;
                        }
                        tv_birthday_value.setText(backData);
                    }
                });
                myTimeDialog.show();
            }
        });
        relation_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIt = new Intent(mContext, MeEditActivity.class);
                editIt.putExtra("current_data", tv_relation_value.getText());
                editIt.putExtra("relation", true);
                startActivityForResult(editIt, REQ_GET_RELATION);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childId == 0) {//没有宝宝先录宝宝信息，再录基础信息
                    asyncAddChildAlone();
                } else {//有宝宝直接录基础信息
                    asyncSaveChildBasicData();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null)
            return;
        String backData = data.getStringExtra("new_data");
        if (requestCode == REQ_GET_NICKNAME) {
            tv_nickname_value.setText(backData);
        } else if (requestCode == REQ_GET_RELATION) {
            tv_relation_value.setText(backData);
            relationId = data.getStringExtra("relationId");
        }
    }

    private void asyncGetDataList() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getBasicDataSubject;
        Map<String, Object> map = new HashMap<String, Object>();
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

            JSONArray dataJay = jsonResult.optJSONArray("list");
            if (dataJay != null) {
                for (int i = 0; i < dataJay.length(); i++) {
                    QBaseInfoItem qBaseInfoItem = gson.fromJson(dataJay.optJSONObject(i).toString(), QBaseInfoItem.class);
                    listItems.add(qBaseInfoItem);
                }
            }
            mQuestionsBaseInfoAdapter.notifyDataSetChanged();
        }
    };

    private void asyncSaveChildBasicData() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.saveChildBasicData;
        Map<String, Object> map = new HashMap<String, Object>();
        int chosenNum = 0;//选中答案的数量，如果该数量小于题目数，那么有题目未答不能提交
        //答案部分
        JSONArray jay_first = new JSONArray();
        for (QBaseInfoItem mQBaseInfoItem : listItems) {//一页中的所有大项
            JSONObject jot = new JSONObject();
            try {
                jot.put("subjectId", mQBaseInfoItem.getId());
                for (QBaseInfoItem.AnswersBean aItem : mQBaseInfoItem.getAnswers()) {
                    if (aItem.isChoosed()) {
                        chosenNum++;
                        jot.put("answerIds", aItem.getId());
                    }
                }
                jay_first.put(jot);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (chosenNum < listItems.size()) {
            showShortToast("请填写所有基础问题");
            return;
        }
        map.put("result", jay_first.toString());
        map.put("childId", childId);
        map.put("eaServiceId", eaServiceId);
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
            showShortToast("基础资料保存成功！");
            String batchCode = jsonResult.optString("batchCode");
            Intent it = new Intent(mContext, TestQuestionNewActivity.class);
            it.putExtra("childId", childId);
            it.putExtra("eaServiceId", eaServiceId);
            if (GlobalParam.userToken == null) {//未登录需要亲属关系id，答完后再登录之后保存,已登录不用传
                it.putExtra("relationId", relationId);
            }
            it.putExtra("batchCode", batchCode);
            startActivity(it);
            finish();
        }
    };

    private void asyncAddChildAlone() {
        if (TextUtils.isEmpty(tv_nickname_value.getText().toString())) {
            showShortToast("宝宝昵称未填写");
            return;
        }
        if (TextUtils.isEmpty(tv_sex_value.getText().toString())) {
            showShortToast("宝宝性别未填写");
            return;
        }
        if (TextUtils.isEmpty(tv_birthday_value.getText().toString())) {
            showShortToast("宝宝生日未填写");
            return;
        }
        if (TextUtils.isEmpty(tv_relation_value.getText().toString())) {
            showShortToast("亲属关系未填写");
            return;
        }
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.addChildAlone;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("birthday", tv_birthday_value.getText().toString());
        map.put("petName", tv_nickname_value.getText().toString().trim());
        map.put("sex", tv_sex_value.getText().toString().equals("女") ? 1 : 0);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, addlistener);
    }

    BaseRequestListener addlistener = new JsonRequestListener() {

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
            childId = jsonResult.optInt("data");
            if (GlobalParam.userToken == null) {//未登录
                asyncSaveChildBasicData();
            } else {//已登录，没有宝宝，新建宝宝后，把关系加上去,再去填基础信息
                asyncAddChildUserRel();
            }
        }
    };

    private void asyncAddChildUserRel() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.addChildUserRel;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("childId", childId);
        map.put("relationId", relationId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, rlistener);
    }

    BaseRequestListener rlistener = new JsonRequestListener() {

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
            asyncSaveChildBasicData();
        }
    };
}
