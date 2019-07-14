package com.park61.moduel.childtest.bean;

/**
 * Created by shubei on 2017/9/19.
 */

public class EaItemDetailsBean {

    /**
     * canEa : false
     * content : <p>啊啊啊啊啊</p><p><img src="http://park61.oss-cn-zhangjiakou.aliyuncs.com/content/20170915/1505474548829042212.png?x-oss-process=style/logo_img" title="1505474548829042212.png" alt="image.png"/></p>
     * eaLowAgeLimit : 0
     * eaSysId : 1
     * eaSysName : DAP测评(适合3-6岁)
     * eaUppAgeLimit : 3
     * id : 3
     * imgUrl : http://park61.oss-cn-zhangjiakou.aliyuncs.com/test/20170909151521697_90.png
     * name : 学习动机
     * remarks :
     * sort : 3
     */

    private boolean canEa;
    private String content;
    private int eaLowAgeLimit;
    private int eaSysId;
    private String eaSysName;
    private int eaUppAgeLimit;
    private int id;
    private String imgUrl;
    private String name;
    private String remarks;
    private int sort;

    public boolean isCanEa() {
        return canEa;
    }

    public void setCanEa(boolean canEa) {
        this.canEa = canEa;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getEaLowAgeLimit() {
        return eaLowAgeLimit;
    }

    public void setEaLowAgeLimit(int eaLowAgeLimit) {
        this.eaLowAgeLimit = eaLowAgeLimit;
    }

    public int getEaSysId() {
        return eaSysId;
    }

    public void setEaSysId(int eaSysId) {
        this.eaSysId = eaSysId;
    }

    public String getEaSysName() {
        return eaSysName;
    }

    public void setEaSysName(String eaSysName) {
        this.eaSysName = eaSysName;
    }

    public int getEaUppAgeLimit() {
        return eaUppAgeLimit;
    }

    public void setEaUppAgeLimit(int eaUppAgeLimit) {
        this.eaUppAgeLimit = eaUppAgeLimit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
