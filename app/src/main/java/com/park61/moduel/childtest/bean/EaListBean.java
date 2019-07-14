package com.park61.moduel.childtest.bean;

import java.util.List;

/**
 * Created by shubei on 2017/9/19.
 */

public class EaListBean {


    private List<SysListBean> sysList;

    public List<SysListBean> getSysList() {
        return sysList;
    }

    public void setSysList(List<SysListBean> sysList) {
        this.sysList = sysList;
    }

    public static class SysListBean {
        /**
         * id : 1
         * items : [{"canEa":false,"eaLowAgeLimit":3,"eaSysId":1,"eaSysName":"DAP测评(适合3-6岁)","eaUppAgeLimit":6,"finishNum":87,"id":1,"imgUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/test/20170909151521697_90.png","name":"学习潜能","subNum":55,"subTime":330},{"canEa":false,"eaLowAgeLimit":3,"eaSysId":1,"eaSysName":"DAP测评(适合3-6岁)","eaUppAgeLimit":6,"finishNum":31,"id":2,"imgUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/test/20170909151521697_90.png","name":"学习行为","subNum":30,"subTime":180},{"canEa":false,"eaLowAgeLimit":3,"eaSysId":1,"eaSysName":"DAP测评(适合3-6岁)","eaUppAgeLimit":6,"finishNum":10,"id":3,"imgUrl":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/test/20170909151521697_90.png","name":"学习动机","subNum":42,"subTime":252}]
         * taskList : [{"childId":132,"childName":"泡泡","eaSysId":1,"id":198,"inviteTime":1505360431000,"itemId":3094,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":132,"childName":"泡泡","eaSysId":1,"id":197,"inviteTime":1505360373000,"itemId":3079,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":132,"childName":"泡泡","eaSysId":1,"id":196,"inviteTime":1505360350000,"itemId":3064,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":3,"childName":"欣怡","eaSysId":1,"id":193,"inviteTime":1504950393000,"itemId":3052,"teachId":4,"teachName":"姓名"},{"childId":3,"childName":"欣怡","eaSysId":1,"id":192,"inviteTime":1504950000000,"itemId":3051,"teachId":4,"teachName":"姓名"},{"childId":3,"childName":"欣怡","eaSysId":1,"id":190,"inviteTime":1504948429000,"itemId":3050,"teachId":4,"teachName":"姓名"},{"childId":132,"childName":"泡泡","eaSysId":1,"id":187,"inviteTime":1504948056000,"itemId":3043,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":132,"childName":"泡泡","eaSysId":1,"id":184,"inviteTime":1504948054000,"itemId":3027,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":3,"childName":"欣怡","eaSysId":1,"id":176,"inviteTime":1504938764000,"itemId":2985,"teachId":4,"teachName":"姓名"},{"childId":132,"childName":"泡泡","eaSysId":1,"id":175,"inviteTime":1504928486000,"itemId":2979,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":3,"childName":"欣怡","eaSysId":1,"id":170,"inviteTime":1504922949000,"itemId":2969,"teachId":4,"teachName":"姓名"},{"childId":132,"childName":"泡泡","eaSysId":1,"id":167,"inviteTime":1504870823000,"itemId":2962,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":132,"childName":"泡泡","eaSysId":1,"id":166,"inviteTime":1504870814000,"itemId":2947,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":132,"childName":"泡泡","eaSysId":1,"id":165,"inviteTime":1504870805000,"itemId":2932,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":132,"childName":"泡泡","eaSysId":1,"id":164,"inviteTime":1504865760000,"itemId":2917,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":132,"childName":"泡泡","eaSysId":1,"id":161,"inviteTime":1504864170000,"itemId":2902,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":132,"childName":"泡泡","eaSysId":1,"id":155,"inviteTime":1504863692000,"itemId":2793,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":132,"childName":"泡泡","eaSysId":1,"id":151,"inviteTime":1504861784000,"itemId":2747,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":132,"childName":"泡泡","eaSysId":1,"id":148,"inviteTime":1504861748000,"itemId":2732,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":132,"childName":"泡泡","eaSysId":1,"id":141,"inviteTime":1504860178000,"itemId":2682,"teachId":200,"teachName":"哈哈哈哈啊"},{"childId":51,"childName":"七月","eaSysId":1,"id":130,"inviteTime":1504854258000,"itemId":2509,"teachId":181,"teachName":"补补老师"},{"childId":50,"childName":"童童","eaSysId":1,"id":130,"inviteTime":1504854258000,"itemId":2508,"teachId":181,"teachName":"补补老师"},{"childId":51,"childName":"七月","eaSysId":1,"id":127,"inviteTime":1504854240000,"itemId":2447,"teachId":181,"teachName":"补补老师"},{"childId":50,"childName":"童童","eaSysId":1,"id":127,"inviteTime":1504854240000,"itemId":2446,"teachId":181,"teachName":"补补老师"},{"childId":51,"childName":"七月","eaSysId":1,"id":123,"inviteTime":1504852906000,"itemId":2385,"teachId":181,"teachName":"补补老师"},{"childId":50,"childName":"童童","eaSysId":1,"id":123,"inviteTime":1504852906000,"itemId":2384,"teachId":181,"teachName":"补补老师"},{"childId":50,"childName":"童童","eaSysId":1,"id":121,"inviteTime":1504852902000,"itemId":2322,"teachId":181,"teachName":"补补老师"},{"childId":51,"childName":"七月","eaSysId":1,"id":121,"inviteTime":1504852902000,"itemId":2323,"teachId":181,"teachName":"补补老师"},{"childId":51,"childName":"七月","eaSysId":1,"id":118,"inviteTime":1504852074000,"itemId":2261,"teachId":181,"teachName":"补补老师"},{"childId":50,"childName":"童童","eaSysId":1,"id":118,"inviteTime":1504852074000,"itemId":2260,"teachId":181,"teachName":"补补老师"},{"childId":51,"childName":"七月","eaSysId":1,"id":115,"inviteTime":1504852065000,"itemId":2199,"teachId":181,"teachName":"补补老师"},{"childId":50,"childName":"童童","eaSysId":1,"id":115,"inviteTime":1504852065000,"itemId":2198,"teachId":181,"teachName":"补补老师"},{"childId":50,"childName":"童童","eaSysId":1,"id":113,"inviteTime":1504851797000,"itemId":2136,"teachId":181,"teachName":"补补老师"},{"childId":51,"childName":"七月","eaSysId":1,"id":113,"inviteTime":1504851797000,"itemId":2137,"teachId":181,"teachName":"补补老师"},{"childId":51,"childName":"七月","eaSysId":1,"id":110,"inviteTime":1504851578000,"itemId":2044,"teachId":181,"teachName":"补补老师"},{"childId":50,"childName":"童童","eaSysId":1,"id":110,"inviteTime":1504851578000,"itemId":2043,"teachId":181,"teachName":"补补老师"},{"childId":51,"childName":"七月","eaSysId":1,"id":107,"inviteTime":1504851565000,"itemId":1982,"teachId":181,"teachName":"补补老师"},{"childId":50,"childName":"童童","eaSysId":1,"id":107,"inviteTime":1504851565000,"itemId":1981,"teachId":181,"teachName":"补补老师"},{"childId":51,"childName":"七月","eaSysId":1,"id":104,"inviteTime":1504851563000,"itemId":1920,"teachId":181,"teachName":"补补老师"},{"childId":50,"childName":"童童","eaSysId":1,"id":104,"inviteTime":1504851563000,"itemId":1919,"teachId":181,"teachName":"补补老师"},{"childId":50,"childName":"童童","eaSysId":1,"id":101,"inviteTime":1504851560000,"itemId":1857,"teachId":181,"teachName":"补补老师"},{"childId":51,"childName":"七月","eaSysId":1,"id":101,"inviteTime":1504851560000,"itemId":1858,"teachId":181,"teachName":"补补老师"},{"childId":51,"childName":"七月","eaSysId":1,"id":99,"inviteTime":1504850631000,"itemId":1796,"teachId":181,"teachName":"补补老师"},{"childId":50,"childName":"童童","eaSysId":1,"id":99,"inviteTime":1504850631000,"itemId":1795,"teachId":181,"teachName":"补补老师"},{"childId":3,"childName":"欣怡","eaSysId":1,"id":28,"inviteTime":1504772208000,"itemId":96,"teachId":4,"teachName":"姓名"},{"childId":3,"childName":"欣怡","eaSysId":1,"id":24,"inviteTime":1504767661000,"itemId":92,"teachId":11,"teachName":"恐龙"},{"childId":3,"childName":"欣怡","eaSysId":1,"id":12,"inviteTime":1504324788000,"itemId":13,"teachId":181,"teachName":"补补老师"},{"childId":3,"childName":"欣怡","eaSysId":1,"id":13,"inviteTime":1504324788000,"itemId":18,"teachId":181,"teachName":"补补老师"}]
         * title : DAP测评(适合3-6岁)
         */

