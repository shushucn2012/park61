package com.park61.common.myokhttptest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.Log;
import com.park61.common.tool.ExitAppUtils;
import com.park61.common.tool.StringUtils;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.net.request.interfa.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyHttpUtils {

    private static MyHttpUtils mMyHttpUtils;
    private Activity mActivity;
    private String requestTag;//请求标签
    private OkHttpClient mOkHttpClient;

    private MyHttpUtils(Activity activity) {
        mActivity = activity;
    }

    public synchronized static MyHttpUtils getInstance(Activity activity) {
        if (mMyHttpUtils == null) {
            mMyHttpUtils = new MyHttpUtils(activity);
        }
        return mMyHttpUtils;
    }

    public void setActTag(String actTag){
        this.requestTag = actTag;
    }

    public void cancelAllRequest(String actTag){
        if (mOkHttpClient == null || actTag == null) return;
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (actTag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (actTag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    private Request.Builder addHeaders() {
        JSONObject clientInfoJot = new JSONObject();
        try {
            clientInfoJot.put("clientAppName", "android");
            clientInfoJot.put("clientAppVersion", GlobalParam.versionName);
            clientInfoJot.put("clientSystem", "android");
            clientInfoJot.put("clientVersion", android.os.Build.VERSION.RELEASE);
            clientInfoJot.put("deviceCode", GlobalParam.macAddress);
            if (GlobalParam.longitude == 0 || GlobalParam.longitude == 4.9e-324) {
                clientInfoJot.put("longitude", "");
            } else {
                clientInfoJot.put("longitude", GlobalParam.longitude);
            }
            if (GlobalParam.latitude == 0 || GlobalParam.latitude == 4.9e-324) {
                clientInfoJot.put("latitude", "");
            } else {
                clientInfoJot.put("latitude", GlobalParam.latitude);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request.Builder builder = new Request.Builder()
                //addHeader，可添加多个请求头  header，唯一，会覆盖
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .addHeader("cityId", StringUtils.noNullStr(GlobalParam.chooseCityCode))
                .addHeader("userToken", StringUtils.noNullStr(GlobalParam.userToken))
                .addHeader("timestamp", StringUtils.noNullStr(GlobalParam.timestamp))
                .addHeader("signCode", StringUtils.noNullStr(GlobalParam.x_sign))
                .addHeader("clientInfo", StringUtils.noNullStr(clientInfoJot.toString()))
                .addHeader("bundleId", StringUtils.noNullStr(GlobalParam.BUNDLE_ID));
        LogHeader();
        return builder;
    }

    private void LogHeader() {
        JSONObject clientInfoJot = new JSONObject();
        try {
            clientInfoJot.put("clientAppName", "android");
            clientInfoJot.put("clientAppVersion", GlobalParam.versionName);
            clientInfoJot.put("clientSystem", "android");
            clientInfoJot.put("clientVersion", android.os.Build.VERSION.RELEASE);
            clientInfoJot.put("deviceCode", GlobalParam.macAddress);
            if (GlobalParam.longitude == 0 || GlobalParam.longitude == 4.9e-324) {
                clientInfoJot.put("longitude", "");
            } else {
                clientInfoJot.put("longitude", GlobalParam.longitude);
            }
            if (GlobalParam.latitude == 0 || GlobalParam.latitude == 4.9e-324) {
                clientInfoJot.put("latitude", "");
            } else {
                clientInfoJot.put("latitude", GlobalParam.latitude);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        header.put("cityId", GlobalParam.chooseCityCode);
        header.put("userToken", GlobalParam.userToken);
        header.put("timestamp", GlobalParam.timestamp);
        header.put("signCode", GlobalParam.x_sign);
        header.put("clientInfo", clientInfoJot.toString());
        header.put("bundleId", GlobalParam.BUNDLE_ID);
        Log.e("httpheader", "------httpheader======" + header);
    }

    public void startRequest(String url, int requestMethod, String requstData, final int requestId, final BaseRequestListener listener) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8");//"类型,字节码"
        RequestBody requestBody = RequestBody.create(mediaType, requstData);
        Request request = addHeaders().url(url).post(requestBody).tag(requestTag).build();

        File sdcache = mActivity.getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        builder.dns(new MyDns());
        mOkHttpClient = builder.build();
        Log.out("=======================request_url========================" + url);
        Call call = mOkHttpClient.newCall(request);
        Log.out("========================request=====================start=======================");
        listener.onStart(requestId);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                GlobalParam.x_sign = null;
                Log.e("onErrorResponse", "error-toString===" + e.toString());
                Log.e("onErrorResponse", "error-getMessage===" + e.getMessage());
                Log.e("onErrorResponse", "error-getCause===" + e.getCause());
                Log.out("========================request=====================failed=======================");
                e.printStackTrace();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onError(requestId, null, mActivity.getString(R.string.net_request_fail));
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doResponse(mActivity, url, responseStr, requestId, listener);
                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onError(requestId, null, mActivity.getString(R.string.net_request_fail));
                        }
                    }
                });
            }
        });
    }

    private static void doResponse(Activity activity, String url, String responseStr, final int requestId, final BaseRequestListener listener) throws IOException {
        //Toast.makeText(activity, "123123", Toast.LENGTH_SHORT).show();
        Log.out("========================response.body()=====================" + responseStr);
        GlobalParam.x_sign = null;
        if (listener instanceof JsonRequestListener) {
            JSONObject jsonResponse;
            try {
                jsonResponse = new JSONObject(responseStr);
                String code = jsonResponse.optString("code");
                if (code.equals("0") || code.equals("00000100010")
                        || code.equals("0000010011")
                        || code.equals("0000010004")
                        || code.equals("0000010005")
                        || code.equals("0000010008")
                        || code.equals("0000027002")
                        || code.equals("0000031004")) {
                    if (jsonResponse.optJSONObject("data") != null) {
                        ((JsonRequestListener) listener).onSuccess(requestId, url, jsonResponse.optJSONObject("data"));
                    } else {
                        JSONArray arr = jsonResponse.optJSONArray("data");
                        JSONObject job = new JSONObject();
                        job.put("list", arr);
                        if (arr == null) {
                            job.put("data", jsonResponse.optString("data"));
                        }
                        ((JsonRequestListener) listener).onSuccess(requestId, url, job);
                    }
                } else {
                    String msg = jsonResponse.optString("msg");
                    if (msg == null || msg.equals("")) {
                        msg = jsonResponse.optString("ext");
                    }
                    final String innerMsg = msg;
                    if (code.equals("0000000002")) {// token失效
                        goToLogin(activity);
                        if (GlobalParam.userToken == null) {
                            listener.onError(requestId, code + "", "未登录，请登录！");
                        } else {
                            listener.onError(requestId, code + "", msg);
                        }
                    } else if (code.equals("0000025025")) {
                        String dataStr = jsonResponse.optString("data");
                        listener.onError(requestId, code + "", dataStr);
                    } else {
                        listener.onError(requestId, code + "", msg);
                    }
                }
            } catch (JSONException e) {
                listener.onError(requestId, null, activity.getString(R.string.net_request_error));
            }
        } else if (listener instanceof StringRequestListener) {
            ((StringRequestListener) listener).onSuccess(requestId, url, responseStr);
        }
    }

    public static void goToLogin(Context mContext) {
        GlobalParam.userToken = null;
        if (ExitAppUtils.getInstance().getLastActivity() instanceof GoodsDetailsActivity) {
            return;
        }
        mContext.startActivity(new Intent(mContext, LoginV2Activity.class));
        Intent changeIt = new Intent();
        changeIt.setAction("ACTION_TAB_CHANGE");
        changeIt.putExtra("TAB_NAME", "tab_main");
        mContext.sendBroadcast(changeIt);
    }


}
