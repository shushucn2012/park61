package com.park61.moduel.firsthead.bean;

/**
 * Created by shubei on 2017/7/5.
 */

public class BlogerInfoBean {


    /**
     * expert : false
     * fansCnt : 2
     * focus : false
     * focusCnt : 1
     * userId : 6
     * userMobile : 17103030303
     * userName : 你猜
     */

    private boolean expert;
    private int fansCnt;
    private boolean focus;
    private int focusCnt;
    private int userId;
    private String userMobile;
    private String userName;
    private String resume;
    private String photo;

    public boolean isExpert() {
        return expert;
    }

    public void setExpert(boolean expert) {
        this.expert = expert;
    }

    public int getFansCnt() {
        return fansCnt;
    }

    public void setFansCnt(int fansCnt) {
        this.fansCnt = fansCnt;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public int getFocusCnt() {
        return focusCnt;
    }

    public void setFocusCnt(int focusCnt) {
        this.focusCnt = focusCnt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
