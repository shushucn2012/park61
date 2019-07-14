package com.park61;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.park61.common.set.GlobalParam;
import com.park61.common.set.Log;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.FU;
import com.park61.moduel.acts.ActDetailsActivity;
import com.park61.moduel.dreamhouse.DreamDetailActivity;
import com.park61.moduel.dreamhouse.DreamThemeListActivity;
import com.park61.moduel.grouppurchase.GroupPurchaseActivity;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.moduel.openmember.OpenMemberMainActivity;
import com.park61.moduel.pay.BalanceActivity;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.MaMaGroupActivity;
import com.park61.moduel.sales.OverValueActivity;
import com.park61.moduel.sales.OverseasGoodsActivity;
import com.park61.moduel.sales.SecKillActivityNew;
import com.park61.moduel.toyshare.TSGetSuccessActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.park61.common.tool.ViewInitTool.initWebViewSetting;
import static com.park61.common.tool.ViewInitTool.syncWebViewCookie;

/**
 * 通用网页展示页, 回退按钮可以回退h5页面
 */
public class CanBackWebViewActivity extends BaseActivity {

    private static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 1;
    private TextView tv_page_title;
    private WebView wv;
    private ProgressBar pb;
    private Button btn_close;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_canback_webview);
    }

    @Override
    public void initView() {
        tv_page_title = (TextView) findViewById(R.id.tv_page_title);
        pb = (ProgressBar) findViewById(R.id.pb);
        wv = (WebView) findViewById(R.id.wv);
        btn_close = (Button) findViewById(R.id.btn_close);
        initWebViewSetting(wv.getSettings());
    }

    @Override
    public void initData() {
        //关闭顶部右侧按钮
        String title = getIntent().getStringExtra("PAGE_TITLE");
        if (!TextUtils.isEmpty(title)) {
            tv_page_title.setText(title);
        }
        String url = getIntent().getStringExtra("url");
        logout("url======" + url);
        syncWebViewCookie(mContext, url);
        wv.loadUrl(url);
        if (url.contains("toGrowEaResult")) {
            String shareUrl = "http://m.61park.cn/ea/toEaIntroduce.do?eaKey=31c7f6b070ebfa101660a4bd4df75063";
            String picUrl = getIntent().getStringExtra("picUrl");
            String titleStr = getIntent().getStringExtra("title");
            String description = getIntent().getStringExtra("content");
            initShareData(shareUrl, picUrl, titleStr, description);
        } else {
            initShareData(url, getIntent().getStringExtra("picUrl"), "61区", title);
        }
    }

    @Override
    public void initListener() {
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                // Otherwise allow the OS to handle things like tel, mailto, etc.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });
        // 在js中调用本地java方法
        wv.addJavascriptInterface(new JsInterface(), "Android");
        //加载进度
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    pb.setVisibility(View.GONE);
                } else {
                    // 加载中
                    pb.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                tv_page_title.setText(title);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
                openFileChooserImplForAndroid5(uploadMsg);
                return true;
            }
        });
        left_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wv.canGoBack()) {
                    wv.goBack();
                } else {
                    finish();
                }
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
    }

    /**
     * 初始化分享数据
     *
     * @param url    跳转链接
     * @param picUrl 分享图片链接
     * @param title  分享标题
     * @param desc   分享描述
     */
    private void initShareData(String url, String picUrl, String title, String desc) {
        shareUrl = url;
        sharePic = picUrl;
        shareTitle = title;
        shareDescription = desc;
    }

    /**
     * 处理js回调传回的json数据
     */
    private class JsInterface {
        // 在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void call(String jsonStr) {
            logout("jsonStr====" + jsonStr);
            //Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
                if (jsonStr != null && jsonStr.contains("userRechargeDetail")) {//会员充值
                    if (GlobalParam.userToken == null) {// 未登录则先去登录
                        startActivity(new Intent(mContext, LoginV2Activity.class));
                        return;
                    }
                    startActivity(new Intent(mContext, OpenMemberMainActivity.class));
                    return;
                }
                if (jsonStr != null && jsonStr.contains("getToken")) {//会员充值
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    finish();
                }
                if (jsonStr != null && jsonStr.contains("setShare")) {//分享
                    showShareDialog(shareUrl, sharePic, shareTitle, shareDescription);
                    return;
                }
                if (jsonStr != null && jsonStr.contains("nextDAP")) {//分享
                   /* startActivity(new Intent(mContext, StartTestActivity.class));
                    finish();*/
                }
            }
            JSONObject data = jsonObject.optJSONObject("data");
            String forwardtype = data.optString("forwardtype");
            logout("forwardtype======" + forwardtype);
            if ("produectDetail".equals(forwardtype)) {//商品详情
                JSONObject param = data.optJSONObject("params");
                long pmInfoId = param.optLong("pmInfoId");
                logout("pmInfoId======" + pmInfoId);
                Intent it = new Intent(mContext, GoodsDetailsActivity.class);
                it.putExtra("goodsId", pmInfoId);
                it.putExtra("goodsOldPrice", "");
                it.putExtra("goodsPrice", "");
                CommonMethod.startOnlyNewActivity(mContext, GoodsDetailsActivity.class, it);
                //startActivity(it);
            } else if (jsonStr != null && jsonStr.contains("userRechargeDetail")) {//会员充值
                if (GlobalParam.userToken == null) {// 未登录则先去登录
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                startActivity(new Intent(mContext, OpenMemberMainActivity.class));
            } else if ("userBalance".equals(forwardtype)) {//余额充值
                if (GlobalParam.userToken == null) {// 未登录则先去登录
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                startActivity(new Intent(mContext, BalanceActivity.class));
            } else if ("activityDetail".equals(forwardtype)) {//活动详情
                JSONObject param = data.optJSONObject("params");
                long actId = param.optLong("actId");
                logout("actId======" + actId);
                Intent it = new Intent(mContext, ActDetailsActivity.class);
                it.putExtra("id", actId);
                startActivity(it);
            } else if ("mallSeckillDetail".equals(forwardtype)) {
                JSONObject param = data.optJSONObject("params");
                String promotionType = param.optString("promotionType");
                String title = param.optString("promotionName");
                Intent it = new Intent(mContext, SecKillActivityNew.class);
                it.putExtra("promotionType", promotionType);
                it.putExtra("title", title);
                startActivity(it);
            } else if ("mallRabbitGroupBuyingDetail".equals(forwardtype)) {
                JSONObject param = data.optJSONObject("params");
                String promotionType = param.optString("promotionType");
                String title = param.optString("promotionName");
                Intent it = new Intent(mContext, GroupPurchaseActivity.class);
                it.putExtra("promotionType", promotionType);
                it.putExtra("title", title);
                startActivity(it);
            } else if ("mallMamaGroupBuyingDetail".equals(forwardtype)) {
                JSONObject param = data.optJSONObject("params");
                String promotionType = param.optString("promotionType");
                String title = param.optString("promotionName");
                Intent it = new Intent(mContext, MaMaGroupActivity.class);
                it.putExtra("promotionType", promotionType);
                it.putExtra("title", title);
                startActivity(it);
            } else if ("mallSuperLimitDetail".equals(forwardtype)) {
                JSONObject param = data.optJSONObject("params");
                String promotionType = param.optString("promotionType");
                String title = param.optString("promotionName");
                Intent it = new Intent(mContext, OverValueActivity.class);
                it.putExtra("promotionType", promotionType);
                it.putExtra("title", title);
                startActivity(it);
            } else if ("mallOverseasProductDetail".equals(forwardtype)) {
                JSONObject param = data.optJSONObject("params");
                String promotionType = param.optString("promotionType");
                String title = param.optString("promotionName");
                Intent it = new Intent(mContext, OverseasGoodsActivity.class);
                it.putExtra("promotionType", promotionType);
                it.putExtra("title", title);
                startActivity(it);
            } else if ("getToken".equals(forwardtype)) {
                startActivity(new Intent(mContext, LoginV2Activity.class));
                finish();
            } else if ("setShare".equals(forwardtype)) {
                showShareDialog(shareUrl, sharePic, shareTitle, shareDescription);
            } else if ("nextDAP".equals(forwardtype)) {
               /* Intent it = new Intent(mContext, StartTestActivity.class);
                CommonMethod.startOnlyNewActivity(mContext, StartTestActivity.class, it);
                finish();*/
            } else if ("requirementDetail".equals(forwardtype)) {
                logout("forwardtype======requirementDetail======");
                JSONObject param = data.optJSONObject("params");
                long requirementId = param.optLong("requirementId");
                Intent it = new Intent(mContext, DreamDetailActivity.class);
                it.putExtra("requirementId", requirementId);
                startActivity(it);
            } else if ("requirementClassify".equals(forwardtype)) {
                logout("forwardtype======requirementClassify======");
                JSONObject param = data.optJSONObject("params");
                String classIds = param.optString("requirementClassIds");
                String cityIds = param.optString("requirementCityIds");
                String requirementClassName = param.optString("requirementClassName");
                Intent it = new Intent(mContext, DreamThemeListActivity.class);
                it.putExtra("cityIds", cityIds);
                it.putExtra("classIds", classIds);
                it.putExtra("requirementClassName", requirementClassName);
                startActivity(it);
            } else if ("toGrowIndex".equals(forwardtype)) {
                JSONObject param = data.optJSONObject("params");
                String childId = param.optString("childId");
                GlobalParam.CUR_CHILD_ID = FU.paseLong(childId);
                GlobalParam.GrowingMainNeedRefresh = true;
                Intent changeIt = new Intent();
                changeIt.setAction("ACTION_TAB_CHANGE");
                changeIt.putExtra("TAB_NAME", "tab_grow");
                mContext.sendBroadcast(changeIt);
                finish();
            } else if ("toShareToySuccess".equals(forwardtype)) {
                Intent it = new Intent(mContext, TSGetSuccessActivity.class);
                JSONObject param = data.optJSONObject("params");
                String applyId = param.optString("applyId");
                String seriesId = param.optString("seriesId");
                String title = param.optString("title");
                String coverUrl = param.optString("coverUrl");


                it.putExtra("applyId", applyId);
                it.putExtra("seriesId", seriesId);
                it.putExtra("title", title);
                it.putExtra("coverUrl", coverUrl);

                it.putExtra("state", "0");
                startActivity(it);
                finish();
            } else if ("toShareToyFail".equals(forwardtype)) {
                Intent it = new Intent(mContext, TSGetSuccessActivity.class);
                JSONObject param = data.optJSONObject("params");
                String applyId = param.optString("applyId");
                String seriesId = param.optString("seriesId");
                String title = param.optString("title");
                String coverUrl = param.optString("coverUrl");

                it.putExtra("applyId", applyId);
                it.putExtra("seriesId", seriesId);
                it.putExtra("title", title);
                it.putExtra("coverUrl", coverUrl);

                it.putExtra("state", "1");
                startActivity(it);
                finish();
            }
        }
    }

}
