package com.park61.moduel.acts.bean;

import java.io.Serializable;

/**
 * Created by shushucn2012 on 2016/7/29.
 */
public class TravellerBean implements Serializable {

    // 主键
    private Long id;

    // 常用联系人姓名
    private String contactsName;

    // 证件类型 (0:身份证;1:护照)
    private Integer credentialsType;

    // 证件号
    private String credentialsNo;

    // 人员类型(0:成人;1:小孩)
    private Integer personType;

    //是否选中
    private boolean isChecked;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public Integer getCredentialsType() {
        return credentialsType;
    }

    public void setCredentialsType(Integer credentialsType) {
        this.credentialsType = credentialsType;
    }

    public String getCredentialsNo() {
        return credentialsNo;
    }

    public void setCredentialsNo(String credentialsNo) {
        this.credentialsNo = credentialsNo;
    }

    public Integer getPersonType() {
        return personType;
    }

    public void setPersonType(Integer personType) {
        this.personType = personType;
    }


    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
