package com.park61.moduel.childtest.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shushucn2012 on 2017/2/27.
 */
public class QuestionCache {

    public static int eaItemId;
    public static long chosenChildId;
    public static List<TestQuestion> questionList = new ArrayList<TestQuestion>();
    public static int answerChildId;//未登录时，答题孩子
    public static String relationId;//未登录时，亲属关系id
    public static String batchCode;//未登录时，答题批次号

}
