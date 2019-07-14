package com.park61.moduel.me;

import org.json.JSONObject;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.RegexValidator;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import java.util.ArrayList;
import java.util.List;

import static com.park61.R.id.edit_user_pwd;
import static com.park61.R.id.img_eye;

public class ChangePasswordActivity extends BaseActivity {
    private EditText edit_current_psw, edit_new_psw;
    private Button btn_confirm;
    private ImageView img_del_old, img_del_new, img_eye_old, img_eye_new;
    private View area_back;

    private String currentPsw, newPsw;
    private boolean isShowPwdOld = false;//是否显示密码，初始为false
    private boolean isShowPwdNew = false;//是否显示密码，初始为false

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_change_password);
    }

    @Override
    public void initView() {
        edit_current_psw = (EditText) findViewById(R.id.edit_current_psw);
        edit_new_psw = (EditText) findViewById(R.id.edit_new_psw);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        img_del_old = (ImageView) findViewById(R.id.img_del_old);
        img_del_new = (ImageView) findViewById(R.id.img_del_new);
        img_eye_old = (ImageView) findViewById(R.id.img_eye_old);
        img_eye_new = (ImageView) findViewById(R.id.img_eye_new);
        area_back = findViewById(R.id.area_back);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        area_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                hideKeyboard();
            }
        });
        btn_confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!validateData()) {
                    return;
                }
                asyncChangePwd();
            }
        });
        List<EditText> etList = new ArrayList<>();
        etList.add(edit_current_psw);
        etList.add(edit_new_psw);
        ViewInitTool.addJudgeBtnEnable2Listener(etList, btn_confirm);

        initEditOldLsner();
        initEditNewLsner();
    }

    private void initEditOldLsner() {
        edit_current_psw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    img_del_old.setVisibility(View.VISIBLE);
                    img_eye_old.setVisibility(View.VISIBLE);
                } else {
                    img_del_old.setVisibility(View.GONE);
                    img_eye_old.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        img_del_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_current_psw.setText("");
            }
        });
        img_eye_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowPwdOld) {//已显示，点击则隐藏
                    img_eye_old.setImageResource(R.drawable.icon_eye_closed);
                    edit_current_psw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edit_current_psw.setSelection(edit_current_psw.getText().length());
                    isShowPwdOld = false;
                } else {//已隐藏，点击则显示
                    img_eye_old.setImageResource(R.drawable.icon_eye_open);
                    edit_current_psw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edit_current_psw.setSelection(edit_current_psw.getText().length());
                    isShowPwdOld = true;
                }
            }
        });
    }

    private void initEditNewLsner() {
        edit_new_psw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    img_del_new.setVisibility(View.VISIBLE);
                    img_eye_new.setVisibility(View.VISIBLE);
                } else {
                    img_del_new.setVisibility(View.GONE);
                    img_eye_new.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        img_del_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_new_psw.setText("");
            }
        });
        img_eye_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowPwdNew) {//已显示，点击则隐藏
                    img_eye_new.setImageResource(R.drawable.icon_eye_closed);
                    edit_new_psw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edit_new_psw.setSelection(edit_current_psw.getText().length());
                    isShowPwdNew = false;
                } else {//已隐藏，点击则显示
                    img_eye_new.setImageResource(R.drawable.icon_eye_open);
                    edit_new_psw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edit_new_psw.setSelection(edit_current_psw.getText().length());
                    isShowPwdNew = true;
                }
            }
        });
    }

    protected boolean validateData() {
        currentPsw = edit_current_psw.getText().toString().trim();
        newPsw = edit_new_psw.getText().toString().trim();
        if (TextUtils.isEmpty(currentPsw)) {
            showShortToast("请输入原密码");
            return false;
        }
        if (TextUtils.isEmpty(newPsw)) {
            showShortToast("请输入新密码");
            return false;
        }
        if (newPsw == null || newPsw.length() < 6) {
            showShortToast("密码太短，请输入6到20位密码!");
            return false;
        }
        if (!RegexValidator.isPassword(newPsw)) {
            showShortToast("密码不符合规则！请设置6到20位，由数字和字母组成密码！");
            return false;
        }
        return true;
    }

    /**
     * 修改密码
     */
    protected void asyncChangePwd() {
        String wholeUrl = AppUrl.host + AppUrl.UPDATE_PASSWORD;
        String requestBodyData = ParamBuild.updatePwd(currentPsw, newPsw, newPsw);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, changeLsner);
    }

    BaseRequestListener changeLsner = new JsonRequestListener() {

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
            showShortToast("修改密码成功");
            //startActivity(new Intent(ChangePasswordActivity.this, LoginV2Activity.class));
            finish();
        }
    };

}
