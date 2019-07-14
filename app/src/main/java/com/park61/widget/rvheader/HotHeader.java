package com.park61.widget.rvheader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.bean.TopicBean;

import java.util.List;

/**
 * RecyclerView的HeaderView，简单的展示HotHeader
 */
public class HotHeader extends RelativeLayout {

    private List<TopicBean> topicList;
    private Context mContext;
    private View root;
    private DisplayImageOptions options;

    public HotHeader(Context context,List<TopicBean> _topicList) {
        super(context);
        this.topicList = _topicList;
        this.mContext = context;
        init(context);
    }

    public HotHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HotHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(final Context context) {
        root = inflate(context, R.layout.main_act_hot_layout, this);
        fillData();
    }

    public void fillData(){
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.img_default_h)
                .showImageOnLoading(R.drawable.img_default_h)
                .showImageOnFail(R.drawable.img_default_h)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .delayBeforeLoading(100)
                .build();
        for (int pos = 0; pos < Math.min(topicList.size(), 5); pos++) {
            int resIdTitle = getResources().getIdentifier("tv_area" + pos + "_title", "id", mContext.getPackageName());
            int resIdDes = getResources().getIdentifier("tv_area" + pos + "_des", "id", mContext.getPackageName());
            int resIdImg = getResources().getIdentifier("img_area" + pos, "id", mContext.getPackageName());
            ((TextView) root.findViewById(resIdTitle)).setText(topicList.get(pos).getMotifName());
            ((TextView) root.findViewById(resIdDes)).setText(topicList.get(pos).getMotifDescription());
            ImageManager.getInstance().displayImg((ImageView) root.findViewById(resIdImg), topicList.get(pos).getPicUrl());
            final int index = pos;
            root.findViewById(resIdImg).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
        for(int pos = Math.min(topicList.size(), 5); pos < 5; pos++){
            int resIdTitle = getResources().getIdentifier("tv_area" + pos + "_title", "id", mContext.getPackageName());
            int resIdDes = getResources().getIdentifier("tv_area" + pos + "_des", "id", mContext.getPackageName());
            int resIdImg = getResources().getIdentifier("img_area" + pos, "id", mContext.getPackageName());
            ((TextView) root.findViewById(resIdTitle)).setText("");
            ((TextView) root.findViewById(resIdDes)).setText("");
            ((ImageView) root.findViewById(resIdImg)).setImageResource(R.drawable.img_default_h);
            root.findViewById(resIdImg).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }

    public void refreshData(List<TopicBean> _topicList){
        this.topicList = _topicList;
        fillData();
    }

}