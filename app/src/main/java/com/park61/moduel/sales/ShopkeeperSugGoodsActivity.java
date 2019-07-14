package com.park61.moduel.sales;

import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.bean.RecommendGoodsInfo;
import com.park61.moduel.sales.adapter.GoodsListAdapter;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.moduel.sales.bean.ProductLimit;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 店长推荐商品列表页面
 */
public class ShopkeeperSugGoodsActivity extends BaseActivity {

    private List<ProductLimit> goodsDataList = new ArrayList<>();
    private List<GoodsCombine> goodsCombList = new ArrayList<>();
    private PullToRefreshListView mPullRefreshListView;
    private GoodsListAdapter adapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_recommend_goods);
    }

    @Override
    public void initView() {
        TextView tv_page_title = (TextView) findViewById(R.id.tv_page_title);
        tv_page_title.setText("爆品推荐");
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    @Override
    public void initData() {
        List<RecommendGoodsInfo> recommendGoodsInfoList =
                (List<RecommendGoodsInfo>) getIntent().getSerializableExtra("GOODS_LIST");
        for (int i = 0; i < recommendGoodsInfoList.size(); i++) {
            ProductLimit productLimit = new ProductLimit();
            RecommendGoodsInfo rgInfoItem = recommendGoodsInfoList.get(i);
            productLimit.setPmInfoid(rgInfoItem.getPmInfoid());
            productLimit.setImg(rgInfoItem.getImg());
            productLimit.setName(rgInfoItem.getName());
            productLimit.setSalesPrice(new BigDecimal(rgInfoItem.getSalesPrice()));
            productLimit.setOldPrice(new BigDecimal(rgInfoItem.getOldPrice()));
            goodsDataList.add(productLimit);
        }
        setGoodsToCombList();
        adapter = new GoodsListAdapter(mContext, goodsCombList, null);
        mPullRefreshListView.setAdapter(adapter);
    }

    /**
     * 把商品列表的数据填充到商品联合bean列表
     */
    public void setGoodsToCombList() {
        goodsCombList.clear();
        for (int i = 0; i < goodsDataList.size(); i = i + 2) {
            GoodsCombine comb = new GoodsCombine();
            if (goodsDataList.get(i) != null) {
                comb.setGoodsLeft(goodsDataList.get(i));
            }
            if (i + 1 < goodsDataList.size() && goodsDataList.get(i + 1) != null) {
                comb.setGoodsRight(goodsDataList.get(i + 1));
            }
            goodsCombList.add(comb);
        }
    }

    @Override
    public void initListener() {
    }

    /**
     * 停止列表进度条
     */
    protected void listStopLoadView() {
        mPullRefreshListView.onRefreshComplete();
    }


}
