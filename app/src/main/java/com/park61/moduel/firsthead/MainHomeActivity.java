package com.park61.moduel.firsthead;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.PermissionHelper;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.MipcaActivityCapture;
import com.park61.moduel.childtest.TestHome;
import com.park61.moduel.firsthead.adapter.FirstHeadAdapter;
import com.park61.moduel.firsthead.adapter.FlagGvAdapter;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.moduel.firsthead.bean.FirstHeadChildBean;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.moduel.msg.MsgActivity;
import com.park61.moduel.sales.adapter.StarBabyListAdapter;
import com.park61.moduel.sales.bean.CmsItem;
import com.park61.moduel.sales.bean.GoldTeacher;
import com.park61.moduel.sales.bean.TabBean;
import com.park61.net.base.Request;
import com.park61.net.request.SimpleRequestListener;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.list.HorizontalListViewV2;
import com.park61.widget.viewpager.CmsBanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by shubei on 2017/11/10.
 * 首页
 */
public class MainHomeActivity extends BaseActivity {

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;
    public static final String COMMENT_SUCCESS = "com.park61.commentSuccess";
    private static final int SCANNIN_GREQUEST_CODE = 1;// 扫描二维码请求

    private int PAGE_NUM = 0;
    private static final int PAGE_SIZE = 6;
    private int totalPage = 100;
    //private int mDistanceY = 0;//滑动的距离
    private boolean isStickyShow = true;
    private int curModuleId;
    private int curTabPos;
    private int goldTeacherPageIndex = 0;

    private FirstHeadAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    protected List<FirstHeadBean> dataList = new ArrayList<>();

    private List<GoldTeacher> gList = new ArrayList<>();
    private List<TabBean> tbList = new ArrayList<>();//标签列表
    private List<CmsItem> adList = new ArrayList<>();//创造力之星列表

