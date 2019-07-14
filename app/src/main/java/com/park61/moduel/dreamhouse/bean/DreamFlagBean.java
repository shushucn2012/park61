package com.park61.moduel.dreamhouse.bean;

import java.io.Serializable;

/**
 * Created by shushucn2012 on 2017/1/11.
 */
public class DreamFlagBean implements Serializable{

    private String id;
    private String name;
    private boolean isChosen;

    public DreamFlagBean(String id, String name) {
        this.id = id;
        this.name = name;
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

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }
}
