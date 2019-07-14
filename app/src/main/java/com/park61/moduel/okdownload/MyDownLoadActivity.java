package com.park61.moduel.okdownload;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.park61.BaseActivity;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.databinding.ActivityDownloadBinding;
import com.park61.moduel.okdownload.fragment.DownloadedFragment;
import com.park61.moduel.okdownload.fragment.DownloadingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlie on 2018/1/25.
 *
 */

public class MyDownLoadActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    public static final String ACTION_DOWNLOADED = "downloaded_refresh";
    public static final String ACTION_DOWNLOADING = "downloading_refresh";
    public static final String ACTION_COMPLETE = "downloading_complete";
    ActivityDownloadBinding binding;
    List<BaseFragment> fragmentList;
    DownloadedFragment downloadedFragment;
    DownloadingFragment downloadingFragment;
    boolean isLeft = true;
    DownloadService.MyBinder myBinder;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_download);
    }

    @Override
    public void initView() {
        changeTab(true);
    }

    @Override
    public void initData() {
        fragmentList = new ArrayList<>();
        downloadedFragment = new DownloadedFragment();
        downloadingFragment = new DownloadingFragment();
        fragmentList.add(downloadedFragment);
        fragmentList.add(downloadingFragment);

        binding.viewpager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
        binding.viewpager.addOnPageChangeListener(this);
        binding.viewpager.setCurrentItem(0);
    }

    @Override
    public void initListener() {
        binding.setLeftClick(v->{
            if(!isLeft){
                isLeft = true;
                changeTab(isLeft);
                binding.viewpager.setCurrentItem(0);
            }
        });

        binding.setRightClick(v -> {
            if(isLeft){
                isLeft = false;
                changeTab(isLeft);
                binding.viewpager.setCurrentItem(1);
            }
        });

        //绑定service获取回调
        bindService(new Intent(this, DownloadService.class), mConnection, BIND_AUTO_CREATE);
        //注册列表管理界面刷新广播
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction(ACTION_DOWNLOADED);
        itFilter.addAction(ACTION_DOWNLOADING);
        itFilter.addAction(ACTION_COMPLETE);
        registerReceiver(refreshReceiver, itFilter);
    }

    ServiceConnection mConnection = new  ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (DownloadService.MyBinder) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 管理界面删除后发出 刷新fragment 广播
     */
    BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(ACTION_DOWNLOADED.equals(action)){
                //刷新已下载
                downloadedFragment.refresh();
            }else if(ACTION_DOWNLOADING.equals(action)){
                //刷新下载中
                downloadingFragment.deleteDownload();
            }else if(ACTION_COMPLETE.equals(action)){
                //下载完成后刷新界面
                downloadedFragment.refresh();
                downloadingFragment.refresh();
            }
        }
    };

    /**
     * 设置service监听
     */
    public void setProgressListener(DownloadService.ProgressListener listener){
        if(myBinder != null){
            myBinder.setDownloadingListener(listener);
        }
    }

    /**
     * viewpager 滑动页面监听
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        isLeft = position==0;
        changeTab(isLeft);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * viewpager adapter
     */
    private class MyFragmentAdapter extends FragmentPagerAdapter{

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    /**
     * @param isLeft 是否点击标题左边
     *
     */
    private void changeTab(boolean isLeft){
        if(isLeft){
            //已下载
            binding.line1.setVisibility(View.VISIBLE);
            binding.line2.setVisibility(View.GONE);
            binding.leftTv.setTextColor(Color.parseColor("#ff5a80"));
            binding.rightTv.setTextColor(Color.parseColor("#222222"));
        }else{
            //下载中
            binding.leftTv.setTextColor(Color.parseColor("#222222"));
            binding.rightTv.setTextColor(Color.parseColor("#ff5a80"));
            binding.line2.setVisibility(View.VISIBLE);
            binding.line1.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DownloadedFragment.DOWNLOADED_REFRESH){
            if(data != null){
                if("delete".equals(data.getStringExtra("operate"))){
                    //删除
                    downloadedFragment.delete(data.getIntExtra("position", -1));
                }else if("play".equals(data.getStringExtra("operate"))){
                    //播放
                    downloadedFragment.play(data.getIntExtra("position", -1));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑服务
        unbindService(mConnection);
        //解注册广播
        unregisterReceiver(refreshReceiver);
    }
}
