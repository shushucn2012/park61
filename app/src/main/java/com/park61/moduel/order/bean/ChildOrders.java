package com.park61.moduel.order.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by HP on 2016/10/27.
 */
public class ChildOrders implements Serializable {
    private ArrayList<OrderBean> orderList;

    public ArrayList<OrderBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<OrderBean> orderList) {
        this.orderList = orderList;
    }
}
