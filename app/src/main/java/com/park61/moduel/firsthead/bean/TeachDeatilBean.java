package com.park61.moduel.firsthead.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/11/16.
 */

public class TeachDeatilBean implements Serializable {

private String bigPictureUrl;
    private String content;
    private String description;
    private int fansNum;
    private String headPictureUrl;
    private int id;
    private String name;
    private Boolean focus;

    public String getContent() {
        return content;
    }

    public int getFansNum() {
        return fansNum;
    }

    public int getId() {
        return id;
    }

    public String getBigPictureUrl() {
        return bigPictureUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getHeadPictureUrl() {
        return headPictureUrl;
    }

    public String getName() {
        return name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBigPictureUrl(String bigPictureUrl) {
        this.bigPictureUrl = bigPictureUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public void setHeadPictureUrl(String headPictureUrl) {
        this.headPictureUrl = headPictureUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFocus() {
        return focus;
    }

    public void setFocus(Boolean focus) {
        this.focus = focus;
    }
}
