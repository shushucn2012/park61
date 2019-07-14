package com.park61.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.R;

public class SkeyBoardPay extends LinearLayout {

    private View k1, k2, k3, k4, k5, k6, k7, k8, k9, k0, k00, kp, kok;// 按键
    private ImageButton khid, kdel;// 隐藏和删除
    private EditText currentEt;// 当前关联Edit
    private View keyboard_layout;
    private OnKeyClickedListener listener;

    public SkeyBoardPay(Context context) {
        super(context);
    }

    public SkeyBoardPay(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.keyboard_pay_layout, this);
        initKeyBoardView();
        initKeyLsner();
    }

    private void initKeyBoardView() {
        keyboard_layout = findViewById(R.id.keyboard_layout);
//		hideKeyboard();
        k1 = findViewById(R.id.k1);
        k2 = findViewById(R.id.k2);
        k3 = findViewById(R.id.k3);
        k4 = findViewById(R.id.k4);
        k5 = findViewById(R.id.k5);
        k6 = findViewById(R.id.k6);
        k7 = findViewById(R.id.k7);
        k8 = findViewById(R.id.k8);
        k9 = findViewById(R.id.k9);
        k0 = findViewById(R.id.k0);
//		k00 = findViewById(R.id.k00);
        kp = findViewById(R.id.kp);
//		kok = findViewById(R.id.kok);
//		khid = (ImageButton) findViewById(R.id.khid);
        kdel = (ImageButton) findViewById(R.id.kdel);
    }

    public void attachEdit(EditText et) {
        this.currentEt = et;
//		kok.setOnClickListener(lsner);
    }

    private void initKeyLsner() {
//		kok.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				 hideKeyboard();
//			}
//		});
//		khid.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				 hideKeyboard();
//			}
//		});
        kdel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (currentEt != null) {
                    int gb_position = currentEt.getSelectionStart();
                    if (gb_position > 0)
                        currentEt.getText().delete(gb_position - 1, gb_position);
                    if(listener!=null) {
                        listener.onClicked();
                    }
                }
            }
        });
        /*kdel.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if(currentEt != null){
					int gb_position = currentEt.getSelectionStart();
					if (gb_position > 0)
						currentEt.getText().delete(0, gb_position);
				}
				return false;
			}
		});*/
        k1.setOnClickListener(keyBoardPressLsner);
        k2.setOnClickListener(keyBoardPressLsner);
        k3.setOnClickListener(keyBoardPressLsner);
        k4.setOnClickListener(keyBoardPressLsner);
        k5.setOnClickListener(keyBoardPressLsner);
        k6.setOnClickListener(keyBoardPressLsner);
        k7.setOnClickListener(keyBoardPressLsner);
        k8.setOnClickListener(keyBoardPressLsner);
        k9.setOnClickListener(keyBoardPressLsner);
        k0.setOnClickListener(keyBoardPressLsner);
//		k00.setOnClickListener(keyBoardPressLsner);
        kp.setOnClickListener(keyBoardPressLsner);
    }

    private OnClickListener keyBoardPressLsner = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (currentEt != null) {
                if (currentEt.getText().length() >= 6) {
                    return;
                }
                String insertStr = ((TextView) v).getText().toString();
                if (insertStr.equals(".") && currentEt.getText().toString().indexOf(".") > -1) {
                    return;
                }
                int gb_position = currentEt.getSelectionStart();
                currentEt.getText().insert(gb_position, insertStr);
                if(listener!=null) {
                    listener.onClicked();
                }
            }
        }
    };

    public void showKeyboard() {
        keyboard_layout.setVisibility(View.VISIBLE);
    }

    public void hideKeyboard() {
        keyboard_layout.setVisibility(View.GONE);
    }

    public interface OnKeyClickedListener {
        public void onClicked();
    }

    public void setOnKeyClickedListener(OnKeyClickedListener listener) {
        this.listener = listener;
    }

}
