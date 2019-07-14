package com.park61.moduel.accountsafe;

import android.content.Intent;
import android.view.View;

import com.park61.BaseActivity;
import com.park61.R;

/**
 * 重置交易密码
 * Created by super on 2016/9/8.
 */
public class ResetTradePwdActivity extends BaseActivity{

    private View know_pwd_area, unknow_pwd_area;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_reset_trade_pwd);
    }

    @Override
    public void initView() {
        know_pwd_area = findViewById(R.id.know_pwd_area);
        unknow_pwd_area = findViewById(R.id.unknow_pwd_area);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        know_pwd_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, InputTradePwdActivity.class);
                it.putExtra("OLD_OR_NEW_OR_AG", "old");
                startActivity(it);
            }
        });
        unknow_pwd_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, AcccountVerifyActivity.class);
                startActivity(it);
            }
        });
    }
}
