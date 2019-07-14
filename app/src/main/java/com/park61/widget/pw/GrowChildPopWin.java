package com.park61.widget.pw;

import java.util.List;

import com.park61.R;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.child.adapter.PwGrowChildListAdapter;
import com.park61.moduel.me.AddBabyActivity;
import com.park61.moduel.me.bean.BabyItem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 成长首页孩子列表
 * 
 * @author super
 */
public class GrowChildPopWin extends PopupWindow {
	private View toolView;
	private ListView lv_child;
	private OnBabySelectLsner mOnSelectLsner;
	private View mcover;
	private Button btn_add_baby;

	private Context mContext;
	private List<BabyItem> mbabyList;
	private PwGrowChildListAdapter adapter;

	public GrowChildPopWin(Context context, View cover,
			List<BabyItem> babyList, int selecteddPos, OnBabySelectLsner lsner) {
		super(context);
		mContext = context;
		this.mcover = cover;
		this.mbabyList = babyList;
		this.mOnSelectLsner = lsner;
		LayoutInflater inflater = LayoutInflater.from(context);
		toolView = inflater.inflate(R.layout.pw_growchild_layout, null);
		// 初始化视图
		lv_child = (ListView) toolView.findViewById(R.id.lv_child);
		btn_add_baby = (Button) toolView.findViewById(R.id.btn_add_baby);
		// 初始化数据
		adapter = new PwGrowChildListAdapter(mContext, mbabyList, selecteddPos);
		lv_child.setAdapter(adapter);
		mcover.setVisibility(View.VISIBLE);
		// 初始化监听
		lv_child.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mOnSelectLsner.onSelect(position);
			}
		});

		// 设置SelectPicPopupWindow的View
		this.setContentView(toolView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(DevAttr.getScreenWidth(mContext));
		// 设置SelectPicPopupWindow弹出窗体的高
		if (babyList.size() > 5) {
			this.setHeight((int) (DevAttr.getScreenHeight(mContext) * 0.6));
		} else {
			int h = babyList.size() * DevAttr.dip2px(mContext, 61);
			h += DevAttr.dip2px(mContext, 70);
			this.setHeight(h);
		}
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(null);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		toolView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					dismiss();
				}
				return true;
			}
		});
		this.setOutsideTouchable(true);
		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				mcover.setVisibility(View.GONE);
			}
		});
		btn_add_baby.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//mContext.startActivity(new Intent(mContext, AddBabyActivity.class));
				mOnSelectLsner.onAdd();
				dismiss();
			}
		});
	}

	public interface OnBabySelectLsner {
		public void onSelect(int pos);
		public void onAdd();
	}

}
