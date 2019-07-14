package com.park61.moduel.pay;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.me.bean.ApplyActItem;
import com.park61.moduel.pay.alipay.Result;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class PayBalanceConfirmActivity extends BaseActivity {
	// 支付宝支付
	private static final int RQF_PAY = 1;
	private Long orderId;
	private String payMoney;

	private ApplyActItem curApply;
	private Long applyId;// 订单id
	private ImageView img_act;
	private TextView tv_total_money, tv_act_title, tv_act_addr, tv_left_day,
			tv_apply_num;
	private Button btn_confirm;
	private CheckBox chk_wxpay, chk_alipay;
	private EditText et_recharge_money;
	private View input_money_area, pay_money_area;
	private View goto_read_xieyi;
	private TextView tv_page_title;

	public class RechargeType {
		public static final int APPLY = 0;// 报名
		public static final int MEMBER = 1;
		public static final int BALANCE = 2;
	}

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_pay_balance_confirm);
	}

	@Override
	public void initView() {
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		chk_wxpay = (CheckBox) findViewById(R.id.chk_wxpay);
		goto_read_xieyi = findViewById(R.id.goto_read_xieyi);
		// 默认选择微信
		chk_wxpay.setChecked(true);
		chk_alipay = (CheckBox) findViewById(R.id.chk_alipay);
		et_recharge_money = (EditText) findViewById(R.id.et_recharge_money);

		input_money_area = findViewById(R.id.input_money_area);
		input_money_area.setVisibility(View.VISIBLE);
		pay_money_area = findViewById(R.id.pay_money_area);
		pay_money_area.setVisibility(View.GONE);
		tv_page_title = (TextView) findViewById(R.id.tv_page_title);
		tv_page_title.setText("充值确认");
	}

	@Override
	public void initData() {
		// orderId = getIntent().getLongExtra("rechargeOrderId", 0l);
		// payMoney = getIntent().getStringExtra("payMoney");
	}

	@Override
	protected void onResume() {
		super.onResume();
		doValidatePhoneEmpty();
	}

	@Override
	public void initListener() {
		chk_wxpay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					chk_alipay.setChecked(false);
			}
		});
		chk_alipay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					chk_wxpay.setChecked(false);
			}
		});
		btn_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				payMoney = et_recharge_money.getText().toString().trim();
				if (TextUtils.isEmpty(payMoney)) {
					showShortToast("请输入充值金额！");
					return;
				}
				if (chk_wxpay.isChecked()) {
					asyncGetWxParams();
				} else {
					asyncGetAlipayParams();
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
		goto_read_xieyi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(mContext, XieyiActivity.class);
				it.putExtra("title", "充值代扣协议");
				it.putExtra("type","1");
				startActivity(it);
			}
		});
	}

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
				RechargeType.BALANCE, null, payMoney);
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
				RechargeType.BALANCE, null, payMoney);
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
					PayTask alipay = new PayTask(PayBalanceConfirmActivity.this);
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
		};
	};

}
