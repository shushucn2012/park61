package com.park61.moduel.childtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.childtest.bean.QBaseInfoItem;
import com.park61.widget.list.ListViewForScrollView;

import java.util.List;

/**
 * Created by shubei on 2017/12/7.
 */

public class QuestionsBaseInfoAdapter extends BaseAdapter {

    private Context mContext;
    private List<QBaseInfoItem> mList;

    public QuestionsBaseInfoAdapter(Context context, List<QBaseInfoItem> list) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public QBaseInfoItem getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.testpage_list_item, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_tips = (TextView) convertView.findViewById(R.id.tv_tips);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_choose_type);
            holder.tv_count = (TextView) convertView.findViewById(R.id.base_info_num);
            holder.lv_ea_items = (ListViewForScrollView) convertView.findViewById(R.id.lv_ea_items);
            holder.lv_ea_items.setFocusable(false);
            holder.area_tips = convertView.findViewById(R.id.area_tips);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_count.setVisibility(View.VISIBLE);
        holder.tv_count.setText(position+1+".");
        final QBaseInfoItem qBaseInfoItem = mList.get(position);
        if(qBaseInfoItem.getIsSingle() == 0){
            holder.tv_type.setVisibility(View.GONE);
        }
        holder.tv_title.setText(qBaseInfoItem.getContent());
        QuestionItemsAdapter itemsAdapter = new QuestionItemsAdapter(qBaseInfoItem.getAnswers(), qBaseInfoItem.getIsSingle() == 0);
        holder.lv_ea_items.setAdapter(itemsAdapter);
        holder.lv_ea_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_title;
        TextView tv_tips;
        TextView tv_date;
        TextView tv_type, tv_count;
        ListViewForScrollView lv_ea_items;
        View area_tips;
    }

    private class QuestionItemsAdapter extends BaseAdapter {

        private boolean isSingle;
        private List<QBaseInfoItem.AnswersBean> listItems;

        public QuestionItemsAdapter(List<QBaseInfoItem.AnswersBean> list, boolean isSingle) {
            this.listItems = list;
            this.isSingle = isSingle;
        }

        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public QBaseInfoItem.AnswersBean getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            QuestionItemsAdapter.ItemViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.question_items_list_item, null);
                holder = new QuestionItemsAdapter.ItemViewHolder();
                holder.img_question_ckb = (ImageView) convertView.findViewById(R.id.img_question_ckb);
                holder.tv_question_title = (TextView) convertView.findViewById(R.id.tv_question_title);
                holder.area_question_body = convertView.findViewById(R.id.area_question_body);
                convertView.setTag(holder);
            } else {
                holder = (ItemViewHolder) convertView.getTag();
            }
            final QBaseInfoItem.AnswersBean item = listItems.get(position);
            holder.tv_question_title.setText(item.getContent());
            if (item.isChoosed()) {
                holder.img_question_ckb.setImageResource(R.drawable.ckb_testquestion_red);
            } else {
                holder.img_question_ckb.setImageResource(R.drawable.ckb_testquestion_gray);
            }
            //判断是否单选
            holder.area_question_body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSingle){
                        for (int j = 0; j < listItems.size(); j++) {
                            if (j != position) {
                                listItems.get(j).setChoosed(false);
                            } else {
                                listItems.get(j).setChoosed(true);
                            }
                        }
                    }else{
                        //多选
                        if (listItems.get(position).isChoosed()) {
                            listItems.get(position).setChoosed(false);
                        } else {
                            listItems.get(position).setChoosed(true);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        class ItemViewHolder {
            ImageView img_question_ckb;
            TextView tv_question_title;
            View area_question_body;
        }
    }
}
