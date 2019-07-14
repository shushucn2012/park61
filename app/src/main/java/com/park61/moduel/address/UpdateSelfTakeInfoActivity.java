package com.park61.moduel.address;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.RegexValidator;

/**
 * 修改自提人名字和电话信息界面
 */
public class UpdateSelfTakeInfoActivity extends BaseActivity {
    private EditText name_info, phone_info;
    private Button btn_save;
    private String rname, rphone;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_address_selftake_updateinfo);
    }

    @Override
    public void initView() {
        name_info = (EditText) findViewById(R.id.name_info);
        phone_info = (EditText) findViewById(R.id.phone_info);
        btn_save = (Button) findViewById(R.id.btn_save);
    }

    @Override
    public void initData() {
        name_info.setText(getIntent().getStringExtra("rname"));
        phone_info.setText(getIntent().getStringExtra("rphone"));
    }

    @Override
    public void initListener() {
        btn_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                rname = name_info.getText().toString().trim();
                rphone = phone_info.getText().toString().trim();
                if (!validateData()) {
                    return;
                }
                Intent returnData = new Intent();
                returnData.putExtra("rname", rname);
                returnData.putExtra("rphone", rphone);
                setResult(RESULT_OK, returnData);
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected boolean validateData() {
        if (TextUtils.isEmpty(rname)) {
            showShortToast("姓名不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(rphone)) {
            showShortToast("电话号码不能为空！");
            return false;
        }
        if (!RegexValidator.isMobile(rphone)) {
            showShortToast("电话号码输入有误！");
            return false;
        }
        return true;
    }

}
