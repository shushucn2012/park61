package com.park61.moduel.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.RegexValidator;
import com.park61.common.tool.ViewInitTool;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/9/26.
 */

public class RegisterV2Activity extends BaseActivity {

    private TextView tv_big_tip, tv_small_tip;
    private EditText edit_user_phone;
    private Button btn_next;
    private View area_back;

    private String userMobile;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_login_by_phone);
    }

    @Override
    public void initView() {
        tv_big_tip = (TextView) findViewById(R.id.tv_big_tip);
        tv_big_tip.setText("请输入手机号码");
        tv_small_tip = (TextView) findViewById(R.id.tv_small_tip);
        tv_small_tip.setVisibility(View.GONE);

        edit_user_phone = (EditText) findViewById(R.id.edit_user_phone);
        btn_next = (Button) findViewById(R.id.btn_next);
        area_back = findViewById(R.id.area_back);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        area_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                hideKeyboard();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePhone()) {
                    return;
                }
                asyncVerifyPhone();
            }
        });
        List<EditText> etList = new ArrayList<>();
        etList.add(edit_user_phone);
        ViewInitTool.addJudgeBtnEnable2Listener(etList, btn_next);
    }

    protected boolean validatePhone() {
        userMobile = edit_user_phone.getText().toString();
        if (TextUtils.isEmpty(userMobile)) {
            showShortToast("登录手机号没有填写！");
            return false;
        }
        if (!RegexValidator.isMobile(userMobile)) {
            showShortToast("手机号输入有误！");
            return false;
        }
        return true;
    }

    /**
     * 验证手机号是否存在
     */
    protected void asyncVerifyPhone() {
        String wholeUrl = AppUrl.host + AppUrl.checkLoginAccountIsExist;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mobile", userMobile);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, LLsner);
    }

    BaseRequestListener LLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            if ("061".equals(errorCode)) { // 该用户未被注册，才允许注册
                Intent it = new Intent(mContext, RegisterV2GetVCodeActivity.class);
                it.putExtra("userMobile", userMobile);
                startActivity(it);
            } else if ("0000015002".equals(errorCode)) {
                dDialog.showDialog("提示", "该手机号已被注册，验证手机号码后可以直接登录", "取消", "发送验证码", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(mContext, LoginByPhoneVCodeActivity.class);
                        it.putExtra("userMobile", userMobile);
                        startActivity(it);
                    }
                });
            } else  {
                showShortToast(errorMsg);
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            //showShortToast("该手机号已注册");
        }
    };

}
