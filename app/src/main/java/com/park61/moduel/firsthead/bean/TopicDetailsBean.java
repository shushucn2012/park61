package com.park61.moduel.firsthead.bean;

import java.util.List;

/**
 * Created by shubei on 2017/6/15.
 */

public class TopicDetailsBean {


    /**
     * classifyType : 0
     * createDate : 1497438880000
     * focus : false
     * imgUrl : http://ghpprod.oss-cn-qingdao.aliyuncs.com/grow/2444C4A5-1CAE-4687-B1C5-39C62B34E808.jpg,http://ghpprod.oss-cn-qingdao.aliyuncs.com/grow/2444C4A5-1CAE-4687-B1C5-39C62B34E808.jpg,
     * issuerDate : 2017-06-14 19:14:40
     * issuerHeadPic : http://ghpprod.oss-cn-qingdao.aliyuncs.com/grf/20170427_532119.jpg
     * issuerName : 17102020202
     * itemDesc : 爱丽尔的帖子啦啦啦啦啦<img src="http://ghpprod.oss-cn-qingdao.aliyuncs.com/grow/2444C4A5-1CAE-4687-B1C5-39C62B34E808.jpg"/>
     * itemGiveUpNum : 0
     * itemId : 5
     * itemMediaList : [{"id":5,"mediaUrl":"http://ghpprod.oss-cn-qingdao.aliyuncs.com/grow/2444C4A5-1CAE-4687-B1C5-39C62B34E808.jpg"},{"id":5,"mediaUrl":"http://ghpprod.oss-cn-qingdao.aliyuncs.com/grow/2444C4A5-1CAE-4687-B1C5-39C62B34E808.jpg"}]
     * itemReadNum : 0
     * itemTitle : 奈落测试5
     */

    private boolean backendUser;
    private int classifyType;
    private long createDate;
    private boolean focus;
    private boolean praise;
    private String imgUrl;
    private String issuerDate;
    private String issuerHeadPic;
    private String issuerName;
    private String itemDesc;
    private int itemGiveUpNum;
    private long itemId; // 点赞数
    private int itemReadNum;
    private String itemTitle;
    private List<ItemMediaListBean> itemMediaList;
    private long userId;
    private boolean focusUser;
    private String summary;

    public int getClassifyType() {
        return classifyType;
    }

    public void setClassifyType(int classifyType) {
        this.classifyType = classifyType;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIssuerDate() {
        return issuerDate;
    }

    public void setIssuerDate(String issuerDate) {
        this.issuerDate = issuerDate;
    }

    public String getIssuerHeadPic() {
        return issuerHeadPic;
    }

    public void setIssuerHeadPic(String issuerHeadPic) {
        this.issuerHeadPic = issuerHeadPic;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public int getItemGiveUpNum() {
        return itemGiveUpNum;
    }

    public void setItemGiveUpNum(int itemGiveUpNum) {
        this.itemGiveUpNum = itemGiveUpNum;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getItemReadNum() {
        return itemReadNum;
    }

    public void setItemReadNum(int itemReadNum) {
        this.itemReadNum = itemReadNum;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public List<ItemMediaListBean> getItemMediaList() {
        return itemMediaList;
    }

    public void setItemMediaList(List<ItemMediaListBean> itemMediaList) {
        this.itemMediaList = itemMediaList;
    }

    public boolean isBackendUser() {
        return backendUser;
    }

    public void setBackendUser(boolean backendUser) {
        this.backendUser = backendUser;
    }

    public boolean isPraise() {
        return praise;
    }

    public void setPraise(boolean praise) {
        this.praise = praise;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isFocusUser() {
        return focusUser;
    }

    public void setFocusUser(boolean focusUser) {
        this.focusUser = focusUser;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public static class ItemMediaListBean {
        /**
         * id : 5
         * mediaUrl : http://ghpprod.oss-cn-qingdao.aliyuncs.com/grow/2444C4A5-1CAE-4687-B1C5-39C62B34E808.jpg
         */

        private int id;
        private String mediaUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMediaUrl() {
            return mediaUrl;
        }

        public void setMediaUrl(String mediaUrl) {
            this.mediaUrl = mediaUrl;
        }
    }
}
