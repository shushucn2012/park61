package com.park61.moduel.order.bean;

import java.io.Serializable;

/**
 * Created by shubei on 2018/1/11.
 */

public class MeOrderDetailsGoodsBean implements Serializable {


    /**
     * id : null
     * productId : 43
     * pmInfoId : 33
     * orderItemAmount : 79
     * orderItemPrice : 79
     * orderItemNum : 1
     * productCname : 万能工匠 儿童益智玩具 齿轮配件玩具包
     * productPicUrl : http://ghpprod.img-cn-qingdao.aliyuncs.com/product/20170515113419598_593.png@100h_100w_0e
     * productColor : 中大小齿轮
     * productSize : 蓝8个绿8个黄8个红8个
     * isGrfGoods : null
     */

    private long id;
    private long productId;
    private long pmInfoId;
    private double orderItemAmount;
    private double orderItemPrice;
    private int orderItemNum;
    private String productCname;
    private String productPicUrl;
    private String productColor;
    private String productSize;
    private boolean isGrfGoods;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getPmInfoId() {
        return pmInfoId;
    }

    public void setPmInfoId(long pmInfoId) {
        this.pmInfoId = pmInfoId;
    }

    public double getOrderItemAmount() {
        return orderItemAmount;
    }

    public void setOrderItemAmount(int orderItemAmount) {
        this.orderItemAmount = orderItemAmount;
    }

    public double getOrderItemPrice() {
        return orderItemPrice;
    }

    public void setOrderItemPrice(int orderItemPrice) {
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

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public boolean getIsGrfGoods() {
        return isGrfGoods;
    }

    public void setIsGrfGoods(boolean isGrfGoods) {
        this.isGrfGoods = isGrfGoods;
    }
}
