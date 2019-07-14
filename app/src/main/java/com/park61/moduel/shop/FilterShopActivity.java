package com.park61.moduel.shop;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.shop.adapter.CountryListAdapter;
import com.park61.moduel.shop.adapter.DistrictShopListAdapter;
import com.park61.moduel.shop.bean.DistrictStore;
import com.park61.moduel.shop.bean.ShopBean;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FilterShopActivity extends BaseActivity {

	private List<DistrictStore> countryList;
	private List<ShopBean> shopList;
	private CountryListAdapter countryAdapter;
	private DistrictShopListAdapter shopAdapter;

	private ListView lv_country, lv_shop;
	private TextView tv_currshop;
	private TextView edit_shopname;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_filter_shop);
	}

	@Override
	public void initView() {
		lv_country = (ListView) findViewById(R.id.lv_country);
		lv_shop = (ListView) findViewById(R.id.lv_shop);
		tv_currshop = (TextView) findViewById(R.id.tv_currshop);
		edit_shopname = (TextView) findViewById(R.id.edit_shopname);
	}

	@Override
	public void initData() {
		tv_currshop.setText(getIntent().getStringExtra("curShop"));
		countryList = new ArrayList<DistrictStore>();
		shopList = new ArrayList<ShopBean>();
		countryAdapter = new CountryListAdapter(mContext, countryList);
		shopAdapter = new DistrictShopListAdapter(mContext, shopList);
		lv_country.setAdapter(countryAdapter);
		lv_shop.setAdapter(shopAdapter);
		asyncGetDistrictsByCityId();
	}

	@Override
	public void initListener() {
		lv_country.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				countryAdapter.selectItem(position);
				DistrictStore dsItem = countryList.get(position);
				Long cId = dsItem.getCountyId() == null ? dsItem.getAreaId()
						: dsItem.getCountyId();
				asyncGetDistrictShopsByCountryId(cId);
			}
		});
		lv_shop.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ShopBean sb = shopList.get(position);
				Intent data = new Intent();
				data.putExtra("shopId", sb.getId());
				data.putExtra("shopName", sb.getName());
				setResult(RESULT_OK, data);
				finish();
			}
		});
		edit_shopname.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(mContext, FilterSearchShopActivity.class);
				startActivityForResult(it, 0);
			}
		});
	}

	/**
	 * 获取区域
	 */
	protected void asyncGetDistrictsByCityId() {
		String wholeUrl = AppUrl.host + AppUrl.GET_DISTRICTS_BY_CITYID;
		String requestBodyData = "";
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				getDistrictsByCityId);
	}

	BaseRequestListener getDistrictsByCityId = new JsonRequestListener() {

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
			for (int i = 0; i < jay.length(); i++) {
				JSONObject jot = jay.optJSONObject(i);
				DistrictStore p = gson.fromJson(jot.toString(),
						DistrictStore.class);
				countryList.add(p);
			}
			countryAdapter.notifyDataSetChanged();
			if (countryList.size() > 0) {
				Long cId = countryList.get(0).getCountyId() == null ? countryList
						.get(0).getAreaId() : countryList.get(0).getCountyId();
				asyncGetDistrictShopsByCountryId(cId);
			}
		}
	};

	/**
	 * 获取区域下的店铺
	 */
	protected void asyncGetDistrictShopsByCountryId(Long countryId) {
		String wholeUrl = AppUrl.host + AppUrl.GET_DISTRICTSHOPS_BY_COUNTRYID;
		String requestBodyData = ParamBuild.getDistrictShopsByCountryId(
				countryId, 1, 300);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				getDistrictShopsByCountryId);
	}

	BaseRequestListener getDistrictShopsByCountryId = new JsonRequestListener() {

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
			shopList.clear();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject jot = jay.optJSONObject(i);
				ShopBean p = gson.fromJson(jot.toString(), ShopBean.class);
				shopList.add(p);
			}
			shopAdapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
			Long shopId = data.getLongExtra("shopId", 0l);
			String shopName = data.getStringExtra("shopName");
			Intent returnData = new Intent();
			returnData.putExtra("shopId", shopId);
			returnData.putExtra("shopName", shopName);
			logout("shopId===" + shopId + " and shopName===" + shopName);
			setResult(RESULT_OK, returnData);
			finish();
		}
	}

}
