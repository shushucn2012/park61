package com.park61.moduel.salesafter;

import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.park61.BaseActivity;
import com.park61.R;

/**
 * 售后政策的界面
 */
public class SalesAfterPolicyActivity extends BaseActivity {
	private WebView wv_policy;
	private ProgressBar pb;
	@Override
	public void setLayout() {
		setContentView(R.layout.activity_salesafter_policy);

	}

	@Override
	public void initView() {
		pb = (ProgressBar) findViewById(R.id.pb);
		wv_policy = (WebView) findViewById(R.id.wv_policy);
		WebSettings webSettings = wv_policy.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBlockNetworkImage(false);//为true阻止图片网络数据,false解除数据阻止
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//设置缓存模式(不使用缓存)
		if(Build.VERSION.SDK_INT>21){
			webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		webSettings.setDomStorageEnabled(true);
		webSettings.setPluginState(WebSettings.PluginState.ON);
		//修改ua让后台识别
		String ua = webSettings.getUserAgentString();
		webSettings.setUserAgentString(ua+"; 61park/android");

		// 设置可以支持缩放
		webSettings.setSupportZoom(true);
		// 设置出现缩放工具
		webSettings.setBuiltInZoomControls(true);
		//扩大比例的缩放
		webSettings.setUseWideViewPort(true);
		//自适应屏幕
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		// 缩放至屏幕的大小
		webSettings.setLoadWithOverviewMode(true);
	}

	@Override
	public void initData() {
		String url = "http://m.61park.cn/grf/policy.do";
		wv_policy.loadUrl(url);
		//加载进度
		wv_policy.setWebChromeClient(new WebChromeClient() {
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
