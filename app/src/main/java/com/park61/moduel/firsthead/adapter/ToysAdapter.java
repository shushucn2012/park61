package com.park61.moduel.firsthead.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.ToyDetailActivity;
import com.park61.moduel.firsthead.ToyNumActivity;
import com.park61.moduel.firsthead.bean.ToyBean;

import java.util.List;

/**
 * Created by chenlie on 2018/1/3.
 * <p>
 * 推荐列表item
 */

public class ToysAdapter extends RecyclerView.Adapter<ToysAdapter.ToysHolder> {

    private List<ToyBean> data;
    private Activity mActivity;
    private OnNumChanged listener;
    private int activityId = -1;

    public ToysAdapter(Activity activity, List<ToyBean> list) {
        mActivity = activity;
        data = list;
    }


    @Override
    public ToysHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.party_toy_item, parent, false);
        return new ToysHolder(view);
    }

    @Override
    public void onBindViewHolder(ToysHolder holder, int position) {

        ToyBean b = data.get(position);
        ImageManager.getInstance().displayImg(holder.icon, b.getProductPicUrl());
        holder.nameTv.setText(b.getProductCname());
        holder.colorTv.setText(b.getProductColor());
        holder.modelTv.setText(b.getProductSize());
        holder.priceTv.setText("￥" + b.getCurrentUnifyPrice());
        holder.origPriceTv.setText("￥" + b.getMarketPrice());
        holder.origPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        //数量，num为0默认显示1
        holder.numEt.setText(b.getNumSelect()+"");
        holder.reduceBt.setOnClickListener(v -> {
            if(b.getNumSelect() - 1 < b.getNum()){
                if(b.getNum() != 0)
                Toast.makeText(mActivity, "至少购买"+b.getNum()+"件", Toast.LENGTH_SHORT).show();
            }else{
                listener.onChanged(position,  b.getNumSelect() - 1);
            }
        });

        holder.addBt.setOnClickListener(v -> {
            if(b.getNumSelect() + 1 > 99){
                Toast.makeText(mActivity, "最多购买99件", Toast.LENGTH_SHORT).show();
            }else{
                listener.onChanged(position, b.getNumSelect() + 1);
            }
        });

        holder.icon.setOnClickListener(v->{
            //去详情
            Intent it = new Intent(mActivity, ToyDetailActivity.class);
            //增加活动id和数量
            it.putExtra("activityId", activityId);
            it.putExtra("toyBean", b);
            mActivity.startActivityForResult(it, ToyDetailActivity.REQUEST_CODE);
        });
        holder.center.setOnClickListener(v->{
            //去详情
            Intent it = new Intent(mActivity, ToyDetailActivity.class);
            it.putExtra("activityId", activityId);
            it.putExtra("toyBean", b);
            mActivity.startActivityForResult(it, ToyDetailActivity.REQUEST_CODE);
        });

        holder.numEt.setOnClickListener(v -> {
            Intent it = new Intent(mActivity, ToyNumActivity.class);
            it.putExtra("position", position);
            it.putExtra("num", b.getNum());
            it.putExtra("tempNum", b.getNumSelect());
            mActivity.startActivityForResult(it, ToyNumActivity.REQUEST_CODE);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnNumChangedListener(OnNumChanged listener){
        this.listener = listener;
    }

    public void setActivityId(int id){
        activityId = id;
    }

    class ToysHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView nameTv;
        TextView colorTv;
        TextView modelTv;
        TextView priceTv;
        TextView origPriceTv;
        Button reduceBt;
        Button addBt;
        EditText numEt;
        RelativeLayout center;

        public ToysHolder(View v) {
            super(v);

            icon = (ImageView) v.findViewById(R.id.toy_icon);
            nameTv = (TextView) v.findViewById(R.id.toy_name);
            colorTv = (TextView) v.findViewById(R.id.toy_color);
            modelTv = (TextView) v.findViewById(R.id.toy_model);
            priceTv = (TextView) v.findViewById(R.id.cur_price);
            origPriceTv = (TextView) v.findViewById(R.id.orig_price);
            reduceBt = (Button) v.findViewById(R.id.btn_reduce);
            addBt = (Button) v.findViewById(R.id.btn_add);
            numEt = (EditText) v.findViewById(R.id.tv_buy_num);
            center = (RelativeLayout) v.findViewById(R.id.center_layout);
        }
    }

    public interface OnNumChanged{
        void onChanged(int position, int num);
    }
}
