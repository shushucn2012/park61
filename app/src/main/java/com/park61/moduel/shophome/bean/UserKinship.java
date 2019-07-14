package com.park61.moduel.shophome.bean;

import java.io.Serializable;

/**
 * Created by HP on 2017/3/14.
 */
public class UserKinship implements Serializable {
   private int outHave;
    private String petName;
    private String pictureUrl;

    public int getOutHave() {
        return outHave;
    }

    public void setOutHave(int outHave) {
        this.outHave = outHave;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
