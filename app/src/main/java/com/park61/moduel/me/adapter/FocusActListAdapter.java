package com.park61.moduel.me.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.ActDetailsActivity;
import com.park61.moduel.acts.bean.ActItem;
import com.park61.moduel.me.actlist.FocusActsActivity;

public class FocusActListAdapter extends BaseAdapter {

    private List<ActItem> mList;
    private Context mContext;
    private LayoutInflater factory;
    private ToApplyCallBack mToApplyCallBack;

    public FocusActListAdapter(Context _context, List<ActItem> _list, ToApplyCallBack _ToApplyCallBack) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
        mToApplyCallBack = _ToApplyCallBack;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ActItem getItem(int position) {
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
            convertView = factory.inflate(R.layout.focus_actlist_item, null);
            holder = new ViewHolder();
            holder.img_act = (ImageView) convertView.findViewById(R.id.img_act);
            holder.actinfo_area = convertView.findViewById(R.id.actinfo_area);
            holder.tv_act_title = (TextView) convertView.findViewById(R.id.tv_act_title);
            holder.tv_act_addr = (TextView) convertView.findViewById(R.id.tv_act_addr);
            holder.tv_act_price = (TextView) convertView.findViewById(R.id.tv_act_price);
            holder.btn_apply = (Button) convertView.findViewById(R.id.btn_apply);
            holder.btn_share = (Button) convertView.findViewById(R.id.btn_share);
            holder.btn_comt = (Button) convertView.findViewById(R.id.btn_comt);
            holder.line = convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ActItem ai = mList.get(position);
        holder.tv_act_title.setText(ai.getActTitle());
        holder.tv_act_addr.setText(ai.getActAddress());
        holder.tv_act_price.setText(FU.isNumEmpty(ai.getActPrice()) ? "免费"
                : "￥" + ai.getActPrice());
        holder.btn_apply.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mToApplyCallBack.onApplyClicked(position);
            }
        });
        holder.btn_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mToApplyCallBack.onShareClicked(position);
            }
        });
        holder.btn_comt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mToApplyCallBack.onComtClicked(position);
            }
        });
        if (!TextUtils.isEmpty(ai.getActCover()))
            ImageManager.getInstance().displayImg(holder.img_act, ai.getActCover());

        holder.actinfo_area.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ActDetailsActivity.class);
                it.putExtra("id", mList.get(position).getId());
                it.putExtra("isFocus", true);
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img_act;
        TextView tv_act_title;
        TextView tv_act_addr;
        TextView tv_act_price;
        View actinfo_area;
        Button btn_apply;
        Button btn_share;
        Button btn_comt;
        View line;
    }

    public interface ToApplyCallBack {
        public void onApplyClicked(int pos);

        public void onComtClicked(int pos);

        public void onShareClicked(int pos);
    }
}
