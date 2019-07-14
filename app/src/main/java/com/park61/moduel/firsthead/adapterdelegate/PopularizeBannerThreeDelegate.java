package com.park61.moduel.firsthead.adapterdelegate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DevAttr;
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
 * 专栏3张小图
 */
public class PopularizeBannerThreeDelegate implements ItemViewDelegate<FirstHeadBean> {

    private Context mContext;
    private MyBanner mp;

    public PopularizeBannerThreeDelegate(Context c) {
        mContext = c;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.firsthead_three_small_pic;
    }

    @Override
    public boolean isForViewType(FirstHeadBean item, int position) {
        //专栏3张小图
        return item.getClassifyType() == GlobalParam.FirstHeadClassifyType.SPECIAL_TYPE
                && item.getComposeType() == GlobalParam.FirstHeadComposeType.THREE_SMALL_PIC;
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
            if (firstHeadBean.getItemMediaList().size() > 0)
                ImageManager.getInstance().displayImg((ImageView) holder.getView(R.id.img_small0), firstHeadBean.getItemMediaList().get(0).getMediaUrl());
            if (firstHeadBean.getItemMediaList().size() > 1)
                ImageManager.getInstance().displayImg((ImageView) holder.getView(R.id.img_small1), firstHeadBean.getItemMediaList().get(1).getMediaUrl());
            if (firstHeadBean.getItemMediaList().size() > 2)
                ImageManager.getInstance().displayImg((ImageView) holder.getView(R.id.img_small2), firstHeadBean.getItemMediaList().get(2).getMediaUrl());
        }
        ImageManager.getInstance().displayCircleImg((ImageView) holder.getView(R.id.img_user_head), firstHeadBean.getIssuerHeadPic());
        holder.setText(R.id.tv_user_name, "专题");
        ((TextView) holder.getView(R.id.tv_user_name)).setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
        holder.setText(R.id.tv_read_num, firstHeadBean.getItemReadNum() + "");
        holder.setText(R.id.tv_commt_num, firstHeadBean.getItemCommentNum() + "");
       /* if (firstHeadBean.getCreateDate() != 0) {
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
        holder.getView(R.id.tv_commt_num).setVisibility(View.GONE);
        holder.getView(R.id.iv_comment).setVisibility(View.GONE);
        RelativeLayout.LayoutParams ll = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ll.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ll.rightMargin = DevAttr.dip2px(mContext, 15);
//       ImageView iv=holder.getView(R.id.iv_brow);

        holder.getView(R.id.ll_brose).setLayoutParams(ll);
//        iv.setLayoutParams(ll);


    }
}