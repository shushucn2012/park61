package com.park61.common.set;

/**
 * APP通信地址
 *
 * @author super
 */
public class AppUrl {
    /**
     * app通信访问主地址
     */
    public static String host = "";

    /**
     * app通信访问测试地址
     */
//    public static final String demoHost = "http://10.10.10.14:8180/service";//old测试服务
    public static final String demoHost = "https://api.61park.cn/central/service";//old测试服务

//    public static final String NEWHOST_HEAD = "http://10.10.10.14:8380";//new测试服务
    public static final String NEWHOST_HEAD = "https://api.61park.cn/park";//new测试服务

    public static final String demoHost_head = "http://m.61park.cn";

    public static final String SHARE_APP_ICON = "http://r.photo.store.qq.com/psb?/4b8a92ff-3677-40b5-80ba-465cc9ff57bc/SxcFnVLiMrSO2odwZdESlUdY5bkm*PpkULF5D5PKW7c!/r/dG0BAAAAAAAA";

    /**
     * 身高和体重曲线domain
     */
    public static final String BUILD_CHART_DOMAIN = ".m.61park.cn";
    /**
     * 身高和体重曲线
     */
    public static final String BUILD_CHART = "http://m.61park.cn/getGrowChar.do?childId=%d&growthCat=%d";
    /**
     * 合同页面
     */
    public static final String CONTRACT_DETAIL = "http://m.61park.cn/contract/toContractDetail.do?contractId=%d&contractType=%d";
    /**
     * app通信访问正式地址
     */
    public static final String releaseHost = "http://m.61park.cn/service";
    /**
     * 总邀请url
     */
    public static final String APP_INVITE_URL = "http://m.61park.cn";

    /**
     * pc页邀请url
     */
    public static final String APP_3W_URL = "http://m.61park.cn";

    /**
     * 游戏分享url
     */
    public static final String ACT_SHARE_URL = "http://m.61park.cn/activity/activity.do?actId=%s";

    /**
     * 课程分享url
     */
    public static final String COURSE_SHARE_URL = "http://m.61park.cn/html-sui/hotcourse/course-detail.html?id=%s";

    /**
     * 成长分享url
     */
    public static final String GROW_SHARE_URL = "http://m.61park.cn/toGrowShare.do?growId=%s&type=%s";

    /**
     * 邀请url
     */
    public static final String INVITE_URL = "http://m.61park.cn/toInvitationConfirm.do?inviteCode=%s&childId=%s&relationCode=%s";

    /**
     * 请求推荐游戏url后缀
     */
    public static final String GET_RECOMMEND_ACTLIST_END = "/activity/getRecommendActList";

    /**
     * 请求附近游戏url后缀
     */
    public static final String GET_ACCESSORY_ACTLIST_END = "/activity/getAccessoryActList";

    /**
     * 请求所有游戏url后缀
     */
    public static final String GET_ALL_ACTLIST_END = "/activity/getAllActList";

    /**
     * 请求用户信息url后缀
     */
    public static final String GET_USER_INFO_END = "/user/getUser";

    /**
     * 请求更新用户信息url后缀
     */
    public static final String CHELD_LIST = "/service/child/childList";


    public static final String UPDATE_USER_INFO_END = "/user/updateUser";


    public static final String TEST_RECORD = "/service/ea/getRecordList";

    /**
     * 请求获取用户孩子列表url后缀
     */
    public static final String GET_USER_CHILDS_END = "/user/getChildByUser";

    /**
     * 请求添加孩子url后缀
     */
    public static final String ADD_CHILD_END = "/child/addChild";

    /**
     * 请求删除孩子url后缀
     */
    public static final String DEL_CHILD_END = "/child/deleteChild";

    /**
     * 请求获取用户孩子关系url后缀
     */
    public static final String GET_RELATION_END = "/child/getUserChildRelation";

    /**
     * 请求获取用户孩子关系列表url后缀
     */
    public static final String GET_RELATION_LIST_END = "/user/getRelationConstant";

    /**
     * 请求更新孩子信息url后缀
     */
    public static final String UPDATE_CHILD_INFO_END = "/child/updateChild";

    /**
     * 确认报名url后缀
     */
    public static final String CONFIRM_APPLY_END = "/activityApply/insertActApplyV2";

    /**
     * 请求阿里云oss加密后的url
     */
    public static final String GET_ALIYUN_SEC_URL = "/oss/upload";

    /**
     * 获取待付款游戏列表url后缀
     */
    public static final String GET_TOPAY_ACTLIST = "/activityApply/getToPaymentActivityList";

    /**
     * 关注游戏url后缀
     */
    public static final String FOCUS_ACT_END = "/focus/addActivityFocus";


    public static final String TestRecommend = "/service/ea/findEaServiceList";

    public static final String TEAND_ORDER = "/service/ea/getEaOrderInfoV2";

    public static final String TRAND_SUBMIT = "/service/ea/submit";


    public static final String TEST_RESULT = "/service/eaUserResult/showEaUserResult";

    public static final String RECOMMEND = "/service/content/getRecommendContentList";
    /**
     * 取消关注游戏url后缀
     */
    public static final String CANCEL_FOCUS_ACT_END = "/focus/cancelActivityFocus";

    /**
     * 获取关注游戏的列表url后缀
     */
    public static final String GET_FOCUS_ACTS_END = "/focus/getFocusListByUser";

    /**
     * 获取游戏详情url后缀
     */
    public static final String GET_ACT_DETAILS_END = "/activity/getActivityDetailById";

    /**
     * 确认支付url后缀
     */
    public static final String PAY_CONFIRM_END = "/activityApply/confirmPayment";

    /**
     * 获取已报名游戏url后缀
     */
    public static final String GET_APPLY_ACTS_END = "/activityApply/getAppliedActivityList";

    /**
     * 取消已报名游戏url后缀
     */
    public static final String CANCEL_APPLY_ACTS_END = "/activityApply/cancelActivityApply";

    /**
     * 获取待评价游戏url后缀
     */
    public static final String GET_TO_EVALUATE_ACTS_END = "/activityEvaluate/getToEvaluateActivityList";

    /**
     * 提交评价url后缀
     */
    public static final String EVALUATE_ACTS_END = "/activityEvaluate/evaluateActivity";

    /**
     * 查询所有我参与的游戏url后缀
     */
    public static final String GET_DONE_ACTS_END = "/activity/getActListByUserId";

    /**
     * 查询所有游戏评论url后缀
     */
    public static final String GET_ACT_COMMENTS_END = "/activityComment/getAllActCommentAndEvaluateListByActId";

    /**
     * 发布游戏评论url后缀
     */
    public static final String COMMIT_ACT_COMMENTS_END = "/activityComment/insertActComment";

