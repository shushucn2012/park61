package com.park61.moduel.childtest.bean;

import java.util.List;

/**
 * Created by nieyu on 2017/12/8.
 */

public class ResultItemBean {

       private int highScores;
    private int id;
    private int infoIntegration;
    private int infoOutput;
    private int infoReception;
    private String intro;
    private String name;
private List<ResultItemListBean>resultItemList;
    private List<StanderResultItemListBean>standardResultItemList;


    public String getName() {
        return name;
    }

    public String getIntro() {
        return intro;
    }

    public int getHighScores() {
        return highScores;
    }

    public int getId() {
        return id;
    }

    public int getInfoIntegration() {
        return infoIntegration;
    }

    public int getInfoOutput() {
        return infoOutput;
    }

    public int getInfoReception() {
        return infoReception;
    }

    public List<ResultItemListBean> getResultItemList() {
        return resultItemList;
    }

    public List<StanderResultItemListBean> getStandardResultItemList() {
        return standardResultItemList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setHighScores(int highScores) {
        this.highScores = highScores;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInfoIntegration(int infoIntegration) {
        this.infoIntegration = infoIntegration;
    }

    public void setInfoOutput(int infoOutput) {
        this.infoOutput = infoOutput;
    }

    public void setInfoReception(int infoReception) {
        this.infoReception = infoReception;
    }

    public void setResultItemList(List<ResultItemListBean> resultItemList) {
        this.resultItemList = resultItemList;
    }

    public void setStandardResultItemList(List<StanderResultItemListBean> standardResultItemList) {
        this.standardResultItemList = standardResultItemList;
    }
}
