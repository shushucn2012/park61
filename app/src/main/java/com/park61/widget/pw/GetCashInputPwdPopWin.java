package com.park61.widget.pw;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.park61.R;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.accountsafe.ResetTradePwdActivity;
import com.park61.widget.SkeyBoardPay;

import java.lang.reflect.Method;

/**
 *取现输入交易密码
 * @author super
 */
public class GetCashInputPwdPopWin extends PopupWindow {

    private Context mContext;
    private OnConfirmListener listener;
    private View toolView, mcover, img_close_cover, tv_forget_pwd;
    private SkeyBoardPay keyboard_panel;
    private EditText edit_trade_pwd;
    private TextView tv_confirm;

    public GetCashInputPwdPopWin(Context context, OnConfirmListener lsner, View cover) {
        super(context);
        mContext = context;
        this.mcover = cover;
        this.listener = lsner;
        LayoutInflater inflater = LayoutInflater.from(context);
        toolView = inflater.inflate(R.layout.pw_getcash_inputpwd, null);

        // 初始化视图
        mcover.setVisibility(View.VISIBLE);
        img_close_cover = toolView.findViewById(R.id.img_close_cover);
        edit_trade_pwd = (EditText) toolView.findViewById(R.id.edit_trade_pwd);
        keyboard_panel = (SkeyBoardPay) toolView.findViewById(R.id.keyboard_panel);
        keyboard_panel.showKeyboard();
        hideOldSysBoard(edit_trade_pwd);
        keyboard_panel.attachEdit(edit_trade_pwd);
        tv_forget_pwd = toolView.findViewById(R.id.tv_forget_pwd);
        tv_confirm = (TextView) toolView.findViewById(R.id.tv_confirm);

        // 设置SelectPicPopupWindow的View
        this.setContentView(toolView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(DevAttr.getScreenWidth(mContext));
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight((int) (DevAttr.getScreenHeight(mContext) * 0.6));
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(null);
        this.setAnimationStyle(R.style.AnimBottom);
        img_close_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                mcover.setVisibility(View.GONE);
            }
        });
        tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ResetTradePwdActivity.class));
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = edit_trade_pwd.getText().toString();
                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(mContext, "密码没有输入！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwd.length() != 6){
                    Toast.makeText(mContext, "密码不足6位！", Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onConfirm(pwd);
            }
        });
    }

    public interface OnConfirmListener {
        public void onConfirm(String mpwd);
    }

    public void hideOldSysBoard(EditText et) {
        Class<EditText> cls = EditText.class;
        try {
            Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setShowSoftInputOnFocus.setAccessible(false);
            setShowSoftInputOnFocus.invoke(et, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
