package com.park61.moduel.accountsafe;

import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.tool.FU;

/**
 * 提现申请成功
 * Created by super on 2016/9/11.
 */
public class GetCashSuccessActivity extends BaseActivity {

    private TextView tv_money;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_getcash_success);
    }

    @Override
    public void initView() {
        tv_money = (TextView) findViewById(R.id.tv_money);
    }

    @Override
    public void initData() {
        String money = getIntent().getStringExtra("money");
        tv_money.setText("提现金额：" + FU.strDbFmt(money) + "元");
    }

    @Override
    public void initListener() {

    }
}
