package com.park61.moduel.grouppurchase;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.park61.BaseActivity;
import com.park61.R;

/**
 * 拼团详情页面
 */
public class FightGroupDescriptionActivity extends BaseActivity{
    private WebView wv_description;
    private ProgressBar pb;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_fightgroup_description);
    }

    @Override
    public void initView() {
        pb = (ProgressBar) findViewById(R.id.pb);
        wv_description = (WebView) findViewById(R.id.wv_description);
    }

    @Override
    public void initData() {
        String url = "http://m.61park.cn/html-sui/shop/groupLaw.html";
        wv_description.loadUrl(url);
        //加载进度
        wv_description.setWebChromeClient(new WebChromeClient() {
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
    public void initListener() {

    }
}
