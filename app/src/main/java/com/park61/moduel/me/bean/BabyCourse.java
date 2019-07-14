package com.park61.moduel.me.bean;

import java.io.Serializable;

/**
 * Created by shubei on 2017/7/23.
 */

public class BabyCourse implements Serializable {


    /**
     * canEvaluate : false
     * id : 22
     * name : 体能构建7-8岁高级班
     * schedule : 0
     * seriesName : 万达体能构建
     * teacherId : 22
     * totalName : 万达体能构建之 - 体能构建7-8岁高级班
     * totalTime : 1
     * usedTime : 0
     * videoImg : http://park61.oss-cn-zhangjiakou.aliyuncs.com/course/20170717111751756_99.png
     */

    private boolean canEvaluate;
    private int id;
    private String name;
    private double schedule;
    private String seriesName;
    private int teacherId;
    private String teacherName;
    private String totalName;
    private int totalTime;
    private int usedTime;
    private String videoImg;
    private String lastClassDate;
    private String teacherUrl;
    private long applyId;

    public boolean isCanEvaluate() {
        return canEvaluate;
    }

    public void setCanEvaluate(boolean canEvaluate) {
        this.canEvaluate = canEvaluate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSchedule() {
        return schedule;
    }

    public void setSchedule(double schedule) {
        this.schedule = schedule;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTotalName() {
        return totalName;
    }

    public void setTotalName(String totalName) {
        this.totalName = totalName;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(int usedTime) {
        this.usedTime = usedTime;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public String getLastClassDate() {
        return lastClassDate;
    }

    public void setLastClassDate(String lastClassDate) {
        this.lastClassDate = lastClassDate;
    }

    public String getTeacherUrl() {
        return teacherUrl;
    }

    public void setTeacherUrl(String teacherUrl) {
        this.teacherUrl = teacherUrl;
    }

    public long getApplyId() {
        return applyId;
    }

    public void setApplyId(long applyId) {
        this.applyId = applyId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
