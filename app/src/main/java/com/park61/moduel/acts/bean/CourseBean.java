package com.park61.moduel.acts.bean;

import java.io.Serializable;

/**
 * Created by super on 2016/8/24.
 */
public class CourseBean implements Serializable {

    private long id; //无用 数据库主键id
    private long actId; //系列课程主ID（所属活动id）
    private long actSingleId; //课程id
    private String actStartTime; //开始时间
    private String actEndTime; //结束时间
    private int sort; //排序
    private boolean end; //是否可报名

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getActId() {
        return actId;
    }

    public void setActId(long actId) {
        this.actId = actId;
    }

    public long getActSingleId() {
        return actSingleId;
    }

    public void setActSingleId(long actSingleId) {
        this.actSingleId = actSingleId;
    }

    public String getActStartTime() {
        return actStartTime;
    }

    public void setActStartTime(String actStartTime) {
        this.actStartTime = actStartTime;
    }

    public String getActEndTime() {
        return actEndTime;
    }

    public void setActEndTime(String actEndTime) {
        this.actEndTime = actEndTime;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

}
