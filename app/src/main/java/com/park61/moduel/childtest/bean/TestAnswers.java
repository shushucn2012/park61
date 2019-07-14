package com.park61.moduel.childtest.bean;

import java.io.Serializable;

/**
 * Created by shushucn2012 on 2017/2/27.
 */
public class TestAnswers implements Serializable {

    private long id;
    private long eaSubjectId;
    private String content;
    private Integer score;
    private boolean choosed;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEaSubjectId() {
        return eaSubjectId;
    }

    public void setEaSubjectId(long eaSubjectId) {
        this.eaSubjectId = eaSubjectId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public boolean isChoosed() {
        return choosed;
    }

    public void setChoosed(boolean choosed) {
        this.choosed = choosed;
    }
}
