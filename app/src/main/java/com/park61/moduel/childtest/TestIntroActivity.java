package com.park61.moduel.childtest;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.widget.pw.SharePopWin;

/**
 * Created by nieyu on 2017/12/8.
 */

public class TestIntroActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_share, tv_bottom;
    private ImageView iv_back;
    private WebView wvContent;
    private ProgressBar pb;
    private WebSettings websettings;
    private String url = "http://m.61park.cn/#/dap/dapintroduce";

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_testintro);
    }

    @Override
    public void initView() {
        tv_bottom = (TextView) findViewById(R.id.tv_bottom);
        tv_bottom.setOnClickListener(this);
        tv_share = (TextView) findViewById(R.id.tv_share);
        tv_share.setOnClickListener(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        pb = (ProgressBar) findViewById(R.id.pb);
        wvContent = (WebView) findViewById(R.id.wvContent);
        websettings = wvContent.getSettings();
        ViewInitTool.initWebViewSetting(websettings);
        ViewInitTool.syncWebViewCookie(mContext, url);
        wvContent.loadUrl(url);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        iv_back.setOnClickListener(this);
        //加载进度
        wvContent.setWebChromeClient(new WebChromeClient() {
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
    }

    @Override
    public void onClick(View view) {
        int viewid = view.getId();
        if (R.id.tv_share == viewid) {
            toShare();
        } else if (R.id.iv_back == viewid) {
            finish();
        } else if (R.id.tv_bottom == viewid) {
            if (GlobalParam.userToken != null) {
                finish();
            } else {
                Intent intent = new Intent(TestIntroActivity.this, LoginV2Activity.class);
                startActivity(intent);
            }
        }
    }

    public void toShare() {
        Intent its = new Intent(mContext, SharePopWin.class);
        String shareUrl = url;
        String title = "DAP测评让您秒懂孩子";
        String description = "来自哈佛大学最专业的儿童DAP测评，测评10分钟了解您的孩子";
        its.putExtra("shareUrl", shareUrl);
        its.putExtra("title", title);
        its.putExtra("description", description);
        mContext.startActivity(its);
    }
}
