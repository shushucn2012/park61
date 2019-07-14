package com.park61.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlie on 2018/1/30.
 *
 */

public class LocalMusicPlay extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    // 注册意图
    public static final String PLAY_ACTION = "com.park61.music.PLAY_ACTION";
    public static final String PAUSE_ACTION = "com.park61.music.PAUSE_ACTION";
    public static final String NEXT_ACTION = "com.park61.music.NEXT_ACTION";
    public static final String PREVIOUS_ACTION = "com.park61.music.PREVIOUS_ACTION";
    public static final String STOP_ACTION = "com.park61.music.STOP_ACTION";

    private MediaPlayer mMediaPlayer; // 声明播放器
    private List<String> datas = new ArrayList<>();
    private int currentPos = 0;
    private boolean isPause = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        // 根据不同的action，做不同的相应
        String action = intent.getAction();
        //播放
        if (action.equals(PLAY_ACTION)) {
            playMusic();
            //暂停
        } else if (action.equals(PAUSE_ACTION)) {
            pause();
            //下一首
        } else if (action.equals(NEXT_ACTION)) {
            next();
            //前一首
        } else if (action.equals(PREVIOUS_ACTION)) {
            previous();
        } else if(action.equals(STOP_ACTION)){
            stopMusic();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 开始播放
     */
    private void playMusic(){
        if(mMediaPlayer != null && isPause){
            isPause = false;
            mMediaPlayer.start();
        }else{
            play();
        }
    }

    /**
     * 暂停
     */
    private void pause() {
        //暂停音乐播放
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            isPause = true;
            mMediaPlayer.pause();
        }
    }

    /**
     * 结束服务
     */
    private void stopMusic() {
        //暂停音乐播放
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
        }
        stopSelf();
    }

    /**
     * 上一首
     */
    private void previous() {
        //得到前一首的歌曲
        if (currentPos != 0 ) {
            currentPos --;
            //开始播放
            play();
        }
    }

    /**
     * 下一首
     */
    private void next() {
        //得到后一首歌曲
        if (currentPos < datas.size() -1) {
           currentPos ++;
            //开始播放
            play();
        }
    }

    /**
     * 播放音乐
     */
    public void play() {

        mMediaPlayer.reset();
        // 获取歌曲位置
        String dataSource = datas.get(currentPos);

        try {
            // 播放器绑定资源
            mMediaPlayer.setDataSource(dataSource);
            // 播放器准备
            mMediaPlayer.prepareAsync();
            // 播放
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if(mp != null){
            mp.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }
}
