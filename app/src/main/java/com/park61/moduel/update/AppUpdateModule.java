package com.park61.moduel.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.InstallUtils;
import com.park61.net.base.Request.Method;
import com.park61.net.request.StringNetRequest;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.AlipayWaitProgressDialog;
import com.park61.widget.dialog.DoubleDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class AppUpdateModule {

	/**
	 * 程序上下文
	 */
	private Context context;
	/**
	 * 升级更新地址
	 */
	private String upgradeUrl;
	/**
	 * 是否强制更新
	 */
	private boolean isForce = false;
	/**
	 * 更新日志
	 */
	private String changeLog;
	/**
	 * 升级版本号
	 */
	private int versionCode = 0;
	/**
	 * 安装更新提示语
	 */
	private String commonDialogTextInstall;
	private static final int DEFAULT_FILE_LENGTH = 3500000; // ~3.5M
	private static final int NETWORK_ERR = 1;
	private static final int DOWNLOAD_ERR = 2;
	private static final int DOWNLOAD_OK = 3;
	private static final int DOWNLOADING = 4;
	private static final String Tag = "AppUpdateModule";

	private DoubleDialog doubleDialog;
	private AlipayWaitProgressDialog aPDialog;
	private OnCheckedUpdate mOnCheckedUpdate;

	public AppUpdateModule() {

	}

	public AppUpdateModule(Context mContext) {
		this.context = mContext;
		doubleDialog = new DoubleDialog(mContext);
		aPDialog = new AlipayWaitProgressDialog(mContext);
		// mOnCheckedUpdate = (LoadingActivity) mContext;
	}

	/**
	 * 请求更新
	 */
	public void requestUpdate() {
		StringNetRequest netRequest = StringNetRequest.getInstance(context);
		netRequest.setMainContext(context);
		if (TextUtils.isEmpty(upgradeUrl)) {
			String wholeUrl = AppUrl.host + AppUrl.CHECK_VERSION_END;
			String requestBodyData = ParamBuild.checkVersion(
					GlobalParam.versionName, 0);
			netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
					listener);
		} else {
			if (isForce) {
				checkIsNeedForceUpdate();
			}
		}
	}

	BaseRequestListener listener = new JsonRequestListener() {

		@Override
		public void onStart(int requestId) {
			// ((BaseActivity) context).showDialog();
		}

		@Override
		public void onError(int requestId, String errorCode, String errorMsg) {
			// ((BaseActivity) context).dismissDialog();
			// ((BaseActivity) context).showTipDialog(errorMsg);
			mOnCheckedUpdate.OnError();
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			boolean isUpgrade = jsonResult.optBoolean("isNeedUpdate", false);
			upgradeUrl = jsonResult.optString("upgradeUrl");
			if (!isUpgrade || TextUtils.isEmpty(upgradeUrl)) {
				mOnCheckedUpdate.OnNoUpdate();
				return;
			}
			isForce = jsonResult.optInt("isForce") == 1 ? true : false;
			doubleDialog.setCancelable(!isForce);
			changeLog = jsonResult.optString("changeLog");
			versionCode = jsonResult.optInt("versionCode");
			checkIsNeedForceUpdate();
		}
	};

	/**
	 * 检查是否强制更新
	 */
	private void checkIsNeedForceUpdate() {
		// app 更新已经下载
		Log.e("getApkFilePath(context, code)",
				InstallUtils.getApkFilePath(context, versionCode));
		if (InstallUtils.isApkExist(context, versionCode)) {
			Log.e("是否存在", "存在着");
			if (isForce) {
				commonDialogTextInstall = "更新包已下载，必须安装才能使用\n" + changeLog;
			} else {
				commonDialogTextInstall = "更新包已下载，请安装\n" + changeLog;
			}
			doubleDialog.showDialog("提示", commonDialogTextInstall, "确定", "取消",
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							doubleDialog.dismissDialog();
							startInstallAPK();

						}
					}, new OnClickListener() {

						@Override
						public void onClick(View v) {
							doubleDialog.dismissDialog();
							if (isForce) {
								((Activity) context).finish();
							} else {
								mOnCheckedUpdate.OnNoUpdate();
							}
						}
					});

		} else { // app 更新第一次下载
			if (isForce) {
				commonDialogTextInstall = "发现新版本，必须更新才能使用\n" + changeLog;

			} else {
				commonDialogTextInstall = "发现新版本，请更新\n" + changeLog;
			}

			doubleDialog.showDialog("提示", commonDialogTextInstall, "确定", "取消",
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							doubleDialog.dismissDialog();
							if (isForce) {
								startAppDownLoad();
							} else {
								startAppDownLoad();
								// Intent intent = new Intent(context,
								// UpdateLoadService.class);
								// intent.putExtra("upgradeUrl", upgradeUrl);
								// intent.putExtra("versionCode", versionCode);
								// context.startService(intent);
							}
						}
					}, new OnClickListener() {

						@Override
						public void onClick(View v) {
							doubleDialog.dismissDialog();
							if (isForce) {
								((Activity) context).finish();
							} else {
								mOnCheckedUpdate.OnNoUpdate();
							}
						}
					});

		}
	}

	@SuppressLint("HandlerLeak")
	private Handler appHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case NETWORK_ERR:
				dismissDialog();
				InstallUtils.deleteUpdateFile(context, versionCode);
				showShortToast("系统繁忙，请稍后再试!");
				break;
			case DOWNLOAD_ERR:
				dismissDialog();
				InstallUtils.deleteUpdateFile(context, versionCode);
				showShortToast("下载失败，请稍后再试!");
				break;
			case DOWNLOADING:
				if (null != msg.obj) {
					showDialog("正在下载更新包，已完成" + msg.obj + "%,请稍后...");
				}
				break;
			case DOWNLOAD_OK:
				dismissDialog();
				startInstallAPK();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 开始安装apk
	 */
	private void startInstallAPK() {
		String[] args2 = { "chmod", "604",
				InstallUtils.getApkFilePath(context, versionCode) };
		InstallUtils.exec(args2);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(
				Uri.parse("file://"
						+ InstallUtils.getApkFilePath(context, versionCode)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 开始下载更新包
	 */
	private void startAppDownLoad() {
		showDialog("正在下载更新包，已完成0%,请稍候...");
		aPDialog.setCancelable(false);
		new AppDownLoadThread().start();
	}

	private class AppDownLoadThread extends Thread {
		@Override
		public void run() {
			super.run();
			try { // 下载安装
				appDownload();
			} catch (IOException e) {
				e.printStackTrace();
				appHandler.sendEmptyMessage(DOWNLOAD_ERR);
			}
		}
	}

	private void appDownload() throws IOException {
		URL url = new URL(upgradeUrl);// UpdateDownloader.mUrl);
		// URL url = new URL("http://192.168.8.115/Dovila3nd_test.apk");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(10 * 1000);
		conn.setRequestProperty("User-agent", "Mozilla/4.0");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type",
				"application/vnd.android.package-archive");
		// get file size and response code
		int total = conn.getContentLength();
		if (total == -1) { // failed
			total = DEFAULT_FILE_LENGTH;
		}
		int code = 0;
		try {
			code = conn.getResponseCode();
		} catch (Exception e) {
			code = 0;
		}
		Log.e(Tag, "ResponseCode:" + code + ", size:" + total);

		if (code != 200) {
			appHandler.sendEmptyMessage(NETWORK_ERR);
		} else {
			InputStream fin = conn.getInputStream();
			String apkPath = InstallUtils.getApkFilePath(context, versionCode);
			File file = new File(apkPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			try {
				int len = -1;
				int init = 0;
				int lastPercent = 0;
				byte buffer[] = new byte[64];

				while ((len = fin.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					init += len;
					int pct = (int) ((float) init / (float) total * 100);

					if (pct - lastPercent >= 1) {
						Message msg = appHandler.obtainMessage();
						msg.what = DOWNLOADING;
						msg.obj = pct + "";
						appHandler.sendMessage(msg);
						lastPercent = pct;
					}
					Log.e(Tag, "PCT: ====== " + pct + "     lastPercent: "
							+ lastPercent);
				}
				fos.flush();
				appHandler.sendEmptyMessage(DOWNLOAD_OK);
			} catch (FileNotFoundException e) {
				throw new FileNotFoundException();
			} finally {
				try {
					fin.close();
					fos.close();
				} catch (IOException e) {
					throw new IOException();
				}
			}
		}
	}

	public void showDialog() {
		try {
			if (aPDialog.isShow()) {
				return;
			} else {
				aPDialog.showDialog(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showDialog(String msg) {
		try {
			if (aPDialog.isShow()) {
				aPDialog.setMessage(msg);
			} else {
				aPDialog.showDialog(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dismissDialog() {
		try {
			if (aPDialog.isShow()) {
				aPDialog.dialogDismiss();
			} else {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * toast短提示
	 */
	public void showShortToast(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 检测更新回调
	 * 
	 * @author shushucn2012
	 */
	public interface OnCheckedUpdate {
		public void OnNoUpdate();

		public void OnError();
	}

}
