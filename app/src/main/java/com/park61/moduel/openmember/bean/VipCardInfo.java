package com.park61.moduel.openmember.bean;

import java.io.Serializable;

/**
 * 会员卡信息
 */
public class VipCardInfo implements Serializable {
    private String name;//会员卡名称
    private String time;//交易时间
    private String value;//购买时长
    private int colorType;//颜色类型：1红色，2黑色

    public int getColorType() {
        return colorType;
    }

    public void setColorType(int colorType) {
        this.colorType = colorType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
