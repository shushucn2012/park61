package com.park61.moduel.dreamhouse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 我的梦想条目信息
 */
public class DreamItemInfo implements Serializable {
    private String content;
    private String coverPic;//梦想图片
    private String headUrl;//用户图像url
    private Long id;//梦想id
    private String title;//标题
    private String requirementLabel;//标签
    private Long userId;
    private String userName;
    private String statusName;//梦想状态名称
    private String status;//梦想状态：0，未审核 1审核通过 2审核未通过 10 造梦中
    //11梦想成真 12遗憾错过 13 造梦成功
    private List<String> picList;//同梦人头像集合
    private Long actTemplateId;
    private int predictionNum;//同梦人数量
    private Long requirementPredictionId;//梦想预报名ID
    private String officeName;//店铺名称
    private String expectDate;//开始日期

    public String getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(String expectDate) {
        this.expectDate = expectDate;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Long getRequirementPredictionId() {
        return requirementPredictionId;
    }

    public void setRequirementPredictionId(Long requirementPredictionId) {
        this.requirementPredictionId = requirementPredictionId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getPredictionNum() {
        return predictionNum;
    }

    public void setPredictionNum(int predictionNum) {
        this.predictionNum = predictionNum;
    }

    public Long getActTemplateId() {
        return actTemplateId;
    }

    public void setActTemplateId(Long actTemplateId) {
        this.actTemplateId = actTemplateId;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequirementLabel() {
        return requirementLabel;
    }

    public void setRequirementLabel(String requirementLabel) {
        this.requirementLabel = requirementLabel;
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
}
