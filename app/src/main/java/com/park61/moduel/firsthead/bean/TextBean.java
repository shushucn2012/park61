package com.park61.moduel.firsthead.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/11/20.
 */

public class TextBean implements Serializable {

private String content;
    private int id;
    private String title;
    private long updateDate;
    private String updateDateStr;
    private int viewNum;
    private TextAuthorBean author;
    private String coverImg;
    private int praiseNum;
    private String videoId;
    private String videoPlayAuth;
    private String intro;
    private boolean praise;
    private boolean focus;
    private boolean focusUser;
    private String playTotalNum;


    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public boolean getFocus() {
        return focus;
    }

    public boolean getFocusUser() {
        return focusUser;
    }

    public boolean getPraise() {
        return praise;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public void setFocusUser(boolean focusUser) {
        this.focusUser = focusUser;
    }

    public void setPraise(boolean praise) {
        this.praise = praise;
    }

    public int getViewNum() {
        return viewNum;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public String getTitle() {
        return title;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public TextAuthorBean getAuthor() {
        return author;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor(TextAuthorBean author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getVideoPlayAuth() {
        return videoPlayAuth;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setVideoPlayAuth(String videoPlayAuth) {
        this.videoPlayAuth = videoPlayAuth;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPlayTotalNum() {
        return playTotalNum;
    }

    public void setPlayTotalNum(String playTotalNum) {
        this.playTotalNum = playTotalNum;
    }
}
