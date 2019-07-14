package com.park61.moduel.address.dialog;


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
import android.widget.TextView;
import com.park61.R;
import com.park61.moduel.acts.bean.CityBean;
import com.park61.moduel.address.adapter.SelectCityDialogListAdapter;

/**
 * 选择城市dialog
 * @author Lucia
 *
 */
public class SelecCityDialog {
	private OnCitySelect mOnCitySelect ;
	private Dialog dialog;
	private View view;
	private LayoutInflater inflater;
	private ListView listView;
	private SelectCityDialogListAdapter adapter;
	private List<CityBean> lists;
	private TextView tv_select;
	
	public SelecCityDialog(Context context,List<CityBean> lists) {
		dialog = new Dialog(context, R.style.dialog);
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.address_select_dialog, null);
		tv_select = (TextView) view.findViewById(R.id.tv_select);
		tv_select.setText("选择城市");
		this.lists = lists;		
		listView = (ListView) view.findViewById(R.id.dialog_listview);		
		adapter = new SelectCityDialogListAdapter(lists, context);
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
					mOnCitySelect.onSelectCity(position);				
					dismissDialog();
			}
		});
	}	
	public void showDialog(){
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
	public interface OnCitySelect{
		public void onSelectCity(int position);
	}

	public void setOnCitySelectLsner(OnCitySelect lsner){
		this.mOnCitySelect = lsner;
	}

}
