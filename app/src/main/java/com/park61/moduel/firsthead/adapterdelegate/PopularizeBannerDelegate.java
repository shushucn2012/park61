package com.park61.moduel.firsthead.adapterdelegate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.moduel.firsthead.fragment.SpecialTypeActivity;
import com.park61.widget.myrv.base.ItemViewDelegate;
import com.park61.widget.myrv.base.ViewHolder;

/**
 * Created by shubei on 2017/5/19.
 * 专栏大图
 */
public class PopularizeBannerDelegate implements ItemViewDelegate<FirstHeadBean> {

    private Context mContext;

    public PopularizeBannerDelegate(Context c) {
        mContext = c;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.firsthead_banner2;
    }

    @Override
    public boolean isForViewType(FirstHeadBean item, int position) {
        //专栏，一张大图
        return item.getClassifyType() == GlobalParam.FirstHeadClassifyType.SPECIAL_TYPE
                && item.getComposeType() == GlobalParam.FirstHeadComposeType.ONE_BIG_PIC;
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
        if (!CommonMethod.isListEmpty(firstHeadBean.getItemMediaList())) {
            ImageManager.getInstance().displayImg((ImageView) holder.getView(R.id.img_big), firstHeadBean.getItemMediaList().get(0).getMediaUrl());
        }
        ImageManager.getInstance().displayCircleImg((ImageView) holder.getView(R.id.img_user_head), firstHeadBean.getIssuerHeadPic());
        holder.setText(R.id.tv_read_num, firstHeadBean.getItemReadNum() + "");
        holder.setText(R.id.tv_commt_num, firstHeadBean.getItemCommentNum() + "");
        holder.setText(R.id.tv_title, firstHeadBean.getItemTitle());
        holder.getView(R.id.tv_commt_num).setVisibility(View.GONE);
        holder.getView(R.id.iv_comment).setVisibility(View.GONE);
    }
}