package com.park61.moduel.sales.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.sales.SalesSrceeningActivity;
import com.park61.moduel.sales.adapter.BigCateListAdapter;
import com.park61.moduel.sales.adapter.SmallCateGvAdapter;
import com.park61.moduel.sales.bean.ProductCategory;
import com.park61.moduel.sales.bean.PromotionBannerData;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.viewpager.TeBuyBanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentSalesCate extends BaseFragment {

    private ListView lv_bigcate;
    private GridView gv_smallcate;
    private TeBuyBanner banner;
    private RelativeLayout banner_area;

    private List<ProductCategory> bigcateList;
    private List<ProductCategory> smallcateList;
    private BigCateListAdapter mBigCateListAdapter;
    private SmallCateGvAdapter mSmallCateGvAdapter;
    private String parentId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_catenavi_search, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        lv_bigcate = (ListView) parentView.findViewById(R.id.lv_bigcate);
        gv_smallcate = (GridView) parentView.findViewById(R.id.gv_smallcate);
        banner_area= (RelativeLayout) parentView.findViewById(R.id.banner_area);
    }

    @Override
    public void initData() {
        bigcateList = new ArrayList<ProductCategory>();
        mBigCateListAdapter = new BigCateListAdapter(parentActivity, bigcateList);
        lv_bigcate.setAdapter(mBigCateListAdapter);

        smallcateList = new ArrayList<ProductCategory>();
        mSmallCateGvAdapter = new SmallCateGvAdapter(parentActivity, smallcateList);
        gv_smallcate.setAdapter(mSmallCateGvAdapter);

        asyncGetTopNodes();
    }

    @Override
    public void initListener() {
        lv_bigcate.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBigCateListAdapter.selectItem(position);
                parentId = bigcateList.get(position).getId()+"";
                initTopBanner();
                asyncGetChildNodes(bigcateList.get(position).getId());
                mSmallCateGvAdapter.notifyDataSetChanged();
            }
        });
        gv_smallcate.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(parentActivity, SalesSrceeningActivity.class);
                it.putExtra("CATE_ID", smallcateList.get(position).getId());
                parentActivity.startActivity(it);
            }
        });
    }

    /**
     * 初始化轮播图
     */
    private void initTopBanner() {
        if (banner != null)
            banner.clear();
        banner = new TeBuyBanner(parentActivity,
                (ViewPager) parentView.findViewById(R.id.top_viewpager),
                (LinearLayout) parentView.findViewById(R.id.top_vp_dot));
        asyncBannerData();
    }
    /**
     * 请求banner数据
     */
    private void asyncBannerData() {
        String wholeUrl = AppUrl.host + AppUrl.GET_PROMOTION_BANNER;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "003");
        map.put("type", "2");
        map.put("level", "1");
        map.put("category", parentId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, bannerLsner);
    }

    BaseRequestListener bannerLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            List<PromotionBannerData> bannerlList = new ArrayList<PromotionBannerData>();
            JSONArray jay = jsonResult.optJSONArray("list");
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                PromotionBannerData bi = gson.fromJson(jot.toString(), PromotionBannerData.class);
                bannerlList.add(bi);
            }
            if(bannerlList.size()<1){
                banner_area.setVisibility(View.GONE);
            }else{
                banner_area.setVisibility(View.VISIBLE);
                banner.init(parentActivity, bannerlList);
            }
        }
    };

    /**
     * 获取顶级类目
     */
    protected void asyncGetTopNodes() {
        String wholeUrl = AppUrl.host + AppUrl.GET_TOP_NODE;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getTopNodesLsner);
    }

    BaseRequestListener getTopNodesLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray jay = jsonResult.optJSONArray("list");
            bigcateList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                ProductCategory p = gson.fromJson(jot.toString(), ProductCategory.class);
                bigcateList.add(p);
            }
            mBigCateListAdapter.notifyDataSetChanged();
            if (bigcateList.size() > 0){
                asyncGetChildNodes(bigcateList.get(0).getId());
                parentId = bigcateList.get(0).getId()+"";
                initTopBanner();
            }
        }
    };

    /**
     * 获取次级类目
     */
    protected void asyncGetChildNodes(Long parentId) {
        String wholeUrl = AppUrl.host + AppUrl.GET_CHILD_NODE;
        String requestBodyData = ParamBuild.getChildNodes(parentId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getChildNodesLsner);
    }

    BaseRequestListener getChildNodesLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray jay = jsonResult.optJSONArray("list");
            smallcateList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                ProductCategory p = gson.fromJson(jot.toString(), ProductCategory.class);
                smallcateList.add(p);
            }
            mSmallCateGvAdapter.notifyDataSetChanged();
        }
    };

}
