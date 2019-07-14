package com.park61.moduel.shop.adapter;

import java.util.HashMap;
import java.util.List;

import com.park61.moduel.shop.fragment.FragmentShopAlbum;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class ShopAblumFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<String> titleList;
	private List<FragmentShopAlbum> fList;
	private HashMap<Integer, FragmentShopAlbum> mPageReferenceMap =new HashMap<Integer, FragmentShopAlbum>();

	public ShopAblumFragmentPagerAdapter(FragmentManager fm, List<String> titleList,
			List<FragmentShopAlbum> fragmentList) {
		super(fm);
		this.titleList = titleList;
		this.fList = fragmentList;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titleList.get(position);
	}

	@Override
	public int getCount() {
		return titleList.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	@Override
	public Fragment getItem(int position) {
		 mPageReferenceMap.put(position, fList.get(position));
		return fList.get(position);
	}
	
	public FragmentShopAlbum getFragment(int key) {
	    return mPageReferenceMap.get(key);
	}
}