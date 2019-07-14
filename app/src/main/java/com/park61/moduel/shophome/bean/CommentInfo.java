package com.park61.moduel.shophome.bean;

import java.io.Serializable;

/**
 * 小样图片评论信息
 */
public class CommentInfo implements Serializable {
    private String content;//评论内容
    private Long contentAlbumId;
    private Long contentItemsId;
    private String createDate;//日期
    private Long id;//评论id
    private Long parentId;//回复评论id
    private String showCreateTime;//展示时间
    private Long userId;
    private UserKinship userKinship;
    private OfficeContentComment officeContentComment;

    public OfficeContentComment getOfficeContentComment() {
        return officeContentComment;
    }

    public void setOfficeContentComment(OfficeContentComment officeContentComment) {
        this.officeContentComment = officeContentComment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getContentAlbumId() {
        return contentAlbumId;
    }

    public void setContentAlbumId(Long contentAlbumId) {
        this.contentAlbumId = contentAlbumId;
    }

    public Long getContentItemsId() {
        return contentItemsId;
    }

    public void setContentItemsId(Long contentItemsId) {
        this.contentItemsId = contentItemsId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getShowCreateTime() {
        return showCreateTime;
    }

    public void setShowCreateTime(String showCreateTime) {
        this.showCreateTime = showCreateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserKinship getUserKinship() {
        return userKinship;
    }

    public void setUserKinship(UserKinship userKinship) {
        this.userKinship = userKinship;
    }
}
