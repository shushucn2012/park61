package com.park61.moduel.pay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.moduel.pay.adapter.PayMethodAdapter;
import com.park61.moduel.pay.alipay.BalanceBean;
import com.park61.moduel.pay.alipay.Result;
import com.park61.moduel.sales.bean.PaymentMethod;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.VCodeDialog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayRechargeConfirmActivity extends BaseActivity {
    // 支付宝支付
    private static final int RQF_PAY = 1;
    private Long orderId;
    private String payMoney;
    private int selectPmethodPos;// 选择的支付方式位置

    private ListView lv_pay_method;
    private TextView tv_pay_amount, tv_order_num;
    private Button btn_confirm;
    private VCodeDialog vd;

    private List<PaymentMethod> pList;
    private PayMethodAdapter mPayMethodAdapter;
    private BalanceBean bb;

    public static class RechargeType {
        public static final int APPLY = 0;// 报名
        public static final int MEMBER = 1;
        public static final int BALANCE = 2;
        public static final int GOODS = 4;// 商品
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_pay_recharge_confirm);
    }

    @Override
    public void initView() {
        lv_pay_method = (ListView) findViewById(R.id.lv_pay_method);
        tv_pay_amount = (TextView) findViewById(R.id.tv_pay_amount);
        tv_order_num = (TextView) findViewById(R.id.tv_order_num);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

    @Override
    public void initData() {
        orderId = getIntent().getLongExtra("rechargeOrderId", 0l);
        payMoney = getIntent().getStringExtra("payMoney");
        tv_pay_amount.setText(payMoney);
        tv_order_num.setText(orderId + "");

        pList = new ArrayList<PaymentMethod>();
        PaymentMethod mPaymentMethod1 = new PaymentMethod();
        mPaymentMethod1.setId(1l);
        mPaymentMethod1.setIsDefault(0);
        mPaymentMethod1.setPaymentMethodName("余额支付");
        mPaymentMethod1
                .setPaymentMethodPicUrl("http://ghpprod.oss-cn-qingdao.aliyuncs.com/icon/yue.png");
        mPaymentMethod1.setPaymentMethodType(2);

        PaymentMethod mPaymentMethod2 = new PaymentMethod();
        mPaymentMethod2.setId(2l);
        mPaymentMethod2.setIsDefault(0);
        mPaymentMethod2.setPaymentMethodName("支付宝支付");
        mPaymentMethod2
                .setPaymentMethodPicUrl("http://ghpprod.oss-cn-qingdao.aliyuncs.com/icon/zhifubao.png");
        mPaymentMethod2.setPaymentMethodType(1);

        PaymentMethod mPaymentMethod3 = new PaymentMethod();
        mPaymentMethod3.setId(3l);
        mPaymentMethod3.setIsDefault(0);
        mPaymentMethod3.setPaymentMethodName("微信支付");
        mPaymentMethod3
                .setPaymentMethodPicUrl("http://ghpprod.oss-cn-qingdao.aliyuncs.com/icon/weixin.png");
        mPaymentMethod3.setPaymentMethodType(1);
        pList.add(mPaymentMethod1);
        pList.add(mPaymentMethod2);
        pList.add(mPaymentMethod3);

        mPayMethodAdapter = new PayMethodAdapter(mContext, pList);
        lv_pay_method.setAdapter(mPayMethodAdapter);

        asyncGetBalance();
    }

    /**
     * 获取余额
     */
    protected void asyncGetBalance() {
        String wholeUrl = AppUrl.host + AppUrl.GET_BALANCE;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
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
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            bb = gson.fromJson(jsonResult.toString(), BalanceBean.class);
            if (bb.getAvailableAmount() == null)
                bb.setAvailableAmount(new BigDecimal(0));
            mPayMethodAdapter.setBalanceValue(bb.getAvailableAmount()
                    .toString(), payMoney);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        doValidatePhoneEmpty();
    }

    @Override
    public void initListener() {
        lv_pay_method.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 余额小于支付金额时余额支付不能选择
                if (FU.paseDb(payMoney) - bb.getAvailableAmount().doubleValue() > 0
                        && pList.get(position).getPaymentMethodType() == 2) {
                    showShortToast("金额不足，不能选择余额支付！");
                    return;
                }
                selectPmethodPos = position;
                for (int i = 0; i < pList.size(); i++) {
                    if (position == i)
                        pList.get(i).setIsDefault(1);
                    else
                        pList.get(i).setIsDefault(0);
                }
                mPayMethodAdapter.notifyDataSetChanged();
            }
        });
        btn_confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pList.get(selectPmethodPos).getPaymentMethodName()
                        .contains("支付宝")) {
                    asyncGetAlipayParams();
                } else if (pList.get(selectPmethodPos).getPaymentMethodName()
                        .contains("微信")) {
                    asyncGetWxParams();
                } else if (pList.get(selectPmethodPos).getPaymentMethodName()
                        .contains("余额")) {
                    if (FU.paseDb(payMoney)
                            - bb.getAvailableAmount().doubleValue() > 0) {
                        return;
                    }
                    asyncSendVerifyCode();
                }
            }
        });
        setLeftListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dDialog.showDialog("提示", "您还没有支付，确认返回吗？", "确认", "取消",
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }, null);
            }
        });
        registerOnPayFinishReceiver();
    }

    /**
     * 获取发送余额支付验证码
     */
    protected void asyncSendVerifyCode() {
        String wholeUrl = AppUrl.host + AppUrl.SEND_PAYMENT_VERIFYCODE_MEMBER;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                sendVcodeListener);
    }

    BaseRequestListener sendVcodeListener = new JsonRequestListener() {

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
            String mobile = GlobalParam.currentUser.getMobile();
            if (TextUtils.isEmpty(mobile)) {
                showShortToast("您未绑定手机号，无法使用余额支付！");
                return;
            }
            if (vd == null || !vd.isShow()) {
                vd = new VCodeDialog(mContext, mobile.substring(
                        mobile.length() - 4, mobile.length()));
                vd.showDialog(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(vd.getInputCode())) {
                            showShortToast("您没有输入验证码！");
                            return;
                        }
                        asyncBalancePay(vd.getInputCode());
                    }
                }, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        asyncSendVerifyCode();
                        vd.reStartMyTimer();
                    }
                });
            } else {
                showShortToast("验证码已发送！");
            }
        }
    };

    /**
     * 发送余额支付请求
     */
    protected void asyncBalancePay(String verifyCode) {
        String wholeUrl = AppUrl.host + AppUrl.BALANCE_PAYMENT_MEMBER;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rechargeId", orderId);
        map.put("verifyCode", verifyCode);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                balancePayListener);
    }

    BaseRequestListener balancePayListener = new JsonRequestListener() {

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
            vd.dismissDialog();
            goToSuccess();
        }
    };

    /**
     * 注册支付成功通知广播接收器
     */
    private void registerOnPayFinishReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_PAY_FINISH");
        mContext.registerReceiver(onPayFinishReceiver, filter);
    }

    /**
     * 注销支付成功通知广播接收器
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(onPayFinishReceiver);
    }

    /**
     * 支付成功通知广播接收器
     */
    private BroadcastReceiver onPayFinishReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String payResult = intent.getStringExtra("payResult");
            Log.e("", "payResult======" + payResult);
            if (payResult.equals("SUCCESS")) {
                goToSuccess();
            }
        }
    };

    /**
     * 跳转到成功页
     */
    public void goToSuccess() {
        GlobalParam.GrowingMainNeedRefresh = true;// 宝宝页面需要刷新
        Intent paySuccessIt = new Intent(mContext, PayAllSuccessActivity.class);
        startActivity(paySuccessIt);
        finish();
    }

    /**
     * 获取微信参数
     */
    private void asyncGetWxParams() {
        String wholeUrl = AppUrl.host + "/pay/doPrePayByWx";
        String requestBodyData = ParamBuild.getRechargeParam(
                RechargeType.MEMBER, orderId, payMoney);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                getWxParamsLsner);
    }

    BaseRequestListener getWxParamsLsner = new JsonRequestListener() {

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
            PayReq req = new PayReq();
            try {
                req.appId = jsonResult.getString("appid");
                req.partnerId = jsonResult.getString("mch_id");
                req.nonceStr = jsonResult.getString("nonce_str");
                req.packageValue = jsonResult.getString("packageStr");
                req.timeStamp = jsonResult.getString("timeStamp");
                req.prepayId = jsonResult.getString("prepay_id");
                req.sign = jsonResult.getString("paySign");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            req.extData = "app data"; // optional
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            IWXAPI api = WXAPIFactory.createWXAPI(mContext,
                    GlobalParam.WX_APP_ID);
            api.sendReq(req);
        }
    };

    /**
     * 获取支付宝参数
     */
    private void asyncGetAlipayParams() {
        String wholeUrl = AppUrl.host + "/pay/doPrePayByAlipay";
        String requestBodyData = ParamBuild.getRechargeParam(
                RechargeType.MEMBER, orderId, payMoney);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                getAlipayParamsLsner);
    }

    BaseRequestListener getAlipayParamsLsner = new JsonRequestListener() {

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
            final String orderInfo = jsonResult.optString("orderInfo");
            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(
                            PayRechargeConfirmActivity.this);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(orderInfo, true);
                    Message msg = new Message();
                    msg.what = RQF_PAY;
                    msg.obj = result;
                    alipayHandler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }
    };

    /**
     * 支付宝结果回传
     */
    Handler alipayHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Result result = new Result((String) msg.obj);
            switch (msg.what) {
                case RQF_PAY:
                    // 处理支付结果
                    result.parseResult();
                    String tips = result.getTips();
                    showShortToast(tips);
                    if ("操作成功".equals(tips))
                        goToSuccess();
                    break;
                default:
                    break;
            }
        }

        ;
    };

}