    /**
     * 获取成长萌照列表url后缀
     */
    public static final String GET_GROWING_SHOW_LIST = "/growShowPhotos/getGrowingShowPhotosList";

    /**
     * 添加成长萌照url后缀
     */
    public static final String ADD_SHOW_PHOTOS = "/growShowPhotos/addShowPhotos";


    public static final String SENDCOMMEN = "/home/addComment";

    /**
     * 获取宝贝能力数据url后缀
     */
    public static final String GET_BB_CAPACITY = "/capacityChild/getCapacityChildAll";

    /**
     * 获取宝贝体格列表数据数据url后缀
     */
    public static final String GET_GROWING_RECS = "/growingrec/getGrowingRecList";

    /**
     * 添加体格数据url后缀
     */
    public static final String ADD_GROWING_REC = "/growingrec/addGrowingRec";

    /**
     * 修改体格数据url后缀
     */
    public static final String UPDATE_GROWING_REC = "/growingrec/updateGrowingRec";

    /**
     * 获取体格数据正常范围url后缀
     */
    public static final String GET_GROWING_DATA_RG = "/growingrec/getGrowingDataRg";

    /**
     * 获取体格图表数据url后缀
     */
    public static final String GET_GROWING_POINTS = "/growingrec/getGrowingPointList";

    /**
     * 登录url后缀
     */
    public static final String LOGIN = "/login/login";

    /**
     * 联合登录验证手机号是否绑定url后缀
     */
    public static final String UNION_LOGIN_CHECK_MOBILE = "/login/unoinLoginCheckMobile";

    /**
     * 退出登录url后缀
     */
    public static final String LOGIN_OUT = "/login/logout";

    /**
     * 发送注册验证码url后缀
     */
    public static final String SEND_REGISTER_VCODE = "/login/sendRegisterVerifyCode";

    /**
     * 发送重置密码验证码url后缀
     */
    public static final String SEND_RESET_VCODE = "/login/sendResetVerifyCode";

    /**
     * 注册url后缀
     */
    public static final String REGISTER = "/login/register";

    /**
     * 重置密码url后缀
     */
    public static final String RESET = "/login/reset";

    /**
     * 绑定url后缀
     */
    public static final String BIND = "/login/binding";

    /**
     * 联合登陆
     */
    public static final String UNION_LOGIN = "/login/unoinLogin";

    /**
     * 添加成长评论url后缀
     */
    public static final String ADD_GROW_COMMENT = "/growComment/insertGrowComment";

    /**
     * 获取用户消息列表
     */
    public static final String GET_MSG_LIST = "/message/getMessageByUser";

    /**
     * 删除用户萌照记录
     */
    public static final String DELETE_SHOW_PHOTOS = "/growShowPhotos/delShowPhotos";

    /**
     * 删除体测记录
     */
    public static final String DELETE_GROWING_REC = "/growingrec/deleteGrowingRec";

    /**
     * 检测更新
     */
    public static final String CHECK_VERSION_END = "/app/checkVersion";

    /**
     * 获取订单详情
     */
    public static final String GET_ORDER_DETAILS = "/activityApply/getActApplyById";

    /**
     * 获取活动订单详情
     */
    public static final String GET_ACT_ORDER_DETAILS = "/activityApply/getCurrActApply";

    /**
     * 发送绑定手机号验证码
     */
    public static final String SEND_BIND_VCODE = "/login/sendBindingVerifyCode";

    /**
     * 请求banner数据
     */
    public static final String GET_BANNER_DATA = "/carousel/getCarouselShowList";

    /**
     * 获取店铺详情url后缀
     */
    public static final String GET_SHOP_DETAILS_END = "/merchant/getMerchantDetailById";

    /**
     * 获取店铺的今天游戏
     */
    public static final String GET_SHOP_ACTS_TODAY_END = "/merchant/getMerchantActivityListToday";

    /**
     * 获取店铺的明天游戏
     */
    public static final String GET_SHOP_ACTS_TOMORROW_END = "/merchant/getMerchantActivityListTomorrow";

    /**
     * 获取店铺的周末游戏
     */
    public static final String GET_SHOP_ACTS_WEEKEND_END = "/merchant/getMerchantActivityListWeekend";

    /**
     * 获取店铺的本周游戏
     */
    public static final String GET_SHOP_ACTS_THEWEEK_END = "/merchant/getMerchantActivityListCurrentWeek";

    /**
     * 根据分类、时间段查询店铺游戏列表
     */
    public static final String GET_SHOP_ACTS_END = "/merchant/getMerchantActivityList";

    /**
     * 搜索店铺列表
     */
    public static final String SEARCH_SHOP = "/merchant/searchMerchant";

    /**
     * 搜索游戏列表
     */
    public static final String SEARCH_ACT = "/activity/searchActivity";

    /**
     * 查询所有充值类型列表
     */
    public static final String GET_ALL_RECHARGE_CARD = "/recharge/getAllRechargeCardList";

    /**
     * 获取可用店铺列表
     */
    public static final String GET_SHOPS_BY_NAME = "/recharge/getLatelyMerchant";

    /**
     * 开通会员
     */
    public static final String START_MEMBER = "/recharge/doMemberRecharge";

    /**
     * 获取余额
     */
    public static final String GET_BALANCE = "/finRechargeRecord/getMyBalance";

    /**
     * 获取消费记录
     */
    public static final String GET_BILL_RECORDS = "/finRechargeRecord/getRechargeRecords";

    /**
     * 请求收藏店铺列表
     */
    public static final String GET_SHOP_COLLECTED = "/merchant/getMerchantFocusList";

    /**
     * 请求收藏店铺
     */
    public static final String FOCUS_SHOP = "/merchant/focusMerchant";

    /**
     * 请求取消收藏店铺
     */
    public static final String UNFOCUS_SHOP = "/merchant/cancelFocusMerchant";

    /**
     * 请求获取支持的城市列表url后缀
     */
    public static final String GET_SUPPORT_CITYS = "/city/getCitysBySupport";

    /**
     * 获取商品详情头部信息
     */
    public static final String GET_GOODS_TOPINFO_NEW_END = "/product/getProductDetailTopNewByPmInfoId";
    /**
     * 获取商品详情头部信息
     */
    public static final String GET_GOODS_TOPINFO_END = "/product/getProductDetailTopByPmInfoId";

    /**
     * 获取商品详情文描
     */
    public static final String GET_GOODS_DETAILS_END = "/product/getProductDetailDescriptionByPmInfoId";

    /**
     * 获取用户所有的地址列表(不区分城市-收货地址管理)
     */
    public static final String GET_ADDR = "/address/getAddressByUserId";

    /**
     * 获取商品评分
     */
    public static final String GET_GOODS_POINTS = "/product/getMainProductEvaluateByPmInfoId";

    /**
     * 查询所有商品评价
     */
    public static final String GET_GOODS_COMMENTS_END = "/product/getProductEvaluateListByAllSubProId";

