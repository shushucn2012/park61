package com.park61.moduel.childtest;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.childtest.bean.QuestionCache;
import com.park61.moduel.childtest.bean.TestResultBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.pw.SharePopWin;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nieyu on 2017/12/7.
 */

public class TestResultActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_share, tv_bottom;
    ImageView iv_back;
    WebView wvContent;
    private WebSettings websettings;

    public int childId;
    public String relationId, batchCode;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_testresult);
    }

    @Override
    public void initView() {
        tv_bottom = (TextView) findViewById(R.id.tv_bottom);
        tv_share = (TextView) findViewById(R.id.tv_share);
        tv_share.setOnClickListener(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        wvContent = (WebView) findViewById(R.id.wvContent);
        wvContent.loadUrl("http://m.61park.cn/#/dap/dapresult/" + batchCode);
    }

    /**
     * 初始化web设置
     */
    private void initWebViewSetting() {
        WebSettings webSettings = wvContent.getSettings();
        websettings = wvContent.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setJavaScriptCanOpenWindowsAutomatically(true);
        websettings.setAppCacheEnabled(true);
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
        websettings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        wvContent.loadUrl("http://m.61park.cn/#/dap/dapresult/1234");
    }

    @Override
    public void initData() {
        childId = getIntent().getIntExtra("childId", 0);
        relationId = getIntent().getStringExtra("relationId");
        batchCode = getIntent().getStringExtra("batchCode");

        QuestionCache.chosenChildId = childId;
        QuestionCache.relationId = relationId;
        QuestionCache.batchCode = batchCode;

        ansyGetTestResult();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onClick(View view) {
        int viewid = view.getId();
        if (R.id.tv_share == viewid) {
            toShare();
        } else if (R.id.iv_back == viewid) {
            finish();
        }
    }

    public void toShare() {
        Intent its = new Intent(mContext, SharePopWin.class);
        String shareUrl = "";
//        shareUrl= "http://m.61park.cn/#/home/homecontent/"+itemid;
//        String picUrl =textBean.getCoverImg();
        String title = "XX（小孩的昵称）的测频结果";
        String description = "XXX（测评服务的名称）的测评报告";
        shareUrl= "http://m.61park.cn/#/dap/dapresult/1234";
        String picUrl =p.getImgUrl();
        title = p.getChildName()+"的测频结果";
        description = p.getEaSysName()+"的测评报告";
        its.putExtra("shareUrl", shareUrl);
        its.putExtra("picUrl", picUrl);
        its.putExtra("title", title);
        its.putExtra("description", description);
        mContext.startActivity(its);
    }

    TestResultBean p;
    public void ansyGetTestResult() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.TEST_RESULT;
//        String wholeUrl = "http://192.168.100.14:8080/mockjsdata/12/service/eaUserResult/showEaUserResult";
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
            TestResultBean p = gson.fromJson(jsonResult.toString(), TestResultBean.class);
            if (p.getStatus() == 0) {
                tv_bottom.setText("继续测评");
            } else if (p.getStatus() == 1) {
                tv_bottom.setText("完成测评");
            }

        }
    };

}
