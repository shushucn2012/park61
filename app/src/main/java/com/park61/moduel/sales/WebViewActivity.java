package com.park61.moduel.sales;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.Log;
import com.park61.common.tool.CommonMethod;
import com.park61.moduel.acts.ActDetailsActivity;
import com.park61.moduel.dreamhouse.DreamDetailActivity;
import com.park61.moduel.dreamhouse.DreamThemeListActivity;
import com.park61.moduel.grouppurchase.GroupPurchaseActivity;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.moduel.openmember.OpenMemberMainActivity;
import com.park61.moduel.pay.BalanceActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class WebViewActivity extends BaseActivity {

    private TextView tv_page_title;
    private WebView wv;
    private ProgressBar pb;

    @Override
    public void setLayout() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_webview);
    }

    @Override
    public void initView() {
        tv_page_title = (TextView) findViewById(R.id.tv_page_title);
        pb = (ProgressBar) findViewById(R.id.pb);
        wv = (WebView) findViewById(R.id.wv);
        WebSettings webSettings = wv.getSettings();
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
        //wv.setInitialScale(60);
    }

    @Override
    public void initData() {
        String title = getIntent().getStringExtra("PAGE_TITLE");
        if (!TextUtils.isEmpty(title)) {
            tv_page_title.setText(title);
        }
        String url = getIntent().getStringExtra("url");
        /*if(!TextUtils.isEmpty(GlobalParam.userToken)) {
            url += "&token=" + GlobalParam.userToken;
        }*/
        syncCookie(url);
        if (url.contains("http:") || url.contains("https:")) {
            logout("url======" + url);
            wv.loadUrl(url);
        } else {
            wv.loadUrl("http://" + url);
        }
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                //view.loadUrl(url);
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
        });

        shareUrl = url;
        sharePic = getIntent().getStringExtra("picUrl");
        shareTitle = "61区";
        shareDescription = title;
    }

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
            }
        }
    }

    @Override
    public void initListener() {

    }

    private void syncCookie(String url) {
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

}
