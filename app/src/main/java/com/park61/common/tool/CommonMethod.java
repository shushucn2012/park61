package com.park61.common.tool;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.park61.CanBackWebViewActivity;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.Log;
import com.park61.moduel.acts.ActDetailsActivity;
import com.park61.moduel.acts.bean.CityBean;
import com.park61.moduel.firsthead.ClassChooseBabyActivity;
import com.park61.moduel.grouppurchase.FightGroupDetailsActivity;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.moduel.login.bean.UserManager;
//import com.wjl.smartpos.modules.coupon.bean.CouponBean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 公共方法
 *
 * @author super
 */
public class CommonMethod {

    /**
     * 加载Assert文本文件，转换成String类型
     *
     * @param context
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String loadAssetsText(Context context, String fileName)
            throws IOException {
        InputStream inputStream = context.getAssets().open(fileName,
                Context.MODE_PRIVATE);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        int len = 0;
        while ((len = inputStream.read(bytes)) > 0) {
            byteStream.write(bytes, 0, len);
        }

        return new String(byteStream.toByteArray(), "UTF-8");
    }

    private static boolean isExistDataCache(Context context, String cachefile) {
        boolean exist = false;
        File data = context.getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 保存对象
     *
     * @param ser
     * @param file
     * @throws IOException
     */
    public static boolean saveObject(Context context, Serializable ser,
                                     String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(file, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Serializable readObject(Context context, String file) {
        if (!isExistDataCache(context, file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = context.getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 判断list是否为空
     *
     * @param list
     * @return true 空 false 不为空
     */
    public static <T> boolean isListEmpty(List<T> list) {
        if (list == null || list.size() <= 0)
            return true;
        return false;
    }

    /**
     * 删除ArrayList中重复元素
     *
     * @param <T>
     * @param list
     */
    public static <T> void removeDuplicate(List<T> list) {
        HashSet<T> h = new HashSet<T>(list);
        list.clear();
        list.addAll(h);
    }

    // /**
    // * 删除优惠券列表中重复的优惠券
    // */
    // public static void removeDuplicateObj(List<CouponBean> list) {
    // for (int i = 0; i < list.size() - 1; i++) {
    // for (int j = list.size() - 1; j > i; j--) {
    // if (list.get(j).getCouponNo().equals(list.get(i).getCouponNo())) {
    // list.remove(j);
    // }
    // }
    // }
    // }

    /**
     * 时间比大小
     *
     * @param t1 开始时间
     * @param t2 结束时间
     * @return 0 相等;-1 t1在t2之前;1 t1在t2之后
     */
    public static int timeCompare(String t1, String t2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(formatter.parse(t1));
            c2.setTime(formatter.parse(t2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = c1.compareTo(c2);
        return result;
    }

    /**
     * 返回当前系统时间 格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    /***
     * 日期时间数字如果小于10则加0显示 列如 9 显示 09
     */
    public static String formatTimeNum(int timeNum) {
        if (timeNum >= 10)
            return String.valueOf(timeNum);
        else {
            return "0" + timeNum;
        }
    }

    /***
     * 日期时间数字如果小于10则加0显示 列如 9 显示 09
     */
    public static String formatTimeNum(String timeNum) {
        int timeNumInt = 0;
        try {
            timeNumInt = Integer.parseInt(timeNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (timeNumInt >= 10)
            return String.valueOf(timeNum);
        else
            return "0" + timeNum;
    }

    /**
     * 比较两个城市名称是否一致
     *
     * @param cityName1
     * @param cityName2
     * @return
     */
    public static boolean compareTwoCityName(String cityName1, String cityName2) {
        if (cityName1.equals(cityName2))
            return true;
        if (cityName1.contains(cityName2))
            return true;
        if (cityName2.contains(cityName1))
            return true;
        return false;
    }

    /**
     * 通过城市名称判断城市是否支持61区
     *
     * @param cityName
     * @return
     */
    public static boolean isCitySupportByName(String cityName) {
        for (CityBean c : GlobalParam.cityList) {
            if (CommonMethod.compareTwoCityName(c.getCityName(), cityName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将距离米转公里显示
     *
     * @param m 米数
     * @return 公里数
     */
    public static String formatM2Km(String m) {
        Double mDb = FU.paseDb(m);
        Double kmDb = mDb / 1000;
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        return df.format(kmDb);
    }

    /**
     * 打开新页面，并关闭之前的该页面实例
     *
     * @param context
     * @param c
     * @param it
     */
    public static void startOnlyNewActivity(Context context, Class c, Intent it) {
        for (Activity a : ExitAppUtils.getInstance().getActList()) {
            if (a.getClass().getSimpleName().equals(c.getSimpleName())) {
                a.finish();
            }
        }
        context.startActivity(it);
    }

    /**
     * 读取assets中的文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readAssetsFile(Context context, String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String info = "";
            while ((info = bufferedReader.readLine()) != null) {
                sb.append(info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 毫秒转化
     */
    public static String formatMs(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒

        return strHour + ":" + strMinute + ":" + strSecond;
    }

    public static String formatMss(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒

        return day + ":" + strHour + ":" + strMinute + ":" + strSecond;
    }

    /**
     * 验证用户是否登录，没有登录则跳去登录页面
     *
     * @param mContext
     */
    public static boolean checkUserLogin(Context mContext) {
        if (GlobalParam.userToken == null) {// 没有登录则跳去登录
            mContext.startActivity(new Intent(mContext, LoginV2Activity.class));
            return false;
        }
        return true;
    }

    /**
     * 判断网络类型
     *
     * @return
     */
    public static String getNetworkType(Context context) {
        String strNetworkType = "";
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }
                Log.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }
        Log.e("cocos2d-x", "Network Type : " + strNetworkType);
        return strNetworkType;
    }

    public static void dealWithScanBack(String str, Context mContext) {
        if (str.contains("activity")) {
            Map<String, String> mapRequest = CRequest.URLRequest(str);
            for (String strRequestKey : mapRequest.keySet()) {
                String strRequestValue = mapRequest.get(strRequestKey);
                if ("actid".equalsIgnoreCase(strRequestKey)) {
                    Intent it = new Intent(mContext, ActDetailsActivity.class);
                    it.putExtra("id", Long.parseLong(strRequestValue));
                    mContext.startActivity(it);
                    return;
                }
            }
        } else if (str.contains("mobiledownload")) {
            Map<String, String> mapRequest = CRequest.URLRequest(str);
            for (String strRequestKey : mapRequest.keySet()) {
                String strRequestValue = mapRequest.get(strRequestKey);
                if ("id".equals(strRequestKey)) {
                    Intent it = new Intent(mContext, ClassChooseBabyActivity.class);
                    it.putExtra("classId", Long.parseLong(strRequestValue));
                    mContext.startActivity(it);
                    return;
                }
            }
        } else if (str.contains("groupDetail")) {
            Map<String, String> mapRequest = CRequest.URLRequest(str);
            for (String strRequestKey : mapRequest.keySet()) {
                String strRequestValue = mapRequest.get(strRequestKey);
                if ("opneId".equalsIgnoreCase(strRequestKey)) {
                    Intent it = new Intent(mContext, FightGroupDetailsActivity.class);
                    it.putExtra("opneId", Long.parseLong(strRequestValue));
                    mContext.startActivity(it);
                    return;
                }
            }
        } else if (str.contains("http:") || str.contains("https:")) {
              /*  String url = str; // web address
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);*/
            Log.out("==========================return----------------url==========================" + str);
            Intent intent = new Intent(mContext, CanBackWebViewActivity.class);
            intent.putExtra("url", str);
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext, "无效的数据！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Hides the soft input if it is active for the input text, by  reflection mechanism
     */
    public static void hideSoftKeyboard(EditText edittext) {
        Object o = null;
        try {
            Class<?> threadClazz = Class.forName("android.view.inputmethod.InputMethodManager");

            Method method = threadClazz.getDeclaredMethod("peekInstance");//return sInstance
            Method[] methods = threadClazz.getDeclaredMethods();

            method.setAccessible(true);
            o = method.invoke(null);
            if (o == null) {
                return;
            }
            InputMethodManager inputMethodManager = (InputMethodManager) o;
            if (inputMethodManager != null && inputMethodManager.isActive(edittext)) {
                inputMethodManager.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
            }
        } catch (Exception e) {
        }
    }

    public static String escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j)
                    || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16)
                    tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src
                            .substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src
                            .substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }

    /**
     * 初始化登录数据
     */
    public static void initLoginData(Context mContext){
        GlobalParam.GrowingMainNeedRefresh = true;
        mContext.sendBroadcast(new Intent().setAction("ACTION_REFRESH_EXPERTLIST"));
        GlobalParam.CUR_SHOP_ID = 0;
        GlobalParam.CUR_SHOP_NAME = "";
        GlobalParam.CUR_CHILD_ID = 0;
    }

    /**
     * 保存用户名，避免登录再输
     */
    public static void saveCurrentUserName(Context mContext) {
        SharedPreferences spf = mContext.getSharedPreferences(LoginV2Activity.FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("username", GlobalParam.currentUser.getMobile());
        editor.putString("logindate", DateTool.getCurrentDate());
        editor.putString("usertoken", GlobalParam.userToken);
        editor.commit();
        UserManager.getInstance().setAccountInfo(GlobalParam.currentUser, mContext);
    }
}
