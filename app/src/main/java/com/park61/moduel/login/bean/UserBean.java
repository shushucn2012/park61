package com.park61.moduel.login.bean;

import java.io.Serializable;

public class UserBean implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1579101214020306709L;

    private Long id;// bigint(20)主键id
    private String petName;// varchar(50)昵称
    private String name;// varchar(50)姓名
    private String loginName;// varchar(50)用户名
    private Integer sex;// int(1)性别 1:男 2:女
    private String birthday;// date生日
    private String pictureUrl;// varchar(200)头像链接
    private String mobile;// char(11)手机号
    private String email;// varchar(50)邮箱
    private Long inviter;// bigint(20)邀请人
    private String inviteCode;// varchar(30)邀请码
    private Long cityId;// bigint(20)城市
    private String cityName;// 城市名称
    private String address;// varchar(200)详情地址
    private String signature;// varchar(200)签名
    private String rechargeTypeCode;// 最高会员类型编码
    private String resume;//个人简介
    private boolean expert;//是否专家标识

    private String pwd;// 密码

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getInviter() {
        return inviter;
    }

    public void setInviter(Long inviter) {
        this.inviter = inviter;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRechargeTypeCode() {
        return rechargeTypeCode;
    }

    public void setRechargeTypeCode(String rechargeTypeCode) {
        this.rechargeTypeCode = rechargeTypeCode;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public boolean isExpert() {
        return expert;
    }

    public void setExpert(boolean expert) {
        this.expert = expert;
    }
}
