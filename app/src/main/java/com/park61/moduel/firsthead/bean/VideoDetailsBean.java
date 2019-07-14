package com.park61.moduel.firsthead.bean;

import java.util.List;

/**
 * Created by shubei on 2017/6/15.
 */

public class VideoDetailsBean {


    /**
     * aliVideoCode : 172f420b305e4ed3a01f51bc64425aa9
     * browseNum : 1
     * classifyType : 1
     * commentNum : 1
     * coverUrl : http://ghpprod.oss-cn-qingdao.aliyuncs.com/grf/20170428_881754.png
     * createBy : 7
     * createDate : 1497514548000
     * domain : 0
     * focus : false
     * id : 2
     * issuerHeadPic : http://ghpprod.oss-cn-qingdao.aliyuncs.com/grf/20170428_881754.png
     * issuerName : 18696167897
     * itemMediaList : [{"id":2,"mediaId":"172f420b305e4ed3a01f51bc64425aa9","mediaUrl":"http://ghpprod.oss-cn-qingdao.aliyuncs.com/grf/20170428_881754.png"}]
     * playAuth : eyJTZWN1cml0eVRva2VuIjoiQ0FJU3VBSjFxNkZ0NUIyeWZTaklwNVBtTHV1QjJ1NUk1SlhTVjF6MnMzUmtacmRWamFUT2lEejJJSHBLZVhkdUFlQVhzL28wbW1oWjcvWVlsck1xRU1jWkhSZVpOcEFvdDg0SHJWbi9KcExGc3QycDZyOEpqc1V4aU1jc3FWMnBzdlhKYXNEVkVmbkFHSjcwR1VpbSt3WjN4YnpsRHh2QU8zV3VMWnlPajdOK2M5MFRSWFBXUkRGYUJkQlFWRTBBenNvR0xpbnBNZis4UHdURG4zYlFiaTV0cGhFdXNXZDIrSVc1ek1yK2pCL0Nsdy9XeCtRUHRwenRBWlcxRmI0T1dxMXlTTkNveHVkN1c3UGMyU3BMa1hodytieHhrYlpQOUVXczNMamZJU0VJczByZGJiV0VyNFV4ZFZVbFB2VmxJY01lOHFpZ3o4OGZrL2ZJaW9INnh5eEtPZXhvU0NuRlRPaWl1cENlUnI3M2FJNXBKT3VsWUNxVmc0bmZiSU9KbWdjbGNHOGRDZ1JHUU5Nb01VUnNFaVlyVGp6SzJ3RlFQcVFob0d3YWdBRXVnZitHcWdYcmdCcnJKU2RINE9HaXJQNkJQbFN3bnN1c3ErQ2x1NkFWdElIUERwRUE4ZkIrdVBwNkV1YmRNVTlPZXJrcWxFUmZGcXoyT3lEMm14NHV6QVprNHZvdmpoc2NiRldRdkhJNUErQWRHOWt6bzJDVkh2ZUpXMU02eTZSZk45YUN1TUpWbmZkdkgvTTRpNFdnN21taUZxUVB2cTl2bzdzZVhETG16dz09IiwiQXV0aEluZm8iOiJ7XCJFeHBpcmVUaW1lXCI6XCIyMDE3LTA2LTE1VDA4OjMxOjU2WlwiLFwiTWVkaWFJZFwiOlwiMTcyZjQyMGIzMDVlNGVkM2EwMWY1MWJjNjQ0MjVhYTlcIixcIlNpZ25hdHVyZVwiOlwiVWNUWWdoYTFjSzdQTlNXRXJXbUdpWTZ6ejFBPVwifSIsIlZpZGVvTWV0YSI6eyJTdGF0dXMiOiJOb3JtYWwiLCJWaWRlb0lkIjoiMTcyZjQyMGIzMDVlNGVkM2EwMWY1MWJjNjQ0MjVhYTkiLCJUaXRsZSI6IuWwj+avm+mptDEiLCJDb3ZlclVSTCI6Imh0dHA6Ly92aWRlby53YXRlcm1vdG8uY29tL3NuYXBzaG90LzE3MmY0MjBiMzA1ZTRlZDNhMDFmNTFiYzY0NDI1YWE5MDAwMDIuanBnP2F1dGhfa2V5PTE0OTc1MTkwMTYtMC0wLTUwNjU2NWU0MDM0NmU5MzhhMTg4N2IyOTJiNzA1OTFmIiwiRHVyYXRpb24iOjE0OS4yOH0sIkFjY2Vzc0tleUlkIjoiU1RTLkZQU2VRNTcxaVNXOVV6R1d0MWo4eWJhZWoiLCJQbGF5RG9tYWluIjoidmlkZW8ud2F0ZXJtb3RvLmNvbSIsIkFjY2Vzc0tleVNlY3JldCI6IjJ4RXdpUk1tS0RadFRGRmVKTjFlb3lndFVXeHJadUJyS2FveUFIa1g3NGJMIiwiUmVnaW9uIjoiY24tc2hhbmdoYWkiLCJDdXN0b21lcklkIjoxMzA0MDM1MzMwMTI4NDI2fQ==
     * praiseNum : 1
     * status : 0
     * summary : roger deploy
     * title : 小毛驴
     * userId : 7
     * videoSize : 9953
     * videoTime : 120
     */

    private String aliVideoCode;
    private int browseNum;
    private int classifyType;
    private int commentNum;
    private String coverUrl;
    private String createBy;
    private long createDate;
    private int domain;
    private boolean focus;
    private boolean praise;
    private long id;
    private String issuerHeadPic;
    private String issuerName;
    private String playAuth;
    private int praiseNum;
    private int status;
    private String summary;
    private String title;
    private long userId;
    private int videoSize;
    private int videoTime;
    private List<ItemMediaListBean> itemMediaList;
    private boolean focusUser;

    public String getAliVideoCode() {
        return aliVideoCode;
    }

    public void setAliVideoCode(String aliVideoCode) {
        this.aliVideoCode = aliVideoCode;
    }

    public int getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(int browseNum) {
        this.browseNum = browseNum;
    }

    public int getClassifyType() {
        return classifyType;
    }

    public void setClassifyType(int classifyType) {
        this.classifyType = classifyType;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getDomain() {
        return domain;
    }

    public void setDomain(int domain) {
        this.domain = domain;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPlayAuth() {
        return playAuth;
    }

    public void setPlayAuth(String playAuth) {
        this.playAuth = playAuth;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(int videoSize) {
        this.videoSize = videoSize;
    }

    public int getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(int videoTime) {
        this.videoTime = videoTime;
    }

    public List<ItemMediaListBean> getItemMediaList() {
        return itemMediaList;
    }

    public void setItemMediaList(List<ItemMediaListBean> itemMediaList) {
        this.itemMediaList = itemMediaList;
    }

    public boolean isPraise() {
        return praise;
    }

    public void setPraise(boolean praise) {
        this.praise = praise;
    }

    public boolean isFocusUser() {
        return focusUser;
    }

    public void setFocusUser(boolean focusUser) {
        this.focusUser = focusUser;
    }

    public static class ItemMediaListBean {
        /**
         * id : 2
         * mediaId : 172f420b305e4ed3a01f51bc64425aa9
         * mediaUrl : http://ghpprod.oss-cn-qingdao.aliyuncs.com/grf/20170428_881754.png
         */

        private int id;
        private String mediaId;
        private String mediaUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public String getMediaUrl() {
            return mediaUrl;
        }

        public void setMediaUrl(String mediaUrl) {
            this.mediaUrl = mediaUrl;
        }
    }

}
