package com.park61.moduel.childtest.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/12/8.
 */

public class StanderResultItemListBean implements Serializable {
    private int delFlag;
    private int id;
    private String intro;
    private String itemContent;
    private String itemName;

    public int getDelFlag() {
        return delFlag;
    }

    public int getId() {
        return id;
    }

    public String getIntro() {
        return intro;
    }

    public String getItemContent() {
        return itemContent;
    }

    public String getItemName() {
        return itemName;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}
