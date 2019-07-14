package com.park61.moduel.child.bean;

import java.io.Serializable;

/**
 * Created by shubei on 2017/5/26.
 */

public class VoteActivity implements Serializable {

    /**
     * description : 投选出你认为最萌的宝贝，前10名有神秘大奖
     * endTime : 1496218356000
     * id : 1
     * startTime : 1495613550000
     * status : 1
     * title : 卖萌大赛
     * voteNo : 6
     * voteToOneMax : 3
     * voteToPersonsMax : 3
     * winnerNum : 10
     */

    private String description;
    private long endTime;
    private int id;
    private long startTime;
    private int status;
    private String title;
    private int voteNo;
    private int voteToOneMax;
    private int voteToPersonsMax;
    private int winnerNum;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVoteNo() {
        return voteNo;
    }

    public void setVoteNo(int voteNo) {
        this.voteNo = voteNo;
    }

    public int getVoteToOneMax() {
        return voteToOneMax;
    }

    public void setVoteToOneMax(int voteToOneMax) {
        this.voteToOneMax = voteToOneMax;
    }

    public int getVoteToPersonsMax() {
        return voteToPersonsMax;
    }

    public void setVoteToPersonsMax(int voteToPersonsMax) {
        this.voteToPersonsMax = voteToPersonsMax;
    }

    public int getWinnerNum() {
        return winnerNum;
    }

    public void setWinnerNum(int winnerNum) {
        this.winnerNum = winnerNum;
    }

}
