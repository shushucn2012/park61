package com.park61.moduel.acts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.bean.ActItem;

import java.util.List;

public class ActTempRvAdapter extends ListBaseAdapter<ActItem> {

    private List<ActItem> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ActTempRvAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TheViewHolder(mLayoutInflater.inflate(R.layout.acttemplist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ActItem item = mDataList.get(position);
        TheViewHolder viewHolder = (TheViewHolder) holder;
        //标题
        viewHolder.tv_act_title.setText(item.getActTitle());
        //价格
        ViewInitTool.fmActPrice(item, viewHolder.tv_act_price, viewHolder.tv_act_price_old);
        //举办方
        if (TextUtils.isEmpty(item.getActReleaseAuthor())) {
            viewHolder.tv_author.setVisibility(View.GONE);
        } else {
            viewHolder.tv_author.setVisibility(View.VISIBLE);
            viewHolder.tv_author.setText(item.getActReleaseAuthor());
        }
        //累计
        viewHolder.tv_leiji.setText("累计" + (item.getGrandTotal() == null ? 0 : item.getGrandTotal()) + "人报名");
        //封面图
        ImageManager.getInstance().displayImg(viewHolder.act_img, item.getActCover(), R.drawable.img_default_h);
    }

    private class TheViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_act_title;
        private ImageView act_img;
        private TextView tv_act_price;
        private TextView tv_leiji;
        private TextView tv_author;
        private TextView tv_act_price_old;

        public TheViewHolder(View itemView) {
            super(itemView);
            tv_act_title = (TextView) itemView.findViewById(R.id.tv_act_title);
            act_img = (ImageView) itemView.findViewById(R.id.act_img);
            tv_act_price = (TextView) itemView.findViewById(R.id.tv_act_price);
            tv_leiji = (TextView) itemView.findViewById(R.id.tv_leiji);
            tv_author = (TextView) itemView.findViewById(R.id.tv_author);
            tv_act_price_old = (TextView) itemView.findViewById(R.id.tv_act_price_old);
        }
    }
}
