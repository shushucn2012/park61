package com.park61.moduel.me;

import org.json.JSONObject;

import android.content.Intent;
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
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.login.bean.UserManager;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.textview.AccoutSafeTimeTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 绑到手机号
 * 
 * @author shushucn2012
 * 
 */
public class BindPhoneActivity extends BaseActivity {

	private EditText edit_userhphone;
	private EditText edit_checkcode;
	private Button btn_get_vcode;
	private Button btn_bind;
	private String userphone, vcode;
	private AccoutSafeTimeTextView ttv_time;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_bindphone);
	}

	@Override
	public void initView() {
		edit_userhphone = (EditText) findViewById(R.id.edit_userhphone);
		edit_checkcode = (EditText) findViewById(R.id.edit_checkcode);
		btn_get_vcode = (Button) findViewById(R.id.btn_get_vcode);
		btn_bind = (Button) findViewById(R.id.btn_bind);
		ttv_time = (AccoutSafeTimeTextView) findViewById(R.id.ttv_time);
	}

	@Override
	public void initData() {

	}

	@Override
	public void initListener() {
		btn_get_vcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!validateGetBindVcode())
					return;
				asyncGetBindVcode();
			}
		});
		btn_bind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!validateBind())
					return;
				asyncBind();
			}
		});
		List<EditText> etList = new ArrayList<>();
		etList.add(edit_userhphone);
		etList.add(edit_checkcode);
		ViewInitTool.addJudgeBtnEnableListener(etList, btn_bind);
	}

	protected boolean validateGetBindVcode() {
		userphone = edit_userhphone.getText().toString();
		if (TextUtils.isEmpty(userphone)) {
			showShortToast("手机号没有填写！");
			return false;
		}
		return true;
	}

	protected boolean validateBind() {
		userphone = edit_userhphone.getText().toString();
		vcode = edit_checkcode.getText().toString();
		if (TextUtils.isEmpty(userphone)) {
			showShortToast("手机号没有填写！");
			return false;
		}
		if (TextUtils.isEmpty(vcode)) {
			showShortToast("验证码没有填写！");
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
	 * 发送绑到手机号验证码
	 */
	protected void asyncGetBindVcode() {
		String wholeUrl = AppUrl.host + AppUrl.SEND_BIND_VCODE;
		String requestBodyData = ParamBuild.getRegVcode(userphone);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				getBindVcodeLsner);
	}

	BaseRequestListener getBindVcodeLsner = new JsonRequestListener() {

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
	 * 绑定
	 */
	private void asyncBind() {
		String wholeUrl = AppUrl.host + AppUrl.BIND;
		String requestBodyData = ParamBuild.bind(userphone, vcode);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				bindLsner);
	}

	BaseRequestListener bindLsner = new JsonRequestListener() {

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
			showShortToast("绑定成功！");
			GlobalParam.currentUser.setMobile(userphone);
			UserManager.getInstance().setAccountInfo(GlobalParam.currentUser,mContext);
			setResult(RESULT_OK, new Intent().putExtra("new_data", userphone));
			finish();
		}
	};

}
