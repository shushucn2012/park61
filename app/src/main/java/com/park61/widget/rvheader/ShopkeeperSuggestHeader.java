package com.park61.widget.rvheader;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.adapter.RecommendGoodsListAdapter;
import com.park61.moduel.acts.bean.RecommendGoodsInfo;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.ShopkeeperSugGoodsActivity;
import com.park61.widget.list.HorizontalListViewV2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView的HeaderView，简单的展示midcate
 */
public class ShopkeeperSuggestHeader extends RelativeLayout {

    private Context mContext;
    private HorizontalListViewV2 horilistview;
    private List<RecommendGoodsInfo> list;
    private boolean isShowMore;
    private String titleImgUrl;

    public ShopkeeperSuggestHeader(Context context, List<RecommendGoodsInfo> _list, boolean isShowMore, String titleImgUrl) {
        super(context);
        this.list = _list;
        this.isShowMore = isShowMore;
        this.titleImgUrl = titleImgUrl;
        init(context);
    }

    public ShopkeeperSuggestHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShopkeeperSuggestHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        View root = inflate(context, R.layout.shopkeeper_suggest_layout, this);
        horilistview = (HorizontalListViewV2) root.findViewById(R.id.horilistview);
        View area_more = root.findViewById(R.id.area_more);
        ImageView img_title = (ImageView) root.findViewById(R.id.img_title);
        if (isShowMore) {
            area_more.setVisibility(VISIBLE);
        } else {
            area_more.setVisibility(GONE);
        }
        RecommendGoodsListAdapter mAdapter = new RecommendGoodsListAdapter(mContext, (ArrayList<RecommendGoodsInfo>) list);
        horilistview.setAdapter(mAdapter);
        horilistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mContext, GoodsDetailsActivity.class);
                it.putExtra("goodsId", list.get(position).getPmInfoid());
                it.putExtra("promotionId", list.get(position).getPromotionId());
                it.putExtra("goodsName", list.get(position).getName());
                it.putExtra("goodsOldPrice", list.get(position).getOldPrice());
                it.putExtra("goodsPrice", list.get(position).getSalesPrice());
                mContext.startActivity(it);
            }
        });
        area_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ShopkeeperSugGoodsActivity.class);
                it.putExtra("GOODS_LIST", (Serializable) list);
                mContext.startActivity(it);
            }
        });
        ImageManager.getInstance().displayImg(img_title, titleImgUrl, R.drawable.dianzhangtuijian);
    }

}