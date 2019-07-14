package com.park61.moduel.child.bean;

import java.io.Serializable;

/**
 * Created by shubei on 2017/5/26.
 */

public class VoteApply implements Serializable {

    /**
     * activityEnded : false
     * applyTime : 1495718341000
     * childAge :
     * childBirth : 1493827200000
     * childId : 23
     * childName : 糊糊
     * childPhoto : http://m.61park.cn/images/laz/laz_ower.png
     * id : 7
     * imgUrl : http://ghpprod.oss-cn-qingdao.aliyuncs.com/grf/20170525_156240.png
     * rank : 6
     * userId : 2
     * voteCode : 6
     * voteDeclaration : 12312312312
     * voteId : 1
     * voteNum : 0
     * voted : false
     */
    private String title;
    private String description;
    private String resume;
    private boolean activityEnded;
    private long applyTime;
    private String childAge;
    private long childBirth;
    private int childId;
    private String childName;
    private String childPhoto;
    private int id;
    private String imgUrl;
    private int rank;
    private int userId;
    private int voteCode;
    private String voteDeclaration;
    private long voteId;
    private int voteNum;
    private boolean voted;

    public boolean isActivityEnded() {
        return activityEnded;
    }

    public void setActivityEnded(boolean activityEnded) {
        this.activityEnded = activityEnded;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public String getChildAge() {
        return childAge;
    }

    public void setChildAge(String childAge) {
        this.childAge = childAge;
    }

    public long getChildBirth() {
        return childBirth;
    }

    public void setChildBirth(long childBirth) {
        this.childBirth = childBirth;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildPhoto() {
        return childPhoto;
    }

    public void setChildPhoto(String childPhoto) {
        this.childPhoto = childPhoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVoteCode() {
        return voteCode;
    }

    public void setVoteCode(int voteCode) {
        this.voteCode = voteCode;
    }

    public String getVoteDeclaration() {
        return voteDeclaration;
    }

    public void setVoteDeclaration(String voteDeclaration) {
        this.voteDeclaration = voteDeclaration;
    }

    public long getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
