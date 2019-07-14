package com.park61.moduel.acts.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.bean.CourseSessionBean;
import com.park61.moduel.coupon.bean.CouponUser;
import com.park61.moduel.me.bean.BabyItem;

import java.util.List;

public class CourseChooseBabyListAdapter extends BaseAdapter {

    private List<BabyItem> mList;
    private Context mContext;
    private OnEditClickedLsner mOnEditClickedLsner;

    public CourseChooseBabyListAdapter(Context _context, List<BabyItem> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public BabyItem getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.course_choose_babylist_item, null);
            holder = new ViewHolder();
            holder.tv_baby_name = (TextView) convertView.findViewById(R.id.tv_baby_name);
            holder.tv_baby_title = (TextView) convertView.findViewById(R.id.tv_baby_title);
            holder.tv_baby_sex = (TextView) convertView.findViewById(R.id.tv_baby_sex);
            holder.tv_baby_age = (TextView) convertView.findViewById(R.id.tv_baby_age);
            holder.img_chosen = (ImageView) convertView.findViewById(R.id.img_chosen);
            holder.item_bottom_line = convertView.findViewById(R.id.item_bottom_line);
            holder.img_edit = (ImageView) convertView.findViewById(R.id.img_edit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BabyItem item = mList.get(position);
        holder.tv_baby_name.setText(item.getPetName());
        if (!TextUtils.isEmpty(item.getSex())) {
            holder.tv_baby_sex.setText(item.getSex().equals("0") ? "男" : "女");
        } else {
            holder.tv_baby_sex.setText("男");
        }
        holder.tv_baby_age.setText(item.getAge());
        if (item.isChosen()) {
            holder.img_chosen.setImageResource(R.drawable.xuanze_focus);
        } else {
            holder.img_chosen.setImageResource(R.drawable.xuanze_default);
        }
        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEditClickedLsner != null) {
                    mOnEditClickedLsner.onClicked(position);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img_chosen, img_edit;
        TextView tv_baby_name;
        TextView tv_baby_title;
        TextView tv_baby_sex;
        TextView tv_baby_age;
        View item_bottom_line;
    }

    public interface OnEditClickedLsner {
        void onClicked(int pos);
    }

    public void setOnEditClickedLsner(OnEditClickedLsner lsner) {
        this.mOnEditClickedLsner = lsner;
    }

}
