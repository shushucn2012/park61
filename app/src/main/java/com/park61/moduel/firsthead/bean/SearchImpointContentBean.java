package com.park61.moduel.firsthead.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/11/14.
 */

public class SearchImpointContentBean implements Serializable {

    private  String authorName;
    private String content;
    private int contentType;
    private String coverImg1;
    private String coverImg2;
    private String coverImg3;
    private int coverType;
    private int id;
    private int praiseNum;
    private int shareNum;
    private String title;
    private int viewNum;
    private int commentNum;
    private String authorPic;
    private String intro;

    public int getContentType() {
        return contentType;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getContent() {
        return content;
    }

    public int getCoverType() {
        return coverType;
    }

    public int getId() {
        return id;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public int getShareNum() {
        return shareNum;
    }

    public int getViewNum() {
        return viewNum;
    }

    public String getCoverImg1() {
        return coverImg1;
    }

    public String getCoverImg2() {
        return coverImg2;
    }

    public String getCoverImg3() {
        return coverImg3;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public void setCoverImg1(String coverImg1) {
        this.coverImg1 = coverImg1;
    }

    public void setCoverImg2(String coverImg2) {
        this.coverImg2 = coverImg2;
    }

    public void setCoverImg3(String coverImg3) {
        this.coverImg3 = coverImg3;
    }

    public void setCoverType(int coverType) {
        this.coverType = coverType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setAuthorPic(String authorPic) {
        this.authorPic = authorPic;
    }

    public String getAuthorPic() {
        return authorPic;
    }

    public String getIntro() {
        return intro;
    }
}
