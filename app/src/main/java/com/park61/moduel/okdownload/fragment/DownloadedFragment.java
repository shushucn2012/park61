package com.park61.moduel.okdownload.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.AudioBaseFragment;
import com.park61.R;
import com.park61.databinding.FragmentDownloadedBinding;
import com.park61.moduel.firsthead.VideoLocalPlayActivity;
import com.park61.moduel.okdownload.DownloadListActivity;
import com.park61.moduel.okdownload.adapter.DownloadedAdapter;
import com.park61.moduel.okdownload.db.DownloadDAO;
import com.park61.moduel.okdownload.db.DownloadTask;
import com.park61.moduel.okdownload.widget.SimpleTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlie on 2018/1/25.
 * <p>
 * 已下载列表
 */

public class DownloadedFragment extends AudioBaseFragment {

    public static final int DOWNLOADED_REFRESH = 0x0007;
    FragmentDownloadedBinding binding;
    private List<DownloadTask> datas = new ArrayList<>();
    private List<DownloadTask> videos = new ArrayList<>();
    private List<DownloadTask> audios = new ArrayList<>();
    LRecyclerViewAdapter mAdapter;
    DownloadedAdapter adapter;
    public boolean isLeft = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_downloaded, container, false);
        parentView = binding.getRoot();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        binding.audioTv.setTextColor(Color.parseColor("#ff5a80"));
    }

    @Override
    public void initData() {

        //分为音频和视频两个列表
        seperatData();
        //初始显示音频列表
        datas.addAll(audios);
        adapter = new DownloadedAdapter(getActivity(), datas);
        adapter.setOnPlayClickedListener(new DownloadedAdapter.OnPlayClickedListener() {
            @Override
            public void OnPlayClicked(int position) {
                play(position);
            }
        });
        mAdapter = new LRecyclerViewAdapter(adapter);
        binding.downloadedLv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.downloadedLv.setAdapter(mAdapter);
    }

    private void seperatData() {
        List<DownloadTask> temp = DownloadDAO.getInstance().selectAllComplete();
        for (DownloadTask task : temp) {
            if (task.getTask_type() == 0) {
                //视频
                videos.add(task);
            } else if (task.getTask_type() == 1) {
                //音频
                audios.add(task);
            }
        }
    }

    @Override
    public void initListener() {
        binding.downloadedLv.setPullRefreshEnabled(false);
        setListNum(audios.size());
        //点击事件
        binding.audioTv.setOnClickListener(v -> {
            if(!isLeft){
                //点击音频
                isLeft = true;
                binding.audioTv.setTextColor(Color.parseColor("#ff5a80"));
                binding.videoTv.setTextColor(Color.parseColor("#222222"));
                setListNum(audios.size());
                datas.clear();
                datas.addAll(audios);
                mAdapter.notifyDataSetChanged();
            }
        });

        binding.videoTv.setOnClickListener(v -> {
            if(isLeft){
                isLeft = false;
                if (aliyunVodPlayer != null && aliyunVodPlayer.isPlaying()) {
                    aliyunVodPlayer.pause();
                }
                adapter.setCurrentPos(-1);
                binding.audioTv.setTextColor(Color.parseColor("#222222"));
                binding.videoTv.setTextColor(Color.parseColor("#ff5a80"));
                setListNum(videos.size());
                datas.clear();
                datas.addAll(videos);
                mAdapter.notifyDataSetChanged();
            }
        });

        binding.setManageClick(v -> {
            if (datas.size() > 0) {
                goDownListActivity();
            }
        });
    }

    /**
     * ... 弹窗后删除 点击项
     */
    public void delete(int position) {
        if (position != -1) {
            //数据库删除
            String task_vid = datas.get(position).getTask_vid();
            DownloadDAO.getInstance().delete(task_vid);
            if (isLeft) {
                audios.remove(position);
            } else {
                videos.remove(position);
            }
            datas.remove(position);
            setListNum(datas.size());
            mAdapter.notifyDataSetChanged();
            List<AliyunDownloadMediaInfo> infos = AliyunDownloadManager.getInstance(getActivity()).getUnfinishedDownload();
            for (AliyunDownloadMediaInfo info : infos) {
                if (info.getVid().equals(task_vid)) {
                    AliyunDownloadManager.getInstance(getActivity()).removeDownloadMedia(info);
                }
            }
        }
    }

    /**
     * 弹窗后 点击播放
     *
     * @param position 播放位置
     */
    public void play(int position) {
        if (isLeft) {
            if (aliyunVodPlayer == null) {
                aliyunVodPlayer = new AliyunVodPlayer(parentActivity);
                aliyunVodPlayer.setOnPreparedListener(new IAliyunVodPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared() {
                        aliyunVodPlayer.start();
                    }
                });
                aliyunVodPlayer.setOnCompletionListener(new IAliyunVodPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion() {
                        adapter.setCurrentPos(-1);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
            if(adapter.getCurrentPos() == position){
                //暂停当前
                if(aliyunVodPlayer.isPlaying()){
                    adapter.setCurrentPos(-1);
                    aliyunVodPlayer.pause();
                }
            }else{
                if(aliyunVodPlayer.getPlayerState() == IAliyunVodPlayer.PlayerState.Paused){
                    aliyunVodPlayer.start();
                }else{
                    String videoPath = datas.get(position).getTask_filePath();
                    AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
                    alsb.setSource(videoPath);
                    AliyunLocalSource localSource = alsb.build();
                    audioStartPlayLocal(localSource);
                }
                adapter.setCurrentPos(position);
            }
            mAdapter.notifyDataSetChanged();
        } else {
            String videoPath = datas.get(position).getTask_filePath();
            Intent it = new Intent(getActivity(), VideoLocalPlayActivity.class);
            it.putExtra("videoPath", videoPath);
            startActivity(it);
        }
    }

    private void setListNum(int num) {
        if (num > 0) {
            //隐藏缺省图
            binding.lvEmpty.setVisibility(View.GONE);
        } else {
            //显示缺省图
            binding.lvEmpty.setVisibility(View.VISIBLE);
        }
        binding.numTv.setText("共" + num + "个");
    }

    /**
     * 跳管理界面
     */
    private void goDownListActivity() {
        List<SimpleTask> simpleTasks = new ArrayList<>();
        for (DownloadTask t : datas) {
            SimpleTask s = new SimpleTask();
            s.setVid(t.getTask_vid());
            s.setTitle(t.getTask_name());
            s.setSize(t.getTask_size());
            simpleTasks.add(s);
        }

        Intent it = new Intent(getActivity(), DownloadListActivity.class);
        it.putExtra("flag", DOWNLOADED_REFRESH);
        it.putExtra("list", (Serializable) simpleTasks);
        getActivity().startActivity(it);
    }

    /**
     * 管理界面删除后刷新界面
     */
    public void refresh() {
        datas.clear();
        audios.clear();
        videos.clear();
        seperatData();
        if (isLeft) {
            datas.addAll(audios);
            setListNum(audios.size());
        } else {
            datas.addAll(videos);
            setListNum(videos.size());
        }
        mAdapter.notifyDataSetChanged();
    }

}
