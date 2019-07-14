package com.park61.widget.dialog;



import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.park61.R;


public class SelectLogisticsDetailDialog {

	private Dialog dialog;
	private View view,rl_select;
	private LayoutInflater inflater;
	private Button btn_cancle, btn_ok;
	private TextView tv_title, delivery_company_name,tv_delivery;
	private EditText et_delivery_number;
	private List<String> list;
//	public Spinner spinner;
	
	public SelectLogisticsDetailDialog(final Context context,List<String> lists) {
		dialog = new Dialog(context, R.style.dialog);
		inflater = LayoutInflater.from(context);		
		view = inflater.inflate(R.layout.select_logistics_detail_dialog, null);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		rl_select = (View) view.findViewById(R.id.rl_select);
		delivery_company_name = (TextView) view.findViewById(R.id.tv_delivery_company_name);
		et_delivery_number = (EditText) view.findViewById(R.id.et_delivery_number);
		tv_delivery = (TextView) view.findViewById(R.id.tv_delivery);
		
		//使用spinner实现下拉框选择（物流名称选择）
//		spinner = (Spinner) view.findViewById(R.id.spinner1); 		
//		List<String> data_list = new ArrayList<String>();
//		data_list.add("顺丰");
//		data_list.add("申通");
//		data_list.add("韵达");
//		data_list.add("中通");
//		ArrayAdapter<String> adapter =  new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, data_list);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
//		spinner.setAdapter(adapter);
					
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		WindowManager m = ((Activity) context).getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);
		
		rl_select.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final SelectLogisticsNameDialog nameDialog = new SelectLogisticsNameDialog(context, list);
				nameDialog.showDialog();
				nameDialog.dialog.setOnDismissListener(new OnDismissListener() {//关闭回调监听
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						delivery_company_name.setText(nameDialog.getSelectItem().toString());
						if((nameDialog.getSelectItem().toString()).contains("京东物流")){
							tv_delivery.setText("订单号");
						}else{
							tv_delivery.setText("快递单号");
						}
					}
				});
			}
		});
				
	}	
	
	public void showDialog( View.OnClickListener listenerLeft,
			View.OnClickListener listenerRight) {
		btn_ok.setOnClickListener(listenerRight);
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
	public String getDeliveryCompanyName(){
		return delivery_company_name.getText().toString();
//		return spinner.getSelectedItem().toString();
	}
	public String getWaybillCode(){
		return et_delivery_number.getText().toString();
	}

}
