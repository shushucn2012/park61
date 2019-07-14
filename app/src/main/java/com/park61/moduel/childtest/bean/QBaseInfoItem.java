package com.park61.moduel.childtest.bean;

import java.util.List;

/**
 * Created by shubei on 2017/12/7.
 */

public class QBaseInfoItem {


    /**
     * id : 152
     * imgUrl : null
     * content : 宝宝平常主要由谁带
     * batchCode : null
     * eaServiceId : null
     * eaItemSubId : null
     * eaItemSubName : null
     * eaItemId : null
     * eaItemName : null
     * eaSysId : null
     * userId : null
     * childId : null
     * cityId : null
     * isSingle : null
     * remarks : 是否隔代教养
     * answers : [{"id":265,"eaSubjectId":152,"content":"父母","score":0,"imgUrl":null,"createBy":null,"createDate":null,"updateBy":null,"updateDate":null,"choosed":false,"remarks":null,"delFlag":0},{"id":266,"eaSubjectId":152,"content":"(外)祖父母","score":1,"imgUrl":null,"createBy":null,"createDate":null,"updateBy":null,"updateDate":null,"choosed":false,"remarks":null,"delFlag":0},{"id":267,"eaSubjectId":152,"content":" 其他","score":0,"imgUrl":null,"createBy":null,"createDate":null,"updateBy":null,"updateDate":null,"choosed":false,"remarks":null,"delFlag":0}]
     * answerIds : null
     * hasChecked : null
     * hasFinished : null
     * listEaSubject : null
     */

    private int id;
    private String content;
    private String remarks;
    private int isSingle = 0;
    private List<AnswersBean> answers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsSingle() {
        return isSingle;
    }

    public void setIsSingle(int isSingle) {
        this.isSingle = isSingle;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<AnswersBean> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswersBean> answers) {
        this.answers = answers;
    }

    public static class AnswersBean {
        /**
         * id : 265
         * eaSubjectId : 152
         * content : 父母
         * score : 0
         * imgUrl : null
         * createBy : null
         * createDate : null
         * updateBy : null
         * updateDate : null
         * choosed : false
         * remarks : null
         * delFlag : 0
         */

        private int id;
        private int eaSubjectId;
        private String content;
        private int score;
        private boolean choosed;
        private int delFlag;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getEaSubjectId() {
            return eaSubjectId;
        }

        public void setEaSubjectId(int eaSubjectId) {
            this.eaSubjectId = eaSubjectId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public boolean isChoosed() {
            return choosed;
        }

        public void setChoosed(boolean choosed) {
            this.choosed = choosed;
        }

        public int getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(int delFlag) {
            this.delFlag = delFlag;
        }
    }
}
