package com.park61;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.park61.common.set.GlobalParam;
import com.park61.common.tool.ExitAppUtils;
import com.park61.moduel.childtest.TestHome;
import com.park61.moduel.firsthead.MainHomeActivity;
import com.park61.moduel.growing.GrowingActivity;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.moduel.me.MeActivity;
import com.park61.moduel.sales.SalesMainV2Activity;
import com.park61.moduel.shophome.ShopHomeMainV2Activity;
import com.umeng.analytics.MobclickAgent;

/**
 * 主界面
 * create by super
 */
@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity implements OnCheckedChangeListener {
    private Context mContext;
    private TabHost tabHost;
    private RadioButton radio_button_main;
    private RadioButton radio_button_home;
    private RadioButton radio_button_grow;
    private RadioButton radio_button_sales;
    private RadioButton radio_button_me;
    public static View grow_view_cover;

    private Intent mainIntent; // 首页
    private Intent homeIntent; // 首页
    private Intent growIntent;// 成长
    private Intent salesIntent;// 特卖
    private Intent meIntent;// 我的
    private long _firstTime = 0;

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
        setContentView(R.layout.home_tab_layout);
        // 判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        intView();
        setupIntent();

        //从h5打开app
        boolean isH5ClickIn = getIntent().getBooleanExtra("isH5ClickIn", false);
        if (isH5ClickIn) {
            Intent intent = new Intent(mContext, TestHome.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销-其他页面要改变当前tab页的广播
        mContext.unregisterReceiver(tabChangeReceiver);
    }

    private void intView() {
        mContext = this;
        // 注册-其他页面要改变当前tab页的广播
        registerTabChangeReceiver();
        radio_button_main = (RadioButton) findViewById(R.id.radio_button_main);
        radio_button_home = (RadioButton) findViewById(R.id.radio_button_home);
        radio_button_grow = (RadioButton) findViewById(R.id.radio_button_grow);
        radio_button_sales = (RadioButton) findViewById(R.id.radio_button_sales);
        radio_button_me = (RadioButton) findViewById(R.id.radio_button_me);
        mainIntent = new Intent(this, MainHomeActivity.class);
        homeIntent = new Intent(this, ShopHomeMainV2Activity.class);
        growIntent = new Intent(this, GrowingActivity.class);
        salesIntent = new Intent(this, SalesMainV2Activity.class);
        meIntent = new Intent(this, MeActivity.class);
        radio_button_main.setOnCheckedChangeListener(this);
        radio_button_home.setOnCheckedChangeListener(this);
        radio_button_grow.setOnCheckedChangeListener(this);
        radio_button_sales.setOnCheckedChangeListener(this);
        radio_button_me.setOnCheckedChangeListener(this);
        grow_view_cover = findViewById(R.id.grow_view_cover);
    }

    private void setupIntent() {
        this.tabHost = getTabHost();
        setupSettingTab("首页", "tab_main", mainIntent);
        setupSettingTab("乐园", "tab_home", homeIntent);
        setupSettingTab("日记", "tab_grow", growIntent);
        setupSettingTab("特卖", "tab_sales", salesIntent);
        setupSettingTab("我的", "tab_me", meIntent);
    }

    private void setupSettingTab(String str, String tag, Intent intent) {
        tabHost.addTab(tabHost.newTabSpec(tag).setIndicator(getView(str)).setContent(intent));
    }

    private View getView(String str) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item_layout, null);
        TextView tab_item_name = (TextView) view.findViewById(R.id.tab_item_name);
        tab_item_name.setText(str);
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.radio_button_main:
                    tabHost.setCurrentTabByTag("tab_main");
                    break;
                case R.id.radio_button_home:
                    tabHost.setCurrentTabByTag("tab_home");
                    break;
                case R.id.radio_button_grow:
                    if (GlobalParam.userToken == null) {
                        startActivity(new Intent(mContext, LoginV2Activity.class));
                        Intent changeIt = new Intent();
                        changeIt.setAction("ACTION_TAB_CHANGE");
                        changeIt.putExtra("TAB_NAME", "tab_main");
                        sendBroadcast(changeIt);
                    } else {
                        tabHost.setCurrentTabByTag("tab_grow");
                    }
                    break;
                case R.id.radio_button_sales:
                    tabHost.setCurrentTabByTag("tab_sales");
                    break;
                case R.id.radio_button_me:
                    if (GlobalParam.userToken == null) {
                        startActivity(new Intent(mContext, LoginV2Activity.class));
                        Intent changeIt = new Intent();
                        changeIt.setAction("ACTION_TAB_CHANGE");
                        changeIt.putExtra("TAB_NAME", "tab_main");
                        sendBroadcast(changeIt);
                    } else {
                        tabHost.setCurrentTabByTag("tab_me");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 其他页面要改变当前tab页的广播
     */
    private void registerTabChangeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_TAB_CHANGE");
        mContext.registerReceiver(tabChangeReceiver, filter);
    }

    private BroadcastReceiver tabChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收到广播，改变当前tab页
            String tab_name = intent.getStringExtra("TAB_NAME");
            if (tab_name != null && !tab_name.equals("")) {
                if ("tab_home".equals(tab_name)) {
                    radio_button_home.setChecked(true);
                } else if ("tab_grow".equals(tab_name)) {
                    radio_button_grow.setChecked(true);
                } else if ("tab_me".equals(tab_name)) {
                    radio_button_me.setChecked(true);
                }else if("tab_main".equals(tab_name)){
                    radio_button_main.setChecked(true);
                }
            }
        }
    };

    /**
     * 两次退出程序
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - _firstTime > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(mContext, "再按一次退出程序...", Toast.LENGTH_SHORT).show();
                _firstTime = secondTime;// 更新firstTime
                return true;
            } else {
                MobclickAgent.onKillProcess(mContext);
                ExitAppUtils.getInstance().exit();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

}
