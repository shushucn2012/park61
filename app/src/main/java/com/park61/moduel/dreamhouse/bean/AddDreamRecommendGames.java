package com.park61.moduel.dreamhouse.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 加入梦想成功后推荐游戏信息
 */
public class AddDreamRecommendGames implements Serializable {
    private Long requirementPredictionId;
    private String actAddress;
    private int actBussinessType;
    private String actCover;
    private BigDecimal actPrice;
    private String actTitle;
    private int grandTotal;
    private Long id;
    private Long refTemplateId;

    public Long getRequirementPredictionId() {
        return requirementPredictionId;
    }

    public void setRequirementPredictionId(Long requirementPredictionId) {
        this.requirementPredictionId = requirementPredictionId;
    }

    public String getActAddress() {
        return actAddress;
    }

    public void setActAddress(String actAddress) {
        this.actAddress = actAddress;
    }

    public int getActBussinessType() {
        return actBussinessType;
    }

    public void setActBussinessType(int actBussinessType) {
        this.actBussinessType = actBussinessType;
    }

    public String getActCover() {
        return actCover;
    }

    public void setActCover(String actCover) {
        this.actCover = actCover;
    }

    public BigDecimal getActPrice() {
        return actPrice;
    }

    public void setActPrice(BigDecimal actPrice) {
        this.actPrice = actPrice;
    }

    public String getActTitle() {
        return actTitle;
    }

    public void setActTitle(String actTitle) {
        this.actTitle = actTitle;
    }

    public int getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(int grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRefTemplateId() {
        return refTemplateId;
    }

    public void setRefTemplateId(Long refTemplateId) {
        this.refTemplateId = refTemplateId;
    }
}
