package com.park61.moduel.firsthead;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.BaseActivity;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.PermissionHelper;
import com.park61.common.tool.ShareTool;
import com.park61.moduel.acts.MipcaActivityCapture;
import com.park61.moduel.firsthead.adapter.FirstHeadAdapter;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.moduel.firsthead.fragment.FragmentClassShow;
import com.park61.moduel.firsthead.fragment.FragmentMainFirst;
import com.park61.moduel.firsthead.fragment.FragmentMainFirstTop;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.moduel.me.bean.AuthInfoBean;
import com.park61.moduel.msg.MsgActivity;
import com.park61.net.base.Request;
import com.park61.net.request.SimpleRequestListener;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.AuthFailedDialog;
import com.park61.widget.dialog.AuthSuccessDialog;
import com.park61.widget.videorecorder.RecordActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/6/12.
 */
public class MainFirstActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private PermissionHelper.PermissionModel[] permissionModels = {
            new PermissionHelper.PermissionModel(0, Manifest.permission.CAMERA, "相机")
    };

    private PermissionHelper permissionHelper;

    private static final int REQUEST_LOGIN = 11;
    private static final int SCANNIN_GREQUEST_CODE = 1;// 扫描二维码请求

    public static boolean needFresh = true;

    private List<FirstHeadBean> dataList = new ArrayList<>();
    private FirstHeadAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private int cur_index = 0;//当前分类

    private LRecyclerView rv_firsthead;
    private ImageView img_fabu, img_publish_share_close, img_fb_close, img_msg, img_scan, img_video_cover;
    private View area_cover_fabu, area_import_video, area_huati, sousuo_area, area_start_record, area_publish_share, area_stick3;
    private TextView tv_msg_remind;

    private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
    private int[] BUTION_IDS = {R.id.rb_class, R.id.rb_first, R.id.rb_tv, R.id.rb_topic};
    private View[] stickArray = new View[4];
    private ViewPager pager;
    private boolean showNotice;//是否显示班级标签

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_main_first);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            lowSdkLayoutInit();
        }
    }

    @Override
    public void initView() {
        rv_firsthead = (LRecyclerView) findViewById(R.id.rv_firsthead);
        rv_firsthead.setLayoutManager(new LinearLayoutManager(mContext));
        img_fabu = (ImageView) findViewById(R.id.img_fabu);
        area_cover_fabu = findViewById(R.id.area_cover_fabu);
        area_import_video = findViewById(R.id.area_import_video);
        area_huati = findViewById(R.id.area_huati);
        sousuo_area = findViewById(R.id.sousuo_area);
        area_start_record = findViewById(R.id.area_start_record);
        area_publish_share = findViewById(R.id.area_publish_share);
        img_publish_share_close = (ImageView) findViewById(R.id.img_publish_share_close);
        img_fb_close = (ImageView) findViewById(R.id.img_fb_close);
        img_msg = (ImageView) findViewById(R.id.img_msg);
        img_scan = (ImageView) findViewById(R.id.img_scan);
        img_video_cover = (ImageView) findViewById(R.id.img_video_cover);
        tv_msg_remind = (TextView) findViewById(R.id.tv_msg_remind);
        area_stick3 = findViewById(R.id.area_stick3);
    }

    private void initShareViewAndData(String shareUrl, String sharePic, String shareTitle, String shareDescription) {
        ShareTool.init(shareUrl, sharePic, shareTitle, shareDescription);
        findViewById(R.id.img_fx_pyq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareTool.shareToWeiXin(mContext, 1);
            }
        });
        findViewById(R.id.img_fx_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareTool.shareToWeiXin(mContext, 0);
            }
        });
        findViewById(R.id.img_fx_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareTool.shareToQQ(mContext);
            }
        });
        findViewById(R.id.img_fx_qzone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareTool.shareToQzone(mContext);
            }
        });
    }

    @Override
    public void initData() {
        adapter = new FirstHeadAdapter(mContext, dataList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rv_firsthead.setAdapter(mLRecyclerViewAdapter);
        asyncSaveAppDeviceToken();
        if (GlobalParam.userToken != null) {
            asyncUpdateTkoenExpireTime();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GlobalParam.MSG_NUM <= 0) {
            tv_msg_remind.setVisibility(View.GONE);
        } else {
            tv_msg_remind.setVisibility(View.VISIBLE);
        }
        if (needFresh) {
            if (GlobalParam.userToken != null) {
                asyncGetAuthInfo();
                asyncListNotice();
            } else {
                showNotice = false;

                if (!CommonMethod.isListEmpty(fragmentList)) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    for (Fragment f : fragmentList) {
                        ft.remove(f);
                    }
                    ft.commitAllowingStateLoss();
                    ft = null;
                    getSupportFragmentManager().executePendingTransactions();
                }

                fragmentList.clear();
                FragmentMainFirstTop fragmentMainFirstTop = new FragmentMainFirstTop();
                fragmentList.add(fragmentMainFirstTop);

                FragmentMainFirst fragment1 = new FragmentMainFirst();
                Bundle data1 = new Bundle();
                data1.putInt("sType", 1);
                fragment1.setArguments(data1);
                fragmentList.add(fragment1);

                FragmentMainFirst fragment2 = new FragmentMainFirst();
                Bundle data2 = new Bundle();
                data2.putInt("sType", 2);
                fragment2.setArguments(data2);
                fragmentList.add(fragment2);

                BUTION_IDS = new int[]{R.id.rb_first, R.id.rb_tv, R.id.rb_topic};
                pager = (ViewPager) findViewById(R.id.pager);
                pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
                pager.setOffscreenPageLimit(3);
                stickArray = new View[3];
                stickArray[0] = findViewById(R.id.stick0);
                stickArray[1] = findViewById(R.id.stick1);
                stickArray[2] = findViewById(R.id.stick2);
                area_stick3.setVisibility(View.GONE);
                pager.setCurrentItem(cur_index, false);
                ((RadioButton) findViewById(R.id.rb_class)).setVisibility(View.GONE);
                ((RadioButton) findViewById(R.id.rb_first)).setChecked(true);
                ((RadioButton) findViewById(R.id.rb_first)).setOnCheckedChangeListener(MainFirstActivity.this);
                ((RadioButton) findViewById(R.id.rb_tv)).setOnCheckedChangeListener(MainFirstActivity.this);
                ((RadioButton) findViewById(R.id.rb_topic)).setOnCheckedChangeListener(MainFirstActivity.this);
                pager.addOnPageChangeListener(mOnPageChangeListener);
            }
            needFresh = false;
        }
    }

    @Override
    public void initListener() {
        permissionHelper = new PermissionHelper(MainFirstActivity.this);
        img_publish_share_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                area_publish_share.setVisibility(View.GONE);
            }
        });
        img_fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParam.userToken == null) {// 没有登录则跳去登录
                    startActivityForResult(new Intent(mContext, LoginV2Activity.class), REQUEST_LOGIN);
                    return;
                }
                area_cover_fabu.setVisibility(View.VISIBLE);
            }
        });
        img_fb_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                area_cover_fabu.setVisibility(View.GONE);
            }
        });
        area_start_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionHelper.setOnAlterApplyPermission(new PermissionHelper.OnAlterApplyPermission() {
                    @Override
                    public void OnAlterApplyPermission() {
                        goToRecord();
                    }
                });
                permissionHelper.setRequestPermission(permissionModels);
                if (Build.VERSION.SDK_INT < 23) {//6.0以下，不需要动态申请
                    goToRecord();
                } else {//6.0+ 需要动态申请
                    //判断是否全部授权过
                    if (permissionHelper.isAllApplyPermission()) {//申请的权限全部授予过，直接运行
                        goToRecord();
                    } else {//没有全部授权过，申请
                        //permissionHelper.applyPermission();
                        showShortToast("相机权限未开启，请在应用设置页面授权！");
                    }
                }
            }
        });
        area_import_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoSelectActivity.class);
                intent.putExtra(VideoSelectActivity.IMAGE_NUM, 9);
                startActivityForResult(intent, VideoSelectActivity.REQ_TO_FABU_VIDEO);
                area_cover_fabu.setVisibility(View.GONE);
            }
        });
        area_huati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TopicPublisActivity.class);
                startActivity(intent);
                area_cover_fabu.setVisibility(View.GONE);
            }
        });
        sousuo_area.setOnClickListener(new View.OnClickListener() {
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
    }

    /**
     * 去扫码
     */
    public void goToScaner() {
        Intent intent = new Intent();
        intent.setClass(mContext, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
        needFresh = true;
    }

    /**
     * 去拍摄
     */
    public void goToRecord() {
        Intent it = new Intent(mContext, RecordActivity.class);
        startActivityForResult(it, VideoSelectActivity.REQ_TO_FABU_VIDEO);
        area_cover_fabu.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        permissionHelper.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VideoSelectActivity.REQ_TO_FABU_VIDEO && resultCode == RESULT_OK) {
            area_publish_share.setVisibility(View.VISIBLE);
            shareUrl = AppUrl.APP_INVITE_URL + "/home/toMedialDetail.do?classifyType=1&itemId=" + data.getStringExtra("id");
            String imgUrl = data.getStringExtra("img");
            if (TextUtils.isEmpty(imgUrl)) {
                imgUrl = AppUrl.SHARE_APP_ICON;
            }
            initShareViewAndData(shareUrl, imgUrl, mContext.getResources().getString(R.string.app_name), data.getStringExtra("descrp"));
            ImageManager.getInstance().displayImg(img_video_cover, "file:///" + data.getStringExtra("path"));
            img_video_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent it = new Intent(mContext, FirstHeadVideoDetailsActivity.class);
                    it.putExtra("itemId", FU.paseLong(data.getStringExtra("id")));
                    mContext.startActivity(it);*/
                }
            });
            String shareType = data.getStringExtra("shareType");
            if (shareType.equals("-1")) {

            } else if (shareType.equals("0")) {
                ShareTool.shareToWeiXin(mContext, 1);
            } else if (shareType.equals("1")) {
                ShareTool.shareToQQ(mContext);
            } else if (shareType.equals("2")) {
                ShareTool.shareToQzone(mContext);
            } else if (shareType.equals("3")) {
                ShareTool.shareToWeiXin(mContext, 0);
            }
        } else if (requestCode == SCANNIN_GREQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String str = bundle.getString("result");
            CommonMethod.dealWithScanBack(str, mContext);
        } else if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
            area_cover_fabu.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (showNotice) {
                switch (buttonView.getId()) {
                    case R.id.rb_class:
                        changeTabList(0);
                        break;
                    case R.id.rb_first:
                        changeTabList(1);
                        break;
                    case R.id.rb_tv:
                        changeTabList(2);
                        break;
                    case R.id.rb_topic:
                        changeTabList(3);
                        break;
                }
            } else {
                switch (buttonView.getId()) {
                    case R.id.rb_first:
                        changeTabList(0);
                        break;
                    case R.id.rb_tv:
                        changeTabList(1);
                        break;
                    case R.id.rb_topic:
                        changeTabList(2);
                        break;
                }
            }
        }
    }

    private void changeTabList(int tabindex) {
        if (tabindex != cur_index) {
            cur_index = tabindex;
            pager.setCurrentItem(tabindex, true);
            showStick(tabindex);
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

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return BUTION_IDS.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) findViewById(BUTION_IDS[position]);
            radioButton.performClick();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void asyncGetAuthInfo() {
        String wholeUrl = AppUrl.host + AppUrl.authInfo;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, infoLsner);
    }

    BaseRequestListener infoLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            String data;
            try {
                data = jsonResult.getString("data");
            } catch (JSONException e) {
                e.printStackTrace();
                data = null;
            }
            if (data != null) {
                //startActivity(new Intent(mContext, AuthFirstActivity.class));
            } else {
                AuthInfoBean authInfoBean = gson.fromJson(jsonResult.toString(), AuthInfoBean.class);
                if (authInfoBean.getStatus() == 0) {//已申请认证，但是还没有审核，显示待审核页面，可以做取消认证操作
                    //startActivity(new Intent(mContext, AuthCheckInfoActivity.class));
                } else if (authInfoBean.getStatus() == 1) {//认证已通过，显示证书
                    if (authInfoBean.getAuditNoticeSign() == 0) {
                        AuthSuccessDialog mDialog = new AuthSuccessDialog(mContext);
                        mDialog.showDialog();
                        mDialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                netRequest.startRequest(AppUrl.host + AppUrl.getAuthNotice, Request.Method.POST, "", 0, new SimpleRequestListener());
                            }
                        });
                    }
                } else if (authInfoBean.getStatus() == 2) {//表示认证不通过
                    if (authInfoBean.getAuditNoticeSign() == 0) {
                        AuthFailedDialog mAuthFailedDialog = new AuthFailedDialog(mContext, authInfoBean.getAuditOpinion());
                        mAuthFailedDialog.showDialog();
                        mAuthFailedDialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                netRequest.startRequest(AppUrl.host + AppUrl.getAuthNotice, Request.Method.POST, "", 0, new SimpleRequestListener());
                            }
                        });
                    }
                }
            }
        }
    };

    private void asyncListNotice() {
        String wholeUrl = AppUrl.host + AppUrl.listNotice;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, lLsner);
    }

    BaseRequestListener lLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {

        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            showNotice = jsonResult.optBoolean("showNotice");
            if (!showNotice) {
                if (!CommonMethod.isListEmpty(fragmentList)) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    for (Fragment f : fragmentList) {
                        ft.remove(f);
                    }
                    ft.commitAllowingStateLoss();
                    ft = null;
                    getSupportFragmentManager().executePendingTransactions();
                }

                fragmentList.clear();
                FragmentMainFirstTop fragmentMainFirstTop = new FragmentMainFirstTop();
                fragmentList.add(fragmentMainFirstTop);

                FragmentMainFirst fragment1 = new FragmentMainFirst();
                Bundle data1 = new Bundle();
                data1.putInt("sType", 1);
                fragment1.setArguments(data1);
                fragmentList.add(fragment1);

                FragmentMainFirst fragment2 = new FragmentMainFirst();
                Bundle data2 = new Bundle();
                data2.putInt("sType", 2);
                fragment2.setArguments(data2);
                fragmentList.add(fragment2);

                BUTION_IDS = new int[]{R.id.rb_first, R.id.rb_tv, R.id.rb_topic};
                pager = (ViewPager) findViewById(R.id.pager);
                pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
                pager.setOffscreenPageLimit(3);
                stickArray = new View[3];
                stickArray[0] = findViewById(R.id.stick0);
                stickArray[1] = findViewById(R.id.stick1);
                stickArray[2] = findViewById(R.id.stick2);
                area_stick3.setVisibility(View.GONE);
                ((RadioButton) findViewById(R.id.rb_class)).setVisibility(View.GONE);
                ((RadioButton) findViewById(R.id.rb_first)).setChecked(true);
                ((RadioButton) findViewById(R.id.rb_first)).setOnCheckedChangeListener(MainFirstActivity.this);
                ((RadioButton) findViewById(R.id.rb_tv)).setOnCheckedChangeListener(MainFirstActivity.this);
                ((RadioButton) findViewById(R.id.rb_topic)).setOnCheckedChangeListener(MainFirstActivity.this);
                pager.addOnPageChangeListener(mOnPageChangeListener);
                pager.setCurrentItem(0, false);
            } else {
                if (!CommonMethod.isListEmpty(fragmentList)) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    for (Fragment f : fragmentList) {
                        ft.remove(f);
                    }
                    ft.commitAllowingStateLoss();
                    ft = null;
                    getSupportFragmentManager().executePendingTransactions();
                }

                fragmentList.clear();
                FragmentClassShow fragmentClassShow = new FragmentClassShow();
                fragmentList.add(fragmentClassShow);

                FragmentMainFirstTop fragmentMainFirstTop = new FragmentMainFirstTop();
                fragmentList.add(fragmentMainFirstTop);

                FragmentMainFirst fragment1 = new FragmentMainFirst();
                Bundle data1 = new Bundle();
                data1.putInt("sType", 1);
                fragment1.setArguments(data1);
                fragmentList.add(fragment1);

                FragmentMainFirst fragment2 = new FragmentMainFirst();
                Bundle data2 = new Bundle();
                data2.putInt("sType", 2);
                fragment2.setArguments(data2);
                fragmentList.add(fragment2);

                BUTION_IDS = new int[]{R.id.rb_class, R.id.rb_first, R.id.rb_tv, R.id.rb_topic};
                pager = (ViewPager) findViewById(R.id.pager);
                pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
                pager.setOffscreenPageLimit(4);
                stickArray = new View[4];
                stickArray[0] = findViewById(R.id.stick0);
                stickArray[1] = findViewById(R.id.stick1);
                stickArray[2] = findViewById(R.id.stick2);
                stickArray[3] = findViewById(R.id.stick3);
                area_stick3.setVisibility(View.VISIBLE);
                ((RadioButton) findViewById(R.id.rb_class)).setOnCheckedChangeListener(MainFirstActivity.this);
                ((RadioButton) findViewById(R.id.rb_class)).setVisibility(View.VISIBLE);
                ((RadioButton) findViewById(R.id.rb_class)).setChecked(true);
                ((RadioButton) findViewById(R.id.rb_first)).setOnCheckedChangeListener(MainFirstActivity.this);
                ((RadioButton) findViewById(R.id.rb_tv)).setOnCheckedChangeListener(MainFirstActivity.this);
                ((RadioButton) findViewById(R.id.rb_topic)).setOnCheckedChangeListener(MainFirstActivity.this);
                pager.addOnPageChangeListener(mOnPageChangeListener);
                pager.setCurrentItem(0, false);
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
