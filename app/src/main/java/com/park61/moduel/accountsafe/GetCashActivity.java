package com.park61.moduel.accountsafe;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.common.tool.ViewInitTool;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.pw.GetCashInputPwdPopWin;
import com.park61.widget.textview.AccoutSafeTimeTextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝提现页面
 * Created by super on 2016/9/9.
 */
public class GetCashActivity extends BaseActivity {

    private Button btn_get;
    private GetCashInputPwdPopWin mGetCashInputPwdPopWin;
    private TextView tv_aliaccount, tv_sendvcode, tv_get_all_money, tv_limit_label, tv_canout_money, tv_limit_time;
    private AccoutSafeTimeTextView ttv_time;//倒计时文本
    private ImageView img_del;
    private EditText edit_vcode, edit_money;
    private View area_ali_account;

    private String alipayAccount;
    private String mobile;
    private String amount, pwd, vcode, limitMsg, limitTimeMsg;
    private double limitMoney, canOutMoney;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_get_cash);
    }

    @Override
    public void initView() {
        btn_get = (Button) findViewById(R.id.btn_get);
        tv_aliaccount = (TextView) findViewById(R.id.tv_aliaccount);
        img_del = (ImageView) findViewById(R.id.img_del);
        edit_vcode = (EditText) findViewById(R.id.edit_vcode);
        edit_money = (EditText) findViewById(R.id.edit_money);
        tv_sendvcode = (TextView) findViewById(R.id.tv_sendvcode);
        ttv_time = (AccoutSafeTimeTextView) findViewById(R.id.ttv_time);
        tv_get_all_money = (TextView) findViewById(R.id.tv_get_all_money);
        tv_limit_label = (TextView) findViewById(R.id.tv_limit_label);
        tv_canout_money = (TextView) findViewById(R.id.tv_canout_money);
        area_ali_account = findViewById(R.id.area_ali_account);
        tv_limit_time = (TextView) findViewById(R.id.tv_limit_time);
    }

    @Override
    public void initData() {
        ViewInitTool.addEditTextLimit2AfterPoint(edit_money);
        asyncGetAliPayInfo();
    }

    @Override
    public void initListener() {
        area_ali_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, BindAliPayActivity.class);
                it.putExtra("bindOrModify", "modify");
                it.putExtra("oldAccount", alipayAccount);
                startActivityForResult(it, 0);
            }
        });
        edit_vcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    img_del.setVisibility(View.VISIBLE);
                } else {
                    img_del.setVisibility(View.INVISIBLE);
                }
            }
        });
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_vcode.setText("");
            }
        });
        tv_sendvcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncSendVcode();
            }
        });
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }
                mGetCashInputPwdPopWin = new GetCashInputPwdPopWin(mContext, onConfirmListener, findViewById(R.id.cover));
                mGetCashInputPwdPopWin.showAtLocation(
                        findViewById(R.id.root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                        0, 0); //设置layout在PopupWindow中显示的位置
                GlobalParam.ProfitActivityNeedRefresh = true;
            }
        });
        tv_get_all_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = FU.strDbFmt(canOutMoney)+"";
                edit_money.setText(amount);
                if (FU.isNumEmpty(amount)) {
                    showShortToast("没有可提现金额！");
                    return;
                }
                if (TextUtils.isEmpty(vcode)) {
                    showShortToast("验证码没有输入！");
                    return;
                }
                mGetCashInputPwdPopWin = new GetCashInputPwdPopWin(mContext, onConfirmListener, findViewById(R.id.cover));
                mGetCashInputPwdPopWin.showAtLocation(
                        findViewById(R.id.root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                        0, 0); //设置layout在PopupWindow中显示的位置
            }
        });
    }

    private GetCashInputPwdPopWin.OnConfirmListener onConfirmListener =
            new GetCashInputPwdPopWin.OnConfirmListener() {
                @Override
                public void onConfirm(String mpwd) {
                    pwd = mpwd;
                    if (!validate()) {
                        return;
                    }
                    asyncDoWithdraw();
                }
            };

    private boolean validate() {
        amount = edit_money.getText().toString();
        vcode = edit_vcode.getText().toString();
        if (FU.isNumEmpty(amount)) {
            showShortToast("没有输入提现金额！");
            return false;
        }
        if (TextUtils.isEmpty(vcode)) {
            showShortToast("验证码没有输入！");
            return false;
        }
        return true;
    }

    /**
     * 开始计时
     */
    public void reStartMyTimer() {
        if (!ttv_time.isRun()) {
            tv_sendvcode.setVisibility(View.GONE);
            ttv_time.setVisibility(View.VISIBLE);
            ttv_time.setTimes(new long[]{0, 0, 0, 61});
            ttv_time.run();
            ttv_time.setOnTimeEndLsner(new AccoutSafeTimeTextView.OnTimeEnd() {
                @Override
                public void onEnd() {
                    tv_sendvcode.setVisibility(View.VISIBLE);
                    ttv_time.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 获取账户安全基本信息
     */
    private void asyncGetAliPayInfo() {
        String wholeUrl = AppUrl.host + AppUrl.GET_ALIPAY_BY_USERID;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            if ("0000010013".equals(errorCode)) {//如果没有绑定支付宝
                //跳到绑定页面
                Intent it = new Intent(mContext, BindAliPayActivity.class);
                it.putExtra("bindOrModify", "bind");
                startActivity(it);
                finish();
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            alipayAccount = jsonResult.optString("alipayAccount");
            mobile = jsonResult.optString("mobile");
            limitMsg = jsonResult.optString("message");
            limitTimeMsg = jsonResult.optString("message1");
            limitMoney = FU.paseDb(jsonResult.optString("limit"));
            canOutMoney = FU.paseDb(jsonResult.optString("outHave"));
            fillData();
        }
    };

    private void fillData() {
        tv_aliaccount.setText(alipayAccount);
        tv_limit_label.setText(limitMsg);
        tv_canout_money.setText("可提现金额：￥" + FU.strDbFmt(canOutMoney));
        tv_limit_time.setText(limitTimeMsg);
        if ("未绑定".equals(alipayAccount) || TextUtils.isEmpty(alipayAccount)) {
            showShortToast("您还未绑定支付宝账户！");
            //跳到绑定页面
            Intent it = new Intent(mContext, BindAliPayActivity.class);
            it.putExtra("bindOrModify", "bind");
            startActivity(it);
            finish();
        }
    }

    /**
     * 发送验证码
     * asyncSendVcode
     */
    private void asyncSendVcode() {
        String wholeUrl = AppUrl.host + AppUrl.SEND_DOWITHDRAW_VCODE;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, vcodelistener);
    }

    BaseRequestListener vcodelistener = new JsonRequestListener() {

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
            showShortToast("验证码已发送！");
            reStartMyTimer();
        }
    };

    /**
     * 提现请求
     * asyncSendVcode
     */
    private void asyncDoWithdraw() {
        String wholeUrl = AppUrl.host + AppUrl.DO_WITHDRAW;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("amount", amount);
        map.put("password", pwd);
        map.put("verifyCode", vcode);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getlistener);
    }

    BaseRequestListener getlistener = new JsonRequestListener() {

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
            Intent it = new Intent(mContext, GetCashSuccessActivity.class);
            it.putExtra("money", amount);
            startActivity(it);
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            asyncGetAliPayInfo();
        }
    }
}
