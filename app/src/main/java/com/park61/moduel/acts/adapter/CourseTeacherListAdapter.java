package com.park61.moduel.acts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.bean.CourseSessionBean;

import java.util.List;

public class CourseTeacherListAdapter extends BaseAdapter {

    private List<CourseSessionBean> mList;
    private Context mContext;

    public CourseTeacherListAdapter(Context _context, List<CourseSessionBean> _list) {
        this.mList = _list;
        this.mContext = _context;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.courseteacher_list_item, null);
            holder = new ViewHolder();
            holder.img_teacher = (ImageView) convertView.findViewById(R.id.img_teacher);
            holder.tv_teacher_name = (TextView) convertView.findViewById(R.id.tv_teacher_name);
            holder.tv_zili = (TextView) convertView.findViewById(R.id.tv_zili);
            holder.tv_teacher_resume = (TextView) convertView.findViewById(R.id.tv_teacher_resume);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CourseSessionBean ai = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_teacher, ai.getTeacherImg());
        holder.tv_teacher_name.setText(ai.getTeacherName());
        holder.tv_zili.setText(ai.getTeacherTitle());
        holder.tv_teacher_resume.setText(ai.getResume());
        return convertView;
    }

    class ViewHolder {
        ImageView img_teacher;
        TextView tv_teacher_name;
        TextView tv_teacher_resume, tv_zili;
    }


}
