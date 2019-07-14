package com.park61.moduel.address;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.moduel.address.adapter.SelectStoreListAdapter;
import com.park61.moduel.address.bean.AddressDetailItem;
import com.park61.moduel.address.bean.StoreVO;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 自提选择店铺信息列表的界面
 * @author Lucia
 * 
 */
public class SelectCollectStoreActivity extends BaseActivity implements
		OnClickListener {
	private TextView selected_tv, reselection_tv, name_tv, phone_tv,
			contact_tv,tv,tv2;
	private ListView select_store_listview;
	private View contact_area;
	private Button confirm_btn;
	private SelectStoreListAdapter adapter;
	private List<StoreVO> storeList;
	private List<AddressDetailItem> mList;
	private long goodReceiverCityId; // 城市id
	private long goodReceiverCountyId;
	private long goodReceiverProvinceId; // 省份id
	private String goodReceiverCountyName;
	private AddressDetailItem item;
	private String goodReceiverAddress;
	private String storeName;//店铺名称
	private String phone;//店铺电话
	private String rname;
	private String rphone;
	private long storeId;
	private static final Integer GOODRECEIVERTYPE = 1;// 快递类型0快递1自提
	public static final Long GOODRECEIVERCOUNTRYID = 1L; // 国家id
	public static final Integer ISDEFAULT = 1; // 是否默认收货地址 0-否 1-是
	private String selfName;
	private String selfPhone;
	private int selectPos;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_select_store_collection);

	}

	@Override
	public void initView() {
		selected_tv = (TextView) findViewById(R.id.selected_tv);
		reselection_tv = (TextView) findViewById(R.id.reselection_tv);
		select_store_listview = (ListView) findViewById(R.id.select_store_listview);
		contact_tv = (TextView) findViewById(R.id.contact_tv);
		tv = (TextView) findViewById(R.id.tv);
		tv2 = (TextView) findViewById(R.id.tv2);
		name_tv = (TextView) findViewById(R.id.name_tv);
		phone_tv = (TextView) findViewById(R.id.phone_tv);
		contact_area = findViewById(R.id.contact_area);
		confirm_btn = (Button) findViewById(R.id.confirm_btn);

	}

	@Override
	public void initData() {
		selectPos = getIntent().getIntExtra("selectPos", selectPos);
		selfName = getIntent().getStringExtra("selfName");
		selfPhone = getIntent().getStringExtra("selfPhone");
		name_tv.setText(selfName);
		phone_tv.setText(selfPhone);

		rname = name_tv.getText().toString().trim();
		rphone = phone_tv.getText().toString().trim();

		if (TextUtils.isEmpty(rname) && TextUtils.isEmpty(rphone)) {
			name_tv.setVisibility(View.GONE);
			phone_tv.setVisibility(View.GONE);
			tv.setVisibility(View.GONE);
			tv2.setVisibility(View.GONE);
			contact_tv.setVisibility(View.VISIBLE);
			contact_tv.setText("没有联系人信息，请新增联系人信息");
		}
		mList = new ArrayList<AddressDetailItem>();
		goodReceiverCityId = Long.parseLong(GlobalParam.chooseCityCode);
		storeList = new ArrayList<StoreVO>();
		adapter = new SelectStoreListAdapter(selectPos,storeList, mContext);
		select_store_listview.setAdapter(adapter);
		goodReceiverProvinceId = getIntent().getLongExtra(
				"goodReceiverProvinceId", goodReceiverProvinceId);
		goodReceiverCountyId = getIntent().getLongExtra("goodReceiverCountyId",
				goodReceiverCountyId);
		goodReceiverCountyName = getIntent().getStringExtra(
				"goodReceiverCountyName");
		selected_tv
				.setText("已筛选 " + "'" + goodReceiverCountyName + "'" + "的店铺");

		asyncGetStoresByCountyId(goodReceiverCountyId);
	}

	@Override
	public void initListener() {
		select_store_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.selectItem(position);
			}

		});
		contact_area.setOnClickListener(this);
		reselection_tv.setOnClickListener(this);
		confirm_btn.setOnClickListener(this);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
			rname = data.getStringExtra("rname");
			rphone = data.getStringExtra("rphone");
			contact_tv.setVisibility(View.GONE);
			name_tv.setVisibility(View.VISIBLE);
			phone_tv.setVisibility(View.VISIBLE);
			tv.setVisibility(View.VISIBLE);
			tv2.setVisibility(View.VISIBLE);
			name_tv.setText(rname);
			phone_tv.setText(rphone);
			logout("--------------------" + rname + rphone);

		} else if (requestCode == 2 && resultCode == RESULT_OK) {
			rname = data.getStringExtra("rname");
			rphone = data.getStringExtra("rphone");
			contact_tv.setVisibility(View.GONE);
			name_tv.setVisibility(View.VISIBLE);
			phone_tv.setVisibility(View.VISIBLE);
			tv.setVisibility(View.VISIBLE);
			tv2.setVisibility(View.VISIBLE);
			name_tv.setText(rname);
			phone_tv.setText(rphone);
		}
	}

	/**
	 * 通过区域id获取自提点列表
	 */
	private void asyncGetStoresByCountyId(long goodReceiverCountyId) {
		String wholeUrl = AppUrl.host + AppUrl.GET_STORE_BY_COUNTYID;
		String requestBodyData = ParamBuild
				.getStoresByCountyId(goodReceiverCountyId);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				storesListener);
	}

	BaseRequestListener storesListener = new JsonRequestListener() {

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
			storeList.clear();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject jot = jay.optJSONObject(i);
				StoreVO sv = gson.fromJson(jot.toString(), StoreVO.class);
				storeList.add(sv);
			}
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.contact_area:
			if (TextUtils.isEmpty(rname) && TextUtils.isEmpty(rphone)) {
				Intent intent = new Intent(SelectCollectStoreActivity.this,
						CreateSelfTakeInfoActivity.class);
				startActivityForResult(intent, 1);
			} else {
				rname = name_tv.getText().toString().trim();
				rphone = phone_tv.getText().toString().trim();
				Intent intent = new Intent(SelectCollectStoreActivity.this,
						UpdateSelfTakeInfoActivity.class);
				intent.putExtra("rname", rname);
				intent.putExtra("rphone", rphone);
				logout("SelectCollectStoreActivity" + rname + rphone);
				startActivityForResult(intent, 2);
			}

			break;
		case R.id.reselection_tv:
			// Intent in = new Intent(SelectCollectStoreActivity.this,
			// SelfTakePointSelectActivity.class);
			// startActivity(in);
			finish();
			break;
		case R.id.confirm_btn:
			if (validate()) {
				asyncAddAddress();
			}

			break;
		default:
			break;
		}

	}

	private boolean validate() {
		if (CommonMethod.isListEmpty(storeList)) {
			showShortToast("请选择代收店铺！");
			return false;
		}
		storeName = storeList.get(adapter.getSelectItem()).getStoreName();
		phone = storeList.get(adapter.getSelectItem()).getPhone();
		goodReceiverAddress = storeList.get(adapter.getSelectItem())
				.getAddress();
		storeId = storeList.get(adapter.getSelectItem()).getId();
		if (TextUtils.isEmpty(goodReceiverAddress)) {
			showShortToast("请选择代收店铺！");
			return false;
		}

		return true;
	}

	/**
	 * 添加地址
	 */
	private void asyncAddAddress() {
		String wholeUrl = AppUrl.host + AppUrl.ADD_ADDR;
		String requestBodyData = ParamBuild.addAddressSimple(rname,
				goodReceiverAddress, rphone, GOODRECEIVERTYPE,
				GOODRECEIVERCOUNTRYID, goodReceiverProvinceId,
				goodReceiverCityId, goodReceiverCountyId, ISDEFAULT, storeId);
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
			Intent intent = new Intent();
			intent.putExtra("rname",rname);
			intent.putExtra("rphone", rphone);
			intent.putExtra("raddr",goodReceiverCountyName+goodReceiverAddress+"  "+storeName+"(联系方式："+phone+")");
			intent.putExtra("rtype", 1);
			setResult(RESULT_OK, intent);
			finish();
		}
	};

}
