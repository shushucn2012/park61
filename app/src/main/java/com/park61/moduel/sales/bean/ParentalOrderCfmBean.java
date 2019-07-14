package com.park61.moduel.sales.bean;

import com.park61.moduel.address.bean.AddressDetailItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shubei on 2018/1/9.
 */

public class ParentalOrderCfmBean implements Serializable{


    /**
     * listPmInfo : [{"productDescription":"测试内容eg33","count":1,"currentUnifyPrice":0.01,"id":84,"marketPrice":8888,"productCname":"联想笔记本电脑Y80","productColor":"黑色","productPicUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/product/20170817105234818_769.png","productSize":"22"},{"productDescription":"测试内容eg33","count":3,"currentUnifyPrice":0.02,"id":85,"marketPrice":6666,"productCname":"联想笔记本电脑Y80","productColor":"灰色","productPicUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/product/20170817105237232_118.png","productSize":"21"},{"productDescription":"测试内容eg33","count":4,"currentUnifyPrice":0.01,"id":86,"marketPrice":5555,"productCname":"联想笔记本电脑Y80","productColor":"黑色","productPicUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/product/20170817105240281_752.png","productSize":"21"},{"productDescription":"测试内容eg33","count":5,"currentUnifyPrice":998,"id":83,"marketPrice":999,"productCname":"xielunyan","productColor":"红色","productPicUrl":"","productSize":"2尺"}]
     * orderAmount : 998.04
     * orderDeliveryFee : 0
     * productAmount : 998.04
     */

    private double orderAmount;
    private int orderDeliveryFee;
    private double productAmount;
    private List<ListPmInfoBean> listPmInfo;
    private AddressDetailItem goodReceiverVO;

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getOrderDeliveryFee() {
        return orderDeliveryFee;
    }

    public void setOrderDeliveryFee(int orderDeliveryFee) {
        this.orderDeliveryFee = orderDeliveryFee;
    }

    public double getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(double productAmount) {
        this.productAmount = productAmount;
    }

    public List<ListPmInfoBean> getListPmInfo() {
        return listPmInfo;
    }

    public void setListPmInfo(List<ListPmInfoBean> listPmInfo) {
        this.listPmInfo = listPmInfo;
    }

    public AddressDetailItem getGoodReceiverVO() {
        return goodReceiverVO;
    }

    public void setGoodReceiverVO(AddressDetailItem goodReceiverVO) {
        this.goodReceiverVO = goodReceiverVO;
    }

    public static class ListPmInfoBean implements Serializable {
        /**
         * productDescription : 测试内容eg33
         * count : 1
         * currentUnifyPrice : 0.01
         * id : 84
         * marketPrice : 8888
         * productCname : 联想笔记本电脑Y80
         * productColor : 黑色
         * productPicUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/product/20170817105234818_769.png
         * productSize : 22
         */

        private String productDescription;
        private int count;
        private double currentUnifyPrice;
        private int id;
        private double marketPrice;
        private String productCname;
        private String productColor;
        private String productPicUrl;
        private String productSize;

        public String getProductDescription() {
            return productDescription;
        }

        public void setProductDescription(String productDescription) {
            this.productDescription = productDescription;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getCurrentUnifyPrice() {
            return currentUnifyPrice;
        }

        public void setCurrentUnifyPrice(double currentUnifyPrice) {
            this.currentUnifyPrice = currentUnifyPrice;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(double marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getProductCname() {
            return productCname;
        }

        public void setProductCname(String productCname) {
            this.productCname = productCname;
        }

        public String getProductColor() {
            return productColor;
        }

        public void setProductColor(String productColor) {
            this.productColor = productColor;
        }

        public String getProductPicUrl() {
            return productPicUrl;
        }

        public void setProductPicUrl(String productPicUrl) {
            this.productPicUrl = productPicUrl;
        }

        public String getProductSize() {
            return productSize;
        }

        public void setProductSize(String productSize) {
            this.productSize = productSize;
        }
    }
}
