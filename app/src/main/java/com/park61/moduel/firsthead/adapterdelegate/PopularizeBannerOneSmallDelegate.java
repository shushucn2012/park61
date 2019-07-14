package com.park61.moduel.firsthead.adapterdelegate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.moduel.firsthead.fragment.SpecialTypeActivity;
import com.park61.widget.myrv.base.ItemViewDelegate;
import com.park61.widget.myrv.base.ViewHolder;
import com.park61.widget.viewpager.MyBanner;

/**
 * Created by shubei on 2017/5/19.
 */

/**
 * 专栏单张小图
 */
public class PopularizeBannerOneSmallDelegate implements ItemViewDelegate<FirstHeadBean> {

    private Context mContext;

    public PopularizeBannerOneSmallDelegate(Context c) {
        mContext = c;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.firsthead_one_small_pic;
    }

    @Override
    public boolean isForViewType(FirstHeadBean item, int position) {
        //专栏，一张小图
        return item.getClassifyType() == GlobalParam.FirstHeadClassifyType.SPECIAL_TYPE
                && item.getComposeType() == GlobalParam.FirstHeadComposeType.ONE_SMALL_PIC;
    }

    @Override
    public void convert(ViewHolder holder, final FirstHeadBean firstHeadBean, int position) {
        holder.getView(R.id.area_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击加1
                firstHeadBean.setItemReadNum(firstHeadBean.getItemReadNum() + 1);
                Intent intent = new Intent(mContext, SpecialTypeActivity.class);
                intent.putExtra("itemId", firstHeadBean.getItemId());
                mContext.startActivity(intent);
            }
        });
        if (firstHeadBean.getItemMediaList() != null && firstHeadBean.getItemMediaList().size() > 0) {
            ImageManager.getInstance().displayImg((ImageView) holder.getView(R.id.img_right), firstHeadBean.getItemMediaList().get(0).getMediaUrl());
        }
        ImageManager.getInstance().displayCircleImg((ImageView) holder.getView(R.id.img_user_head), firstHeadBean.getIssuerHeadPic());
        holder.setText(R.id.tv_user_name, "专题");
        ((TextView) holder.getView(R.id.tv_user_name)).setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
        holder.setText(R.id.tv_read_num, firstHeadBean.getItemReadNum() + "");
        holder.setText(R.id.tv_commt_num, firstHeadBean.getItemCommentNum() + "");
        holder.getView(R.id.img_bofang).setVisibility(View.GONE);
        holder.setText(R.id.tv_time, firstHeadBean.getDateTag());
        holder.setText(R.id.tv_title, firstHeadBean.getItemTitle());
        holder.setText(R.id.tv_tag, firstHeadBean.getItemTag());
        if (firstHeadBean.isExpert()) {
            holder.getView(R.id.area_expert_flag).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.area_expert_flag).setVisibility(View.GONE);
        }
        holder.getView(R.id.tv_commt_num).setVisibility(View.GONE);
        holder.getView(R.id.iv_comment).setVisibility(View.GONE);
    }
}