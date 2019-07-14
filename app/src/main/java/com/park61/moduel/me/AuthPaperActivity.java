package com.park61.moduel.me;

import android.view.View;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.moduel.me.bean.AuthInfoBean;

/**
 * Created by shubei on 2017/7/6.
 */

public class AuthPaperActivity extends BaseActivity {

    private AuthInfoBean authInfoBean;

    private TextView tv_name, tv_time;
    private View area_back;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_auth_paper);
    }

    @Override
    public void initView() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_time = (TextView) findViewById(R.id.tv_time);
        area_back = findViewById(R.id.area_back);
    }

    @Override
    public void initData() {
        authInfoBean = (AuthInfoBean) getIntent().getSerializableExtra("authInfoBean");
        tv_name.setText(authInfoBean.getExpertName());
        tv_time.setText(DateTool.L2SEndDay3(authInfoBean.getAuditTime() + ""));
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
    }
}
