package com.park61.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;


/**
 * 单选按钮弹出框
 *
 * @author super
 */
public class SingleConfirmDialog {

    private Dialog dialog;
    private View view;
    private Button btn_bottom;
    private LayoutInflater inflater;
    private TextView text_msg, text_title;

    public SingleConfirmDialog(Context context) {
        dialog = new Dialog(context, R.style.dialog);
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.single_confirm_dialog_layout, null);
        text_title = (TextView) view.findViewById(R.id.text_title);
        text_msg = (TextView) view.findViewById(R.id.text_msg);
        btn_bottom = (Button) view.findViewById(R.id.btn_bottom);
        dialog.setContentView(view);
        setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager m = ((Activity) context).getWindowManager();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
    }

    /**
     * 自动关闭的提示框
     *
     * @param message
     */
    public void showDialog(String title, String message, String btntxt, View.OnClickListener mlsner) {
        text_title.setText(title);
        text_msg.setText(message);
        btn_bottom.setText(btntxt);
        btn_bottom.setOnClickListener(mlsner);
        showDialog();
    }

    public void showDialog() {
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissDialog() {
        try {
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

    public Dialog getDialog() {
        return dialog;
    }

    public void setCancelable(boolean is) {
        dialog.setCancelable(is);
    }


}
