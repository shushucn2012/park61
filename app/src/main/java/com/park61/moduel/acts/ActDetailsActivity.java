package com.park61.moduel.acts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.HtmlParserTool;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.QRCodeCreator;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.adapter.ActCapabilityChatAdapter;
import com.park61.moduel.acts.adapter.ActDetailsComtFiller;
import com.park61.moduel.acts.adapter.ActSessionListAdapter;
import com.park61.moduel.acts.adapter.ActSessionListAdapter.OnChooseListener;
import com.park61.moduel.acts.adapter.ComtListAdapter.OnReplyClickedLsner;
import com.park61.moduel.acts.adapter.RecommendGoodsListAdapter;
import com.park61.moduel.acts.bean.ActItem;
import com.park61.moduel.acts.bean.ActivitySessionVo;
import com.park61.moduel.acts.bean.ComtItem;
import com.park61.moduel.acts.bean.RecommendGoodsInfo;
import com.park61.moduel.acts.course.CApplyAllConfirmActivity;
import com.park61.moduel.acts.course.CourseChooseActivity;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.RecommendGoodsActivity;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.CanRefreshProgressDialog;
import com.park61.widget.dialog.CanRefreshProgressDialog.OnRefreshClickedLsner;
import com.park61.widget.list.HorizontalListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActDetailsActivity extends BaseActivity implements
        OnReplyClickedLsner {

    private static final int REQ_FOCUS = 0;
    private static final int REQ_CANCEL_FOCUS = 1;

    private List<ComtItem> comtList;
    private float y;
    private Long actId, actTempId;
    private ActItem curAct;
    private boolean isFocus;// 是否有关注
    private boolean isToComt;// 是否跳到评论
    private boolean isEnd;// 评论列表是否到最后一页了
    private int PAGE_NUM = 1;// 评论列表页码
    private final int PAGE_SIZE = 5;// 评论列表每页条数
    public boolean needRefreshComts = true;// 是否需要刷新评论数据，避免切换时刷新
    private int comtType = 0;// 评论类型 0 评论游戏；1评论评论；2评论回复
    private Long curRootId;// 当前评论的评论id
    private Long curParentId;// 当前评论的回复id
    private List<ActItem> actSessionList;// 场次列表
    private ActSessionListAdapter mActSessionListAdapter;
    private String act_latitude, act_longitude, phone;
    private ActivitySessionVo sessions;

    private RadioGroup mtabGroup;
    private View[] stickArray = new View[2];
    private LinearLayout actdetails_content, actcomt_content;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private ObservableScrollView scrollView;
    private View stopView, stickyView, area_change_shop, area_location,
            area_call, area_recommend, line, area_smallclass;
    private Button btn_comt, btn_share, btn_apply, btn_send, btn_shop;
    private View bottom_btn;// 底部按钮区域
    private RelativeLayout bottom_input_area;// 底部输入区域
    private EditText edit_comt;// 评论输入框
    private TextView tv_shop, tv_price, tv_leiji, tv_age_range, tv_price_old,
            tv_addr, tv_contact, tv_act_title, tv_act_type, tv_session_and_num;
    private ImageView act_img, img_focus;
    private RadarChart mChart;// 游戏能力维度图表
    private WebView webview;// 游戏详情加载浏览器
    private ObservableScrollView.Callbacks mCallbacks;
    private LinearLayout linear_comment;
    private ImageView img_qrcode;// 二维码图
    private ListView lv_session;
    private HorizontalListView horilistview;
    private RecommendGoodsListAdapter mAdapter;
    private ArrayList<RecommendGoodsInfo> list;

    private CanRefreshProgressDialog cpDialog;
    private Long reqPredId;//梦想预报名id

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_actdetails);
    }

    @Override
    public void initView() {
        line = findViewById(R.id.line);
        horilistview = (HorizontalListView) findViewById(R.id.horilistview);
        area_recommend = findViewById(R.id.area_recommend);
        tv_act_title = (TextView) findViewById(R.id.tv_act_title);
        act_img = (ImageView) findViewById(R.id.act_img);
        img_focus = (ImageView) findViewById(R.id.img_focus);
        tv_shop = (TextView) findViewById(R.id.tv_shop);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_price_old = (TextView) findViewById(R.id.tv_price_old);
        tv_leiji = (TextView) findViewById(R.id.tv_leiji);
        tv_age_range = (TextView) findViewById(R.id.tv_age_range);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        tv_contact = (TextView) findViewById(R.id.tv_contact);
        mChart = (RadarChart) findViewById(R.id.radar_chart);
        webview = (WebView) findViewById(R.id.webview);
        btn_apply = (Button) findViewById(R.id.btn_apply);
        btn_send = (Button) findViewById(R.id.btn_send);
        tv_act_type = (TextView) findViewById(R.id.tv_act_type);
        tv_session_and_num = (TextView) findViewById(R.id.tv_session_and_num);
        lv_session = (ListView) findViewById(R.id.lv_session);
        lv_session.setFocusable(false);

        btn_comt = (Button) findViewById(R.id.btn_comt);
        btn_share = (Button) findViewById(R.id.btn_share);
        btn_shop = (Button) findViewById(R.id.btn_shop);
        bottom_btn = findViewById(R.id.bottom_btn);
        bottom_input_area = (RelativeLayout) findViewById(R.id.bottom_input_area);
        edit_comt = (EditText) findViewById(R.id.edit_comt);
        stopView = findViewById(R.id.stopView);
        stickyView = findViewById(R.id.stickyView);
        area_change_shop = findViewById(R.id.area_change_shop);
        area_location = findViewById(R.id.area_location);
        area_call = findViewById(R.id.area_call);
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        actdetails_content = (LinearLayout) findViewById(R.id.actdetails_content);
        actcomt_content = (LinearLayout) findViewById(R.id.actcomt_content);
        area_smallclass = findViewById(R.id.area_smallclass);

        img_qrcode = (ImageView) findViewById(R.id.img_qrcode);
        linear_comment = (LinearLayout) findViewById(R.id.linear_comment);

        initActTabs();
        // 初始化上下拉刷新控件
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        ViewInitTool.initPullToRefresh(mPullRefreshScrollView, mContext);
        // 初始化滑动黏贴控件
        scrollView = (ObservableScrollView) mPullRefreshScrollView.getRefreshableView();
        initObservableScrollView();

        cpDialog = new CanRefreshProgressDialog(mContext,
                new OnRefreshClickedLsner() {

                    @Override
                    public void OnRefreshClicked() {
                        asyncGetActDetails(actId, actTempId);
                    }
                });
    }

    @Override
    public void initData() {
        reqPredId = getIntent().getLongExtra("reqPredId", -1l);
        actId = getIntent().getLongExtra("id", -1l);
        actTempId = getIntent().getLongExtra("actTempId", -1l);
        isFocus = getIntent().getBooleanExtra("isFocus", false);
        isToComt = getIntent().getBooleanExtra("isToComt", false);
        asyncGetActDetails(actId, actTempId);
        actSessionList = new ArrayList<ActItem>();
        mActSessionListAdapter = new ActSessionListAdapter(mContext, actSessionList);
        lv_session.setAdapter(mActSessionListAdapter);

        // 初始化评论列表
        comtList = new ArrayList<ComtItem>();

        list = new ArrayList<RecommendGoodsInfo>();
        mAdapter = new RecommendGoodsListAdapter(mContext, list);
        horilistview.setAdapter(mAdapter);
    }

    public void setDataToView() {
        tv_act_title.setText(curAct.getActTitle());
        tv_shop.setText("(" + curAct.getShopName() + ")");
        //价格
        //先显示一个价格，避免下面没有场次的时候，价格为空
        ViewInitTool.fmActPrice(curAct, tv_price, tv_price_old);
        //ViewInitTool.fmActPriceInDetails(curAct.getActivitySessionVo(), tv_price, tv_price_old);

        tv_leiji.setText("累计" + (curAct.getGrandTotal() == null ? 0 : curAct.getGrandTotal()) + "人报名");
        tv_age_range.setText(curAct.getActUppAgeLimit() + " - " + curAct.getActLowAgeLimit() + "岁");
        tv_act_type.setText(curAct.getLabelName());
        tv_addr.setText(curAct.getActivitySessionVo().getStoreVO().getAddress());

        phone = curAct.getContactTel();
        tv_contact.setText(curAct.getContactor() + "  " + curAct.getContactTel());
//        ImageLoader.getInstance().displayImage(curAct.getActCover(), act_img, options);
        ImageManager.getInstance().displayImg(act_img, curAct.getActCover());
        if (isFocus) {
            img_focus.setImageResource(R.drawable.guanzhu_focus);
        }
        sessions = curAct.getActivitySessionVo();
        actSessionList.clear();
        actSessionList.addAll(curAct.getActivitySessionVo().getActSessionList());
        mActSessionListAdapter.notifyDataSetChanged();
        tv_session_and_num.setText("游戏场次" + "(" + curAct.getActivitySessionVo().getActSessionList().size() + ")");
        if (GlobalParam.SMALL_CLASS_CODE.equals(curAct.getActBussinessType())) {
            lv_session.setVisibility(View.GONE);
            area_smallclass.setVisibility(View.VISIBLE);
            ViewInitTool.initSamllClassFirstData(ActDetailsActivity.this, curAct.getActivitySessionVo(), area_smallclass);
        }

        String htmlDetailsStr = "";
        try {
            htmlDetailsStr = HtmlParserTool.replaceImgStr(curAct.getActDetail());
        } catch (Exception e) {
            e.printStackTrace();
        }
        webview.loadDataWithBaseURL(null, htmlDetailsStr, "text/html", "utf-8", null);
        ActCapabilityChatAdapter.initChart(mChart, mContext, curAct);

        // 初始化二维码
        String qrUrl = String.format(AppUrl.ACT_SHARE_URL, curAct.getId() + "");
        QRCodeCreator.createQRImage(qrUrl, DevAttr.dip2px(mContext, 200),
                DevAttr.dip2px(mContext, 200), img_qrcode);
    }

    @Override
    public void initListener() {
        horilistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent it = new Intent(ActDetailsActivity.this, GoodsDetailsActivity.class);
                it.putExtra("goodsId", list.get(position).getPmInfoid());
                it.putExtra("promotionId", list.get(position).getPromotionId());
                it.putExtra("goodsName", list.get(position).getName());
                it.putExtra("goodsOldPrice", list.get(position).getOldPrice());
                it.putExtra("goodsPrice", list.get(position).getSalesPrice());
                startActivity(it);
            }
        });
        area_recommend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ActDetailsActivity.this, RecommendGoodsActivity.class);
                it.putExtra("refTemplateId", actTempId.toString());
                startActivity(it);
            }
        });
        area_location.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                act_latitude = curAct.getActLatitude();
                act_longitude = curAct.getActLongitude();
                Intent it = new Intent(mContext, ActMapActivity.class);
                it.putExtra("act_latitude", act_latitude);
                it.putExtra("act_longitude", act_longitude);
                startActivity(it);
            }
        });
        area_call.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phone)) {
                    showShortToast("无效的电话号码！");
                    return;
                }
                dDialog.showDialog("提醒", "确定需要拨打此号码吗？", "取消", "确定", null,
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                dDialog.dismissDialog();
                            }
                        });
            }
        });
        area_change_shop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ActDetailsChangeShopActivity.class);
                it.putExtra("curShop", curAct.getShopName());
                it.putExtra("refTemplateId", curAct.getRefTemplateId());
                startActivityForResult(it, 0);
            }
        });
        btn_comt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showComtArea(0);
            }
        });
        btn_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String shareUrl = String.format(AppUrl.ACT_SHARE_URL, curAct.getId() + "");
                String picUrl = curAct.getActCover();
                String title = getString(R.string.app_name) + "游戏" + curAct.getActTitle();
                String addr = "地址：" + curAct.getActAddress();
                String dateStr = "日期：" + DateTool.L2SByDay2(curAct.getActStartTime()) + " - " + DateTool.L2SByDay2(curAct.getActEndTime());
                String description = addr + "\n" + dateStr;
                showShareDialog(shareUrl, picUrl, title, description);
            }
        });
        btn_apply.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (GlobalParam.userToken == null) {// 没有登录则跳去登录
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                Intent it = null;
                if (GlobalParam.SMALL_CLASS_CODE.equals(curAct.getActBussinessType())) {
                    it = new Intent(mContext, CApplyAllConfirmActivity.class);
                } else {
                    it = new Intent(mContext, ApplyConfirmActivity.class);
                }
                it.putExtra("actdetails", curAct);
                if (sessions == null || CommonMethod.isListEmpty(sessions.getActSessionList()) || !hasSessonCanApply()) {
                    showShortToast("暂无可报名的场次！");
                    return;
                }
                it.putExtra("activitySessionVo", sessions);
                it.putExtra("reqPredId", reqPredId);
                logout("========游戏详情====reqPredId=======" + reqPredId);
                startActivity(it);
            }
        });
        img_focus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isFocus) {// 关注则取消
                    asyncCancelFocusAct();
                } else {
                    asyncFocusAct();
                }
            }
        });
        btn_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_comt.getText().toString().trim())) {
                    showShortToast("您未填写评论！");
                    return;
                }
                switch (comtType) {
                    case 0:
                        asyncCommitActComt(null, null);
                        break;
                    case 1:
                        asyncCommitActComt(curParentId, curRootId);
                        break;
                    case 2:
                        asyncCommitActComt(curParentId, curRootId);
                        break;
                }
            }
        });
        mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (isEnd) {
                    showShortToast("已经是最后一页了");
                    mPullRefreshScrollView.onRefreshComplete();
                } else {
                    PAGE_NUM++;
                    asyncGetActComts();
                }
            }
        });
        mActSessionListAdapter.setOnChooseListener(new OnChooseListener() {

            @Override
            public void onChoose(int pos) {
                if (GlobalParam.userToken == null) {// 没有登录则跳去登录
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                Intent it = new Intent(mContext, ApplyConfirmActivity.class);
                it.putExtra("actdetails", curAct);
                it.putExtra("activitySessionVo", sessions);
                it.putExtra("sessionPos", pos);
                it.putExtra("reqPredId", reqPredId);
                startActivity(it);
            }
        });
        area_smallclass.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (GlobalParam.userToken == null) {// 没有登录则跳去登录
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                Intent it = new Intent(mContext, CourseChooseActivity.class);
                it.putExtra("activitySessionVo", sessions);
                it.putExtra("reqPredId", reqPredId);
                startActivity(it);
            }
        });
    }

    /**
     * 初始化标签
     */
    private void initActTabs() {
        mtabGroup = (RadioGroup) findViewById(R.id.mtab_group);
        mtabGroup.check(R.id.rb_actdetails);
        mtabGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                y = scrollView.getScrollY();
                int checkedIndex = 0;
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (checkedId == group.getChildAt(i).getId()) {
                        checkedIndex = i;
                    }
                }
                showStick(checkedIndex);
                swithPage(checkedIndex);
            }
        });
        stickArray[0] = findViewById(R.id.stick0);
        stickArray[1] = findViewById(R.id.stick1);
        stickArray[0].setVisibility(View.VISIBLE);
    }

    /**
     * 切换详情和游戏评价
     */
    public void swithPage(int pageIndex) {
        switch (pageIndex) {
            case 0:
                actdetails_content.setVisibility(View.VISIBLE);
                actcomt_content.setVisibility(View.GONE);
                scrollView.scrollTo(0, (int) y);
                mPullRefreshScrollView.setMode(Mode.DISABLED);
                break;
            case 1:
                actdetails_content.setVisibility(View.GONE);
                actcomt_content.setVisibility(View.VISIBLE);
                mPullRefreshScrollView.setMode(Mode.PULL_FROM_END);
                ViewInitTool.initPullToRefresh(mPullRefreshScrollView, mContext);
                initComtList();
                break;
        }
    }

    private void initComtList() {
        // 获取评论首页数据
        if (needRefreshComts) {
            PAGE_NUM = 1;
            isEnd = false;
            asyncGetActComts();
            // 刷新后置否
            needRefreshComts = false;
        } else {
            scrollView.scrollTo(0, (int) y);
        }
    }

    /**
     * 变化标签组下方红杠
     */
    private void showStick(int which) {
        stickArray[which].setVisibility(View.VISIBLE);
        for (int i = 0; i < stickArray.length; i++) {
            if (i != which) {
                stickArray[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 监控点击按钮如果点击在评论输入框之外就关闭输入框，变回报名栏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = bottom_input_area;// getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                bottom_btn.setVisibility(View.VISIBLE);
                bottom_input_area.setVisibility(View.GONE);
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 初始化黏贴滑动控件
     */
    public void initObservableScrollView() {
        mCallbacks = new ObservableScrollView.Callbacks() {

            @Override
            public void onUpOrCancelMotionEvent() {
            }

            @Override
            public void onScrollChanged(int scrollY) {
                stickyView.setTranslationY(Math.max(stopView.getTop(), scrollY));
            }

            @Override
            public void onDownMotionEvent() {
            }
        };
        ViewInitTool.initObservableScrollView(scrollView, mCallbacks);
    }

    /**
     * 获取游戏详情
     */
    protected void asyncGetActDetails(Long actId, Long refTemplateId) {
        String wholeUrl = AppUrl.host + AppUrl.GET_ACT_DETAILS_END;
        String requestBodyData = ParamBuild.getActDetails(actId, refTemplateId);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            cpDialog.showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            cpDialog.showRefreshBtn();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            cpDialog.dialogDismiss();
           /* logout(jsonResult.toString().substring(0, jsonResult.toString().length() / 2));
            logout(jsonResult.toString().substring(jsonResult.toString().length() / 2, jsonResult.toString().length()));

            String resultJson = CommonMethod.readAssetsFile(mContext, "act_details_json_file");
            logout("resultJson======"+resultJson);
            JSONObject fResult = null;
            try {
                JSONObject mJSONObject = new JSONObject(resultJson);
                fResult = mJSONObject.optJSONObject("data");
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            curAct = gson.fromJson(jsonResult.toString(), ActItem.class);
            setDataToView();
            if (isToComt) {// 如果需要跳到评论，则切换
                mtabGroup.check(R.id.rb_actcomt);
            }
            if (curAct != null && GlobalParam.userToken != null) {
                asyncGetFocusActs();
            }
            asyncGetRecommendGoods();
        }
    };

    /**
     * 关注游戏
     */
    protected void asyncFocusAct() {
        String wholeUrl = AppUrl.host + AppUrl.FOCUS_ACT_END;
        String requestBodyData = ParamBuild.focusAct(curAct.getId());
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, REQ_FOCUS, focuslistener);
    }

    /**
     * 取消关注游戏
     */
    protected void asyncCancelFocusAct() {
        String wholeUrl = AppUrl.host + AppUrl.CANCEL_FOCUS_ACT_END;
        String requestBodyData = ParamBuild.focusAct(curAct.getId());
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, REQ_CANCEL_FOCUS, focuslistener);
    }

    BaseRequestListener focuslistener = new JsonRequestListener() {

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
            switch (requestId) {
                case REQ_FOCUS:
                    showShortToast("关注成功!");
                    img_focus.setImageResource(R.drawable.guanzhu_focus);
                    isFocus = true;
                    break;
                case REQ_CANCEL_FOCUS:
                    showShortToast("取消关注成功!");
                    img_focus.setImageResource(R.drawable.guanzhu_default);
                    isFocus = false;
                    break;
            }
        }
    };

    /**
     * 获取游戏评论列表
     */
    protected void asyncGetActComts() {
//        String wholeUrl = AppUrl.host + AppUrl.GET_ACT_COMMENTS_END;
        String wholeUrl = AppUrl.host + AppUrl.GAME_EVALUATE_LIST;
        String requestBodyData = ParamBuild.getActComts(curAct.getId(),
                curAct.getRefTemplateId(), PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, comtslistener);
    }

    BaseRequestListener comtslistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            listStopLoadView();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            listStopLoadView();
            ArrayList<ComtItem> currentPageList = new ArrayList<ComtItem>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                showShortToast("没有评论数据！");
                comtList.clear();
                isEnd = true;
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                comtList.clear();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM == jsonResult.optInt("totalPage")) {
                isEnd = true;
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                ComtItem c = gson.fromJson(actJot.toString(), ComtItem.class);
                currentPageList.add(c);
            }
            comtList.addAll(currentPageList);
            ActDetailsComtFiller.buildListInLinear(linear_comment, comtList,
                    mContext, ActDetailsActivity.this);
        }
    };

    protected void listStopLoadView() {
        mPullRefreshScrollView.onRefreshComplete();
    }

    /**
     * 请求提交评论
     */
    private void asyncCommitActComt(Long parentId, Long rootId) {
        String wholeUrl = AppUrl.host + AppUrl.COMMIT_ACT_COMMENTS_END;
        String requestBodyData = ParamBuild.comtAct(curAct.getId(), parentId,
                rootId, edit_comt.getText().toString());
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, comtActListener);
    }

    BaseRequestListener comtActListener = new JsonRequestListener() {

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
            showShortToast("评论成功！");
            // 清空评论并隐藏评论框
            edit_comt.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(edit_comt.getWindowToken(), 0);
            }
            bottom_btn.setVisibility(View.VISIBLE);
            bottom_input_area.setVisibility(View.GONE);

            PAGE_NUM = 1;
            isEnd = false;
            asyncGetActComts();
        }
    };

    @Override
    public void onComtClicked(Long rootId) {
        curRootId = rootId;
        curParentId = rootId;
        showComtArea(1);
    }

    @Override
    public void onReplyClicked(Long rootId, Long parentId) {
        curRootId = rootId;
        curParentId = parentId;
        showComtArea(2);
    }

    /**
     * 点击评论按钮，显示评论输入框
     *
     * @param curComtType 当前评论类型
     */
    public void showComtArea(int curComtType) {
        comtType = curComtType;
        // 点击评论按钮，显示评论输入框
        bottom_input_area.setVisibility(View.VISIBLE);
        bottom_btn.setVisibility(View.GONE);
        edit_comt.requestFocus();
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(edit_comt, 0);
    }

    /**
     * 请求关注的游戏数据
     */
    private void asyncGetFocusActs() {
        String wholeUrl = AppUrl.host + AppUrl.GET_FOCUS_ACTS_END;
        String requestBodyData = ParamBuild.getActList(1, 100);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getFocuslistener);
    }

    BaseRequestListener getFocuslistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray actJay = jsonResult.optJSONArray("list");
            for (int i = 0; i < actJay.length(); i++) {
                ActItem focusActItem = gson.fromJson(actJay.optJSONObject(i).toString(), ActItem.class);
                if (curAct.getId().equals(focusActItem.getId())) {
                    img_focus.setImageResource(R.drawable.guanzhu_focus);
                    isFocus = true;
                    break;
                }
            }
        }
    };

    /**
     * 游戏详情推荐商品列表
     */
    private void asyncGetRecommendGoods() {
        String wholeUrl = AppUrl.host + AppUrl.RECOMMEND_GOODS;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("refTemplateId", actTempId.toString());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, recommendlistener);
    }

    BaseRequestListener recommendlistener = new JsonRequestListener() {

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
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                RecommendGoodsInfo r = gson.fromJson(jot.toString(), RecommendGoodsInfo.class);
                list.add(r);
            }
            if (list.size() < 1) {
                area_recommend.setVisibility(View.GONE);
                horilistview.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
            }
            mAdapter.notifyDataSetChanged();
        }
    };


    /**
     * 请求游戏场次数据
     */
    private void asyncGetActSessionByShop(Long shopId) {
        String wholeUrl = AppUrl.host + AppUrl.GET_ACT_SESSION_BY_SHOP;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("merchantId", shopId);
        map.put("refTemplateId", curAct.getRefTemplateId());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getSessionslistener);
    }

    BaseRequestListener getSessionslistener = new JsonRequestListener() {

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
            logout(jsonResult.toString());
            JSONObject jot = jsonResult.optJSONObject("activitySessionVo");
            sessions = gson.fromJson(jot.toString(), ActivitySessionVo.class);
            if (sessions == null || sessions.getActSessionList() == null) {
                return;
            }
            //重设场次信息
            actSessionList.clear();
            actSessionList.addAll(sessions.getActSessionList());
            mActSessionListAdapter.notifyDataSetChanged();
            tv_session_and_num.setText("游戏场次" + "(" + sessions.getActSessionList().size() + ")");
            if (GlobalParam.SMALL_CLASS_CODE.equals(curAct.getActBussinessType())) {
                lv_session.setVisibility(View.GONE);
                area_smallclass.setVisibility(View.VISIBLE);
                ViewInitTool.initSamllClassFirstData(ActDetailsActivity.this, sessions, area_smallclass);
            }
            //重设活动基本信息
            curAct.setId(sessions.getActSessionList().get(0).getId());
            curAct.setBackendShopId(sessions.getStoreVO().getId());
            curAct.setActAddress(sessions.getStoreVO().getAddress());
            tv_shop.setText("(" + sessions.getStoreVO().getStoreName() + ")");
            tv_addr.setText(sessions.getStoreVO().getAddress());
            tv_contact.setText(sessions.getStoreVO().getMaster() + "  " + sessions.getStoreVO().getPhone());
            act_latitude = sessions.getStoreVO().getLatitude();
            act_longitude = sessions.getStoreVO().getLongitude();
            phone = sessions.getStoreVO().getPhone();
            //价格
            //ViewInitTool.fmActPriceInDetails(sessions, tv_price, tv_price_old);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Long shopId = data.getLongExtra("shopId", 0l);
            asyncGetActSessionByShop(shopId);
        }
    }

    /**
     * 判断是否有场次可以报名
     *
     * @return
     */
    private boolean hasSessonCanApply() {
        for (ActItem s : sessions.getActSessionList()) {
            if (s.isApply()) {
                return true;
            }
        }
        return false;
    }


}
