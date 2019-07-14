package com.park61.moduel.coupon.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.coupon.CouponChooseActivity;
import com.park61.moduel.coupon.adapter.MyCouponListAdapter;
import com.park61.moduel.coupon.bean.CouponBean;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠券选择fragment
 * modify by super 20181034
 */
public class CouponChooseFragment extends BaseFragment {

    private final int PAGE_SIZE = 10;
    private int PAGE_NUM = 0;

    private PullToRefreshListView mPullRefreshListView;
    private View area_nodata, bottom_bar;//无数据提示

    private List<CouponBean> list;
    private MyCouponListAdapter adapter;
    private int useType;//优惠券使用状态（0：可使用 1 不可用）
    private int eaServiceId;
    private int chosenCouponId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_mycoupon, container, false);
        useType = getArguments().getInt("useType");
        chosenCouponId = getArguments().getInt("chosenCouponId");
        eaServiceId = getArguments().getInt("eaServiceId");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        area_nodata = parentView.findViewById(R.id.area_nodata);
        bottom_bar = parentView.findViewById(R.id.bottom_bar);
        mPullRefreshListView = (PullToRefreshListView) parentView.findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, parentActivity);
        list = new ArrayList<>();
        adapter = new MyCouponListAdapter(parentActivity, list, 0, useType);
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                asyncCouponList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncCouponList();
            }
        });
        if (useType == 1) {
            bottom_bar.setVisibility(View.GONE);
        } else {
            bottom_bar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData() {
        mPullRefreshListView.setRefreshing(true);
    }

    @Override
    public void initListener() {
        if (useType == 0) {//可用优惠券列表时才能点选
            mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    list.get(position - 1).setChosen(true);
                    for (int i = 0; i < list.size(); i++) {
                        if (i != position - 1) {
                            list.get(i).setChosen(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    Intent returnData = new Intent();
                    returnData.putExtra("chosenCouponId", list.get(position - 1).getId());
                    parentActivity.setResult(Activity.RESULT_OK, returnData);
                    parentActivity.finish();
                }
            });
        }
        bottom_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.setResult(Activity.RESULT_OK, new Intent());
                parentActivity.finish();
            }
        });
    }

    /**
     * 查询优惠券列表数据
     */
    private void asyncCouponList() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getEaCanUseCoupon;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("serviceId", eaServiceId);
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            mPullRefreshListView.onRefreshComplete();
            showShortToast(errorMsg);
            if (PAGE_NUM > 0) {
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            mPullRefreshListView.onRefreshComplete();
            ArrayList<CouponBean> currentPageList = new ArrayList<>();
            JSONArray actJay = null;
            if (useType == 0) {
                actJay = jsonResult.optJSONArray("canUseList");
            } else {
                actJay = jsonResult.optJSONArray("notUseList");
            }
            if (parentActivity != null && parentActivity instanceof CouponChooseActivity) {
                ((CouponChooseActivity) parentActivity).setTopRadionText(0, jsonResult.optInt("canUseNum"));
                ((CouponChooseActivity) parentActivity).setTopRadionText(1, jsonResult.optInt("notUseNum"));
            }
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (actJay == null || actJay.length() <= 0)) {
                list.clear();
                adapter.notifyDataSetChanged();
                mPullRefreshListView.setMode(Mode.PULL_FROM_START);
                area_nodata.setVisibility(View.VISIBLE);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 0) {
                list.clear();
                mPullRefreshListView.setMode(Mode.BOTH);
                area_nodata.setVisibility(View.GONE);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                mPullRefreshListView.setMode(Mode.PULL_FROM_START);
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject couponJot = actJay.optJSONObject(i);
                CouponBean g = gson.fromJson(couponJot.toString(), CouponBean.class);
                if (chosenCouponId == g.getId()) {
                    g.setChosen(true);
                }
                currentPageList.add(g);
            }
            list.addAll(currentPageList);
            adapter.notifyDataSetChanged();
        }
    };

}
