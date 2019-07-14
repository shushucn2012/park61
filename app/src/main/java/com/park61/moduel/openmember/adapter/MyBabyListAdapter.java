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
import com.park61.moduel.me.bean.BabyItem;
import com.park61.moduel.me.bean.MemberButtonVo;

import java.util.List;

/**
 * 我的宝宝列表adapter
 */
public class MyBabyListAdapter extends BaseAdapter {
    private List<BabyItem> mList;
    private Context mContext;
    private LayoutInflater factory;

    public MyBabyListAdapter(Context _context, List<BabyItem> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = factory
                    .inflate(R.layout.mybaby_list_item, null);
            holder = new ViewHolder();
            holder.img_baby = (ImageView) convertView
                    .findViewById(R.id.img_baby);
            holder.img_vip_card = (ImageView) convertView
                    .findViewById(R.id.img_vip_card);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_age = (TextView) convertView.findViewById(R.id.tv_age);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BabyItem bi = mList.get(position);
        holder.tv_name.setText(bi.getPetName());
        MemberButtonVo mb = bi.getMemberButtonVoList().get(0);
        if (mb.getButtonNamePic()!=null) {
            holder.img_vip_card.setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayImg(holder.img_vip_card, mb.getButtonNamePic());
        } else {
            holder.img_vip_card.setVisibility(View.GONE);
        }
//        if ("0".equals(bi.getGhpVip())) {
//            holder.img_vip_card.setVisibility(View.VISIBLE);
//            holder.img_vip_card.setImageResource(R.drawable.huiyuanka_xiaotubiao);
//            holder.img_value_card.setVisibility(View.GONE);
//        } else {
//            holder.img_value_card.setVisibility(View.GONE);
//            holder.img_vip_card.setVisibility(View.GONE);
//        }
        holder.tv_age.setText(bi.getAge());
        ImageManager.getInstance().displayImg(holder.img_baby, bi.getPictureUrl(), R.drawable.headimg_default_img);
        return convertView;
    }

    class ViewHolder {
        ImageView img_baby, img_vip_card;
        TextView tv_name;
        TextView tv_age;
    }

}
