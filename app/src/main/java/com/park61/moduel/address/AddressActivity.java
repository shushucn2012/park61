package com.park61.moduel.address;

import android.content.Intent;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.address.adapter.AddressDetailAdapter;
import com.park61.moduel.address.adapter.AddressDetailAdapter.DelAddress;
import com.park61.moduel.address.adapter.AddressDetailAdapter.UpdateAddress;
import com.park61.moduel.address.bean.AddressDetailItem;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有收货地址列表
 * 
 * @author Lucia
 */
public class AddressActivity extends BaseActivity implements OnClickListener,
		DelAddress, UpdateAddress {

	private ListView listView;
	private AddressDetailAdapter adapter;
	ArrayList<String> list;

	private List<AddressDetailItem> mList;
	private List<AddressDetailItem> selfList;
	private Button create_btn;
	private Long id; // 编辑地址项的id
	private Long itemId; // 删除地址项的id
	private Long selectId; // 更新默认地址项的id
	private View choose_selftake_point_area;// 选择自提点区域
	private int selectedPos;

	private TextView store_name_tv, name_tv, receive_phone_tv, distance_tv,
			storename_tv, address_tv,tv1;
	private View store_area;
	private String storeName;
	private String phone;
	int selectPosition = 0;
	private long goodReceiverProvinceId;
	private long goodReceiverCityId;
	private String goodReceiverAddress;
	private long goodReceiverCountyId;
	private String goodReceiverProvinceName;// 省份名称
	private String goodReceiverCityName;// 城市名称
	private String goodReceiverCountyName;// 区县名称
	private static final Integer GOODRECEIVERTYPE = 1;// 快递类型0快递1自提
	public static final Long GOODRECEIVERCOUNTRYID = 1L; // 国家id
	public static final Integer ISDEFAULT = 1; // 是否默认收货地址 0-否 1-是
	private long storeId;
	private int goodReceiverType;// 0-快递 1-自提
	private String selfName;
	private String selfPhone;

	private ImageView empty_img;
	private View block;
	private TextView empty_tv;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_address_delivery);
	}

	@Override
	public void initView() {
		empty_img = (ImageView) findViewById(R.id.empty_img);
		empty_tv = (TextView) findViewById(R.id.empty_tv);
		block = findViewById(R.id.block);
		block.setVisibility(View.GONE);

		listView = (ListView) findViewById(R.id.address_listview);
		create_btn = (Button) findViewById(R.id.create_btn);
		choose_selftake_point_area = findViewById(R.id.choose_selftake_point_area);
		store_name_tv = (TextView) findViewById(R.id.store_name_tv);
		store_area = findViewById(R.id.store_area);
		name_tv = (TextView) findViewById(R.id.name_tv);
		receive_phone_tv = (TextView) findViewById(R.id.receive_phone_tv);
		distance_tv = (TextView) findViewById(R.id.distance_tv);
		address_tv = (TextView) findViewById(R.id.address_tv);
		storename_tv = (TextView) findViewById(R.id.storename_tv);
		tv1 = (TextView) findViewById(R.id.tv1);
		// setEmptyView(listView);
	}

	/**
	 * 设置列表为空提示
	 */
	public void setEmptyView(ListView lv) {
		TextView emptyView = new TextView(mContext);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		emptyView.setTextColor(mContext.getResources().getColor(R.color.g666666));
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		emptyView.setText("您还没有收货地址,赶紧去新建地址吧");
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv.getParent()).addView(emptyView);
		lv.setEmptyView(emptyView);
	}

	@Override
	public void initData() {
		selfList = new ArrayList<AddressDetailItem>();

		mList = new ArrayList<AddressDetailItem>();
		adapter = new AddressDetailAdapter(mList, mContext);
		adapter.setDelAddress(this); // 删除地址点击事件
		adapter.setUpdateAddress(AddressActivity.this); // 编辑地址点击事件
		listView.setAdapter(adapter);

		asyncGetAllAddress();
	}

	/**
	 * 获取所有收货地址列表
	 */
	private void asyncGetAllAddress() {
		String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getAddressByUserId;//获取用户所有的地址列表(不区分城市)
		//String wholeUrl = AppUrl.host + AppUrl.GET_ADDR_BY_USERID;
		String requestBodyData = "";
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, listener);
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
			tv1.setVisibility(View.VISIBLE);
			JSONArray jay = jsonResult.optJSONArray("list"); // 获取名为list的Json数组
			mList.clear(); // 清除mList集合，避免重复加载
			selfList.clear();
			for (int i = 0; i < jay.length(); i++) { // 遍历Json数组
				JSONObject jot = jay.optJSONObject(i);
				AddressDetailItem b = gson.fromJson(jot.toString(), AddressDetailItem.class);// 把Json数据解析成对象
				goodReceiverType = b.getGoodReceiverType();
				mList.add(b);
				if (goodReceiverType == 1) {
					selfList.add(b);
				}
			}

			if (selfList.size() > 0) {
				store_area.setVisibility(View.VISIBLE);
			} else {
				store_name_tv.setVisibility(View.GONE);
				store_area.setVisibility(View.GONE);
			}
			if (selfList.size() == 0 && mList.size() == 0) {
//				setEmptyView(listView);
			}

			if (mList.size() > 0) {
				selfName = mList.get(0).getGoodReceiverName();
				selfPhone = mList.get(0).getGoodReceiverMobile();
				name_tv.setText("收件人："+selfName);
				receive_phone_tv.setText(selfPhone);
			}

			adapter.notifyDataSetChanged();
			if (mList.size()<1) {
				empty_tv.setText("好冷清呀~您还没有地址，赶紧去新增一个吧~");
				listView.setEmptyView(block);
			}
			// 选中默认
			for (int j = 0; j < mList.size(); j++) {
				if (mList.get(j).getIsDefault() == 1) {
					adapter.selectItem(j);
					break;
				}
			}
