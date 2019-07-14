package com.park61.moduel.address;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.address.adapter.MyAddressManageAdapter;
import com.park61.moduel.address.adapter.MyAddressManageAdapter.DelAddress;
import com.park61.moduel.address.adapter.MyAddressManageAdapter.UpdateAddress;
import com.park61.moduel.address.bean.AddressDetailItem;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收货地址列表界面
 * 
 * @author Lucia
 */
public class AddressManageActivity extends BaseActivity implements
		OnClickListener, DelAddress, UpdateAddress{

	private ListView listView;
//	private AddressDetailAdapter adapter;
	private MyAddressManageAdapter adapter;
	private List<AddressDetailItem> mList;
	private Button create_btn;
	private long id; // 编辑地址项的id
	private long itemId; // 删除地址项的id
	private long selectId; // 更新默认地址项的id
	private int selectedPos;
	@Override
	public void setLayout() {
		setContentView(R.layout.activity_address_manage);
	}

	@Override
	public void initView() {
		listView = (ListView) findViewById(R.id.address_listview);
		create_btn = (Button) findViewById(R.id.create_btn);
		ViewInitTool.setListEmptyView(mContext,listView,"好冷清呀~您还没有地址，赶紧去新增一个吧~",
				R.drawable.quexing,null,100,95);
	}


	@Override
	public void initData() {
		mList = new ArrayList<AddressDetailItem>();
		adapter = new MyAddressManageAdapter(mList, mContext);
		adapter.setDelAddress(this); // 删除地址点击事件
		adapter.setUpdateAddress(AddressManageActivity.this); // 编辑地址点击事件
		listView.setAdapter(adapter);
		asyncGetAllAddress();
	}

	/**
	 * 获取用户所有的地址列表(不区分城市-收货地址管理)
	 */
	private void asyncGetAllAddress() {
		String wholeUrl = AppUrl.host + AppUrl.GET_ADDR;
		String requestBodyData = "";
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
			JSONArray jay = jsonResult.optJSONArray("list"); // 获取名为list的Json数组
			mList.clear(); // 清除mList集合，避免重复加载
			for (int i = 0; i < jay.length(); i++) { // 遍历Json数组
				JSONObject jot = jay.optJSONObject(i);
				AddressDetailItem b = gson.fromJson(jot.toString(),
						AddressDetailItem.class);// 把Json数据解析成对象
//				if ((b.getGoodReceiverCityId() + "")
//						.equals(GlobalParam.chooseCityCode))
//					mList.add(b);
				mList.add(b);
			}
			adapter.notifyDataSetChanged();
			// 选中默认
			for (int j = 0; j < mList.size(); j++) {
				if (mList.get(j).getIsDefault() == 1) {
					adapter.selectItem(j);
					break;
				}
			}
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
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				delListener);
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
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedPos = position;
				selectId = mList.get(position).getId();
				updateDefaultAddress(selectId);
				adapter.selectItem(position);
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
		String wholeUrl = AppUrl.host + AppUrl.UPDATE_DEFAULT_ADDR;
		String requestBodyData = ParamBuild.getAddressById(selectId);
//		String wholeUrl = AppUrl.host + AppUrl.TRADE_ORDER_SAVEADDR;
//		String requestBodyData = ParamBuild.saveAddr(selectId);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				defaultListener);
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
			data.putExtra("rname", grv.getGoodReceiverName());
			data.putExtra("rphone", grv.getGoodReceiverMobile());
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
			setResult(RESULT_OK, data);
			finish();
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.create_btn:
			Intent createIntent = new Intent(this, MyAddressManageCreateActivity.class);
			createIntent.putExtra("from","my");
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
		Intent intent = new Intent(this, MyAddressManageEditActivity.class);
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

}
