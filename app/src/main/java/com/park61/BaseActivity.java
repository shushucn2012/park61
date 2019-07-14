package com.park61;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.player.AliyunErrorCode;
import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.aliyun.vodplayerview.utils.NetWatchdog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.park61.common.json.NullStringToEmptyAdapterFactory;
import com.park61.common.myokhttptest.MyHttpUtils;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.Log;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.ExitAppUtils;
import com.park61.common.tool.FU;
import com.park61.moduel.firsthead.MainHomeActivity;
import com.park61.moduel.growing.GrowingActivity;
import com.park61.moduel.me.BindPhoneActivity;
import com.park61.moduel.me.MeActivity;
import com.park61.moduel.okdownload.DownloadService;
import com.park61.moduel.okdownload.db.DownloadDAO;
import com.park61.moduel.okdownload.db.DownloadTask;
import com.park61.moduel.sales.SalesMainV2Activity;
import com.park61.net.base.Request;
import com.park61.net.request.SimpleRequestListener;
import com.park61.net.request.StringNetRequest;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.CommonProgressDialog;
import com.park61.widget.dialog.DoubleDialog;
import com.park61.widget.pw.NaviShopPopWin;
import com.park61.widget.pw.SharePopWin;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础activity类
 *
 * @author super
 */
public abstract class BaseActivity extends FragmentActivity {
    public Context mContext;
    /**
     * log打印需要用到的标签
     */
    public String Tag = "BaseActivity";
    public String actTag;// 网络请求标记，用于标识请求以便取消
    /**
     * 传入参数为字符串拼接方式的网络请求实例
     */
    //public StringNetRequest netRequest;
    public MyHttpUtils netRequest;
    public CommonProgressDialog commonProgressDialog;
    public DoubleDialog dDialog;

    public Button left_img, right_img;
    public View area_right, area_left;
    public TextView page_title;
    public DisplayImageOptions options;
    public AliyunDownloadManager downloadManager;
    public NetWatchdog netWatchdog;
    public Gson gson;

