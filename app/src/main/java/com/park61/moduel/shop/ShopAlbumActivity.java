package com.park61.moduel.shop;

import java.util.ArrayList;
import java.util.List;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.moduel.shop.adapter.ShopAblumFragmentPagerAdapter;
import com.park61.moduel.shop.adapter.ShopMainHelper;
import com.park61.moduel.shop.fragment.FragmentShopAlbum;
import com.park61.widget.viewpager.PagerSlidingTabStrip;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

/**
 * 店铺相册
 * 
 * @author ouyangji
 *
 */
public class ShopAlbumActivity extends BaseActivity {

	private Long shopId;
	private List<FragmentShopAlbum> fragmentList = new ArrayList<FragmentShopAlbum>();
	private List<String> titleList = new ArrayList<String>();

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_shop_album);
	}

	@Override
	public void initView() {
		setOverflowShowingAlways();
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
	}

	@Override
	public void initData() {
		shopId = getIntent().getLongExtra("shopId", -1l);
		for (int j = 0; j < 2; j++) {
			if (j == 0) {
				titleList.add("店铺风采");
				FragmentShopAlbum tMFragment = new FragmentShopAlbum();
				Bundle data = new Bundle();
				data.putLong("shopId", shopId);
				data.putString("ablumType", "1");
				tMFragment.setArguments(data);
				fragmentList.add(tMFragment);
			} else {
				titleList.add("精彩瞬间");
				FragmentShopAlbum tMFragment = new FragmentShopAlbum();
				Bundle data = new Bundle();
				data.putLong("shopId", shopId);
				data.putString("ablumType", "2");
				tMFragment.setArguments(data);
				fragmentList.add(tMFragment);
			}
		}
		pager.setAdapter(new ShopAblumFragmentPagerAdapter(getSupportFragmentManager(), titleList, fragmentList));
		tabs.setViewPager(pager);
		ShopMainHelper.getInstance(mContext).setTabsValue(tabs, 14);
		tabs.setFadeEnabled(false);
		pager.setCurrentItem(0);
	}

	@Override
	public void initListener() {

	}
}
