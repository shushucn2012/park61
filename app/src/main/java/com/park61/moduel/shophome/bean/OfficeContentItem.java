package com.park61.moduel.shophome.bean;

import java.io.Serializable;

/**
 * Created by HP on 2017/3/13.
 */
public class OfficeContentItem implements Serializable {
    private Long id;
    private String resourceUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
}
