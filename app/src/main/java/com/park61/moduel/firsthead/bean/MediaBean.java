package com.park61.moduel.firsthead.bean;

/**
 * Created by shubei on 2017/6/15.
 */

public class MediaBean {

    private String mediaId;//媒体id
    private String mediaUrl;//媒体地址

    public MediaBean() {
    }

    public MediaBean(String mediaId, String mediaUrl) {
        this.mediaId = mediaId;
        this.mediaUrl = mediaUrl;
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
