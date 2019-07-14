package com.park61.moduel.firsthead.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.bean.FirstHeadBean;

import java.util.List;

/**
 * Created by nieyu on 2017/11/23.
 */

public class ContentRecommendAdapter extends BaseAdapter {
    List<FirstHeadBean> mlsit;
    Context ctx;

    public ContentRecommendAdapter(Context ctx, List<FirstHeadBean> mlsit) {
        this.ctx = ctx;
        this.mlsit = mlsit;

    }

    @Override
    public int getCount() {
        if (mlsit != null) {
            return mlsit.size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getItem(int i) {
        return mlsit.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.firsthead_one_small_pic, null);
        }
        FirstHeadBean fh = mlsit.get(i);
        TextView tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
        TextView tv_read_num = (TextView) convertView.findViewById(R.id.tv_read_num);
        TextView tv_commt_num = (TextView) convertView.findViewById(R.id.tv_commt_num);
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        ImageView img_right = (ImageView) convertView.findViewById(R.id.img_right);
        ImageView img_bofang = (ImageView) convertView.findViewById(R.id.img_bofang);
        ImageView iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);
        if (fh.getClassifyType() == 2) {//2专栏
            tv_user_name.setText("专题");
            iv_comment.setVisibility(View.GONE);
            tv_user_name.setTextColor(ctx.getResources().getColor(R.color.com_red));
            img_bofang.setVisibility(View.GONE);
            tv_commt_num.setVisibility(View.GONE);
        } else if (fh.getClassifyType() == 3) {//3活动推广
            tv_user_name.setTextColor(ctx.getResources().getColor(R.color.g999999));
            tv_user_name.setText(fh.getIssuerName());
            img_bofang.setVisibility(View.GONE);
            tv_commt_num.setVisibility(View.VISIBLE);
            iv_comment.setVisibility(View.VISIBLE);
        } else if (fh.getClassifyType() == 1) {//1视频
            tv_user_name.setTextColor(ctx.getResources().getColor(R.color.g999999));
            tv_user_name.setText(fh.getIssuerName());
            img_bofang.setVisibility(View.VISIBLE);
            tv_commt_num.setVisibility(View.VISIBLE);
            iv_comment.setVisibility(View.VISIBLE);
        } else if (fh.getClassifyType() == 0) {//0图文
            tv_user_name.setTextColor(ctx.getResources().getColor(R.color.g999999));
            tv_user_name.setText(fh.getIssuerName());
            img_bofang.setVisibility(View.GONE);
            tv_commt_num.setVisibility(View.VISIBLE);
            iv_comment.setVisibility(View.VISIBLE);
        } else {
            tv_commt_num.setVisibility(View.VISIBLE);
            iv_comment.setVisibility(View.VISIBLE);
            img_bofang.setVisibility(View.GONE);

        }

        tv_read_num.setText(fh.getItemReadNum() + "");
        tv_commt_num.setText(fh.getItemCommentNum() + "");
        tv_title.setText(fh.getItemTitle());
        if (mlsit != null && mlsit.size() > 0) {
            ImageManager.getInstance().displayImg(img_right, fh.getItemMediaList().get(0).getMediaUrl());
        }
        return convertView;
    }
}
