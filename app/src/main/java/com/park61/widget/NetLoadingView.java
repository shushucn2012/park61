package com.park61.widget;

import com.park61.R;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NetLoadingView extends LinearLayout {

	private View area_loading, area_refresh;
	private OnRefreshClickedLsner lsner;
	private ImageView dialog_image;
	//private AnimationDrawable animationDrawable;
	private Animation dialogAnimation;

	public NetLoadingView(Context context) {
		super(context);
	}

	public NetLoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.net_loading_layout, this, true);
		area_loading = findViewById(R.id.area_loading);
		area_refresh = findViewById(R.id.area_refresh);
		dialog_image = (ImageView) findViewById(R.id.dialog_image);
//		dialog_image.setImageResource(R.drawable.animation_list_loading);
//		animationDrawable = (AnimationDrawable) dialog_image.getDrawable();

		dialogAnimation = AnimationUtils.loadAnimation(context, R.anim.dialog_rotate);
		dialogAnimation.setInterpolator(new LinearInterpolator());

		Button btn_refresh = (Button) findViewById(R.id.btn_refresh);
		btn_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lsner.OnRefreshClicked();
			}
		});
	}

	/**
	 * 显示加载
	 * 
	 * @param dataView
	 *            需要隐藏的数据区域
	 */
	public void showLoading(View dataView) {
		this.setVisibility(View.VISIBLE);
		dataView.setVisibility(View.GONE);
		area_loading.setVisibility(View.VISIBLE);
		area_refresh.setVisibility(View.GONE);
		//animationDrawable.start();
		dialog_image.startAnimation(dialogAnimation);
	}

	/**
	 * 隐藏加载
	 * 
	 * @param dataView
	 *            需要隐藏的数据区域
	 */
	public void hideLoading(View dataView) {
		this.setVisibility(View.GONE);
		dataView.setVisibility(View.VISIBLE);
		area_loading.setVisibility(View.VISIBLE);
		area_refresh.setVisibility(View.GONE);
//		animationDrawable.stop();
		dialog_image.clearAnimation();
	}

	/**
	 * 显示重载框
	 */
	public void showRefresh() {
		area_loading.setVisibility(View.GONE);
		area_refresh.setVisibility(View.VISIBLE);
		/*if(animationDrawable.isRunning()){
			animationDrawable.stop();
		}*/
		dialog_image.clearAnimation();
	}

	public interface OnRefreshClickedLsner {
		public void OnRefreshClicked();
	}

	public void setOnRefreshClickedLsner(OnRefreshClickedLsner lsner) {
		this.lsner = lsner;
	}
}
