package com.park61.widget.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.salesafter.adapter.DialogAdapter;
/**
 * 退货原因选择项的dialog
 * @author Lucia
 *
 */
public class SelectSalesAfterTypeDialog {

	private Dialog dialog;
	private View view;
	private LayoutInflater inflater;
	private Button btn_cancle, btn_ok;
	private TextView tv_title, tv_msg;
	private ListView mList;
	private DialogAdapter adapter;
	private List<String> lists;
	
	
	public SelectSalesAfterTypeDialog(Context context) {
		dialog = new Dialog(context, R.style.dialog);
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.select_reason_dialog, null);
		mList = (ListView) view.findViewById(R.id.dialog_listview);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_msg = (TextView) view.findViewById(R.id.tv_msg);
		

		lists = new ArrayList<String>();  		
		lists.add("退款");
		lists.add("退货/退款");
//		adapter = new DialogAdapter(lists, context);
		mList.setAdapter(adapter);
		

		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		WindowManager m = ((Activity) context).getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);
		
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.selectItem(position);
				
			}

		});
				
	}	
	
	public void showDialog(String title, String message, String leftText,
			String rightText, View.OnClickListener listenerLeft,
			View.OnClickListener listenerRight) {
		tv_title.setText(title);
		tv_msg.setText(message);
		btn_ok.setText(rightText);
		btn_cancle.setText(leftText);
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
		dialog.setCancelable(true);
		dialog.show();
	}
	
	public String getSelectItem(){	
		return lists.get(adapter.getSelectItem());
	}
	
	public void dismissDialog() {
		dialog.dismiss();
	}

}
