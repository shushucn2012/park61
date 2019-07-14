package com.park61.moduel.openmember.bean;

import java.io.Serializable;

/**
 * 消费明细adapter
 */
public class ConsumptionDetail implements Serializable {
    private Long cardId;
    private String cardLengthName;
    private String cardName;
    private String time;
    private String updateTime;
    private Long userId;
    private String consumerType;//字体颜色：1-红色，2-黑色

    public String getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(String consumerType) {
        this.consumerType = consumerType;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getCardLengthName() {
        return cardLengthName;
    }

    public void setCardLengthName(String cardLengthName) {
        this.cardLengthName = cardLengthName;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
