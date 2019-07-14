package com.park61.moduel.address.bean;

import java.io.Serializable;

/**
 * 乡镇信息
 */
public class TownBean implements Serializable {
    private Long id;// id
    private String townName;//乡镇名称
    private Long countyId;//区域ID
    private String name;//乡镇名称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
