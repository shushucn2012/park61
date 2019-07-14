package com.park61.moduel.childtest.bean;

import java.io.Serializable;

/**
 * Created by shushucn2012 on 2017/3/2.
 */
public class EaSysBean implements Serializable {

    private String id;
    private boolean canEa;
    private int eaSysId;
    private String name;

    public boolean isCanEa() {
        return canEa;
    }

    public void setCanEa(boolean canEa) {
        this.canEa = canEa;
    }

    public int getEaSysId() {
        return eaSysId;
    }

    public void setEaSysId(int eaSysId) {
        this.eaSysId = eaSysId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
