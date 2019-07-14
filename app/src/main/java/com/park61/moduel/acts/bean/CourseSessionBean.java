package com.park61.moduel.acts.bean;

import java.io.Serializable;

/**
 * Created by shubei on 2017/7/22.
 */

public class CourseSessionBean implements Serializable {

    /**
     * classTime : 周三08:00
     * planId : 72
     * resume : 测试
     * startTime : 08:00
     * teacherId : 23
     * teacherImg : http://park61.oss-cn-zhangjiakou.aliyuncs.com/banner/20170717164329604_157.png
     * teacherName : 测试1
     * weekCourseDay : 4
     */

    private String classTime;
    private int planId;
    private String resume;
    private String startTime;
    private int teacherId;
    private String teacherImg;
    private String teacherName;
    private String teacherTitle;
    private String weekCourseDay;

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherImg() {
        return teacherImg;
    }

    public void setTeacherImg(String teacherImg) {
        this.teacherImg = teacherImg;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getWeekCourseDay() {
        return weekCourseDay;
    }

    public void setWeekCourseDay(String weekCourseDay) {
        this.weekCourseDay = weekCourseDay;
    }

    public String getTeacherTitle() {
        return teacherTitle;
    }

    public void setTeacherTitle(String teacherTitle) {
        this.teacherTitle = teacherTitle;
    }
}
