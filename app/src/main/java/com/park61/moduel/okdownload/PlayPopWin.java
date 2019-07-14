package com.park61.moduel.okdownload;

import android.content.Intent;
import android.databinding.DataBindingUtil;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.databinding.ActivityPlayPopBinding;

/**
 * Created by chenlie on 2018/1/29.
 *
 *  音视频下载管理单条记录操作界面
 */

public class PlayPopWin extends BaseActivity {

    ActivityPlayPopBinding binding;
    int position = -1;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_pop);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        //接收下载管理界面传递的参数
        position = getIntent().getIntExtra("position", -1);
    }

    @Override
    public void initListener() {
        binding.play.setOnClickListener(v -> {
            //跳转详情页播放
            Intent it = new Intent();
            it.putExtra("operate", "play");
            it.putExtra("position", position);
            setResult(RESULT_OK, it);
            finish();
        });

        binding.delete.setOnClickListener(v -> {
            //删除
            Intent it = new Intent();
            it.putExtra("operate", "delete");
            it.putExtra("position", position);
            setResult(RESULT_OK, it);
            finish();
        });

        binding.cancel.setOnClickListener(v -> finish());
    }


}
