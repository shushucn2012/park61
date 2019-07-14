package com.park61.moduel.sales.bean;

/**
 * Created by shushucn2012 on 2016/6/7.
 */
public class ProductDeliverRegionVo {

    private String cityId;
    private String cityName;
    private String deliverType;
    private String provinceId;
    private String provinceName;
    private String showDeliverRegionName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(String deliverType) {
        this.deliverType = deliverType;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getShowDeliverRegionName() {
        return showDeliverRegionName;
    }

    public void setShowDeliverRegionName(String showDeliverRegionName) {
        this.showDeliverRegionName = showDeliverRegionName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
