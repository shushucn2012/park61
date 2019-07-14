package com.park61.moduel.shophome.bean;

import java.io.Serializable;

/**
 * Created by shushucn2012 on 2017/3/14.
 */
public class PhotoItem implements Serializable{

    private long id;
    private String resourceUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
}
