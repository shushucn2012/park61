package com.park61.moduel.sales.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shushucn2012 on 2016/10/30.
 */

public class DataEntity implements Comparable<DataEntity> {
    private char mChar_First;   //定义首字母
    private List<BrandBean> mDatas = new ArrayList<>();

    public boolean isSameFirst(char des) {//判断是否与传入的des相等
        return des == this.mChar_First;
    }

    public void addData(BrandBean data) {
        mDatas.add(data);
        //Collections.sort(mDatas);  //排列首字母相同的列表数据
        //System.out.println(mDatas.toString());
    }

    @Override
    public int compareTo(DataEntity another) {
        return this.mChar_First - another.getChar_First();
    }

    public List<BrandBean> getDatas() {  //存放同首字母的列表数据
        return mDatas;
    }

    public char getChar_First() {
        return mChar_First;
    }

    public void setChar_First(char char_First) {
        mChar_First = char_First;
    }

    @Override
    public String toString() {
        return "DataEntity{" +
                "mChar_First=" + mChar_First +
                ", mDatas=" + mDatas +
                '}';
    }
}
