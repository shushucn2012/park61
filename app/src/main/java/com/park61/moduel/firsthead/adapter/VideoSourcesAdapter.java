package com.park61.moduel.firsthead.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.bean.VideoItemSource;

import java.util.List;

/**
 * 视频详情页-视频列表adapter
 * creatby super on2018/2/1
 */
public class VideoSourcesAdapter extends BaseAdapter {

    private List<VideoItemSource> mList;
    private Context mContext;
    private OnDownLoadClickedLsner mOnDownLoadClickedLsner;
    private long lastClickTime = 0;

    public VideoSourcesAdapter(Context context, List<VideoItemSource> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.video_source_listitem, null);
            holder = new ViewHolder();
            holder.img_bofang_cover = (ImageView) convertView.findViewById(R.id.img_bofang_cover);
            holder.img_bofang_icon = (ImageView) convertView.findViewById(R.id.img_bofang_icon);
            holder.tv_source_title = (TextView) convertView.findViewById(R.id.tv_source_title);
            holder.tv_source_duration = (TextView) convertView.findViewById(R.id.tv_source_duration);
            holder.tv_download_times = (TextView) convertView.findViewById(R.id.tv_download_times);
            holder.img_download_state = (ImageView) convertView.findViewById(R.id.img_download_state);
            holder.linear_bottom = convertView.findViewById(R.id.linear_bottom);
            holder.area_download_state = convertView.findViewById(R.id.area_download_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        VideoItemSource itemSource = mList.get(position);
        holder.tv_source_title.setText(itemSource.getTitle());
        holder.tv_source_duration.setText(itemSource.getTime());
        holder.tv_download_times.setText(itemSource.getViewNum() + "次播放");
        ImageManager.getInstance().displayImg(holder.img_bofang_cover, itemSource.getPicUrl());

        if (itemSource.isPlaying()) {
            holder.img_bofang_icon.setImageResource(R.drawable.gequzanting);
        } else {
            holder.img_bofang_icon.setImageResource(R.drawable.gequcaidan);
        }

        if (itemSource.getStatus() == -1) {//未下载
            holder.img_download_state.setImageResource(R.drawable.xiazai);
        } else if (itemSource.getStatus() == 2) {//下载中
            holder.img_download_state.setImageResource(R.drawable.icon_downloading);
        } else if (itemSource.getStatus() == 1) {//已完成
            holder.img_download_state.setImageResource(R.drawable.icon_hasdown);
        } else {
            holder.img_download_state.setImageResource(R.drawable.xiazai);
        }

        holder.area_download_state.setOnClickListener(v -> {
            if(System.currentTimeMillis() - lastClickTime > 500){
                mOnDownLoadClickedLsner.onCliked(position);
                lastClickTime = System.currentTimeMillis();
            }
        });

        if (position == mList.size() - 1) {
            holder.linear_bottom.setVisibility(View.GONE);
        } else {
            holder.linear_bottom.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_bofang_cover, img_bofang_icon, img_download_state;
        TextView tv_source_title;
        TextView tv_source_duration;
        TextView tv_download_times;
        View linear_bottom, area_download_state;
    }

    public interface OnDownLoadClickedLsner {
        void onCliked(int index);
    }

    public void setOnDownLoadClickedLsner(OnDownLoadClickedLsner listener) {
        this.mOnDownLoadClickedLsner = listener;
    }
}
