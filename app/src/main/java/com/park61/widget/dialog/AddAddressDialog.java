package com.park61.widget.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.park61.R;


public class AddAddressDialog {

	private Dialog dialog;
	private View view;
	private LayoutInflater inflater;
	private Button btn_cancle, btn_add_address;
	private TextView tv_title;
	
	
	
	public AddAddressDialog(Context context) {
		dialog = new Dialog(context, R.style.dialog);
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.add_address_dialog, null);
		btn_add_address = (Button) view.findViewById(R.id.btn_add_address);
		btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		WindowManager m = ((Activity) context).getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);
				
	}	
	
	public void showDialog( View.OnClickListener listenerLeft,
			View.OnClickListener listenerRight) {
		btn_add_address.setOnClickListener(listenerRight);
		if (listenerLeft == null) {
			listenerLeft = new OnClickListener() {
				@Override
				public void onClick(View v) {
					dismissDialog();
				}
			};
		}
		btn_cancle.setOnClickListener(listenerLeft);
		setCancelable(true);
		dialog.show();
	}
	public void dismissDialog() {
		dialog.dismiss();
	}
	public boolean isShow() {
		if (dialog.isShowing()) {
			return true;
		} else {
			return false;
		}
	}

	public Dialog getDialog() {
		return dialog;
	}
	public void setCancelable(boolean is) {
		dialog.setCancelable(is);
	}


}
