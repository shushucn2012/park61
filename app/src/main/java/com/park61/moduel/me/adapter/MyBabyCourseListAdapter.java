package com.park61.moduel.me.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.CourseEvaListActivity;
import com.park61.moduel.me.BabyCourseEvaActivity;
import com.park61.moduel.me.bean.BabyCourse;
import com.park61.moduel.me.bean.MyBabyCourseItem;
import com.park61.widget.list.ListViewForScrollView;
import com.park61.widget.progress.RoundProgressBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyBabyCourseListAdapter extends BaseAdapter {

    private List<MyBabyCourseItem> mList;
    private Context mContext;
    private LayoutInflater factory;

    public MyBabyCourseListAdapter(Context _context, List<MyBabyCourseItem> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public MyBabyCourseItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = factory.inflate(R.layout.mybaby_courselist_item, null);
        ListViewForScrollView lv_baby_course = (ListViewForScrollView) convertView.findViewById(R.id.lv_baby_course);
        lv_baby_course.setAdapter(new BabyCourseAdapter(mList.get(position).getCourseList(), mList.get(position).getChildId()));
        ImageView img_baby = (ImageView) convertView.findViewById(R.id.img_baby);
        ImageManager.getInstance().displayImg(img_baby, mList.get(position).getChildImg());
        TextView tv_baby_name = (TextView) convertView.findViewById(R.id.tv_baby_name);
        tv_baby_name.setText(mList.get(position).getChildPetName());
        return convertView;
    }

    public class BabyCourseAdapter extends BaseAdapter {
        private List<BabyCourse> bList;
        private int childId;

        public BabyCourseAdapter(List<BabyCourse> _list, int childId) {
            this.bList = _list;
            this.childId = childId;
        }

        @Override
        public int getCount() {
            return bList.size();
        }

        @Override
        public BabyCourse getItem(int position) {
            return bList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = factory.inflate(R.layout.mybaby_course_item, null);

            ImageView img_course = (ImageView) convertView.findViewById(R.id.img_course);
            ImageManager.getInstance().displayImg(img_course, bList.get(position).getVideoImg());

            TextView tv_cousre_title = (TextView) convertView.findViewById(R.id.tv_cousre_title);
            tv_cousre_title.setText(bList.get(position).getTotalName());

            TextView tv_last_time = (TextView) convertView.findViewById(R.id.tv_last_time);
            if (TextUtils.isEmpty(bList.get(position).getLastClassDate())) {
                tv_last_time.setText("上次学习：无");
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                String dateStr = sdf.format(new Date(FU.paseLong(bList.get(position).getLastClassDate())));
                tv_last_time.setText("上次学习：" + dateStr);
            }
            TextView tv_pinjia = (TextView) convertView.findViewById(R.id.tv_pinjia);
            tv_pinjia.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, BabyCourseEvaActivity.class);
                    it.putExtra("courseId", bList.get(position).getId());
                    it.putExtra("teacherId", bList.get(position).getTeacherId());
                    it.putExtra("applyId", bList.get(position).getApplyId());
                    it.putExtra("childId", childId);
                    it.putExtra("courseImg", bList.get(position).getVideoImg());
                    it.putExtra("teacherUrl", bList.get(position).getTeacherUrl());
                    it.putExtra("teacherName", bList.get(position).getTeacherName());
                    mContext.startActivity(it);
//                    mContext.startActivity(new Intent(mContext, CourseEvaListActivity.class));
                }
            });
            if (bList.get(position).isCanEvaluate()) {
                tv_pinjia.setVisibility(View.VISIBLE);
            } else {
                tv_pinjia.setVisibility(View.INVISIBLE);
            }
            RoundProgressBar roundProgressBar2 = (RoundProgressBar) convertView.findViewById(R.id.roundProgressBar2);
            roundProgressBar2.setProgress((int) bList.get(position).getSchedule());
            return convertView;
        }
    }

}