        private int id;
        private String title;
        private List<EaSysItemBean> items;
        private List<TaskListBean> taskList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<EaSysItemBean> getItems() {
            return items;
        }

        public void setItems(List<EaSysItemBean> items) {
            this.items = items;
        }

        public List<TaskListBean> getTaskList() {
            return taskList;
        }

        public void setTaskList(List<TaskListBean> taskList) {
            this.taskList = taskList;
        }


        public static class TaskListBean {
            /**
             * childId : 132
             * childName : 泡泡
             * eaSysId : 1
             * id : 198
             * inviteTime : 1505360431000
             * itemId : 3094
             * teachId : 200
             * teachName : 哈哈哈哈啊
             */

            private int childId;
            private String childName;
            private int eaSysId;
            private int id;
            private long inviteTime;
            private int itemId;
            private int teachId;
            private String teachName;

            public int getChildId() {
                return childId;
            }

            public void setChildId(int childId) {
                this.childId = childId;
            }

            public String getChildName() {
                return childName;
            }

            public void setChildName(String childName) {
                this.childName = childName;
            }

            public int getEaSysId() {
                return eaSysId;
            }

            public void setEaSysId(int eaSysId) {
                this.eaSysId = eaSysId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getInviteTime() {
                return inviteTime;
            }

            public void setInviteTime(long inviteTime) {
                this.inviteTime = inviteTime;
            }

            public int getItemId() {
                return itemId;
            }

            public void setItemId(int itemId) {
                this.itemId = itemId;
            }

            public int getTeachId() {
                return teachId;
            }

            public void setTeachId(int teachId) {
                this.teachId = teachId;
            }

            public String getTeachName() {
                return teachName;
            }

            public void setTeachName(String teachName) {
                this.teachName = teachName;
            }
        }
    }
}
