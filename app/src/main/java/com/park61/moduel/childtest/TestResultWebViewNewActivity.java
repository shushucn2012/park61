package com.park61.moduel.childtest;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.childtest.bean.TestResultBean;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TestResultWebViewNewActivity extends BaseActivity {

    private static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 1;
    private WebView wv;
    private ProgressBar pb;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;
    private TextView tv_btn_name;
    private View btn_finish;

    private int childId;
    private String relationId, batchCode;
    private TestResultBean mTestResultBean;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_testresult_webview);
    }

    @Override
    public void initView() {
        setPagTitle("测评结果");
        ((TextView) findViewById(R.id.tv_area_right)).setText("分享");
        pb = (ProgressBar) findViewById(R.id.pb);
        wv = (WebView) findViewById(R.id.wv);
        tv_btn_name = (TextView) findViewById(R.id.tv_btn_name);
        btn_finish = findViewById(R.id.btn_finish);
        initWebViewSetting();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //登录答题不会有relationId，未登录答题不会有userToken，两个都有就是未登录答完后登录
        if (GlobalParam.userToken != null && !TextUtils.isEmpty(relationId)) {
            ansyLoginCallback();
        }
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
        childId = getIntent().getIntExtra("childId", 0);
        relationId = getIntent().getStringExtra("relationId");
        batchCode = getIntent().getStringExtra("batchCode");
        String url = "http://m.61park.cn/#/dap/dapresult/" + batchCode;
        syncCookie(url);
        wv.loadUrl(url);
        ansyGetTestResult();
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
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTestResultBean == null)
                    return;
                shareUrl = "http://m.61park.cn/#/dap/dapresult/" + batchCode;
                sharePic = mTestResultBean.getImgUrl();
                shareTitle = mTestResultBean.getChildName() + "的测评结果";
                shareDescription = mTestResultBean.getEaSysName() + "的测评报告";
                showShareDialog(shareUrl, sharePic, shareTitle, shareDescription);
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
            } else if ("back".equals(forwardtype)) {
                finish();
            }
        }
    }

    public void ansyGetTestResult() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.TEST_RESULT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("batchCode", batchCode);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, testresultlisenter);
    }

    BaseRequestListener testresultlisenter = new JsonRequestListener() {

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
            ArrayList<TestResultBean> currentPageList = new ArrayList<TestResultBean>();
            mTestResultBean = gson.fromJson(jsonResult.toString(), TestResultBean.class);
            if (mTestResultBean.getStatus() == 1) {//完成
                if (GlobalParam.userToken != null) {//已登录
                    btn_finish.setVisibility(View.GONE);
                } else {//未登录
                    btn_finish.setVisibility(View.VISIBLE);
                    tv_btn_name.setText("保存");
                    btn_finish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dDialog.showDialog("提示", "保存答题记录必须登录，是否登录？", "取消", "确定", null, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(mContext, LoginV2Activity.class));
                                    dDialog.dismissDialog();
                                }
                            });
                        }
                    });
                }
            } else if (mTestResultBean.getStatus() == 0) {//未完成
                btn_finish.setVisibility(View.VISIBLE);
                tv_btn_name.setText("继续测评");
                btn_finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(mContext, TestQuestionNewActivity.class);
                        it.putExtra("childId", mTestResultBean.getChildId());
                        it.putExtra("eaServiceId", mTestResultBean.getEaServiceId());
                        it.putExtra("batchCode", mTestResultBean.getBatchCode());
                        startActivity(it);
                    }
                });
            }
        }
    };

    public void ansyLoginCallback() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.loginCallback;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("batchCode", batchCode);
        map.put("childId", childId);
        map.put("relationConstantId", relationId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, clisenter);
    }

    BaseRequestListener clisenter = new JsonRequestListener() {

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
            showShortToast("答题记录保存成功！请到答题记录页面查看");
            finish();
        }
    };

    /*private void asyncListSubjects() {
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
                TestQuestion question = new Gson().fromJson(actJot.toString(), TestQuestion.class);
                QuestionCache.questionList.add(question);
            }
            JSONArray unFinishedJay = jsonResult.optJSONArray("unFinishedSubjects");
            for (int i = 0; i < unFinishedJay.length(); i++) {
                JSONObject actJot = unFinishedJay.optJSONObject(i);
                TestQuestion question = new Gson().fromJson(actJot.toString(), TestQuestion.class);
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
    };*/

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
