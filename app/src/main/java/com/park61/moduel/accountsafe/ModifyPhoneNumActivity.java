package com.park61.moduel.accountsafe;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.GlobalParam;

/**
 * 修改手机号-展示原手机号
 * Created by shushucn2012 on 2016/11/21.
 */
public class ModifyPhoneNumActivity extends BaseActivity {

    private TextView tv_old_phone;
    private Button btn_to_change;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_modify_phonenum);
    }

    @Override
    public void initView() {
        tv_old_phone = (TextView) findViewById(R.id.tv_old_phone);
        btn_to_change = (Button) findViewById(R.id.btn_to_change);
    }

    @Override
    public void initData() {
        String oldPhoneNumStr = GlobalParam.currentUser.getMobile();
        String oldPhoneNumStrShow = oldPhoneNumStr.substring(0, 3) + "****" + oldPhoneNumStr.substring(7);
        tv_old_phone.setText(oldPhoneNumStrShow);
        tv_old_phone.setEnabled(false);
    }

    @Override
    public void initListener() {
        findViewById(R.id.area_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_to_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ModifyPNVerifyActivity.class);
                startActivity(it);
            }
        });
    }
}
