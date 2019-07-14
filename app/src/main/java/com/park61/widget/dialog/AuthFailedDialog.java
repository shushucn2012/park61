package com.park61.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.me.AuthFirstActivity;

/**
 * 双选按钮弹出框 by super
 */
public class AuthFailedDialog {
    private Dialog dialog;
    private View view;
    private ImageView img_close;
    private TextView tv_failed_info;
    private Button btn_redo;
    private LayoutInflater inflater;

    public AuthFailedDialog(final Context context, String failedInfo) {
        dialog = new Dialog(context, R.style.dialog);
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.authfailed_dialog_layout, null);
        img_close = (ImageView) view.findViewById(R.id.img_close);
        tv_failed_info = (TextView) view.findViewById(R.id.tv_failed_info);
        btn_redo = (Button) view.findViewById(R.id.btn_redo);
        tv_failed_info.setText(failedInfo);
        dialog.setContentView(view);
        setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

        img_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        btn_redo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                context.startActivity(new Intent(context, AuthFirstActivity.class));
            }
        });
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
