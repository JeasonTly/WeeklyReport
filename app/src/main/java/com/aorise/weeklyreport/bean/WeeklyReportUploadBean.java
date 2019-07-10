package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/5.
 */
public class WeeklyReportUploadBean extends BaseObservable {

    @Override
    public String toString() {
        return "WeeklyReportUploadBean{" +
                "approvalState=" + approvalState +
                ", byWeek=" + byWeek +
                ", endDate='" + endDate + '\'' +
                ", explain='" + explain + '\'' +
                ", id=" + id +
                ", issue='" + issue + '\'' +
                ", output='" + output + '\'' +
                ", percentComplete=" + percentComplete +
                ", planId=" + planId +
                ", projectId=" + projectId +
                ", startDate='" + startDate + '\'' +
                ", state=" + state +
                ", type=" + type +
                ", userId=" + userId +
                ", workTime=" + workTime +
                ", workType=" + workType +
                '}';
    }

    /**
     * approvalState : 0
     * byWeek : 0
     * endDate : string
     * explain : string
     * id : 0
     * issue : string
     * output : string
     * percentComplete : 0
     * planId : 0
     * projectId : 0
     * startDate : string
     * state : 0
     * type : 0
     * userId : 0
     * workTime : 0
     * workType : 0
     */

    private int approvalState;
    private int byWeek;
    private String endDate;
    private String explain;
    private int id;
    private String issue;
    private String output;
    private int percentComplete;
    private int planId;
    private int projectId;
    private String startDate;
    private int state;
    private int type;
    private int userId;
    private int workTime;
    private int workType;

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

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }
}
