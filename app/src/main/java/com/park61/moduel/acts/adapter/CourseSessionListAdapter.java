package com.park61.moduel.acts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.bean.ActItem;
import com.park61.moduel.acts.bean.CourseSessionBean;

import java.util.List;

public class CourseSessionListAdapter extends BaseAdapter {

    private List<CourseSessionBean> mList;
    private Context mContext;
    private boolean needBg;

    public CourseSessionListAdapter(Context _context, List<CourseSessionBean> _list, boolean needBg) {
        this.mList = _list;
        this.mContext = _context;
        this.needBg = needBg;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CourseSessionBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.coursesession_list_item, null);
            holder = new ViewHolder();
            holder.img_teacher = (ImageView) convertView.findViewById(R.id.img_teacher);
            holder.tv_teacher_name = (TextView) convertView.findViewById(R.id.tv_teacher_name);
            holder.tv_course_time = (TextView) convertView.findViewById(R.id.tv_course_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CourseSessionBean ai = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_teacher, ai.getTeacherImg());
        holder.tv_teacher_name.setText(ai.getTeacherName() + "老师");
        holder.tv_course_time.setText("每" + ai.getClassTime());
        if (needBg) {
            holder.tv_course_time.setBackgroundResource(R.drawable.rec_gray_stroke_trans_solid);
        } else {
            holder.tv_course_time.setBackground(null);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_teacher;
        TextView tv_teacher_name;
        TextView tv_course_time;
    }

}
