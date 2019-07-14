package com.park61.moduel.childtest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.childtest.StartTestV2Activity;
import com.park61.moduel.childtest.bean.EaListBean;
import com.park61.moduel.childtest.bean.EaSysItemBean;
import com.park61.widget.list.ListViewForScrollView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by shubei on 2017/9/19.
 */

public class EaListAdapter extends BaseAdapter {

    private List<EaListBean.SysListBean> mList;
    private Context mContext;

    public EaListAdapter(Context _context, List<EaListBean.SysListBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public EaListBean.SysListBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ea_sys_list_item, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_tips = (TextView) convertView.findViewById(R.id.tv_tips);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.lv_ea_items = (ListViewForScrollView) convertView.findViewById(R.id.lv_ea_items);
            holder.area_tips = convertView.findViewById(R.id.area_tips);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final EaListBean.SysListBean sysListBean = mList.get(position);
        holder.tv_title.setText(sysListBean.getTitle());
        if (!CommonMethod.isListEmpty(sysListBean.getTaskList())) {
            holder.area_tips.setVisibility(View.VISIBLE);
            holder.tv_tips.setText(sysListBean.getTaskList().get(0).getTeachName() + "老师邀请" + sysListBean.getTaskList().get(0).getChildName() + "宝宝完成测评");
            holder.tv_date.setText(new SimpleDateFormat("MM/dd HH:mm").format(sysListBean.getTaskList().get(0).getInviteTime()).toString());
        } else {
            holder.area_tips.setVisibility(View.GONE);
        }
        EaItemsAdapter eaItemsAdapter = new EaItemsAdapter(sysListBean.getItems());
        holder.lv_ea_items.setAdapter(eaItemsAdapter);
        holder.lv_ea_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mContext, StartTestV2Activity.class);
                it.putExtra("eaItemId", sysListBean.getItems().get(position).getId());
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_title, tv_tips, tv_date;
        ListViewForScrollView lv_ea_items;
        View area_tips;
    }

    private class EaItemsAdapter extends BaseAdapter {

        private List<EaSysItemBean> listItems;

        public EaItemsAdapter(List<EaSysItemBean> list) {
            this.listItems = list;
        }

        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public EaSysItemBean getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ea_items_list_item, null);
            ImageView img_item = (ImageView) convertView.findViewById(R.id.img_item);
            TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            TextView tv_age = (TextView) convertView.findViewById(R.id.tv_age);
            TextView tv_question_num = (TextView) convertView.findViewById(R.id.tv_question_num);
            TextView tv_child_num = (TextView) convertView.findViewById(R.id.tv_child_num);
            EaSysItemBean eaSysItemBean = listItems.get(position);
            ImageManager.getInstance().displayImg(img_item, eaSysItemBean.getImgUrl());
            tv_title.setText(eaSysItemBean.getName());
            tv_age.setText("适合年龄：" + eaSysItemBean.getEaLowAgeLimit() + "-" + eaSysItemBean.getEaUppAgeLimit() + "岁");
            tv_question_num.setText("共" + eaSysItemBean.getSubNum() + "题，" + "预计" + eaSysItemBean.getSubTime() / 60 + "分钟");
            tv_child_num.setText(eaSysItemBean.getFinishNum() + "");
            return convertView;
        }
    }
}
