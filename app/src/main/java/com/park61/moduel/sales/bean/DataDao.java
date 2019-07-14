package com.park61.moduel.sales.bean;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by shushucn2012 on 2016/10/30.
 */
public class DataDao {
    private List<DataEntity> mDataEntities = new ArrayList<>();

    public void add(BrandBean data) {
        char data_first = data.getBrandNameSpell().toUpperCase().charAt(0);   //将首个字母转换为大写字母
        /*char data_first = data.getBrandCompanyName().toUpperCase().charAt(0);   //将首个字母转换为大写字母
        if (Pinyin.isChinese(data_first)) {   //如果首个字母是中文,则获取汉字首个字母的大写
            data_first = Pinyin.toPinyin(data_first).charAt(0);
        }*/
        //确保取得大写的唯一首字母,然后继续执行
        for (DataEntity dataEntity : mDataEntities) {
            if (dataEntity.isSameFirst(data_first)) {
                dataEntity.addData(data);
                return;
            }
        }
        DataEntity dataEntity = new DataEntity();
        dataEntity.setChar_First(data_first);
        dataEntity.addData(data);
        mDataEntities.add(dataEntity);
        Collections.sort(mDataEntities);
    }

    public List<DataEntity> getDataEntities() {
        System.out.println(mDataEntities.toString());
        return mDataEntities;
    }
}
