package com.park61.moduel.firsthead.adapterdelegate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.AudioListDetailsActivity;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.moduel.firsthead.fragment.TextDetailActivity;
import com.park61.widget.myrv.base.ItemViewDelegate;
import com.park61.widget.myrv.base.ViewHolder;

/**
 * Created by shubei on 2017/6/12.
 * 图文1小图
 */
public class OneSmallPicDelegate implements ItemViewDelegate<FirstHeadBean> {

    private Context mContext;

    public OneSmallPicDelegate(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.firsthead_one_small_pic;
    }

    @Override
    public boolean isForViewType(FirstHeadBean item, int position) {
        //图文或者音频，一张小图
        return (item.getClassifyType() == GlobalParam.FirstHeadClassifyType.TEXT_TYPE
                || item.getClassifyType() == GlobalParam.FirstHeadClassifyType.AUDIO_TYPE)
                && item.getComposeType() == GlobalParam.FirstHeadComposeType.ONE_SMALL_PIC;
    }

    @Override
    public void convert(ViewHolder holder, final FirstHeadBean firstHeadBean, int position) {
        holder.getView(R.id.area_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击加1
                firstHeadBean.setItemReadNum(firstHeadBean.getItemReadNum() + 1);
                if(firstHeadBean.getClassifyType() == GlobalParam.FirstHeadClassifyType.TEXT_TYPE) {
                    Intent intent = new Intent(mContext, TextDetailActivity.class);
                    intent.putExtra("itemId", firstHeadBean.getItemId());
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, AudioListDetailsActivity.class);
                    intent.putExtra("itemId", firstHeadBean.getItemId());
                    mContext.startActivity(intent);
                }
            }
        });
        if (firstHeadBean.getItemMediaList() != null && firstHeadBean.getItemMediaList().size() > 0) {
            ImageManager.getInstance().displayImg((ImageView) holder.getView(R.id.img_right), firstHeadBean.getItemMediaList().get(0).getMediaUrl());
        }
        ImageManager.getInstance().displayCircleImg((ImageView) holder.getView(R.id.img_user_head), firstHeadBean.getIssuerHeadPic());
        holder.setText(R.id.tv_user_name, firstHeadBean.getIssuerName());
        holder.setText(R.id.tv_read_num, firstHeadBean.getItemReadNum() + "");
        holder.setText(R.id.tv_commt_num, firstHeadBean.getItemCommentNum() + "");
        holder.getView(R.id.img_bofang).setVisibility(View.GONE);
        /*if (firstHeadBean.getCreateDate() != 0) {
            holder.setText(R.id.tv_time, DateTool.toPDateStr(System.currentTimeMillis(), firstHeadBean.getCreateDate()));
        } else {
            holder.setText(R.id.tv_time, firstHeadBean.getDateTag());
        }*/
        holder.setText(R.id.tv_time, firstHeadBean.getDateTag());
        holder.setText(R.id.tv_title, firstHeadBean.getItemTitle());
        holder.setText(R.id.tv_tag, firstHeadBean.getItemTag());
        if (firstHeadBean.isExpert()) {
            holder.getView(R.id.area_expert_flag).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.area_expert_flag).setVisibility(View.GONE);
        }
    }
}
