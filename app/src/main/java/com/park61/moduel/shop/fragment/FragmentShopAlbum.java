package com.park61.moduel.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.adapter.ShopAlbumAdapter;
import com.park61.moduel.acts.bean.ShopAlbumItem;
import com.park61.moduel.shop.ShowPhotoInViewPagerActivity;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentShopAlbum extends BaseFragment {

	private Long shopId;
	private String ablumType;

	private PullToRefreshGridView mPullToRefreshGridView;
	private List<ShopAlbumItem> actDataList = new ArrayList<ShopAlbumItem>();
	private ShopAlbumAdapter adapter;

	public FragmentShopAlbum() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.fragment_shop_ablum_gridview, container, false);
		shopId = getArguments().getLong("shopId");
		ablumType = getArguments().getString("ablumType");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void initView() {
		mPullToRefreshGridView = (PullToRefreshGridView) parentView.findViewById(R.id.pull_refresh_list);
		ViewInitTool.initPullToRefresh(mPullToRefreshGridView);
		mPullToRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				asyncGetShopActs();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
			}
		});
		GridView actualListView = mPullToRefreshGridView.getRefreshableView();
		actDataList = new ArrayList<ShopAlbumItem>();
		adapter = new ShopAlbumAdapter(parentActivity, actDataList);
		mPullToRefreshGridView.setAdapter(adapter);
		mPullToRefreshGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position < 0 || actDataList.size() < 0)
					return;
				ArrayList<String> strArray = new ArrayList<>();
				for (int i = 0; i < actDataList.size(); i++) {
					strArray.add(actDataList.get(i).getPicUrl());
				}
				Intent it = new Intent(parentActivity, ShowPhotoInViewPagerActivity.class);
				it.putExtra("index", position);
				String[] array = (String[])strArray.toArray(new String[strArray.size()]);
				it.putExtra("imageUrlArray", array);
				FragmentShopAlbum.this.getActivity().startActivity(it);
			}
		});
		setGridEmptyView(actualListView);
		
	}

	@Override
	public void initData() {
		asyncGetShopActs();
	}

	@Override
	public void initListener() {
	}

	public void onListRefresh() {
		asyncGetShopActs();
	}

	/**
	 * 根据分类、时间段查询店铺游戏列表
	 */
	private void asyncGetShopActs() {
		String wholeUrl = AppUrl.host + AppUrl.GET_MERCHANT_PICTURE_LIST;
		String requestBodyData = ParamBuild.getMerchantPictureList(shopId + "", ablumType);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getShopAblumListener);
	}

	BaseRequestListener getShopAblumListener = new JsonRequestListener() {

		@Override
		public void onStart(int requestId) {
		}

		@Override
		public void onError(int requestId, String errorCode, String errorMsg) {
			listStopLoadView();
			showShortToast(errorMsg);
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			listStopLoadView();
			actDataList.clear();
			ArrayList<ShopAlbumItem> currentPageList = new ArrayList<ShopAlbumItem>();
			JSONArray actJay = jsonResult.optJSONArray("list");
			// 第一次查询的时候没有数据，则提示没有数据，页面置空
			if ((actJay == null || actJay.length() <= 0)) {
				actDataList.clear();
				adapter.notifyDataSetChanged();
				setPullToRefreshViewEnd();
				return;
			}
			// 解析数据
			for (int i = 0; i < actJay.length(); i++) {
				JSONObject actJot = actJay.optJSONObject(i);
				ShopAlbumItem c = gson.fromJson(actJot.toString(), ShopAlbumItem.class);
				c.setThumbNail(c.getPicUrl().replace("ghpprod.oss-cn", "ghpprod.img-cn") + "@250h_250w_1e");
				currentPageList.add(c);
			}
			// 加载数据并刷新列表
			actDataList.addAll(currentPageList);
			adapter.notifyDataSetChanged();
		}
	};

	/**
	 * 停止列表进度条
	 */
	protected void listStopLoadView() {
		mPullToRefreshGridView.onRefreshComplete();
	}

	/**
	 * 将上下拉控件设为到底
	 */
	protected void setPullToRefreshViewEnd() {
		mPullToRefreshGridView.setMode(Mode.PULL_FROM_START);
	}
}
