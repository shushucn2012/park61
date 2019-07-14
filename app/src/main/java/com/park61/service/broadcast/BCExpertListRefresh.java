package com.park61.service.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * 专家关注之后返回刷新列表广播
 * Created by shubei on 2017/7/5.
 */
public class BCExpertListRefresh {

    private BroadcastReceiver broadcastReceiver;

    public BCExpertListRefresh(final OnReceiveDoneLsner lsner) {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                lsner.onGot(intent);
            }
        };
    }

    public interface OnReceiveDoneLsner {
        void onGot(Intent intent);
    }

    public void registerReceiver(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_REFRESH_EXPERTLIST");
        mContext.registerReceiver(broadcastReceiver, filter);
    }

    public void unregisterReceiver(Context mContext) {
        Intent intent = new Intent();
        intent.setAction("ACTION_REFRESH_EXPERTLIST");
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryBroadcastReceivers(intent, 0);
        if(resolveInfos != null && !resolveInfos.isEmpty()){
            mContext.unregisterReceiver(broadcastReceiver);
        }
    }

}
