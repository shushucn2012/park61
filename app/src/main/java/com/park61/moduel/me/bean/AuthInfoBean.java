package com.park61.moduel.me.bean;

import java.io.Serializable;

/**
 * Created by shubei on 2017/7/6.
 */

public class AuthInfoBean implements Serializable {


    /**
     * auditNoticeSign : 0
     * auditOpinion : pass
     * auditTime : 1498812917000
     * domainId : 1,2
     * evidence : evidence
     * expertName : roger
     * focus : false
     * identityBackUrl : identityBackUrl
     * identityFrontUrl : identityFrontUrl
     * identityNo : 12345
     * resume : expert
     * status : 1
     * userId : 7
     */

    private int auditNoticeSign;
    private String auditOpinion;
    private long auditTime;
    private String domainId;
    private String evidence;
    private String expertName;
    private boolean focus;
    private String identityBackUrl;
    private String identityFrontUrl;
    private String identityNo;
    private String resume;
    private int status;
    private long userId;

    public int getAuditNoticeSign() {
        return auditNoticeSign;
    }

    public void setAuditNoticeSign(int auditNoticeSign) {
        this.auditNoticeSign = auditNoticeSign;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    public long getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(long auditTime) {
        this.auditTime = auditTime;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getExpertName() {
        return expertName;
    }

    public void setExpertName(String expertName) {
        this.expertName = expertName;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public String getIdentityBackUrl() {
        return identityBackUrl;
    }

    public void setIdentityBackUrl(String identityBackUrl) {
        this.identityBackUrl = identityBackUrl;
    }

    public String getIdentityFrontUrl() {
        return identityFrontUrl;
    }

    public void setIdentityFrontUrl(String identityFrontUrl) {
        this.identityFrontUrl = identityFrontUrl;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
