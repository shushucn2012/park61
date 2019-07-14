package com.park61.moduel.sales.bean;

import java.io.Serializable;

/**
 * 品牌类
 * Created by shushucn2012 on 2016/10/30.
 */
public class BrandBean implements Serializable, Comparable<BrandBean> {

    private long id;//品牌id
    private String brandName;//品牌名称
    private String brandNameFirstSpell;//品牌名称首字母
    private String brandNameSpell;//品牌名称全拼
    private String brandCompanyName;//品牌公司名称
    private String brandLogoUrl;//品牌图片链接
    private boolean isChosen;//是否选中

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandCompanyName() {
        return brandCompanyName;
    }

    public void setBrandCompanyName(String brandCompanyName) {
        this.brandCompanyName = brandCompanyName;
    }

    public String getBrandLogoUrl() {
        return brandLogoUrl;
    }

    public void setBrandLogoUrl(String brandLogoUrl) {
        this.brandLogoUrl = brandLogoUrl;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public String getBrandNameFirstSpell() {
        return brandNameFirstSpell;
    }

    public void setBrandNameFirstSpell(String brandNameFirstSpell) {
        this.brandNameFirstSpell = brandNameFirstSpell;
    }

    public String getBrandNameSpell() {
        return brandNameSpell;
    }

    public void setBrandNameSpell(String brandNameSpell) {
        this.brandNameSpell = brandNameSpell;
    }

    @Override
    public int compareTo(BrandBean another) {
        return this.getBrandName().charAt(0) - another.getBrandName().charAt(0);
    }

}
