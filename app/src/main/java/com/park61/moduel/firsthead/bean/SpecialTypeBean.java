package com.park61.moduel.firsthead.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/11/17.
 */

public class SpecialTypeBean implements Serializable {

    private String detailImg;
    private String title;
    private int topicId;
    private SpecialContentBean page;
     private String intro;
    public int getTopicId() {
        return topicId;
    }

    public SpecialContentBean getPage() {
        return page;
    }

    public String getDetailImg() {
        return detailImg;
    }

    public String getTitle() {
        return title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
