package com.park61.moduel.childtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.childtest.bean.QuestionCache;
import com.park61.moduel.childtest.bean.TestQuestion;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class TestResultWebViewActivity extends BaseActivity {

    private static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 1;
    private WebView wv;
    private ProgressBar pb;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;

    private int eaItemId;
    private int childId;
    private int resultId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_notop_webview);
    }

    @Override
    public void initView() {
        pb = (ProgressBar) findViewById(R.id.pb);
        wv = (WebView) findViewById(R.id.wv);
        initWebViewSetting();
    }

    /**
     * 初始化web设置
     */
    private void initWebViewSetting() {
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
    }

    @Override
    public void initData() {
        String title = getIntent().getStringExtra("PAGE_TITLE");
        String url = getIntent().getStringExtra("url");
        logout("url======" + url);
        syncCookie(url);
        wv.loadUrl(url);

        shareUrl = url;
        sharePic = getIntent().getStringExtra("picUrl");
        ;
        shareTitle = title;
        shareDescription = getIntent().getStringExtra("content");
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
                if (newProgress == 100) { // 网页加载完成
                    pb.setVisibility(View.GONE);
                } else {  // 加载中
                    pb.setProgress(newProgress);
                }
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
                openFileChooserImplForAndroid5(uploadMsg);
                return true;
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
     * 处理js回调传回的json数据
     */
    private class JsInterface {
        // 在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void call(String jsonStr) {
            logout("jsonStr====" + jsonStr);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject data = jsonObject.optJSONObject("data");
            String forwardtype = data.optString("forwardtype");
            logout("forwardtype======" + forwardtype);
            if ("getToken".equals(forwardtype)) {
                startActivity(new Intent(mContext, LoginV2Activity.class));
                finish();
            } else if ("setShare".equals(forwardtype)) {
                showShareDialog(shareUrl, sharePic, shareTitle, shareDescription);
            } else if ("nextDAP".equals(forwardtype)) {
                JSONObject param = data.optJSONObject("params");
                eaItemId = param.optInt("eaItemId");
                childId = param.optInt("childId");
                resultId = param.optInt("resultId");
                asyncListSubjects();
            } else if ("back".equals(forwardtype)) {
                finish();
            }
        }
    }

    private void asyncListSubjects() {
        String wholeUrl = AppUrl.host + AppUrl.LISTSUBJECTS_V2;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eaItemId", eaItemId);
        map.put("childId", childId);
        map.put("resultId", resultId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listSubjectsLsner);
    }

    BaseRequestListener listSubjectsLsner = new JsonRequestListener() {

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
            QuestionCache.questionList.clear();
            JSONArray finishedJay = jsonResult.optJSONArray("finishedSubjects");
            for (int i = 0; i < finishedJay.length(); i++) {
                JSONObject actJot = finishedJay.optJSONObject(i);
                TestQuestion question = gson.fromJson(actJot.toString(), TestQuestion.class);
                QuestionCache.questionList.add(question);
            }
            JSONArray unFinishedJay = jsonResult.optJSONArray("unFinishedSubjects");
            for (int i = 0; i < unFinishedJay.length(); i++) {
                JSONObject actJot = unFinishedJay.optJSONObject(i);
                TestQuestion question = gson.fromJson(actJot.toString(), TestQuestion.class);
                QuestionCache.questionList.add(question);
            }
            QuestionCache.eaItemId = eaItemId;
            QuestionCache.chosenChildId = childId;
            Intent it = new Intent(mContext, TestQuestionActivity.class);
            it.putExtra("finished_num", finishedJay.length());
            it.putExtra("resultId", resultId + "");
            startActivity(it);
            finish();
        }
    };

    /**
     * 设置浏览器cookie值
     *
     * @param url
     */
    private void syncCookie(String url) {
        try {
            CookieSyncManager.createInstance(mContext);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("ut=%s", GlobalParam.userToken));
            cookieManager.setCookie(url, sbCookie.toString());
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {
        }
    }
}
