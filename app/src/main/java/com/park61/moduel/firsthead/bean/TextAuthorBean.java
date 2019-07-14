package com.park61.moduel.firsthead.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/11/20.
 */

public class TextAuthorBean implements Serializable {

    private int id;
    private String userMobile;
    private String userPic;
    private String username;

    public int getId() {
        return id;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public String getUsername() {
        return username;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
}
