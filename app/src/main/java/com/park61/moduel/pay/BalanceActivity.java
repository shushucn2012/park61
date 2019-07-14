package com.park61.moduel.pay;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.tool.FU;
import com.park61.moduel.pay.alipay.BalanceBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

/**
 * 账户余额
 */
public class BalanceActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_bill_detail, btn_recharge;
    private ImageView help_img;
    private TextView total_amount, recharge_balance_amount, presenter_balance_amount;
    private BalanceBean bb;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_account_balance);
    }

    @Override
    public void initView() {
        help_img = (ImageView) findViewById(R.id.help_img);
        total_amount = (TextView) findViewById(R.id.total_amount);
        recharge_balance_amount = (TextView) findViewById(R.id.recharge_balance_amount);
        presenter_balance_amount = (TextView) findViewById(R.id.presenter_balance_amount);
        btn_bill_detail = (Button) findViewById(R.id.btn_bill_detail);
        btn_recharge = (Button) findViewById(R.id.btn_recharge);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncGetBalance();
    }

    @Override
    public void initListener() {
        btn_recharge.setOnClickListener(this);
        btn_bill_detail.setOnClickListener(this);
        help_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_recharge:
                startActivity(new Intent(mContext, PayBalanceConfirmActivity.class));
                break;
            case R.id.btn_bill_detail:
                startActivity(new Intent(mContext, BillRecordActivity.class));
                break;

            case R.id.help_img:
                startActivity(new Intent(mContext,BalanceHelpActivity.class));
                break;
        }
    }

    /**
     * 获取余额
     */
    protected void asyncGetBalance() {
        String wholeUrl = AppUrl.host + AppUrl.GET_BALANCE;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,
                getBalancelistener);
    }

    BaseRequestListener getBalancelistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            bb = gson.fromJson(jsonResult.toString(), BalanceBean.class);
            fillData();
        }
    };

    protected void fillData() {
        if (bb != null && bb.getAvailableAmount() != null) {
            if (bb.getAvailableAmount() != null) {
                total_amount.setText(FU.strBFmt(bb.getAvailableAmount()));
            }
            if (bb.getRechargeAmount() != null) {
                recharge_balance_amount.setText(FU.strBFmt(bb.getRechargeAmount()));
            }
            if (bb.getGiftAmount() != null) {
                presenter_balance_amount.setText(FU.strBFmt(bb.getGiftAmount()));
            }
        }
    }
}
