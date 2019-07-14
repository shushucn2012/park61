package com.park61.moduel.address.pw;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.park61.R;
import com.park61.common.json.NullStringToEmptyAdapterFactory;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.address.SelectCollectStoreActivity;
import com.park61.moduel.address.SelfTakePointSelectActivity;
import com.park61.moduel.address.adapter.CountryAdapter;
import com.park61.moduel.address.adapter.DistrictShopNameListAdapter;
import com.park61.moduel.shop.bean.DistrictStore;
import com.park61.moduel.shop.bean.ShopBean;
import com.park61.net.base.Request.Method;
import com.park61.net.request.StringNetRequest;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.CommonProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 二级导航窗口
 * 
 * @author super
 */
public class StoreFilterPopWin extends PopupWindow {
	private View toolView;
	private ListView lv_country, lv_shop;
	public CommonProgressDialog commonProgressDialog;
	private OnShopSelectLsner mOnSelectLsner;
	private View mcover;

	private Context mContext;
	private List<DistrictStore> countryList;
	private List<ShopBean> shopList;
//	private CountryListAdapter countryAdapter;
//	private DistrictShopListAdapter shopAdapter;
	private CountryAdapter countryAdapter;
	private DistrictShopNameListAdapter shopAdapter;
	private StringNetRequest netRequest;

	private long goodReceiverCountyId; // 区域id
	private String goodReceiverCountyName;
	private DistrictStore dsItem;


	public StoreFilterPopWin(Context context, OnShopSelectLsner lsner,
			View cover, final long goodReceiverProvinceId,final String selfName,final String selfPhone) {
		super(context);
		mContext = context;
		this.mcover = cover;
		this.mOnSelectLsner = lsner;
		LayoutInflater inflater = LayoutInflater.from(context);
		toolView = inflater.inflate(R.layout.pw_shopfilter_layout, null);
		// 初始化视图
		lv_country = (ListView) toolView.findViewById(R.id.lv_country);
		lv_shop = (ListView) toolView.findViewById(R.id.lv_shop);
		commonProgressDialog = new CommonProgressDialog(mContext);
		// 初始化数据
		countryList = new ArrayList<DistrictStore>();
		shopList = new ArrayList<ShopBean>();
//		countryAdapter = new CountryListAdapter(mContext, countryList);
//		shopAdapter = new DistrictShopListAdapter(mContext, shopList);
		
		countryAdapter = new CountryAdapter(mContext, countryList);
		shopAdapter = new DistrictShopNameListAdapter(mContext, shopList);
		lv_country.setAdapter(countryAdapter);
		lv_shop.setAdapter(shopAdapter);
		netRequest = StringNetRequest.getInstance(mContext);
		netRequest.setMainContext(mContext);
		asyncGetDistrictsByCityId();
		// 初始化监听
		lv_country.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				countryAdapter.selectItem(position);				
				dsItem = countryList.get(position);
				goodReceiverCountyId = dsItem.getCountyId();
				goodReceiverCountyName = dsItem.getDistrictName();	
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
				if (goodReceiverCountyId==0) {
					goodReceiverCountyId = countryList.get(0).getCountyId();
					goodReceiverCountyName = countryList.get(0).getDistrictName();
				}
				mOnSelectLsner.onShopSelect(sb.getId(), sb.getName());
				
				Intent intent = new Intent(mContext,
						SelectCollectStoreActivity.class);
				intent.putExtra("goodReceiverCountyId", goodReceiverCountyId);
				intent.putExtra("goodReceiverCountyName",
						goodReceiverCountyName);
				intent.putExtra("goodReceiverProvinceId",
						goodReceiverProvinceId);
				intent.putExtra("selfName", selfName);
				intent.putExtra("selfPhone", selfPhone);
				intent.putExtra("selectPos", position);
				((SelfTakePointSelectActivity)mContext).startActivityForResult(intent, 5);
			}
		});

		// 设置SelectPicPopupWindow的View
		this.setContentView(toolView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(DevAttr.getScreenWidth(mContext));
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight((int) (DevAttr.getScreenHeight(mContext) * 0.6));
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(null);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		toolView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					dismiss();
				}
				return true;
			}
		});
		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				mcover.setVisibility(View.GONE);
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
			mcover.setVisibility(View.VISIBLE);
			JSONArray jay = jsonResult.optJSONArray("list");
			Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject jot = jay.optJSONObject(i);
				DistrictStore p = gson.fromJson(jot.toString(), DistrictStore.class);
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
			Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
			for (int i = 0; i < jay.length(); i++) {
				JSONObject jot = jay.optJSONObject(i);
				ShopBean p = gson.fromJson(jot.toString(), ShopBean.class);
				shopList.add(p);
			}
			shopAdapter.notifyDataSetChanged();
		}
	};

	public void showDialog() {
		try {
			if (commonProgressDialog.isShow()) {
				return;
			} else {
				commonProgressDialog.showDialog(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dismissDialog() {
		try {
			if (commonProgressDialog.isShow()) {
				commonProgressDialog.dialogDismiss();
			} else {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showShortToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	public interface OnShopSelectLsner {
		public void onShopSelect(Long id, String name);
	}

}
