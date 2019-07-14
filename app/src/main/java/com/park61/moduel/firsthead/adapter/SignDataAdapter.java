package com.park61.moduel.firsthead.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.bean.SignBabyBean;

import java.util.List;


public class SignDataAdapter extends RecyclerView.Adapter<SignDataAdapter.SignViewHolder> {
    private Context context;
    private List<SignBabyBean> datas;

    public SignDataAdapter(Context c, List<SignBabyBean> list) {
        this.context = c;
        this.datas = list;
    }

    @Override
    public SignViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_join_people, parent, false);
        return new SignViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SignViewHolder holder, int position) {
        SignBabyBean b = datas.get(position);
        ImageManager.getInstance().displayCircleImg(holder.imgHead, b.getUserPic());
        holder.babyName.setText(b.getChildName());
        holder.babyClass.setText(b.getClassName());
        holder.signTime.setText(b.getShowApplyDate());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    class SignViewHolder extends ViewHolder{
        ImageView imgHead;
        TextView babyName;
        TextView babyClass;
        TextView signTime;

        public SignViewHolder(View v) {
            super(v);
            imgHead = (ImageView) v.findViewById(R.id.head_img);
            babyName = (TextView) v.findViewById(R.id.baby_name);
            babyClass = (TextView) v.findViewById(R.id.baby_class);
            signTime = (TextView) v.findViewById(R.id.sign_time);
        }
    }

}
