package com.park61.moduel.sales.bean;

/**
 * Created by shubei on 2017/11/15.
 */

public class TabBean {

    private int moduleId;
    private String name;
    private boolean isChosen;

    public TabBean() {
    }

    public TabBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }
}
