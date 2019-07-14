package com.park61.moduel.acts.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.ActDetailsActivity;
import com.park61.moduel.acts.bean.ComtItem;
import com.park61.moduel.acts.bean.CourseComtItem;
import com.park61.moduel.acts.bean.EvaImage;
import com.park61.moduel.acts.bean.ReplyItem;
import com.park61.moduel.child.ShowBigPicActivity;
import com.park61.moduel.me.BabyCourseEvaActivity;
import com.park61.widget.MyRatingBar;
import com.park61.widget.gridview.GridViewForScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;

public class CourseComtListAdapter extends BaseAdapter {

    private List<CourseComtItem> mList;
    private Context mContext;

    public CourseComtListAdapter(Context _context, List<CourseComtItem> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CourseComtItem getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.course_comtlist_item, null);
            holder = new ViewHolder();
            holder.img_user = (ImageView) convertView.findViewById(R.id.img_user);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_comt_content = (TextView) convertView.findViewById(R.id.tv_comt_content);
            holder.tv_comt_time = (TextView) convertView.findViewById(R.id.tv_comt_time);
            holder.tv_progress = (TextView) convertView.findViewById(R.id.tv_progress);
            holder.gv_eva_pic = (GridViewForScrollView) convertView.findViewById(R.id.gv_eva_pic);
            holder.list_line = convertView.findViewById(R.id.list_line);
            holder.ratingbar_score = (MyRatingBar) convertView.findViewById(R.id.ratingbar_score);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CourseComtItem courseComtItem = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_user, courseComtItem.getUserUrl());
        holder.tv_username.setText(courseComtItem.getUserName());
        String contentStr = courseComtItem.getContent();
        if (TextUtils.isEmpty(contentStr)) {
            holder.tv_comt_content.setText("");
        } else {
            holder.tv_comt_content.setText(courseComtItem.getContent().replace("\r\n", ""));
        }
        holder.tv_progress.setText("学习进度" + courseComtItem.getSchedule() + "%");
        holder.tv_comt_time.setText(courseComtItem.getShowDate());
        if (CommonMethod.isListEmpty(courseComtItem.getUrlList())) {
            List<String> urlList = new ArrayList<>();
            holder.gv_eva_pic.setAdapter(new GvEvaPicAdapter(urlList));
        } else {
            holder.gv_eva_pic.setAdapter(new GvEvaPicAdapter(courseComtItem.getUrlList()));
        }


        holder.ratingbar_score.setPoint(courseComtItem.getScore());

        holder.gv_eva_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String url = courseComtItem.getUrlList().get(position);
                Intent bIt = new Intent(mContext, ShowBigPicActivity.class);
                bIt.putExtra("picUrl", url);
                if (courseComtItem.getUrlList().size() > 1)// url集合
                    bIt.putStringArrayListExtra("urlList", (ArrayList<String>) courseComtItem.getUrlList());
                mContext.startActivity(bIt);
            }
        });
        if (position == mList.size() - 1) {
            holder.list_line.setVisibility(View.GONE);
        } else {
            holder.list_line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_user;
        TextView tv_username;
        TextView tv_comt_content;
        TextView tv_comt_time, tv_progress;
        GridViewForScrollView gv_eva_pic;
        View list_line;
        MyRatingBar ratingbar_score;
    }

    private class GvEvaPicAdapter extends BaseAdapter {

        private List<String> listPictrue;

        public GvEvaPicAdapter(List<String> list) {
            this.listPictrue = list;
        }

        @Override
        public int getCount() {
            return listPictrue.size();
        }

        @Override
        public String getItem(int position) {
            return listPictrue.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_course_eva_show_item, null);
            ImageView img_input = (ImageView) convertView.findViewById(R.id.img_input);
            ImageManager.getInstance().displayImg(img_input, listPictrue.get(position));
            return convertView;
        }
    }
}
