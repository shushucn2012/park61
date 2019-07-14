package com.park61;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.park61.common.json.NullStringToEmptyAdapterFactory;
import com.park61.moduel.umeng.UmengResponse;
import com.umeng.message.UmengNotifyClickActivity;
import com.umeng.message.common.UmLog;

import org.android.agoo.common.AgooConstants;

/**
 * Created by zhangchi on 2017/9/18.
 */

public class PushAdapterActivity extends UmengNotifyClickActivity {

    private static String TAG = PushAdapterActivity.class.getName();
    private static final String WEB_VIEW_URL = "web_view_url";

    private Context     mContext;
//    private TextView    mipushTextView;
    private WebView      webview;
    private LinearLayout btnGoBack;

    //定义Handler对象
    private Handler handler =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            String value = msg.obj.toString();
//            mipushTextView.setText(value);

            webview.loadUrl(value);
            webview.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    WebView.HitTestResult hit = view.getHitTestResult();
                    int hitType = hit.getType();
                    if (hitType == WebView.HitTestResult.SRC_ANCHOR_TYPE) {//点击超链接
                        //这里执行自定义的操作
                        return true;//返回true浏览器不再执行默认的操作
                    }else if(hitType == 0){//重定向时hitType为0
                        return false;//不捕获302重定向
                    }else{
                        return false;
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mipush);

//        mipushTextView = (TextView) findViewById(R.id.mipushTextView);
        webview = (WebView) findViewById(R.id.webview);
        btnGoBack = (LinearLayout) findViewById(R.id.area_left);

        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webview.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webview.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        webview.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webview.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        webview.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        webview.getSettings().setAppCacheEnabled(true);//是否使用缓存
        webview.getSettings().setDomStorageEnabled(true);//DOM Storage
// webview.getSettings().setUserAgentString("User-Agent:Android");//设置用户代理，一般不用

        //直接返回APP启动页
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoadingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        UmLog.e(TAG, body);

        //APP离线时收到消息的处理流程
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
        UmengResponse umengResponse = gson.fromJson(body, new TypeToken<UmengResponse>() {}.getType());
        String url = umengResponse.getExtra().get(WEB_VIEW_URL);

        Message message = Message.obtain();
        message.obj = url;

        handler.sendMessage(message);

    }

    @Override
    protected void onResume() { //APP在线时收到消息的处理流程
        super.onResume();

        Bundle bun = getIntent().getExtras();
        if (bun != null) {

            if (null != bun) {
                for (String key : bun.keySet()) {

                    if (WEB_VIEW_URL.equals(key)){
                        final String url = bun.getString(key);

                        //APP离线时收到消息的处理流程
                        Message message = Message.obtain();
                        message.obj = url;

                        handler.sendMessage(message);
                        break;
                    }
                }
            }
        }
    }
}
