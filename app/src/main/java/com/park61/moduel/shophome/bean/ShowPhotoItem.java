package com.park61.moduel.shophome.bean;

import java.io.Serializable;

/**
 * Created by HP on 2017/3/9.
 */
public class ShowPhotoItem implements Serializable {
    private Long id;
    private String picUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
