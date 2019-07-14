package com.park61.moduel.login;

import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.GlobalParam;

public class AliLoginAcitivity extends BaseActivity {

	public static final String loginUrl = "http://m.61park.cn/unionlogin/alipay";

	private WebView wv_ali_login;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_ali_login);
	}

	@Override
	public void initView() {
		wv_ali_login = (WebView) findViewById(R.id.wv_ali_login);
		// 如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件
		wv_ali_login.requestFocus();
		wv_ali_login.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		wv_ali_login.getSettings().setJavaScriptEnabled(true);
		wv_ali_login.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				String script = "function getCookie(name){"
						+ "var strCookie=document.cookie;"
						+ "var arrCookie=strCookie.split(\"; \");"
						+ "for(var i=0;i<arrCookie.length;i++){"
						+ "var arr=arrCookie[i].split(\"=\");"
						+ "if(arr[0]==name)" + "return arr[1];}"
						+ "return \"\";}" + "var result=getCookie(\'ut\');"
						+ "if(result!=null&&result!=\"\")" + "alert(result);";
				wv_ali_login.loadUrl("javascript:" + script);
			}
		});
		// 此方法可以处理webview 在加载时和加载完成时一些操作
		wv_ali_login.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				wv_ali_login.stopLoading();
				GlobalParam.userToken = message;
				setResult(RESULT_OK);
				finish();
				return true;
			}
		});
		wv_ali_login.loadUrl(loginUrl);
	}

	@Override
	public void initData() {

	}

	@Override
	public void initListener() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		wv_ali_login.destroy();
	}

}
