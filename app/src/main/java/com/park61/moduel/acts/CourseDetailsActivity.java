package com.park61.moduel.acts;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alivc.player.VcPlayerLog;
import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.aliyun.vodplayerview.utils.ScreenUtils;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.github.mikephil.charting.charts.RadarChart;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.FU;
import com.park61.common.tool.HtmlParserTool;
import com.park61.common.tool.ScreenStatusController;
import com.park61.common.tool.ShareTool;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.adapter.ActCapabilityChatAdapter;
import com.park61.moduel.acts.adapter.CourseComtListAdapter;
import com.park61.moduel.acts.adapter.CourseSessionListAdapter;
import com.park61.moduel.acts.adapter.CourseTeacherListAdapter;
import com.park61.moduel.acts.bean.CourseComtItem;
import com.park61.moduel.acts.bean.CourseSessionBean;
import com.park61.moduel.shophome.bean.MyCourseDetailsBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.list.ListViewForScrollView;
import com.park61.widget.pw.CourseChoosePlanPopWin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/7/21.
 */

public class CourseDetailsActivity extends BaseActivity {

    private int courseId, courseNum;
    private String priceSale, priceOriginal, className;
    private MyCourseDetailsBean curMyCourseDetailsBean;
    private List<CourseSessionBean> showTeacherInfoList = new ArrayList<>();
    private WindowManager.LayoutParams params;

