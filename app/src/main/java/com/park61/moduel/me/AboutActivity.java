package com.park61.moduel.me;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

public class AboutActivity extends BaseActivity {

    private TextView tv_version;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_about);
    }

    @Override
    public void initView() {
        tv_version = (TextView) findViewById(R.id.tv_version);
        Button btn_request = (Button) findViewById(R.id.btn_request);
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 退用户押金
     *//*
    private void asyncBackUserDeposit() {
        String wholeUrl = "http://101.200.173.227:9090/rest/auth/login?user=ayyey&passwd=MTIzNDU2&_=1497342288801";//AppUrl.host + AppUrl.EXTRACTDEPOSITAMOUNT;
        String requestBodyData = ParamBuild.getUserInfo();
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, ASYNCREQ_GET_USER, blistener);
    }

    BaseRequestListener blistener = new JsonRequestListener() {

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
            showShortToast("押金退还申请成功，预计2小时内到账");
        }
    };*/

    @Override
    public void initData() {
        tv_version.setText("版本号：V" + GlobalParam.versionName);
    }

    @Override
    public void initListener() {

    }

}