    private LRecyclerView rv_firsthead;
    private CmsBanner mp0;
    private View headview, stickyview, area_sticky_bar, area_sticky_top_space, area_change_teacher, serach_area, area_feilei, area_gv_label;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout hscrollview_linear, area_quick3;
    private LinearLayout linear_gold_teacher;
    private TextView tv_gold_teacher, tv_msg_remind, edit_sousuo;
    private HorizontalListViewV2 horilistview;
    private HorizontalScrollView hscrollview;
    private ImageView img_msg, img_scan, img_fenlei;
    private GridView gv_label;
    private FlagGvAdapter mFlagGvAdapter;
    private boolean isFirstIn = true;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_main_home);
    }

    @Override
    public void initView() {
        headview = LayoutInflater.from(mContext).inflate(R.layout.main_headview, null);
        rv_firsthead = (LRecyclerView) findViewById(R.id.rv_firsthead);
        linearLayoutManager = new LinearLayoutManager(mContext);
        rv_firsthead.setLayoutManager(linearLayoutManager);
        area_sticky_bar = findViewById(R.id.area_sticky_bar);

        stickyview = LayoutInflater.from(mContext).inflate(R.layout.main_stickyview, null);
        area_sticky_top_space = findViewById(R.id.area_sticky_top_space);
        hscrollview_linear = (LinearLayout) findViewById(R.id.hscrollview_linear);
        tv_msg_remind = (TextView) findViewById(R.id.tv_msg_remind);
        img_msg = (ImageView) findViewById(R.id.img_msg);
        img_scan = (ImageView) findViewById(R.id.img_scan);
        serach_area = findViewById(R.id.serach_area);
        edit_sousuo = (TextView) findViewById(R.id.edit_sousuo);
        area_feilei = findViewById(R.id.area_feilei);
        area_gv_label = findViewById(R.id.area_gv_label);
        gv_label = (GridView) findViewById(R.id.gv_label);
        img_fenlei = (ImageView) findViewById(R.id.img_fenlei);
        hscrollview = (HorizontalScrollView) findViewById(R.id.hscrollview);
        area_quick3 = (LinearLayout) headview.findViewById(R.id.area_quick3);
        area_quick3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainHomeActivity.this, TestHome.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        adapter = new FirstHeadAdapter(mContext, dataList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rv_firsthead.setAdapter(mLRecyclerViewAdapter);

        mFlagGvAdapter = new FlagGvAdapter(tbList, mContext);
        gv_label.setAdapter(mFlagGvAdapter);

        asyncGetParkHomePage();

        asyncSaveAppDeviceToken();
        if (GlobalParam.userToken != null) {
            asyncUpdateTkoenExpireTime();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(COMMENT_SUCCESS);
        registerReceiver(commentSuccessBroadcast, filter);
    }

    BroadcastReceiver commentSuccessBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新评论数量
            long itemId = intent.getLongExtra("itemId", -1);
            if (itemId != -1 && dataList != null) {
                for (FirstHeadBean b : dataList) {
                    if (b.getItemId() == itemId) {
                        b.setItemCommentNum(b.getItemCommentNum() + 1);
                        mLRecyclerViewAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    };

    /*@Override
    protected void onResume() {
        super.onResume();
        if (!isFirstIn) {
            mLRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            isFirstIn = false;
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(commentSuccessBroadcast);
    }

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(MainHomeActivity.this);
        rv_firsthead.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                asyncGetParkHomePage();
            }
        });
        rv_firsthead.setPullRefreshEnabled(true);
        rv_firsthead.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (PAGE_NUM < totalPage - 1) {
                    getNextPage();
                } else {
                    rv_firsthead.setNoMore(true);
                }
            }
        });

        rv_firsthead.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
                View view_sticky = linearLayoutManager.findViewByPosition(2);
                if (view_sticky != null) {
                    if (view_sticky.getTop() < 100) {
                        findViewById(R.id.top_bar).setVisibility(GONE);
                    } else {
                        findViewById(R.id.top_bar).setVisibility(VISIBLE);
                    }
                    if (isStickyShow) {
                        area_sticky_bar.setVisibility(VISIBLE);
                    } else {
                        area_sticky_bar.setVisibility(GONE);
                    }
                    //logout("view_sticky.getTop()============================" + view_sticky.getTop());
                    area_sticky_bar.setY(Math.max(0, view_sticky.getTop() - DevAttr.dip2px(mContext, 24)));
                    if (view_sticky.getTop() <= 0) {
                        area_sticky_top_space.setBackgroundColor(mContext.getResources().getColor(R.color.gffffff));
                    } else {
                        area_sticky_top_space.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                    }
                } else {
                    if (linearLayoutManager.findFirstVisibleItemPosition() < 2) {
                        area_sticky_bar.setVisibility(GONE);
                    } else {
                        area_sticky_bar.setY(0);
                    }
                }

                //logout("distanceY==================" + distanceY);

                View viewFirst = linearLayoutManager.findViewByPosition(1);
                if (viewFirst != null) {
                    // 控制TOPBAR的渐变效果
                    //logout("viewFirst.getTop()============================" + viewFirst.getTop());
                    if ((0 - viewFirst.getTop()) > DevAttr.dip2px(mContext, 185)) {
                        findViewById(R.id.top_bar).setBackgroundColor(mContext.getResources().getColor(R.color.gffffff));
                        findViewById(R.id.area_top_bar_space).setBackgroundColor(mContext.getResources().getColor(R.color.gffffff));
                        img_scan.setImageResource(R.drawable.icon_scan_red);
                        img_msg.setImageResource(R.drawable.icon_msg_red);
                        serach_area.setBackgroundResource(R.drawable.rec_gray_stroke_gray_solid_corner30);
                    } else if ((0 - viewFirst.getTop()) > DevAttr.dip2px(mContext, 145)) {
                        findViewById(R.id.top_bar).setBackgroundColor(Color.parseColor("#66ffffff"));
                    } else {
                        findViewById(R.id.top_bar).setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                        findViewById(R.id.area_top_bar_space).setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                        img_scan.setImageResource(R.drawable.icon_scan_white);
                        img_msg.setImageResource(R.drawable.icon_msg_white);
                        serach_area.setBackgroundResource(R.drawable.rec_white_stroke_white_solid_corner30);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(int state) {

            }
        });
        edit_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, TopicSearchActivity.class));
            }
        });
        img_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalParam.userToken == null) {// 没有登录则跳去登录
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                startActivity(new Intent(mContext, MsgActivity.class));
            }
        });
        img_scan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                permissionHelper.setOnAlterApplyPermission(new PermissionHelper.OnAlterApplyPermission() {
                    @Override
                    public void OnAlterApplyPermission() {
                        goToScaner();
                    }
                });
                permissionHelper.setRequestPermission(permissionModels);
                if (Build.VERSION.SDK_INT < 23) {//6.0以下，不需要动态申请
                    goToScaner();
                } else {//6.0+ 需要动态申请
                    //判断是否全部授权过
                    if (permissionHelper.isAllApplyPermission()) {//申请的权限全部授予过，直接运行
                        goToScaner();
                    } else {//没有全部授权过，申请
                        //permissionHelper.applyPermission();
                        showShortToast("相机权限未开启，请在应用设置页面授权！");
                    }
                }
            }
        });
        area_feilei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (area_gv_label.getVisibility() == VISIBLE) {
                    area_gv_label.setVisibility(GONE);
                    img_fenlei.setImageResource(R.drawable.icon_home_tab_fenlei);
                    findViewById(R.id.cover).setVisibility(GONE);
                } else {
                    area_gv_label.setVisibility(VISIBLE);
                    mFlagGvAdapter.notifyDataSetChanged();
                    img_fenlei.setImageResource(R.drawable.icon_home_tab_fenlei_red);
                    findViewById(R.id.cover).setVisibility(VISIBLE);
                }
            }
        });
        gv_label.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainHomeViewHelper.hScrollviewChangeOthers(hscrollview_linear, mContext, position);
                chosenTab(position);
                hscrollview.smoothScrollBy((position - curTabPos) * DevAttr.dip2px(mContext, 80), 0);
                curTabPos = position;
                curModuleId = tbList.get(position).getModuleId();
                refreshList();
                area_gv_label.setVisibility(GONE);
                findViewById(R.id.cover).setVisibility(GONE);
                img_fenlei.setImageResource(R.drawable.icon_home_tab_fenlei);
            }
        });
    }

    /**
     * 去扫码
     */
    public void goToScaner() {
        Intent intent = new Intent();
        intent.setClass(mContext, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        permissionHelper.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCANNIN_GREQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String str = bundle.getString("result");
            CommonMethod.dealWithScanBack(str, mContext);
        }
    }

    /**
     * 获得首页动态配置内容
     */
    private void asyncGetParkHomePage() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.loadHomePageData;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, blistener);
    }

    BaseRequestListener blistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            rv_firsthead.refreshComplete(PAGE_SIZE);
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            ViewInitTool.judgeIsShow(headview, jsonResult);
            JSONArray dataJay = jsonResult.optJSONArray("modules");
            for (int i = 0; i < dataJay.length(); i++) {
                JSONObject tempItemJot = dataJay.optJSONObject(i);
                if ("horizontalBanner".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray bannerJay = tempItemJot.optJSONArray("templeteData");
                    if (bannerJay != null && bannerJay.length() > 0) {//如果数组不为空
                        List<CmsItem> bannerlList = new ArrayList<>();
                        for (int j = 0; j < bannerJay.length(); j++) {
                            bannerlList.add(gson.fromJson(bannerJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        ViewPager top_viewpager = (ViewPager) headview.findViewById(R.id.top_viewpager);
                        LinearLayout top_vp_dot = (LinearLayout) headview.findViewById(R.id.top_vp_dot);
                        if (mp0 == null) {
                            mp0 = new CmsBanner(mContext, top_viewpager, top_vp_dot, "top");
                            mp0.init(bannerlList);
                        }
                    }
                }
                if ("fastGoTo".equals(tempItemJot.optString("templeteCode"))) {
                    if (tempItemJot.optJSONArray("templeteData") != null && tempItemJot.optJSONArray("templeteData").length() > 0) {
                        JSONArray dateJay = tempItemJot.optJSONArray("templeteData");
                        final List<CmsItem> quickList = new ArrayList<>();
                        if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                            for (int j = 0; j < dateJay.length(); j++) {
                                quickList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), CmsItem.class));
                            }
                        }
                        MainHomeViewHelper.initFastGo(headview, mContext, quickList);
                    }
                }
                if ("horizontalAd".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray adJay = tempItemJot.optJSONArray("templeteData");
                    if (adJay != null && adJay.length() > 0) {//如果数组不为空
                        adList.clear();
                        for (int j = 0; j < adJay.length(); j++) {
                            adList.add(gson.fromJson(adJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                    }
                    ((TextView) headview.findViewById(R.id.tv_star_baby)).setText(tempItemJot.optString("moduleName"));
                    horilistview = (HorizontalListViewV2) headview.findViewById(R.id.horilistview);
                    StarBabyListAdapter mAdapter = new StarBabyListAdapter(mContext, adList);
                    horilistview.setAdapter(mAdapter);
                    horilistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            logout("adfadfa=====================================adList");
                            ViewInitTool.judgeGoWhere(adList.get(position), mContext);
                        }
                    });
                }
                if ("childrenTrainer".equals(tempItemJot.optString("templeteCode"))) {
                    initLinearGoldTeacher(tempItemJot);
                }
                if ("subjectAd".equals(tempItemJot.optString("templeteCode"))) {
                    JSONArray cmsMiddleBannerTwoJay = tempItemJot.optJSONArray("templeteData");
                    if (cmsMiddleBannerTwoJay != null && cmsMiddleBannerTwoJay.length() > 0) {
                        final List<CmsItem> midBanner2List = new ArrayList<>();
                        for (int j = 0; j < cmsMiddleBannerTwoJay.length(); j++) {
                            midBanner2List.add(gson.fromJson(cmsMiddleBannerTwoJay.optJSONObject(j).toString(), CmsItem.class));
                        }
                        MainHomeViewHelper.initMiddleBanner2(mContext, headview, midBanner2List);
                    }
                }
                if ("cmsContentRecommend".equals(tempItemJot.optString("templeteCode"))) {
                    initHScrollview(tempItemJot);
                }
            }

            if (mLRecyclerViewAdapter.getHeaderViewsCount() < 2) {
                mLRecyclerViewAdapter.addHeaderView(headview);
                mLRecyclerViewAdapter.addHeaderView(stickyview);
            }

            if (jsonResult.toString().contains("cmsContentRecommend")) {//判断内容标签导航栏是否隐藏
                stickyview.setVisibility(VISIBLE);
                isStickyShow = true;
            } else {
                stickyview.setVisibility(View.GONE);
                isStickyShow = false;
            }

        }
    };

    /**
     * 标签位初始化
     */
    public void initHScrollview(JSONObject tempItemJot) {
        JSONArray tabJay = tempItemJot.optJSONArray("templeteData");
        if (tabJay != null && tabJay.length() > 0) {//如果数组不为空
            tbList.clear();
            for (int j = 0; j < tabJay.length(); j++) {
                tbList.add(gson.fromJson(tabJay.optJSONObject(j).toString(), TabBean.class));
            }
        } else {
            rv_firsthead.refreshComplete(PAGE_SIZE);
            dataList.clear();
            mLRecyclerViewAdapter.notifyDataSetChanged();
            return;
        }
        hscrollview_linear.removeAllViews();
        for (int i = 0; i < tbList.size(); i++) {
            View view_tab_item = LayoutInflater.from(mContext).inflate(R.layout.view_tab_item, null);
            final TextView tv_tab_name = (TextView) view_tab_item.findViewById(R.id.tv_tab_name);
            tv_tab_name.setText(tbList.get(i).getName());
            final View bottom_line = view_tab_item.findViewById(R.id.bottom_line);
            final int position = i;
            if (position == 0) {
                tv_tab_name.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
                bottom_line.setVisibility(VISIBLE);
            } else {
                tv_tab_name.setTextColor(mContext.getResources().getColor(R.color.g333333));
                bottom_line.setVisibility(View.INVISIBLE);
            }
            view_tab_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainHomeViewHelper.hScrollviewChangeOthers(hscrollview_linear, mContext, position);
                    chosenTab(position);
                    curTabPos = position;
                    curModuleId = tbList.get(position).getModuleId();
                    refreshList();
                }
            });
            hscrollview_linear.addView(view_tab_item);
        }

        //默认选择第一个
        tbList.get(0).setChosen(true);
        curTabPos = 0;
        curModuleId = tbList.get(0).getModuleId();
        refreshList();
    }

    /**
     * 点击grid标签，点亮当前标签，更换其他标签视图
     */
    public void chosenTab(int pos) {
        tbList.get(pos).setChosen(true);
        for (int i = 0; i < tbList.size(); i++) {
            if (i != pos) {
                tbList.get(i).setChosen(false);
            }
        }
        mFlagGvAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化专家栏
     */
    private void initLinearGoldTeacher(JSONObject tempItemJot) {
        linear_gold_teacher = (LinearLayout) headview.findViewById(R.id.linear_gold_teacher);
        tv_gold_teacher = (TextView) headview.findViewById(R.id.tv_gold_teacher);
        JSONObject dataJot = tempItemJot.optJSONObject("templeteData");
        if (dataJot != null) {
            JSONArray dateJay = dataJot.optJSONArray("rows");
            if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                gList.clear();
                for (int j = 0; j < dateJay.length(); j++) {
                    gList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), GoldTeacher.class));
                }
                MainHomeViewHelper.buildLinearGoldTeacher(linear_gold_teacher, mContext, gList);
            } else {
                headview.findViewById(R.id.area_gold_teacher).setVisibility(View.GONE);
            }
        } else {
            headview.findViewById(R.id.area_gold_teacher).setVisibility(View.GONE);
        }
        tv_gold_teacher.setText(tempItemJot.optString("moduleName"));
        area_change_teacher = headview.findViewById(R.id.area_change_teacher);
        area_change_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncGetTrainerList();
            }
        });
    }

    /**
     * 刷新列表数据
     */
    public void refreshList() {
        if (linearLayoutManager.findFirstVisibleItemPosition() > 2) {
            rv_firsthead.scrollToPosition(3);
        }

        PAGE_NUM = 0;
        //重置可以加载更多
        rv_firsthead.setNoMore(false);
        asyncGetFirstHeadData();
    }

    /**
     * 获得列表下一页数据
     */
    public void getNextPage() {
        PAGE_NUM++;
        asyncGetFirstHeadData();
    }

    /**
     * 获得对应标签栏列表数据
     */
    private void asyncGetFirstHeadData() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.loadModuleDataPageById;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", curModuleId);
        map.put("pageIndex", PAGE_NUM);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getLsner);
    }

    BaseRequestListener getLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            //showShortToast(errorMsg);
            rv_firsthead.refreshComplete(PAGE_SIZE);
            if (PAGE_NUM > 0) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            } else {//第一页报错时，清空数据
                dataList.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            rv_firsthead.refreshComplete(PAGE_SIZE);
            parseJosnToShow(jsonResult);
        }
    };

    /**
     * 解析列表数据
     */
    private void parseJosnToShow(JSONObject jsonResult) {
        JSONObject pageJob = jsonResult.optJSONObject("templeteData");
        if (pageJob == null)
            return;
        JSONArray jayList = pageJob.optJSONArray("rows");
        // 第一次查询的时候没有数据，则提示没有数据，页面置空
        if (PAGE_NUM == 0 && (jayList == null || jayList.length() <= 0)) {
            dataList.clear();
            mLRecyclerViewAdapter.notifyDataSetChanged();
            //ViewInitTool.setListEmptyTipByDefaultPic(mContext, rv_firsthead, "暂无数据");
            return;
        }
        // 首次加载清空所有项列表,并重置控件为可下滑
        if (PAGE_NUM == 0) {
            dataList.clear();
        }
        ArrayList<FirstHeadBean> currentPageList = new ArrayList<>();
        totalPage = pageJob.optInt("pageCount");
        for (int i = 0; i < jayList.length(); i++) {
            JSONObject jot = jayList.optJSONObject(i);
            FirstHeadChildBean p = gson.fromJson(jot.toString(), FirstHeadChildBean.class);
            currentPageList.add(MainHomeViewHelper.transNewBeanToOld(p));
        }
        dataList.addAll(currentPageList);
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * 专家换一换
     */
    private void asyncGetTrainerList() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getTrainerList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", goldTeacherPageIndex + 1);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, gtlistener);
    }

    BaseRequestListener gtlistener = new JsonRequestListener() {

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
            goldTeacherPageIndex = jsonResult.optInt("pageIndex");
            JSONArray dateJay = jsonResult.optJSONArray("rows");
            if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                gList.clear();
                for (int j = 0; j < dateJay.length(); j++) {
                    gList.add(gson.fromJson(dateJay.optJSONObject(j).toString(), GoldTeacher.class));
                }
                MainHomeViewHelper.buildLinearGoldTeacher(linear_gold_teacher, mContext, gList);
            }
        }
    };

    /**
     * 发送有盟deviceToken给后台
     */
    private void asyncSaveAppDeviceToken() {
        String wholeUrl = AppUrl.host + AppUrl.SAVE_APP_DEVICE_TOKEN;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("deviceToken", GlobalParam.YOUMENG_DEVICE_TOKEN);
        map.put("bundleId", GlobalParam.BUNDLE_ID);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }

    /**
     * 登录更新失效时间
     */
    private void asyncUpdateTkoenExpireTime() {
        String wholeUrl = AppUrl.host + AppUrl.updateTkoenExpireTime;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, new SimpleRequestListener());
    }

}
