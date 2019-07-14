package com.park61.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.QRCodeCreator;
import com.park61.widget.circleimageview.GlideCircleTransform;

/**
 * 二维码邀请dialog
 */
public class QRcodeInviteDialog {
    public Dialog dialog;
    private View view;
    private LayoutInflater inflater;
    private String shareUrl;
    public QRcodeInviteDialog(Context context,String shareUrl){
        this.shareUrl = shareUrl;
        dialog = new Dialog(context, R.style.dialog);
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.invite_qr_code, null);
        ImageView img_close = (ImageView) view.findViewById(R.id.img_close);
        ImageView img_me = (ImageView) view.findViewById(R.id.img_me);
        TextView tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
        ImageView img_qrcode = (ImageView) view.findViewById(R.id.img_qrcode);

        Glide.with(context).load(GlobalParam.currentUser.getPictureUrl()).placeholder(R.drawable.pintuanicon)
                .transform(new GlideCircleTransform(context)).into(img_me);

        tv_nickname.setText(GlobalParam.currentUser.getPetName());
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
        QRCodeCreator.createQRImage(shareUrl, DevAttr.dip2px(context, 200),
                DevAttr.dip2px(context, 200), img_qrcode);

        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) (d.getWidth() * 0.75); // 宽度设置为屏幕的0.65
        p.height = (int) (d.getHeight() * 0.7);
        dialogWindow.setAttributes(p);
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
