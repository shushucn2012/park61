package com.park61.moduel.me;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.park61.BaseActivity;
import com.park61.R;

/**
 * Created by shubei on 2017/7/5.
 */

public class AuthFirstActivity extends BaseActivity {

    private Button btn_to_auth;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_auth_first);
    }

    @Override
    public void initView() {
        btn_to_auth = (Button) findViewById(R.id.btn_to_auth);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        btn_to_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AuthInputActivity.class));
            }
        });
    }
}
