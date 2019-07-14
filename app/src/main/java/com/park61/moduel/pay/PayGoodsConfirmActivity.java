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
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.moduel.grouppurchase.FightGroupDetailsActivity;
import com.park61.moduel.order.bean.OrderInfoBean;
import com.park61.moduel.pay.adapter.PayMethodAdapter;
import com.park61.moduel.pay.alipay.Result;
import com.park61.moduel.sales.bean.PaymentMethod;
import com.park61.moduel.sales.bean.TradeOrder;
import com.park61.net.base.Request;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.VCodeDialog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayGoodsConfirmActivity extends BaseActivity {

    // 支付宝支付
    private static final int RQF_PAY = 1;
    private long orderId;// 订单id
    private int selectPmethodPos = -1;// 选择的支付方式位置

    private ListView lv_pay_method;
    private TextView tv_pay_amount, tv_order_num;
    private Button btn_confirm;
    private VCodeDialog vd;

    private List<PaymentMethod> pList;
    private PayMethodAdapter mPayMethodAdapter;
    private TradeOrder tradeOrder;
    private OrderInfoBean order;
    private Long fightGroupOpenId;
    private int orderType;
    private String finishToPage;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_pay_goods_confirm);
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
        finishToPage = getIntent().getStringExtra("finishToPage");
        orderId = getIntent().getLongExtra("orderId", 0l);
        pList = new ArrayList<PaymentMethod>();
        mPayMethodAdapter = new PayMethodAdapter(mContext, pList);
        lv_pay_method.setAdapter(mPayMethodAdapter);
        asyncGetPayInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doValidatePhoneEmpty();
    }

    @Override
    public void initListener() {
        registerOnPayFinishReceiver();
        lv_pay_method.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (pList.get(position).getPaymentMethodType() == 2 && !pList.get(position).isEnough()) {
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
                if (selectPmethodPos < 0) {
                    showShortToast("请选择支付方式");
                    return;
                }
                if (pList.get(selectPmethodPos).getId() == 2) {// 支付宝
                    asyncGetAlipayParams();
                } else if (pList.get(selectPmethodPos).getId() == 3) {// 微信
                    asyncGetWxParams();
                } else if (pList.get(selectPmethodPos).getId() == 1) {// 余额支付
                    if (!pList.get(selectPmethodPos).isEnough()) {
                        return;
                    }
                    asyncSendVerifyCode();
                } else {
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
                                dDialog.dismissDialog();
                                if ("childtest".equals(finishToPage)) {
                                    asyncCancelOrder();
                                } else {
                                    finish();
                                }
                            }
                        }, null);
            }
        });
    }

    /**
     * 获取支付详情
     */
    protected void asyncGetPayInfo() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.paymentInfo;
        String requestBodyData = ParamBuild.getPayInfo(orderId);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getPayInfolistener);
    }

    BaseRequestListener getPayInfolistener = new JsonRequestListener() {

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
            tradeOrder = gson.fromJson(jsonResult.toString(), TradeOrder.class);
            if (pList != null && tradeOrder != null) {
                pList.addAll(tradeOrder.getPaymentMethodList());
            }
            mPayMethodAdapter.notifyDataSetChanged();
            mPayMethodAdapter.setBalanceValue(FU.strBFmt(tradeOrder.getAccountBalance()),
                    FU.strBFmt(tradeOrder.getOrderToPayAmount()));
            setDateToView();
        }
    };

    protected void setDateToView() {
        tv_pay_amount.setText(tradeOrder.getOrderToPayAmount() + "");
        tv_order_num.setText(tradeOrder.getOrderId() + "");
    }

    /**
     * 获取发送余额支付验证码
     */
    protected void asyncSendVerifyCode() {
        String wholeUrl = AppUrl.host + AppUrl.SEND_PAYMENT_VERIFYCODE;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, sendVcodeListener);
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
            if (vd == null) {
                vd = new VCodeDialog(mContext, mobile.substring(mobile.length() - 4, mobile.length()));
                vd.showDialog(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(vd.getInputCode())) {
                            showShortToast("您没有输入验证码！");
                            return;
                        }
                        asyncBalancePay(vd.getInputCode(), pList.get(selectPmethodPos).getId());
                        vd.dismissDialog();
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
            GlobalParam.TradeCartNeedRefresh = true;
            GlobalParam.MyOrderNeedRefresh = true;
        }
    };

    /**
     * 发送余额支付请求
     */
    protected void asyncBalancePay(String verifyCode, long paymentMethodId) {
        String wholeUrl = AppUrl.host + AppUrl.BALANCE_PAYMENT;
        String requestBodyData = ParamBuild.balancePayment(orderId,
                tradeOrder.getOrderToPayAmount() + "", verifyCode, paymentMethodId);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, balancePayListener);
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
            goToSuccess();
        }
    };

    /**
     * 获取微信参数
     */
    private void asyncGetWxParams() {
        String wholeUrl = AppUrl.host + "/pay/doPrePayByWx";
        String requestBodyData = ParamBuild.toPay(
                PayRechargeConfirmActivity.RechargeType.GOODS,
                tradeOrder.getOrderId() + "");
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getWxParamsLsner);
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
            IWXAPI api = WXAPIFactory.createWXAPI(mContext, GlobalParam.WX_APP_ID);
            api.sendReq(req);
            GlobalParam.TradeCartNeedRefresh = true;
            GlobalParam.MyOrderNeedRefresh = true;
        }
    };

    /**
     * 获取支付宝参数
     */
    private void asyncGetAlipayParams() {
        String wholeUrl = AppUrl.host + "/pay/doPrePayByAlipay";
        String requestBodyData = ParamBuild.toPay(
                PayRechargeConfirmActivity.RechargeType.GOODS,
                tradeOrder.getOrderId() + "");
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getAlipayParamsLsner);
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
                    PayTask alipay = new PayTask(PayGoodsConfirmActivity.this);
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
            GlobalParam.TradeCartNeedRefresh = true;
            GlobalParam.MyOrderNeedRefresh = true;
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
                    logout("result======" + result);
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

    /**
     * 微信支付成功通知广播接收器
     */
    private BroadcastReceiver onPayFinishReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String payResult = intent.getStringExtra("payResult");
            if (payResult.equals("SUCCESS")) {
                goToSuccess();
            }
        }
    };

    /**
     * 注册微信支付成功通知广播接收器
     */
    private void registerOnPayFinishReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_PAY_FINISH");
        mContext.registerReceiver(onPayFinishReceiver, filter);
    }

    /**
     * 跳转到成功页
     */
    public void goToSuccess() {
        if ("course".equals(finishToPage)) {//课程跳转课程支付成功页
            Intent paySuccessIt = new Intent(mContext, PayCourseSuccessActivity.class);
            paySuccessIt.putExtra("orderId", orderId);
            startActivity(paySuccessIt);
            finish();
            return;
        }
        asyncGetOrderDetail();
        Intent changeIt = new Intent();
        changeIt.setAction("ACTION_REFRESH_ORDER");
        changeIt.putExtra("ORDER_TYPE", 2);
        mContext.sendBroadcast(changeIt);
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
     * 请求订单详情
     */
    private void asyncGetOrderDetail() {
        String wholeUrl = AppUrl.host + AppUrl.ORDER_DETAIL_V2;
        String requestBodyData = ParamBuild.getOrderDetail(orderId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
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
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            order = gson.fromJson(jsonResult.toString(), OrderInfoBean.class);
            orderType = order.getOrderType();
            fightGroupOpenId = order.getFightGroupOpenId();
            logout("==PayAllSuccessActivity====" + fightGroupOpenId);
            if (orderType == 2) {//拼团
                Intent it = new Intent(mContext, FightGroupDetailsActivity.class);
                it.putExtra("opneId", fightGroupOpenId);
                it.putExtra("soId", orderId);
                it.putExtra("from", "pay");
                startActivity(it);
                finish();
            } else {
                Intent paySuccessIt = new Intent(mContext, PayAllSuccessActivity.class);
                startActivity(paySuccessIt);
                finish();
            }
        }
    };

    /**
     * 请求取消订单
     */
    private void asyncCancelOrder() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.cancelOrder;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", orderId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, clistener);
    }

    BaseRequestListener clistener = new JsonRequestListener() {

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
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        dDialog.showDialog("提示", "您还没有支付，确认返回吗？", "确认", "取消",
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                        if ("childtest".equals(finishToPage)) {
                            asyncCancelOrder();
                        } else {
                            finish();
                        }
                    }
                }, null);
    }
}
