package com.park61.moduel.grouppurchase.bean;


public class GroupGoodsCombine {
    private GroupProductLimit goodsLeft;
    private GroupProductLimit goodsRight;

    public GroupGoodsCombine() {

    }

    public GroupGoodsCombine(GroupProductLimit goodsLeft, GroupProductLimit goodsRight) {
        this.setGoodsLeft(goodsLeft);
        this.setGoodsRight(goodsRight);
    }

    public GroupProductLimit getGoodsLeft() {
        return goodsLeft;
    }

    public void setGoodsLeft(GroupProductLimit goodsLeft) {
        this.goodsLeft = goodsLeft;
    }

    public GroupProductLimit getGoodsRight() {
        return goodsRight;
    }

    public void setGoodsRight(GroupProductLimit goodsRight) {
        this.goodsRight = goodsRight;
    }
}
