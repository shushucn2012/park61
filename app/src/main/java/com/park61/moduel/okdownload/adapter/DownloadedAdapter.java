package com.park61.moduel.okdownload.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.okdownload.PlayPopWin;
import com.park61.moduel.okdownload.db.DownloadTask;
import com.park61.moduel.okdownload.fragment.DownloadedFragment;

import java.util.List;

/**
 * Created by chenlie on 2018/1/26.
 */

public class DownloadedAdapter extends RecyclerView.Adapter<DownloadedAdapter.ViewHolder> {

    private List<DownloadTask> datas;
    private Activity mContext;
    private OnPlayClickedListener mOnPlayClickedListener;
    public int currentPos = -1;

    public DownloadedAdapter(Activity context, List<DownloadTask> tasks) {
        this.mContext = context;
        this.datas = tasks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_downloaded, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DownloadTask task = datas.get(position);
        ImageManager.getInstance().displayImg(holder.icon, task.getTask_icon());
        holder.title.setText(task.getTask_name());
        holder.time.setText(task.getTask_time());
        holder.size.setText(Formatter.formatFileSize(mContext, task.getTask_size()));

        //弹出操作框
        holder.operate.setOnClickListener(v -> {
            Intent it = new Intent(mContext, PlayPopWin.class);
            it.putExtra("position", position);
            mContext.startActivityForResult(it, DownloadedFragment.DOWNLOADED_REFRESH);
        });

        holder.itemView.setOnClickListener(v -> {
           /* String videoPath = datas.get(position).getTask_filePath();
            Intent it = new Intent(mContext, VideoLocalPlayActivity.class);
            it.putExtra("videoPath", videoPath);
            mContext.startActivity(it);*/
            mOnPlayClickedListener.OnPlayClicked(position);
        });

        if(currentPos == position){
            holder.playImg.setImageResource(R.drawable.icon_waiting);
        }else{
            holder.playImg.setImageResource(R.drawable.icon_play);
        }
    }

    public void setCurrentPos(int pos){
        currentPos = pos;
    }

    public int getCurrentPos(){
        return currentPos;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        //播放状态 图标
        ImageView playImg;
        TextView title;
        //下载时间
        TextView time;
        //文件时长
        TextView size;
        //操作图标
        LinearLayout operate;

        public ViewHolder(View v) {
            super(v);
            icon = (RoundedImageView) v.findViewById(R.id.icon);
            playImg = (ImageView) v.findViewById(R.id.play_state);
            title = (TextView) v.findViewById(R.id.title);
            time = (TextView) v.findViewById(R.id.download_time);
            size = (TextView) v.findViewById(R.id.size);
            operate = (LinearLayout) v.findViewById(R.id.operate);
        }
    }

    public interface OnPlayClickedListener {
        void OnPlayClicked(int position);
    }

    public void setOnPlayClickedListener(OnPlayClickedListener listener) {
        this.mOnPlayClickedListener = listener;
    }
}
