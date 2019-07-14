package com.park61.moduel.okdownload;

import android.content.Intent;
import android.databinding.DataBindingUtil;

import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.databinding.ActivityDownloadListBinding;
import com.park61.moduel.okdownload.adapter.DownListAdapter;
import com.park61.moduel.okdownload.db.DownloadDAO;
import com.park61.moduel.okdownload.fragment.DownloadingFragment;
import com.park61.moduel.okdownload.widget.SimpleTask;

import java.util.List;

/**
 * Created by chenlie on 2018/2/5.
 *
 *  已下载和下载中管理界面
 */

public class DownloadListActivity extends BaseActivity {

    private ActivityDownloadListBinding binding;
    private List<SimpleTask> list;
    private DownListAdapter mAdapter;
    private boolean isSelectAll = true;
    private int refreshFlag = -1;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_download_list);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        refreshFlag = getIntent().getIntExtra("flag", -1);
        list = (List<SimpleTask>) getIntent().getSerializableExtra("list");
        if(list != null){
            mAdapter = new DownListAdapter(this, list);
            binding.lv.setAdapter(mAdapter);

            binding.lv.setOnItemClickListener((parent, view, position, id) ->{
                SimpleTask simpleTask = list.get(position);
                if(simpleTask.isCheck()){
                    simpleTask.setCheck(false);
                }else{
                    simpleTask.setCheck(true);
                }
                mAdapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    public void initListener() {

        binding.complete.setOnClickListener(v -> finish());
        binding.selectAll.setOnClickListener(v -> updateSelect());
        binding.setDeleteData(v -> {
            if(mAdapter != null){
                deleteSelect();
            }
        });
    }

    /**
     * 全选 和 取消全选
     */
    private void updateSelect(){
        if(list != null){
            for(SimpleTask s : list){
                if(isSelectAll){
                    if(!s.isCheck()){
                        s.setCheck(true);
                    }
                }else{
                    if(s.isCheck()){
                        s.setCheck(false);
                    }
                }
            }
            mAdapter.notifyDataSetChanged();

            isSelectAll = !isSelectAll;

            binding.selectAll.setText(isSelectAll ? "全选" : "取消");
        }
    }

    /**
     * 删除所选任务
     */
    private void deleteSelect(){
        showDialog();
        for(int i=list.size()-1; i>=0 ; i--){
            //删除本地数据库
            SimpleTask task = list.get(i);
            if(!task.isCheck()){
                continue;
            }
            DownloadDAO.getInstance().delete(task.getVid());
            //当前界面刷新
            list.remove(i);
            //删除阿里云
            List<AliyunDownloadMediaInfo> infos = downloadManager.getUnfinishedDownload();
            for(AliyunDownloadMediaInfo info : infos){
                if (info.getVid().equals(task.getVid())){
                    downloadManager.removeDownloadMedia(info);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        dismissDialog();

        //可能删除的任务已下载完成，刷新已下载fragment
        Intent it = new Intent();
        it.setAction(MyDownLoadActivity.ACTION_DOWNLOADED);
        sendBroadcast(it);
        if(refreshFlag == DownloadingFragment.DOWNLOADING_REFRESH){
            //刷新下载中列表
            Intent it2 = new Intent();
            it2.setAction(MyDownLoadActivity.ACTION_DOWNLOADING);
            sendBroadcast(it2);
        }
    }
}