    private TextView tv_course_title, tv_price, tv_price_old, tv_read_num, tv_address;
    private AliyunVodPlayerView mAliyunVodPlayerView;
    private ListViewForScrollView lv_session, lv_teacher, lv_comt;
    private CourseSessionListAdapter mCourseSessionListAdapter;
    private CourseTeacherListAdapter mCourseTeacherListAdapter;
    private RadarChart mChart;// 游戏能力维度图表
    private WebView webview;// 游戏详情加载浏览器
    private View area_expand, area_small, area_more_comt, area_addr, area_comt;
    private TextView tv_course_comt_title, tv_age_range, tv_act_type;
    private ImageView img_back;
    private Button btn_call, btn_share, btn_apply;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_course_details);
    }

    @Override
    public void initView() {
        tv_course_title = (TextView) findViewById(R.id.tv_course_title);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_price_old = (TextView) findViewById(R.id.tv_price_old);
        tv_read_num = (TextView) findViewById(R.id.tv_read_num);
        tv_address = (TextView) findViewById(R.id.tv_address);
        mAliyunVodPlayerView = (AliyunVodPlayerView) findViewById(R.id.video_view);
        mAliyunVodPlayerView.setTitleBarCanShow(false);
        lv_session = (ListViewForScrollView) findViewById(R.id.lv_session);
        lv_session.setFocusable(false);
        lv_teacher = (ListViewForScrollView) findViewById(R.id.lv_teacher);
        lv_teacher.setFocusable(false);
        lv_comt = (ListViewForScrollView) findViewById(R.id.lv_comt);
        lv_comt.setFocusable(false);
        mChart = (RadarChart) findViewById(R.id.radar_chart);
        webview = (WebView) findViewById(R.id.webview);
        area_expand = findViewById(R.id.area_expand);
        area_small = findViewById(R.id.area_small);
        area_more_comt = findViewById(R.id.area_more_comt);
        area_addr = findViewById(R.id.area_addr);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_call = (Button) findViewById(R.id.btn_call);
        btn_share = (Button) findViewById(R.id.btn_share);
        btn_apply = (Button) findViewById(R.id.btn_apply);
        tv_course_comt_title = (TextView) findViewById(R.id.tv_course_comt_title);
        ViewInitTool.setListEmptyView(mContext, lv_comt);
        tv_age_range = (TextView) findViewById(R.id.tv_age_range);
        tv_act_type = (TextView) findViewById(R.id.tv_act_type);
        area_comt = findViewById(R.id.area_comt);

        mScreenStatusController = new ScreenStatusController(this);
        mScreenStatusController.setScreenStatusListener(new ScreenStatusController.ScreenStatusListener() {
            @Override
            public void onScreenOn() {
            }

            @Override
            public void onScreenOff() {

            }
        });
        mScreenStatusController.startListen();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updatePlayerViewMode();
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onStop();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("lfj1019", " orientation = " + getResources().getConfiguration().orientation);
        updatePlayerViewMode();
    }

    private ScreenStatusController mScreenStatusController = null;

    private boolean isStrangePhone() {
        boolean strangePhone = Build.DEVICE.equalsIgnoreCase("mx5")
                || Build.DEVICE.equalsIgnoreCase("Redmi Note2")
                || Build.DEVICE.equalsIgnoreCase("Z00A_1")
                || Build.DEVICE.equalsIgnoreCase("hwH60-L02")
                || Build.DEVICE.equalsIgnoreCase("hermes")
                || (Build.DEVICE.equalsIgnoreCase("V4") && Build.MANUFACTURER.equalsIgnoreCase("Meitu"));

        VcPlayerLog.e("lfj1115 ", " Build.Device = " + Build.DEVICE + " , isStrange = " + strangePhone);
        return strangePhone;

    }

    private void updatePlayerViewMode() {
        if (mAliyunVodPlayerView != null) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {                //转为竖屏了。
                //显示状态栏
//                if (!isStrangePhone()) {
//                    getSupportActionBar().show();
//                }

                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                //设置view的布局，宽高之类
                ViewGroup.LayoutParams aliVcVideoViewLayoutParams = (ViewGroup.LayoutParams) mAliyunVodPlayerView.getLayoutParams();
                aliVcVideoViewLayoutParams.height = (int) (ScreenUtils.getWidth(this) * 9.0f / 16);
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                if (!isStrangePhone()) {
//                    aliVcVideoViewLayoutParams.topMargin = getSupportActionBar().getHeight();
//                }

            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {                //转到横屏了。
                //隐藏状态栏
                if (!isStrangePhone()) {
                    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }

                //设置view的布局，宽高
                ViewGroup.LayoutParams aliVcVideoViewLayoutParams = (ViewGroup.LayoutParams) mAliyunVodPlayerView.getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                if (!isStrangePhone()) {
//                    aliVcVideoViewLayoutParams.topMargin = 0;
//                }

            }

        }
    }

    @Override
    protected void onDestroy() {
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onDestroy();
            mAliyunVodPlayerView = null;
        }
        mScreenStatusController.stopListen();

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAliyunVodPlayerView != null) {
            boolean handler = mAliyunVodPlayerView.onKeyDown(keyCode, event);
            if (!handler) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //解决某些手机上锁屏之后会出现标题栏的问题。
        VcPlayerLog.d("lfj1030", "onWindowFocusChanged = " + hasFocus);
        updatePlayerViewMode();
    }

    @Override
    public void initData() {
        courseId = getIntent().getIntExtra("courseId", -1);
        courseNum = getIntent().getIntExtra("courseNum", -1);
        priceSale = getIntent().getStringExtra("priceSale");
        priceOriginal = getIntent().getStringExtra("priceOriginal");
        className = getIntent().getStringExtra("className");
        asyncGetCourseDetail();
        asyncGetEvaluateList();
    }

    @Override
    public void initListener() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                } else {
                    finish();
                }*/
                finish();
            }
        });
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = GlobalParam.CUR_SHOP_PHONE;
                if (TextUtils.isEmpty(phone)) {
                    showShortToast("无效的电话号码！");
                    return;
                }
                dDialog.showDialog("提示", "确认拨打" + phone + "?", "取消", "确认", null,
                        new View.OnClickListener() {

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
        btn_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showShareDialog(shareUrl, shareUrl, shareTitle, shareDescription);
            }
        });
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (!CommonMethod.checkUserLogin(mContext)) {
                    return;
                }
                showPopFormBottom();
            }
        });
        area_more_comt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, CourseEvaListActivity.class);
                it.putExtra("courseId", courseId);
                startActivity(it);
            }
        });
        area_addr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String act_latitude = curMyCourseDetailsBean.getLatitude();
                String act_longitude = curMyCourseDetailsBean.getLongitude();
                Intent it = new Intent(mContext, ActMapActivity.class);
                it.putExtra("act_latitude", act_latitude);
                it.putExtra("act_longitude", act_longitude);
                startActivity(it);
            }
        });
        lv_session.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mContext, CourseOrderInputActivity.class);
                it.putExtra("coursePlanId", curMyCourseDetailsBean.getTeacherList().get(position).getPlanId() + "");
                startActivity(it);
            }
        });
    }

    private void asyncGetCourseDetail() {
        String wholeUrl = AppUrl.host + AppUrl.courseDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseId);
        map.put("courseNum", courseNum);
        map.put("priceSale", priceSale);
        map.put("priceOriginal", priceOriginal);
        map.put("className", className);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, cLsner);
    }

    BaseRequestListener cLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            //showShortToast(errorMsg);
            dDialog.showDialog("提示", errorMsg, "取消", "重试",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            asyncGetCourseDetail();
                        }
                    });
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            curMyCourseDetailsBean = gson.fromJson(jsonResult.toString(), MyCourseDetailsBean.class);
            setDataToView();
        }
    };

    public void setDataToView() {
        shareUrl = String.format(AppUrl.COURSE_SHARE_URL, curMyCourseDetailsBean.getId() + "");
        shareUrl += "&priceSale=" + priceSale + "&priceOriginal=" + priceOriginal + "&className=" + CommonMethod.escape(className) + "&courseNum=" + courseNum;
        sharePic = curMyCourseDetailsBean.getVideoImg();
        shareTitle = getString(R.string.app_name) + "课程" + curMyCourseDetailsBean.getName();
        shareDescription = "地址：" + curMyCourseDetailsBean.getAddress();
        //将分享数据记录到静态变量中，支付完成页面可以调用
        ShareTool.init(shareUrl, shareUrl, shareTitle, shareDescription);

        tv_course_title.setText(curMyCourseDetailsBean.getTotalName());
        tv_price.setText(FU.formatBigPrice(curMyCourseDetailsBean.getPriceSale()));
        tv_price_old.setText("￥" + curMyCourseDetailsBean.getPriceOriginal());
        ViewInitTool.lineText(tv_price_old);
        tv_age_range.setText(curMyCourseDetailsBean.getAgeRange());
        tv_act_type.setText(curMyCourseDetailsBean.getSeriesName());
        tv_read_num.setText(curMyCourseDetailsBean.getUserViewNum() + "");
        tv_address.setText("上课地点：" + curMyCourseDetailsBean.getAddress());
        mAliyunVodPlayerView.setCoverUri(curMyCourseDetailsBean.getVideoImg());
        mCourseSessionListAdapter = new CourseSessionListAdapter(mContext, curMyCourseDetailsBean.getTeacherList(), true);
        lv_session.setAdapter(mCourseSessionListAdapter);

        String htmlDetailsStr = "";
        try {
            htmlDetailsStr = HtmlParserTool.replaceImgStr(curMyCourseDetailsBean.getDetail());
        } catch (Exception e) {
            e.printStackTrace();
        }
        webview.loadDataWithBaseURL(null, htmlDetailsStr, "text/html", "utf-8", null);
        if (!CommonMethod.isListEmpty(curMyCourseDetailsBean.getCapacityList())) {
            ActCapabilityChatAdapter.initChart(mChart, mContext, curMyCourseDetailsBean);
        }

        mCourseTeacherListAdapter = new CourseTeacherListAdapter(mContext, showTeacherInfoList);
        lv_teacher.setAdapter(mCourseTeacherListAdapter);

        area_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTeacherInfoList.clear();
                showTeacherInfoList.addAll(curMyCourseDetailsBean.getTeachers());
                mCourseTeacherListAdapter.notifyDataSetChanged();
                area_expand.setVisibility(View.GONE);
                area_small.setVisibility(View.VISIBLE);
            }
        });

        area_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTeacherInfoList.clear();
                showTeacherInfoList.addAll(curMyCourseDetailsBean.getTeachers().subList(0, 2));
                mCourseTeacherListAdapter.notifyDataSetChanged();
                area_expand.setVisibility(View.VISIBLE);
                area_small.setVisibility(View.GONE);
            }
        });

        if (curMyCourseDetailsBean.getTeachers().size() > 2) {
            showTeacherInfoList.clear();
            showTeacherInfoList.addAll(curMyCourseDetailsBean.getTeachers().subList(0, 2));
            mCourseTeacherListAdapter.notifyDataSetChanged();
            area_expand.setVisibility(View.VISIBLE);
            area_small.setVisibility(View.GONE);
        } else {
            showTeacherInfoList.clear();
            if (!CommonMethod.isListEmpty(curMyCourseDetailsBean.getTeachers())) {
                showTeacherInfoList.addAll(curMyCourseDetailsBean.getTeachers());
            }
            mCourseTeacherListAdapter.notifyDataSetChanged();
            area_expand.setVisibility(View.GONE);
            area_small.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(curMyCourseDetailsBean.getAuthInfo()) && !TextUtils.isEmpty(curMyCourseDetailsBean.getVideoId())) {
            AliyunPlayAuth.AliyunPlayAuthBuilder aliyunAuthInfoBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
            aliyunAuthInfoBuilder.setPlayAuth(curMyCourseDetailsBean.getAuthInfo());
            aliyunAuthInfoBuilder.setVid(curMyCourseDetailsBean.getVideoId());
            aliyunAuthInfoBuilder.setQuality(IAliyunVodPlayer.QualityValue.QUALITY_ORIGINAL);
            mAliyunVodPlayerView.setAuthInfo(aliyunAuthInfoBuilder.build());
        }
    }

    private void asyncGetEvaluateList() {
        String wholeUrl = AppUrl.host + AppUrl.evaluateList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseId", courseId);
        map.put("curPage", 1);
        map.put("pageSize", 2);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, eLsner);
    }

    BaseRequestListener eLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            area_more_comt.setVisibility(View.GONE);
            area_comt.setVisibility(View.GONE);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay == null || actJay.length() == 0) {
                area_comt.setVisibility(View.GONE);
            } else {
                area_comt.setVisibility(View.VISIBLE);
                tv_course_comt_title.setText("课程评价(" + jsonResult.optString("totalSize") + ")");
                List<CourseComtItem> currentPageList = new ArrayList<CourseComtItem>();
                for (int i = 0; i < actJay.length(); i++) {
                    currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), CourseComtItem.class));
                }
                CourseComtListAdapter adapter = new CourseComtListAdapter(mContext, currentPageList);
                lv_comt.setAdapter(adapter);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showPopFormBottom() {
        final CourseChoosePlanPopWin takePhotoPopWin = new CourseChoosePlanPopWin(mContext, curMyCourseDetailsBean.getTeacherList());
        // 设置Popupwindow显示位置（从底部弹出）
        takePhotoPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        takePhotoPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        takePhotoPopWin.getLvTeachers().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(mContext, CourseOrderInputActivity.class);
                it.putExtra("coursePlanId", curMyCourseDetailsBean.getTeacherList().get(position).getPlanId() + "");
                startActivity(it);
                takePhotoPopWin.dismiss();
            }
        });
    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mAliyunVodPlayerView != null) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {                //转为竖屏了。
                //显示状态栏
                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                //设置view的布局，宽高之类
                ViewGroup.LayoutParams aliVcVideoViewLayoutParams = mAliyunVodPlayerView.getLayoutParams();
                aliVcVideoViewLayoutParams.height = (int) (ScreenUtils.getWight(this) * 9.0f / 16);
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;

                //设置为小屏状态
                mAliyunVodPlayerView.changeScreenMode(AliyunScreenMode.Small);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {                //转到横屏了。
                //隐藏状态栏
                this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
                //设置view的布局，宽高
                ViewGroup.LayoutParams aliVcVideoViewLayoutParams = mAliyunVodPlayerView.getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;

                //设置为全屏状态
                mAliyunVodPlayerView.changeScreenMode(AliyunScreenMode.Full);
            }
        }
    }*/
}
