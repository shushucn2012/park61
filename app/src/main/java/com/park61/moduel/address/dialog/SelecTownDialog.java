package com.park61.moduel.address.dialog;


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
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.address.adapter.SelectTownDialogListAdapter;
import com.park61.moduel.address.bean.TownBean;

import java.util.List;


/**
 * 选择乡镇dialog
 * @author Lucia
 *
 */
public class SelecTownDialog {
	private OnTownSelect mOnTownSelect ;

	private Dialog dialog;
	private View view;
	private LayoutInflater inflater;
	private ListView listView;
	private SelectTownDialogListAdapter adapter;
	private List<TownBean> lists;
	private TextView tv_select;

	public SelecTownDialog(Context context, List<TownBean> lists) {
		dialog = new Dialog(context, R.style.dialog);
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.address_select_dialog, null);
		tv_select = (TextView) view.findViewById(R.id.tv_select);
		tv_select.setText("选择乡镇");
		listView = (ListView) view.findViewById(R.id.dialog_listview);
		adapter = new SelectTownDialogListAdapter(lists,context);
		listView.setAdapter(adapter);
		
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		WindowManager m = ((Activity) context).getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
		p.height = (int) (d.getHeight() * 0.8);
		dialogWindow.setAttributes(p);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mOnTownSelect.onTownSelect(position);
				dismissDialog();
			}
		});
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
	public interface OnTownSelect{
		public void onTownSelect(int position);
	}
	public void setOnTownSelectLsner(OnTownSelect lsner) {
		this.mOnTownSelect = lsner;
	}

}
