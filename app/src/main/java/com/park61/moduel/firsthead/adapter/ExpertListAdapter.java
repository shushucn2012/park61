package com.park61.moduel.firsthead.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.BlogerInfoActivity;
import com.park61.moduel.firsthead.bean.ExpertBean;

import java.util.List;

public class ExpertListAdapter extends BaseAdapter {

    private List<ExpertBean> mList;
    private Context mContext;
    private LayoutInflater factory;
    private OnFocusClickedLsner mOnFocusClickedLsner;

    public ExpertListAdapter(Context _context, List<ExpertBean> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ExpertBean getItem(int position) {
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
            convertView = factory.inflate(R.layout.expertlist_item, null);
            holder = new ViewHolder();
            holder.img_expert = (ImageView) convertView.findViewById(R.id.img_expert);
            holder.tv_expert_name = (TextView) convertView.findViewById(R.id.tv_expert_name);
            holder.tv_expert_funs = (TextView) convertView.findViewById(R.id.tv_expert_funs);
            holder.tv_expert_level = (TextView) convertView.findViewById(R.id.tv_expert_level);
            holder.tv_expert_focus = (TextView) convertView.findViewById(R.id.tv_expert_focus);
            holder.area_content = convertView.findViewById(R.id.area_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ExpertBean expertBean = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_expert, expertBean.getPhoto());
        holder.tv_expert_name.setText(expertBean.getExpertName());
        holder.tv_expert_funs.setText(expertBean.getFansCnt() + "粉丝");
        holder.tv_expert_level.setText(expertBean.getResume());
        if (expertBean.isFocus()) {
            holder.tv_expert_focus.setBackgroundResource(R.drawable.rec_gray_stroke_trans_solid_corner30);
            holder.tv_expert_focus.setTextColor(mContext.getResources().getColor(R.color.g999999));
            holder.tv_expert_focus.setText("已关注");
        } else {
            holder.tv_expert_focus.setBackgroundResource(R.drawable.rec_red_stroke_trans_solid_corner30);
            holder.tv_expert_focus.setTextColor(mContext.getResources().getColor(R.color.com_orange));
            holder.tv_expert_focus.setText("+关注");
        }
        holder.tv_expert_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFocusClickedLsner.onFocus(position);
            }
        });
        holder.area_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, BlogerInfoActivity.class);
                it.putExtra("userId", expertBean.getUserId());
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img_expert;
        TextView tv_expert_name;
        TextView tv_expert_funs;
        TextView tv_expert_level;
        TextView tv_expert_focus;
        View area_content;
    }

    public interface OnFocusClickedLsner {
        void onFocus(int position);
    }

    public void setOnFocusClickedLsner(OnFocusClickedLsner lsner) {
        this.mOnFocusClickedLsner = lsner;
    }

}
