package com.park61.moduel.me;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.child.bean.ShowItem;
import com.park61.moduel.login.bean.UserManager;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubei on 2017/7/11.
 */

public class ResumeInputActivity extends BaseActivity {

    private View top_area;
    private EditText edit_comt;
    private Button btn_send;
    private String contentStr;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_resume_input);
    }

    @Override
    public void initView() {
        top_area = findViewById(R.id.top_area);
        edit_comt = (EditText) findViewById(R.id.edit_comt);
        btn_send = (Button) findViewById(R.id.btn_send);
        /*new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                edit_comt.requestFocus();
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.showSoftInput(edit_comt, 0);
            }
        }, 500);*/
    }

    @Override
    public void initData() {
        contentStr = getIntent().getStringExtra("resume");
        edit_comt.setText(contentStr);
    }

    @Override
    public void initListener() {
        top_area.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_comt.getWindowToken(), 0);
                finish();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                contentStr = edit_comt.getText().toString().trim();
                if (TextUtils.isEmpty(contentStr)) {
                    showShortToast("您还未填写个人简介");
                    return;
                }
                asyncCommitGrowComt();
            }
        });
    }

    /**
     * 请求提交成长评论
     */
    private void asyncCommitGrowComt() {
        String wholeUrl = AppUrl.host + AppUrl.changeResume;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resume", contentStr);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, comtActListener);
    }

    BaseRequestListener comtActListener = new JsonRequestListener() {

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
            setResult(RESULT_OK, new Intent());
            GlobalParam.currentUser.setResume(contentStr);
            UserManager.getInstance().setAccountInfo(GlobalParam.currentUser, mContext);
            finish();
        }
    };
}
