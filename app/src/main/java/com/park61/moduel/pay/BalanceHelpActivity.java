package com.park61.moduel.pay;

import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.park61.BaseActivity;
import com.park61.R;

/**
 * 账户余额帮助说明
 */
public class BalanceHelpActivity extends BaseActivity {
    private WebView wv_help;
    private ProgressBar pb;

    @Override
    public void setLayout() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_blance_help);
    }

    @Override
    public void initView() {
        wv_help = (WebView) findViewById(R.id.wv_help);
        pb = (ProgressBar) findViewById(R.id.pb);
        WebSettings webSettings = wv_help.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);//为true阻止图片网络数据,false解除数据阻止
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//设置缓存模式
        if(Build.VERSION.SDK_INT>=21){
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setDomStorageEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        //修改ua让后台识别
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua+"; 61park/android");
        //设置可以支持缩放
        webSettings.setSupportZoom(true);
        //设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        //设置扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //设置自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
    }

    @Override
    public void initData() {
        String url = "http://m.61park.cn/html-sui/balanceExplain.html";
        wv_help.loadUrl(url);
        //加载进度
        wv_help.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100){
                    //网页加载完成
                   pb.setVisibility(View.GONE);
                }else{
                    //加载中
                    pb.setProgress(newProgress);
                }
            }
        });
    }

    @Override
    public void initListener() {

    }
}
