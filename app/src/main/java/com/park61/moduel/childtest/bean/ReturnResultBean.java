package com.park61.moduel.childtest.bean;

import java.io.Serializable;

/**
 * Created by shushucn2012 on 2017/2/27.
 */
public class ReturnResultBean implements Serializable {

    private long answerIds;
    private long subjectId;
    private long eaItemSubId;

    public long getEaItemSubId() {
        return eaItemSubId;
    }

    public void setEaItemSubId(long eaItemSubId) {
        this.eaItemSubId = eaItemSubId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public long getAnswerIds() {
        return answerIds;
    }

    public void setAnswerIds(long answerIds) {
        this.answerIds = answerIds;
    }
}
