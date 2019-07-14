package com.park61.moduel.childtest.bean;

import java.util.List;

/**
 * Created by shubei on 2017/12/6.
 */

public class QuestionPage {

    private String batchCode;
    private int eaServiceId;
    private int eaItemId;
    private boolean hasFinished;
    private List<TestBigQuestion> listEaSubject;

    public int getEaServiceId() {
        return eaServiceId;
    }

    public void setEaServiceId(int eaServiceId) {
        this.eaServiceId = eaServiceId;
    }

    public int getEaItemId() {
        return eaItemId;
    }

    public void setEaItemId(int eaItemId) {
        this.eaItemId = eaItemId;
    }

    public boolean isHasFinished() {
        return hasFinished;
    }

    public void setHasFinished(boolean hasFinished) {
        this.hasFinished = hasFinished;
    }

    public List<TestBigQuestion> getListEaSubject() {
        return listEaSubject;
    }

    public void setListEaSubject(List<TestBigQuestion> listEaSubject) {
        this.listEaSubject = listEaSubject;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }
}
