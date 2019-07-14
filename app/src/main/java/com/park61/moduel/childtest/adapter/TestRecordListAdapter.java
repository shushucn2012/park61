package com.park61.moduel.childtest.adapter;

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
import com.park61.moduel.childtest.bean.EaSysItemBean;
import com.park61.moduel.childtest.bean.TestRecordBean;
import com.park61.moduel.msg.MsgItem;

import java.text.SimpleDateFormat;
import java.util.List;

public class TestRecordListAdapter extends BaseAdapter {

    private List<TestRecordBean> mList;
    private Context mContext;
    private LayoutInflater factory;

    public TestRecordListAdapter(Context _context, List<TestRecordBean> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TestRecordBean getItem(int position) {
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
            convertView = factory.inflate(R.layout.test_records_item, null);
            holder = new ViewHolder();
            holder.img_item = (ImageView) convertView.findViewById(R.id.img_item);
            holder.img_unfinished = (ImageView) convertView.findViewById(R.id.img_unfinished);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_child_name = (TextView) convertView.findViewById(R.id.tv_child_name);
            holder.tv_test_time = (TextView) convertView.findViewById(R.id.tv_test_time);
            holder.tv_question_num = (TextView) convertView.findViewById(R.id.tv_question_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TestRecordBean testRecordBean = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_item, testRecordBean.getImgUrl());
        holder.tv_title.setText(testRecordBean.getEaSysName() + testRecordBean.getEaItemName());
        holder.tv_child_name.setText("测评宝宝：" + testRecordBean.getChildName());

        holder.tv_test_time.setText(new SimpleDateFormat("MM-dd HH:mm").format(testRecordBean.getCreateDate()).toString());
        if (testRecordBean.getStatus() == 0) {
            holder.img_unfinished.setVisibility(View.VISIBLE);
            int leftMin = testRecordBean.getUnfinishedTime() / 60;
            if (leftMin == 0) leftMin = 1;
            holder.tv_question_num.setText("共" + testRecordBean.getUnfinishedCount() + "题未完成，" + "预计" + leftMin + "分钟");
            holder.tv_question_num.setTextColor(mContext.getResources().getColor(R.color.color_text_red_light));
        } else {
            holder.img_unfinished.setVisibility(View.GONE);
            holder.tv_question_num.setText("测评完成，棒棒的！");
            holder.tv_question_num.setTextColor(mContext.getResources().getColor(R.color.g999999));
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_item, img_unfinished;
        TextView tv_title;
        TextView tv_child_name;
        TextView tv_test_time;
        TextView tv_question_num;
    }

}
