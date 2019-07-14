package com.park61.widget.dialog;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.park61.R;
import com.park61.moduel.salesafter.adapter.SelectLogisticNameListAdapter;



public class SelectLogisticsNameDialog {
	public Dialog dialog;
	private View view;
	private ListView dialog_listview;
	private LayoutInflater inflater;
	private SelectLogisticNameListAdapter adapter;
	private List<String> list;

	
	public SelectLogisticsNameDialog(Context context,List<String> lists) {
		dialog = new Dialog(context, R.style.dialog);
		inflater = LayoutInflater.from(context);		
		view = inflater.inflate(R.layout.select_logistics_name_dialog, null);
		dialog_listview = (ListView) view.findViewById(R.id.dialog_listview);
		this.list = lists;
		list = new ArrayList<String>();
		list.add("申通");
		list.add("圆通");
		list.add("顺丰");
		list.add("京东物流");
		adapter = new SelectLogisticNameListAdapter(list, context);
		dialog_listview.setAdapter(adapter);
		
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		WindowManager m = ((Activity) context).getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);	
		
		dialog_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.selectItem(position);
				
				dismissDialog();
			}
		});
	}
	public String getSelectItem(){	
		return list.get(adapter.getSelectItem());
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
	public void showDialog(){
		dialog.show();
	}
	public Dialog getDialog() {
		return dialog;
	}
	public void setCancelable(boolean is) {
		dialog.setCancelable(is);
	}
	
}
