package com.park61.moduel.firsthead.bean;

/**
 * Created by shubei on 2017/6/12.
 */

public class FirstHeadChildBean {


    /**
     * authorName : 61区
     * commentNum : 0
     * contentType : 0
     * coverImg1 : http://park61.oss-cn-zhangjiakou.aliyuncs.com/content/20170619/1497862889272009535.jpg
     * coverImg2 : http://park61.oss-cn-zhangjiakou.aliyuncs.com/content/20170619/1497862889272009535.jpg
     * coverImg3 : http://park61.oss-cn-zhangjiakou.aliyuncs.com/content/20170619/1497862889272009535.jpg
     * coverType : 2
     * id : 5
     * title : 捷瑞_三张图
     * viewNum : 62
     */

    private String authorName;
    private int commentNum;
    private int contentType;
    private String coverImg1;
    private String coverImg2;
    private String coverImg3;
    private int coverType;
    private long id;
    private String title;
    private int viewNum;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getCoverImg1() {
        return coverImg1;
    }

    public void setCoverImg1(String coverImg1) {
        this.coverImg1 = coverImg1;
    }

    public String getCoverImg2() {
        return coverImg2;
    }

    public void setCoverImg2(String coverImg2) {
        this.coverImg2 = coverImg2;
    }

    public String getCoverImg3() {
        return coverImg3;
    }

    public void setCoverImg3(String coverImg3) {
        this.coverImg3 = coverImg3;
    }

    public int getCoverType() {
        return coverType;
    }

    public void setCoverType(int coverType) {
        this.coverType = coverType;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }
}
