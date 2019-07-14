package com.park61.moduel.shophome.bean;

import java.io.Serializable;

/**
 * Created by HP on 2017/3/17.
 */
public class OfficeContentComment implements Serializable {
    private String content;//评论内容
    private UserKinship userKinship;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserKinship getUserKinship() {
        return userKinship;
    }

    public void setUserKinship(UserKinship userKinship) {
        this.userKinship = userKinship;
    }
}
