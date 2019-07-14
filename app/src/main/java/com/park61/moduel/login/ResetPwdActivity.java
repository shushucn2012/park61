package com.park61.moduel.login;

import org.json.JSONObject;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.RegexValidator;
import com.park61.common.tool.ViewInitTool;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.textview.AccoutSafeTimeTextView;

import java.util.ArrayList;
import java.util.List;

public class ResetPwdActivity extends BaseActivity {

	private EditText edit_userhphone;
	private EditText edit_checkcode;
	private Button btn_get_vcode;
	private EditText edit_input_pwd;
	private Button btn_reset;
	private String userphone, userpwd, vcode;
	private AccoutSafeTimeTextView ttv_time;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_resetpwd);
	}

	@Override
	public void initView() {
		edit_userhphone = (EditText) findViewById(R.id.edit_userhphone);
		edit_checkcode = (EditText) findViewById(R.id.edit_checkcode);
		btn_get_vcode = (Button) findViewById(R.id.btn_get_vcode);
		edit_input_pwd = (EditText) findViewById(R.id.edit_input_pwd);
		btn_reset = (Button) findViewById(R.id.btn_reset);
		ttv_time = (AccoutSafeTimeTextView) findViewById(R.id.ttv_time);
	}

	@Override
	public void initData() {
		if (GlobalParam.currentUser != null) {
			edit_userhphone.setText(GlobalParam.currentUser.getMobile());
			// edit_userhphone.setEnabled(false);
		}
	}

	@Override
	public void initListener() {
		btn_get_vcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!validateGetRegVcode())
					return;
				asyncGetResetVcode();
			}
		});
		btn_reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!validateReg())
					return;
				asyncReset();
			}
		});
		List<EditText> etList = new ArrayList<>();
		etList.add(edit_userhphone);
		etList.add(edit_checkcode);
		etList.add(edit_input_pwd);
		ViewInitTool.addJudgeBtnEnableListener(etList, btn_reset);
	}

	protected boolean validateGetRegVcode() {
		userphone = edit_userhphone.getText().toString();
		if (TextUtils.isEmpty(userphone)) {
			showShortToast("手机号没有填写！");
			return false;
		}
		if (!RegexValidator.isMobile(userphone)) {
			showShortToast("手机号输入有误！");
			return false;
		}
		return true;
	}

	protected boolean validateReg() {
		userphone = edit_userhphone.getText().toString();
		userpwd = edit_input_pwd.getText().toString();
		vcode = edit_checkcode.getText().toString();
		if (TextUtils.isEmpty(userphone)) {
			showShortToast("手机号没有填写！");
			return false;
		}
		if (!RegexValidator.isMobile(userphone)) {
			showShortToast("手机号输入有误！");
			return false;
		}
		if (TextUtils.isEmpty(userpwd)) {
			showShortToast("登录密码没有填写！");
			return false;
		}
		if (TextUtils.isEmpty(vcode)) {
			showShortToast("验证码没有填写！");
			return false;
		}
		if (!RegexValidator.isPassword(userpwd)) {
			showShortToast("密码不符合规则！请设置6到20位，由数字和字母组成密码！");
			return false;
		}
		return true;
	}

	/**
	 * 开始计时
	 */
	public void reStartMyTimer() {
		if (!ttv_time.isRun()) {
			btn_get_vcode.setVisibility(View.GONE);
			ttv_time.setVisibility(View.VISIBLE);
			ttv_time.setTimes(new long[] { 0, 0, 0, 61 });
			ttv_time.run();
			ttv_time.setOnTimeEndLsner(new AccoutSafeTimeTextView.OnTimeEnd() {
				@Override
				public void onEnd() {
					btn_get_vcode.setVisibility(View.VISIBLE);
					ttv_time.setVisibility(View.GONE);
				}
			});
		}
	}

	/**
	 * 发送重置密码验证码
	 */
	protected void asyncGetResetVcode() {
		String wholeUrl = AppUrl.host + AppUrl.SEND_RESET_VCODE;
		String requestBodyData = ParamBuild.getRegVcode(userphone);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				getRegVcodeLsner);
	}

	BaseRequestListener getRegVcodeLsner = new JsonRequestListener() {

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
	 * 重置
	 */
	private void asyncReset() {
		String wholeUrl = AppUrl.host + AppUrl.RESET;
		String requestBodyData = ParamBuild.reset(userphone, userpwd, vcode);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				resetLsner);
	}

	BaseRequestListener resetLsner = new JsonRequestListener() {

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
			showShortToast("密码重置成功！");
			finish();
		}
	};

}
