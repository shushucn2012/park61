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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.salesafter.adapter.DialogAdapter;
import com.park61.moduel.salesafter.bean.GrfReason;

import java.util.ArrayList;

/**
 * 退货原因选择项的dialog
 * @author Lucia
 *
 */
public class SelectReasonDialog {
	private Dialog dialog;
	private View view;
	private LayoutInflater inflater;
	private Button btn_cancle, btn_ok;
	private TextView tv_title, tv_msg;
	private ListView listView;
	private DialogAdapter adapter;
	int location;
	private ArrayList<GrfReason> reasonList;
	private int selectPos;

	public SelectReasonDialog(int selectPos,final Context context,ArrayList<GrfReason> reasonList) {
		dialog = new Dialog(context, R.style.dialog);
		inflater = LayoutInflater.from(context);
		this.reasonList = reasonList;
		this.selectPos = selectPos;

		view = inflater.inflate(R.layout.select_reason_dialog, null);
		listView = (ListView) view.findViewById(R.id.dialog_listview);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_msg = (TextView) view.findViewById(R.id.tv_msg);

		adapter = new DialogAdapter(selectPos,reasonList, context);
		listView.setAdapter(adapter);
		

		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		WindowManager m = ((Activity) context).getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.selectItem(position);
				location = position;
				adapter.notifyDataSetChanged();
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
		return reasonList.get(adapter.getSelectItem()).getReason();
	}
	public int getType(){
		return reasonList.get(adapter.getSelectItem()).getType();
	}
	public int getSelectId(){
		return location;
	}
	
	public void dismissDialog() {
		dialog.dismiss();
	}

}
