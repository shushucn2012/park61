package com.park61.moduel.shophome.bean;

import java.io.Serializable;


public class XplanInfo implements Serializable {

    private Long activityId;

    private String activityTypeName;

    private String activityName;

    private int cityId;

    private int classroomId;

    private int hasApply;

    private Long id;

    private Long officeId;

    private Long refTemplateId;

    private Long sectionDate;

    private String sectionEndTime;

    private int sectionNum;

    private String sectionStartTime;

    private int status;

    private int teacherId;

    private int weekIndex=-1;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityTypeName() {
        return activityTypeName;
    }

    public void setActivityTypeName(String activityTypeName) {
        this.activityTypeName = activityTypeName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(int classroomId) {
        this.classroomId = classroomId;
    }

    public int getHasApply() {
        return hasApply;
    }

    public void setHasApply(int hasApply) {
        this.hasApply = hasApply;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public Long getRefTemplateId() {
        return refTemplateId;
    }

    public void setRefTemplateId(Long refTemplateId) {
        this.refTemplateId = refTemplateId;
    }

    public Long getSectionDate() {
        return sectionDate;
    }

    public void setSectionDate(Long sectionDate) {
        this.sectionDate = sectionDate;
    }

    public String getSectionEndTime() {
        return sectionEndTime;
    }

    public void setSectionEndTime(String sectionEndTime) {
        this.sectionEndTime = sectionEndTime;
    }

    public int getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(int sectionNum) {
        this.sectionNum = sectionNum;
    }

    public String getSectionStartTime() {
        return sectionStartTime;
    }

    public void setSectionStartTime(String sectionStartTime) {
        this.sectionStartTime = sectionStartTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getWeekIndex() {
        return weekIndex;
    }

    public void setWeekIndex(int weekIndex) {
        this.weekIndex = weekIndex;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
