package com.park61.moduel.order.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详情信息
 */
public class MeOrderDetailBean implements Serializable{

    /**
     * id : 101766358088
     * endUserId : null
     * orderAmount : 527.58
     * productAmount : 527.58
     * orderDeliveryFee : 0
     * orderToPayAmount : 527.58
     * couponAmount : 0
     * discountAmount : 0
     * orderStatus : 10
     * orderStatusName : 订单取消
     * orderType : 0
     * orderRemark : null
     * orderCreateTime : 2018-01-11 16:59:38
     * paidOnlineThreshold : null
     * parentSoId : null
     * isLeaf : null
     * goodReceiverName : 办业务
     * goodReceiverProvince : 海南
     * goodReceiverCity : 海口市
     * goodReceiverCounty : 琼山区
     * goodReceiverTown : 长昌煤矿
     * goodReceiverAddress : 徒劳无功哈本中央五套基本中央五套了咯血<好空虚无卡阿芬www
     * goodReceiverPostcode : null
     * goodReceiverMobile : 18062518966
     * deliveryRemark : null
     * countDownTime : null
     * items : [{"id":null,"productId":43,"pmInfoId":33,"orderItemAmount":79,"orderItemPrice":79,"orderItemNum":1,"productCname":"万能工匠 儿童益智玩具 齿轮配件玩具包","productPicUrl":"http://ghpprod.img-cn-qingdao.aliyuncs.com/product/20170515113419598_593.png@100h_100w_0e","productColor":"中大小齿轮","productSize":"蓝8个绿8个黄8个红8个","isGrfGoods":null},{"id":null,"productId":40,"pmInfoId":38,"orderItemAmount":145.58,"orderItemPrice":145.58,"orderItemNum":1,"productCname":"万能工匠益智早教儿童玩具 幼儿园小班中班大班课堂玩具男孩女孩","productPicUrl":"http://ghpprod.img-cn-qingdao.aliyuncs.com/product/20170515112614495_616.png@100h_100w_0e","productColor":"创造力小班","productSize":"初级（上）","isGrfGoods":null},{"id":null,"productId":36,"pmInfoId":39,"orderItemAmount":135,"orderItemPrice":135,"orderItemNum":1,"productCname":"万能工匠 宝宝早教益智玩具 儿童智拼插积木 男孩女孩 火箭车","productPicUrl":"http://ghpprod.img-cn-qingdao.aliyuncs.com/product/20170515111910912_322.png@100h_100w_0e","productColor":"火箭车","productSize":"默认","isGrfGoods":null},{"id":null,"productId":35,"pmInfoId":40,"orderItemAmount":49,"orderItemPrice":49,"orderItemNum":1,"productCname":"万能工匠益拼装早教 男孩女孩3-6周岁益智拼插宝宝儿童玩具学徒版","productPicUrl":"http://ghpprod.img-cn-qingdao.aliyuncs.com/product/20170515111611797_803.png@100h_100w_0e","productColor":"学徒入门（升级包装）","productSize":"默认","isGrfGoods":null},{"id":null,"productId":31,"pmInfoId":41,"orderItemAmount":119,"orderItemPrice":119,"orderItemNum":1,"productCname":"万能工匠积木早教益智拼装积木儿童玩具男孩女孩包邮L系列特价","productPicUrl":"http://ghpprod.img-cn-qingdao.aliyuncs.com/product/20170515110943408_207.png@100h_100w_0e","productColor":"NO.1梦幻乐园","productSize":"默认","isGrfGoods":null}]
     * isEvaluated : null
     * childOrders : null
     * picUrlList : null
     * packageNum : null
     * orderItemNum : 5
     * fightGroupOpenId : null
     * fightGroupOpensStatus : null
     * orderCancelReason : 支付超时，交易关闭
     * returnFlag : true
     * merchantId : 2
     * thirdPartyOrderId : null
     * orderDeliveryCode : null
     * orderDeliveryCompany : null
     * merchantName : null
     * returnAmount : null
     * merchantOrder : null
     */

