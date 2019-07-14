package com.park61.moduel.firsthead.bean;

import com.park61.moduel.sales.bean.MySalesTemplete;

import java.util.List;

/**
 * Created by shubei on 2017/6/12.
 */

public class FirstHeadBean {

    private int classifyType;// 资源类型;0:图文;1:视频;2:专栏;3:音频
    private int composeType;//1 一小图；2 一大图；3 三小图
    private String issuerName;// 发布者名称
    private long userId;//发布者id
    private String issuerHeadPic;//发布者头像地址
    private long createDate;// 发布时间
    private long itemId;//id
    private String itemTitle;//标题
    private long queryDate;// 查询时间
    private List<MediaBean> itemMediaList;//图片列表
    private int itemReadNum;//阅读数
    private int itemCommentNum;//评论数
    private String itemTag;//标签
    private String dateTag;//时间标签
    private String internalLink;//推广链接
    private boolean expert;
    private int contentType; // 封面图片类型：0单张大图，1单张小图，2三张小图
    private int commentNum;
    private String coverImg1;
    private String coverImg2;
    private String coverImg3;
    private int viewNum;
    private String itemtype;

    public FirstHeadBean() {
    }

    public FirstHeadBean(int classifyType) {
        this.classifyType = classifyType;
    }

    public int getClassifyType() {
        return classifyType;
    }

    public void setClassifyType(int classifyType) {
        this.classifyType = classifyType;
    }

    public int getComposeType() {
        return composeType;
    }

    public void setComposeType(int composeType) {
        this.composeType = composeType;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getIssuerHeadPic() {
        return issuerHeadPic;
    }

    public void setIssuerHeadPic(String issuerHeadPic) {
        this.issuerHeadPic = issuerHeadPic;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public long getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(long queryDate) {
        this.queryDate = queryDate;
    }

    private MySalesTemplete templete;

    public FirstHeadBean(MySalesTemplete templete) {
        this.templete = templete;
    }

    public MySalesTemplete getTemplete() {
        return templete;
    }

    public void setTemplete(MySalesTemplete templete) {
        this.templete = templete;
    }

    public List<MediaBean> getItemMediaList() {
        return itemMediaList;
    }

    public void setItemMediaList(List<MediaBean> itemMediaList) {
        this.itemMediaList = itemMediaList;
    }

    public int getItemReadNum() {
        return itemReadNum;
    }

    public void setItemReadNum(int itemReadNum) {
        this.itemReadNum = itemReadNum;
    }

    public int getItemCommentNum() {
        return itemCommentNum;
    }

    public void setItemCommentNum(int itemCommentNum) {
        this.itemCommentNum = itemCommentNum;
    }

    public String getItemTag() {
        return itemTag;
    }

    public void setItemTag(String itemTag) {
        this.itemTag = itemTag;
    }

    public String getDateTag() {
        return dateTag;
    }

    public void setDateTag(String dateTag) {
        this.dateTag = dateTag;
    }

    public String getInternalLink() {
        return internalLink;
    }

    public void setInternalLink(String internalLink) {
        this.internalLink = internalLink;
    }

    public boolean isExpert() {
        return expert;
    }

    public void setExpert(boolean expert) {
        this.expert = expert;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getCoverImg3() {
        return coverImg3;
    }

    public String getCoverImg2() {
        return coverImg2;
    }

    public int getViewNum() {
        return viewNum;
    }

    public String getCoverImg1() {
        return coverImg1;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
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

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }
}
