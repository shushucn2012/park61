package com.park61.moduel.sales.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.bean.ProductLimit;
import com.park61.widget.textview.SecTimeTextView;

import java.util.List;

public class SecKillListNewAdapter extends BaseAdapter {

    private List<ProductLimit> mList;
    private Context mContext;
    private LayoutInflater factory;

    private final int TYPE_HAS_DATE_TITLE = 0;
    private final int TYPE_NO_DATE_TITLE = 1;

    public SecKillListNewAdapter(Context _context, List<ProductLimit> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ProductLimit getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position > 0) {
            ProductLimit siBefore = mList.get(position - 1);
            ProductLimit si = mList.get(position);
            String theDateStr = si.getStartTimeStr();
            String lastDateStr = siBefore.getStartTimeStr();
            if (theDateStr.equals(lastDateStr)) {
                return TYPE_NO_DATE_TITLE;
            } else {
                return TYPE_HAS_DATE_TITLE;
            }
        }
        return TYPE_HAS_DATE_TITLE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            convertView = factory.inflate(R.layout.seckill_list_item, null);
            holder = new ViewHolder();
            holder.area_st_title = convertView.findViewById(R.id.area_st_title);
            holder.tv_start_label = (TextView) convertView.findViewById(R.id.tv_start_label);
            holder.tv_time = (SecTimeTextView) convertView.findViewById(R.id.tv_time);
            holder.img_goods = (ImageView) convertView.findViewById(R.id.img_goods);
            holder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_goods_price = (TextView) convertView.findViewById(R.id.tv_goods_price);
            holder.tv_old_goods_price = (TextView) convertView.findViewById(R.id.tv_old_goods_price);
            holder.tv_old_goods_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_total_num = (TextView) convertView.findViewById(R.id.tv_total_num);
            holder.tv_cover = (TextView) convertView.findViewById(R.id.tv_cover);
            holder.seekbar = (SeekBar) convertView.findViewById(R.id.seekbar);
            holder.btn_gobuy = (Button) convertView.findViewById(R.id.btn_gobuy);
            holder.area_item_root = convertView.findViewById(R.id.area_item_root);
            switch (type) {
                case TYPE_HAS_DATE_TITLE:
                    holder.area_st_title.setVisibility(View.VISIBLE);
                    break;
                case TYPE_NO_DATE_TITLE:
                    holder.area_st_title.setVisibility(View.GONE);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.seekbar.setEnabled(false);
        final ProductLimit pl = mList.get(position);
        switch (type) {
            case TYPE_HAS_DATE_TITLE:
                if (TextUtils.isEmpty(pl.getCountDownTime()) || pl.getCountDownTime().equals("0")
                        || Long.parseLong(pl.getCountDownTime()) < 0) {
                    holder.tv_start_label.setVisibility(View.GONE);
                    holder.tv_time.setVisibility(View.GONE);
                } else {
                    Log.out("pl.getCountDownTime()====="+pl.getCountDownTime());
                    holder.tv_start_label.setVisibility(View.VISIBLE);
                    holder.tv_time.setVisibility(View.VISIBLE);
                    holder.tv_time.setText(CommonMethod.formatMs(Long.parseLong(pl.getCountDownTime())));
                }
                break;
            case TYPE_NO_DATE_TITLE:
                holder.area_st_title.setVisibility(View.GONE);
                break;
        }
        holder.tv_goods_name.setText(pl.getName());
        holder.tv_goods_price.setText(FU.formatPrice(pl.getSalesPrice()));
        holder.tv_old_goods_price.setText("￥" + pl.getOldPrice());
        if (pl.getMsg().equals("即将开始")) {
            holder.btn_gobuy.setText(pl.getStartTimeStr() + "开抢");
            holder.btn_gobuy.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_sec_selector));
            holder.tv_cover.setVisibility(View.GONE);
        } else if (pl.getMsg().equals("抢购中")) {
            String num = pl.getNum().toString();
            String salesNum = pl.getSalesNum();
            if (num.equals(salesNum)) {
                holder.btn_gobuy.setText("抢完了");
                holder.btn_gobuy.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_sec_finish));
                holder.tv_cover.setVisibility(View.VISIBLE);
            } else {
                holder.btn_gobuy.setText("马上抢");
                holder.btn_gobuy.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.btn_apply_selector));
                holder.tv_cover.setVisibility(View.GONE);
            }
        }
        if (pl.getNum() != null) {
            holder.tv_total_num.setText("限量" + pl.getNum() + "件");
            holder.seekbar.setMax(pl.getNum().intValue());
        }
        holder.seekbar.setProgress(FU.paseInt(pl.getSalesNum()));
        ImageManager.getInstance().displayImg(holder.img_goods, pl.getImg(), R.drawable.img_default_v);
        holder.btn_gobuy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, GoodsDetailsActivity.class);
                it.putExtra("goodsId", pl.getPmInfoid());
                it.putExtra("goodsName", pl.getName());
                it.putExtra("goodsPrice", pl.getSalesPrice() + "");
                it.putExtra("goodsOldPrice", pl.getOldPrice() + "");
                it.putExtra("promotionId", pl.getPromotionId());
                mContext.startActivity(it);
            }
        });
        holder.area_item_root.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, GoodsDetailsActivity.class);
                it.putExtra("goodsId", pl.getPmInfoid());
                it.putExtra("goodsName", pl.getName());
                it.putExtra("goodsPrice", pl.getSalesPrice() + "");
                it.putExtra("goodsOldPrice", pl.getOldPrice() + "");
                it.putExtra("promotionId", pl.getPromotionId());
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    class ViewHolder {
        View area_st_title;
        ImageView img_goods;
        TextView tv_goods_name;
        TextView tv_goods_price;
        TextView tv_old_goods_price;
        TextView tv_total_num;
        TextView tv_cover;
        SeekBar seekbar;
        Button btn_gobuy;
        TextView tv_start_label;
        SecTimeTextView tv_time;
        View area_item_root;
    }

}
