package com.park61.moduel.childtest.bean;

/**
 * Created by shubei on 2017/12/5.
 */

public class TestQuestionItem {

    private int id;
    private String content;
    private int eaServiceId;
    private int eaItemSubId;
    private String eaItemSubName;
    private int eaItemId;
    private String eaItemName;
    private int eaSysId;
    private int isSingle = 0;
    private boolean hasChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getEaServiceId() {
        return eaServiceId;
    }

    public void setEaServiceId(int eaServiceId) {
        this.eaServiceId = eaServiceId;
    }

    public int getEaItemSubId() {
        return eaItemSubId;
    }

    public void setEaItemSubId(int eaItemSubId) {
        this.eaItemSubId = eaItemSubId;
    }

    public String getEaItemSubName() {
        return eaItemSubName;
    }

    public void setEaItemSubName(String eaItemSubName) {
        this.eaItemSubName = eaItemSubName;
    }

    public int getEaItemId() {
        return eaItemId;
    }

    public void setEaItemId(int eaItemId) {
        this.eaItemId = eaItemId;
    }

    public String getEaItemName() {
        return eaItemName;
    }

    public void setEaItemName(String eaItemName) {
        this.eaItemName = eaItemName;
    }

    public int getEaSysId() {
        return eaSysId;
    }

    public void setEaSysId(int eaSysId) {
        this.eaSysId = eaSysId;
    }

    public int getIsSingle() {
        return isSingle;
    }

    public void setIsSingle(int isSingle) {
        this.isSingle = isSingle;
    }

    public boolean isHasChecked() {
        return hasChecked;
    }

    public void setHasChecked(boolean hasChecked) {
        this.hasChecked = hasChecked;
    }
}
