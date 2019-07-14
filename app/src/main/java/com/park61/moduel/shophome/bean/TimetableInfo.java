package com.park61.moduel.shophome.bean;

import java.io.Serializable;


public class TimetableInfo implements Serializable {

    /**
     * courseId : 16
     * courseName : 梦幻小镇-粉色公主
     * endTime : 18:00
     * hadApply : false
     * officeId : 455
     * planId : 52
     * roomId : 14
     * sectionDate : 1500998400000
     * seriesId : 22
     * seriesName : coco梦幻小镇
     * startTime : 07:00
     * totalName : 梦幻小镇-粉色公主-coco梦幻小镇
     * weekCourseDay : 4
     * weekDay : 3
     */

    private int courseId;
    private String courseName;
    private String endTime;
    private boolean hadApply;
    private int officeId;
    private int planId;
    private int roomId;
    private long sectionDate;
    private int seriesId;
    private String seriesName;
    private String startTime;
    private String totalName;
    private String weekCourseDay;
    private int weekDay;
    private String priceSale;
    private String priceOriginal;
    private int courseNum;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isHadApply() {
        return hadApply;
    }

    public void setHadApply(boolean hadApply) {
        this.hadApply = hadApply;
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public long getSectionDate() {
        return sectionDate;
    }

    public void setSectionDate(long sectionDate) {
        this.sectionDate = sectionDate;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTotalName() {
        return totalName;
    }

    public void setTotalName(String totalName) {
        this.totalName = totalName;
    }

    public String getWeekCourseDay() {
        return weekCourseDay;
    }

    public void setWeekCourseDay(String weekCourseDay) {
        this.weekCourseDay = weekCourseDay;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public String getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(String priceSale) {
        this.priceSale = priceSale;
    }

    public String getPriceOriginal() {
        return priceOriginal;
    }

    public void setPriceOriginal(String priceOriginal) {
        this.priceOriginal = priceOriginal;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(int courseNum) {
        this.courseNum = courseNum;
    }
}
