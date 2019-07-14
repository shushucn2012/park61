package com.park61.moduel.openmember;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;

/**
 * 新增推荐码
 */
public class CreateRecommendCodeActivity  extends BaseActivity{
    private EditText edit_info;
    private Button btn_save;
    private String recommendCode;
    private TextView tv_title;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_create_recommendcode);
    }

    @Override
    public void initView() {
        edit_info = (EditText) findViewById(R.id.edit_info);
        btn_save = (Button) findViewById(R.id.btn_save);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    @Override
    public void initData() {
        tv_title.setText(getIntent().getStringExtra("title"));
    }

    @Override
    public void initListener() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow
                        (getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                recommendCode = edit_info.getText().toString().replace("请输入内容", "");
                if (TextUtils.isEmpty(recommendCode)) {
                    showShortToast("请输入内容");
                    return ;
                }else {
                    Intent returnData = new Intent();
                    returnData.putExtra("recommendCode", edit_info.getText().toString());
                    setResult(RESULT_OK, returnData);
                    finish();
                }
            }
        });
    }
}
