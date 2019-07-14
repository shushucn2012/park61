package com.park61.moduel.firsthead.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.bean.CommentItemBean;

import java.util.List;

/**
 * Created by nieyu on 2017/10/18.
 */

public class CommentAllAdapter extends BaseAdapter {

    private List<CommentItemBean> mList;
    private Context mContext;
    private OnReplyClickedLsner mOnReplyClickedLsner;

    public CommentAllAdapter(Context _context, List<CommentItemBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adpteitem_commentlist, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(mList.get(position).getUserUrl())) {
            ImageManager.getInstance().displayImg(holder.iv_icon, mList.get(position).getUserUrl());
        }
        holder.tv_time.setText(mList.get(position).getCreateTime() + "");
        if (mList.get(position).getReply()) {
            holder.tv_name.setText(mList.get(position).getUserName() + "回复");
            holder.tv_replay.setVisibility(View.VISIBLE);
            holder.tv_replay.setText(mList.get(position).getParentUserName());
            holder.tvContent.setText(mList.get(position).getContent());
        } else {
            holder.tv_name.setText(mList.get(position).getUserName());
            holder.tv_replay.setVisibility(View.GONE);
            holder.tvContent.setText(mList.get(position).getContent());
        }

        holder.llDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnReplyClickedLsner.onComtClicked(view, position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time, tv_replay;
        TextView tvContent;
        LinearLayout llDetail;

        public ViewHolder(View view) {
            tv_replay = (TextView) view.findViewById(R.id.tv_replay);
            llDetail = (LinearLayout) view.findViewById(R.id.llDetail);
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
        }
    }

    public interface OnReplyClickedLsner {
        void onComtClicked(View view, int position);
    }

    public void setOnReplyClickedLsner(CommentAllAdapter.OnReplyClickedLsner lsner) {
        this.mOnReplyClickedLsner = lsner;
    }
}
