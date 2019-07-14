package com.park61;

import android.*;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.alivc.player.AliyunErrorCode;
import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.aliyun.vodplayerview.utils.ScreenUtils;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.park61.common.set.Log;
import com.park61.common.tool.ScreenStatusController;
import com.park61.moduel.firsthead.AudioListDetailsActivity;

import java.lang.ref.WeakReference;

import static com.aliyun.vodplayer.media.IAliyunVodPlayer.PlayerState.Completed;
import static com.aliyun.vodplayer.media.IAliyunVodPlayer.PlayerState.Idle;
import static com.aliyun.vodplayer.media.IAliyunVodPlayer.PlayerState.Paused;
import static com.aliyun.vodplayer.media.IAliyunVodPlayer.PlayerState.Started;
import static com.aliyun.vodplayer.media.IAliyunVodPlayer.PlayerState.Stopped;

/**
 * Created by shubei on 2018/1/31.
 */

public abstract class AudioBaseActivity extends BaseActivity {

    public String authInfo;//播放凭证
    public String videoId;//播放id
    public boolean mAutoPlay = true;

    public AliyunVodPlayer aliyunVodPlayer;
    public AliyunPlayAuth mPlayAuth;
    public IAliyunVodPlayer.PlayerState mPlayerState; //用来记录前后台切换时的状态，以供恢复。

    /**
     * 播放
     */
    public void audioStartPlay() {
        mPlayerState = aliyunVodPlayer.getPlayerState();
        if (mPlayerState == Idle || mPlayerState == Stopped || mPlayerState == Completed) {
            if (mPlayAuth != null) {
                aliyunVodPlayer.prepareAsync(mPlayAuth);
            }
        } else if (mPlayerState == Started || mPlayerState == Paused) {
            aliyunVodPlayer.stop();
            aliyunVodPlayer.prepareAsync(mPlayAuth);
        } else {
            aliyunVodPlayer.start();
        }
    }

    public void initVodPlayer() {
        aliyunVodPlayer = new AliyunVodPlayer(this);
        //aliyunVodPlayer.setCirclePlay(true);

        aliyunVodPlayer.setOnPreparedListener(new MyPrepareListener(this));
        aliyunVodPlayer.setOnErrorListener(new MyErrorListener(this));

        aliyunVodPlayer.enableNativeLog();
    }

    public class MyPrepareListener implements IAliyunVodPlayer.OnPreparedListener {

        private WeakReference<AudioBaseActivity> activityWeakReference;

        public MyPrepareListener(AudioBaseActivity skinActivity) {
            activityWeakReference = new WeakReference<AudioBaseActivity>(skinActivity);
        }

        @Override
        public void onPrepared() {
            AudioBaseActivity activity = activityWeakReference.get();
            if (activity != null) {
                logout("=====================MyPrepareListener=========================");
                activity.onPrepared();
            }
        }
    }

    public void onPrepared() {
        if (mAutoPlay) {
            aliyunVodPlayer.start();
        }
    }

    public class MyErrorListener implements IAliyunVodPlayer.OnErrorListener {

        private WeakReference<AudioBaseActivity> activityWeakReference;

        public MyErrorListener(AudioBaseActivity skinActivity) {
            activityWeakReference = new WeakReference<AudioBaseActivity>(skinActivity);
        }

        @Override
        public void onError(int arg0, int arg1, String msg) {
            AudioBaseActivity activity = activityWeakReference.get();
            if (activity != null) {
                logout("=====================MyErrorListener=========================");
                activity.onError(arg0, arg1, msg);
            }
        }
    }

    public void onError(int arg0, int arg1, String msg) {
        if (aliyunVodPlayer != null) {
            aliyunVodPlayer.stop();
        }
        if (arg0 == AliyunErrorCode.ALIVC_ERR_INVALID_INPUTFILE.getCode()) {
            //当播放本地报错4003的时候，可能是文件地址不对，也有可能是没有权限。
            //如果是没有权限导致的，就做一个权限的错误提示。其他还是正常提示：
            int storagePermissionRet = ContextCompat.checkSelfPermission(AudioBaseActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (storagePermissionRet != PackageManager.PERMISSION_GRANTED) {
                showShortToast("没有播放权限");
                return;
            }
        }
        showShortToast("播放失败");
    }

    /**
     * 设置播放源
     */
    public void setPlaySource() {
        //注意过期时间。特别是重播的时候，可能已经过期。所以重播的时候最好重新请求一次服务器。
        AliyunPlayAuth.AliyunPlayAuthBuilder aliyunPlayAuthBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
        aliyunPlayAuthBuilder.setVid(videoId);
        aliyunPlayAuthBuilder.setPlayAuth(authInfo);
        aliyunPlayAuthBuilder.setQuality(IAliyunVodPlayer.QualityValue.QUALITY_ORIGINAL);
        //aliyunVodPlayer.setAuthInfo(aliyunPlayAuthBuilder.build());
        mPlayAuth = aliyunPlayAuthBuilder.build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumePlayerState();//保存播放器的状态，供resume恢复使用。
    }

    @Override
    protected void onStop() {
        super.onStop();
        savePlayerState();//onStop中记录下来的状态，在这里恢复使用
    }

    public void savePlayerState() {
        mPlayerState = aliyunVodPlayer.getPlayerState();
        if (aliyunVodPlayer.isPlaying()) { //然后再暂停播放器
            aliyunVodPlayer.pause();
        }
    }

    public void resumePlayerState() {
        if (mPlayerState == IAliyunVodPlayer.PlayerState.Paused) {
            aliyunVodPlayer.pause();
        } else if (mPlayerState == IAliyunVodPlayer.PlayerState.Started) {
            aliyunVodPlayer.start();
            //pauseBtn.setText(R.string.pause_button);
        }
    }

    @Override
    protected void onDestroy() {
        aliyunVodPlayer.stop();
        aliyunVodPlayer.release();

        super.onDestroy();
    }

}
