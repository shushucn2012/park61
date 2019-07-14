package com.park61.moduel.address;



import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.park61.BaseActivity;
import com.park61.R;

public class CreateSelfTakeInfoActivity extends BaseActivity {
	private EditText name_info,phone_info;
	private Button btn_save;
	private String rname,rphone;
	@Override
	public void setLayout() {
		setContentView(R.layout.activity_address_selftake_createinfo);

	}

	@Override
	public void initView() {
		name_info = (EditText) findViewById(R.id.name_info);
		phone_info = (EditText) findViewById(R.id.phone_info);
		btn_save = (Button) findViewById(R.id.btn_save);
	}

	@Override
	public void initData() {		

	}

	@Override
	public void initListener() {
		btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rname = name_info.getText().toString().trim();
				rphone = phone_info.getText().toString().trim();
				if (!validateData()) {
					return ;
				}				
				Intent returnData = new Intent();
				returnData.putExtra("rname", rname);
				returnData.putExtra("rphone",rphone);
				setResult(RESULT_OK, returnData);
				finish();
			}
		});

	}
	protected boolean validateData() {
		if (TextUtils.isEmpty(rname)) {
			showShortToast("姓名不能为空！");
			return false;
		}
		if (TextUtils.isEmpty(rphone)) {
			showShortToast("电话号码不能为空！");
			return false;
		}
		return true;
	}

}
