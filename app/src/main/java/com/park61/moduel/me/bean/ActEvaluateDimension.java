package com.park61.moduel.me.bean;

import java.io.Serializable;

/**
 * Created by HP on 2017/2/8.
 */
public class ActEvaluateDimension implements Serializable {
    private long id;
    private String name;
    private int point;

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