    /**
     * 获取商品结算详情
     */
    public static final String GET_GOODS_CONFIRM = "/tradeOrder/confirm";

    /**
     * 提交订单
     */
    public static final String SUBMIT_ORDER = "/tradeOrder/submit";

    /**
     * 获取支付信息
     */
    //public static final String GET_PAY_INFO = "/tradeOrder/paymentInfo";

    /**
     * 商品发送余额支付验证码
     */
    public static final String SEND_PAYMENT_VERIFYCODE = "/tradeOrder/sendPaymentVerifyCode";

    /**
     * 会员充值发送余额支付验证码
     */
    public static final String SEND_PAYMENT_VERIFYCODE_MEMBER = "/pay/sendPaymentVerifyCode";

    /**
     * 商品余额支付
     */
    public static final String BALANCE_PAYMENT = "/tradeOrder/balancePayment";

    /**
     * 会员充值余额支付
     */
    public static final String BALANCE_PAYMENT_MEMBER = "/pay/doBalanceByRechargeCard";
    /**
     * 新增收货地址
     */
    public static final String ADD_ADDR = "/address/addAddressByUserId";
    /**
     * 获取详细地址
     */
    public static final String GET_DETAIL_ADDR = "/address/getAddressById";
    /**
     * 更新地址
     */
    public static final String UPDATE_ADDR = "/address/updateAddressByUserId";
    /**
     * 删除地址
     */
    public static final String DEL_ADDR = "/address/delAddressByUserId";
    /**
     * 更新默认收货地址
     */
    public static final String UPDATE_DEFAULT_ADDR = "/address/updateDefaultAddressId";
    /**
     * 通过用户id和城市获取可用地址--下单流程
     */
    public static final String GET_ADDR_BY_USERID = "/address/getAddressByUserIdAndCityId";
    /**
     * 通过城市id获取区域列表
     */
    public static final String GET_ADDR_BY_CITYID = "/address/getCountysByCityId";
    /**
     * 通过区域id获取自提点列表
     */
    public static final String GET_STORE_BY_COUNTYID = "/address/getStoresByCountyId";

    /**
     * 获取省份列表
     */
    public static final String GET_PROVINCE_LIST = "/city/getProvinceList";
    /**
     * 获取城市列表 参数pid：上级省份id
     */
    public static final String GET_CITY_BY_PROVINCE = "/city/getCitysByProvince";
    /**
     * 退货申请
     */
    public static final String RETURN_APPLY_FOR = "/grf/apply";
    /**
     * 退货查询
     */
    public static final String RETURN_QUERY = "/grf/queryV2";
    /**
     * 退货更新
     */
    public static final String RETURN_UPDATE = "/grf/updateV2";
    /**
     * 分页查询售后订单
     */
    public static final String QUERY_GRF_LIST = "/grf/myGrfListV2";
    /**
     * 获取根类目
     */
    public static final String GET_TOP_NODE = "/productCategoryAdapter/getTopNode";

    /**
     * 获取次级类目
     */
    public static final String GET_CHILD_NODE = "/productCategoryAdapter/getChildNode";

    /**
     * 顶层导航获取种类和列表
     */
    public static final String GET_GOODS_CLASSIFY = "/productCategoryAdapter/goodsClassify";

    /**
     * 获取商品列表
     */
    public static final String GET_TE_BUYLIST = "/productCategoryAdapter/teBuyList";

    /**
     * 获取搜索商品
     */
    public static final String SEARCH_GOODS = "/productCategoryAdapter/getProductList";

    /**
     * 获取订单列表
     */
    public static final String ORDER_LIST = "/myOrder/orderListV2";

    /**
     * 获取订单详情
     */
    public static final String ORDER_DETAIL = "/myOrder/orderDetail";

    /**
     * 订单确认收货
     */
    public static final String ORDER_CONFRIM_RECEIPT = "/myOrder/confirmReceiptGoods";

    /**
     * 取消订单
     */
    public static final String ORDER_CANCEL = "/myOrder/cancelOrder";

    /**
     * 商品评价
     */
    public static final String PRODUCT_EVALUATE = "/product/insertProductEvaluate";

    /**
     * 获取特卖首页种类
     */
    public static final String GET_TEMAI_TYPE = "/productCategoryAdapter/getPromotionType";

    /**
     * 获取特卖首页商品
     */
    public static final String GET_TEMAI_GOODS = "/productCategoryAdapter/promotion";

    /**
     * 获取所有会员卡类型
     */
    public static final String GET_ALL_MEMBERCARD_LIST = "/recharge/getAllMemberCardList";

    /**
     * 通过城市id获取区域列表
     */
    public static final String GET_DISTRICTS_BY_CITYID = "/recharge/getDistrictShopCountByCityId";

    /**
     * 通过区域获取店铺列表
     */
    public static final String GET_DISTRICTSHOPS_BY_COUNTRYID = "/recharge/getDistrictShopListByCityId";

    /**
     * 获取游戏种类列表
     */
    public static final String GET_ACT_CLASSIFY_LIST = "/activity/merchantActivityClassifyList";

    /**
     * 获取游戏收费类型列表
     */
    public static final String GET_ACT_CHARGEMODE_LIST = "/activity/merchantChargeModeList";

    /**
     * 根据类型获取游戏列表
     */
    public static final String GET_ACT_LIST_BY_FILTER = "/activity/searchClassifyActivityMerchantList";

    /**
     * 将消息设为已读
     */
    public static final String SET_MSG_READ = "/message/setMsgRead";

    /**
     * 获取用户推荐码
     */
    public static final String GET_USER_INVITE_CODE = "/user/getUserInviteCode";
    /**
     * 获取最近的店铺信息
     */
    public static final String GET_NEAEST_STORE = "/address/getNearestStore";

