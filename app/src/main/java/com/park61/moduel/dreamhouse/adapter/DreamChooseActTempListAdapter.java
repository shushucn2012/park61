package com.park61.moduel.dreamhouse.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.dreamhouse.bean.DreamChooseActTempBean;

import java.util.ArrayList;

/**
 * 我的梦想列表adapter
 */
public class DreamChooseActTempListAdapter extends BaseAdapter {
    private ArrayList<DreamChooseActTempBean> mList;
    private Context mContext;
    private LayoutInflater factory;

    public DreamChooseActTempListAdapter(Context _context, ArrayList<DreamChooseActTempBean> _list) {
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = factory.inflate(R.layout.dream_acttemplist_item, null);
            holder = new ViewHolder();
            holder.area_root = convertView.findViewById(R.id.area_root);
            holder.img_acttemp = (ImageView) convertView.findViewById(R.id.img_acttemp);
            holder.tv_acttemp_name = (TextView) convertView.findViewById(R.id.tv_acttemp_name);
            holder.tv_act_price = (TextView) convertView.findViewById(R.id.tv_act_price);
            holder.tv_old_act_price = (TextView) convertView.findViewById(R.id.tv_old_act_price);
            holder.tv_old_act_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_already_salse_num = (TextView) convertView.findViewById(R.id.tv_already_salse_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DreamChooseActTempBean actTempBean = mList.get(position);
        holder.tv_acttemp_name.setText(actTempBean.getActTitle());
        holder.tv_act_price.setText(FU.formatPrice(actTempBean.getActPrice()));
        holder.tv_old_act_price.setText(FU.formatPrice(actTempBean.getActOriginalPrice()));
        holder.tv_already_salse_num.setText("已售" + actTempBean.getPopularSum());
        ImageManager.getInstance().displayImg(holder.img_acttemp, actTempBean.getActCover(), R.drawable.img_default_v);
        return convertView;
    }

    class ViewHolder {
        ImageView img_acttemp;
        TextView tv_acttemp_name;
        TextView tv_act_price;
        TextView tv_old_act_price;
        TextView tv_already_salse_num;
        View area_root;
    }

}
