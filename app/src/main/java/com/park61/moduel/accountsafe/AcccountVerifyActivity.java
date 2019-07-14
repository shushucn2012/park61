package com.park61.moduel.accountsafe;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.textview.AccoutSafeTimeTextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 账号验证页面
 * Created by super on 2016/9/9.
 */
public class AcccountVerifyActivity extends BaseActivity{

    private EditText edit_vcode;
    private Button btn_confirm;
    private ImageView img_del;
    private TextView tv_sendvcode;
    private AccoutSafeTimeTextView ttv_time;//倒计时文本

    private String vcode;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_account_verify);
    }

    @Override
    public void initView() {
        edit_vcode = (EditText) findViewById(R.id.edit_vcode);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        img_del = (ImageView) findViewById(R.id.img_del);
        tv_sendvcode = (TextView) findViewById(R.id.tv_sendvcode);
        ttv_time =  (AccoutSafeTimeTextView) findViewById(R.id.ttv_time);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        edit_vcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    img_del.setVisibility(View.VISIBLE);
                } else {
                    img_del.setVisibility(View.INVISIBLE);
                }
            }
        });
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_vcode.setText("");
            }
        });
        tv_sendvcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncSendVcode();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate())
                    return;
                asyncCheckVcode();
            }
        });
    }

    /**
     * 验证输入
     */
    private boolean validate() {
        vcode = edit_vcode.getText().toString();
        if(TextUtils.isEmpty(vcode)){
            showShortToast("验证码没有输入！");
            return false;
        }
        return true;
    }

    /**
     * 开始计时
     */
    public void reStartMyTimer() {
        if (!ttv_time.isRun()) {
            tv_sendvcode.setVisibility(View.GONE);
            ttv_time.setVisibility(View.VISIBLE);
            ttv_time.setTimes(new long[] { 0, 0, 0, 61 });
            ttv_time.run();
            ttv_time.setOnTimeEndLsner(new AccoutSafeTimeTextView.OnTimeEnd() {
                @Override
                public void onEnd() {
                    tv_sendvcode.setVisibility(View.VISIBLE);
                    ttv_time.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 发送验证码
     * asyncSendVcode
     */
    private void asyncSendVcode() {
        String wholeUrl = AppUrl.host + AppUrl.SEND_MODIFY_TRADEPWD_VCODE;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, vcodelistener);
    }

    BaseRequestListener vcodelistener = new JsonRequestListener() {

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
            showShortToast("验证码已发送！");
            reStartMyTimer();
        }
    };

    /**
     * 验证验证码是否正确
     * asyncCheckVcode
     */
    private void asyncCheckVcode() {
        String wholeUrl = AppUrl.host + AppUrl.MODIFY_TRADEPWD_BY_VCODE;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("verifyCode", vcode);
        String requestBodyData =  ParamBuild.buildParams(map);
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
            Intent it = new Intent(mContext, InputTradePwdActivity.class);
            it.putExtra("OLD_OR_NEW_OR_AG", "new");
            startActivity(it);
        }
    };
}
