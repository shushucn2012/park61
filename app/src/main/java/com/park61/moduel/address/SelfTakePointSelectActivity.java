package com.park61.moduel.address;

import org.json.JSONObject;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.address.pw.StoreFilterPopWin;
import com.park61.moduel.address.pw.StoreFilterPopWin.OnShopSelectLsner;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

/**
 * 自提选择区域界面
 * @author Lucias
 *
 */
public class SelfTakePointSelectActivity extends BaseActivity implements OnClickListener, OnShopSelectLsner{
	private View county_area;
	private TextView tv_privince,tv_city,tv_county;
	private StoreFilterPopWin mStoreFilterPopWin;
	private View cover;// 背景阴影遮罩	
	private long goodReceiverProvinceId; // 省份id
	private long goodReceiverCityId; // 城市id
	private long goodReceiverCountyId; // 区域id
	private String goodReceiverProvinceName;// 省份名称
	private String goodReceiverCityName;// 城市名称
	private String goodReceiverCountyName;// 区县名称
	private long storeId;//最近店鋪id
	private String selfName;
	private String selfPhone;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_self_take_select);
		
	}

	@Override
	public void initView() {
		county_area = findViewById(R.id.county_area);
		cover = findViewById(R.id.cover);
		tv_privince = (TextView) findViewById(R.id.tv_privince);
		tv_city = (TextView) findViewById(R.id.tv_city);
		tv_county = (TextView) findViewById(R.id.tv_county);
	}

	@Override
	public void initData() {
		goodReceiverProvinceName = getIntent().getStringExtra("goodReceiverProvinceName");
		goodReceiverCityName = getIntent().getStringExtra("goodReceiverCityName");
		goodReceiverCountyName = getIntent().getStringExtra("goodReceiverCountyName");
		goodReceiverProvinceId = getIntent().getLongExtra("goodReceiverProvinceId", goodReceiverProvinceId);
		goodReceiverCityId = getIntent().getLongExtra("goodReceiverCityId", goodReceiverCityId);
		goodReceiverCountyId = getIntent().getLongExtra("goodReceiverCountyId", goodReceiverCountyId);
		storeId = getIntent().getLongExtra("storeId", storeId);
		selfName = getIntent().getStringExtra("selfName");
		selfPhone = getIntent().getStringExtra("selfPhone");
		
		tv_privince.setText(goodReceiverProvinceName);
		tv_city.setText(goodReceiverCityName);
		tv_county.setText(goodReceiverCountyName);
		asyncGetClosestShop();
	}
	

	@Override
	public void initListener() {
		county_area.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.county_area:
			mStoreFilterPopWin = new StoreFilterPopWin(mContext, this,cover,goodReceiverProvinceId,selfName,selfPhone);
			mStoreFilterPopWin.showAsDropDown(v, 0, 0);

			break;

		}
		
	}
	/**
	 * 获取可用店铺列表
	 */
	private void asyncGetClosestShop() {
		String wholeUrl = AppUrl.host + AppUrl.GET_SHOPS_BY_NAME;
		String requestBodyData = ParamBuild.searchShop("", null, 1, 10);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				getClosestShopLsner);
	}

	BaseRequestListener getClosestShopLsner = new JsonRequestListener() {

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
		}
	};

	@Override
	public void onShopSelect(Long id, String name) {
		mStoreFilterPopWin.dismiss();
		asyncGetClosestShop();
	}	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 5 && resultCode == RESULT_OK && data != null) {
			Intent intent = new Intent();
			intent.putExtra("rname", data.getStringExtra("rname"));
			intent.putExtra("rphone", data.getStringExtra("rphone"));
			intent.putExtra("raddr", goodReceiverProvinceName+goodReceiverCityName+data.getStringExtra("raddr"));
			intent.putExtra("rtype", data.getIntExtra("rtype", 1));
			setResult(RESULT_OK, intent);
			finish();
		}
	}


}
