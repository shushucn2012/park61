package com.park61.moduel.sales;

import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.moduel.sales.adapter.PackageGoodsListAdapter;
import com.park61.moduel.sales.adapter.ParentalPkgGoodsListAdapter;
import com.park61.moduel.sales.bean.MerchantTradeOrder;
import com.park61.moduel.sales.bean.ParentalOrderCfmBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 亲子活动确认订单页-订单包裹商品清单
 * createby super 20180109
 */
public class ParentalPkgGoodsListActivity extends BaseActivity {

    private ParentalPkgGoodsListAdapter mAdapter;
    private List<ParentalOrderCfmBean.ListPmInfoBean> mList;
    private ListView goods_listview;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_orderconfirm_goodslist);
    }

    @Override
    public void initView() {
        goods_listview = (ListView) findViewById(R.id.goods_listview);
    }

    @Override
    public void initData() {
        mList = (List<ParentalOrderCfmBean.ListPmInfoBean>) getIntent().getSerializableExtra("orderList");
        mAdapter = new ParentalPkgGoodsListAdapter(mContext, mList);
        goods_listview.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
    }
}
