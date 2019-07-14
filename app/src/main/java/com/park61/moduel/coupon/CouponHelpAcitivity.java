package com.park61.moduel.coupon;

import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.park61.BaseActivity;
import com.park61.R;
/**
 * 优惠券使用帮助界面
 *
 */
public class CouponHelpAcitivity extends BaseActivity {
	private WebView wv_help;
	private ProgressBar pb;
	@Override
	public void setLayout() {
		setContentView(R.layout.activity_mycoupon_help);

	}

	@Override
	public void initView() {
		pb = (ProgressBar) findViewById(R.id.pb);
		wv_help = (WebView) findViewById(R.id.wv_help);
		WebSettings webSettings = wv_help.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBlockNetworkImage(false);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置缓存模式
		if (Build.VERSION.SDK_INT >= 21) {
			webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		webSettings.setDomStorageEnabled(true);
		webSettings.setPluginState(WebSettings.PluginState.ON);
		//修改ua让后台识别
		String ua = webSettings.getUserAgentString();
		webSettings.setUserAgentString(ua + "; 61park/android");

		// 设置可以支持缩放
		webSettings.setSupportZoom(true);
		// 设置出现缩放工具
		webSettings.setBuiltInZoomControls(true);
		//扩大比例的缩放
		webSettings.setUseWideViewPort(true);
		//自适应屏幕
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setLoadWithOverviewMode(true);

	}

	@Override
	public void initData() {
		String url = "http://m.61park.cn/coupon/useHelpApp.do";
		wv_help.loadUrl(url);
		wv_help.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					// 网页加载完成
					pb.setVisibility(View.GONE);
				} else {
					// 加载中
					pb.setProgress(newProgress);
				}
			}
		});
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

}
