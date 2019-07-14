package com.park61.moduel.dreamhouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.dreamhouse.bean.EvaluateItemInfo;

import java.util.ArrayList;

/**
 * 梦想详情页评论
 */
public class DreamEvaluateListAdapter extends BaseAdapter {
    private ArrayList<EvaluateItemInfo> mList;
    private Context mContext;
    private LayoutInflater factory;
//    private OnReplyClickedLsner mOnReplyClickedLsner;


    public DreamEvaluateListAdapter(Context _context, ArrayList<EvaluateItemInfo> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = factory.inflate(R.layout.dream_evaluate_list_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_replay = (TextView) convertView.findViewById(R.id.tv_replay);
            holder.tv_comt_content = (TextView) convertView.findViewById(R.id.tv_comt_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        EvaluateItemInfo itemInfo = mList.get(position);

//        holder.tv_comt_content.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
////                mOnReplyClickedLsner.onComtClicked(ci.getId());
//            }
//        });
        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView tv_name, tv_date, tv_replay, tv_comt_content;
        LinearLayout ll_reply;
    }

//    public interface OnReplyClickedLsner {
//        public void onComtClicked(Long rootId);
////        public void onReplyClicked(Long rootId, Long parentId);
//    }
//
//    public void setOnReplyClickedLsner(OnReplyClickedLsner lsner) {
//        this.mOnReplyClickedLsner = lsner;
//    }

}