    /**
     * 搜索商品
     */
    public static final String SEARCH_GOODS_BY_NAME = "/product/searchPminfoListByPmInfoName";
    /**
     * 修改密码
     */
    public static final String UPDATE_PASSWORD = "/user/updatePassword";
    /**
     * 查询优惠券列表数据
     */
    public static final String QUERY_COUPON_LIST = "/coupon/getMyCouponList";
    /**
     * 优惠券详情
     */
    public static final String COUPON_DETAIL = "/coupon/couponUseDetail";
    /**
     * 应用优惠券规则
     */
    public static final String COUPON_RULE = "/coupon/applyCouponRule";
    /**
     * 意见反馈接口
     */
    public static final String SUGGESTION_FEEDBACK = "/suggerst/addSuggerst";
    /**
     * 订单结算可用优惠券列表
     */
    public static final String ACCOUNT_CONPON_LIST = "/coupon/getCanUseOrderCouponList";
    /**
     * 商品订单结算优惠券列表
     */
    public static final String ORDER_COUNPON_LIST_V2 = "/coupon/getCanUseOrderCouponListV2";
    /**
     * 请求获取宝宝成长日程 getBabyCalendar
     */
    public static final String GET_BABY_CALENDAR = "/growCalendar/getBabyCalendar";
    /**
     * 游戏结算可用优惠券列表
     */
    public static final String GET_ACT_CONPON_LIST = "/coupon/getCanUseActivityCouponListV2";
    /**
     * 会员开通可用优惠券列表
     */
    public static final String GET_VIP_CONPON_LIST = "/coupon/getCanUseVipCouponList";
    /**
     * 会员开通可用优惠券列表
     */
    public static final String GET_VIP_CONPON_LIST_V2 = "/coupon/getCanUseVipCouponListV2";
    /**
     * 游戏优惠券核销
     */
    public static final String UPDATE_ACT_APPLYS = "/activityApply/updateActApplysV2";
    /**
     * 会员充值订单信息
     */
    public static final String GET_MEMBER_ORDER = "/recharge/getMemberOrder";
    /**
     * 进入会员卡订单
     */
    public static final String GET_MEMBER_ORDER_V2 = "/recharge/initCardOrder";
    /**
     * 选择会员优惠券后
     */
    public static final String GET_SAVE_COUPON_LIST = "/recharge/saveCoupouList";
    /**
     * 会员充值确认订单
     */
//	public static final String DO_CONFIRM_ORDER = "/recharge/doConfirmOrder";
    public static final String DO_CONFIRM_ORDER = "/recharge/doConfirmCardOrder";
    /**
     * 会员充值确认订单
     */
    public static final String DO_CONFIRM_ORDER_V2 = "/recharge/doConfirmCardOrderV2";
    /**
     * 计算游戏优惠券的总额
     */
    public static final String ACT_COUPON_COUNT_TOTAL = "/activityApply/saveCoupon";

    /**
     * 查询报名游戏详情
     */
    public static final String GET_ACT_APPLY_DETAIL = "/activityApply/getApplyByIdAndUserId";
    /**
     * 查询报名游戏详情
     */
    public static final String GET_ACT_ORDER_DETAIL = "/activityApply/getActivityApplyByOrderId";
    /**
     * 能力阶梯推荐游戏查询
     */
    public static final String GET_CAPACITY_CHILD_RECOMMAND = "/capacityChild/getRecommandActivity";
    /**
     * 查询当前店铺信息及游戏场次
     */
    public static final String GET_ACT_SESSION_BY_SHOP = "/activity/getActSessionAndMerchantInfo";
    /**
     * 查询购物车
     */
    public static final String GET_TRADE_CRAT = "/tradeCart/getSessionCart";
    /**
     * 添加商品到购物车
     */
    public static final String TRADE_CART_ADD_PRODUCT = "/tradeCart/addProduct";
    /**
     * 更新购物车商品的数量
     */
    public static final String UPDATE_PRODUCT_NUM = "/tradeCart/updateProductNum";
    /**
     * 删除购物车单个商品
     */
    public static final String DELETE_ONE_PRODUCT = "/tradeCart/deleteProduct";
    /**
     * 批量删除购物车商品
     */
    public static final String DELETE_PRODUCT_BATCH = "/tradeCart/deleteProductBatch";
    /**
     * 勾选购物车商品
     */
    public static final String CHECK_CART_ITEMS = "/tradeCart/checkCartItems";
    /**
     * 清空购物车
     */
    public static final String CLEAR_CART = "/tradeCart/clearCart";
    /**
     * 查询购物车商品数量
     */
    public static final String GET_CART_ITEMNUM = "/tradeCart/getCartItemNum";
    /**
     * 根据相册类型获取相册列表
     */
    public static final String GET_MERCHANT_PICTURE_LIST = "/merchant/getMerchantPictureList";
    /**
     * 获取首页游戏列表
     */
    public static final String GET_HOME_ACTTEMP = "/activity/getHomeActTempleteList";
    /**
     * 根据区域和游戏模板获取店铺列表
     */
    public static final String GET_STORES_BY_COUNTRY_AND_TMP = "/merchant/getStoresByCountyIdAndTempleteId";
    /**
     * 进入结算页面的初始信息
     */
    public static final String GET_GOODS_CONFIRMV2 = "/tradeOrder/confirmV2";
    /**
     * 保存收货地址到结算订单
     */
    public static final String TRADE_ORDER_SAVEADDR = "/tradeOrder/saveGoodReceiver";
    /**
     * 商品结算选择优惠券
     */
    public static final String TRADE_ORDER_SAVERE = "/tradeOrder/saveCoupon";
    /**
     *
     */
    public static final String TRADE_ORDER_SAVERE_V2 = "/tradeOrder/saveCouponV2";
    /**
     * 用户操作后，查看结算订单
     */
    public static final String TRADE_ORDER_GETORDER = "/tradeOrder/getTradeOrder";
    /**
     * 结算页面提交订单
     */
    public static final String TRADE_ORDER_SUBMITV2 = "/tradeOrder/submitV2";
    /**
     * 获取购物车勾选的商品数量
     */
    public static final String GET_CART_ITEM_NUM = "/tradeCart/getCartItemNum";

    /**
     * 查询订单详情数据，有拆单结构
     */
    public static final String ORDER_DETAIL_V2 = "/myOrder/orderDetailV2";
    /**
     * 获取游戏主题
     */
    public static final String SALES_MOTIF = "/activityMotif/salesMotif";
    /**
     * 获取促销banner
     */
    public static final String GET_PROMOTION_BANNER = "/productCategoryAdapter/getPromotionBanner";
    /**
     * 游戏报名余额支付
     */
    public static final String BALANCE_PAYMENT_ACT = "/pay/doBalanceByApplyId";
    /**
     * 获取促销分时段信息
     */
    public static final String GET_TIME_DIVISION = "/productCategoryAdapter/getTimeDivision";

    /**
     * 会员中心推荐游戏
     */
    public static final String RECOMMEND_ACT = "/memberCenter/recommendActivity";
    /**
     * 会员中心的会员记录
     */
    public static final String MEMBER_RECORD = "/memberCenter/dealRecord";
    /**
     * 游戏详情页面的推荐商品
     */
    public static final String RECOMMEND_GOODS = "/productCategoryAdapter/getRecommend";
    /**
     * 游戏到店体验
     */
    public static final String ACT_STATE = "/activity/recommendExperience";
    /**
     * 拼团商品列表
     */
    public static final String FIGHT_GROUP_LIST = "/productCategoryAdapter/getFightGroupList";
    /**
     * 拼团详情
     */
    public static final String FIGHT_GROUP_DETAIL = "/productCategoryAdapter/getFightGroupOpenDetails";
    /**
     * 拼团详情分享
     */
    public static final String FIGHTGROUP_SHARE_URL = "http://m.61park.cn/group/groupDetail.do?opneId=%s";
    /**
     * 添加旅客信息
     */
    public static final String INSERT_TRAVELLER = "/activityFrequent/insertFrequentContacts";

