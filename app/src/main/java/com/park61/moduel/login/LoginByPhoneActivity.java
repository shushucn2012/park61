package com.park61.moduel.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.tool.RegexValidator;
import com.park61.common.tool.ViewInitTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/9/25.
 */

public class LoginByPhoneActivity extends BaseActivity {

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
        edit_user_phone = (EditText) findViewById(R.id.edit_user_phone);
        btn_next = (Button) findViewById(R.id.btn_next);
        area_back = findViewById(R.id.area_back);
    }

    @Override
    public void initData() {
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
                if(!validatePhone()){
                    return;
                }
                Intent it = new Intent(mContext, LoginByPhoneVCodeActivity.class);
                it.putExtra("userMobile", userMobile);
                startActivity(it);
            }
        });
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

    @Override
    public void initListener() {
        List<EditText> etList = new ArrayList<>();
        etList.add(edit_user_phone);
        ViewInitTool.addJudgeBtnEnable2Listener(etList, btn_next);
    }
}
