package com.park61;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class GuideViewPagerAdapter extends PagerAdapter {

	// 界面列表
	private List<View> views;
	private int maxNuM = Integer.MAX_VALUE;

	public GuideViewPagerAdapter(List<View> views) {
		this.views = views;
	}

	public GuideViewPagerAdapter(List<View> views, int num) {
		this.views = views;
		maxNuM = num;
	}

	// 销毁arg1位置的界面
	@Override
	public void destroyItem(View container, int position, Object object) {
		// ((ViewPager) container).removeView(views.get(position));
	}

	@Override
	public void finishUpdate(View arg0) {

	}

	// 获得当前界面数
	@Override
	public int getCount() {
		return maxNuM;
	}

	// 初始化arg1位置的界面
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		try {
			((ViewPager) container).addView(
					(View) views.get(position % views.size()), 0);
		} catch (Exception e) {
		}
		return views.get(position % views.size());
	}

	// 判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub
	}

}
