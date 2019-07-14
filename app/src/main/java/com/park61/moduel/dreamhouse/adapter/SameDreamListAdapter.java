package com.park61.moduel.dreamhouse.adapter;

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
import com.park61.moduel.dreamhouse.bean.SameDreamInfo;

import java.util.ArrayList;

/**
 * Created by HP on 2017/1/4.
 */
public class SameDreamListAdapter extends BaseAdapter {
    private ArrayList<SameDreamInfo> mList;
    private Context mContext;
    private LayoutInflater factory;

    public SameDreamListAdapter(Context _context, ArrayList<SameDreamInfo> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView=factory.inflate(R.layout.same_dream_list_item,null);
            holder.img= (ImageView) convertView.findViewById(R.id.img);
            holder.tv_title= (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_num= (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_join_time= (TextView) convertView.findViewById(R.id.tv_join_time);
            holder.line = convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SameDreamInfo item = mList.get(position);
        ImageManager.getInstance().displayCircleImg(holder.img,item.getUserUrl());
        holder.tv_title.setText(item.getUserName());
        if(position==mList.size()-1){
            holder.line.setVisibility(View.GONE);
        }else{
            holder.line.setVisibility(View.VISIBLE);
        }
//        if(item.getRequirementNum()==0){
//            holder.tv_num.setVisibility(View.GONE);
//        }else{
//            holder.tv_num.setText(item.getRequirementNum()+"");
//        }
        holder.tv_join_time.setText(toPDateStr(System.currentTimeMillis(), item.getShowTime()));
        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView tv_title,tv_num,tv_join_time;
        View line;
    }
    /**
     * 时间个性化转换
     *
     * @param currDate   当前时间
     * @param covertDate 发布时间
     * @return 转化后的字符串
     */
    @SuppressWarnings("deprecation")
    public String toPDateStr(long currDate, String covertDate) {
        int d_minutes, d_hours, d_days;
        long currcovertDate = Long.parseLong(covertDate);
        long d;
        String result;
        d = (currDate - currcovertDate) / 1000;
        d_days = (int) (d / 86400);
        d_hours = (int) (d / 3600);
        d_minutes = (int) (d / 60);
        if (d_days > 0 && d_days < 4) {
            result = d_days + "天前加入";
        }else if (d_days <= 0 && d_hours > 0) {
            result = d_hours + "小时前加入";
        } else if (d_hours <= 0 && d_minutes > 0) {
            result = d_minutes + "分钟前加入";
        } else if (d_minutes <= 0 && d >= 0) {
            return Math.round(d) + "秒前加入";
        } else {
            result = DateTool.L2SEndDay(covertDate)+"加入";
        }
        return result;
    }
}
