package com.park61.moduel.shophome.bean;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by HP on 2017/3/7.
 */
public class OfficeContentAlbums implements Serializable {
    private Long actTemplateId;
    private ActivityVO activityVO;//游戏信息
    private Requirement requirement;//梦想信息
    private int commentNum;//评论数量
    private String createDate;
    private String desc;
    private Long id;
    private ArrayList<OfficeContentItem> officeContentItemsList;//图片集合
    private int praiseNum;//点赞数量
    private String status;
    private String tagName;
    private String title;
    private int viewNum;

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public Long getActTemplateId() {
        return actTemplateId;
    }

    public void setActTemplateId(Long actTemplateId) {
        this.actTemplateId = actTemplateId;
    }

    public ActivityVO getActivityVO() {
        return activityVO;
    }

    public void setActivityVO(ActivityVO activityVO) {
        this.activityVO = activityVO;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<OfficeContentItem> getOfficeContentItemsList() {
        return officeContentItemsList;
    }

    public void setOfficeContentItemsList(ArrayList<OfficeContentItem> officeContentItemsList) {
        this.officeContentItemsList = officeContentItemsList;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }
}
