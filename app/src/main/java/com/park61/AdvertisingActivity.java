package com.park61;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.park61.common.tool.ImageManager;

import java.io.File;

public class AdvertisingActivity extends BaseActivity {

    private View loading_root, area_jump;
    private TextView tv_left_sec;
    private int sec = 3;

    private Handler timeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                tv_left_sec.setText(sec-- + "S");
                if (sec >= 0) {
                    timeHandler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    startActivity(new Intent(mContext, MainTabActivity.class));
                    finish();
                }
            }
        }
    };

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_loading);
    }

    @Override
    public void initView() {
        logout("====AdvertisingActivity=initView======");
        loading_root = findViewById(R.id.loading_root);
        area_jump = findViewById(R.id.area_jump);
        tv_left_sec = (TextView) findViewById(R.id.tv_left_sec);
        area_jump.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void initData() {
        String zipFloderPath = Environment.getExternalStorageDirectory()
                .getPath() + "/GHPCacheFolder/firstpageFloder";
        String imgFile = zipFloderPath + "/1.jpg";
        File file = new File(imgFile);
        if (!file.exists()) {
            imgFile = zipFloderPath + "/1.png";
        }
        file = new File(imgFile);
        if (!file.exists()) {
            logout("====AdvertisingActivity=initView11111======");
            startActivity(new Intent(mContext, MainTabActivity.class));
            finish();
        } else {
            Bitmap bp = ImageManager.getInstance().readFileBitMap(imgFile);
            loading_root.setBackgroundDrawable(new BitmapDrawable(getResources(), bp));
            timeHandler.sendEmptyMessageDelayed(0, 1000);
        }
    }

    @Override
    public void initListener() {
        area_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeHandler.removeMessages(0);
                startActivity(new Intent(mContext, MainTabActivity.class));
                finish();
            }
        });
    }

}
