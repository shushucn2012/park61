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
 * 支付宝绑定页面
 * Created by super on 2016/9/9.
 */
public class BindAliPayActivity extends BaseActivity {

    private EditText edit_aliaccount, edit_realname, edit_vcode;
    private ImageView img_del;
    private TextView tv_sendvcode, tv_aliaccount_label, tv_old_account;
    private AccoutSafeTimeTextView ttv_time;//倒计时文本
    private Button btn_confirm;
    private View area_old_account;

    private String vcode, account, realName;
    private String bindOrModify;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_bind_alipay);
    }

    @Override
    public void initView() {
        area_old_account = findViewById(R.id.area_old_account);
        tv_aliaccount_label = (TextView) findViewById(R.id.tv_aliaccount_label);
        edit_aliaccount = (EditText) findViewById(R.id.edit_aliaccount);
        edit_realname = (EditText) findViewById(R.id.edit_realname);
        edit_vcode = (EditText) findViewById(R.id.edit_vcode);
        img_del = (ImageView) findViewById(R.id.img_del);
        tv_sendvcode = (TextView) findViewById(R.id.tv_sendvcode);
        ttv_time =  (AccoutSafeTimeTextView) findViewById(R.id.ttv_time);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        tv_old_account = (TextView) findViewById(R.id.tv_old_account);
    }

    @Override
    public void initData() {
        bindOrModify = getIntent().getStringExtra("bindOrModify");
        if ("modify".equals(bindOrModify)) {
            area_old_account.setVisibility(View.VISIBLE);
            tv_aliaccount_label.setText("新账户");
            tv_old_account.setText(getIntent().getStringExtra("oldAccount"));
        }
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
                if(!validateAccount())
                    return;
                asyncBindAliAccount();
            }
        });
    }

    /**
     * 验证输入的账户信息是否完整
     */
    private boolean validateAccount() {
        account = edit_aliaccount.getText().toString().trim();
        realName  = edit_realname.getText().toString().trim();
        vcode = edit_vcode.getText().toString().trim();
        if(TextUtils.isEmpty(account)){
            showShortToast("账户没有输入！");
            return false;
        }
        if(TextUtils.isEmpty(realName)){
            showShortToast("姓名没有输入！");
            return false;
        }
        if(TextUtils.isEmpty(vcode)){
            showShortToast("验证码没有输入！");
            return false;
        }
        return true;
    }

    /**
     * 发送验证码
     * asyncSendVcode
     */
    private void asyncSendVcode() {
        String wholeUrl = AppUrl.host + AppUrl.SEND_BIND_ACCOUNT_VCODE;
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
     * 绑定支付宝账户
     * asyncBindAliAccount
     */
    private void asyncBindAliAccount() {
        String wholeUrl = AppUrl.host + AppUrl.SET_ALI_ACCOUNT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("account", account);
        map.put("realName", realName);
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
            showShortToast("绑定成功！");
            Intent it = new Intent(mContext, InputTradePwdActivity.class);
            it.putExtra("OLD_OR_NEW_OR_AG", "new");
            startActivity(it);
            finish();
        }
    };
}
