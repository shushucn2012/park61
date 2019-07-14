package com.park61.moduel.pay.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 控件联合体，用于将会产生联锁变化的控件归纳管理
 * 
 * @author super
 */
public class CombCardTypeToTab {

	private Context mContext;
	private List<View> tablist;
	private List<TextView> textlist;
	private int resDefault, resChoose;
	private int resDefTextColor, resChoTextColor;

	public CombCardTypeToTab(Context c, List<View> tablist,
			List<TextView> textlist, int resDefault, int resChoose,
			int resDefTextColor, int resChoTextColor) {
		this.mContext = c;
		this.tablist = tablist;
		this.textlist = textlist;
		this.resDefault = resDefault;
		this.resChoose = resChoose;
		this.resDefTextColor = resDefTextColor;
		this.resChoTextColor = resChoTextColor;
	}

	public void init() {
		View item = tablist.get(0);
		TextView text = textlist.get(0);
		if (resDefault != 0)// 没有自定义资源时
			item.setBackgroundResource(resChoose);
		if (resDefTextColor != 0)
			text.setTextColor(mContext.getResources().getColor(resChoTextColor));
		disColorOthers(0);
	}

	public void setFocus(int index){
		View item = tablist.get(index);
		TextView text = textlist.get(index);
		if (resDefault != 0)// 没有自定义资源时
			item.setBackgroundResource(resChoose);
		if (resDefTextColor != 0)
			text.setTextColor(mContext.getResources().getColor(resChoTextColor));
		disColorOthers(index);
	}

	public void initLsner(final OnChangeEndLsner mOnChangeEndLsner) {
		for (int i = 0; i < tablist.size(); i++) {
			final View item = tablist.get(i);
			final TextView text = textlist.get(i);
			final int pos = i;
			item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (resDefault != 0)// 没有自定义资源时
						item.setBackgroundResource(resChoose);
					if (resDefTextColor != 0)
						text.setTextColor(mContext.getResources().getColor(
								resChoTextColor));
					disColorOthers(pos);
					mOnChangeEndLsner.doEnd(pos);
				}
			});
		}
	}

	protected void disColorOthers(int position) {
		for (int i = 0; i < tablist.size(); i++) {
			if (i != position) {
				View item = tablist.get(i);
				TextView text = textlist.get(i);
				if (resDefault != 0)// 没有自定义资源时
					item.setBackgroundResource(resDefault);
				if (resDefTextColor != 0)
					text.setTextColor(mContext.getResources().getColor(
							resDefTextColor));
			}
		}
	}

	public interface OnChangeEndLsner {
		public void doEnd(int pos);
	}

}
