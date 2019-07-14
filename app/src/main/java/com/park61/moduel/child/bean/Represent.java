package com.park61.moduel.child.bean;

import java.io.Serializable;

/**
 * 测评信息
 */
public class Represent implements Serializable {
    private String property;//测评类型
    private String content;//测评内容
    private String name;//榜样人物

    private Long eaStdResultId;
    private Long gender;
    private Long id;
    private String picUrl;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEaStdResultId() {
        return eaStdResultId;
    }

    public void setEaStdResultId(Long eaStdResultId) {
        this.eaStdResultId = eaStdResultId;
    }

    public Long getGender() {
        return gender;
    }

    public void setGender(Long gender) {
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
