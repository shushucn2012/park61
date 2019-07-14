package com.park61;

import android.app.Service;
import android.content.Context;
import android.os.Environment;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.aliyun.vodplayer.downloader.AliyunDownloadConfig;
import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.park61.common.set.AppInit;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.Log;
import com.park61.moduel.okdownload.db.DownloadDAO;
import com.park61.service.LocationService;
import com.park61.service.WriteLog;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

/**
 * 应用程序application
 *
 * @author super
 */
public class GHPApplication extends MultiDexApplication {
    public LocationService locationService;
    public Vibrator mVibrator;
    public static boolean isShowNetDialog = true;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DownloadDAO.init(this);
        AppInit.initEnvironment(this, true, true);
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        WriteLog.getInstance().init(); // 初始化日志
        SDKInitializer.initialize(getApplicationContext());


        AliyunDownloadConfig config = new AliyunDownloadConfig();
        config.setMaxNums(3);
        config.setDownloadDir(getExternalCacheDir() + "/download/");
        config.setSecretImagePath(Environment.getExternalStorageDirectory()+"/test_save/");
        AliyunDownloadManager.getInstance(this).setDownloadConfig(config);

        // 创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);

        // 友盟推送
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //mPushAgent.setDebugMode(false);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                GlobalParam.YOUMENG_DEVICE_TOKEN = deviceToken;
                Log.out("deviceToken===================" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.out("onFailure===================" + s);
            }
        });

        MiPushRegistar.register(this, "2882303761517584138", "5961758436138");

        HuaWeiRegister.register(this);
    }
}
