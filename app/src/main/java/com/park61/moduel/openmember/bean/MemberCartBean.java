package com.park61.moduel.openmember.bean;

import java.io.Serializable;

/**
 * 会员卡类型数据
 */
public class MemberCartBean implements Serializable{
    private int type;//会员卡卡类型，1：次卡，2：小时卡
    private String times;//到期时间，剩余次数
    private String pic;//会员卡图片
    private String name;
    private String hasChild;
    private String cardId;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getHasChild() {
        return hasChild;
    }

    public void setHasChild(String hasChild) {
        this.hasChild = hasChild;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