//			asyncGetNeaestStore();

		}
	};

	/**
	 * 获取最近的店铺信息
	 */
	private void asyncGetNeaestStore() {
		String wholeUrl = AppUrl.host + AppUrl.GET_NEAEST_STORE;
		String requestBodyData = "";
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, storeListener);
	}

	BaseRequestListener storeListener = new JsonRequestListener() {

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
			AddressDetailItem a = gson.fromJson(jsonResult.toString(), AddressDetailItem.class);
			goodReceiverProvinceName = a.getArea().getGoodReceiverProvinceName();
			goodReceiverProvinceId = a.getArea().getGoodReceiverProvinceId();
			goodReceiverCityName = a.getArea().getGoodReceiverCityName();
			goodReceiverCityId = a.getArea().getGoodReceiverCityId();
			goodReceiverCountyName = a.getStore().getCountyName();
			goodReceiverCountyId = a.getStore().getCountyId();
			storeName = a.getStore().getStoreName();
			phone = a.getStore().getPhone();
			goodReceiverAddress = a.getStore().getAddress();
			storeId = a.getStore().getId();

			store_name_tv.setText(a.getStore().getStoreName());
			storename_tv.setText(storeName + "(联系电话:" + phone + ")");
			distance_tv.setText(a.getStore().getKiloDistance() + "km");
				address_tv.setText(goodReceiverProvinceName + goodReceiverCityName
						+ goodReceiverCountyName + goodReceiverAddress);
			}
	};

	/**
	 * 删除地址信息
	 * 
	 * @param itemId
	 *            选择删除项的id
	 */
	private void delAddress(Long itemId) {
		String wholeUrl = AppUrl.host + AppUrl.DEL_ADDR;
		String requestBodyData = ParamBuild.getAddressById(itemId);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, delListener);
	}

	BaseRequestListener delListener = new JsonRequestListener() {

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
			asyncGetAllAddress();
			showShortToast("删除成功");
		}
	};

	@Override
	public void initListener() {
		create_btn.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectedPos = position;
				selectId = mList.get(position).getId();
				updateDefaultAddress(selectId);
				adapter.selectItem(position);
			}
		});
		store_area.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				asyncAddAddress();
			}
		});

		choose_selftake_point_area.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(mContext, SelfTakePointSelectActivity.class);
