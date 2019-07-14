package com.park61.moduel.me;

import org.json.JSONObject;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.CPUpdateDownloadCallback;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.DataCleanManager;
import com.park61.moduel.accountsafe.AccountSafeActivity;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.AlipayWaitProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends BaseActivity implements OnClickListener {

	private Button btn_logout;
	private View account_area, suggest_area, cache_area, about_area, version_area;
	private TextView tv_version_name;
	private AlipayWaitProgressDialog aPDialog;// 下载更新包进度框

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_setting);
	}

	@Override
	public void initView() {
		btn_logout = (Button) findViewById(R.id.btn_logout);
		account_area = findViewById(R.id.account_area);
		suggest_area = findViewById(R.id.suggest_area);
		cache_area = findViewById(R.id.cache_area);
		about_area = findViewById(R.id.about_area);
		version_area = findViewById(R.id.version_area);
		tv_version_name = (TextView) findViewById(R.id.tv_version_name);
		aPDialog = new AlipayWaitProgressDialog(this);
	}

	@Override
	public void initData() {
		if (!GlobalParam.versionNameNext.equals("")) {
			tv_version_name.setText("可更新至" + GlobalParam.versionNameNext + "版本");
			tv_version_name.setTextColor(mContext.getResources().getColor(R.color.com_orange));
			findViewById(R.id.img_version_item_arrow).setVisibility(View.VISIBLE);
			version_area.setOnClickListener(this);
		}
	}

	@Override
	public void initListener() {
		btn_logout.setOnClickListener(this);
		account_area.setOnClickListener(this);
		suggest_area.setOnClickListener(this);
		cache_area.setOnClickListener(this);
		about_area.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_logout:
			showLoginOutDialog();
			break;
		case R.id.account_area:
//			if (GlobalParam.LOGIN_TYPE == 1)// 联合登陆不能修改密码
//				return;
			startActivity(new Intent(mContext, AccountSafeActivity.class));
			break;
		case R.id.suggest_area:
//			startActivity(new Intent(mContext, SuggestionFeedbackActivity.class));
			break;
		case R.id.cache_area:
			showDialog();
			DataCleanManager.cleanExternalCache(mContext);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					dismissDialog();
					showShortToast("已清除！");
				}
			}, 1500);
			break;
		case R.id.about_area:
			startActivity(new Intent(mContext, AboutActivity.class));
			break;
		case R.id.version_area:
			showDialog();
			BDAutoUpdateSDK.cpUpdateCheck(this, new MyCPCheckUpdateCallback());
			break;
		}
	}

	/**
	 * 检测更新监听
	 */
	private class MyCPCheckUpdateCallback implements CPCheckUpdateCallback {

		@Override
		public void onCheckUpdateCallback(final AppUpdateInfo info, AppUpdateInfoForInstall infoForInstall) {
			dismissDialog();
			if (info != null) {
				String changeLog = info.getAppChangeLog();
				GlobalParam.versionNameNext = info.getAppVersionName();
				// 如果更新log里说明是强制更新，那么必须更新
				final boolean isForce = changeLog.contains("强制更新") ? true : false;
				String installStr = "";
				if (isForce) {
					installStr = "发现新版本，必须更新才能使用\n" + changeLog;
				} else {
					installStr = "发现新版本，请更新\n" + changeLog;
				}
				dDialog.showDialog("提示", installStr, "确定", "取消",
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								dDialog.dismissDialog();
								BDAutoUpdateSDK.cpUpdateDownload(mContext, info, new UpdateDownloadCallback());
							}
						}, null);
			}
		}
	}

	/**
	 * 更新进度监听
	 */
	private class UpdateDownloadCallback implements CPUpdateDownloadCallback {

		@Override
		public void onDownloadComplete(String apkPath) {
			aPDialog.dialogDismiss();
			BDAutoUpdateSDK.cpUpdateInstall(getApplicationContext(), apkPath);
		}

		@Override
		public void onStart() {
			aPDialog.setCancelable(false);
			aPDialog.showDialog("正在下载更新包，已完成0%,请稍候...");
		}

		@Override
		public void onPercent(int percent, long rcvLen, long fileSize) {
			aPDialog.showDialog("正在下载更新包，已完成" + percent + "%,请稍后...");
		}

		@Override
		public void onFail(Throwable error, String content) {
			Toast.makeText(mContext, "下载失败！", Toast.LENGTH_SHORT).show();
			aPDialog.dialogDismiss();
		}

		@Override
		public void onStop() {
			aPDialog.dialogDismiss();
		}
	}

	public void showLoginOutDialog() {
		dDialog.showDialog("提示", "确定要退出吗？", "取消", "确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dDialog.dismissDialog();

			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				dDialog.dismissDialog();
				asyncLoginOut();
			}
		});
	}

	/**
	 * 退出登录
	 */
	private void asyncLoginOut() {
		String wholeUrl = AppUrl.host + AppUrl.LOGIN_OUT;
		String requestBodyData = "";
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, loginOutLsner);
	}

	BaseRequestListener loginOutLsner = new JsonRequestListener() {

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
			showShortToast("已退出，请重新登录！");
			GlobalParam.userToken = null;
			GlobalParam.GrowingMainNeedRefresh = true;
			GlobalParam.CUR_SHOP_ID = 0;
			GlobalParam.CUR_SHOP_NAME = "";
			// ---清除用户登录信息缓存---start---//
			SharedPreferences spf = getSharedPreferences(
					LoginV2Activity.FILE_NAME, Context.MODE_PRIVATE);
			Editor editor = spf.edit();
			editor.putString("logindate", "");
			editor.putString("usertoken", "");
			editor.commit();
			// ---清除用户登录信息缓存---end---//
			startActivity(new Intent(mContext, LoginV2Activity.class));
			Intent changeIt = new Intent();
			changeIt.setAction("ACTION_TAB_CHANGE");
			changeIt.putExtra("TAB_NAME", "tab_main");
			sendBroadcast(changeIt);
			finish();
		}
	};

}
