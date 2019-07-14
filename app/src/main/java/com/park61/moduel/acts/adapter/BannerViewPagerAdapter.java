package com.park61.moduel.acts.adapter;

import java.util.ArrayList;
import java.util.List;

import com.park61.common.tool.ImageManager;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BannerViewPagerAdapter extends PagerAdapter {

	// 界面列表
	private List<String> pics;
	private List<ImageView> imgViews;
	private Context mContext;

	public BannerViewPagerAdapter(List<String> pics, Context mContext) {
		this.pics = pics;
		this.mContext = mContext;
		imgViews = new ArrayList<ImageView>();
		for (int i = 0; i < pics.size(); i++) {
			imgViews.add(new ImageView(mContext));
		}
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
		return Integer.MAX_VALUE;
	}

	// 初始化arg1位置的界面
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		try {
			ImageView view = imgViews.get(position % pics.size());
			String url = pics.get(position % pics.size());
			((ViewPager) container).addView(view, 0);
			ImageManager.getInstance().displayImg(view, url);
		} catch (Exception e) {
		}
		return pics.get(position % pics.size());
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
