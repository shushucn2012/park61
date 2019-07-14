package com.park61.moduel.acts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.DateTool;
import com.park61.moduel.acts.bean.CourseBean;

import java.util.List;

public class CourseListAdapter extends BaseAdapter {

    private List<CourseBean> mList;
    private Context mContext;
    private boolean isFromOrder = false; //是否来自订单

    public CourseListAdapter(Context _context, List<CourseBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    public CourseListAdapter(Context _context, List<CourseBean> _list, boolean isFromOrder) {
        this.mList = _list;
        this.mContext = _context;
        this.isFromOrder = true;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CourseBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.course_list_item, null);
            holder = new ViewHolder();
            holder.tv_sort = (TextView) convertView.findViewById(R.id.tv_sort);
            holder.tv_act_date = (TextView) convertView.findViewById(R.id.tv_act_date);
            holder.tv_course_state = (TextView) convertView.findViewById(R.id.tv_course_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CourseBean cb = mList.get(position);
        holder.tv_sort.setText("第" + (cb.getSort() + 1) + "节");
        String dateStr = DateTool.L2SByDay2(cb.getActStartTime()) + " - " + DateTool.L2SByDay2(cb.getActEndTime());
        holder.tv_act_date.setText(dateStr);
        if (cb.isEnd()) {
            holder.tv_sort.setTextColor(mContext.getResources().getColor(R.color.g666666));
            holder.tv_act_date.setTextColor(mContext.getResources().getColor(R.color.g666666));
            holder.tv_course_state.setTextColor(mContext.getResources().getColor(R.color.g666666));
            holder.tv_course_state.setText("已结束");
        } else {
            holder.tv_sort.setTextColor(mContext.getResources().getColor(R.color.g333333));
            holder.tv_act_date.setTextColor(mContext.getResources().getColor(R.color.g333333));
            holder.tv_course_state.setTextColor(mContext.getResources().getColor(R.color.g333333));
            if (!isFromOrder) { //非订单详情
                holder.tv_course_state.setText("可报名");
            } else { //订单详情中
                Log.out("getActEndTime======"+Long.parseLong(cb.getActEndTime()));
                Log.out("System.currentTimeMillis()======"+System.currentTimeMillis());
                if(Long.parseLong(cb.getActEndTime()) < System.currentTimeMillis()){// 小于当前时间
                    holder.tv_course_state.setText("已参加");
                } else { // 大于当前时间
                    holder.tv_course_state.setText("待参加");
                }
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_sort;
        TextView tv_act_date;
        TextView tv_course_state;
    }


}
