package com.park61.moduel.childtest.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/12/8.
 */

public class ResultItemListBean implements Serializable {

private int eaItemSubId;
    private String name;
    private int scores;

    public int getEaItemSubId() {
        return eaItemSubId;
    }

    public int getScores() {
        return scores;
    }

    public String getName() {
        return name;
    }

    public void setEaItemSubId(int eaItemSubId) {
        this.eaItemSubId = eaItemSubId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }
}
