package com.park61.moduel.sales.cache;

import com.park61.moduel.sales.bean.ProductLimit;
import com.park61.moduel.sales.bean.PromotionPageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类用于在秒杀主页面与各fragment之间传递数据
 * Created by shushucn2012 on 2016/7/1.
 */
public class SecKillDTO {

    public static List<ProductLimit> skTimeList = new ArrayList<ProductLimit>();

    public static ProductLimit getSkTime(long id, String startTime){
        for (int i=0;i<skTimeList.size();i++){
            ProductLimit p = skTimeList.get(i);
            if(p.getId()==id || p.getStart().equals(startTime)){
                return p;
            }
        }
        return null;
    }

    public static List<PromotionPageBean> ppbCacheList = new ArrayList<PromotionPageBean>();

    public static PromotionPageBean getPageBean(String id){
        for (int i=0;i<ppbCacheList.size();i++){
            PromotionPageBean p = ppbCacheList.get(i);
            if(p.getId().equals(id)){
                return p;
            }
        }
        return null;
    }
}
