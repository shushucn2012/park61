package com.park61.moduel.firsthead.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/11/14.
 */

public class SearchHotBean implements Serializable {
    private String keyword;
private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
