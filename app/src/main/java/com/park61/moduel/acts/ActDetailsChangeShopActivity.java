package com.park61.moduel.acts;

import android.content.Intent;
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
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.moduel.acts.adapter.ActDetailsChangeShopListAdapter;
import com.park61.moduel.shop.adapter.CountryListAdapter;
import com.park61.moduel.shop.bean.DistrictStore;
import com.park61.moduel.shop.bean.ShopBean;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActDetailsChangeShopActivity extends BaseActivity {

	private List<DistrictStore> countryList;
	private List<ShopBean> shopList;
	private Long refTemplateId;
	private ActDetailsChangeShopListAdapter adapter;
	private CountryListAdapter countryAdapter;

	private TextView tv_currshop;
	private TextView tv_change;
	private ListView lv_shops, lv_country;
	private View area_country;
	private Button btn_confirm;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_actdetails_changeshop);
	}

	@Override
	public void initView() {
		lv_shops = (ListView) findViewById(R.id.lv_shops);
		lv_country = (ListView) findViewById(R.id.lv_country);
		tv_currshop = (TextView) findViewById(R.id.tv_currshop);
		tv_change = (TextView) findViewById(R.id.tv_change);
		area_country = findViewById(R.id.area_country);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
	}

	@Override
	public void initData() {
		countryList = new ArrayList<DistrictStore>();
		shopList = new ArrayList<ShopBean>();
		adapter = new ActDetailsChangeShopListAdapter(mContext, shopList);
		lv_shops.setAdapter(adapter);

		countryAdapter = new CountryListAdapter(mContext, countryList);
		lv_country.setAdapter(countryAdapter);

		refTemplateId = getIntent().getLongExtra("refTemplateId", 0l);
		asyncGetDistrictsByCityId();
	}

	@Override
	public void initListener() {
		tv_change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				area_country.setVisibility(View.VISIBLE);
			}
		});
		lv_country.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				countryAdapter.selectItem(position);
				Long cId = countryList.get(position).getCountyId() == null ? countryList
						.get(position).getAreaId() : countryList.get(position)
						.getCountyId();
				String name = countryList.get(position).getDistrictName() == null ? countryList
						.get(position).getAreaName() : countryList
						.get(position).getDistrictName();
				tv_currshop.setText("已筛选“" + name + "”的店铺");
				asyncGetStoresByCountryAndTmp(cId, refTemplateId);
				area_country.setVisibility(View.GONE);
			}
		});
		lv_shops.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.selectItem(position);
			}
		});
		btn_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CommonMethod.isListEmpty(shopList)) {
					showShortToast("没有选择店铺！");
					return;
				}
				ShopBean sb = shopList.get(adapter.getSelectItem());
				Intent data = new Intent();
				data.putExtra("shopId", sb.getId());
				data.putExtra("shopName", sb.getStoreName());
				setResult(RESULT_OK, data);
				finish();
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
			if (countryList.size() > 0) {
				Long cId = countryList.get(0).getCountyId() == null ? countryList
						.get(0).getAreaId() : countryList.get(0).getCountyId();
				String name = countryList.get(0).getDistrictName() == null ? countryList
						.get(0).getAreaName() : countryList.get(0)
						.getDistrictName();
				tv_currshop.setText("已筛选“" + name + "”的店铺");
				asyncGetStoresByCountryAndTmp(cId, refTemplateId);
			}
			countryAdapter.notifyDataSetChanged();
		}
	};

	/**
	 * 获取区域下的店铺
	 */
	protected void asyncGetStoresByCountryAndTmp(Long countryId,
			Long refTemplateId) {
		String wholeUrl = AppUrl.host + AppUrl.GET_STORES_BY_COUNTRY_AND_TMP;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", countryId);
		map.put("refTemplateId", refTemplateId);
		String requestBodyData = ParamBuild.buildParams(map);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getShopsLsner);
	}

	BaseRequestListener getShopsLsner = new JsonRequestListener() {

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
			adapter.notifyDataSetChanged();
		}
	};

}
