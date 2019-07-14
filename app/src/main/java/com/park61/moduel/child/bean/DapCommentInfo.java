package com.park61.moduel.child.bean;

import java.io.Serializable;

/**
 * 测评记录信息
 */
public class DapCommentInfo implements Serializable {
    private Long eaItemId;
    private String eaItemName;
    private String createDate;
    private String childAge;
    private Long childId;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEaItemId() {
        return eaItemId;
    }

    public void setEaItemId(Long eaItemId) {
        this.eaItemId = eaItemId;
    }

    public String getEaItemName() {
        return eaItemName;
    }

    public void setEaItemName(String eaItemName) {
        this.eaItemName = eaItemName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getChildAge() {
        return childAge;
    }

    public void setChildAge(String childAge) {
        this.childAge = childAge;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }
}
