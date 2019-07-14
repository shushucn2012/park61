package com.park61.moduel.shop;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.widget.imageview.PhotoView;
import com.park61.widget.imageview.ScannerUtils;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ShowPhotoInViewPagerActivity extends BaseActivity {

	private ViewPager myViewPager;

	private Intent mPreIntent;

	private String[] mUrlArray;

	private int mIndex;

	private int mIconId = R.drawable.img_default_v;

	private TextView titleTxt, saveText;

	@Override
	public void setLayout() {
		setContentView(R.layout.cmp_show_photo);
	}

	@Override
	public void initView() {
		myViewPager = (ViewPager) findViewById(R.id.newspaper_body_view_pager);
		titleTxt = (TextView) findViewById(R.id.txt_title);
		saveText = (TextView) findViewById(R.id.txt_toolbar_save);
	}

	@Override
	public void initData() {
		mPreIntent = getIntent();
		// mIconId = mPreIntent.getIntExtra("iconId", mIconId);
		if (mPreIntent.getIntExtra("index", 0) > 0) {
			mIndex = mPreIntent.getIntExtra("index", 0);
		}
		if (mPreIntent.getStringArrayExtra("imageUrlArray") != null) {
			mUrlArray = mPreIntent.getStringArrayExtra("imageUrlArray");

			myViewPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
			myViewPager.setAdapter(new PagerAdapter() {
				@Override
				public int getCount() {
					return mUrlArray.length;
				}

				@Override
				public boolean isViewFromObject(View view, Object object) {
					return view == object;
				}

				@Override
				public Object instantiateItem(ViewGroup container, int position) {
					PhotoView view = new PhotoView(ShowPhotoInViewPagerActivity.this);
					view.enable();
					view.loadImageUrl(mUrlArray[position], mIconId);
					container.addView(view);
					return view;
				}

				@Override
				public void destroyItem(ViewGroup container, int position, Object object) {
					container.removeView((View) object);
				}
			});
		}

	}

	@Override
	public void initListener() {
		myViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				mIndex = index;
				setActivityTitle((index + 1) + "/" + mUrlArray.length);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		saveText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String strSaveUrl = mUrlArray[mIndex];
				ScannerUtils.saveImageToGallery(ShowPhotoInViewPagerActivity.this, strSaveUrl);
				// 保存当前查看的图片
				// showShortToast("图片保存成功");
			}
		});

		setActivityTitle((mIndex + 1) + "/" + mUrlArray.length);
		myViewPager.setCurrentItem(mIndex);
	}

	private void setActivityTitle(String pTitle) {
		if (!TextUtils.isEmpty(pTitle)) {
			titleTxt.setText(pTitle);
		}
	}

}
