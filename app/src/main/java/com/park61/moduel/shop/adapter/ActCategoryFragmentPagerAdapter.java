package com.park61.moduel.shop.adapter;

import java.util.List;

import com.park61.moduel.shop.fragment.FragmentActListFromCategory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class ActCategoryFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<String> titleList;
	private List<FragmentActListFromCategory> fList;

	public ActCategoryFragmentPagerAdapter(FragmentManager fm, List<String> titleList,
			List<FragmentActListFromCategory> fragmentList) {
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
		return fList.get(position);
	}
}