package com.park61.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.park61.R;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.growing.GrowingActivity;
import com.park61.moduel.me.MeActivity;
import com.park61.moduel.sales.SalesMainV2Activity;

/**
 * 可以失败重刷的进度加载框
 * 
 * @author super
 */
public class CanRefreshProgressDialog {

	private Context mContext;
	private Dialog dialog;
	private View view, area_loading, area_refresh;
	private LayoutInflater inflater;
	private ImageView dialog_image;
	private AnimationDrawable animationDrawable;

	public CanRefreshProgressDialog(Context context, final OnRefreshClickedLsner lsner) {
		mContext = context;
		dialog = new Dialog(context, R.style.can_refresh_dialog);
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.canrefresh_progress_dialog_layout, null);
		Button left_img = (Button) view.findViewById(R.id.left_img);
		left_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((Activity) mContext).finish();
			}
		});
		Button btn_refresh = (Button) view.findViewById(R.id.btn_refresh);
		btn_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lsner.OnRefreshClicked();
			}
		});
		area_loading = view.findViewById(R.id.area_loading);
		area_refresh = view.findViewById(R.id.area_refresh);
		dialog_image = (ImageView) view.findViewById(R.id.dialog_image);
		dialog_image.setImageResource(R.drawable.animation_list_loading);
		animationDrawable = (AnimationDrawable) dialog_image.getDrawable();
		dialog.setContentView(view);
		dialog.setCancelable(false);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
					if(mContext == null)
						return false;
					if((Activity)mContext instanceof SalesMainV2Activity
							|| (Activity)mContext instanceof GrowingActivity
							|| (Activity)mContext instanceof MeActivity){
						return false;
					}
					if(isShow()) {
						dialogDismiss();
					}
					((Activity)mContext).finish();
				}
				return false;
			}
		});
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.height = DevAttr.getScreenHeight(mContext);
		p.width = DevAttr.getScreenWidth(mContext);
		dialogWindow.setAttributes(p);
	}

	public void showDialog() {
		try {
			showLoadingView();
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dialogDismiss() {
		try {
			animationDrawable.stop();
			dialog.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isShow() {
		if (dialog.isShowing()) {
			return true;
		} else {
			return false;
		}
	}

	public void showLoadingView() {
		area_loading.setVisibility(View.VISIBLE);
		area_refresh.setVisibility(View.GONE);
		animationDrawable.start();
	}

	public void showRefreshBtn() {
		area_loading.setVisibility(View.GONE);
		area_refresh.setVisibility(View.VISIBLE);
		animationDrawable.stop();
	}

	public interface OnRefreshClickedLsner {
		public void OnRefreshClicked();
	}

}