    /**
     * 请求获取旅客列表
     */
    public static final String GET_TRAVELLERS_LIST = "/activityFrequent/getFrequentContactsList";

    /**
     * 修改旅客信息
     */
    public static final String UPDATE_TRAVELLER = "/activityFrequent/updFrequentContacts";

    /**
     * 删除旅客信息
     */
    public static final String DELETE_TRAVELLER = "/activityFrequent/delFrequentContacts";

    /**
     * 获取亲子游订单基础信息
     */
    public static final String GET_ACTAPPLY_BY_TRAVELID = "/activityApply/getActApplyByTravelId";

    /**
     * 亲子游提交订单信息
     */
    public static final String FAMILY_TRIP_APPLY = "/activityApply/doTravelActApplyByApplyIdV2";

    /**
     * 确认报名小课url后缀
     */
    public static final String CONFIRM_APPLYS_END = "/activityApply/insertActsApply";

    /**
     * 游戏优惠券核销(小课)
     */
    public static final String UPDATE_ACT_APPLYS_COURSE = "/activityApply/updateActCourseApplys";

    /**
     * 获取店铺推荐码列表
     */
    public static final String GET_INVITECODES_BYSHOP = "/merchant/getRecommCodeListByOffId";

    /**
     * 游戏小课课程安排
     */
    public static final String GET_CLASS_AGMT = "/activity/classAgmt";
    /**
     * 卡明细
     */
    public static final String GET_CARD_DETAIL = "/memberCenter/cardDeal";
    /**
     * 会员卡类型
     */
    public static final String GET_CARD_TYPE = "/memberCenter/cardType";
    /**
     * 会员卡宝宝列表
     */
    public static final String GET_CHILDREN_LIST = "/memberCenter/childrenList";
    /**
     * 小时卡、次卡剩余次数
     */
    public static final String GET_REST_TIMES = "/memberCenter/restTimes";
    /**
     * 会员卡明细
     */
    public static final String GET_VIP_DETAIL = "/memberCenter/vipDeal";
    /**
     * 我的界面
     */
    public static final String GET_USER_INFO = "/userInfo/getUserInfoByUserId";
    /**
     * 我的界面
     */
    public static final String GET_DETAIL_RECORD = "/memberCenter/dealRecord";

    /**
     * 游戏已报名小课课程安排
     */
    public static final String GET_REGISTER_CLASS = "/activityApply/getRegisterClassV2";
    /**
     * 订单小课课程安排
     */
    public static final String GET_REGISTER_CLASS_BYORDER = "/activityApply/getRegisterClassByOrderId";
    /**
     * 店铺附近的店铺
     */
    public static final String GET_SHOP_NEAR = "/memberCenter/getShopNear";
    /**
     * 热门店铺排名
     */
    public static final String GET_POPULAR_SHOP = "/memberCenter/getPopularShop";
    /**
     * 常去店铺
     */
    public static final String GET_LOVE_SHOP_BY_USERID = "/memberCenter/getLoveShopByUserId";
    /**
     * 根据品牌获取商品
     */
    public static final String GET_PRODUCT_BY_BRANDID = "/product/getProductByBrandId";

    /**
     * 小课活动获取优惠券列表
     */
    public static final String GET_CANUSE_CLASS_COUPONLIST = "/coupon/getCanUseClassCouponListV2";
    /**
     * 可提现总金额
     */
    public static final String GET_WITHDRAW_AMOUNT = "/investor/withdrawAmount";
    /**
     * 分红详情
     */
    public static final String GET_MY_DIVIDENT = "/investor/myDivident";
    /**
     * 我投资的店铺
     */
    public static final String GET_CONTRACT_BY_USERID = "/investor/getContractByUserId";
    /**
     * 本期分红明细
     */
    public static final String GET_MONTH_DIVIDENT = "/investor/monthDivident";
    /**
     * 历史分红明细
     */
    public static final String GET_ALL_DIVIDENT = "/investor/allDivident";
    /**
     * 历史指定月份分红明细
     */
    public static final String GET_ALL_DIVIDENT_DETAIL = "/investor/allDividentDetail";
    /**
     * 合伙人类别
     */
    public static final String GET_INVESTOR_TYPE = "/investor/investorType";
    /**
     * 历史分红时间选择
     */
    public static final String GET_DIVIDENT_TIME = "/investor/dividentTime";
    /**
     * 提现明细
     */
    public static final String GET_OUT_LIST = "/investor/outList";
    /**
     * 分红提现明细
     */
    public static final String GET_OUT_LIST_V2 = "/investor/outListV2";
    /**
     * 提现详情
     */
    public static final String GET_OUT_DETAIL = "/investor/outDetail";
    /**
     * 分红提现详情
     */
    public static final String GET_OUT_DETAIL_V2 = "/investor/outDetailV2";

    /**
     * 获取账户安全基本信息
     */
    public static final String GET_ALIPAY_BY_USERID = "/user/getAlipayByUserId";

    /**
     * 验证原交易密码
     */
    public static final String CHECK_TRADE_PWD = "/investor/checkTradePwd";

    /**
     * SET_ALI_ACCOUNT
     */
    public static final String SET_ALI_ACCOUNT = "/investor/setWithdrawAccount";

    /**
     * SEND_BIND_ACCOUNT_VCODE
     */
    public static final String SEND_BIND_ACCOUNT_VCODE = "/investor/sendModifyAccountVerifyCode";

    /**
     * MODIFY_TRADEPWD_BY_OLDPWD
     */
    public static final String MODIFY_TRADEPWD_BY_OLDPWD = "/investor/modifyTradePwdByOldPwd";

    /**
     * modifyTradePwdByVerifyCode
     */
    public static final String MODIFY_TRADEPWD_BY_VCODE = "/investor/modifyTradePwdByVerifyCode";

    /**
     * SEND_MODIFY_TRADEPWD_VCODE
     */
    public static final String SEND_MODIFY_TRADEPWD_VCODE = "/investor/sendModifyTradePwdVerifyCode";

    /**
     * SET_TRADE_PWD
     */
    public static final String SET_TRADE_PWD = "/investor/setTradePwd";

    /**
     * SEND_DOWITHDRAW_VCODE
     */
    public static final String SEND_DOWITHDRAW_VCODE = "/investor/sendDoWithdrawVerifyCode";

    /**
     * DO_WITHDRAW
     */
    public static final String DO_WITHDRAW = "/investor/doWithdraw";

