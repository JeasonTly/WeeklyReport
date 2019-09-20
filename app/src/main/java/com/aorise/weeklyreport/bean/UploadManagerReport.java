package com.aorise.weeklyreport.bean;

import java.io.Serializable;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/15.
 */
public class UploadManagerReport implements Serializable {
    @Override
    public String toString() {
        return "UploadManagerReport{" +
                "approvalState=" + approvalState +
                ", byWeek=" + byWeek +
                ", endDate='" + endDate + '\'' +
                ", id=" + id +
                ", overallSituation='" + overallSituation + '\'' +
                ", percentComplete=" + percentComplete +
                ", projectId=" + projectId +
                ", startDate='" + startDate + '\'' +
                ", state=" + state +
                ", type=" + type +
                ", weeklyType=" + weeklyType +
                '}';
    }

    /**
     * approvalState : 0
     * byWeek : 0
     * endDate : string
     * id : 0
     * overallSituation : string
     * percentComplete : 0
     * projectId : 0
     * startDate : string
     * state : 0
     * type : 0
     * weeklyType : 0
     */

    private int approvalState;
    private int byWeek;
    private String endDate;
    private int id;
    private String overallSituation;
    private double percentComplete;
    private int projectId;
    private String startDate;
    private int state;
    private int type;
    private int weeklyType;

    public int getApprovalState() {
        return approvalState;
    }

    public void setApprovalState(int approvalState) {
        this.approvalState = approvalState;
    }

    public int getByWeek() {
        return byWeek;
    }

    public void setByWeek(int byWeek) {
        this.byWeek = byWeek;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverallSituation() {
        return overallSituation;
    }

    public void setOverallSituation(String overallSituation) {
        this.overallSituation = overallSituation;
    }

    public double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWeeklyType() {
        return weeklyType;
    }

    public void setWeeklyType(int weeklyType) {
        this.weeklyType = weeklyType;
    }
}
