package com.park61.common.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.CanBackWebViewActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.Log;
import com.park61.moduel.acts.bean.ActItem;
import com.park61.moduel.acts.bean.ActivitySessionVo;
import com.park61.moduel.childtest.TestHome;
import com.park61.moduel.firsthead.AudioListDetailsActivity;
import com.park61.moduel.firsthead.KindergartenActivity;
import com.park61.moduel.firsthead.SearchReaultActivity;
import com.park61.moduel.firsthead.VideoListDetailsActivity;
import com.park61.moduel.firsthead.fragment.SpecialTypeActivity;
import com.park61.moduel.firsthead.fragment.TextDetailActivity;
import com.park61.moduel.sales.bean.CmsItem;
import com.park61.moduel.shophome.ShopHomeMainV2Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ViewInitTool {

    /**
     * 初始化上下拉刷新控件 文字样式
     */
    public static void initPullToRefresh(PullToRefreshScrollView mPullRefreshScrollView, Context context) {
        ILoadingLayout startLabels = mPullRefreshScrollView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
//        Drawable ld = context.getResources().getDrawable(R.drawable.animation_list_small_loading);
//        startLabels.setLoadingDrawable(ld);

        ILoadingLayout endLabels = mPullRefreshScrollView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
    }

    /**
     * 初始化上下拉刷新控件 文字样式
     */
    public static void initPullToRefresh(PullToRefreshListView mPullRefreshListView, Context context) {
        ILoadingLayout startLabels = mPullRefreshListView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
//        Drawable ld = context.getResources().getDrawable(R.drawable.animation_list_small_loading);
//        startLabels.setLoadingDrawable(ld);

        ILoadingLayout endLabels = mPullRefreshListView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
    }

    /**
     * 初始化上下拉刷新控件 文字样式
     */
    public static void initPullToRefresh(PullToRefreshGridView mPullToRefreshGridView) {
        ILoadingLayout startLabels = mPullToRefreshGridView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = mPullToRefreshGridView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
    }

    /**
     * 初始化黏贴控件
     */
    public static void initObservableScrollView(
            final ObservableScrollView scrollView,
            final ObservableScrollView.Callbacks mCallbacks) {
        scrollView.setCallbacks(mCallbacks);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        mCallbacks.onScrollChanged(scrollView.getScrollY());
                    }
                });
        scrollView.scrollTo(0, 0);
    }

    /**
     * 设置浏览器cookie值
     *
     * @param url
     */
    public static void syncWebViewCookie(Context mContext, String url) {
        try {
            Log.d("Nat: webView.syncCookie.url", url);

            CookieSyncManager.createInstance(mContext);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(url);
            if (oldCookie != null) {
                Log.d("Nat: webView.syncCookieOutter.oldCookie", oldCookie);
            }

            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("ut=%s", GlobalParam.userToken));
            sbCookie.append(";domain=" + AppUrl.BUILD_CHART_DOMAIN);
            sbCookie.append(";path=/");

            String cookieValue = sbCookie.toString();
            Log.i("--cookieValue", cookieValue);
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();

            String newCookie = cookieManager.getCookie(url);
            if (newCookie != null) {
                Log.d("Nat: webView.syncCookie.newCookie", newCookie);
            }
        } catch (Exception e) {
            Log.e("Nat: webView.syncCookie failed", e.toString());
        }
    }

    /**
     * 初始化web设置
     */
    public static void initWebViewSetting(WebSettings webSettings) {
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置缓存模式
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setDomStorageEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        //修改ua让后台识别
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua + "; 61park/android");

        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
    }

    /**
     * 设置列表为空提示
     */
    public static void setListEmptyView(Context mContext, ListView lv) {
        TextView emptyView = new TextView(mContext);
        emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setTextColor(mContext.getResources()
                .getColor(R.color.g666666));
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setText("暂无数据");
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        lv.setEmptyView(emptyView);
    }

    /**
     * 设置列表为空提示（设置高度）
     */
    public static void setListEmptyView(Context mContext, ListView lv, int mTop) {
        TextView emptyView = new TextView(mContext);
        emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setTextColor(mContext.getResources()
                .getColor(R.color.g666666));
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setText("暂无数据");
        emptyView.setPadding(mTop, mTop, mTop, mTop);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        lv.setEmptyView(emptyView);
    }

    /**
     * 设置列表为空提示(设置图片) 有文字，图片默认
     */
    public static void setListEmptyByDefaultTipPic(Context mContext, ListView lv) {
        setListEmptyView(mContext, lv, "暂无数据", R.drawable.quexing, null, 100, 100);
    }

    /**
     * 设置列表为空提示(设置图片) 有文字，图片默认
     */
    public static void setListEmptyTipByDefaultPic(Context mContext, ListView lv, String tip) {
        setListEmptyView(mContext, lv, tip, R.drawable.quexing, null, 100, 100);
    }

    /**
     * 设置列表为空提示(设置图片) 有文字，图片默认
     */
    public static void setListEmptyTipByDefaultPic(Context mContext, LRecyclerView lv, String tip) {
        setListEmptyView(mContext, lv, tip, R.drawable.quexing, null, 100, 100);
    }

    /**
     * 设置列表为空提示(设置图片)
     */
    public static void setListEmptyView(Context mContext, ListView lv,
                                        String tip, int res, OnClickListener lsner) {
        setListEmptyView(mContext, lv, tip, res, lsner, 0, 0);
    }

    public static void setListEmptyView(Context mContext, ListView lv,
                                        String tip, int res, OnClickListener lsner, int pWidth, int pHeight) {
        LinearLayout ll = new LinearLayout(mContext);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);

        ImageView img = new ImageView(mContext);
        img.setImageResource(res);
        if (pWidth > 0 && pHeight > 0) {
            img.setLayoutParams(new LayoutParams(DevAttr.dip2px(mContext, pWidth),
                    DevAttr.dip2px(mContext, pHeight)));
        } else {
            img.setLayoutParams(new LayoutParams(DevAttr.dip2px(mContext, 200),
                    DevAttr.dip2px(mContext, 200)));
        }
        img.setOnClickListener(lsner);
        TextView emptyView = new TextView(mContext);
        emptyView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        emptyView.setTextColor(mContext.getResources().getColor(R.color.g999999));
        emptyView.setText(tip);
        setMargins(emptyView, 0, 50, 0, 0);

        ll.addView(img);
        ll.addView(emptyView);
        ll.setVisibility(View.GONE);
        ll.setTag("empty_tag");

        //-----避免重复加载空提示视图 start------//
        ViewGroup parentGroup = (ViewGroup) lv.getParent();
        boolean hasEmptyView = false;
        for (int i = 0; i < parentGroup.getChildCount(); i++) {
            if (parentGroup.getChildAt(i).getTag() != null && parentGroup.getChildAt(i).getTag().equals("empty_tag")) {
                hasEmptyView = true;
            }
        }
        //-----避免重复加载空提示视图 end------//

        if (!hasEmptyView) { // 不存在空提示视图时才加上去
            ((ViewGroup) lv.getParent()).addView(ll);
            lv.setEmptyView(ll);
        }
    }

    public static void setListEmptyView(Context mContext, LRecyclerView lv,
                                        String tip, int res, OnClickListener lsner, int pWidth, int pHeight) {
        LinearLayout ll = new LinearLayout(mContext);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);

        ImageView img = new ImageView(mContext);
        img.setImageResource(res);
        if (pWidth > 0 && pHeight > 0) {
            img.setLayoutParams(new LayoutParams(DevAttr.dip2px(mContext, pWidth),
                    DevAttr.dip2px(mContext, pHeight)));
        } else {
            img.setLayoutParams(new LayoutParams(DevAttr.dip2px(mContext, 200),
                    DevAttr.dip2px(mContext, 200)));
        }
        img.setOnClickListener(lsner);
        TextView emptyView = new TextView(mContext);
        emptyView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        emptyView.setTextColor(mContext.getResources().getColor(R.color.g999999));
        emptyView.setText(tip);
        setMargins(emptyView, 0, 50, 0, 0);

        ll.addView(img);
        ll.addView(emptyView);
        ll.setVisibility(View.GONE);
        ll.setTag("empty_tag");

        //-----避免重复加载空提示视图 start------//
        ViewGroup parentGroup = (ViewGroup) lv.getParent();
        boolean hasEmptyView = false;
        for (int i = 0; i < parentGroup.getChildCount(); i++) {
            if (parentGroup.getChildAt(i).getTag() != null && parentGroup.getChildAt(i).getTag().equals("empty_tag")) {
                hasEmptyView = true;
            }
        }
        //-----避免重复加载空提示视图 end------//

        if (!hasEmptyView) { // 不存在空提示视图时才加上去
            ((ViewGroup) lv.getParent()).addView(ll);
            lv.setEmptyView(ll);
        }
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v
                    .getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static void setEndFootView(Context mContext, ListView lv, String tip) {
        /*LinearLayout ll = new LinearLayout(mContext);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.CENTER);

        ImageView imgStart = new ImageView(mContext);
        imgStart.setLayoutParams(new LayoutParams(DevAttr.dip2px(mContext, 80), DevAttr.dip2px(mContext, 1)));
        imgStart.setBackgroundColor(mContext.getResources().getColor(R.color.colorLine));

        ImageView imgEnd = new ImageView(mContext);
        imgEnd.setLayoutParams(new LayoutParams(DevAttr.dip2px(mContext, 80), DevAttr.dip2px(mContext, 1)));
        imgEnd.setBackgroundColor(mContext.getResources().getColor(R.color.colorLine));

        TextView emptyView = new TextView(mContext);
        emptyView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        emptyView.setTextColor(mContext.getResources().getColor(R.color.g999999));
        emptyView.setText(tip);

        View leftSpaceView = new View(mContext);
        leftSpaceView.setLayoutParams(new LayoutParams(DevAttr.dip2px(mContext, 16), LayoutParams.WRAP_CONTENT));

        View RightSpaceView = new View(mContext);
        RightSpaceView.setLayoutParams(new LayoutParams(DevAttr.dip2px(mContext, 16), LayoutParams.WRAP_CONTENT));

        ll.addView(imgStart);
        ll.addView(leftSpaceView);
        ll.addView(emptyView);
        ll.addView(RightSpaceView);
        ll.addView(imgEnd);*/

        View footView = LayoutInflater.from(mContext).inflate(R.layout.listview_foot, null);
        ((TextView) footView.findViewById(R.id.tv_end_tip)).setText(tip);
        if (lv.getFooterViewsCount() == 1) {
            lv.addFooterView(footView);
        }
    }

    public static void removeEndFootView(ListView lv) {
        if (lv.getFooterViewsCount() > 1) {
            View footView = lv.getChildAt(lv.getChildCount() - 1);
            lv.removeFooterView(footView);
        }
    }

    /**
     * 显示活动详情中小课第一节课的信息
     */
    public static void initSamllClassFirstData(final Activity activity, ActivitySessionVo as, View container) {
        if (CommonMethod.isListEmpty(as.getActSessionList())) {
            container.setVisibility(View.GONE);
            return;
        } else {
            container.setVisibility(View.VISIBLE);
        }
        ActItem firstSession = as.getActSessionList().get(0);
        if (TextUtils.isEmpty(firstSession.getClassTime())) {
            ((TextView) (activity.findViewById(R.id.tv_small_info))).setText(
                    DateTool.L2SByDay3(firstSession.getActStartTime()) +
                            "开始(可报" + firstSession.getCanEnrolCount() + "节，共" + firstSession.getActClassCount() + "节)");
        } else {
            ((TextView) (activity.findViewById(R.id.tv_small_info))).setText(
                    DateTool.L2SByDay3(firstSession.getClassTime()) +
                            "开始(可报" + firstSession.getCanEnrolCount() + "节，共" + firstSession.getActClassCount() + "节)");
        }
//        ((TextView) (activity.findViewById(R.id.tv_small_applynum))).setText(
//                firstSession.getActLowQuotaLimit() + "人班，已报" + firstSession.getActNumberVisitor() + "人");
        ((TextView) (activity.findViewById(R.id.tv_small_applynum))).setText(
                firstSession.getActLowQuotaLimit() + "人班");
        String price = "";
        if (firstSession.getIsProm() == 0) {//非促销
            price = firstSession.getActPrice();
        } else if (firstSession.getIsProm() == 1) {//促销
            price = firstSession.getChildPromPrice();
        }
        ((TextView) (activity.findViewById(R.id.tv_small_class_session_price))).setText(FU.zeroToMF(price));
    }

    /**
     * 停止列表进度条
     */
    public static void listStopLoadView(PullToRefreshListView mPullRefreshListView) {
        mPullRefreshListView.onRefreshComplete();
    }

    /**
     * 将上下拉控件设为到底
     */
    public static void setPullToRefreshViewEnd(PullToRefreshListView mPullRefreshListView) {
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    /**
     * 将上下拉控件设为可上下拉
     */
    public static void setPullToRefreshViewBoth(PullToRefreshListView mPullRefreshListView) {
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    /**
     * 活动价格显示格式化
     *
     * @param act          当前活动
     * @param tv_price     现价显示控件
     * @param tv_price_old 原价显示控件
     */
    public static void fmActPrice(ActItem act, TextView tv_price, TextView tv_price_old) {
        String price = "";
        String priceOld = "";
        if (act.getIsProm() == 0) {//非促销
            price = act.getActPrice();
        } else if (act.getIsProm() == 1) { //促销
            price = act.getChildPromPrice();
            priceOld = act.getActPrice();
        }
        //现价
        if ("0.00".equals(FU.strDbFmt(price))) {
            tv_price.setText("免费");
        } else {
            String priceStrWhole = "￥" + price + "起";
            SpannableString sp = new SpannableString(priceStrWhole);
            sp.setSpan(new AbsoluteSizeSpan(10, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new AbsoluteSizeSpan(10, true), priceStrWhole.length() - 1, priceStrWhole.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_price.setText(sp);
        }
        //原价
        if ("0.00".equals(FU.strDbFmt(priceOld))) {
            tv_price_old.setText("");
        } else {
            tv_price_old.setText("￥" + priceOld);
            tv_price_old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    /**
     * 活动价格显示格式化
     *
     * @param as           场次信息
     * @param tv_price     现价显示控件
     * @param tv_price_old 原价显示控件
     */
    public static void fmActPriceInDetails(ActivitySessionVo as, TextView tv_price, TextView tv_price_old) {
        if (CommonMethod.isListEmpty(as.getActSessionList())) {
            return;
        }
        boolean isNeedQi = false;//是否需要‘起’
        //找到价格最低的游戏
        int theLowestPos = 0;
        double theLowerPrice = FU.paseDb(getActCurPrice(as.getActSessionList().get(0)));
        for (int i = 0; i < as.getActSessionList().size(); i++) {
            double nowPrice = FU.paseDb(getActCurPrice(as.getActSessionList().get(i)));
            if (nowPrice < theLowerPrice) {
                isNeedQi = true;//有大小，就有不同，必须用‘起’
                theLowerPrice = nowPrice;
                theLowestPos = i;
            }
        }
        ActItem act = as.getActSessionList().get(theLowestPos);

        String price = "";
        String priceOld = "";

        if (act.getIsProm() == 0) {//非促销
            price = act.getActPrice();
        } else if (act.getIsProm() == 1) { //促销
            price = act.getChildPromPrice();
            priceOld = act.getActPrice();
        }

        //现价
        if ("0.00".equals(FU.strDbFmt(price))) {
            String priceStrWhole = "免费";
            if (isNeedQi) {
                priceStrWhole += "起";
            }
            SpannableString sp = new SpannableString(priceStrWhole);
            if (isNeedQi) {
                sp.setSpan(new AbsoluteSizeSpan(10, true), priceStrWhole.length() - 1,
                        priceStrWhole.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tv_price.setText(sp);
        } else {
            String priceStrWhole = "￥" + price;
            if (isNeedQi) {
                priceStrWhole += "起";
            }
            SpannableString sp = new SpannableString(priceStrWhole);
            sp.setSpan(new AbsoluteSizeSpan(10, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (isNeedQi) {
                sp.setSpan(new AbsoluteSizeSpan(10, true), priceStrWhole.length() - 1,
                        priceStrWhole.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tv_price.setText(sp);
        }

        //原价
        if ("0.00".equals(FU.strDbFmt(priceOld))) {
            tv_price_old.setText("");
        } else {
            tv_price_old.setText("￥" + priceOld);
            tv_price_old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    /**
     * 通过判断是否促销，获取活动当前价格
     */
    public static String getActCurPrice(ActItem act) {
        String price = "";
        if (act.getIsProm() == 0) {//非促销
            price = act.getActPrice();
        } else if (act.getIsProm() == 1) { //促销
            price = act.getChildPromPrice();
        }
        return price;
    }

    /**
     * 判断活动场次是否可用优惠券
     */
    public static boolean getCanUseCouponByAct(ActItem actItem) {
        boolean canUseCoupon = true;
        if (actItem.getIsProm() == 0) {//非促销
            canUseCoupon = true;
        } else if (actItem.getIsProm() == 1) {//促销
            if (actItem.getCanUseCoupon() == 0) {
                canUseCoupon = false;
            } else if (actItem.getCanUseCoupon() == 1) {
                canUseCoupon = true;
            }
        }
        return canUseCoupon;
    }

    /**
     * 给输入框加小数点后只能输入2位的限制
     *
     * @param edit
     */
    public static void addEditTextLimit2AfterPoint(final EditText edit) {
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        edit.setText(s);
                        edit.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    edit.setText(s);
                    edit.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        edit.setText(s.subSequence(0, 1));
                        edit.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 给输入框增加验证按钮是否可以点击
     */
    public static void addJudgeBtnEnableListener(final List<EditText> etList, final Button okBtn) {
        okBtn.setTextColor(Color.parseColor("#FF8989"));
        okBtn.setEnabled(false);
        for (int i = 0; i < etList.size(); i++) {
            EditText edit = etList.get(i);
            edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boolean isOkBtnEnable = true;
                    Log.out("s=======" + s);
                    if (!TextUtils.isEmpty(s)) {
                        for (EditText etItem : etList) {
                            String inputStr = etItem.getText().toString().trim();
                            Log.out("inputStr=======" + inputStr);
                            if (TextUtils.isEmpty(inputStr)) {
                                isOkBtnEnable = false;
                                break;
                            }
                        }
                    } else {
                        Log.out("isOkBtnEnable1=======" + isOkBtnEnable);
                        isOkBtnEnable = false;
                    }
                    Log.out("isOkBtnEnable1=======" + isOkBtnEnable);
                    if (isOkBtnEnable) {
                        okBtn.setTextColor(Color.parseColor("#ffffff"));
                        okBtn.setEnabled(true);
                    } else {
                        okBtn.setTextColor(Color.parseColor("#FF8989"));
                        okBtn.setEnabled(false);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    /**
     * 给输入框增加验证按钮是否可以点击
     */
    public static void addJudgeBtnAndDelEnableListener(
            final List<EditText> etList, final List<ImageView> delViewList, final Button okBtn) {
        okBtn.setTextColor(Color.parseColor("#FF8989"));
        okBtn.setEnabled(false);
        for (int i = 0; i < etList.size(); i++) {
            EditText edit = etList.get(i);
            final ImageView delImg = delViewList.get(i);
            edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boolean isOkBtnEnable = true;
                    if (!TextUtils.isEmpty(s)) {
                        delImg.setVisibility(View.VISIBLE);
                        for (EditText etItem : etList) {
                            String inputStr = etItem.getText().toString().trim();
                            if (TextUtils.isEmpty(inputStr)) {
                                isOkBtnEnable = false;
                                break;
                            }
                        }
                    } else {
                        delImg.setVisibility(View.INVISIBLE);
                        isOkBtnEnable = false;
                    }
                    if (isOkBtnEnable) {
                        okBtn.setTextColor(Color.parseColor("#ffffff"));
                        okBtn.setEnabled(true);
                    } else {
                        okBtn.setTextColor(Color.parseColor("#FF8989"));
                        okBtn.setEnabled(false);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    public static void lineText(TextView tv) {
        tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }


    /**
     * 给输入框增加验证按钮是否可以点击
     */
    public static void addJudgeBtnEnable2Listener(final List<EditText> etList, final Button okBtn) {
        //okBtn.setTextColor(Color.parseColor("#FF8989"));
        okBtn.setBackgroundResource(R.drawable.rec_unpress_stroke_unpress_solid_corner30);
        okBtn.setEnabled(false);
        for (int i = 0; i < etList.size(); i++) {
            EditText edit = etList.get(i);
            edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boolean isOkBtnEnable = true;
                    Log.out("s=======" + s);
                    if (!TextUtils.isEmpty(s)) {
                        for (EditText etItem : etList) {
                            String inputStr = etItem.getText().toString().trim();
                            Log.out("inputStr=======" + inputStr);
                            if (TextUtils.isEmpty(inputStr)) {
                                isOkBtnEnable = false;
                                break;
                            }
                        }
                    } else {
                        Log.out("isOkBtnEnable1=======" + isOkBtnEnable);
                        isOkBtnEnable = false;
                    }
                    Log.out("isOkBtnEnable1=======" + isOkBtnEnable);
                    if (isOkBtnEnable) {
                        //okBtn.setTextColor(Color.parseColor("#ffffff"));
                        okBtn.setBackgroundResource(R.drawable.rec_deepred_stroke_deepred_solid_corner30);
                        okBtn.setEnabled(true);
                    } else {
                        //okBtn.setTextColor(Color.parseColor("#FF8989"));
                        okBtn.setBackgroundResource(R.drawable.rec_unpress_stroke_unpress_solid_corner30);
                        okBtn.setEnabled(false);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    public static void judgeGoWhere(CmsItem bi, Context context) {
        if (bi.getLinkType() == 1 || bi.getLinkType() == 2) {//外链和内链
            String url = bi.getLinkUrl(); // web address
            Intent intent = new Intent(context, CanBackWebViewActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("picUrl", bi.getLinkPic());
            intent.putExtra("PAGE_TITLE", bi.getTitle());
            context.startActivity(intent);
        } else if (bi.getLinkType() == 3) {//搜索
            try {
                JSONObject jsonObject = new JSONObject(bi.getLinkData());
                int id = jsonObject.optInt("id");
                int level = jsonObject.optInt("level");
                Intent intent = new Intent(context, SearchReaultActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("level", level);
                context.startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (bi.getLinkType() == 4) {//幼儿园
            if (!CommonMethod.checkUserLogin(context)) {
                return;
            }
            Intent intent = new Intent(context, KindergartenActivity.class);
            context.startActivity(intent);
        } else if (bi.getLinkType() == 5) {//乐园
            Intent intent = new Intent(context, ShopHomeMainV2Activity.class);
            context.startActivity(intent);
        } else if (bi.getLinkType() == 6) {//成长
            Intent changeIt = new Intent();
            changeIt.setAction("ACTION_TAB_CHANGE");
            changeIt.putExtra("TAB_NAME", "tab_grow");
            context.sendBroadcast(changeIt);
        } else if (bi.getLinkType() == 7) {//评测
//            if (!CommonMethod.checkUserLogin(context)) {
//                return;
//            }
//            Intent abilityIt = new Intent(context, DAPFirstV2Activity.class);
            Intent abilityIt = new Intent(context, TestHome.class);
            context.startActivity(abilityIt);
        } else if (bi.getLinkType() == 8) {//模块
           /* Intent intent = new Intent(context, ActivityList.class);
            intent.putExtra("pageId", FU.paseInt(bi.getLinkData()));
            context.startActivity(intent);*/
        } else if (bi.getLinkType() == 9) {//内容
            try {
                JSONObject jsonObject = new JSONObject(bi.getLinkData());
                long id = jsonObject.optLong("id");
                int contentType = jsonObject.optInt("contentType");
                if (contentType == 0) {//话题
                    Intent intent = new Intent(context, TextDetailActivity.class);
                    intent.putExtra("itemId", id);
                    context.startActivity(intent);
                } else if (contentType == 1) {//视频
                    Intent it = new Intent(context, VideoListDetailsActivity.class);
                    it.putExtra("itemId", id);
                    context.startActivity(it);
                } else if (contentType == 2) {//专题
                    Intent intent = new Intent(context, SpecialTypeActivity.class);
                    intent.putExtra("itemId", id);
                    context.startActivity(intent);
                } else if (contentType == 3) {//音频
                    Intent intent = new Intent(context, AudioListDetailsActivity.class);
                    intent.putExtra("itemId", id);
                    context.startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void judgeIsShow(View headview, JSONObject jsonResult) {
        if (jsonResult.toString().contains("horizontalBanner")) {
            headview.findViewById(R.id.banner_area).setVisibility(View.VISIBLE);
        } else {
            headview.findViewById(R.id.banner_area).setVisibility(View.GONE);
        }

        if (jsonResult.toString().contains("fastGoTo")) {
            headview.findViewById(R.id.area_banner2).setVisibility(View.VISIBLE);
        } else {
            headview.findViewById(R.id.area_banner2).setVisibility(View.GONE);
        }

        if (jsonResult.toString().contains("horizontalAd")) {
            headview.findViewById(R.id.area_star_baby).setVisibility(View.VISIBLE);
        } else {
            headview.findViewById(R.id.area_star_baby).setVisibility(View.GONE);
        }

        if (jsonResult.toString().contains("childrenTrainer")) {
            headview.findViewById(R.id.area_gold_teacher).setVisibility(View.VISIBLE);
        } else {
            headview.findViewById(R.id.area_gold_teacher).setVisibility(View.GONE);
        }

        if (jsonResult.toString().contains("subjectAd")) {
            headview.findViewById(R.id.area_mid_banner2).setVisibility(View.VISIBLE);
        } else {
            headview.findViewById(R.id.area_mid_banner2).setVisibility(View.GONE);
        }
    }


}
