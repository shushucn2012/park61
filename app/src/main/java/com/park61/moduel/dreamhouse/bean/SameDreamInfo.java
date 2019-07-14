package com.park61.moduel.dreamhouse.bean;

import java.io.Serializable;

/**
 * 同梦人信息
 */
public class SameDreamInfo implements Serializable {
    private Long activityId;
    private int requirementType;
    private String userName;
    private String showTime;
    private String userUrl;
    private int requirementNum;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public int getRequirementType() {
        return requirementType;
    }

    public void setRequirementType(int requirementType) {
        this.requirementType = requirementType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public int getRequirementNum() {
        return requirementNum;
    }

    public void setRequirementNum(int requirementNum) {
        this.requirementNum = requirementNum;
    }
}