    /**
     * 限时秒杀接口修改
     */
    public static final String PROMOTION_MERGE = "/productCategoryAdapter/promotionMerge";
    /**
     * 物流详情
     */
    public static final String ORDER_TRACK = "/myOrder/orderTrack";
    /**
     * 订单详情页面接口修改
     */
    public static final String ORDER_MERCHANT_DETAIL = "/myOrder/orderMerchantDetailV2";
    /**
     * 确认订单页面接口修改
     */
    public static final String ORDER_CONFIRM_V3 = "/tradeOrder/confirmV3";
    /**
     * 获取四级地址的接口
     */
    public static final String ADDRESS_GET_TOWNS = "/address/getTownsByCountyId";

    /**
     * 获取商城模板
     */
    public static final String GET_MALL_PAGE = "/mallPage/getMallPage";

    /**
     * 获取所有品牌
     */
    public static final String FIND_PRODUCT_BRANDLIST = "/product/findProductBrandList";

    /**
     * 商品筛选搜索
     */
    public static final String UNIFORM_SEARCH = "/search/uniformSearch";

    /**
     * 获取首页icon
     */
    public static final String GET_ICON_BY_MODEL_ID = "/appIcon/getIconByModelId";

    /**
     * 获取广告页
     */
    public static final String ADVERTISING_VERSION = "/advertisingVersion/getVersion";

    /**
     * 更换手机号获取原手机号验证码
     */
    public static final String SEND_UNBINDING_VERIFY_CODE = "/login/sendUnbindingVerifyCode";

    /**
     * 更换手机号获取确认原手机号验证码
     */
    public static final String UNBINDING = "/login/unbinding";

    /**
     * 更换手机号获取新手机号验证码
     */
    public static final String SEND_BINDING_VERIFY_CODE = "/login/sendRebindingVerifyCode";

    /**
     * 更换手机号获取重新绑定
     */
    public static final String REBINDING = "/login/rebinding";
    /**
     * 计算退货应退金额
     */
    public static final String GRF_AMOUNT = "/grf/getGrfAmount";
    /**
     * 申请退货
     */
    public static final String GRF_APPlYV2 = "/grf/applyV2";
    /**
     * 撤销售后申请
     */
    public static final String CANCLE_GRF = "/grf/cancelGrf";
    /**
     * 退货类型申请
     */
    public static final String GRF_TYPE = "/grf/grfTypeList";
    /**
     * 余额消费明细列表数据
     */
    public static final String ACCOUNT_BALANCE_LIST = "/userAccountLog/list";
    /**
     * 余额消费明细详情数据
     */
    public static final String ACCOUNT_BALANCE_DETAIL = "/userAccountLog/detail";
    /**
     * 梦想详情发表评论数据
     */
    public static final String REQUERE_ADD_COMMENT = "/requirementComment/addComment";
    /**
     * 梦想详情评论列表
     */
    public static final String REQUERE_COMMENT_LIST = "/requirementComment/commentList";
    /**
     * 梦想详情点赞
     */
    public static final String REQUERE_PRAISE = "/requirementPraise/addRequirementPraise";
    /**
     * 梦想详情
     */
    public static final String REQUERE_DETAIL = "/requirement/findRequirement";
    /**
     * 我的梦想列表
     */
    public static final String REQUIREMEN_TLIST = "/requirement/requirement/findMyRequirementList";
    /**
     * 确认行程加入梦想接口
     */
    public static final String ADD_REQPRED = "/requirementPrediction/addReqPred";
    /**
     * 确认行程加入梦想接口
     */
    public static final String RECOMMENDED_ACT_BYREQ = "/requirementPrediction/getRecommendedActByReq";
    /**
     * 我的梦想列表放弃接口
     */
    public static final String GIVE_UP_REQUIREMENT = "/requirement/doGiveUpRequirement";
    /**
     * 梦想主题列表
     */
    public static final String THEME_REQUIREMENT_LIST = "/requirement/findThemeRequirementList";
    /**
     * 发布梦想-选择游戏模板列表
     */
    public static final String GET_ACTIVITY_TEMPLATE_LIST = "/requirement/getActivityTemplateList";
    /**
     * 发布梦想-选择标签-获取标签
     */
    public static final String GET_REQUIREMENT_TYPE_LIST = "/requirement/getRequirementTypeList";
    /**
     * 发布梦想
     */
    public static final String PUBLISH_REQUIREMENT = "/requirement/publishRequirement";
    /**
     * 游戏评价维度接口
     */
    public static final String GET_DIMENSION = "/activityEvaluate/getDimension";
    /**
     * 游戏评价接口
     */
    public static final String CLASS_EVALUATE = "/activityEvaluate/classEvaluate";
    /**
     * 游戏评价列表接口
     */
    public static final String GAME_EVALUATE_LIST = "/activityEvaluate/evaluateList";
    /**
     * 将友盟token上传到服务器
     */
    public static final String SAVE_APP_DEVICE_TOKEN = "/appDevice/saveAppDeviceToken";
    /**
     * 获取测评列表
     */
    public static final String LISTEASYS = "/ea/listEaSys";
    /**
     * 题目列表
     */
    public static final String LISTSUBJECTS = "/ea/listSubjects";

    /**
     * 题目列表2
     */
    public static final String LISTSUBJECTS_V2 = "/ea/listSubjectsV2";
    /**
     * 题目答案提交
     */
    public static final String RESULT_SUBMIT = "/ea/submit";
    /**
     * 题目答案提交2
     */
    public static final String RESULT_SUBMIT_V2 = "/ea/submitV2";
    /**
     *
     */
    public static final String GET_EA_RESULT = "/ea/getEaResult";
    /**
     * 能力阶梯测评列表
     */
    public static final String GET_LIST_EA_CHILD_RESULT = "/ea/listEaChildResult";
    /**
     * 能力阶梯推荐游戏
     */
    public static final String CAPACITY_RECOMMAND_ACT = "/capacityChild/getRecommandActivity";
    /**
     * 获取x计划按定周数据
     */
    public static final String OFFICE_XPLAN = "/activityScheduleArrange/officeXPlan";

    public static final String courseSchedule = "/course/courseSchedule";
    /**
     * 店铺首页小样列表
     */
    public static final String GET_OFFICE_CONTENT_ALBUMS = "/officePage/getOfficeContentAlbumsOfOffice";
    /**
     * 店铺首页小样单张评论列表
     */
    public static final String SINGLE_PIC_COMMENT_LIST = "/officePageOperate/getCommentList";
    /**
     * 店铺首页小样评论列表
     */
    public static final String GET_ALBUM_COMMENT_LIST = "/officePageOperate/getAlbumCommentList ";

