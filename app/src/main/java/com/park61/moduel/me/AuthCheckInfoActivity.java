package com.park61.moduel.me;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.moduel.me.bean.AuthInfoBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubei on 2017/7/6.
 */

public class AuthCheckInfoActivity extends BaseActivity {

    private Button btn_cancel;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_auth_checkinfo);
    }

    @Override
    public void initView() {
        setPagTitle("我的认证");
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDialog.showDialog("提示", "确定取消吗？", "确定", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        asyncCancelAuth();
                    }
                }, null);
            }
        });
    }

    private void asyncCancelAuth() {
        String wholeUrl = AppUrl.host + AppUrl.cancelAuth;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, infoLsner);
    }

    BaseRequestListener infoLsner = new JsonRequestListener() {

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
            showShortToast("取消成功!");
            finish();
        }
    };
}
