package com.park61.moduel.order.bean;

import java.util.List;

/**
 * Created by shubei on 2018/8/20.
 */

public class InterestClassOrderDetailBean {


    /**
     * id : 101915204491
     * orderAmount : 0.01
     * productAmount : 0.01
     * orderToPayAmount : 0.01
     * couponAmount : 0
     * discountAmount : 0
     * orderStatus : 1
     * orderStatusName : 等待买家付款
     * orderType : 14
     * orderRemark : 111
     * orderCreateTime : 2018-08-20 15:13:53
     * items : [{"id":null,"productId":null,"pmInfoId":14,"orderItemAmount":0.01,"orderItemPrice":0.01,"orderItemNum":1,"productCname":"COCO的绘画班","productPicUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20180817154036716_563.jpg","productColor":null,"productSize":null,"isGrfGoods":null}]
     * childOrders : null
     * orderItemNum : 1
     * orderCancelReason :
     * merchantId : 0
     * className : xiachao
     * childName : 1311
     * contactMobile : 13131111111
     */

    private long id;
    private double orderAmount;
    private double productAmount;
    private double orderToPayAmount;
    private int couponAmount;
    private int discountAmount;
    private int orderStatus;
    private String orderStatusName;
    private int orderType;
    private String orderRemark;
    private String orderCreateTime;
    private int orderItemNum;
    private String orderCancelReason;
    private int merchantId;
    private String className;
    private String childName;
    private String contactMobile;
    private List<ItemsBean> items;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getOrderItemNum() {
        return orderItemNum;
    }

    public void setOrderItemNum(int orderItemNum) {
        this.orderItemNum = orderItemNum;
    }

    public String getOrderCancelReason() {
        return orderCancelReason;
    }

    public void setOrderCancelReason(String orderCancelReason) {
        this.orderCancelReason = orderCancelReason;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * id : null
         * productId : null
         * pmInfoId : 14
         * orderItemAmount : 0.01
         * orderItemPrice : 0.01
         * orderItemNum : 1
         * productCname : COCO的绘画班
         * productPicUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20180817154036716_563.jpg
         * productColor : null
         * productSize : null
         * isGrfGoods : null
         */

        private Object id;
        private Object productId;
        private int pmInfoId;
        private double orderItemAmount;
        private double orderItemPrice;
        private int orderItemNum;
        private String productCname;
        private String productPicUrl;
        private Object productColor;
        private Object productSize;
        private Object isGrfGoods;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public Object getProductId() {
            return productId;
        }

        public void setProductId(Object productId) {
            this.productId = productId;
        }

        public int getPmInfoId() {
            return pmInfoId;
        }

        public void setPmInfoId(int pmInfoId) {
            this.pmInfoId = pmInfoId;
        }

        public double getOrderItemAmount() {
            return orderItemAmount;
        }

        public void setOrderItemAmount(double orderItemAmount) {
            this.orderItemAmount = orderItemAmount;
        }

        public double getOrderItemPrice() {
            return orderItemPrice;
        }

        public void setOrderItemPrice(double orderItemPrice) {
            this.orderItemPrice = orderItemPrice;
        }

        public int getOrderItemNum() {
            return orderItemNum;
        }

        public void setOrderItemNum(int orderItemNum) {
            this.orderItemNum = orderItemNum;
        }

        public String getProductCname() {
            return productCname;
        }

        public void setProductCname(String productCname) {
            this.productCname = productCname;
        }

        public String getProductPicUrl() {
            return productPicUrl;
        }

        public void setProductPicUrl(String productPicUrl) {
            this.productPicUrl = productPicUrl;
        }

        public Object getProductColor() {
            return productColor;
        }

        public void setProductColor(Object productColor) {
            this.productColor = productColor;
        }

        public Object getProductSize() {
            return productSize;
        }

        public void setProductSize(Object productSize) {
            this.productSize = productSize;
        }

        public Object getIsGrfGoods() {
            return isGrfGoods;
        }

        public void setIsGrfGoods(Object isGrfGoods) {
            this.isGrfGoods = isGrfGoods;
        }
    }
}
