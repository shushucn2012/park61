package com.park61.moduel.firsthead.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenlie on 2018/1/3.
 *
 * 推荐装备
 */

public class ToyBean implements Parcelable {

    //最低购买数量
    private int num;
    //原价
    private double marketPrice;
    //商品id
    private int pmInfoId;
    //相关商品idList
    private String pmInfoIdList;
    //现价
    private double currentUnifyPrice;
    //颜色
    private String productColor;
    //名称
    private String productCname;
    //图片
    private String productPicUrl;
    //规格
    private String productSize;
    //实际选择购买数量
    private int numSelect;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public int getPmInfoId() {
        return pmInfoId;
    }

    public void setPmInfoId(int pmInfoId) {
        this.pmInfoId = pmInfoId;
    }

    public String getPmInfoIdList() {
        return pmInfoIdList;
    }

    public void setPmInfoIdList(String pmInfoIdList) {
        this.pmInfoIdList = pmInfoIdList;
    }

    public double getCurrentUnifyPrice() {
        return currentUnifyPrice;
    }

    public void setCurrentUnifyPrice(double currentUnifyPrice) {
        this.currentUnifyPrice = currentUnifyPrice;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductCname() {
        return productCname;
    }

    public void setProductCname(String productCname) {
        this.productCname = productCname;
    }

    public String getProductPicUrl() {
        return productPicUrl;
    }

    public void setProductPicUrl(String productPicUrl) {
        this.productPicUrl = productPicUrl;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public int getNumSelect() {
        return numSelect;
    }

    public void setNumSelect(int numSelect) {
        this.numSelect = numSelect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.num);
        dest.writeDouble(this.marketPrice);
        dest.writeInt(this.pmInfoId);
        dest.writeString(this.pmInfoIdList);
        dest.writeDouble(this.currentUnifyPrice);
        dest.writeString(this.productColor);
        dest.writeString(this.productCname);
        dest.writeString(this.productPicUrl);
        dest.writeString(this.productSize);
        dest.writeInt(this.numSelect);
    }

    public ToyBean() {
    }

    protected ToyBean(Parcel in) {
        this.num = in.readInt();
        this.marketPrice = in.readDouble();
        this.pmInfoId = in.readInt();
        this.pmInfoIdList = in.readString();
        this.currentUnifyPrice = in.readDouble();
        this.productColor = in.readString();
        this.productCname = in.readString();
        this.productPicUrl = in.readString();
        this.productSize = in.readString();
        this.numSelect = in.readInt();
    }

    public static final Parcelable.Creator<ToyBean> CREATOR = new Parcelable.Creator<ToyBean>() {
        @Override
        public ToyBean createFromParcel(Parcel source) {
            return new ToyBean(source);
        }

        @Override
        public ToyBean[] newArray(int size) {
            return new ToyBean[size];
        }
    };
}
