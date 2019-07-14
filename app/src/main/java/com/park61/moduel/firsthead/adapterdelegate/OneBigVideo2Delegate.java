package com.park61.moduel.firsthead.adapterdelegate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.firsthead.VideoListDetailsActivity;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.widget.myrv.base.ItemViewDelegate;
import com.park61.widget.myrv.base.ViewHolder;

/**
 * 搜索以及纯视频列表视频大图无白边
 * Created by shubei on 2017/6/12.
 */
public class OneBigVideo2Delegate implements ItemViewDelegate<FirstHeadBean> {

    private Context mContext;

    public OneBigVideo2Delegate(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.firsthead_one_big_video2;
    }

    @Override
    public boolean isForViewType(FirstHeadBean item, int position) {
        //视频，一张大图(兼容展现类型为0的情况)
        return item.getClassifyType() == GlobalParam.FirstHeadClassifyType.VIDEIO_TYPE
                && (item.getComposeType() == GlobalParam.FirstHeadComposeType.ONE_BIG_PIC
                || item.getComposeType() == 0);
    }

    @Override
    public void convert(ViewHolder holder, final FirstHeadBean firstHeadBean, int position) {
        holder.getView(R.id.area_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, VideoListDetailsActivity.class);
                it.putExtra("itemId", firstHeadBean.getItemId());
                mContext.startActivity(it);
            }
        });
        if (!CommonMethod.isListEmpty(firstHeadBean.getItemMediaList())) {
            ImageManager.getInstance().displayImg((ImageView) holder.getView(R.id.img_big), firstHeadBean.getItemMediaList().get(0).getMediaUrl());
        }
        ImageManager.getInstance().displayCircleImg((ImageView) holder.getView(R.id.img_user_head), firstHeadBean.getIssuerHeadPic());
        holder.setText(R.id.tv_user_name, firstHeadBean.getIssuerName());
        holder.setText(R.id.tv_read_num, firstHeadBean.getItemReadNum() + "");
        holder.setText(R.id.tv_commt_num, firstHeadBean.getItemCommentNum() + "");
        holder.setText(R.id.tv_time, firstHeadBean.getDateTag());
        holder.setText(R.id.tv_title, firstHeadBean.getItemTitle());
    }
}