//				Intent it = new Intent(mContext, SelfTakeActivity.class);
				it.putExtra("goodReceiverProvinceName", goodReceiverProvinceName);
				it.putExtra("goodReceiverProvinceId", goodReceiverProvinceId);
				it.putExtra("goodReceiverCityName", goodReceiverCityName);
				it.putExtra("goodReceiverCityId", goodReceiverCityId);
				it.putExtra("goodReceiverCountyName", goodReceiverCountyName);
				it.putExtra("goodReceiverCountyId", goodReceiverCountyId);
				it.putExtra("storeId", storeId);
				it.putExtra("selfName", selfName);
				it.putExtra("selfPhone", selfPhone);
				startActivityForResult(it, 3);
			}
		});
	}

	/**
	 * 更新默认地址
	 * 
	 * @param selectId
	 *            默认地址id
	 */
	private void updateDefaultAddress(Long selectId) {
//		String wholeUrl = AppUrl.host + AppUrl.UPDATE_DEFAULT_ADDR;
//		String requestBodyData = ParamBuild.getAddressById(selectId);
		String wholeUrl = AppUrl.host + AppUrl.TRADE_ORDER_SAVEADDR;
		String requestBodyData = ParamBuild.saveAddr(selectId);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, defaultListener);
	}

	BaseRequestListener defaultListener = new JsonRequestListener() {

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
			Intent data = new Intent();
			AddressDetailItem grv = mList.get(selectedPos);
			data.putExtra("addrId", grv.getId());
			data.putExtra("rname", grv.getGoodReceiverName());
			data.putExtra("rphone", grv.getGoodReceiverMobile());

			if (grv.getGoodReceiverType()==1) {
				data.putExtra("raddr",grv.getGoodReceiverProvinceName()
						+ grv.getGoodReceiverCityName()
						+ grv.getGoodReceiverCountyName()
						+ grv.getGoodReceiverAddress()+grv.getStoreName()+"(联系方式："+grv.getStorePhone()+")");
			}else {
				if(TextUtils.isEmpty(grv.getGoodReceiverTownName())){//所在区域没有四级地址
					data.putExtra("raddr",grv.getGoodReceiverProvinceName()
							+ grv.getGoodReceiverCityName()
							+ grv.getGoodReceiverCountyName()
							+ grv.getGoodReceiverAddress());
				}else{//有四级地址
					data.putExtra("raddr",grv.getGoodReceiverProvinceName()
							+ grv.getGoodReceiverCityName()
							+ grv.getGoodReceiverCountyName()+grv.getGoodReceiverTownName()
							+ grv.getGoodReceiverAddress());
				}

			}
			data.putExtra("rtype", grv.getGoodReceiverType());
			setResult(RESULT_OK, data);
			//showShortToast("设置默认地址成功");
			finish();
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.create_btn:
			Intent createIntent = new Intent(this, AddressCreateActivity.class);
			createIntent.putExtra("from","order");
			startActivityForResult(createIntent, 1);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			asyncGetAllAddress();
		} else if (requestCode == 2) {
			asyncGetAllAddress();
		} else if (requestCode == 3 && resultCode == RESULT_OK) {
			Intent returnData = new Intent();
			returnData.putExtra("rname", data.getStringExtra("rname"));
			returnData.putExtra("rphone", data.getStringExtra("rphone"));
			returnData.putExtra("raddr", data.getStringExtra("raddr"));
			setResult(RESULT_OK, data);
			finish();
		}

	}

	// 更新地址信息
	@Override
	public void updateItem(int position) {
		id = mList.get(position).getId();
		Intent intent = new Intent(this, AddressEditActivity.class);
		intent.putExtra("id", id);
		startActivityForResult(intent, 2);
	}

	// 删除地址条目信息
	@Override
	public void delItem(int position) {
		itemId = mList.get(position).getId();
		dDialog.showDialog("提示", "您确定要删除吗？", "取消", "确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				setCancelable(true);
				dDialog.dismissDialog();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				dDialog.dismissDialog();
				delAddress(itemId);
			}
		});
	}

	/**
	 * 添加最近店铺地址
	 */
	private void asyncAddAddress() {
		String wholeUrl = AppUrl.host + AppUrl.ADD_ADDR;
		String requestBodyData = ParamBuild.addAddressSimple(selfName,
				goodReceiverAddress, selfPhone, GOODRECEIVERTYPE,
				GOODRECEIVERCOUNTRYID, goodReceiverProvinceId,
				goodReceiverCityId, goodReceiverCountyId, ISDEFAULT, storeId);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, addListener);
	}

	BaseRequestListener addListener = new JsonRequestListener() {

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
			// updateDefaultAddress(storeId);
			Intent data = new Intent();
			data.putExtra("rname", selfName);
			data.putExtra("rphone", selfPhone);
			data.putExtra("raddr",goodReceiverProvinceName
					+ goodReceiverCityName + goodReceiverCountyName
					+ goodReceiverAddress + "  " + storeName + "(联系方式：" + phone
					+ ")");
			data.putExtra("rtype", 1);
			setResult(RESULT_OK, data);
			finish();
		}
	};

}
