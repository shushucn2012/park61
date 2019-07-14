package com.park61.moduel.address;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.RegexValidator;
import com.park61.moduel.acts.bean.CityBean;
import com.park61.moduel.address.bean.TownBean;
import com.park61.moduel.address.dialog.SelecCityDialog;
import com.park61.moduel.address.dialog.SelecCityDialog.OnCitySelect;
import com.park61.moduel.address.dialog.SelecCountyDialog;
import com.park61.moduel.address.dialog.SelecCountyDialog.OnCountySelect;
import com.park61.moduel.address.dialog.SelecProvinceDialog;
import com.park61.moduel.address.dialog.SelecProvinceDialog.OnProvinceSelect;
import com.park61.moduel.address.dialog.SelecTownDialog;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * 地址管理新建地址信息界面
 * @author Lucia
 *
 */
public class MyAddressManageCreateActivity extends BaseActivity implements
		OnClickListener, OnCountySelect, OnCitySelect, OnProvinceSelect, SelecTownDialog.OnTownSelect {

	private String goodReceiverName;// 收货人姓名
	private String goodReceiverAddress;// 收货人地址
	private String goodReceiverMobile;// 收货人手机号
	private String goodReceiverProvinceName;// 省份
	private String goodReceiverCityName;// 城市
	private String goodReceiverCountyName;// 区域
	private String goodReceiverTownName;//乡镇名

	private EditText name_value, number_value, address_detail;
	private TextView tv_province, tv_city, tv_county,tv_town;
	private ImageView img_chioce;
	private View defauit_address_area;
	private Button btn_commit;

	private Long goodReceiverProvinceId; // 省份id
	private Long goodReceiverCityId; // 城市id
	private Long goodReceiverCountyId; // 区域id
	private Long goodReceiverTownId;//乡镇id

	public static final Long GOODRECEIVERCOUNTRYID = 1L; // 国家id
	public static final Integer GOODRECEIVERTYPE = 0;// 快递类型 传0
	public static final Integer ISDEFAULT = 1; // 是否默认收货地址 0-否 1-是
	private List<CityBean> mList = new ArrayList<>();
	private List<TownBean> townList = new ArrayList<>();
	private String from;// 来源
	private int hasChild; //子节点个数，0代表没有子节点
	@Override
	public void setLayout() {
		setContentView(R.layout.activity_address_manage_add);

	}

	@Override
	public void initView() {
		name_value = (EditText) findViewById(R.id.name_value);
		number_value = (EditText) findViewById(R.id.number_value);
		address_detail = (EditText) findViewById(R.id.address_detail_value);
		tv_province = (TextView) findViewById(R.id.tv_province);
//		tv_province.setVisibility(View.GONE);
		tv_city = (TextView) findViewById(R.id.tv_city);
//		tv_city.setText(GlobalParam.chooseCityStr);
		tv_county = (TextView) findViewById(R.id.tv_county);
		tv_town = (TextView) findViewById(R.id.tv_town);

		img_chioce = (ImageView) findViewById(R.id.img_chioce);
		defauit_address_area = findViewById(R.id.defauit_address_area);
		btn_commit = (Button) findViewById(R.id.btn_commit);

	}

	@Override
	public void initData() {
		from = getIntent().getStringExtra("from");
	}

	@Override
	public void initListener() {
		tv_province.setOnClickListener(this);
		tv_city.setOnClickListener(this);
		tv_county.setOnClickListener(this);
		tv_town.setOnClickListener(this);
		defauit_address_area.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				img_chioce.setBackgroundResource(R.drawable.xuanze_focus);
			}
		});

		btn_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!validateAddress())
					return;
				asyncAddAddress();

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_province:
				asyncGetProvince();

				break;
			case R.id.tv_city:
				if(goodReceiverProvinceName==null){
					showShortToast("请选择省份");
				}else{
					asyncGetCity(goodReceiverProvinceId);
				}
				break;
			case R.id.tv_county:
				if(goodReceiverCityName==null){
					showShortToast("请选择城市");
				}else{
					asyncGetCounty(goodReceiverCityId);
				}

				break;
			case R.id.tv_town:
				if(goodReceiverCountyName==null){
					showShortToast("请选择区域");
				}else{
					asyncGetTown(goodReceiverCountyId);
				}
				break;
			default:
				break;
		}

	}

	protected boolean validateAddress() {
		goodReceiverName = name_value.getText().toString()
				.replace("请输入收货人姓名", "");
		goodReceiverMobile = number_value.getText().toString()
				.replace("请输入手机号码", "");
		goodReceiverAddress = address_detail.getText().toString()
				.replace("请输入您的详细地址", "");
		goodReceiverProvinceName = tv_province.getText().toString()
				.replace("省份", "");
		goodReceiverCityName = tv_city.getText().toString().replace("城市", "");
		goodReceiverCountyName = tv_county.getText().toString()
				.replace("区域", "");
		goodReceiverTownName = tv_town.getText().toString().replace("乡镇", "");
		if (TextUtils.isEmpty(goodReceiverName)) {
			showShortToast("请输入收货人姓名");
			return false;
		}
		if(goodReceiverName.length()<2){
			showShortToast("姓名不能小于2个字符!");
			return false;
		}
		if (TextUtils.isEmpty(goodReceiverMobile)) {
			showShortToast("请输入手机号码");
			return false;
		}
		if (!RegexValidator.isMobile(goodReceiverMobile)) {
			showShortToast("手机号输入有误！");
			return false;
		}
		 if (TextUtils.isEmpty(goodReceiverProvinceName)) {
			 showShortToast("请选择省份");
			 return false;
		 }
		if (TextUtils.isEmpty(goodReceiverCityName)) {
			showShortToast("请选择省份对应的城市");
			return false;
		}
		if (goodReceiverCountyId == null) {
			showShortToast("请选择城市对应的区域");
			return false;
		}
		if(hasChild!=0&&TextUtils.isEmpty(goodReceiverTownName)){
			showShortToast("请选择区域对应的乡镇");
			return false;
		}
		if (TextUtils.isEmpty(goodReceiverAddress)) {
			showShortToast("请输入详细地址");
			return false;
		}

		return true;
	}

	/**
	 * 通过城市id获取对应区域名
	 */
	private void asyncGetCounty(Long goodReceiverCityId) {
		String wholeUrl = AppUrl.host + AppUrl.GET_ADDR_BY_CITYID;
		String requestBodyData = ParamBuild
				.getGoodReceiverCityId(goodReceiverCityId);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				countyListener);
	}

	BaseRequestListener countyListener = new JsonRequestListener() {

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
			JSONObject parentJob = jsonResult.optJSONObject("area");
			goodReceiverProvinceId = parentJob
					.optLong("goodReceiverProvinceId");
			goodReceiverCityId = parentJob.optLong("goodReceiverCityId");
			JSONArray jay = jsonResult.optJSONArray("counties");
			mList.clear();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject jot = jay.optJSONObject(i);
				CityBean county = gson.fromJson(jot.toString(), CityBean.class);
				mList.add(county);
			}
			SelecCountyDialog dialog = new SelecCountyDialog(mContext, mList);
			dialog.showDialog();
			dialog.setOnCountySelectLsner(MyAddressManageCreateActivity.this);// 选择区域点击事件
		}
	};
	/**
	 * 通过区域id获取对应乡镇名
	 */
	private void asyncGetTown(Long goodReceiverCountyId) {
		String wholeUrl = AppUrl.host + AppUrl.ADDRESS_GET_TOWNS;
		String requestBodyData = ParamBuild
				.goodReceiverCountyId(goodReceiverCountyId);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				townListener);
	}

	BaseRequestListener townListener = new JsonRequestListener() {

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
			JSONArray jay = jsonResult.optJSONArray("towns");
			townList.clear();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject jot = jay.optJSONObject(i);
				TownBean town = gson.fromJson(jot.toString(), TownBean.class);
				townList.add(town);
			}
			SelecTownDialog dialog = new SelecTownDialog(mContext, townList);
			dialog.showDialog();
			dialog.setOnTownSelectLsner(MyAddressManageCreateActivity.this);// 选择乡镇点击事件
		}
	};


	/**
	 * 通过省份id获取对应城市名
	 */
	private void asyncGetCity(Long goodReceiverProvinceId) {
		String wholeUrl = AppUrl.host + AppUrl.GET_CITY_BY_PROVINCE;
		String requestBodyData = ParamBuild.getPId(goodReceiverProvinceId);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				cityListener);
	}

	BaseRequestListener cityListener = new JsonRequestListener() {

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
			JSONArray jay = jsonResult.optJSONArray("list");
			mList.clear();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject jot = jay.optJSONObject(i);
				CityBean city = gson.fromJson(jot.toString(), CityBean.class);
				mList.add(city);
			}
			SelecCityDialog dialog = new SelecCityDialog(mContext, mList);
			dialog.showDialog();
			dialog.setOnCitySelectLsner(MyAddressManageCreateActivity.this);// 选择城市点击事件
		}
	};

	/**
	 * 获取省份名
	 */
	private void asyncGetProvince() {
		String wholeUrl = AppUrl.host + AppUrl.GET_PROVINCE_LIST;
		String requestBodyData = "";
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				provinceListener);
	}

	BaseRequestListener provinceListener = new JsonRequestListener() {

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
			JSONArray jay = jsonResult.optJSONArray("list");
			mList.clear();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject jot = jay.optJSONObject(i);
				CityBean p = gson.fromJson(jot.toString(), CityBean.class);
				mList.add(p);
			}
			SelecProvinceDialog dialog = new SelecProvinceDialog(mContext,
					mList);
			dialog.showDialog();
			dialog.setOnProvinceSelectLsner(MyAddressManageCreateActivity.this);// 选择省份点击事件

		}
	};

	/**
	 * 添加地址
	 */
	private void asyncAddAddress() {
		String wholeUrl = AppUrl.host + AppUrl.ADD_ADDR;
//		String requestBodyData = ParamBuild.addAddress(goodReceiverName,
//				goodReceiverAddress, goodReceiverMobile,
//				goodReceiverProvinceName, goodReceiverCityName,
//				goodReceiverCountyName, GOODRECEIVERTYPE,
//				GOODRECEIVERCOUNTRYID, goodReceiverProvinceId,
//				goodReceiverCityId, goodReceiverCountyId, ISDEFAULT);
		String requestBodyData = ParamBuild.addAddress(from,goodReceiverName,
				goodReceiverAddress, goodReceiverMobile,
				goodReceiverProvinceName, goodReceiverCityName,
				goodReceiverCountyName,goodReceiverTownName, GOODRECEIVERTYPE,
				GOODRECEIVERCOUNTRYID, goodReceiverProvinceId,
				goodReceiverCityId, goodReceiverCountyId,goodReceiverTownId, ISDEFAULT);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				listener);
	}

	BaseRequestListener listener = new JsonRequestListener() {

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
			showShortToast("地址添加成功！");
			setResult(RESULT_OK);
			finish();
		}
	};

	// 选择省份名称
	@Override
	public void onSelectProvince(int position) {
		tv_province.setText(mList.get(position).getProvinceName());
		goodReceiverProvinceId = mList.get(position).getId(); // 获取省份ID
		tv_city.setText("");
		tv_county.setText("");
		tv_town.setText("");
		address_detail.setText("");
		asyncGetCity(goodReceiverProvinceId);
	}

	// 选择城市名称
	@Override
	public void onSelectCity(int position) {
		tv_city.setText(mList.get(position).getCityName());
		goodReceiverCityId = mList.get(position).getId(); // 获取城市ID
		tv_county.setText("");
		tv_town.setText("");
		address_detail.setText("");
		asyncGetCounty(goodReceiverCityId);
	}

	// 选择区域名称
	@Override
	public void onSelectCounty(int position) {
		tv_county.setText(mList.get(position).getCountyName());
		goodReceiverCountyId = mList.get(position).getId(); // 获取区域ID
		hasChild = mList.get(position).getHasChild();
		tv_town.setText("");
		address_detail.setText("");
		if(hasChild!=0){
			asyncGetTown(goodReceiverCountyId);
			tv_town.setVisibility(View.VISIBLE);
		}else{
			tv_town.setVisibility(View.GONE);
		}
	}

	@Override
	public void onTownSelect(int position) {
		tv_town.setText(townList.get(position).getTownName());
		goodReceiverTownId = townList.get(position).getId();
		address_detail.setText("");
	}
}
