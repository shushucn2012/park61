package com.park61.moduel.sales.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.StoreTasteActivity;
import com.park61.moduel.sales.BrandShowActivity;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.bean.ProductDetail;
import com.park61.moduel.sales.bean.ProductEvaluate;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.AutoLinefeedLayout;
import com.park61.widget.pw.GoodsDetailsDeAddrPopWin;
import com.park61.widget.pw.GoodsPromotionsPopWin;
import com.park61.widget.textview.GroupTimeTextView;
import com.park61.widget.viewpager.GoodsBanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentGoodsTop extends BaseFragment implements GroupTimeTextView.OnTimeEnd {

    private PullToRefreshScrollView mPullRefreshScrollView;
    public GoodsBanner banner;// 轮播图控件
    private GoodsDetailsDeAddrPopWin mGoodsDetailsDeAddrPopWin;//地址选择pw
    private GoodsPromotionsPopWin mGoodsPromotionsPopWin;
    public TextView tv_goods_name, tv_goods_price, tv_old_price, tv_brand, tv_deaddr, tv_ishas, tv_mj_title, tv_description_point,
            tv_server_point, tv_speed_point;
    private ImageView img_main_product;
    private View area_mj, area_brand, area_taste, area_choose_deaddr, line, area_countdown;
    public ScrollView scrollView;
    private AutoLinefeedLayout view_wordwrap;
    private GroupTimeTextView tv_countdown;

    private ProductDetail detail;// 商品概要信息
    private ProductEvaluate point;// 商品评分信息
    private String countDown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_goodstop, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (parentActivity != null && ((GoodsDetailsActivity) parentActivity).img_to_top != null) {
                ((GoodsDetailsActivity) parentActivity).img_to_top.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void initView() {
        // 初始化上下拉刷新控件
        mPullRefreshScrollView = (PullToRefreshScrollView) parentView.findViewById(R.id.pull_refresh_scrollview);
        ILoadingLayout endLabels = mPullRefreshScrollView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉查看图文详情");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("放开查看图文详情");// 刷新时
        endLabels.setReleaseLabel("放开查看图文详情");// 下来达到一定距离时，显示的提示
        endLabels.setLoadingDrawable(null);
        initTopBanner();

        tv_goods_name = (TextView) parentView.findViewById(R.id.tv_goods_name);
        tv_goods_price = (TextView) parentView.findViewById(R.id.tv_goods_price);
        tv_old_price = (TextView) parentView.findViewById(R.id.tv_old_price);
        tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_brand = (TextView) parentView.findViewById(R.id.tv_brand);
        img_main_product = (ImageView) parentView.findViewById(R.id.img_main_product);
        tv_deaddr = (TextView) parentView.findViewById(R.id.tv_deaddr);
        tv_ishas = (TextView) parentView.findViewById(R.id.tv_ishas);
        tv_mj_title = (TextView) parentView.findViewById(R.id.tv_mj_title);
        area_mj = parentView.findViewById(R.id.area_mj);
        area_countdown = parentView.findViewById(R.id.area_countdown);
        tv_countdown = (GroupTimeTextView) parentView.findViewById(R.id.tv_countdown);
        area_choose_deaddr = parentView.findViewById(R.id.area_choose_deaddr);

        tv_description_point = (TextView) parentView.findViewById(R.id.tv_description_point);
        tv_server_point = (TextView) parentView.findViewById(R.id.tv_server_point);
        tv_speed_point = (TextView) parentView.findViewById(R.id.tv_speed_point);
        area_brand = parentView.findViewById(R.id.area_brand);
        area_taste = parentView.findViewById(R.id.area_taste);
        line = parentView.findViewById(R.id.line);

        scrollView = (ScrollView) mPullRefreshScrollView.getRefreshableView();

        view_wordwrap = (AutoLinefeedLayout) parentView.findViewById(R.id.view_wordwrap);
    }

    /**
     * 初始化轮播图
     */
    private void initTopBanner() {
        ViewPager vp = (ViewPager) parentView.findViewById(R.id.top_viewpager);
        LinearLayout ll = (LinearLayout) parentView.findViewById(R.id.top_vp_dot);
        banner = new GoodsBanner(parentActivity, vp, ll, (TextView) parentView.findViewById(R.id.tv_banner_pos_and_total));
    }

    @Override
    public void initData() {
        detail = ((GoodsDetailsActivity) parentActivity).productDetail;
        try {
            tv_goods_name.setText(detail.getPmInfo().getProduct().getProductCname());
            tv_goods_price.setText(FU.formatBigPrice(detail.getPmPrice().getCurrentUnifyPrice()));
            tv_old_price.setText(FU.formatPrice(detail.getPmPrice().getMarketPrice()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (detail.getProductBrand() != null) {// 品牌数据
            ImageManager.getInstance().displayImg(img_main_product, detail.getProductBrand().getBrandLogoUrl());
            tv_brand.setText(detail.getProductBrand().getBrandName());
        }
        if (detail.getProductDeliverRegionVo() != null) { // 当前收货地址
            tv_deaddr.setText(detail.getProductDeliverRegionVo().getShowDeliverRegionName());
        }
        if (detail.isBuy()) {
            tv_ishas.setText("现货");
        } else {
            tv_ishas.setText("无货");
        }
        if (((GoodsDetailsActivity) parentActivity).bannerPicUrlList.size() > 0) {
            banner.init(((GoodsDetailsActivity) parentActivity).bannerPicUrlList);
        }

        //显示促销信息
        if (!CommonMethod.isListEmpty(detail.getDiscountActivity())) {
            tv_mj_title.setText(detail.getDiscountActivity().get(0).getTitle());
            area_mj.setVisibility(View.VISIBLE);
            area_countdown.setVisibility(View.VISIBLE);

            long time = FU.paseLong(detail.getDiscountActivity().get(0).getEndDate()) - System.currentTimeMillis();
            countDown = CommonMethod.formatMss(time);
            if (countDown.equals("0:00:00:00")) {
                area_countdown.setVisibility(View.GONE);
                tv_countdown.setVisibility(View.GONE);
            } else {
                String[] ss = countDown.split(":");
                String d = ss[0];
                String h = ss[1];
                String m = ss[2];
                String s = ss[3];
                if (d.substring(0, 1).equals("0")) {
                    d = h.substring(1);
                }
                if (h.substring(0, 1).equals("0")) {
                    h = h.substring(1);
                }
                if (m.substring(0, 1).equals("0")) {
                    m = m.substring(1);
                }
                if (s.substring(0, 1).equals("0")) {
                    s = s.substring(1);
                }
                tv_countdown.setTimes(new long[]{Long.parseLong(d), Long.parseLong(h), Long.parseLong(m), Long.parseLong(s)});
                if (!tv_countdown.isRun()) {
                    tv_countdown.run();
                }
            }
        } else {
            area_mj.setVisibility(View.GONE);
            area_countdown.setVisibility(View.GONE);
        }
        setCanDoFuWu();
        asyncGetGoodsPoints();
        asyncActStoreTaste();
    }

    @Override
    public void initListener() {
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                mPullRefreshScrollView.onRefreshComplete();
                ((GoodsDetailsActivity) parentActivity).getThisViewPager().setPageTransformer(true, new UpDownPageTransformer());
                ((GoodsDetailsActivity) parentActivity).getThisViewPager().setCurrentItem(1, true);
            }
        });
        area_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detail == null || detail.getProductBrand() == null) {
                    return;
                }
                Intent intent = new Intent(parentActivity, BrandShowActivity.class);
                intent.putExtra("brandTitle", detail.getProductBrand().getBrandName());
                intent.putExtra("productBrandId", detail.getProductBrand().getId());
                startActivity(intent);
            }
        });
        area_mj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoodsPromotionsPopWin = new GoodsPromotionsPopWin(parentActivity,
                        parentActivity.findViewById(R.id.cover), detail.getDiscountActivity());
                mGoodsPromotionsPopWin.showAtLocation(
                        parentActivity.findViewById(R.id.root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        area_taste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parentActivity, StoreTasteActivity.class);
                intent.putExtra("pmInfoId", detail.getPmInfo().getId());
                startActivity(intent);
            }
        });
        area_choose_deaddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoodsDetailsDeAddrPopWin = new GoodsDetailsDeAddrPopWin(parentActivity,
                        new GoodsDetailsDeAddrPopWin.OnShopSelectLsner() {
                            @Override
                            public void onShopSelect(Long id, String name) {
                                ((GoodsDetailsActivity) parentActivity).asyncGetGoodsTopInfo(id);
                                mGoodsDetailsDeAddrPopWin.dismiss();
                            }
                        }, parentActivity.findViewById(R.id.cover));
                mGoodsDetailsDeAddrPopWin.showAtLocation(parentActivity.findViewById(R.id.root),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    /**
     * 获取商品评分
     */
    protected void asyncGetGoodsPoints() {
        String wholeUrl = AppUrl.host + AppUrl.GET_GOODS_POINTS;
        String requestBodyData = ParamBuild.getGoodsDetails(detail.getPmInfo().getId(), null, null);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getGoodsPointsLsner);
    }

    BaseRequestListener getGoodsPointsLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            point = gson.fromJson(jsonResult.toString(), ProductEvaluate.class);
            setPoints();
        }
    };

    protected void setPoints() {
        tv_description_point.setText(point.getConformityScore() + "");
        tv_server_point.setText(point.getServeScore() + "");
        tv_speed_point.setText(point.getDeliveryScore() + "");
    }

    private void setCanDoFuWu() {
        for (int i = 0; i < detail.getPmInfoServicetype().size(); i++) {
            LinearLayout ll = new LinearLayout(parentActivity);
            ll.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, DevAttr.dip2px(parentActivity, 25)));
            ll.setGravity(Gravity.CENTER);

            ImageView img = new ImageView(parentActivity);
            img.setImageResource(R.drawable.goods_fuwu_icon);
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                    DevAttr.dip2px(parentActivity, 15), DevAttr.dip2px(parentActivity, 15));
            img.setLayoutParams(linearParams);
            ll.addView(img);

            View view_img_space = new View(parentActivity);
            LinearLayout.LayoutParams linearParamsImg = new LinearLayout.LayoutParams(
                    DevAttr.dip2px(parentActivity, 5), DevAttr.dip2px(parentActivity, 5));
            view_img_space.setLayoutParams(linearParamsImg);
            ll.addView(view_img_space);

            TextView textview = new TextView(parentActivity);
            textview.setText(detail.getPmInfoServicetype().get(i).getName());
            textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textview.setTextColor(getResources().getColor(R.color.g999999));
            ll.addView(textview);

            View view = new View(parentActivity);
            LinearLayout.LayoutParams linearParams2 = new LinearLayout.LayoutParams(
                    DevAttr.dip2px(parentActivity, 12), DevAttr.dip2px(parentActivity, 12));
            view.setLayoutParams(linearParams2);
            ll.addView(view);

            view_wordwrap.addView(ll);
        }
    }

    /**
     * 到店体验游戏列表
     */
    private void asyncActStoreTaste() {
        String wholeUrl = AppUrl.host + AppUrl.ACT_STATE;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pmId", detail.getPmInfo().getId() + "");
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray actJay = jsonResult.optJSONArray("list");
            Log.out("jsonResult=========" + jsonResult.toString());
            if (actJay == null || actJay.length() < 1) {
                area_taste.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
            } else {
                area_taste.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onEnd() {
        area_countdown.setVisibility(View.GONE);
        tv_countdown.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tv_countdown.setRun(false);
        tv_countdown.setOnTimeEndLsner(null);
    }

}
