package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/28.
 */
public class ProjectBaseInfo extends BaseObservable {
    @Override
    public String toString() {
        return "ProjectBaseInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", leaderId=" + leaderId +
                ", intor='" + intor + '\'' +
                ", state=" + state +
                ", code='" + code + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", leaderName='" + leaderName + '\'' +
                '}';
    }

    /**
     * id : 2
     * name : 无双剑姬
     * leaderId : 2
     * intor : 往事随风开始的浪费萨克
     * state : 3
     * code : 0002
     * startDate : 2019-07-02 09:41:51
     * endDate : 2019-07-07 09:41:53
     * leaderName : 涂立沅
     */

    private int id;
    private String name;
    private int leaderId;
    private String intor;
    private int state;
    private String code;
    private String startDate;
    private String endDate;
    private String leaderName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public String getIntor() {
        return intor;
    }

    public void setIntor(String intor) {
        this.intor = intor;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }
}