    private long id;
    private long endUserId;
    private double orderAmount;
    private double productAmount;
    private double orderDeliveryFee;
    private double orderToPayAmount;
    private int couponAmount;
    private int discountAmount;
    private int orderStatus;
    private String orderStatusName;
    private int orderType;
    private String orderRemark;
    private String orderCreateTime;
    private String paidOnlineThreshold;
    private String parentSoId;
    private String isLeaf;
    private String goodReceiverName;
    private String goodReceiverProvince;
    private String goodReceiverCity;
    private String goodReceiverCounty;
    private String goodReceiverTown;
    private String goodReceiverAddress;
    private String goodReceiverPostcode;
    private String goodReceiverMobile;
    private String deliveryRemark;
    private String countDownTime;
    private String isEvaluated;
    private String childOrders;
    private String picUrlList;
    private String packageNum;
    private int orderItemNum;
    private String fightGroupOpenId;
    private String fightGroupOpensStatus;
    private String orderCancelReason;
    private boolean returnFlag;
    private int merchantId;
    private String thirdPartyOrderId;
    private String orderDeliveryCode;
    private String orderDeliveryCompany;
    private String merchantName;
    private String returnAmount;
    private String merchantOrder;
    private List<MeOrderDetailsGoodsBean> items;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(long endUserId) {
        this.endUserId = endUserId;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public double getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(double productAmount) {
        this.productAmount = productAmount;
    }

    public double getOrderDeliveryFee() {
        return orderDeliveryFee;
    }

    public void setOrderDeliveryFee(double orderDeliveryFee) {
        this.orderDeliveryFee = orderDeliveryFee;
    }

    public double getOrderToPayAmount() {
        return orderToPayAmount;
    }

    public void setOrderToPayAmount(double orderToPayAmount) {
        this.orderToPayAmount = orderToPayAmount;
    }

    public int getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(int couponAmount) {
        this.couponAmount = couponAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getPaidOnlineThreshold() {
        return paidOnlineThreshold;
    }

    public void setPaidOnlineThreshold(String paidOnlineThreshold) {
        this.paidOnlineThreshold = paidOnlineThreshold;
    }

    public String getParentSoId() {
        return parentSoId;
    }

    public void setParentSoId(String parentSoId) {
        this.parentSoId = parentSoId;
    }

    public String getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getGoodReceiverName() {
        return goodReceiverName;
    }

    public void setGoodReceiverName(String goodReceiverName) {
        this.goodReceiverName = goodReceiverName;
    }

    public String getGoodReceiverProvince() {
        return goodReceiverProvince;
    }

    public void setGoodReceiverProvince(String goodReceiverProvince) {
        this.goodReceiverProvince = goodReceiverProvince;
    }

    public String getGoodReceiverCity() {
        return goodReceiverCity;
    }

    public void setGoodReceiverCity(String goodReceiverCity) {
        this.goodReceiverCity = goodReceiverCity;
    }

    public String getGoodReceiverCounty() {
        return goodReceiverCounty;
    }

    public void setGoodReceiverCounty(String goodReceiverCounty) {
        this.goodReceiverCounty = goodReceiverCounty;
    }

    public String getGoodReceiverTown() {
        return goodReceiverTown;
    }

    public void setGoodReceiverTown(String goodReceiverTown) {
        this.goodReceiverTown = goodReceiverTown;
    }

    public String getGoodReceiverAddress() {
        return goodReceiverAddress;
    }

    public void setGoodReceiverAddress(String goodReceiverAddress) {
        this.goodReceiverAddress = goodReceiverAddress;
    }

    public String getGoodReceiverPostcode() {
        return goodReceiverPostcode;
    }

    public void setGoodReceiverPostcode(String goodReceiverPostcode) {
        this.goodReceiverPostcode = goodReceiverPostcode;
    }

    public String getGoodReceiverMobile() {
        return goodReceiverMobile;
    }

    public void setGoodReceiverMobile(String goodReceiverMobile) {
        this.goodReceiverMobile = goodReceiverMobile;
    }

    public String getDeliveryRemark() {
        return deliveryRemark;
    }

    public void setDeliveryRemark(String deliveryRemark) {
        this.deliveryRemark = deliveryRemark;
    }

    public String getCountDownTime() {
        return countDownTime;
    }

    public void setCountDownTime(String countDownTime) {
        this.countDownTime = countDownTime;
    }

    public String getIsEvaluated() {
        return isEvaluated;
    }

    public void setIsEvaluated(String isEvaluated) {
        this.isEvaluated = isEvaluated;
    }

    public String getChildOrders() {
        return childOrders;
    }

    public void setChildOrders(String childOrders) {
        this.childOrders = childOrders;
    }

    public String getPicUrlList() {
        return picUrlList;
    }

    public void setPicUrlList(String picUrlList) {
        this.picUrlList = picUrlList;
    }

    public String getPackageNum() {
        return packageNum;
    }

    public void setPackageNum(String packageNum) {
        this.packageNum = packageNum;
    }

    public int getOrderItemNum() {
        return orderItemNum;
    }

    public void setOrderItemNum(int orderItemNum) {
        this.orderItemNum = orderItemNum;
    }

    public String getFightGroupOpenId() {
        return fightGroupOpenId;
    }

    public void setFightGroupOpenId(String fightGroupOpenId) {
        this.fightGroupOpenId = fightGroupOpenId;
    }

    public String getFightGroupOpensStatus() {
        return fightGroupOpensStatus;
    }

    public void setFightGroupOpensStatus(String fightGroupOpensStatus) {
        this.fightGroupOpensStatus = fightGroupOpensStatus;
    }

    public String getOrderCancelReason() {
        return orderCancelReason;
    }

    public void setOrderCancelReason(String orderCancelReason) {
        this.orderCancelReason = orderCancelReason;
    }

    public boolean isReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getThirdPartyOrderId() {
        return thirdPartyOrderId;
    }

    public void setThirdPartyOrderId(String thirdPartyOrderId) {
        this.thirdPartyOrderId = thirdPartyOrderId;
    }

    public String getOrderDeliveryCode() {
        return orderDeliveryCode;
    }

    public void setOrderDeliveryCode(String orderDeliveryCode) {
        this.orderDeliveryCode = orderDeliveryCode;
    }

    public String getOrderDeliveryCompany() {
        return orderDeliveryCompany;
    }

    public void setOrderDeliveryCompany(String orderDeliveryCompany) {
        this.orderDeliveryCompany = orderDeliveryCompany;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(String returnAmount) {
        this.returnAmount = returnAmount;
    }

    public String getMerchantOrder() {
        return merchantOrder;
    }

    public void setMerchantOrder(String merchantOrder) {
        this.merchantOrder = merchantOrder;
    }

    public List<MeOrderDetailsGoodsBean> getItems() {
        return items;
    }

    public void setItems(List<MeOrderDetailsGoodsBean> items) {
        this.items = items;
    }
}
