package com.park61.moduel.okdownload.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alivc.player.AliyunErrorCode;
import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.databinding.FragmentDownloadingBinding;
import com.park61.moduel.okdownload.DownloadListActivity;
import com.park61.moduel.okdownload.DownloadService;
import com.park61.moduel.okdownload.MyDownLoadActivity;
import com.park61.moduel.okdownload.adapter.DownloadingAdapter;
import com.park61.moduel.okdownload.db.DownloadDAO;
import com.park61.moduel.okdownload.db.DownloadTask;
import com.park61.moduel.okdownload.widget.SimpleTask;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlie on 2018/1/25.
 *
 * 下载中任务列表
 */

public class DownloadingFragment extends BaseFragment implements DownloadingAdapter.ReDownloadListener {

    public static final String TAG = "DownloadingFragment";
    public static final int DOWNLOADING_REFRESH = 0x0008;
    FragmentDownloadingBinding binding;
    private boolean isPrepare = false;
    private boolean isDownload = true;
    private List<AliyunDownloadMediaInfo> tasks = new ArrayList<>();
    LRecyclerViewAdapter mAdapter;
    private AliyunDownloadManager downloadManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_downloading, container, false);
        parentView = binding.getRoot();
        isPrepare = true;
        downloadManager = AliyunDownloadManager.getInstance(parentActivity);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            loadData();
        }
    }

    private void loadData(){
        if(isPrepare){
            isPrepare = false;
            List<AliyunDownloadMediaInfo> list = downloadManager.getUnfinishedDownload();
            if(list != null){
                for (AliyunDownloadMediaInfo info : list){
                    if (info.getStatus() != AliyunDownloadMediaInfo.Status.Complete ){
                        if(info.getProgress() < 100 && DownloadDAO.getInstance().hasTask(info.getVid())){
                            tasks.add(info);
                        }
                    }else{
                        //下载完成但是本地数据库没存
                        DownloadTask t = DownloadDAO.getInstance().selectTask(info.getVid());
                        if(t!=null && t.getTask_status() != 1){
                            DownloadDAO.getInstance().delete(info.getVid());
                            downloadManager.removeDownloadMedia(info);
                        }
                    }
                }
            }
            DownloadingAdapter adapter = new DownloadingAdapter(getActivity(), tasks);
            adapter.setReDownloadListener(this);
            mAdapter = new LRecyclerViewAdapter(adapter);
            binding.downloadingLv.setAdapter(mAdapter);
            binding.downloadingLv.setLayoutManager(new LinearLayoutManager(getActivity()));
            if(getActivity() instanceof MyDownLoadActivity){
                ((MyDownLoadActivity) getActivity()).setProgressListener(adapter);
            }
            if(tasks.size() == 0){
                binding.lvEmpty.setVisibility(View.VISIBLE);
                binding.manageRylt.setVisibility(View.GONE);
            }else{
                binding.manageRylt.setVisibility(View.VISIBLE);
            }
            binding.downloadingLv.setPullRefreshEnabled(false);
            binding.downloadingLv.setLoadMoreEnabled(false);
        }
    }

    @Override
    public void reDownload(String vid) {
        String sid = DownloadDAO.getInstance().selectSid(vid);
        if(!TextUtils.isEmpty(sid))
            getPlayAuth(sid);
    }

    /**
     * 退出应用重新进入时，重新下载需要凭证
     */
    public void getPlayAuth(String sid) {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.videoPlayAuth;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", sid);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, alister);
    }

    BaseRequestListener alister = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            String vid = jsonResult.optString("videoId");
            String playAuth = jsonResult.optString("videoPlayAuth");
            DownloadTask t = DownloadDAO.getInstance().selectTask(vid);
            if(t != null){
                Intent it = new Intent(getActivity(), DownloadService.class);
                it.putExtra("title", t.getTask_name());
                it.putExtra("sid", FU.paseLong(t.getSourceId()));
                it.putExtra("vid", t.getTask_vid());
                it.putExtra("contentId", FU.paseLong(t.getContentId()));
                it.putExtra("type", t.getTask_type());
                it.putExtra("icon", t.getTask_icon());
                it.putExtra("playAuth", playAuth);
                getActivity().startService(it);
            }
        }
    };

    @Override
    public void initView() {
        
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

        binding.setPauseClick(v -> {
            //切换下载状态
            if(isDownload){
                //全部暂停
                binding.downloadState.setText(getText(R.string.download_all));
                binding.downloadIcon.setImageResource(R.drawable.icon_download_gray);
                downloadManager.stopDownloadMedias(tasks);
            }else{
                //开始下载全部
                binding.downloadIcon.setImageResource(R.drawable.icon_waiting);
                binding.downloadState.setText(getText(R.string.pause_all));
                for (AliyunDownloadMediaInfo info : tasks){
                    int tmp = downloadManager.startDownloadMedia(info);
                    Log.e(TAG, "继续下载:"+tmp);
                    //如果退出应用重新打开app时，tmp不为0无法继续下载，重新添加到service
                    if(tmp != AliyunErrorCode.ALIVC_SUCCESS.getCode()){
                        Log.e(TAG, "error, renew add");
                        reDownload(info.getVid());
                    }
                }
            }
            isDownload = !isDownload;
        });

        binding.setManageClick(v -> {
            //跳转管理界面
            if(tasks.size() > 0){
                //全部暂停
                goDownListActivity();
            }
        });
    }

    private void goDownListActivity(){
        List<SimpleTask> simpleTasks = new ArrayList<>();
        for(AliyunDownloadMediaInfo info : tasks){
            SimpleTask s = new SimpleTask();
            s.setVid(info.getVid());
            s.setTitle(info.getTitle());
            s.setSize(info.getSize());
            simpleTasks.add(s);
        }

        Intent it = new Intent(getActivity(), DownloadListActivity.class);
        it.putExtra("flag", DOWNLOADING_REFRESH);
        it.putExtra("list", (Serializable) simpleTasks);
        getActivity().startActivity(it);
    }

    public void deleteDownload(){
        tasks.clear();
        List<AliyunDownloadMediaInfo> list = downloadManager.getUnfinishedDownload();
        if(list != null){
            for (AliyunDownloadMediaInfo info : list){
                if (info.getStatus() != AliyunDownloadMediaInfo.Status.Complete){
                    if(info.getProgress() < 100 && DownloadDAO.getInstance().hasTask(info.getVid()))
                        tasks.add(info);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        if(tasks.size() == 0){
            binding.lvEmpty.setVisibility(View.VISIBLE);
            binding.manageRylt.setVisibility(View.GONE);
        }else{
            binding.manageRylt.setVisibility(View.VISIBLE);
        }
    }

    public void refresh(){
        if(tasks.size() == 0){
            binding.lvEmpty.setVisibility(View.VISIBLE);
            binding.manageRylt.setVisibility(View.GONE);
        }else{
            binding.manageRylt.setVisibility(View.VISIBLE);
        }
    }
}
