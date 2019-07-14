package com.park61.moduel.salesafter.bean;

import java.io.Serializable;

/**
 * Created by HP on 2016/12/13.
 */
public class GrfReason implements Serializable {
    private String reason;//退货原因
    private int type;//退货类型

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
