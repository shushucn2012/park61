package com.park61.moduel.acts.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shushucn2012 on 2016/7/18.
 */
public class ActCouponSelectChildCache {

    public static List<Long> selectChilds = new ArrayList<Long>();

    /**
     * 记录活动是否可用优惠券
     */
    public static HashMap<Long, Boolean> canUseCouponAct = new HashMap<>();
}
