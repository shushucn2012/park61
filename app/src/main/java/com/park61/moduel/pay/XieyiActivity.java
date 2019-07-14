package com.park61.moduel.pay;

import android.webkit.WebView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;

public class XieyiActivity extends BaseActivity {

	private WebView wv_xieyi;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_xieyi);
	}

	@Override
	public void initView() {
		wv_xieyi = (WebView) findViewById(R.id.wv_xieyi);
	}

	@Override
	public void initData() {
		((TextView)findViewById(R.id.tv_page_title)).setText(getIntent().getStringExtra("title"));
		String url = AppUrl.host+"/getRechargeAgreement.do?type="+getIntent().getStringExtra("type");
		wv_xieyi.loadUrl(url);
	}

	@Override
	public void initListener() {

	}

}
