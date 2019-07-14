package com.park61.moduel.child.bean;

import com.park61.moduel.acts.bean.Entity;

import java.math.BigDecimal;

/**
 * 能力阶梯推荐游戏
 */
public class AblityLevelRecGames extends Entity {
    private String actCover;//游戏图片url
    private String actTitle;//活动title
    private BigDecimal actPrice;//活动单价
    private String actBusinessName;//标签名
    private BigDecimal actOriginalPrice;//原价

    private Long id;//活动id
    private Long backendShopId;//举办店铺id
    private int actVisitorType;//活动参与类型: 0:免费;1:收费
    private int actReleaseType;//活动发布类型（官方，自主）
    private int grandTotal;
    private boolean isSmallClass;
    private Long refTemplateId;//模板id

    public BigDecimal getActOriginalPrice() {
        return actOriginalPrice;
    }

    public void setActOriginalPrice(BigDecimal actOriginalPrice) {
        this.actOriginalPrice = actOriginalPrice;
    }

    public String getActBusinessName() {
        return actBusinessName;
    }

    public void setActBusinessName(String actBusinessName) {
        this.actBusinessName = actBusinessName;
    }

    public int getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(int grandTotal) {
        this.grandTotal = grandTotal;
    }

    public boolean isSmallClass() {
        return isSmallClass;
    }

    public void setSmallClass(boolean smallClass) {
        isSmallClass = smallClass;
    }

    public Long getRefTemplateId() {
        return refTemplateId;
    }

    public void setRefTemplateId(Long refTemplateId) {
        this.refTemplateId = refTemplateId;
    }

    public String getActCover() {
        return actCover;
    }

    public void setActCover(String actCover) {
        this.actCover = actCover;
    }

    public String getActTitle() {
        return actTitle;
    }

    public void setActTitle(String actTitle) {
        this.actTitle = actTitle;
    }

    public BigDecimal getActPrice() {
        return actPrice;
    }

    public void setActPrice(BigDecimal actPrice) {
        this.actPrice = actPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBackendShopId() {
        return backendShopId;
    }

    public void setBackendShopId(Long backendShopId) {
        this.backendShopId = backendShopId;
    }

    public int getActVisitorType() {
        return actVisitorType;
    }

    public void setActVisitorType(int actVisitorType) {
        this.actVisitorType = actVisitorType;
    }

    public int getActReleaseType() {
        return actReleaseType;
    }

    public void setActReleaseType(int actReleaseType) {
        this.actReleaseType = actReleaseType;
    }
}
