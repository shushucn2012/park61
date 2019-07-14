package com.park61.moduel.shophome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.shophome.bean.CommentInfo;

import java.util.List;

/**
 * Created by HP on 2017/3/8.
 */
public class ShopCommentListAdapter extends BaseAdapter {
    private List<CommentInfo> mList;
    private Context mContext;
    private LayoutInflater factory;

    public ShopCommentListAdapter(Context _context, List<CommentInfo> _list){
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
            convertView = factory.inflate(R.layout.shop_comment_list_item, null);
            holder = new ViewHolder();
            holder.item_area = convertView.findViewById(R.id.item_area);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_evaluate_date = (TextView) convertView.findViewById(R.id.tv_evaluate_date);
            holder.tv_replay = (TextView) convertView.findViewById(R.id.tv_replay);
            holder.tv_comt_content = (TextView) convertView.findViewById(R.id.tv_comt_content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommentInfo itemInfo = mList.get(position);
        ImageManager.getInstance().displayCircleImg(holder.img, itemInfo.getUserKinship().getPictureUrl());
        holder.tv_name.setText(itemInfo.getUserKinship().getPetName());
        holder.tv_evaluate_date.setText(itemInfo.getShowCreateTime());
//        holder.tv_evaluate_date.setText(toPDateStr(System.currentTimeMillis(), itemInfo.getCreateDate()));
//        if ((itemInfo.isReply() == true)) {
//            holder.tv_replay.setVisibility(View.VISIBLE);
//            holder.tv_replay.setText("回复  " + itemInfo.getParentUserName() + ":");
//        } else {
//            holder.tv_replay.setVisibility(View.GONE);
//        }
        if(itemInfo.getOfficeContentComment()==null){//评论
            holder.tv_replay.setVisibility(View.GONE);
            holder.tv_comt_content.setText(itemInfo.getContent());
        }else{//评论的回复
            holder.tv_replay.setVisibility(View.VISIBLE);
            holder.tv_replay.setText("回复 "+itemInfo.getOfficeContentComment().getUserKinship().getPetName()+"：");
            holder.tv_comt_content.setText(itemInfo.getOfficeContentComment().getContent());
        }
        return convertView;
    }
    class ViewHolder{
        ImageView img;
        TextView tv_name, tv_evaluate_date, tv_replay, tv_comt_content;
        View item_area;
    }
    /**
     * 时间个性化转换
     *
     * @param currDate   当前时间
     * @param covertDate 发布时间
     * @return 转化后的字符串
     */
    @SuppressWarnings("deprecation")
    public String toPDateStr(long currDate, String covertDate) {
        int d_minutes, d_hours, d_days;
        long currcovertDate = Long.parseLong(covertDate);
        long d;
        String result;
        d = (currDate - currcovertDate) / 1000;
        d_days = (int) (d / 86400);
        d_hours = (int) (d / 3600);
        d_minutes = (int) (d / 60);
        if (d_days <= 0 && d_hours > 0) {
            result = d_hours + "小时前";
        } else if (d_hours <= 0 && d_minutes > 0) {
            result = d_minutes + "分钟前";
        } else if (d_minutes <= 0 && d >= 0) {
            return Math.round(d) + "秒前";
        } else {
            result = DateTool.L2S(covertDate);
        }
        return result;
    }
}
