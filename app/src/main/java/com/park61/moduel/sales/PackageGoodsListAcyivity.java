package com.park61.moduel.sales;

import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.moduel.sales.adapter.PackageGoodsListAdapter;
import com.park61.moduel.sales.bean.MerchantTradeOrder;

import java.util.ArrayList;

/**
 * 确认订单包裹商品清单
 */
public class PackageGoodsListAcyivity extends BaseActivity {
    private PackageGoodsListAdapter mAdapter;
    private ArrayList<MerchantTradeOrder> mList;
    private ListView goods_listview;
    private TextView tv_bags_num;
    private int packageNum;
    private int orderItemNum;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_orderconfirm_goodslist);
    }

    @Override
    public void initView() {
        tv_bags_num = (TextView) findViewById(R.id.tv_bags_num);
        goods_listview = (ListView) findViewById(R.id.goods_listview);
    }

    @Override
    public void initData() {
        packageNum = getIntent().getIntExtra("packageNum",packageNum);
        orderItemNum = getIntent().getIntExtra("orderItemNum",orderItemNum);
        //tv_bags_num.setText(packageNum+"个包裹(共"+orderItemNum+"件)");
        mList = (ArrayList)getIntent().getParcelableArrayListExtra("orderList");
        mAdapter = new PackageGoodsListAdapter(mContext,mList);
        goods_listview.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {

    }
}
