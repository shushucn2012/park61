package com.park61.moduel.sales.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.CommonMethod;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.SecKillActivity;
import com.park61.moduel.sales.adapter.SecGoodsListAdapter;
import com.park61.moduel.sales.bean.ProductLimit;
import com.park61.moduel.sales.bean.PromotionPageBean;
import com.park61.moduel.sales.cache.SecKillDTO;
import com.park61.widget.list.ListViewForScrollView;
import com.park61.widget.textview.SecTimeTextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class FragmentSecKillNew extends BaseFragment implements SecTimeTextView.OnTimeEnd {

    private TextView tv_msg, tv_start_label;
    private View layout;
    private TextView tv_empty_tip;
    private SecTimeTextView tv_time;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private ListViewForScrollView listview_goods;

    private SecGoodsListAdapter mAdapter;
    private PromotionPageBean mPromotionPageBean;
    private int index = -1;
    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_sec_kill, container, false);
        id = getArguments().getString("id");
        mPromotionPageBean = SecKillDTO.getPageBean(id);
        index = getArguments().getInt("index");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        tv_msg = (TextView) parentView.findViewById(R.id.tv_msg);
        tv_start_label = (TextView) parentView.findViewById(R.id.tv_start_label);
        tv_empty_tip = (TextView) parentView.findViewById(R.id.tv_empty_tip);
        layout = parentView.findViewById(R.id.layout);
        tv_time = (SecTimeTextView) parentView.findViewById(R.id.tv_time);
        listview_goods = (ListViewForScrollView) parentView.findViewById(R.id.listview_goods);
        listview_goods.setFocusable(false);
        mPullRefreshScrollView = (PullToRefreshScrollView) parentView.findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    @Override
    public void initData() {
        mAdapter = new SecGoodsListAdapter(parentActivity,
                mPromotionPageBean.getProductLimit(), mPromotionPageBean.getStart(), mPromotionPageBean.getMsg());
        listview_goods.setAdapter(mAdapter);
        if (TextUtils.isEmpty(mPromotionPageBean.getCountDownTime())
                || mPromotionPageBean.getCountDownTime().equals("0")) {
            tv_start_label.setVisibility(View.GONE);
            tv_time.setVisibility(View.GONE);
        } else {
            tv_start_label.setVisibility(View.VISIBLE);
            long ms = Long.parseLong(mPromotionPageBean.getCountDownTime());
            Log.out("ms======"+ms);
            String[] ss = CommonMethod.formatMs(ms).split(":");
            String h = ss[0];
            String m = ss[1];
            String s = ss[2];
            if (h.substring(0, 1).equals("0")) {
                h = h.substring(1);
            }
            if (m.substring(0, 1).equals("0")) {
                m = m.substring(1);
            }
            if (s.substring(0, 1).equals("0")) {
                s = s.substring(1);
            }
            tv_time.setTimes(new long[]{0, Long.parseLong(h), Long.parseLong(m), Long.parseLong(s)});
           /* if(index == 0) {
                tv_time.setTimes(new long[]{0, 0, 0, 30});
            }*/
            if (!tv_time.isRun()) {
                tv_time.run();
            }
        }
    }

    @Override
    public void initListener() {
        listview_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent it = new Intent(parentActivity, GoodsDetailsActivity.class);
                ProductLimit pl = mPromotionPageBean.getProductLimit().get(position);
                it.putExtra("goodsId", pl.getPmInfoid());
                it.putExtra("goodsName", pl.getName());
                it.putExtra("goodsPrice", pl.getSalesPrice() + "");
                it.putExtra("goodsOldPrice", pl.getOldPrice() + "");
                it.putExtra("promotionId", pl.getPromotionId());
                parentActivity.startActivity(it);
            }
        });
        tv_time.setOnTimeEndLsner(this);
    }

    @Override
    public void onEnd() {
        tv_start_label.setVisibility(View.GONE);
        tv_time.setVisibility(View.GONE);
        showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((SecKillActivity) parentActivity).asyncGetPromotionMerge();
                dismissDialog();
            }
        }, 3 * 1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tv_time.setRun(false);
        tv_time.setOnTimeEndLsner(null);
    }
}
