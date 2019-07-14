package com.park61.moduel.childtest.bean;

import java.util.List;

/**
 * Created by shubei on 2017/12/5.
 */

public class TestBigQuestion {

    private String eaItemSubName;
    private int eaServiceId;
    private int eaItemId;
    private int isSingle = 0;
    private List<TestQuestionItem> listEaSubject;

    public String getEaItemSubName() {
        return eaItemSubName;
    }

    public void setEaItemSubName(String eaItemSubName) {
        this.eaItemSubName = eaItemSubName;
    }

    public int getEaServiceId() {
        return eaServiceId;
    }

    public void setEaServiceId(int eaServiceId) {
        this.eaServiceId = eaServiceId;
    }

    public int getEaItemId() {
        return eaItemId;
    }

    public int getIsSingle() {
        return isSingle;
    }

    public void setIsSingle(int isSingle) {
        this.isSingle = isSingle;
    }

    public void setEaItemId(int eaItemId) {
        this.eaItemId = eaItemId;
    }

    public List<TestQuestionItem> getListEaSubject() {
        return listEaSubject;
    }

    public void setListEaSubject(List<TestQuestionItem> listEaSubject) {
        this.listEaSubject = listEaSubject;
    }
}
