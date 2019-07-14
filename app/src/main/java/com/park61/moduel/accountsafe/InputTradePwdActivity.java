package com.park61.moduel.accountsafe;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ExitAppUtils;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.SkeyBoardPay;
import com.park61.widget.dialog.SingleDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 输入原交易密码页面
 * Created by super on 2016/9/8.
 */
public class InputTradePwdActivity extends BaseActivity {

    private EditText et_trade_pwd;
    private SkeyBoardPay keyboard_panel;
    private TextView tv_input_tip, tv_pwd_1, tv_pwd_2, tv_pwd_3, tv_pwd_4, tv_pwd_5, tv_pwd_6;
    private Button btn_confirm;
    private SingleDialog singleDialog;

    private String oldOrNewOrAg;//页面功能：old 输入旧密码；new 输入新密码；ag 重输入密码
    private String pwd;
    private String oldPwd, newPwd;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_input_trade_pwd);
    }

    @Override
    public void initView() {
        tv_input_tip = (TextView) findViewById(R.id.tv_input_tip);
        tv_pwd_1 = (TextView) findViewById(R.id.tv_pwd_1);
        tv_pwd_2 = (TextView) findViewById(R.id.tv_pwd_2);
        tv_pwd_3 = (TextView) findViewById(R.id.tv_pwd_3);
        tv_pwd_4 = (TextView) findViewById(R.id.tv_pwd_4);
        tv_pwd_5 = (TextView) findViewById(R.id.tv_pwd_5);
        tv_pwd_6 = (TextView) findViewById(R.id.tv_pwd_6);
        et_trade_pwd = (EditText) findViewById(R.id.et_trade_pwd);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        keyboard_panel = (SkeyBoardPay) findViewById(R.id.keyboard_panel);
        keyboard_panel.showKeyboard();
        keyboard_panel.attachEdit(et_trade_pwd);
        keyboard_panel.setOnKeyClickedListener(new SkeyBoardPay.OnKeyClickedListener() {
            @Override
            public void onClicked() {
                int num = et_trade_pwd.getText().toString().length();
                switch (num) {
                    case 0:
                        tv_pwd_1.setText("");
                        tv_pwd_2.setText("");
                        tv_pwd_3.setText("");
                        tv_pwd_4.setText("");
                        tv_pwd_5.setText("");
                        tv_pwd_6.setText("");
                        break;
                    case 1:
                        tv_pwd_1.setText("•");
                        tv_pwd_2.setText("");
                        tv_pwd_3.setText("");
                        tv_pwd_4.setText("");
                        tv_pwd_5.setText("");
                        tv_pwd_6.setText("");
                        break;
                    case 2:
                        tv_pwd_1.setText("•");
                        tv_pwd_2.setText("•");
                        tv_pwd_3.setText("");
                        tv_pwd_4.setText("");
                        tv_pwd_5.setText("");
                        tv_pwd_6.setText("");
                        break;
                    case 3:
                        tv_pwd_1.setText("•");
                        tv_pwd_2.setText("•");
                        tv_pwd_3.setText("•");
                        tv_pwd_4.setText("");
                        tv_pwd_5.setText("");
                        tv_pwd_6.setText("");
                        break;
                    case 4:
                        tv_pwd_1.setText("•");
                        tv_pwd_2.setText("•");
                        tv_pwd_3.setText("•");
                        tv_pwd_4.setText("•");
                        tv_pwd_5.setText("");
                        tv_pwd_6.setText("");
                        break;
                    case 5:
                        tv_pwd_1.setText("•");
                        tv_pwd_2.setText("•");
                        tv_pwd_3.setText("•");
                        tv_pwd_4.setText("•");
                        tv_pwd_5.setText("•");
                        tv_pwd_6.setText("");
                        break;
                    case 6:
                        tv_pwd_1.setText("•");
                        tv_pwd_2.setText("•");
                        tv_pwd_3.setText("•");
                        tv_pwd_4.setText("•");
                        tv_pwd_5.setText("•");
                        tv_pwd_6.setText("•");
                        break;
                }
            }
        });
        singleDialog = new SingleDialog(mContext);
    }

    @Override
    public void initData() {
        oldPwd = getIntent().getStringExtra("oldPwd");
        newPwd = getIntent().getStringExtra("newPwd");
        oldOrNewOrAg = getIntent().getStringExtra("OLD_OR_NEW_OR_AG");
        if ("old".equals(oldOrNewOrAg)) {
            tv_input_tip.setText("请输入原交易密码验证您的身份");
        } else if ("new".equals(oldOrNewOrAg)) {
            tv_input_tip.setText("请设置您的交易密码，密码由6位数字组成");
        } else if ("ag".equals(oldOrNewOrAg)) {
            tv_input_tip.setText("请再次输入以确认");
        } else { //默认
            tv_input_tip.setText("请设置您的交易密码，密码由6位数字组成");
        }
    }

    @Override
    public void initListener() {
        if ("old".equals(oldOrNewOrAg)) { //输入原密码页面
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validate())
                        return;
                    asyncCheckOldTradePwd();
                }
            });
        } else if ("new".equals(oldOrNewOrAg)) {//输入新密码页面
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validate())
                        return;
                    Intent it = new Intent(mContext, InputTradePwdActivity.class);
                    it.putExtra("OLD_OR_NEW_OR_AG", "ag");
                    it.putExtra("oldPwd", oldPwd);//将旧密码与新密码都传入确认页面
                    it.putExtra("newPwd", pwd);
                    startActivity(it);
                }
            });
        } else if ("ag".equals(oldOrNewOrAg)) {//重复新密码页面
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validate())
                        return;
                    asyncModifyTradePwd();
                }
            });
        }
    }

    private boolean validate() {
        pwd = et_trade_pwd.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            showShortToast("没有输入密码！");
            return false;
        }
        if (pwd.length() != 6) {
            showShortToast("密码不足6位！");
            return false;
        }
        if(!TextUtils.isEmpty(newPwd)){//新密码不为空，代表是确认页面，需要判断是否一致
            if(!newPwd.equals(pwd)){
                showShortToast("两次输入的密码不一致!");
                return false;
            }
        }
        return true;
    }

    /**
     * asyncCheckOldTradePwd
     */
    private void asyncCheckOldTradePwd() {
        String wholeUrl = AppUrl.host + AppUrl.CHECK_TRADE_PWD;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("password", pwd);
        String requestBodyData =  ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, checkOldListener);
    }

    BaseRequestListener checkOldListener = new JsonRequestListener() {

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
            it.putExtra("oldPwd", pwd);
            startActivity(it);
        }
    };

    /**
     * asyncModifyTradePwd
     */
    private void asyncModifyTradePwd() {
        String wholeUrl = "";
        if(TextUtils.isEmpty(oldPwd)) {//没有旧密码，设置密码
            wholeUrl = AppUrl.host + AppUrl.SET_TRADE_PWD;
        }else{//有旧密码，就是修改密码
            wholeUrl = AppUrl.host + AppUrl.MODIFY_TRADEPWD_BY_OLDPWD;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("oldPassword", oldPwd);
        map.put("password", newPwd);
        String requestBodyData =  ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, modifyListener);
    }

    BaseRequestListener modifyListener = new JsonRequestListener() {

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
            singleDialog.setOnDisLsner(new SingleDialog.OnDisLsner() {
                @Override
                public void onDismiss() {
                    for (Activity mActivity : ExitAppUtils.getInstance().getActList()) {
                        if (mActivity instanceof InputTradePwdActivity
                                || mActivity instanceof AcccountVerifyActivity) {
                            if (mActivity != null) {// 关闭所有页面，置回首页
                                mActivity.finish();
                            }
                        }
                    }
                }
            });
            singleDialog.showDialog("修改成功啦！", true);
        }
    };
}
