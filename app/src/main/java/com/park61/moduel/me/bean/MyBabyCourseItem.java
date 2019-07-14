package com.park61.moduel.me.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shubei on 2017/7/23.
 */

public class MyBabyCourseItem implements Serializable {


    /**
     * childId : 78
     * childImg : http://ghpprod.oss-cn-qingdao.aliyuncs.com/client/9150721e-9c01-4aa4-847c-aafadd53ceca.jpg
     * childPetName : 小宝
     * courseList : [{"canEvaluate":false,"id":22,"name":"体能构建7-8岁高级班","schedule":0,"seriesName":"万达体能构建","teacherId":22,"totalName":"万达体能构建之 - 体能构建7-8岁高级班","totalTime":1,"usedTime":0,"videoImg":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/course/20170717111751756_99.png"}]
     * userId : 11
     */

    private int childId;
    private String childImg;
    private String childPetName;
    private int userId;
    private List<BabyCourse> courseList;

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public String getChildImg() {
        return childImg;
    }

    public void setChildImg(String childImg) {
        this.childImg = childImg;
    }

    public String getChildPetName() {
        return childPetName;
    }

    public void setChildPetName(String childPetName) {
        this.childPetName = childPetName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<BabyCourse> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<BabyCourse> courseList) {
        this.courseList = courseList;
    }

}
