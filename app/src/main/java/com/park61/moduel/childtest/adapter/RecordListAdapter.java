package com.park61.moduel.childtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.childtest.bean.TestRecordItemBean;

import java.util.List;

/**
 * Created by nieyu on 2017/12/6.
 */

public class RecordListAdapter extends BaseAdapter {

  private List<TestRecordItemBean> mList;
    private Context ctx;
    public RecordListAdapter(Context ctx,  List<TestRecordItemBean> mList){
        this.ctx=ctx;
        this.mList=mList;
    }
    @Override
    public int getCount() {
        if(mList!=null){
            return mList.size();
        }else {
            return 0;
        }

    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        TestRecordItemBean testRecommedBean = mList.get(i);

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(
                    R.layout.adapter_recordlistitem, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageManager.getInstance().displayImg(holder.img_item, testRecommedBean.getEaServiceImg());
        holder.tv_title.setText(testRecommedBean.getEaServiceTitle());
        holder.tv_child_name.setText("测评宝宝：" + testRecommedBean.getChildName());
        holder.tv_test_time.setText(testRecommedBean.getCreateDate()+"");

        if (testRecommedBean.getStatus() == 0) {
            holder.img_unfinished.setVisibility(View.VISIBLE);
//            int leftMin = testRecommedBean.getUnfinishedTime() / 60;
//            if (leftMin == 0) leftMin = 1;
            holder.tv_question_num.setText("共" + testRecommedBean.getUnfinishedEaItemNum() + "个测评项未完成，" + "预计" + testRecommedBean.getUnfinishedTime() + "分钟");
            holder.tv_question_num.setTextColor(ctx.getResources().getColor(R.color.color_text_red_light));
        } else {
            holder.img_unfinished.setVisibility(View.GONE);
            holder.tv_question_num.setText("测评完成，棒棒的！");
            holder.tv_question_num.setTextColor(ctx.getResources().getColor(R.color.g999999));
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_item;
        TextView tv_question_num;
        ImageView img_unfinished;
        TextView tv_child_name;
        TextView tv_title;
        TextView tv_test_time;
//        CirButton tv_expert_focus_btn;
        public ViewHolder(View view) {
//            tv_realprice = (TextView) view.findViewById(R.id.tv_realprice);
            tv_test_time = (TextView) view.findViewById(R.id.tv_test_time);
            img_unfinished = (ImageView) view.findViewById(R.id.img_unfinished);
            img_item = (ImageView) view.findViewById(R.id.img_item);
            tv_question_num = (TextView) view.findViewById(R.id.tv_question_num);
            tv_child_name = (TextView) view.findViewById(R.id.tv_child_name);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
//            tv_expert_focus_btn=(CirButton)view.findViewById(R.id.tv_expert_focus_btn);
        }
    }
}
