package com.park61.moduel.accountsafe;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.moduel.me.BindPhoneActivity;
import com.park61.moduel.me.ChangePasswordActivity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

/**
 * 账户安全页面
 * Created by super on 2016/9/8.
 */
public class AccountSafeActivity extends BaseActivity {

    private View reset_trade_pwd_area, reset_login_pwd_area, modify_mobile_area,
            bind_alipay_area;
    private TextView tv_mobile, tv_account;

    private String alipayAccount;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_accountsafe);
    }

    @Override
    public void initView() {
        modify_mobile_area = findViewById(R.id.modify_mobile_area);
        reset_login_pwd_area = findViewById(R.id.reset_login_pwd_area);
        reset_trade_pwd_area = findViewById(R.id.reset_trade_pwd_area);
        bind_alipay_area = findViewById(R.id.bind_alipay_area);

        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        tv_account = (TextView) findViewById(R.id.tv_account);
    }

    @Override
    public void initData() {
        tv_mobile.setText(GlobalParam.currentUser.getMobile());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //asyncGetAliPayInfo();
    }

    @Override
    public void initListener() {
        modify_mobile_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, BindPhoneActivity.class));
            }
        });
        reset_trade_pwd_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ResetTradePwdActivity.class));
            }
        });
        reset_login_pwd_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ChangePasswordActivity.class));
            }
        });
        bind_alipay_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, BindAliPayActivity.class);
                if ("未绑定".equals(alipayAccount) || TextUtils.isEmpty(alipayAccount)) {
                    it.putExtra("bindOrModify", "bind");
                } else {
                    it.putExtra("bindOrModify", "modify");
                    it.putExtra("oldAccount", alipayAccount);
                }
                startActivity(it);
            }
        });
    }

    /**
     * 获取账户安全基本信息
     */
    private void asyncGetAliPayInfo() {
        String wholeUrl = AppUrl.host + AppUrl.GET_ALIPAY_BY_USERID;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, listener);
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
            if ("0000010013".equals(errorCode)) {
                reset_trade_pwd_area.setVisibility(View.GONE);
            } else {
                reset_trade_pwd_area.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            alipayAccount = jsonResult.optString("alipayAccount");
            fillData();
        }
    };

    private void fillData() {
        tv_account.setText(alipayAccount);
        if ("未绑定".equals(alipayAccount)) {
            reset_trade_pwd_area.setVisibility(View.GONE);
        } else {
            reset_trade_pwd_area.setVisibility(View.VISIBLE);
        }
    }
}
