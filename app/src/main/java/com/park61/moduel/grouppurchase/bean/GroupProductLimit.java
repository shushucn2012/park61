package com.park61.moduel.grouppurchase.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class GroupProductLimit implements Serializable {
    // 图片
    private String pic;
    // 商品名称
    private String name;
    //拼团价
    private BigDecimal fightGroupPrice;
    private BigDecimal oldPrice;
    private Long pmInfoId;
    private int personNum;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    public Long getPmInfoId() {
        return pmInfoId;
    }

    public void setPmInfoId(Long pmInfoId) {
        this.pmInfoId = pmInfoId;
    }

    public BigDecimal getFightGroupPrice() {
        return fightGroupPrice;
    }

    public void setFightGroupPrice(BigDecimal fightGroupPrice) {
        this.fightGroupPrice = fightGroupPrice;
    }

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
