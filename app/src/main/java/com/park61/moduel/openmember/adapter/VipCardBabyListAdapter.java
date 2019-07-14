package com.park61.moduel.openmember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.openmember.bean.ChildInfo;

import java.util.List;

/**
 * 会员卡宝宝列表adapter
 */
public class VipCardBabyListAdapter extends BaseAdapter {
    private List<ChildInfo> mList;
    private Context mContext;
    private LayoutInflater factory;

    public VipCardBabyListAdapter(Context _context, List<ChildInfo> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ChildInfo getItem(int position) {
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
            convertView = factory
                    .inflate(R.layout.vipcard_babylist_item, null);
            holder = new ViewHolder();
            holder.img_baby = (ImageView) convertView
                    .findViewById(R.id.img_baby);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.line = convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ChildInfo info = mList.get(position);
        if (position == mList.size() - 1) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        holder.tv_name.setText(info.getPetName());
        holder.tv_date.setText(info.getExpireDate());
        ImageManager.getInstance().displayImg(holder.img_baby, info.getPicUrl(), R.drawable.headimg_default_img);
        if (info.getType() == 1) {//时间字体颜色：0灰色，1黑色
            holder.tv_date.setTextColor(mContext.getResources().getColor(R.color.g333333));
        } else {
            holder.tv_date.setTextColor(mContext.getResources().getColor(R.color.g999999));
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_baby;
        TextView tv_name;
        TextView tv_date;
        View line;
    }
}
