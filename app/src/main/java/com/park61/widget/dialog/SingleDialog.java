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
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;


/**
 * 单选按钮弹出框
 *
 * @author super
 */
public class SingleDialog {

    private Dialog dialog;
    private View view;
    private LayoutInflater inflater;
    private TextView text_msg;
    private ImageView img_title;
    private Handler mHandler = new Handler();
    private OnDisLsner lsner;

    public SingleDialog(Context context) {
        dialog = new Dialog(context, R.style.dialog);
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.single_dialog_layout, null);
        img_title = (ImageView) view.findViewById(R.id.img_title);
        text_msg = (TextView) view.findViewById(R.id.text_msg);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager m = ((Activity) context).getWindowManager();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        p.height = (int) (d.getHeight() * 0.3); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.6); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
    }

    public SingleDialog(Context context, int width, int height) {
        dialog = new Dialog(context, R.style.dialog);
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.single_dialog_layout, null);
        img_title = (ImageView) view.findViewById(R.id.img_title);
        text_msg = (TextView) view.findViewById(R.id.text_msg);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager m = ((Activity) context).getWindowManager();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        //p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        //p.width = (int) (d.getWidth() * 0.6); // 宽度设置为屏幕的0.65
        p.height = height;
        p.width = width;
        dialogWindow.setAttributes(p);
    }

    /**
     * 自动关闭的提示框
     *
     * @param message
     */
    public void showDialog(String message) {
        showDialog(message, false);
    }

    /**
     * 自动关闭的提示框
     *
     * @param message
     */
    public void showDialog(String message, boolean isShowImg) {
        if (isShowImg) {
            img_title.setVisibility(View.VISIBLE);
        } else {
            img_title.setVisibility(View.GONE);
        }
        text_msg.setText(message);
        dialog.show();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                dismissDialog();
                if(lsner != null){
                    lsner.onDismiss();
                }
            }
        }, 2 * 1000);
    }

    /**
     * 自动关闭的提示框
     *
     * @param message
     */
    public void showDialog(String message, OnDisLsner mlsner) {
        this.lsner = mlsner;
        text_msg.setText(message);
        dialog.show();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                dismissDialog();
                lsner.onDismiss();
            }
        }, 2 * 1000);
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

    public interface OnDisLsner {
        public void onDismiss();
    }

    public void setOnDisLsner(OnDisLsner mlsner){
        this.lsner = mlsner;
    }

}