    /**
     * 店铺首页小样单张图片评论接口
     */
    public static final String SINGLE_PIC_COMMENT = "/officePageOperate/comment";
    /**
     * 店铺首页小样单张图片点赞收藏接口
     */
    public static final String GET_PRAISE_COLLECTITON = "/officePageOperate/operate";
    /**
     * 店铺首页小样单张图片点赞、收藏状态接口
     */
    public static final String GET_OPERATE_STATUS = "/officePageOperate/getOperateStatus";
    /**
     * 店铺首页照片
     */
    public static final String GETOFFICEPAGE = "/officePage/getOfficePage";

    /**
     * 店铺首页照片getOfficePageV2
     */
    public static final String GETOFFICEPAGE_V2 = "/officePage/getOfficePageV2";
    /**
     * 店铺信息
     */
    public static final String GET_OFFICE_INFO = "/office/info";
    /**
     * 获取常去店铺
     */
    public static final String TOLOVEOFFICE = "/loveOffice/toLoveOffice";

    /**
     * 设置常去店铺
     */
    public static final String SETLOVEOFFICE = "/loveOffice/toChangeLoveOffice";

    public static final String RELODEHOTGAME = "/officePage/relodeHotGame";

    /**
     * 投票分享
     */
    public static final String VOTESHARE = "/vote/voteShare";

    /**
     * 获取视频详情
     */
    public static final String GET_VIDEO_DETAILS = "/home/getVideoDetail";

    /**
     * 获取首页数据
     */
    public static final String VIDEO_SEARCH = "/search/videoSearch";

    /**
     * 获取图文详情
     */
    public static final String GET_PIC_DETAILS = "/home/getBlogDetail";

    /**
     * 获取评论列表
     */
    public static final String GET_COMMENT_LIST = "/home/commentList";

    /**
     * 发表评论
     */
    public static final String ADD_COMMENT = "/home/addComment";

    /**
     * 发表图片
     */
    public static final String RELEASE_BLOG = "/home/releaseBlog";

    /**
     * 获取上传授权信息
     */
    public static final String GET_ALIYUN_AUTH = "/home/getAliyunAuth";

    /**
     * 发布视频
     */
    public static final String RELEASE_VIDEO = "/home/releaseVideo";

    /**
     * 我的动态视频
     */
    public static final String GET_VIDEO_DYNAMIC = "/my/videoDynamic";

    /**
     * 我的动态图文
     */
    public static final String GET_BLOG_DYNAMIC = "/my/blogDynamic";

    /**
     * 点赞
     */
    public static final String ADD_PRAISE = "/home/addPraise";

    /**
     * 话题收藏
     */
    public static final String ADD_COLLECTION = "/home/addCollection";

    /**
     * 话题取消收藏
     */
    public static final String DEL_COLLECTION = "/home/delCollection";

    /**
     * 内容收藏
     */
    public static final String CONTENT_COLLECT = "/service/focus/getContentFocusList";

    /**
     * 玩具共享列表
     */
    public static final String GET_BOX_SERIESLIST = "/toyShare/getBoxSeriesList";

    /**
     * 玩具共享详情
     */
    public static final String GET_BOX_SERIES_OBJECT = "/toyShare/getBoxSeriesObject";

    /**
     * 玩具共享详情
     */
    public static final String GETUSERDEPOSITAMOUNT = "/toyShare/getUserDepositAmount";

    /**
     * 玩具共享创建押金订单
     */
    public static final String CREATEDEPOSITORDER = "/toyShare/createDepositOrder";

    /**
     * 领取地址查询
     */
    public static final String GETUSERTOYADDRESSLIST = "/toyShare/getUserToyAddressList";

    /**
     * 添加申请记录
     */
    public static final String TOYBOXAPPLY = "/toyShare/toyBoxApply";

    /**
     * 我领取的玩具
     */
    public static final String GETUSERTOYAPPLYLOGLIST = "/toyShare/getUserToyApplyLogList";

    /**
     * 获取用户押金额
     */
    public static final String SHOWDEPOSITAMOUNT = "/toyShare/showDepositAmount";

    /**
     * 退用户押金额
     */
    public static final String EXTRACTDEPOSITAMOUNT = "/toyShare/extractDepositAmount";

    /**
     * 展示费用
     */
    public static final String GETTOYBOXCOST = "/toyShare/getToyBoxCost";

    /**
     * 申请退还
     */
    public static final String TOYBOXRETURN = "/toyShare/toyBoxReturn";

    /**
     * 申请进程展示
     */
    public static final String GETTOYAPPLYPROCESSLIST = "/toyShare/getToyApplyProcessList";

    /**
     * 专家列表
     */
    public static final String EXPERTLIST = "/home/expertList";

    public static final String contentUserInfo = "/home/contentUserInfo";

    public static final String fansList = "/home/fansList";

    public static final String focusUserList = "/home/focusUserList";

    public static final String expertAuth = "/home/expertAuth";

    public static final String domainList = "/home/domainList";

    public static final String authInfo = "/home/authInfo";

    public static final String cancelAuth = "/home/cancelAuth";

    public static final String getAuthNotice = "/home/getAuthNotice";

    public static final String changeResume = "/user/changeResume";

    public static final String courseDetail = "/course/courseDetail";

    public static final String evaluateList = "/course/evaluateList";

    public static final String applyCourse = "/course/applyCourse";

    public static final String submitCourseOrder = "/course/submitCourseOrder";

    public static final String listCourses = "/course/listCourses";

    public static final String childCourseList = "/course/childCourseList";

    public static final String addEvaluate = "/course/addEvaluate";

    public static final String orderPaySuccess = "/course/orderPaySuccess";

    public static final String getOfficeByCity = "/loveOffice/getOfficeByCity";

    public static final String listNotice = "/class/listNotice";

    public static final String commentList = "/class/commentList";

    public static final String addComment = "/class/addComment";

    public static final String addPraise = "/class/addPraise";

    public static final String detailNotice = "/class/detailNotice";

    public static final String init = "/class/init";

    public static final String join = "/class/join";

    public static final String ea_itemList = "/ea/itemList";

    public static final String ea_itemDetail = "/ea/itemDetail";

    public static final String ea_getRecordList = "/ea/getRecordList";

    public static final String eaGuide = "/ea/eaGuide";

    public static final String sendLoginVerifyCode = "/login/sendLoginVerifyCode";

    public static final String loginByVerifyCode = "/login/loginByVerifyCode";

    public static final String updateTkoenExpireTime = "/login/updateTkoenExpireTime";

    public static final String valRegisterVerifyCode = "/login/valRegisterVerifyCode";

    public static final String registerV2 = "/login/registerV2";

    public static final String checkLoginAccountIsExist = "/login/checkLoginAccountIsExist";

    public static final String checkVerifyCode = "/login/checkVerifyCode";

    public static final String editPassword = "/login/editPassword";

    public static final String deleteRecord = "/ea/deleteRecord";

    public static final String checkUpdateStatus = "/app/checkUpdateStatus";

    //--------------------新接口start------------------------------//
    public static final String loadHomePageData = "/service/cms/loadHomePageData";

