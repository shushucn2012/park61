package com.park61.moduel.firsthead.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shubei on 2017/8/31.
 */

public class TeachClassNotice implements Serializable {


    /**
     * classId : 6
     * className : 哈弗神童
     * commentNum : 5
     * headPic : http://park61.oss-cn-zhangjiakou.aliyuncs.com/activity/20170711180258759_31.png
     * imgList : [{"id":1,"imgUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/banner/20170804115307835_521.jpg","noticeId":1},{"id":2,"imgUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/banner/20170804115823111_789.mp3","noticeId":1}]
     * noticeId : 1
     * noticeType : 2
     * praiseNum : 2
     * title :
     * userId : 8
     * userName : 爱丽尔的测试名称
     * viewNum : 146
     */

    private int classId;
    private String className;
    private int commentNum;
    private String headPic;
    private long noticeId;
    private int noticeType;
    private int praiseNum;
    private String title;
    private String content;
    private int userId;
    private String userName;
    private int viewNum;
    private String createDate;
    private List<ImgListBean> imgList;
    private boolean praiseStatus;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(long noticeId) {
        this.noticeId = noticeId;
    }

    public int getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(int noticeType) {
        this.noticeType = noticeType;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public List<ImgListBean> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgListBean> imgList) {
        this.imgList = imgList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean isPraiseStatus() {
        return praiseStatus;
    }

    public void setPraiseStatus(boolean praiseStatus) {
        this.praiseStatus = praiseStatus;
    }

    public static class ImgListBean implements Serializable{
        /**
         * id : 1
         * imgUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/banner/20170804115307835_521.jpg
         * noticeId : 1
         */

        private long id;
        private String imgUrl;
        private int noticeId;
        private boolean praiseStatus;
        private boolean collectStatus;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getNoticeId() {
            return noticeId;
        }

        public void setNoticeId(int noticeId) {
            this.noticeId = noticeId;
        }

        public boolean isPraiseStatus() {
            return praiseStatus;
        }

        public void setPraiseStatus(boolean praiseStatus) {
            this.praiseStatus = praiseStatus;
        }

        public boolean isCollectStatus() {
            return collectStatus;
        }

        public void setCollectStatus(boolean collectStatus) {
            this.collectStatus = collectStatus;
        }
    }
}
