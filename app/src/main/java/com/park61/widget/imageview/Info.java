package com.park61.widget.imageview;

import android.graphics.RectF;
import android.widget.ImageView;

public class Info {
	RectF mRect = new RectF();
	RectF mLocalRect = new RectF();
	RectF mImgRect = new RectF();
	RectF mWidgetRect = new RectF();
	float mScale;
	float mDegrees;
	ImageView.ScaleType mScaleType;

	public Info(RectF rect, RectF local, RectF img, RectF widget, float scale,
			float degrees, ImageView.ScaleType scaleType) {
		mRect.set(rect);
		mLocalRect.set(local);
		mImgRect.set(img);
		mWidgetRect.set(widget);
		mScale = scale;
		mScaleType = scaleType;
		mDegrees = degrees;
	}
}
