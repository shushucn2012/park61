package com.park61.moduel.dreamhouse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 梦想详情
 */
public class DreamDetailBean implements Serializable {
    private String classId;	//小课类型
    private int type;	//类型
    private String typeName;	//类型名称(标签)
    private int praiseNum;	//点赞数
    private Long id;
    private String content;		//内容
    private int commentNum;	//评论数
    private String title;	//标题
    private List<SameDreamInfo> predictionList;//同行人列表
    private String coverPic;	//背景图片
    private Long userId;		// 点赞用户Id
    private String userName; // 用户名
    private boolean hadPraised;	//是否点赞
    private String createDate;	//创建时间
    private List<String> picList;//同行人头像
    private String headUrl;

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SameDreamInfo> getPredictionList() {
        return predictionList;
    }

    public void setPredictionList(List<SameDreamInfo> predictionList) {
        this.predictionList = predictionList;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isHadPraised() {
        return hadPraised;
    }

    public void setHadPraised(boolean hadPraised) {
        this.hadPraised = hadPraised;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }
}
