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
import com.park61.moduel.childtest.bean.TestBigQuestion;
import com.park61.moduel.childtest.bean.TestQuestionItem;
import com.park61.widget.list.ListViewForScrollView;

import java.util.List;


/**
 * 评测问题列表适配器
 */
public class TestPageListAdapter extends BaseAdapter {

    private List<TestBigQuestion> mList;
    private Context mContext;

    public TestPageListAdapter(Context _context, List<TestBigQuestion> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TestBigQuestion getItem(int position) {
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
            holder.lv_ea_items = (ListViewForScrollView) convertView.findViewById(R.id.lv_ea_items);
            holder.area_tips = convertView.findViewById(R.id.area_tips);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_choose_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final TestBigQuestion sysListBean = mList.get(position);
        holder.tv_title.setText(sysListBean.getEaItemSubName());
        if(sysListBean.getIsSingle() == 0){
            holder.tv_type.setVisibility(View.GONE);
        }
        QuestionItemsAdapter itemsAdapter = new QuestionItemsAdapter(sysListBean.getListEaSubject());
        holder.lv_ea_items.setAdapter(itemsAdapter);
        holder.lv_ea_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_title, tv_tips, tv_date, tv_type;
        ListViewForScrollView lv_ea_items;
        View area_tips;
    }

    private class QuestionItemsAdapter extends BaseAdapter {

        private List<TestQuestionItem> listItems;

        public QuestionItemsAdapter(List<TestQuestionItem> list) {
            this.listItems = list;
        }

        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public TestQuestionItem getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ItemViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.question_items_list_item, null);
                holder = new ItemViewHolder();
                holder.img_question_ckb = (ImageView) convertView.findViewById(R.id.img_question_ckb);
                holder.tv_question_title = (TextView) convertView.findViewById(R.id.tv_question_title);
                holder.area_question_body = convertView.findViewById(R.id.area_question_body);
                convertView.setTag(holder);
            } else {
                holder = (ItemViewHolder) convertView.getTag();
            }
            TestQuestionItem item = listItems.get(position);
            holder.tv_question_title.setText(item.getContent());
            if (listItems.get(position).isHasChecked()) {
                holder.img_question_ckb.setImageResource(R.drawable.ckb_testquestion_red);
            } else {
                holder.img_question_ckb.setImageResource(R.drawable.ckb_testquestion_gray);
            }
            holder.area_question_body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TestQuestionItem item = listItems.get(position);
                    if(item.getIsSingle() == 0){
                        //单选
                        for (int j = 0; j < listItems.size(); j++) {
                            if (j != position) {
                                listItems.get(j).setHasChecked(false);
                            } else {
                                listItems.get(j).setHasChecked(true);
                            }
                        }
                    }else{
                        //多选
                        if (item.isHasChecked()) {
                            item.setHasChecked(false);
                        } else {
                            item.setHasChecked(true);
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
