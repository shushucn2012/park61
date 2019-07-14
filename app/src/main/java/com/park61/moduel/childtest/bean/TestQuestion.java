package com.park61.moduel.childtest.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shushucn2012 on 2017/2/27.
 */
public class TestQuestion implements Serializable {

    private int id;
    private int eaItemId;
    private int eaItemSubId;
    private int eaSysId;
    private String content;
    private String eaItemSubName;
    private List<TestAnswers> answers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEaItemId() {
        return eaItemId;
    }

    public void setEaItemId(int eaItemId) {
        this.eaItemId = eaItemId;
    }

    public int getEaItemSubId() {
        return eaItemSubId;
    }

    public void setEaItemSubId(int eaItemSubId) {
        this.eaItemSubId = eaItemSubId;
    }

    public int getEaSysId() {
        return eaSysId;
    }

    public void setEaSysId(int eaSysId) {
        this.eaSysId = eaSysId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEaItemSubName() {
        return eaItemSubName;
    }

    public void setEaItemSubName(String eaItemSubName) {
        this.eaItemSubName = eaItemSubName;
    }

    public List<TestAnswers> getAnswers() {
        return answers;
    }

    public void setAnswers(List<TestAnswers> answers) {
        this.answers = answers;
    }
}
