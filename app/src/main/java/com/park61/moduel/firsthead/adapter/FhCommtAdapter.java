package com.park61.moduel.firsthead.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.dreamhouse.bean.EvaluateItemInfo;

import java.util.List;

/**
 * Created by shubei on 2017/6/12.
 */

public class FhCommtAdapter extends BaseAdapter {

    private List<EvaluateItemInfo> mList;
    private Context mContext;
    private LayoutInflater factory;
    private OnReplyClickedLsner mOnReplyClickedLsner;

    public FhCommtAdapter(Context _context, List<EvaluateItemInfo> _list, OnReplyClickedLsner lsner) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
        mOnReplyClickedLsner = lsner;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = factory.inflate(R.layout.fh_commt_list_item, null);
            holder = new ViewHolder();
            holder.item_area = convertView.findViewById(R.id.item_area);
            holder.area_commt_content =  convertView.findViewById(R.id.area_commt_content);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_evaluate_date = (TextView) convertView.findViewById(R.id.tv_evaluate_date);
            holder.tv_replay = (TextView) convertView.findViewById(R.id.tv_replay);
            holder.tv_comt_content = (TextView) convertView.findViewById(R.id.tv_comt_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final EvaluateItemInfo item = mList.get(position);
        ImageManager.getInstance().displayCircleImg(holder.img, item.getUserUrl());
        holder.tv_name.setText(item.getUserName());
        holder.tv_evaluate_date.setText(DateTool.toPDateStr(System.currentTimeMillis(), Long.parseLong(item.getCreateDate())));
        holder.tv_comt_content.setText(item.getContent());
        if (!TextUtils.isEmpty(item.getParentUserName())) {
            holder.tv_replay.setVisibility(View.VISIBLE);
            holder.tv_replay.setText("回复:" + item.getParentUserName() );
        } else {
            holder.tv_replay.setVisibility(View.GONE);
        }
        holder.area_commt_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnReplyClickedLsner.onComtClicked(item.getId());
            }
        });
        return convertView;
    }


    class ViewHolder {
        ImageView img;
        TextView tv_name, tv_evaluate_date, tv_replay, tv_comt_content;
        View item_area, area_commt_content;
    }

    public interface OnReplyClickedLsner {
        void onComtClicked(long parentId);
    }
}
