package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/4.
 */
public class WeeklyReportDetailBean extends BaseObservable {
    @Override
    public String toString() {
        return "WeeklyReportDetailBean{" +
                "id=" + id +
                ", byWeek=" + byWeek +
                ", projectId=" + projectId +
                ", type=" + type +
                ", workType=" + workType +
                ", planId=" + planId +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", percentComplete=" + percentComplete +
                ", workTime=" + workTime +
                ", output='" + output + '\'' +
                ", explain='" + explain + '\'' +
                ", issue='" + issue + '\'' +
                ", state=" + state +
                ", approvalState=" + approvalState +
                ", userId=" + userId +
                ", projectName='" + projectName + '\'' +
                ", planName='" + planName + '\'' +
                '}';
    }

    /**
     * id : 2
     * byWeek : 28
     * projectId : 3
     * type : 1
     * workType : 1
     * planId : 1
     * startDate : 2019-07-01 00:00:00
     * endDate : 0001-01-01 00:00:00
     * percentComplete : 100
     * workTime : 5
     * output : GIT代码
     * explain : 完成周报系统的审核周报功能
     * issue : 阿达还真没有数据呢
     * state : 1
     * approvalState : 3
     * userId : 1
     * projectName : 周报系统
     * planName : 弗雷尔卓德的使命
     */

    private int id;
    private int byWeek;
    private int projectId;
    private int type;
    private int workType;
    private int planId;
    private String startDate;
    private String endDate;
    private int percentComplete;
    private int workTime;
    private String output;
    private String explain;
    private String issue;
    private int state;
    private int approvalState;
    private int userId;
    private String projectName;
    private String planName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getByWeek() {
        return byWeek;
    }

    public void setByWeek(int byWeek) {
        this.byWeek = byWeek;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
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

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getApprovalState() {
        return approvalState;
    }

    public void setApprovalState(int approvalState) {
        this.approvalState = approvalState;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }
}
