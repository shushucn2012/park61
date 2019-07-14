package com.park61.common.set;

import java.util.ArrayList;
import java.util.List;

import com.park61.moduel.acts.bean.CityBean;
import com.park61.moduel.login.bean.UserBean;
import com.park61.moduel.toyshare.bean.JoyShareItem;

import android.os.Environment;

/**
 * 静态全局参数，变量
 *
 * @author super
 */
public class GlobalParam {

    /**
     * 支持的城市列表
     */
    public static List<CityBean> cityList = new ArrayList<CityBean>();

    /**
     * 系统版本号
     */
    public static int versionCode = 0;
    /**
     * 系统版本名称
     */
    public static String versionName = "";
    /**
     * 新系统版本名称
     */
    public static String versionNameNext = "";
    /**
     * 系统版本名称
     */
    public static String macAddress;
    /**
     * userToken
     */
    public static String userToken = null;
    /**
     * 客户端当前纬度
     */
    public static double latitude = 0;
    /**
     * 客户端当前经度
     */
    public static double longitude = 0;
    /**
     * 客户端当前定位出的城市名称
     */
    public static String locationCityStr = "";
    /**
     * 客户端当前定位出的区域名称
     */
    public static String locationCountryStr = "";
    /**
     * 客户端当前用户选择的城市名称
     */
    public static String chooseCityStr = "";
    /**
     * 客户端当前用户选择的城市数据库编号
     */
    public static String chooseCityCode = "";
    /**
     * 当前用户信息
     */
    public static UserBean currentUser;
    /**
     * 请求头x_sign
     */
    public static String x_sign;
    /**
     * 请求头timestamp
     */
    public static String timestamp;
    /**
     * tencent appid
     */
    public static final String TENCENT_APP_ID = "1106198344";// "222222"1104835049;
    /**
     * weixin appid
     */
    public static final String WX_APP_ID = "wxc5a41c3a7a23c561";// 测试：wx9f6a8153fff62089;正式：wx753971e884df0fe0
    /**
     * weixin AppSecret
     */
    public static final String WX_APP_SECRET = "d23d5177516a0ff7a2fd599fd439d55e";

    /**
     * 秀萌照主页是否需要刷新
     */
    public static boolean GrowingMainNeedRefresh = true;

    /**
     * 商品首页是否需要刷新
     */
    public static boolean SalesMainNeedRefresh = true;

    /**
     * 我的选图页面是否需要刷新数据
     */
    public static boolean PhotosNeedRefresh = false;

    /**
     * 我的订单页面刷新数据
     */
    public static boolean MyOrderNeedRefresh = false;
    /**
     * 购物车页面刷新数据
     */
    public static boolean TradeCartNeedRefresh = false;
    /**
     * 图片下载本机路径
     */
    public static String IMAGE_FILE_PATH = Environment
            .getExternalStorageDirectory() + "/qjw/Qjw_Images/";

    /**
     * 商品订单详情页面刷新数据
     */
    public static boolean TradeOrderDetailNeedRefresh = false;
    /**
     * 订单列表页面刷新数据
     */
    public static boolean TradeOrderListNeedRefresh = false;

    /**
     * 登录类型
     */
    public static int LOGIN_TYPE = 0;// 061区用户登录；1联合登陆

    /**
     * 小课游戏类型码
     */
    public final static String SMALL_CLASS_CODE = "7";

    /**
     * 活动订单状态值
     */
    public static class ActOrderState {
        /**
         * 待付款
         */
        public static final String WAITFORPAY = "waitforpay";
        /**
         * 已报名
         */
        public static final String APPLIED = "applied";
        /**
         * 待评价
         */
        public static final String WAITFORCOMT = "waitforcomt";
        /**
         * 已参加
         */
        public static final String DONE = "done";
    }

    /**
     * 我的分红页面刷新数据
     */
    public static boolean ProfitActivityNeedRefresh = false;
    /**
     * 拼团页面刷新数据
     */
    public static boolean GroupPurchaseActivityNeedRefresh = false;
    /**
     * 售后列表页面刷新数据
     */
    public static boolean AfterSaleListNeedRefresh = false;
    /**
     * you
     */
    public static String YOUMENG_DEVICE_TOKEN = "";
    /**
     * 用于区分应用的标识，跟iOS保持一致
     */
    public static final String BUNDLE_ID = "com.61Park.61park";
    /**
     * 当前店铺id
     */
    public static long CUR_SHOP_ID = 0;
    /**
     * 当前区域id
     */
    public static long CUR_COUNTRY_ID = 0;
    /**
     * 当前孩子id
     */
    public static long CUR_CHILD_ID = 0;

    public static JoyShareItem CurJoy;

    public static long CUR_TOYSHARE_APPLY_ID = 0;

    public static int MSG_NUM = 0;

    public static String CUR_SHOP_PHONE;

    public static String CUR_SHOP_NAME;

    public static String CUR_SHOP_IMG;

    public static int CUR_COURSE_ID = 0;

    /**
     * 内容类型
     */
    public static class FirstHeadClassifyType {
        public static int TEXT_TYPE = 0;//图文类型
        public static int VIDEIO_TYPE = 1;//视频类型
        public static int SPECIAL_TYPE = 2;//专栏类型
        public static int AUDIO_TYPE = 3;//音频类型
    }

    /**
     * 内容列表图片展示方式
     */
    public static class FirstHeadComposeType {
        public static int ONE_SMALL_PIC = 1;//1 一小图；
        public static int ONE_BIG_PIC = 2;//2 一大图；
        public static int THREE_SMALL_PIC = 3;//3 三小图
    }
}
