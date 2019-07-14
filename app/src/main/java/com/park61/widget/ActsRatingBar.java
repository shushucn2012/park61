package com.park61.widget;

import java.util.ArrayList;
import java.util.List;
import com.park61.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ActsRatingBar extends LinearLayout {
	private int point = 0;// 分数默认5，最高5
	private ImageView hua1, hua2, hua3, hua4, hua5;
	private OnItemClickedListener mOnItemClicked;
	
	public ActsRatingBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public ActsRatingBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.star_eva_layout, this, true);
		hua1 = (ImageView) findViewById(R.id.hua1);
		hua2 = (ImageView) findViewById(R.id.hua2);
		hua3 = (ImageView) findViewById(R.id.hua3);
		hua4 = (ImageView) findViewById(R.id.hua4);
		hua5 = (ImageView) findViewById(R.id.hua5);
		hua1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (point < 1) {// 如果分数为0，点击则点亮该星
					hua1.setImageResource(R.drawable.goods_evaluate_star);
					point = 1;
				} else if (point == 1) {// 如果分数为1，代表该星显示，则置灰
					hua1.setImageResource(R.drawable.goods_evaluate_star_default);
					point = 0;
				} else if (point > 1) {// 如果分数大于1，代表多星显示，则之后的星全部置灰
					hua2.setImageResource(R.drawable.goods_evaluate_star_default);
					hua3.setImageResource(R.drawable.goods_evaluate_star_default);
					hua4.setImageResource(R.drawable.goods_evaluate_star_default);
					hua5.setImageResource(R.drawable.goods_evaluate_star_default);
					point = 1;
				}
				if (mOnItemClicked != null)
					mOnItemClicked.OnClicked();
			}
		});
		hua2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (point < 2) {// 如果分数为0，点击则点亮该星
					hua1.setImageResource(R.drawable.goods_evaluate_star);
					hua2.setImageResource(R.drawable.goods_evaluate_star);
					point = 2;
				} else if (point == 2) {// 如果分数为2，代表该星及之前显示，则置灰该星
					hua2.setImageResource(R.drawable.goods_evaluate_star_default);
					point = 1;
				} else if (point > 2) {// 如果分数大于2，代表多星显示，则该星之后的全部置灰
					hua3.setImageResource(R.drawable.goods_evaluate_star_default);
					hua4.setImageResource(R.drawable.goods_evaluate_star_default);
					hua5.setImageResource(R.drawable.goods_evaluate_star_default);
					point = 2;
				}
				if (mOnItemClicked != null)
					mOnItemClicked.OnClicked();
			}
		});
		hua3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (point < 3) {// 如果分数为0，点击则点亮该星
					hua1.setImageResource(R.drawable.goods_evaluate_star);
					hua2.setImageResource(R.drawable.goods_evaluate_star);
					hua3.setImageResource(R.drawable.goods_evaluate_star);
					point = 3;
				} else if (point == 3) {// 如果分数为3，代表该星及之前显示，则置灰该星
					hua3.setImageResource(R.drawable.goods_evaluate_star_default);
					point = 2;
				} else if (point > 3) {// 如果分数大于3，代表多星显示，则该星之后的全部置灰
					hua4.setImageResource(R.drawable.goods_evaluate_star_default);
					hua5.setImageResource(R.drawable.goods_evaluate_star_default);
					point = 3;
				}
				if (mOnItemClicked != null)
					mOnItemClicked.OnClicked();
			}
		});
		hua4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (point < 4) {// 如果分数为0，点击则点亮该星
					hua1.setImageResource(R.drawable.goods_evaluate_star);
					hua2.setImageResource(R.drawable.goods_evaluate_star);
					hua3.setImageResource(R.drawable.goods_evaluate_star);
					hua4.setImageResource(R.drawable.goods_evaluate_star);
					point = 4;
				} else if (point == 4) {// 如果分数为4，代表该星及之前显示，则置灰该星
					hua4.setImageResource(R.drawable.goods_evaluate_star_default);
					point = 3;
				} else if (point > 4) {// 如果分数大于3，代表多星显示，则该星之后的全部置灰
					hua5.setImageResource(R.drawable.goods_evaluate_star_default);
					point = 4;
				}
				if (mOnItemClicked != null)
					mOnItemClicked.OnClicked();
			}
		});
		hua5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (point < 5) {// 如果分数为0，点击则点亮该星
					hua1.setImageResource(R.drawable.goods_evaluate_star);
					hua2.setImageResource(R.drawable.goods_evaluate_star);
					hua3.setImageResource(R.drawable.goods_evaluate_star);
					hua4.setImageResource(R.drawable.goods_evaluate_star);
					hua5.setImageResource(R.drawable.goods_evaluate_star);
					point = 5;
				} else if (point == 5) {// 如果分数为5，代表该星及之前显示，则置灰该星
					hua5.setImageResource(R.drawable.goods_evaluate_star_default);
					point = 4;
				}
				if (mOnItemClicked != null)
					mOnItemClicked.OnClicked();
			}
		});
	}

	public ActsRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * 获取评分
	 */
	public int getPoint() {
		return point;
	}

	/**
	 * 设置分数
	 */
	public void setPoint(Integer point) {
		if (point >= 5) {
			point = 5;
		}
		List<ImageView> huaList = new ArrayList<ImageView>();
		huaList.add(hua1);
		huaList.add(hua2);
		huaList.add(hua3);
		huaList.add(hua4);
		huaList.add(hua5);
		for (int i = 0; i < huaList.size(); i++) {
			huaList.get(i).setImageResource(R.drawable.goods_evaluate_star_default);
		}
		for (int i = 0; i < point; i++) {
			huaList.get(i).setImageResource(R.drawable.goods_evaluate_star);
		}
		setPointNotClick();
	}
	
	/**
	 * 设置为不可点击
	 */
	public void setPointNotClick(){
		hua1.setOnClickListener(null);
		hua2.setOnClickListener(null);
		hua3.setOnClickListener(null);
		hua4.setOnClickListener(null);
		hua5.setOnClickListener(null);
	}
	
	public interface OnItemClickedListener {
		public void OnClicked();
	}

	public void setOnItemClickedListener(OnItemClickedListener listener) {
		this.mOnItemClicked = listener;
	}

}
