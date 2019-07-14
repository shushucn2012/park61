package com.park61.moduel.me;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;

public class CustomerServiceActivity extends BaseActivity {

	private TextView tv_cs_phone;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_customer_service);
	}

	@Override
	public void initView() {
		tv_cs_phone = (TextView) findViewById(R.id.tv_cs_phone);
	}

	@Override
	public void initData() {

	}

	@Override
	public void initListener() {
		tv_cs_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dDialog.showDialog("提醒", "确定需要拨打此号码吗？", "取消", "确定", null,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:02765524760"));
								startActivity(intent);
								dDialog.dismissDialog();
							}
						});
			}
		});
	}

}
