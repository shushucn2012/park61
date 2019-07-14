package com.park61.common.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by chenlie on 2018/1/24.
 * <p>
 * 判断是否是3G/4G
 */

public class NetWorkUtil {

    //播放提示
    public static boolean isPlay = true;
    //下载提示
    public static boolean isDownload = true;

    public static boolean hasNetWork(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo hasNet = conn.getActiveNetworkInfo();
        if (hasNet != null) {
            return hasNet.isAvailable();
        } else {
            return false;
        }
    }


    /**
     * 如果返回true，弹窗后用户点了继续，设置isPlay为false
     */
    public static boolean isPlayTip(Context context) {
        //如果第一次3G/4G下允许了，以后就不弹
        boolean isNeedDialog = false;
        if (isPlay) {
            //判断是否是3G/4G
            ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile = conn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = conn.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifi == null || !wifi.isConnected()) {
                if (mobile != null && mobile.isConnected()) {
                    isNeedDialog = true;
                }
            }
        }
        return isNeedDialog;
    }

    /**
     * 播放和下载的提示要分开 都提示一次
     */
    public static boolean isDownloadTip(Context context) {
        //如果第一次3G/4G下允许了，以后就不弹
        boolean isNeedDialog = false;
        if (isDownload) {
            //判断是否是3G/4G
            ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile = conn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = conn.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifi == null || !wifi.isConnected()) {
                if (mobile != null && mobile.isConnected()) {
                    isNeedDialog = true;
                }
            }
        }
        return isNeedDialog;
    }

}
