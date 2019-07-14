package com.park61.moduel.shophome.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shushucn2012 on 2017/3/14.
 */
public class TempleteData implements Serializable {

    private String actTemplateId;
    private String createDate;
    private String desc;
    private long id;
    private String status;
    private String tagName;
    private String title;
    private String viewNum;
    private List<OfficeContentItem> officeContentItemsList;

    public String getActTemplateId() {
        return actTemplateId;
    }

    public void setActTemplateId(String actTemplateId) {
        this.actTemplateId = actTemplateId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getViewNum() {
        return viewNum;
    }

    public void setViewNum(String viewNum) {
        this.viewNum = viewNum;
    }

    public List<OfficeContentItem> getOfficeContentItemsList() {
        return officeContentItemsList;
    }

    public void setOfficeContentItemsList(List<OfficeContentItem> officeContentItemsList) {
        this.officeContentItemsList = officeContentItemsList;
    }

}