    public static final String loadModuleDataPageById = "/service/cms/loadModuleDataPageById";

    public static final String getTrainerList = "/service/cms/getTrainerList";

    public static final String SEARCHHOT = "/service/search/hot";

    public static final String SEARCHHIS = "/service/search/history";

    public static final String DELSEARCHHOT = "/service/search/history/clear";

    public static final String AUTOSEARCHHOT = "/service/search/autoInput";

    public static final String CATEGORYSEARCHHOT = "/service/contentCategory/list";

    public static final String ORDERSEARCHHOT = "/service/dict/list";

    public static final String TEACH = "/service/trainer/getContentListPageByTrainerId";

    public static final String TEACHDETAIL = "/service/trainer/getTrainerDetail";

    public static final String TEACHCANCELFORCK = "/service/focus/delTrainerFocus";

    public static final String TEACHADDFORCUS = "/service/focus/addTrainerFocus";

    public static final String IMPORTSEARCHHOT = "/service/search/content";

    public static final String TEXTDETAIL = "/service/content/findContentBlogDetailById";

    public static final String WATCH = "/service/focus/addContentFocus";

    public static final String CANCELWATCH = "/service/focus/delContentFocus";

    public static final String SPECIALDETAIL = "/service/contentTopic/getTopicDetail";

    public static final String isJoined = "/service/class/isJoined";

    public static final String classNotice_photoList = "/service/classNotice/photoList";

    public static final String classNotice_noticeList = "/service/classNotice/noticeList";

    public static final String join_new = "/service/class/join";

    public static final String init_new = "/service/class/init";

    public static final String noticeDetails = "/service/classNotice/noticeDetails";

    public static final String checkPhotoFocus = "/service/classNotice/checkPhotoFocus";

    public static final String addPhotoFocus = "/service/focus/addPhotoFocus";

    public static final String VIDODETAIL = "/service/content/findContentVideoDetailById";


    public static final String TEXTCOMM = "/home/commentList";

    public static final String ADMIRE = "/home/addPraise";

    public static final String focusUserList_new = "/service/home/focusUserList";

    public static final String listSubjectsV3 = "/service/eaUserResult/listSubjectsV3";

    public static final String submitV3 = "/service/eaUserResult/submitV3";

    public static final String findEaServiceList = "/service/ea/findEaServiceList";

    public static final String getBasicDataSubject = "/service/eaUserResult/getBasicDataSubject";

    public static final String childList = "/service/child/childList";

    public static final String saveChildBasicData = "/service/eaUserResult/saveChildBasicData";

    public static final String addChildAlone = "/service/child/addChildAlone";

    public static final String loginCallback = "/service/eaUserResult/loginCallback";

    public static final String addChildUserRel = "/service/child/addChildUserRel";

    public static final String paymentInfo = "/service/pay/paymentInfo";

    //班级圈活动banner接口
    public static final String classWordBanner = "/service/party/bannerList";

    //活动详情
    public static final String activityDetail = "/service/party/detail";

    //报名接口
    public static final String signActivity = "/service/party/submit";

    //亲子活动关联商品(装备列表)
    public static final String childActivityToys = "/service/party/findPartyApplyProductList";

    public static final String childNumSelect = "/service/pmInfo/numSelect";

    //亲子活动商品详情
    public static final String toyDetail = "/service/pmInfo/detail";

    //商品规格
    public static final String toySizeAndColor = "/service/pmInfo/colorSize";

    //亲子活动报名详情页面头像
    public static final String signPeopleHeads = "/service/party/findPartyApplyHead";

    //亲子活动参加人员列表
    public static final String joinPartyPeoples = "/service/party/findPartyApplyUserList";

    //我的亲子活动报名列表
    public static final String findMyPartyApplyList = "/service/party/findMyPartyApplyList";

    //我的亲子活动报名详情
    public static final String applyDetail = "/service/party/applyDetail";

    //商品结算收货地址列表
    public static final String getAddressByUserId = "/service/address/getAddressByUserId";

    //商品结算收货地址-新增
    public static final String addAddressByUserId = "/service/address/addAddressByUserId";

    //获取省份列表
    public static final String getProvinces = "/service/address/getProvinces";

    //获取省份的城市列表
    public static final String getCitiesByProvinceId = "/service/address/getCitiesByProvinceId";

    //获取城市的区域列表
    public static final String getCountysByCityId = "/service/address/getCountysByCityId";

    //获取区域的乡镇列表
    public static final String getTownsByCountyId = "/service/address/getTownsByCountyId";

    //是否购买了装备
    public static final String hasBuyToys = "/service/party/isBuyedProduct";

    //亲子活动-商品结算-确认订单页面接口
    public static final String account = "/service/pmInfo/account";

    //亲子活动-商品订单列表
    public static final String getOrderList = "/service/myOrder/getOrderList";

    //亲子活动-商品订单提交
    public static final String soSubmit = "/service/so/submit";

    //亲子活动-商品订单详情
    public static final String getOrderDetail = "/service/myOrder/getOrderDetail";

    //获取售后电话
    public static final String getTel = "/service/aftersale/getTel";

    //商品订单_取消
    public static final String cancelOrder = "/service/so/cancelOrder";

    //商品订单_确认收货
    public static final String confirmGoods = "/service/so/confirmGoods";

    //获取地址详情
    public static final String getAddressById = "/service/address/getAddressById";

    //删除地址
    public static final String delAddressById = "/service/address/delAddressById";

    //更新地址
    public static final String updateAddressByUserId = "/service/address/updateAddressByUserId";

    //能够报名参加亲子活动宝宝列表
    public static final String canJoinBabyList = "/service/party/findApplyChildList";

    //我的优惠券列表
    public static final String getUserCouponList = "/service/coupon/getUserCouponList";

    //选择优惠券列表
    public static final String getEaCanUseCoupon = "/service/coupon/getEaCanUseCoupon";

    //个人中心优惠券数量
    public static final String getUserInfoByUserId = "/service/home/getUserInfoByUserId";

    //音视频详情
    public static final String findContentDetailById = "/service/content/findContentDetailById";

    //视频列表
    public static final String findContentVideoList = "/service/content/findContentVideoList";

    //根据资源id获取播放凭证
    public static final String videoPlayAuth = "/service/contentSource/videoPlayAuth";

    //增加播放次数
    public static final String addViewNum = "/service/contentSource/addViewNum";

    //音频列表
    public static final String getAudioList = "/service/content/getAudioList";

    //增加下载次数
    public static final String addDownLoadNum = "/service/contentSource/addDownLoadNum";

    //兴趣班订单详情
    public static final String interestClassOrderDetail = "/service/myOrder/interestClassOrderDetail";
    /**
     * 请求用户信息url后缀
     */
    public static final String getUser = "/service/user/getUser";


}
