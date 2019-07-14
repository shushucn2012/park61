package com.park61.moduel.firsthead.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.ShowClassNoticeActivity;
import com.park61.moduel.firsthead.bean.TeachClassNotice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by chenlie on 2018/1/11.
 *
 */

public class ClassWordAdapter extends RecyclerView.Adapter<ClassWordAdapter.WordViewHolder>{

    private List<TeachClassNotice> mList;
    private Activity mActivity;

    public ClassWordAdapter(Activity activity, List<TeachClassNotice> list){
        mActivity = activity;
        mList = list;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mActivity).inflate(R.layout.class_word_item, parent, false);

        return new WordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v->{
            Intent bIt = new Intent(mActivity, ShowClassNoticeActivity.class);
            bIt.putExtra("noticeId", mList.get(position).getNoticeId());
            bIt.putExtra("classId", mList.get(position).getClassId());
            bIt.putExtra("TeachClassNotice", mList.get(position));
            mActivity.startActivity(bIt);
        });
        TeachClassNotice item = mList.get(position);

        if (TextUtils.isEmpty(item.getTitle())) {
            holder.tv_title.setVisibility(View.GONE);
        } else {
            holder.tv_title.setText(item.getTitle());
            holder.tv_title.setVisibility(View.VISIBLE);
        }
        ImageManager.getInstance().displayCircleImg(holder.img_teacher, item.getHeadPic());
        holder.tv_name.setText(item.getUserName());
        holder.tv_class.setText(item.getClassName());
        if (TextUtils.isEmpty(item.getContent())) {
            holder.tv_content.setVisibility(View.GONE);
        } else {
            holder.tv_content.setText(item.getContent());
            holder.tv_content.setVisibility(View.VISIBLE);
        }
        holder.tv_date.setText(formatTimeAndDate(item.getCreateDate()));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class WordViewHolder extends RecyclerView.ViewHolder{

        TextView tv_date;
        TextView tv_title, tv_content, tv_view_num, tv_comment_num, tv_praise_num, tv_name, tv_class;
        ImageView img_teacher;
        public WordViewHolder(View v) {
            super(v);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_view_num = (TextView) v.findViewById(R.id.tv_view_num);
            tv_comment_num = (TextView) v.findViewById(R.id.tv_comment_num);
            tv_praise_num = (TextView) v.findViewById(R.id.tv_praise_num);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
            img_teacher = (ImageView) v.findViewById(R.id.img_teacher);
            tv_class = (TextView) v.findViewById(R.id.tv_class);
        }
    }

    public SpannableString formatTimeAndDate(String timestamp) {
        if (timestamp == null || timestamp.equals(""))
            return new SpannableString("");
        Long tsp = Long.parseLong(timestamp);
        int betweenDays = 0;
        try {
            betweenDays = DateTool.daysBetween(new Date(tsp), new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (betweenDays == 0) {
            return new SpannableString("今天");
        }
        /*else if (betweenDays == 1) {
            return new SpannableString("昨天");
        }*/
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        String dateStr = sdf.format(new Date(tsp));
        SpannableString sp = new SpannableString(dateStr);
        //sp.setSpan(new AbsoluteSizeSpan(28, true), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }
}
