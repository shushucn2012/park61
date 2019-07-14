package com.park61.moduel.sales.bean;

/**
 * Created by shubei on 2017/5/19.
 */

public class MySalesTemplete {

    private String templeteCode;
    private String templeteData;
    private TempleteHead templeteHead;

    public MySalesTemplete() {
    }

    public MySalesTemplete(String templeteCode, String templeteData) {
        this.templeteCode = templeteCode;
        this.templeteData = templeteData;
    }

    public String getTempleteCode() {
        return templeteCode;
    }

    public void setTempleteCode(String templeteCode) {
        this.templeteCode = templeteCode;
    }

    public String getTempleteData() {
        return templeteData;
    }

    public void setTempleteData(String templeteData) {
        this.templeteData = templeteData;
    }

    public TempleteHead getTempleteHead() {
        return templeteHead;
    }

    public void setTempleteHead(TempleteHead templeteHead) {
        this.templeteHead = templeteHead;
    }

    public class TempleteHead {
        private String moreBtnCanShow;
        private String titlePicUrl;

        public TempleteHead(String moreBtnCanShow, String titlePicUrl) {
            this.moreBtnCanShow = moreBtnCanShow;
            this.titlePicUrl = titlePicUrl;
        }

        public TempleteHead() {
        }

        public String getMoreBtnCanShow() {
            return moreBtnCanShow;
        }

        public void setMoreBtnCanShow(String moreBtnCanShow) {
            this.moreBtnCanShow = moreBtnCanShow;
        }

        public String getTitlePicUrl() {
            return titlePicUrl;
        }

        public void setTitlePicUrl(String titlePicUrl) {
            this.titlePicUrl = titlePicUrl;
        }
    }
}