    public String shareUrl = "", sharePic = "", shareTitle = "", shareDescription = "";

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //友盟推送
        //PushAgent.getInstance(mContext).onAppStart();
        setLayout();
        ExitAppUtils.getInstance().addActivity(this);
        Tag = this.getClass().getSimpleName();
        logout("Tag------onCreate-----" + Tag);
        actTag = Tag + System.currentTimeMillis();
        netRequest = MyHttpUtils.getInstance(this);//StringNetRequest.getInstance(mContext);
        netRequest.setActTag(actTag);
        commonProgressDialog = new CommonProgressDialog(mContext);
        // 进度框不可取消
        setCancelable(false);
        downloadManager = AliyunDownloadManager.getInstance(getApplicationContext());
        netWatchdog = new NetWatchdog(this);
        netWatchdog.setNetChangeListener(new NetWatchdog.NetChangeListener() {
            @Override
            public void onWifiTo4G() {
                if (GHPApplication.isShowNetDialog && GlobalParam.userToken != null && hasDownloading()) {
                    showNetChangeDialog();
                }
            }

            @Override
            public void on4GToWifi() {
//                if (GHPApplication.isShowNetDialog && GlobalParam.userToken != null && hasDownloading()) {
//                    //重新进入时，开始下载全部
//                    showNetChangeDialog();
//                }
            }

            @Override
            public void onNetDisconnected() {

            }
        });
        netWatchdog.startWatch();
        gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
        dDialog = new DoubleDialog(mContext);
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.img_default_v)
                .showImageOnLoading(R.drawable.img_default_v)
                .showImageOnFail(R.drawable.img_default_v)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .delayBeforeLoading(100)
                .build();
        initView();
        initTopBar();
        initData();
        initListener();
    }

    private boolean hasDownloading() {
        List<AliyunDownloadMediaInfo> downloadingMedias = downloadManager.getUnfinishedDownload();
        if (downloadingMedias != null) {
            for (AliyunDownloadMediaInfo info : downloadingMedias) {
                if (info.getStatus() != AliyunDownloadMediaInfo.Status.Complete && info.getProgress() < 100) {
                    return true;
                }
            }
        }
        return false;
    }

    private AlertDialog netChangeDialog = null;

    private void showNetChangeDialog() {
        if (netChangeDialog == null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialogBuilder.setTitle(R.string.net_change_to_4g);
            dialogBuilder.setMessage(R.string.continue_to_download);
            dialogBuilder.setPositiveButton(R.string.ok, (dialog, which) -> {
                netChangeDialog = null;
                dialog.dismiss();
                GHPApplication.isShowNetDialog = false;
                List<AliyunDownloadMediaInfo> downloadMediaInfos = downloadManager.getUnfinishedDownload();
                if (downloadMediaInfos != null) {
                    for (AliyunDownloadMediaInfo info : downloadMediaInfos) {
                        if (info.getStatus() != AliyunDownloadMediaInfo.Status.Complete) {
                            //重新下载
                            int tmp = downloadManager.startDownloadMedia(info);
                            Log.e(Tag, "继续下载:" + tmp);
                            //如果退出应用重新打开app时，tmp不为0无法继续下载，重新添加到service
                            if (tmp != AliyunErrorCode.ALIVC_SUCCESS.getCode()) {
                                Log.e(Tag, "error, renew add");
                                reDownload(info.getVid());
                            }
                        }
                    }
                }
            });
            dialogBuilder.setNegativeButton(R.string.no, (dialog, which) -> {
                //取消全部下载
                GHPApplication.isShowNetDialog = false;
                netChangeDialog = null;
                dialog.dismiss();
                List<AliyunDownloadMediaInfo> downloadMediaInfos = downloadManager.getUnfinishedDownload();
                if (downloadMediaInfos != null) {
                    for (AliyunDownloadMediaInfo info : downloadMediaInfos) {
                        if (info.getStatus() != AliyunDownloadMediaInfo.Status.Complete) {
                            //暂停
                            downloadManager.stopDownloadMedia(info);
                        }
                    }
                }
            });
            dialogBuilder.setCancelable(false);
            netChangeDialog = dialogBuilder.create();
        }
        if (!netChangeDialog.isShowing()) {
            netChangeDialog.show();
        }
    }

    public void reDownload(String vid) {
        String sid = DownloadDAO.getInstance().selectSid(vid);
        getPlayAuth(sid);
    }

    /**
     * 退出应用重新进入时，重新下载需要凭证
     */
    public void getPlayAuth(String sid) {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.videoPlayAuth;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", sid);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, alister);
    }

    BaseRequestListener alister = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            String vid = jsonResult.optString("videoId");
            String playAuth = jsonResult.optString("videoPlayAuth");

            DownloadTask t = DownloadDAO.getInstance().selectTask(vid);
            if (t != null) {
                Intent it = new Intent(mContext, DownloadService.class);
                it.putExtra("title", t.getTask_name());
                it.putExtra("sid", FU.paseLong(t.getSourceId()));
                it.putExtra("vid", t.getTask_vid());
                it.putExtra("contentId", FU.paseLong(t.getContentId()));
                it.putExtra("type", t.getTask_type());
                it.putExtra("icon", t.getTask_icon());
                it.putExtra("playAuth", playAuth);
                startService(it);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Tag);
        MobclickAgent.onResume(mContext);
        netRequest.setActTag(actTag);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Tag);
        MobclickAgent.onPause(mContext);
        logout("Tag------onPause-----" + Tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        netRequest.setActTag(actTag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        netRequest.cancelAllRequest(actTag);
        netWatchdog.stopWatch();
        logout("---onDestroy---netRequest---cancelAllRequest---" + actTag);
        ExitAppUtils.getInstance().delActivity(this);
    }

    /**
     * 设置布局
     */
    public abstract void setLayout();

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化监听事件
     */
    public abstract void initListener();

    private void initTopBar() {
        left_img = (Button) findViewById(R.id.left_img);
        if (left_img != null) {
            left_img.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        area_left = findViewById(R.id.area_left);
        if (area_left != null) {
            area_left.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        area_right = findViewById(R.id.area_right);
        if (area_right != null) {
            area_right.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    NaviShopPopWin mNaviShopPopWin = new NaviShopPopWin(mContext, rightMenuClickedLsner, null, true);
                    int offX = DevAttr.dip2px(mContext, 12);
                    int offY = DevAttr.dip2px(mContext, -18);
                    mNaviShopPopWin.showAsDropDown(area_right, offX, offY);
                }
            });
        }
    }

    /**
     * 设置页面标题
     */
    public void setPagTitle(String str) {
        page_title = (TextView) findViewById(R.id.page_title);
        page_title.setText(str);
    }

    public OnClickListener rightMenuClickedLsner = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.view_home:
                    backToHome();
                    break;
                case R.id.view_share:
                    showShareDialog(shareUrl, sharePic, shareTitle, shareDescription);
                    break;
                case R.id.view_conactor:
                    break;
            }
        }
    };

    /**
     * 左侧按钮监听
     */
    public void setLeftListener(OnClickListener listener) {
        if (left_img != null)
            left_img.setOnClickListener(listener);
    }

    /**
     * 打印日志
     */
    public void logout(String msg) {
        Log.out(msg);
    }

    /**
     * 显示分享
     *
     * @param shareUrl    分享链接
     * @param picUrl      分享图像
     * @param title       分享标题
     * @param description 分享描述
     */
    public void showShareDialog(String shareUrl, String picUrl, String title, String description) {
        Intent it = new Intent(mContext, SharePopWin.class);
        it.putExtra("shareUrl", shareUrl);
        it.putExtra("picUrl", picUrl);
        it.putExtra("title", title);
        it.putExtra("description", description);
        startActivity(it);
    }

    /**
     * 显示分享
     *
     * @param shareUrl    分享链接
     * @param picUrl      分享图像
     * @param title       分享标题
     * @param description 分享描述
     */
    public void showShareDialog(String shareUrl, String picUrl, String title, String description, long voteId, long voteApplyId) {
        Intent it = new Intent(mContext, SharePopWin.class);
        it.putExtra("shareUrl", shareUrl);
        it.putExtra("picUrl", picUrl);
        it.putExtra("title", title);
        it.putExtra("description", description);
        it.putExtra("voteId", voteId);
        it.putExtra("voteApplyId", voteApplyId);
        startActivity(it);
    }

    /**
     * toast短提示
     */
    public void showShortToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    public void showDialog() {
        try {
            if (commonProgressDialog.isShow()) {
                return;
            } else {
                commonProgressDialog.showDialog(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog(String msg) {
        try {
            if (commonProgressDialog.isShow()) {
                commonProgressDialog.setMessage(msg);
            } else {
                commonProgressDialog.showDialog(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCancelable(boolean is) {
        commonProgressDialog.setCancelable(is);
    }

    public void dismissDialog() {
        try {
            if (commonProgressDialog.isShow()) {
                commonProgressDialog.dialogDismiss();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDialogShowing() {
        return commonProgressDialog.isShow();
    }

    /**
     * 判断点击区域是否在评论输入框内
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null) {// && (v instanceof EditText)
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证是否需要绑定手机号： 1,未绑定手机号则提示，确定去绑定页面；取消关闭当前页面 2,已绑定手机号，不做任何提示
     */
    public void doValidatePhoneEmpty() {
        if (TextUtils.isEmpty(GlobalParam.currentUser.getMobile())) {
            dDialog.showDialog("提醒", "您还未绑定手机号，请绑定！", "确定", "取消",
                    new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            startActivity(new Intent(mContext,
                                    BindPhoneActivity.class));
                            dDialog.dismissDialog();
                        }
                    }, new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            dDialog.dismissDialog();
                            finish();
                        }
                    });
            dDialog.setCancelable(false);
        }
    }

    /**
     * 返回首页
     */
    public void backToHome() {
        for (Activity mActivity : ExitAppUtils.getInstance().getActList()) {
            if (!(mActivity instanceof MainTabActivity)
                    && !(mActivity instanceof GrowingActivity)
                    && !(mActivity instanceof SalesMainV2Activity)
                    && !(mActivity instanceof MeActivity)
                    && !(mActivity instanceof MainHomeActivity)) {
                if (mActivity != null) {// 关闭所有页面，置回首页
                    mActivity.finish();
                    Intent changeIt = new Intent();
                    changeIt.setAction("ACTION_TAB_CHANGE");
                    changeIt.putExtra("TAB_NAME", "tab_main");
                    sendBroadcast(changeIt);
                }
            }
        }
    }

    public void lowSdkLayoutInit() {
        findViewById(R.id.top_space_area).setVisibility(View.GONE);
        findViewById(R.id.top_bar).setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, DevAttr.dip2px(
                        mContext, 44)));
    }

    /**
     * 请求添加分享
     * source：来源：1投票，2图文，3视频，4玩具共享	必填
     * voteId：sourceId对应source表的主键	必填
     * type：source=1时必填：投票分享类型:1:拉票;2:活动分享
     * voteApplyId：type=1时必填：拉票报名记录主键
     */
    public void asyncVoteShares(int source, int type, Long voteId, Long voteApplyId) {
        String wholeUrl = AppUrl.host + AppUrl.VOTESHARE;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("source", source);
        map.put("type", type);
        map.put("voteId", voteId);
        map.put("voteApplyId", voteApplyId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, new SimpleRequestListener());
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

}
