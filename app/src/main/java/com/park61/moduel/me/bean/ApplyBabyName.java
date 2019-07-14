package com.park61.moduel.me.bean;

import java.io.Serializable;

/**
 * 参加游戏小孩名字信息
 */
public class ApplyBabyName implements Serializable{


    private Long applyId;
    private String visitorName;

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }
}
